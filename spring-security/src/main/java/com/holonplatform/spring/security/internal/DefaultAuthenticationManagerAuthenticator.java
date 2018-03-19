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
package com.holonplatform.spring.security.internal;

import org.springframework.security.authentication.AuthenticationManager;

import com.holonplatform.auth.Authenticator;
import com.holonplatform.spring.security.SpringSecurityAuthenticationToken;

/**
 * Default Spring Security {@link Authenticator} using {@link SpringSecurityAuthenticationToken} types.
 * 
 * @since 5.1.0
 */
public class DefaultAuthenticationManagerAuthenticator
		extends AbstractAuthenticationManagerAuthenticator<SpringSecurityAuthenticationToken> {

	/**
	 * Constructor.
	 * @param authenticationManager Spring Security {@link AuthenticationManager} (not null)
	 */
	public DefaultAuthenticationManagerAuthenticator(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authenticator#getTokenType()
	 */
	@Override
	public Class<? extends SpringSecurityAuthenticationToken> getTokenType() {
		return SpringSecurityAuthenticationToken.class;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.test.springsecurity.integration.AbstractAuthenticationManagerAuthenticator#getAuthentication(
	 * com.holonplatform.auth.AuthenticationToken)
	 */
	@Override
	protected org.springframework.security.core.Authentication getAuthentication(
			SpringSecurityAuthenticationToken authenticationToken) {
		return authenticationToken.getAuthentication();
	}

}
