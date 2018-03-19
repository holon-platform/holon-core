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

import com.holonplatform.auth.Authentication;
import com.holonplatform.spring.security.internal.DefaultSpringSecurityAuthentication;

/**
 * Represents an {@link Authentication} within a Spring Security environment, extending the Spring Security
 * {@link org.springframework.security.core.Authentication} interface too.
 * <p>
 * The {@link #AUTHENTICATION_DETAILS_KEY} parameter name is used to store the Spring Security authentication details as
 * an {@link Authentication} parameter.
 * </p>
 * 
 * @since 5.1.0
 */
public interface SpringSecurityAuthentication extends Authentication, org.springframework.security.core.Authentication {

	/**
	 * Authentication details parameter name.
	 */
	public static final String AUTHENTICATION_DETAILS_KEY = SpringSecurityAuthentication.class.getName() + "#details";

	/**
	 * Get the {@link Authentication} representation of the given Spring Security Authentication as a
	 * {@link SpringSecurityAuthentication}.
	 * <p>
	 * If authentication details are available through
	 * {@link org.springframework.security.core.Authentication#getDetails()}, it will be available as an
	 * {@link Authentication} parameter with name {@link SpringSecurityAuthentication#AUTHENTICATION_DETAILS_KEY}.
	 * </p>
	 * @param authentication The Spring Security Authentication (not null)
	 * @return The {@link SpringSecurityAuthentication} representation of the Spring Security Authentication
	 */
	static SpringSecurityAuthentication create(org.springframework.security.core.Authentication authentication) {
		return new DefaultSpringSecurityAuthentication(authentication);
	}

}
