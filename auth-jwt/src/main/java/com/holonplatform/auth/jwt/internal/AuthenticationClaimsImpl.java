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
package com.holonplatform.auth.jwt.internal;

import com.holonplatform.auth.jwt.AuthenticationClaims;

import io.jsonwebtoken.Claims;

/**
 * Default {@link AuthenticationClaims} implementation
 * 
 * @since 5.0.0
 */
public class AuthenticationClaimsImpl implements AuthenticationClaims {

	private final Claims claims;

	/**
	 * Constructor
	 * @param claims JWT claims
	 */
	public AuthenticationClaimsImpl(Claims claims) {
		super();
		this.claims = claims;
	}

	/**
	 * JWT claims
	 * @return JWT claims
	 */
	protected Claims getClaims() {
		return claims;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.jwt.AuthenticationClaims#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T get(String claimName, Class<T> requiredType) {
		if (getClaims() != null) {
			return getClaims().get(claimName, requiredType);
		}
		return null;
	}

}
