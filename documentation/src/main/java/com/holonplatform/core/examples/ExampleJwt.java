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
import com.holonplatform.auth.exceptions.AuthenticationException;
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
		JwtConfiguration cfg = JwtConfiguration.builder().issuer("MyIssuer") // set the JWT token issuer
				.expireTime(10000) // token expire time in milliseconds
				.includeDetails(true) // include the Authentication details in JWT token generation
				.includePermissions(true) // include the Authentication permissions in JWT token generation
				.signatureAlgorithm(JwtSignatureAlgorithm.HS256) // use HS256 as signature algorithm
				.sharedKey(new byte[] { 1, 2, 3 }) // shared key to use with the symmetric signing algorithm
				.build();
		// end::config[]
	}

	public void config2() throws IOException {
		// tag::config2[]
		JwtConfiguration cfg = JwtConfiguration
				.build(JwtConfigProperties.builder().withPropertySource("jwt.properties").build());
		// end::config2[]
	}

	public void build() throws IOException {
		// tag::build[]
		JwtConfiguration configuration = JwtConfiguration
				.build(JwtConfigProperties.builder().withPropertySource("jwt.properties").build()); // <1>

		Authentication authc = Authentication.builder("test").permission("role1").parameter("name", "TestName").build(); // <2>

		String jwtToken = JwtTokenBuilder.buildJwtToken(configuration, authc, UUID.randomUUID().toString()); // <3>
		// end::build[]
	}

	public void authenticator() throws IOException {
		// tag::authenticator[]
		JwtConfiguration configuration = JwtConfiguration.builder().issuer("MyIssuer") // set the JWT token issuer
				.expireTime(10000) // token expire time in milliseconds
				.includeDetails(true) // include the Authentication details in JWT token generation
				.includePermissions(true) // include the Authentication permissions in JWT token generation
				.signatureAlgorithm(JwtSignatureAlgorithm.HS256) // use HS256 as signature algorithm
				.sharedKey(new byte[] { 1, 2, 3 }) // shared key to use with the symmetric signing algorithm
				.build();

		// Build the Jwt authenticator using the JwtConfiguration
		JwtAuthenticator jwtAuthenticator = JwtAuthenticator.builder().configuration(configuration)
				.issuer("allowedIssuer").requiredClaim("myClaim").build();

		// Build a Realm and register the authenticator
		Realm realm = Realm.builder().authenticator(jwtAuthenticator).withDefaultAuthorizer().build();

		try {

			// Authentication using a bearer token
			Authentication authc = realm.authenticate(AuthenticationToken.bearer("TheJWTtokenHere..."));

			// Authentication using a message
			HttpRequest request = null; // expected an HttpRequest message with an 'Authorization' header with 'Bearer:
										// JWTtokenValue'
			authc = realm.authenticate(request);

		} catch (AuthenticationException e) {
			// handle authentication failures
		}
		// end::authenticator[]
	}

}
