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

import org.springframework.security.core.GrantedAuthority;

import com.holonplatform.auth.Permission;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * {@link Permission} representation using a Spring Security {@link GrantedAuthority}.
 * <p>
 * The {@link GrantedAuthority#getAuthority()} value is used as {@link String} permission representation.
 * </p>
 * 
 * @since 5.1.0
 */
public class SpringSecurityPermission implements Permission {

	private static final long serialVersionUID = -3849686897833818846L;

	/**
	 * Spring Security authority
	 */
	private final GrantedAuthority authority;

	/**
	 * Constructor.
	 * @param authority Spring Security authority (not null)
	 */
	public SpringSecurityPermission(GrantedAuthority authority) {
		super();
		ObjectUtils.argumentNotNull(authority, "GrantedAuthority must be not null");
		this.authority = authority;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Permission#getPermission()
	 */
	@Override
	public Optional<String> getPermission() {
		return Optional.ofNullable(authority.getAuthority());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getPermission().orElse(null);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
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
		SpringSecurityPermission other = (SpringSecurityPermission) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		return true;
	}

}
