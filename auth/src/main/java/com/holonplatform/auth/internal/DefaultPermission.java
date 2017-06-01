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
package com.holonplatform.auth.internal;

import java.util.Optional;

import com.holonplatform.auth.Permission;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link Permission} implementation, using a String to represent permission authority.
 * 
 * @since 5.0.0
 */
public class DefaultPermission implements Permission {

	private static final long serialVersionUID = -6300515775575638564L;

	/*
	 * Permission String representation (immutable)
	 */
	private final String permission;

	/**
	 * Construct a permission
	 * @param permission String representation of this permission. Must be not <code>null</code>.
	 */
	public DefaultPermission(String permission) {
		super();
		ObjectUtils.argumentNotNull(permission, "Permission string must be not null");
		this.permission = permission;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Permission#getPermission()
	 */
	@Override
	public Optional<String> getPermission() {
		return Optional.ofNullable(permission);
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
		if (!(obj instanceof Permission)) {
			return false;
		}
		Permission other = (Permission) obj;
		if (permission == null) {
			if (other.getPermission().isPresent())
				return false;
		} else if (!permission.equals(other.getPermission().get()))
			return false;
		return true;
	}

}
