/*
 * Copyright 2016-2018 Axioma srl.
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
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.jwt.JwtConfiguration.InvalidJwtConfigurationException;
import com.holonplatform.auth.jwt.internal.DefaultJwtTokenParser;

/**
 * A parser to parse a JWT and obtain it as an {@link Authentication} instance.
 * 
 * @since 5.1.1
 */
public interface JwtTokenParser {

	/**
	 * Parse given JWT and obtain it as an {@link Authentication} instance.
	 * <p>
	 * If the {@link JwtConfiguration#isIncludeDetails()} switch is <code>true</code>, the JWT claims will be included
	 * in the {@link Authentication} instance parameter set, using the claim name as detail key.
	 * </p>
	 * <p>
	 * If the {@link JwtConfiguration#isIncludePermissions()} switch is <code>true</code>, the default JWT claim named
	 * {@link AuthenticationClaims#CLAIM_NAME_PERMISSIONS}, if present, is parsed to obtain the the authentication
	 * permissions.
	 * </p>
	 * @param configuration JWT configuration to use (not null)
	 * @param jwt JWT value (not null)
	 * @return The {@link Authentication} instance which represents the JWT value, if the token is valid.
	 * @throws InvalidJwtConfigurationException If the JWT confguration is not valid
	 * @throws AuthenticationException If the JWT token validation fails
	 */
	Authentication.Builder parseJwt(JwtConfiguration configuration, String jwt)
			throws InvalidJwtConfigurationException, AuthenticationException;

	/**
	 * Get the default {@link JwtTokenParser}.
	 * @return The default {@link JwtTokenParser}.
	 */
	static JwtTokenParser get() {
		return DefaultJwtTokenParser.INSTANCE;
	}

}
