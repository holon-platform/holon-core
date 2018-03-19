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

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.spring.security.SpringSecurityAuthentication;

/**
 * Adapter to represent an {@link com.holonplatform.auth.Authentication} as a Spring Security authentication.
 *
 * @since 5.1.0
 */
public class SpringSecurityAuthenticationAdapter implements Authentication {

	private static final long serialVersionUID = 915059383615990660L;

	private final com.holonplatform.auth.Authentication authentication;
	
	private boolean authenticated;

	/**
	 * Constructor.
	 * @param authentication The concrete {@link com.holonplatform.auth.Authentication} (not null)
	 */
	public SpringSecurityAuthenticationAdapter(com.holonplatform.auth.Authentication authentication) {
		super();
		ObjectUtils.argumentNotNull(authentication, "Authentication must be not null");
		this.authentication = authentication;
		this.authenticated = true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getPrincipal()
	 */
	@Override
	public Object getPrincipal() {
		return authentication;
	}

	/*
	 * (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return authentication.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authentication.getPermissions().stream().map(p -> new PermissionGrantedAuthority(p))
				.collect(Collectors.toSet());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getDetails()
	 */
	@Override
	public Object getDetails() {
		return authentication.getParameter(SpringSecurityAuthentication.AUTHENTICATION_DETAILS_KEY).orElse(null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#isAuthenticated()
	 */
	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#setAuthenticated(boolean)
	 */
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
	}

}
