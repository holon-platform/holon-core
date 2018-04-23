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
package com.holonplatform.spring.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.spring.security.internal.DefaultSpringSecurityAuthenticationToken;

/**
 * An {@link AuthenticationToken} which uses a Spring Security {@link Authentication} as authentication principal and
 * credentials provider.
 * <p>
 * The Spring Security {@link Authentication} is returned from {@link AuthenticationToken#getPrincipal()}, the
 * {@link Authentication#getCredentials()} is returned from {@link AuthenticationToken#getCredentials()}.
 * </p>
 * 
 * @since 5.1.0
 */
public interface SpringSecurityAuthenticationToken extends AuthenticationToken {

	/**
	 * Get the Spring Security {@link Authentication} bound to this token.
	 * @return The Spring Security {@link Authentication} (not null)
	 */
	Authentication getAuthentication();

	/**
	 * Create a new authentican token using given Spring Security {@link Authentication} as concrete authentication
	 * token.
	 * @param authentication Spring Security {@link Authentication} token (not null)
	 * @return A new {@link SpringSecurityAuthenticationToken}
	 */
	static SpringSecurityAuthenticationToken create(Authentication authentication) {
		return new DefaultSpringSecurityAuthenticationToken(authentication);
	}

	/**
	 * Create an account credentials authentican token, using the Spring Security
	 * {@link UsernamePasswordAuthenticationToken} as concrete authentication token.
	 * @param accountId Account id (username)
	 * @param secret Account secret (password)
	 * @return A new {@link SpringSecurityAuthenticationToken} with given account credentials
	 */
	static SpringSecurityAuthenticationToken account(String accountId, String secret) {
		return new DefaultSpringSecurityAuthenticationToken(new UsernamePasswordAuthenticationToken(accountId, secret));
	}

}
