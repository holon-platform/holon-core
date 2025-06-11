/*
 * Copyright 2016-2017 Axioma srl.
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
package com.holonplatform.auth.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.holonplatform.auth.Account;
import com.holonplatform.auth.Account.AccountProvider;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.CredentialsContainer;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.exceptions.DisabledAccountException;
import com.holonplatform.auth.exceptions.ExpiredCredentialsException;
import com.holonplatform.auth.exceptions.InvalidCredentialsException;
import com.holonplatform.auth.exceptions.LockedAccountException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.auth.internal.AccountAuthenticator;
import com.holonplatform.auth.internal.DefaultAccount;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.core.internal.utils.Hash;

public class TestAccount {

	final AtomicInteger cacheCounter = new AtomicInteger(0);

	@Test
	public void testAccount() {

		Map<String, Object> details = new HashMap<>();
		details.put("testS", "string");

		Account act = Account.builder("test").enabled(true).expired(false).locked(false).details(details)
				.withDetail("testI", Integer.valueOf(1)).withPermission("p1").withPermission("p2")
				.credentials(Credentials.builder().secret("secret").hashAlgorithm("SHA-256").hashIterations(1).build())
				.build();

		Permission p1 = Permission.create("p1");
		Permission p2 = Permission.create("p2");
		Set<Permission> permissions = new HashSet<>(3);
		permissions.add(p1);
		permissions.add(p2);

		assertFalse(act.isRoot());
		assertTrue(act.isEnabled());
		assertFalse(act.isExpired());
		assertFalse(act.isLocked());
		assertNotNull(act.getDetails());
		assertEquals("string", act.getDetails().get("testS"));
		assertEquals(Integer.valueOf(1), act.getDetails().get("testI"));
		assertNotNull(act.getPermissions());
		assertTrue(act.getPermissions().contains(p1));
		assertNotNull(act.getCredentials());
		Credentials crd = (Credentials) act.getCredentials();
		assertEquals("secret", new String(crd.getSecret()));
		assertEquals("SHA-256", crd.getHashAlgorithm());
		assertNull(crd.getSalt());
		assertEquals(1, crd.getHashIterations());
		assertFalse(crd.isBase64Encoded());
		assertFalse(crd.isHexEncoded());
		assertNull(crd.getExpireDate());

		Object creds = Credentials.builder().secret("secret").hashAlgorithm("SHA-256").hashIterations(1).build();

		Account actx = Account.builder("test").root(true).credentials(creds).permissions(Collections.singleton(p1))
				.withPermission(p2).build();

		assertTrue(actx.isRoot());
		assertTrue(actx.isEnabled());
		assertFalse(actx.isExpired());
		assertFalse(actx.isLocked());
		assertNotNull(actx.getPermissions());
		assertEquals(2, actx.getPermissions().size());
		assertTrue(actx.getPermissions().contains(p1));
		assertTrue(actx.getPermissions().contains(p2));
		assertNotNull(actx.getCredentials());
		Credentials crdx = (Credentials) act.getCredentials();
		assertEquals("secret", new String(crdx.getSecret()));
		assertEquals("SHA-256", crdx.getHashAlgorithm());
		assertNull(crdx.getSalt());
		assertEquals(1, crdx.getHashIterations());

		Account act2 = Account.builder("test").withDetail("test", "val").build();
		assertNotNull(act2.getDetails());

		assertNotNull(act2.toString());

		assertEquals(act, act);
		assertEquals(act, act2);
		assertNotEquals(act, Account.builder("testx").build());

		assertNotEquals(act, null);
		assertNotEquals(act, "x");

		assertTrue(act.hashCode() == act2.hashCode());

		act = Account.builder("test").enabled(true).expired(false).locked(false).permissionStrings("p1", "p2").build();

		assertNotNull(act.getPermissions());
		assertTrue(act.getPermissions().contains(p1));
		assertTrue(act.getPermissions().contains(p2));

		act = Account.builder("test").permissionStrings((String[]) null).build();

		assertNotNull(act.getPermissions());
		assertEquals(0, act.getPermissions().size());

		DefaultAccount ia = new DefaultAccount("testd");
		ia.setRoot(true);
		ia.setPermissions(Collections.singleton(Permission.create("px")));

		assertTrue(ia.isRoot());
		assertNotNull(ia.getPermissions());
		assertEquals(1, ia.getPermissions().size());

	}

	@Test
	public void testAuthc() {

		byte[] salt = Hash.generateSalt();

		String secret = Credentials.encoder().secret("testpwd").hashSHA256().salt(salt).hashIterations(3)
				.buildAndEncodeBase64();

		final Account act = Account.builder("test").enabled(true).expired(false).locked(false)
				.credentials(Credentials.builder().secret(secret).hashAlgorithm("SHA-256")
						.salt(Base64.getEncoder().encode(salt)).hashIterations(3).base64Encoded().build())
				.build();

		final AccountProvider service = new AccountProvider() {

			@Override
			public Optional<Account> loadAccountById(String id) {
				if ("test".equals(id)) {
					return Optional.of(act);
				}
				return Optional.empty();
			}
		};

		AccountAuthenticator resolver = new AccountAuthenticator(service);
		assertNotNull(resolver.getCredentialsMatcher());
		assertEquals(AccountCredentialsToken.class, resolver.getTokenType());

		assertNotNull(resolver.getAccountService());

		resolver = new AccountAuthenticator(service, CredentialsContainer.defaultMatcher());
		assertNotNull(resolver.getCredentialsMatcher());

		resolver = new AccountAuthenticator(service);

		Authentication authc = resolver.authenticate(new AccountCredentialsToken("test", "testpwd"));
		assertNotNull(authc);

		assertThrows(UnexpectedAuthenticationException.class, () -> {
			new AccountAuthenticator(service).authenticate(null);
		});

		assertThrows(UnknownAccountException.class, () -> {
			new AccountAuthenticator(service).authenticate(new AccountCredentialsToken("testx", "testpwd"));
		});

		assertThrows(InvalidCredentialsException.class, () -> {
			new AccountAuthenticator(service).authenticate(new AccountCredentialsToken(null, "testpwd"));
		});

		final AccountProvider service2 = new AccountProvider() {

			@Override
			public Optional<Account> loadAccountById(String id) {
				if ("enb".equals(id)) {
					return Optional.of(Account.builder("test").enabled(false).build());
				}
				if ("lck".equals(id)) {
					return Optional.of(Account.builder("test").locked(true).build());
				}
				if ("exp".equals(id)) {
					return Optional.of(Account.builder("test").expired(true).build());
				}
				return Optional.empty();
			}
		};

		assertThrows(DisabledAccountException.class, () -> {
			new AccountAuthenticator(service2).authenticate(new AccountCredentialsToken("enb", "testpwd"));
		});
		assertThrows(LockedAccountException.class, () -> {
			new AccountAuthenticator(service2).authenticate(new AccountCredentialsToken("lck", "testpwd"));
		});
		assertThrows(ExpiredCredentialsException.class, () -> {
			new AccountAuthenticator(service2).authenticate(new AccountCredentialsToken("exp", "testpwd"));
		});

		final AccountProvider service4 = new AccountProvider() {

			@Override
			public Optional<Account> loadAccountById(String id) {
				throw new RuntimeException("test");
			}
		};
		assertThrows(UnexpectedAuthenticationException.class, () -> {
			new AccountAuthenticator(service4).authenticate(new AccountCredentialsToken("test", "testpwd"));
		});

		Authenticator<AccountCredentialsToken> aa = Account.authenticator(service);

		authc = aa.authenticate(new AccountCredentialsToken("test", "testpwd"));
		assertNotNull(authc);

		aa = Account.authenticator(service, CredentialsContainer.defaultMatcher());

		authc = aa.authenticate(new AccountCredentialsToken("test", "testpwd"));
		assertNotNull(authc);

	}

}
