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

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

import com.holonplatform.auth.internal.DefaultAuthentication;
import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.config.ConfigProperty;

/**
 * Represents an authenticated principal (for example a user) using
 * {@link Authenticator#authenticate(AuthenticationToken)}.
 * 
 * <p>
 * Extends standard {@link Principal} interface, so the the principal name (for example the username) can be obtained
 * using {@link #getName()}.
 * </p>
 * 
 * <p>
 * Supports authorizations using {@link Permission} grants. All granted permissions should be provided by
 * {@link #getPermissions()} method. If {@link #isRoot()} returns <code>true</code>, this authentication will be treated
 * as <code>root</code>, i.e. with all permissions, and any granted permission will be ignored.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see Authenticator
 */
public interface Authentication extends Principal, ParameterSet {

	/**
	 * Get the authentication scheme with which this authentication was produced.
	 * @return Optional authentication scheme name
	 */
	Optional<String> getScheme();

	/**
	 * Set of {@link Permission}s granted to principal with this Authentication
	 * @return Granted permissions, or an empty Collection if none
	 */
	Collection<Permission> getPermissions();

	/**
	 * If this method returns <code>true</code>, this Authentication is considered a <code>root</code> principal, for
	 * which permission checking is always skipped, assuming that any permission is granted to this Authentication.
	 * @return <code>true</code> if this Authentication is considered a <code>root</code> principal
	 */
	boolean isRoot();

	/**
	 * Builder to create Authentication instance for given <code>principalName</code>.
	 * @param principalName {@link Principal} name (not null)
	 * @return AuthenticationBuilder
	 */
	static Builder builder(String principalName) {
		return new DefaultAuthentication.AuthenticationBuilder(principalName);
	}

	/**
	 * Builder to create {@link Authentication} instances.
	 */
	public interface Builder {

		/**
		 * Set the authentication scheme with which this authentication was produced.
		 * @param scheme Authentication scheme name
		 * @return this
		 */
		Builder scheme(String scheme);

		/**
		 * Set whether authentication is considered a <code>root</code> principal, for which permission checking is
		 * always skipped, assuming that any permission is granted.
		 * @param root <code>true</code> to set authentication as <code>root</code> principal
		 * @return this
		 */
		Builder root(boolean root);

		/**
		 * Add an authentication parameter.
		 * @param name Parameter name (not null)
		 * @param value Parameter value
		 * @return this
		 */
		Builder withParameter(String name, Object value);

		/**
		 * Add an authentication parameter
		 * @param name Parameter name (not null)
		 * @param value Parameter value
		 * @return this
		 * @deprecated Use {@link #withParameter(String, Object)}
		 */
		@Deprecated
		default Builder parameter(String name, Object value) {
			return withParameter(name, value);
		}

		/**
		 * Add an authentication parameter using a {@link ConfigProperty} an {@link ConfigProperty#getKey()} as
		 * parameter name
		 * @param <T> Property type
		 * @param property ConfigProperty to obtain parameter name (not null)
		 * @param value Parameter value
		 * @return this
		 */
		<T> Builder withParameter(ConfigProperty<T> property, T value);

		/**
		 * Add an authentication parameter using a {@link ConfigProperty} an {@link ConfigProperty#getKey()} as
		 * parameter name
		 * @param <T> Property type
		 * @param property ConfigProperty to obtain parameter name (not null)
		 * @param value Parameter value
		 * @return this
		 * @deprecated Use {@link #withParameter(ConfigProperty, Object)}
		 */
		@Deprecated
		default <T> Builder parameter(ConfigProperty<T> property, T value) {
			return withParameter(property, value);
		}

		/**
		 * Add a permission granted to Authentication
		 * @param permission Permission to add (not null)
		 * @return this
		 */
		Builder withPermission(Permission permission);

		/**
		 * Add a permission granted to Authentication
		 * @param permission Permission to add (not null)
		 * @return this
		 * @deprecated Use {@link #withPermission(Permission)}
		 */
		@Deprecated
		default Builder permission(Permission permission) {
			return withPermission(permission);
		}

		/**
		 * Add a permission granted to Authentication using String representation.
		 * @param permission Permission string to add (not null)
		 * @return this
		 */
		Builder withPermission(String permission);

		/**
		 * Add a permission granted to Authentication using String representation.
		 * @param permission Permission string to add (not null)
		 * @return this
		 * @deprecated Use {@link #withPermission(String)}
		 */
		@Deprecated
		default Builder permission(String permission) {
			return withPermission(permission);
		}

		/**
		 * Create Authentication instance
		 * @return Authentication
		 */
		Authentication build();

	}

	/**
	 * Listener to handle authentication events.
	 */
	@FunctionalInterface
	public interface AuthenticationListener {

		/**
		 * Called when an authentication or deauthentication event occurs.
		 * @param authentication Authentication instance, <code>null</code> in case of deauthentication event
		 */
		void onAuthentication(Authentication authentication);

	}

	/**
	 * Supports {@link AuthenticationListener} registration.
	 */
	public interface AuthenticationNotifier {

		/**
		 * Register an {@link AuthenticationListener}.
		 * @param authenticationListener The listener to add (not null)
		 */
		void addAuthenticationListener(AuthenticationListener authenticationListener);

		/**
		 * Removes a registered {@link AuthenticationListener}.
		 * @param authenticationListener Listener to remove
		 */
		void removeAuthenticationListener(AuthenticationListener authenticationListener);

	}

}
