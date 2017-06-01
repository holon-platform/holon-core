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

import java.util.Collection;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Authorizer;
import com.holonplatform.auth.Permission;

/**
 * Default {@link Authorizer} implementation.
 * 
 * <p>
 * This Authorizer never throws exceptions, adopting the following behaviour:
 * <ul>
 * <li>if <code>null</code> {@link Authentication} is given, always returns <code>false</code></li>
 * <li>if <code>null</code> or empty permissions are given, always returns <code>false</code></li>
 * <li>if {@link Authentication} is not <code>null</code> and is <code>root</code>, always returns
 * <code>true</code></li>
 * <li>For other cases, permission checking is performed comparing Authentication granted permission with requested
 * permissions using {@link Permission#equals(Object)} to compare a single permission to another</li>
 * </ul>
 *
 * @since 5.0.0
 */
public class DefaultAuthorizer extends AbstractAuthorizer<Permission> {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#getPermissionType()
	 */
	@Override
	public Class<? extends Permission> getPermissionType() {
		return Permission.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.authz.AbstractAuthorizer#permissionFromString(java.lang.String)
	 */
	@Override
	protected Permission permissionFromString(String permission) {
		return Permission.create(permission);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.authz.AbstractAuthorizer#checkPermitted(com.holonplatform.auth.Authentication,
	 * java.util.Collection, boolean)
	 */
	@Override
	protected boolean checkPermitted(Authentication authentication, Collection<Permission> permissions, boolean all) {
		if (authentication != null && authentication.isRoot()) {
			return true;
		}
		if (authentication != null && permissions != null && !permissions.isEmpty()) {
			Collection<Permission> granted = authentication.getPermissions();
			if (granted != null && !granted.isEmpty()) {
				if (all) {
					return granted.containsAll(permissions);
				} else {
					for (Permission p : permissions) {
						if (granted.contains(p)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
