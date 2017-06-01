/*
 * Copyright 2000-2016 Holon TDCN.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.holonplatform.auth.jwt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

import javax.crypto.SecretKey;

import org.junit.Test;

import com.holonplatform.auth.AuthContext;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.ExpiredCredentialsException;
import com.holonplatform.auth.exceptions.InvalidTokenException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.jwt.AuthenticationClaims;
import com.holonplatform.auth.jwt.JwtAuthenticator;
import com.holonplatform.auth.jwt.JwtConfiguration;
import com.holonplatform.auth.jwt.JwtTokenBuilder;
import com.holonplatform.auth.jwt.JwtTokenBuilder.AuthPart;
import com.holonplatform.auth.jwt.internal.AuthenticationClaimsImpl;
import com.holonplatform.core.internal.utils.TestUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class TestJwt {

	@Test
	public void testBase() {
		TestUtils.checkUtilityClass(JwtTokenBuilder.class);
		TestUtils.checkEnum(AuthPart.class);
	}

	@Test
	public void testConfig() {

		final JwtAuthenticator jwtAuthenticator = JwtAuthenticator.builder()
				.configuration(JwtConfiguration.builder().build()).issuer("TestUnit").build();

		final Realm realm = Realm.builder().authenticator(jwtAuthenticator).build();

		final Authentication authc = Authentication.builder("testuser").build();

		String jwt = JwtTokenBuilder.buildJWT(authc, null, "TestUnit", null);

		Authentication authenticated = realm.authenticate(AuthenticationToken.bearer(jwt));
		assertNotNull(authenticated);

		TestUtils.expectedException(InvalidTokenException.class, new Callable<Authentication>() {

			@Override
			public Authentication call() throws Exception {
				String jwt = JwtTokenBuilder.buildJWT(authc, null, "InvalidIssuer", null);
				return realm.authenticate(AuthenticationToken.bearer(jwt));
			}
		});

		final JwtAuthenticator jwtAuthenticator2 = JwtAuthenticator.builder()
				.configuration(JwtConfiguration.builder().build()).issuer("TestUnit").requiredClaim("testReq").build();

		TestUtils.expectedException(InvalidTokenException.class, new Callable<Authentication>() {

			@Override
			public Authentication call() throws Exception {
				String jwt = JwtTokenBuilder.buildJWT(authc, null, "TestUnit", null);
				return Realm.builder().authenticator(jwtAuthenticator2).build()
						.authenticate(AuthenticationToken.bearer(jwt));
			}
		});

		final Authentication authc2 = Authentication.builder("testuser").parameter("testReq", "VAL").build();
		jwt = JwtTokenBuilder.buildJWT(authc2, null, "TestUnit", null, AuthPart.DETAILS);

		authenticated = realm.authenticate(AuthenticationToken.bearer(jwt));
		assertNotNull(authenticated);

		TestUtils.expectedException(UnexpectedAuthenticationException.class, new Callable<Authentication>() {

			@Override
			public Authentication call() throws Exception {
				JwtAuthenticator.builder().configuration(JwtConfiguration.builder().build()).build().authenticate(null);
				return null;
			}
		});

		AuthenticationClaims cs = new AuthenticationClaimsImpl(null);
		assertNull(cs.get("test", String.class));

		TestUtils.expectedException(ExpiredCredentialsException.class, new Callable<Authentication>() {

			@Override
			public Authentication call() throws Exception {
				String jwt = JwtTokenBuilder.buildJWT(authc2, null, "TestUnit", 1L, AuthPart.DETAILS);
				Thread.sleep(2L);
				realm.authenticate(AuthenticationToken.bearer(jwt));
				return null;
			}
		});

		TestUtils.expectedException(InvalidTokenException.class, new Callable<Authentication>() {

			@Override
			public Authentication call() throws Exception {
				realm.authenticate(AuthenticationToken.bearer("x"));
				return null;
			}
		});

	}

	@Test
	public void testJWTAuthentication_unsigned() throws Exception {

		final Realm realm = Realm.builder()
				.authenticator(JwtAuthenticator.builder().configuration(JwtConfiguration.builder().build()).build())
				.build();

		Authentication authc = Authentication.builder("testuser").build();

		String jwt = JwtTokenBuilder.buildJWT(authc, null, "TestUnit", null);
		assertNotNull(jwt);

		Authentication authenticated = realm.authenticate(AuthenticationToken.bearer(jwt));
		assertNotNull(authenticated);
		assertEquals(authc, authenticated);
		assertEquals("TestUnit", authenticated.getParameter(Claims.ISSUER, String.class).get());

		// with id
		String id = UUID.randomUUID().toString();

		jwt = JwtTokenBuilder.buildJWT(authc, id, "TestUnit", null);
		assertNotNull(jwt);

		authenticated = realm.authenticate(AuthenticationToken.bearer(jwt));
		assertNotNull(authenticated);
		assertEquals(authc, authenticated);
		assertEquals("TestUnit", authenticated.getParameter(Claims.ISSUER, String.class).get());
		assertEquals(id, authenticated.getParameter(Claims.ID, String.class).get());

		// with expiration (10s)

		jwt = JwtTokenBuilder.buildJWT(authc, id, "TestUnit", 10000L);
		assertNotNull(jwt);

		authenticated = realm.authenticate(AuthenticationToken.bearer(jwt));
		assertNotNull(authenticated);
		assertEquals(authc, authenticated);
		assertEquals("TestUnit", authenticated.getParameter(Claims.ISSUER, String.class).get());
		assertEquals(id, authenticated.getParameter(Claims.ID, String.class).get());
		assertNotNull(authenticated.getParameter(Claims.EXPIRATION, Integer.class).get());

		// with permissions

		Permission p1 = Permission.create("p1");
		Permission p2 = Permission.create("p2");
		Permission p3 = Permission.create("p3");

		Set<Permission> permissions = new HashSet<>(3);
		permissions.add(p1);
		permissions.add(p2);
		permissions.add(p3);

		authc = Authentication.builder("testuser").permission(p1).permission(p2).permission(p3).build();

		jwt = JwtTokenBuilder.buildJWT(authc, id, "TestUnit", 10000L, AuthPart.PERMISSIONS);
		assertNotNull(jwt);

		authenticated = realm.authenticate(AuthenticationToken.bearer(jwt));
		assertNotNull(authenticated);
		assertEquals(authc, authenticated);
		assertEquals("TestUnit", authenticated.getParameter(Claims.ISSUER, String.class).get());
		assertEquals(id, authenticated.getParameter(Claims.ID, String.class).get());
		assertNotNull(authenticated.getParameter(Claims.EXPIRATION, Integer.class).get());
		assertFalse(authenticated.getParameter(AuthenticationClaims.CLAIM_NAME_ROOT, boolean.class).get());

		// with details

		authc = Authentication.builder("testuser").parameter("testd", 1).permission(p1).permission(p2).permission(p3)
				.build();

		jwt = JwtTokenBuilder.buildJWT(authc, id, "TestUnit", 10000L, AuthPart.PERMISSIONS, AuthPart.DETAILS);
		assertNotNull(jwt);

		authenticated = realm.authenticate(AuthenticationToken.bearer(jwt));
		assertNotNull(authenticated);
		assertEquals(authc, authenticated);
		assertEquals("TestUnit", authenticated.getParameter(Claims.ISSUER, String.class).get());
		assertEquals(id, authenticated.getParameter(Claims.ID, String.class).get());
		assertNotNull(authenticated.getParameter(Claims.EXPIRATION, Integer.class).get());
		assertFalse(authenticated.getParameter(AuthenticationClaims.CLAIM_NAME_ROOT, boolean.class).get());

		assertEquals(new Integer(1), authenticated.getParameter("testd", Integer.class).get());

	}

	@Test
	public void testJWTAuthentication_signed_symmetric() throws Exception {

		SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS256);

		final JwtConfiguration cfg = JwtConfiguration.builder().signatureAlgorithm(SignatureAlgorithm.HS256.getValue())
				.sharedKey(key.getEncoded()).build();

		final Realm realm = Realm.builder().authenticator(JwtAuthenticator.builder().configuration(cfg).build())
				.build();

		final Authentication authc = Authentication.builder("testuser").root(true).build();

		String jwt = JwtTokenBuilder.buildJWT(authc, "TestId", "TestUnit", 20000L, SignatureAlgorithm.HS256,
				key.getEncoded());
		assertNotNull(jwt);

		Authentication authenticated = realm.authenticate(AuthenticationToken.bearer(jwt));
		assertNotNull(authenticated);
		assertEquals(authc, authenticated);
		assertEquals("TestUnit", authenticated.getParameter(Claims.ISSUER, String.class).get());
		assertEquals("TestId", authenticated.getParameter(Claims.ID, String.class).get());

		jwt = JwtTokenBuilder.buildJWT(authc, "TestId", "TestUnit", 20000L, SignatureAlgorithm.HS256, key.getEncoded(),
				AuthPart.PERMISSIONS);
		assertNotNull(jwt);

		authenticated = realm.authenticate(AuthenticationToken.bearer(jwt));
		assertNotNull(authenticated);
		assertEquals(authc, authenticated);
		assertEquals("TestUnit", authenticated.getParameter(Claims.ISSUER, String.class).get());
		assertEquals("TestId", authenticated.getParameter(Claims.ID, String.class).get());
		assertTrue(authenticated.getParameter(AuthenticationClaims.CLAIM_NAME_ROOT, boolean.class).get());

		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				JwtTokenBuilder.buildJWT(null, "TestId", "TestUnit", 20000L, SignatureAlgorithm.HS256,
						key.getEncoded());
			}
		});

		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				JwtTokenBuilder.buildJWT(authc, "TestId", "TestUnit", 20000L, null, key.getEncoded());
			}
		});

		jwt = JwtTokenBuilder.buildJWT(authc, "TestId", "TestUnit", 20000L, null, (String) null);
		assertNotNull(jwt);

	}

	@Test
	public void testJWTAuthentication_signed_asymmetric() throws Exception {

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(512);
		KeyPair keyPair = keyGen.genKeyPair();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		final JwtConfiguration cfg = JwtConfiguration.builder().signatureAlgorithm(SignatureAlgorithm.RS256.getValue())
				.privateKey(privateKey).publicKey(publicKey).build();

		final Realm realm = Realm.builder().authenticator(JwtAuthenticator.builder().configuration(cfg).build())
				.build();

		Authentication authc = Authentication.builder("testuser").root(true).build();

		String jwt = JwtTokenBuilder.buildJWT(authc, "TestId", "TestUnit", 20000L, SignatureAlgorithm.RS256,
				privateKey);
		assertNotNull(jwt);

		Authentication authenticated = realm.authenticate(AuthenticationToken.bearer(jwt));
		assertNotNull(authenticated);
		assertEquals(authc, authenticated);
		assertEquals("TestUnit", authenticated.getParameter(Claims.ISSUER, String.class).get());
		assertEquals("TestId", authenticated.getParameter(Claims.ID, String.class).get());

	}

	@Test
	public void testJWTAuthentication_permissions() throws Exception {

		byte[] sharedKey = generateKey();

		final JwtConfiguration cfg = JwtConfiguration.builder().signatureAlgorithm(SignatureAlgorithm.HS256.getValue())
				.sharedKey(sharedKey).build();

		final Realm realm = Realm.builder().authenticator(JwtAuthenticator.builder().configuration(cfg).build())
				.withDefaultAuthorizer().build();

		Authentication authc = Authentication.builder("testuser").root(true).parameter("language", "it")
				.permission(Permission.create("RoleX")).build();

		String jwt = JwtTokenBuilder.buildJWT(authc, "TestId", "TestUnit", 2000000L, SignatureAlgorithm.HS256,
				sharedKey, AuthPart.DETAILS, AuthPart.PERMISSIONS);
		assertNotNull(jwt);

		AuthContext ctx = AuthContext.create(realm);

		ctx.authenticate(AuthenticationToken.bearer(jwt));
		assertTrue(ctx.getAuthentication().isPresent());
		assertEquals(authc, ctx.getAuthentication().get());
		assertEquals("it", ctx.getAuthentication().get().getParameter("language", String.class, null));
		assertTrue(ctx.isPermitted("RoleX"));
		assertFalse(ctx.isPermitted("xxx"));

	}

	private static byte[] generateKey() throws Exception {
		String random = UUID.randomUUID().toString();
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.reset();
		algorithm.update(random.getBytes());
		byte[] messageDigest = algorithm.digest();
		return Base64.getEncoder().encode(messageDigest);
	}

}
