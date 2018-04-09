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
package com.holonplatform.core.examples;

import java.util.Date;
import java.util.Optional;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Authorizer;
import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.InvalidCredentialsException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.http.HttpRequest;

@SuppressWarnings({ "unused", "serial" })
public class ExampleRealm {

	private static final Authenticator<AuthenticationToken> AUTHENTICATOR1 = null;
	private static final Authenticator<AuthenticationToken> AUTHENTICATOR2 = null;
	private static final Authorizer<Permission> AUTHORIZER1 = null;
	private static final Authorizer<Permission> AUTHORIZER2 = null;

	public void name() {
		// tag::name[]
		Realm realm = Realm.builder().name("nyname").build(); // <1>

		Optional<String> name = realm.getName(); // <2>
		// end::name[]
	}

	public void builder() {
		// tag::builder[]
		Realm realm = Realm.builder() // <1>
				.authenticator(AUTHENTICATOR1) // <2>
				.authenticator(AUTHENTICATOR2) // <3>
				.authorizer(AUTHORIZER1) // <4>
				.authorizer(AUTHORIZER2) // <5>
				.build();
		// end::builder[]
	}

	public void builder2() {
		// tag::builder2[]
		Realm realm = Realm.builder().authenticator(AUTHENTICATOR1).build(); // <1>

		realm.addAuthenticator(AUTHENTICATOR2); // <2>
		// end::builder2[]
	}

	public void token1() {
		// tag::accounttoken[]
		AuthenticationToken token = AuthenticationToken.accountCredentials("username", "password"); // <1>
		// end::accounttoken[]
	}

	public void token2() {
		// tag::bearertoken[]
		AuthenticationToken token = AuthenticationToken.bearer("Agr564FYda78dsff8Trf7"); // <1>
		// end::bearertoken[]
	}

	// tag::authenticator[]
	class MyAuthenticationToken implements AuthenticationToken { // <1>

		private final String principalName;

		public MyAuthenticationToken(String principalName) {
			super();
			this.principalName = principalName;
		}

		@Override
		public Object getPrincipal() {
			return principalName;
		}

		@Override
		public Object getCredentials() {
			return null;
		}

	}

	class MyAuthenticator implements Authenticator<MyAuthenticationToken> { // <2>

		@Override
		public Class<? extends MyAuthenticationToken> getTokenType() {
			return MyAuthenticationToken.class; // <3>
		}

		@Override
		public Authentication authenticate(MyAuthenticationToken authenticationToken) throws AuthenticationException {
			if (!"test".equals(authenticationToken.getPrincipal())) { // <4>
				throw new UnknownAccountException();
			}
			return Authentication.builder(authenticationToken.principalName).build();
		}

	}

	public void authenticate() {
		Realm realm = Realm.builder().authenticator(new MyAuthenticator()).build(); // <5>

		try {
			Authentication authc = realm.authenticate(new MyAuthenticationToken("test")); // <6>
		} catch (AuthenticationException e) {
			// handle failed authentication
		}
	}
	// end::authenticator[]

	public void authenticator1() {
		// tag::authenticator1[]
		Realm realm = getRealm();

		try {
			Authentication authc = realm.authenticate(new MyAuthenticationToken("test")); // <1>
		} catch (AuthenticationException e) {
			// handle failed authentication
		}
		// end::authenticator1[]
	}

	public void authenticator2() {
		// tag::authenticator2[]
		Realm realm = getRealm();

		boolean supported = realm.supportsToken(MyAuthenticationToken.class); // <1>
		// end::authenticator2[]
	}

	public void authenticator3() {
		// tag::authenticator3[]
		Authenticator<MyAuthenticationToken> authenticator = Authenticator.create(MyAuthenticationToken.class, // <1>
				token -> {
					// check authentication token information
					token.getPrincipal();
					token.getCredentials();
					boolean valid = true; // ...
					// if not valid, throw an exception
					if (!valid) {
						throw new InvalidCredentialsException();
					}
					// otherwise, return the authenticated principal representation
					return Authentication.builder("thePrincipalName").build();
				});

		try {
			Authentication authc = authenticator.authenticate(new MyAuthenticationToken("test")); // <2>
		} catch (AuthenticationException e) {
			// <3>
		}
		// end::authenticator3[]
	}

