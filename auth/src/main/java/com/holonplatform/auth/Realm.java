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

import java.util.List;
import java.util.Optional;

import com.holonplatform.auth.Authentication.AuthenticationListener;
import com.holonplatform.auth.Authentication.AuthenticationNotifier;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.Authenticator.MessageAuthenticator;
import com.holonplatform.auth.internal.DefaultRealm;
import com.holonplatform.core.Context;
import com.holonplatform.core.messaging.Message;

/**
 * A Realm represents a security abstraction providing operations for principals authentication (for example login a
 * user relying on user accounts informations) and authorization (permission check against principal grants stored in
 * this realm).
 * 
 * <p>
 * A Realm may be identified by name, which should be unique within the same application.
 * </p>
 * 
 * <p>
 * Authentication is performed using {@link Authenticator} interface, but it is delegated to concrete
 * {@link Authenticator} instances which must be registered in realm using {@link #addAuthenticator(Authenticator)}.
 * </p>
 * 
 * <p>
 * Authorization is performed through {@link Authorizer} interface, and delegated to concrete {@link Authorizer}
 * instances which must be registered in realm using {@link #addAuthorizer(Authorizer)}.
 * </p>
 * 
 * <p>
 * Supports {@link AuthenticationTokenResolver} registration to perform authentication requests translation into
 * {@link AuthenticationToken}s.
 * </p>
 * 
 * <p>
 * Extends {@link MessageAuthenticator} to support authentication using generic authentication request {@link Message}.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see MessageAuthenticator
 * @see AuthenticationNotifier
 */
public interface Realm extends Authenticator<AuthenticationToken>, Authorizer<Permission>, MessageAuthenticator,
		AuthenticationNotifier {

	/**
	 * Default {@link Context} resource reference
	 */
	public static final String CONTEXT_KEY = Realm.class.getName();

	/**
	 * Get optional realm name. Should be unique at application level.
	 * @return Realm name or an empty Optional if not available
	 */
	Optional<String> getName();

	/**
	 * Returns whether this Realm supports given {@link AuthenticationToken} type (i.e. and {@link Authenticator} bound
	 * to given token type is registered in realm)
	 * @param authenticationTokenType AuthenticationToken type
	 * @return <code>true</code> if Realm supports given AuthenticationToken type
	 */
	boolean supportsToken(Class<? extends AuthenticationToken> authenticationTokenType);

	/**
	 * Add a concrete {@link Authenticator} for a specific {@link AuthenticationToken} type to this Realm
	 * @param <T> Authentication token type
	 * @param authenticator Authenticator to add
	 */
	<T extends AuthenticationToken> void addAuthenticator(Authenticator<T> authenticator);

	/**
	 * Returns whether this Realm supports given {@link Permission} type
	 * @param permissionType Permission type
	 * @return <code>true</code> if this Realm supports given Permission type
	 */
	boolean supportsPermission(Class<? extends Permission> permissionType);

	/**
	 * Add and {@link Authorizer} to support a specific {@link Permission} type
	 * @param <P> Permission type
	 * @param authorizer Authorizer to add
	 */
	<P extends Permission> void addAuthorizer(Authorizer<P> authorizer);

	/**
	 * Get registered AuthenticationListeners
	 * @return AuthenticationListeners or an empty List if none
	 */
	List<AuthenticationListener> getAuthenticationListeners();

	// Builder

	/**
	 * Builder to create Realm instances
	 * @return RealmBuilder
	 */
	static Builder builder() {
		return new DefaultRealm.RealmBuilder();
	}

	// Context resource

	/**
	 * Convenience method to obtain the current {@link Realm} made available as {@link Context} resource, using default
	 * {@link ClassLoader}.
	 * <p>
	 * See {@link Context#resource(String, Class)} for details about context resources availability conditions.
	 * </p>
	 * @return Optional Realm, empty if not available as context resource
	 */
	static Optional<Realm> getCurrent() {
		return Context.get().resource(CONTEXT_KEY, Realm.class);
	}

	/**
	 * Builder to create {@link Realm} instances.
	 */
	public interface Builder {

		/**
		 * Set Realm name
		 * @param name Realm name to set
		 * @return this
		 */
		Builder name(String name);

		/**
		 * Register given {@link Authenticator} in Realm.
		 * <p>
		 * Registered Authenticators will be inspected in the order they were added to Realm to find the first one which
		 * is consistent with the {@link AuthenticationToken} type to process during authentication operations performed
		 * using {@link Realm#authenticate(AuthenticationToken)}.
		 * </p>
		 * @param <T> Authentication token type
		 * @param authenticator Authenticator to add
		 * @return this
		 */
		<T extends AuthenticationToken> Builder authenticator(Authenticator<T> authenticator);

		/**
		 * Add an {@link AuthenticationTokenResolver} to translate {@link Message}s into {@link AuthenticationToken}s.
		 * @param authenticationTokenResolver Resolver to add
		 * @return this
		 */
		Builder resolver(AuthenticationTokenResolver<?> authenticationTokenResolver);

		/**
		 * Register given {@link Authorizer} in Realm.
		 * <p>
		 * Registered Authorizers will be inspected in the order they were added to Realm to find the first one which is
		 * consistent with the {@link Permission} type to process during authorization checking performed using
		 * <code>isPermitted</code> methods.
		 * </p>
		 * @param <P> Permission type
		 * @param authorizer Authorizer to add
		 * @return this
		 */
		<P extends Permission> Builder authorizer(Authorizer<P> authorizer);

		/**
		 * Register the default {@link Authorizer}
		 * @return this
		 * @see Authorizer#create()
		 */
		Builder withDefaultAuthorizer();

		/**
		 * Register an AuthenticationListener for authentication events
		 * @param authenticationListener AuthenticationListener to register
		 * @return this
		 */
		Builder listener(AuthenticationListener authenticationListener);

		/**
		 * Build {@link Realm} instance
		 * @return Realm
		 */
		Realm build();

	}

}
