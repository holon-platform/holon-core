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

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import com.holonplatform.auth.AuthContext.AuthenticationHolder;
import com.holonplatform.auth.Authentication;
import com.holonplatform.spring.security.SpringSecurityAuthentication;

/**
 * An {@link AuthenticationHolder} using {@link SecurityContextHolder#getContext()} as Authentication handler.
 *
 * @since 5.1.0
 */
public class SpringSecurityAuthenticationHolder implements AuthenticationHolder {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext.AuthenticationHolder#getAuthentication()
	 */
	@Override
	public Optional<Authentication> getAuthentication() {
		final org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth != null) {
			if (auth instanceof Authentication) {
				return Optional.of((Authentication) auth);
			}
			return Optional.of(SpringSecurityAuthentication.create(auth));
		}
		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.auth.AuthContext.AuthenticationHolder#setAuthentication(com.holonplatform.auth.Authentication)
	 */
	@Override
	public void setAuthentication(Authentication authentication) {
		if (authentication == null) {
			SecurityContextHolder.getContext().setAuthentication(null);
		} else {
			// check type
			if (org.springframework.security.core.Authentication.class.isAssignableFrom(authentication.getClass())) {
				SecurityContextHolder.getContext()
						.setAuthentication((org.springframework.security.core.Authentication) authentication);
			} else {
				// use an adapter
				SecurityContextHolder.getContext()
						.setAuthentication(new SpringSecurityAuthenticationAdapter(authentication));
			}
		}
	}

}
