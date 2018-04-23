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

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.jwt.JwtConfiguration.InvalidJwtConfigurationException;
import com.holonplatform.auth.jwt.internal.DefaultJwtTokenBuilder;

/**
 * JWT builder using {@link Authentication} objects.
 * 
 * @since 5.0.0
 */
public interface JwtTokenBuilder {

	/**
	 * Enumeration of {@link Authentication} parts
	 */
	public enum AuthPart {

		/**
		 * Permissions
		 */
		PERMISSIONS,

		/**
		 * Details
		 */
		DETAILS;

	}

	/**
	 * Build a JSON Web Token using given {@link JwtConfiguration} for given {@link Authentication}.
	 * @param configuration JWT configuration (not null)
	 * @param authentication Authentication for which to create the JWT (not null)
	 * @param jwtTokenId JWT token id (jit claim)
	 * @return The JWT representation
	 * @throws InvalidJwtConfigurationException Invalid JWT configuration
	 */
	String buildJwt(JwtConfiguration configuration, Authentication authentication, String jwtTokenId)
			throws InvalidJwtConfigurationException;

	/**
	 * Build a JSON Web Token using given {@link JwtConfiguration} for given {@link Authentication}.
	 * @param configuration JWT configuration (not null)
	 * @param authentication Authentication for which to create the JWT (not null)
	 * @return The JWT representation
	 * @throws InvalidJwtConfigurationException Invalid JWT configuration
	 */
	default String buildJwt(JwtConfiguration configuration, Authentication authentication)
			throws InvalidJwtConfigurationException {
		return buildJwt(configuration, authentication, null);
	}

	/**
	 * Get the default {@link JwtTokenBuilder}.
	 * @return The default {@link JwtTokenBuilder}.
	 */
	static JwtTokenBuilder get() {
		return DefaultJwtTokenBuilder.INSTANCE;
	}

	/**
	 * Build a JWT token using given {@link JwtConfiguration} for given {@link Authentication}.
	 * @param configuration JWT configuration (not null)
	 * @param authentication Authentication for which to create the token (not null)
	 * @param jwtTokenId Optional JWT token id (jit claim)
	 * @return JWT token
	 * @throws InvalidJwtConfigurationException Invalid JWT configuration
	 * @deprecated Use {@link #get()} to obtain the default {@link JwtTokenBuilder} on which to call the
	 *             {@link JwtTokenBuilder#buildJwt(JwtConfiguration, Authentication, String)} method
	 */
	@Deprecated
	static String buildJwtToken(JwtConfiguration configuration, Authentication authentication, String jwtTokenId)
			throws InvalidJwtConfigurationException {
		return get().buildJwt(configuration, authentication, jwtTokenId);
	}

}
