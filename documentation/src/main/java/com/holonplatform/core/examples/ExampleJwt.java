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

import java.io.IOException;
import java.util.UUID;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.jwt.JwtAuthenticator;
import com.holonplatform.auth.jwt.JwtConfigProperties;
import com.holonplatform.auth.jwt.JwtConfiguration;
import com.holonplatform.auth.jwt.JwtSignatureAlgorithm;
import com.holonplatform.auth.jwt.JwtTokenBuilder;
import com.holonplatform.http.HttpRequest;

@SuppressWarnings("unused")
public class ExampleJwt {

	public void config() {
		// tag::config[]
		JwtConfiguration cfg = JwtConfiguration.builder() // <1>
				.issuer("MyIssuer") // <2>
				.expireTime(10000) // <3>
				.includeDetails(true) // <4>
				.includePermissions(true) // <5>
				.signatureAlgorithm(JwtSignatureAlgorithm.HS256) // <6>
				.sharedKey(new byte[] { 1, 2, 3 }) // <7>
				.build();
		// end::config[]
	}

	public void config2() throws IOException {
		// tag::config2[]
		JwtConfiguration cfg = JwtConfiguration
				.build(JwtConfigProperties.builder().withPropertySource("jwt.properties").build()); // <1>
		// end::config2[]
	}

	public void build1() throws IOException {
		// tag::build1[]
		JwtConfiguration configuration = JwtConfiguration
				.build(JwtConfigProperties.builder().withPropertySource("jwt.properties").build()); // <1>

		Authentication authc = Authentication.builder("test").build(); // <2>

		String jwt = JwtTokenBuilder.get().buildJwt(configuration, authc); // <3>
		jwt = JwtTokenBuilder.get().buildJwt(configuration, authc, UUID.randomUUID().toString()); // <4>
		// end::build1[]
	}

	public void build2() throws IOException {
		// tag::build2[]
		JwtConfiguration configuration = JwtConfiguration.builder().includePermissions(true).build(); // <1>

		Authentication authc = Authentication.builder("test").permission("role1").permission("role2").build(); // <2>

		String jwt = JwtTokenBuilder.get().buildJwt(configuration, authc); // <3>
		// end::build2[]
	}

	public void build3() throws IOException {
		// tag::build3[]
		JwtConfiguration configuration = JwtConfiguration.builder().includeDetails(true).build(); // <1>

		Authentication authc = Authentication.builder("test").parameter("name", "John").build(); // <2>

		String jwt = JwtTokenBuilder.get().buildJwt(configuration, authc); // <3>
		// end::build3[]
	}

	public void authenticator() throws IOException {
		// tag::authenticator[]
		JwtConfiguration configuration = JwtConfiguration.builder() // <1>
				.issuer("MyIssuer") // JWT token issuer
				.expireTime(10000) // expire time in milliseconds
				.includeDetails(true) // include the Authentication details in JWT token generation
				.includePermissions(true) // include the Authentication permissions in JWT token generation
				.signatureAlgorithm(JwtSignatureAlgorithm.HS256) // use HS256 as signature algorithm
				.sharedKey(new byte[] { 1, 2, 3 }) // shared key to use with the symmetric signing algorithm
				.build();

		// JWT authenticator
		JwtAuthenticator jwtAuthenticator = JwtAuthenticator.builder().configuration(configuration) // <2>
				.issuer("allowedIssuer") // <3>
				.requiredClaim("myClaim") // <4>
				.build();

		// Realm
		Realm realm = Realm.builder().authenticator(jwtAuthenticator) // <5>
				.withDefaultAuthorizer().build();

		Authentication authc = realm.authenticate(AuthenticationToken.bearer("TheJWTtokenHere...")); // <6>

		realm = Realm.builder().authenticator(jwtAuthenticator) //
				.resolver(AuthenticationToken.httpBearerResolver()) // <7>
				.withDefaultAuthorizer().build();

		HttpRequest request = obtainHttpRequest();

		authc = realm.authenticate(request); // <8>
		// end::authenticator[]
	}

	private static HttpRequest obtainHttpRequest() {
		return null;
	}

}
