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
package com.holonplatform.auth;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Represents an object which provides the current {@link Authentication}, if available. The <em>current
 * authentication</em> semantic depends on the concrete authentication context to which the inspector is bound.
 * <p>
 * A list of authorization control methods are provided to perform access control decisions using the current
 * {@link Authentication}.
 * </p>
 * 
 * @since 5.1.0
 * 
 * @see AuthContext
 */
public interface AuthenticationInspector {

	/**
	 * Get the current {@link Authentication}, if available.
	 * @return The current {@link Authentication}, or an empty Optional if not available.
	 */
	Optional<Authentication> getAuthentication();

	/**
	 * Get the current {@link Authentication}, throwing an {@link IllegalStateException} if not available.
	 * @return The current {@link Authentication}
	 */
	default Authentication requireAuthentication() {
		return getAuthentication().orElseThrow(() -> new IllegalStateException("No Authentication available"));
	}

	/**
	 * Gets whether an {@link Authentication} is available.
	 * @return <code>true</code> if an {@link Authentication} is available, <code>false</code> otherwise
	 */
	default boolean isAuthenticated() {
		return getAuthentication().isPresent();
	}

	// ------- Authorization

	/**
	 * Checks if the current {@link Authentication} has all the specified {@link Permission}s.
	 * <p>
	 * The permission control strategy depends on the concrete authorization strategy of the current authentication
	 * context.
	 * </p>
	 * @param permissions The permissions to be checked
	 * @return <code>true</code> if the current {@link Authentication} has all the specified permission,
	 *         <code>false</code> otherwise
	 */
	boolean isPermitted(Collection<? extends Permission> permissions);

	/**
	 * Checks if the current {@link Authentication} has all the specified {@link Permission}s.
	 * <p>
	 * The permission control strategy depends on the concrete authorization strategy of the current authentication
	 * context.
	 * </p>
	 * @param permissions The permissions to be checked
	 * @return <code>true</code> if the current {@link Authentication} has all the specified permission,
	 *         <code>false</code> otherwise
	 */
	default boolean isPermitted(Permission... permissions) {
		return isPermitted(Arrays.asList(permissions));
	}

	/**
	 * Checks if the current {@link Authentication} has all the specified permissions, using the {@link String}
	 * permission representation.
	 * <p>
	 * The permission control strategy depends on the concrete authorization strategy of the current authentication
	 * context.
	 * </p>
	 * @param permissions The permissions to be checked
	 * @return <code>true</code> if the current {@link Authentication} has all the specified permission,
	 *         <code>false</code> otherwise
	 */
	boolean isPermitted(String... permissions);

	/**
	 * Checks if the current {@link Authentication} has any of the specified {@link Permission}.
	 * <p>
	 * The permission control strategy depends on the concrete authorization strategy of the current authentication
	 * context.
	 * </p>
	 * @param permissions The permissions to be checked
	 * @return <code>true</code> if the current {@link Authentication} has any of the specified permission,
	 *         <code>false</code> otherwise
	 */
	boolean isPermittedAny(Collection<? extends Permission> permissions);

	/**
	 * Checks if the current {@link Authentication} has any of the specified {@link Permission}.
	 * <p>
	 * The permission control strategy depends on the concrete authorization strategy of the current authentication
	 * context.
	 * </p>
	 * @param permissions The permissions to be checked
	 * @return <code>true</code> if the current {@link Authentication} has any of the specified permission,
	 *         <code>false</code> otherwise
	 */
	default boolean isPermittedAny(Permission... permissions) {
		return isPermittedAny(Arrays.asList(permissions));
	}

	/**
	 * Checks if the current {@link Authentication} has any of the specified permission, using the {@link String}
	 * permission representation.
	 * <p>
	 * The permission control strategy depends on the concrete authorization strategy of the current authentication
	 * context.
	 * </p>
	 * @param permissions The permissions to be checked
	 * @return <code>true</code> if the current {@link Authentication} has any of the specified permission,
	 *         <code>false</code> otherwise
	 */
	boolean isPermittedAny(String... permissions);

}
