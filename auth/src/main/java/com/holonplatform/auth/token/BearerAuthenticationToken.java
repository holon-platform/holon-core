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
package com.holonplatform.auth.token;

import com.holonplatform.auth.AuthenticationToken;

/**
 * {@link AuthenticationToken} representing a Bearer authententication token.
 * <p>
 * This AuthenticationToken returns always <code>null</code> from {@link #getPrincipal()} and returns the token from
 * {@link #getCredentials()}.
 * </p>
 * 
 * @since 5.0.0
 */
public class BearerAuthenticationToken implements AuthenticationToken {

	private static final long serialVersionUID = 8649804947453284621L;

	/**
	 * Token
	 */
	private final String token;

	/**
	 * Construct a new BearerAuthenticationToken
	 * @param token Bearer token value
	 */
	public BearerAuthenticationToken(String token) {
		super();
		this.token = token;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationToken#getPrincipal()
	 */
	@Override
	public Object getPrincipal() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationToken#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return token;
	}

}
