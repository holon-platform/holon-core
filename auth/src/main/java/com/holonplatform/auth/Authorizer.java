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

import java.util.Collection;

import com.holonplatform.auth.internal.DefaultAuthorizer;

/**
 * Authorizer is responsible for {@link Authentication} authorization control using
 * {@link Authentication#getPermissions() permissions}.
 * 
 * @param <P> Supported Permission type
 * 
 * @since 5.0.0
 */
public interface Authorizer<P extends Permission> {

	/**
	 * Get supported {@link Permission} type
	 * @return Supported {@link Permission} type
	 */
	Class<? extends P> getPermissionType();

	/**
	 * Check if given Authentication has all specified permission/s
	 * @param authentication Authentication subject
	 * @param permissions Permissions to check
	 * @return <code>true</code> if given Authentication has all specified permission
	 */
	@SuppressWarnings("unchecked")
	boolean isPermitted(Authentication authentication, P... permissions);

	/**
	 * Check if given Authentication has all specified permission/s using String permission form.
	 * <p>
	 * String permission match against Authentication {@link Permission}s will be performed using
	 * {@link Permission#getPermission()} method.
	 * </p>
	 * @param authentication Authentication subject
	 * @param permissions Permissions to check
	 * @return <code>true</code> if given Authentication has all specified permission
	 */
	boolean isPermitted(Authentication authentication, String... permissions);

	/**
	 * Check if given Authentication has any of specified permission/s
	 * @param authentication Authentication subject
	 * @param permissions Permissions to check
	 * @return <code>true</code> if given Authentication has any of specified permission
	 */
	@SuppressWarnings("unchecked")
	boolean isPermittedAny(Authentication authentication, P... permissions);

	/**
	 * Check if given Authentication has any of specified permission/s using String permission form.
	 * <p>
	 * String permission match against Authentication {@link Permission}s will be performed using
	 * {@link Permission#getPermission()} method.
	 * </p>
	 * @param authentication Authentication subject
	 * @param permissions Permissions to check
	 * @return <code>true</code> if given Authentication has any of specified permission
	 */
	boolean isPermittedAny(Authentication authentication, String... permissions);

	/**
	 * Check if given Authentication has all specified permission/s using a Collection
	 * @param authentication Authentication subject
	 * @param permissions Permissions to check
	 * @return <code>true</code> if given Authentication has all specified permission
	 */
	boolean isPermitted(Authentication authentication, Collection<P> permissions);

	/**
	 * Check if given Authentication has any of specified permission/s using a Collection
	 * @param authentication Authentication subject
	 * @param permissions Permissions to check
	 * @return <code>true</code> if given Authentication has any of specified permission
	 */
	boolean isPermittedAny(Authentication authentication, Collection<P> permissions);

	/**
	 * Create a default Authorizer.
	 * <p>
	 * The default Authorizer never throws exceptions, adopting the following behaviour:
	 * <ul>
	 * <li>if <code>null</code> {@link Authentication} is given, always returns <code>false</code></li>
	 * <li>if <code>null</code> or empty permissions are given, always returns <code>false</code></li>
	 * <li>if {@link Authentication} is not <code>null</code> and is <code>root</code>, always returns
	 * <code>true</code></li>
	 * <li>For other cases, permission checking is performed comparing Authentication granted permission with requested
	 * permissions using {@link Permission#equals(Object)} to compare a single permission to another</li>
	 * </ul>
	 * @return Authorizer
	 * @see DefaultAuthorizer
	 */
	static Authorizer<Permission> create() {
		return new DefaultAuthorizer();
	}

}