	public void authenticationListener() {
		// tag::listener[]
		Realm realm = getRealm();

		realm.addAuthenticationListener(authentication -> { // <1>
			// do something ...
			authentication.getName();
		});
		// end::listener[]
	}

	private static class MyPermission implements Permission {

		@Override
		public Optional<String> getPermission() {
			return Optional.empty();
		}

	}

	public void authentication() {
		// tag::authentication[]
		Authentication authc = Authentication.builder("userId") // <1>
				.permission("VIEW") // <2>
				.permission(new MyPermission()) // <3>
				.parameter("name", "John") // <4>
				.parameter("surname", "Doe") // <5>
				.scheme("myscheme") // <6>
				.build();
		// end::authentication[]
	}

	public void messageResolvers() {
		// tag::basic[]
		AuthenticationTokenResolver<HttpRequest> basicResolver = AuthenticationToken.httpBasicResolver();
		// end::basic[]
		// tag::bearer[]
		AuthenticationTokenResolver<HttpRequest> bearerResolver = AuthenticationToken.httpBearerResolver();
		// end::bearer[]
	}

	public void resolver() {
		// tag::resolver[]
		Realm realm = Realm.builder().resolver(AuthenticationToken.httpBasicResolver())
				.authenticator(new MyAuthenticator()).build(); // <1>

		HttpRequest request = null; // obtain the HttpRequest message in real world code
		try {
			Authentication authc = realm.authenticate(request); // <2>
		} catch (AuthenticationException e) {
			// handle failed authentication
		}
		// end::resolver[]
	}

	public void credentials() {
		// tag::credentials[]
		Credentials credentials = Credentials.builder().secret("test").build(); // <1>

		credentials = Credentials.builder().secret("test").hashAlgorithm(Credentials.Encoder.HASH_MD5).build(); // <2>

		credentials = Credentials.builder().secret("test").hashAlgorithm(Credentials.Encoder.HASH_MD5).hashIterations(7)
				.salt(new byte[] { 1, 2, 3 }).build(); // <3>

		credentials = Credentials.builder().secret("test").hashAlgorithm(Credentials.Encoder.HASH_MD5).base64Encoded()
				.build(); // <4>

		credentials = Credentials.builder().secret("test").expireDate(new Date()).build(); // <5>
		// end::credentials[]
	}

	public void encoder() {
		// tag::encoder[]
		String encoded = Credentials.encoder().secret("test").buildAndEncodeBase64(); // <1>

		byte[] bytes = Credentials.encoder().secret("test").hashSHA256().build(); // <2>

		encoded = Credentials.encoder().secret("test").hashSHA256().salt(new byte[] { 1, 2, 3 }).buildAndEncodeBase64(); // <3>

		encoded = Credentials.encoder().secret("test").hashSHA512().charset("UTF-8").buildAndEncodeBase64(); // <4>
		// end::encoder[]
	}

	public void permissions() {
		// tag::permissions[]
		final Permission p1 = Permission.create("role1");
		final Permission p2 = Permission.create("role2");

		// build an Authentication and grant the two permissions to it
		Authentication authc = Authentication.builder("test").permission(p1).permission(p2).build();

		// Realm with default authorizer
		Realm realm = Realm.builder().withDefaultAuthorizer().build();

		// permission checking
		boolean permitted = realm.isPermitted(authc, p1);
		permitted = realm.isPermitted(authc, p1, p2);
		permitted = realm.isPermitted(authc, "p1");
		permitted = realm.isPermittedAny(authc, p1, p2);
		permitted = realm.isPermittedAny(authc, "p1", "p2");
		// end::permissions[]
	}

	private static Realm getRealm() {
		return null;
	}

}
