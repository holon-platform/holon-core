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
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import com.holonplatform.auth.Permission;
import com.holonplatform.core.internal.DefaultParameterSet;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.spring.security.SpringSecurityAuthentication;

/**
 * Default {@link SpringSecurityAuthentication} implementation.
 * 
 * @since 5.1.0
 */
public class DefaultSpringSecurityAuthentication extends DefaultParameterSet implements SpringSecurityAuthentication {

	private static final long serialVersionUID = 8101815940531618115L;

	/**
	 * Spring Security Authentication
	 */
	private final org.springframework.security.core.Authentication authentication;

	/**
	 * Authentication scheme
	 */
	private String scheme;

	/**
	 * Constructor.
	 * @param authentication The Spring Security Authentication (not null)
	 */
	public DefaultSpringSecurityAuthentication(org.springframework.security.core.Authentication authentication) {
		super();
		ObjectUtils.argumentNotNull(authentication, "Authentication must be not null");
		this.authentication = authentication;
		
		// details
		addParameter(AUTHENTICATION_DETAILS_KEY, authentication.getDetails());
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
	 * @see com.holonplatform.auth.Authentication#getScheme()
	 */
	@Override
	public Optional<String> getScheme() {
		return Optional.ofNullable(scheme);
	}

	/**
	 * Set the authentication scheme
	 * @param scheme the scheme to set
	 */
	protected void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authentication#getPermissions()
	 */
	@Override
	public Collection<Permission> getPermissions() {
		return authentication.getAuthorities().stream().map(a -> new SpringSecurityPermission(a))
				.collect(Collectors.toSet());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authentication#isRoot()
	 */
	@Override
	public boolean isRoot() {
		return false;
	}

	// Spring Security

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authentication.getAuthorities();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return authentication.getCredentials();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getDetails()
	 */
	@Override
	public Object getDetails() {
		return authentication.getDetails();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getPrincipal()
	 */
	@Override
	public Object getPrincipal() {
		return authentication.getPrincipal();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#isAuthenticated()
	 */
	@Override
	public boolean isAuthenticated() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#setAuthenticated(boolean)
	 */
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// noop
	}

	// equals/hashCode

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authentication == null) ? 0 : authentication.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultSpringSecurityAuthentication other = (DefaultSpringSecurityAuthentication) obj;
		if (authentication == null) {
			if (other.authentication != null)
				return false;
		} else if (!authentication.equals(other.authentication))
			return false;
		return true;
	}

}
