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

import org.springframework.security.core.Authentication;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.spring.security.SpringSecurityAuthenticationToken;

/**
 * Default {@link SpringSecurityAuthenticationToken} implementation.
 * 
 * @since 5.1.0
 */
public class DefaultSpringSecurityAuthenticationToken implements SpringSecurityAuthenticationToken {

	private static final long serialVersionUID = -8765100562355612601L;

	private final Authentication authentication;

	/**
	 * Constructor.
	 * @param authentication Spring Security {@link Authentication} token (not null)
	 */
	public DefaultSpringSecurityAuthenticationToken(Authentication authentication) {
		super();
		ObjectUtils.argumentNotNull(authentication, "Authentication must be not null");
		this.authentication = authentication;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationToken#getPrincipal()
	 */
	@Override
	public Object getPrincipal() {
		return authentication.getPrincipal();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationToken#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return authentication.getCredentials();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.test.springsecurity.integration.SpringSecurityAuthenticationToken#getAuthentication()
	 */
	@Override
	public Authentication getAuthentication() {
		return authentication;
	}

}
