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
package com.holonplatform.core.examples;

import java.util.Date;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Authorizer;
import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.http.HttpRequest;

@SuppressWarnings({ "unused", "serial" })
public class ExampleRealm {

	public void token1() {
		// tag::accounttoken[]
		AuthenticationToken token = AuthenticationToken.accountCredentials("username", "password");
		// end::accounttoken[]
	}

	public void token2() {
		// tag::bearertoken[]
		AuthenticationToken token = AuthenticationToken.bearer("Agr564FYda78dsff8Trf7");
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

	public void authentication() {
		// tag::authentication[]
		Authentication authc = Authentication.builder("userId").permission("VIEW").permission("MANAGE")
				.parameter("name", "John").parameter("surname", "Doe").build();
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

}
