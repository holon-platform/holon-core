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
package com.holonplatform.auth.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Authorizer;
import com.holonplatform.auth.Permission;

/**
 * Abstract {@link Authorizer} implementation
 * 
 * @param <P> Supported permission type
 * 
 * @since 5.0.0
 * 
 * @see DefaultAuthorizer
 */
public abstract class AbstractAuthorizer<P extends Permission> implements Authorizer<P> {

	/**
	 * Check permissions for given Authentication
	 * @param authentication Authentication subject
	 * @param permissions Permissions to control
	 * @param all <code>true</code> if all permissions must be granted to Authentication, <code>false</code> if at least
	 *        one
	 * @return <code>true</code> if permissions check was successful
	 */
	protected abstract boolean checkPermitted(Authentication authentication, Collection<P> permissions, boolean all);

	/**
	 * Build a Permission of required type using given String representation.
	 * <p>
	 * This method is used to perform permissions control using String representations
	 * </p>
	 * @param permission Permission String representation
	 * @return Permission instance
	 */
	protected abstract P permissionFromString(String permission);

	/**
	 * Convert given String representations into Permission objects
	 * @param permissions Permission String representations
	 * @return Permission objects collection
	 */
	protected Collection<P> convertPermissions(String... permissions) {
		Collection<P> ps = new LinkedList<>();
		if (permissions != null) {
			for (String permission : permissions) {
				ps.add(permissionFromString(permission));
			}
		}
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermitted(com.holonplatform.auth.Authentication,
	 * com.holonplatform.auth.Permission[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isPermitted(Authentication authentication, P... permissions) {
		return checkPermitted(authentication,
				(permissions != null && permissions.length > 0) ? Arrays.asList(permissions) : null, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermitted(com.holonplatform.auth.Authentication, java.lang.String[])
	 */
	@Override
	public boolean isPermitted(Authentication authentication, String... permissions) {
		return checkPermitted(authentication, convertPermissions(permissions), true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermittedAny(com.holonplatform.auth.Authentication,
	 * com.holonplatform.auth.Permission[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isPermittedAny(Authentication authentication, P... permissions) {
		return checkPermitted(authentication,
				(permissions != null && permissions.length > 0) ? Arrays.asList(permissions) : null, false);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermittedAny(com.holonplatform.auth.Authentication, java.lang.String[])
	 */
	@Override
	public boolean isPermittedAny(Authentication authentication, String... permissions) {
		return checkPermitted(authentication, convertPermissions(permissions), false);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermitted(com.holonplatform.auth.Authentication, java.util.Collection)
	 */
	@Override
	public boolean isPermitted(Authentication authentication, Collection<P> permissions) {
		return checkPermitted(authentication, permissions, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermittedAny(com.holonplatform.auth.Authentication,
	 * java.util.Collection)
	 */
	@Override
	public boolean isPermittedAny(Authentication authentication, Collection<P> permissions) {
		return checkPermitted(authentication, permissions, false);
	}

}
