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

import org.springframework.security.core.GrantedAuthority;

import com.holonplatform.auth.Permission;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * A {@link GrantedAuthority} using a {@link Permission} as concrete representation.
 * 
 * @since 5.1.0
 */
public class PermissionGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = -3078288281786256578L;

	/**
	 * Permission
	 */
	private final Permission permission;

	/**
	 * Constructor.
	 * @param permission The permission (not null)
	 */
	public PermissionGrantedAuthority(Permission permission) {
		super();
		ObjectUtils.argumentNotNull(permission, "Permission must be not null");
		this.permission = permission;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority() {
		return permission.getPermission().orElse(null);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getAuthority();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((permission == null) ? 0 : permission.hashCode());
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
		PermissionGrantedAuthority other = (PermissionGrantedAuthority) obj;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		return true;
	}

}
