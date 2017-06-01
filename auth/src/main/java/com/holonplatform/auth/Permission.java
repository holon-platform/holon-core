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
package com.holonplatform.auth;

import java.io.Serializable;
import java.util.Optional;

import com.holonplatform.auth.internal.DefaultPermission;

/**
 * Represents a generic permission granted to a principal in authorization context and associated to an
 * {@link Authentication} object.
 * 
 * <p>
 * Could be for example a role associated to a user principal.
 * </p>
 * 
 * <p>
 * Explicit {@link #equals(Object)} and {@link #hashCode()} methods implementation is expected for a proper
 * authorization control behaviour.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see Authorizer
 */
public interface Permission extends Serializable {

	/**
	 * If this permission can be represented as a String retaining a sufficient precision to be relied upon for a access
	 * control decisions, this method should return such string.
	 * @return a representation of the Permission, or empty if this permission cannot be expressed as a String with
	 *         sufficient precision
	 */
	Optional<String> getPermission();

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	int hashCode();

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	boolean equals(Object permission);

	/**
	 * Create a default Permission implementation, using given <code>permission</code> String to represent permission
	 * authority.
	 * @param permission String representation of the permission. Must be not <code>null</code>.
	 * @return Permission instance
	 */
	static Permission create(String permission) {
		return new DefaultPermission(permission);
	}

}
