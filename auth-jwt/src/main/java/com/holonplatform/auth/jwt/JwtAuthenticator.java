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
package com.holonplatform.auth.jwt;

import java.util.Collection;

import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.jwt.internal.DefaultJwtAuthenticator;
import com.holonplatform.auth.token.BearerAuthenticationToken;

/**
 * An {@link Authenticator} able to authenticate {@link BearerAuthenticationToken} tokens, expecting a JWT token as
 * bearer token and using a {@link JwtConfiguration} to perform authentication.
 *
 * @since 5.0.0
 */
public interface JwtAuthenticator extends Authenticator<BearerAuthenticationToken> {

	/**
	 * Get the {@link JwtConfiguration} to use to validate and authenticate the JWT tokens
	 * @return JwtConfiguration
	 */
	JwtConfiguration getConfiguration();

	/**
	 * Get the allowed JWT issuers
	 * @return Allowed JWT issuers, an empty list if any issuer is allowed
	 */
	Collection<String> getIssuers();

	/**
	 * Get optional required JWT claims
	 * @return Required JWT claims, empty if none
	 */
	Collection<String> getRequiredClaims();

	/**
	 * Builder to create a JwtAuthenticator.
	 * @return JwtAuthenticator builder
	 */
	static Builder builder() {
		return new DefaultJwtAuthenticator.DefaultBuilder();
	}

	/**
	 * Builder to create {@link JwtAuthenticator} instances
	 *
	 * @since 5.0.0
	 */
	public interface Builder {

		/**
		 * Set the {@link JwtConfiguration} to use to perform authentication and token validation
		 * @param configuration JwtConfiguration (not null)
		 * @return this
		 */
		Builder configuration(JwtConfiguration configuration);

		/**
		 * Add an allowed JWT issuer.
		 * <p>
		 * If one ore more allowed issuer is registered, JWT Issuer claim (iss) will be required and checked during
		 * token authentication: if token issuer doesn't match one of the given issuers, authentication will fail.
		 * </p>
		 * @param issuer Issuer to add (not null)
		 * @return this
		 */
		Builder issuer(String issuer);

		/**
		 * Add a required JWT claim: specified claim must exist in JWT token, otherwise authentication will fail.
		 * @param claim Claim to add (not null)
		 * @return this
		 */
		Builder withRequiredClaim(String claim);

		/**
		 * Add a required JWT claim: specified claim must exist in JWT token, otherwise authentication will fail.
		 * @param claim Claim to add (not null)
		 * @return this
		 * @deprecated Use {@link #withRequiredClaim(String)}
		 */
		@Deprecated
		default Builder requiredClaim(String claim) {
			return withRequiredClaim(claim);
		}

		/**
		 * Build the JwtAuthenticator
		 * @return JwtAuthenticator
		 */
		JwtAuthenticator build();

	}

}
