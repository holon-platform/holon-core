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

import java.util.Collection;
import java.util.Optional;

import com.holonplatform.auth.Authentication.AuthenticationListener;
import com.holonplatform.auth.Authentication.AuthenticationNotifier;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.internal.DefaultAuthContext;
import com.holonplatform.core.Context;
import com.holonplatform.core.messaging.Message;

/**
 * Represents current authentication/authorization context, typically bound to an application session or a service
 * request.
 * <p>
 * The AuthContext stores an {@link Authentication} obtained through an {@link Authenticator} using an
 * AuthenticationToken. The {@link #authenticate(AuthenticationToken)} method is provided to perform an authentication
 * operation. As opposite, the {@link #unauthenticate()} method removes any authentication informations from the
 * AuthContext.
 * </p>
 * <p>
 * The {@link #getAuthentication()} method can be used to check whether an Authentication is currently bound to
 * AuthContext and obtain it.
 * </p>
 * <p>
 * A list of authorization control methods are provided to perform access control decisions using current context
 * Authentication.
 * </p>
 * <p>
 * Extends {@link AuthenticationNotifier} to allow {@link AuthenticationListener} registration.
 * </p>
 * 
 * @since 3.0.0
 */
public interface AuthContext extends AuthenticationNotifier {

	/**
	 * Default {@link Context} resource reference
	 */
	public static final String CONTEXT_KEY = AuthContext.class.getName();

	/**
	 * Get current Authentication in this context.
	 * <p>
	 * Principals are authenticated using {@link #authenticate(AuthenticationToken)}. When authentication process is
	 * successful, an {@link Authentication} is bound to this AuthContext and can be obtained using this method.
	 * </p>
	 * 
	 * @return Optional Authentication, an empty Optional is returned if no {@link Authentication} is bound to this
	 *         AuthContext
	 */
	Optional<Authentication> getAuthentication();

	/**
	 * Gets whether this {@link AuthContext} is authenticated, i.e. an {@link Authentication} is available.
	 * @return <code>true</code> if this {@link AuthContext} is authenticated, <code>false</code> otherwise
	 */
	default boolean isAuthenticated() {
		return getAuthentication().isPresent();
	}

	/**
	 * Attempts to authenticate a user using given {@link AuthenticationToken}.
	 * <p>
	 * If the authentication is successful, an {@link Authentication} instance is stored in this AuthContext and can be
	 * retrieved using {@link #getAuthentication()}. That Authentication instance is also returned by this method.
	 * </p>
	 * <p>
	 * If authentication is not successful, a suitable exception is thrown.
	 * </p>
	 * 
	 * @param authenticationToken the authentication request token
	 * @return The {@link Authentication} instance result of authentication process
	 * @throws AuthenticationException Authentication failed
	 */
	Authentication authenticate(AuthenticationToken authenticationToken) throws AuthenticationException;

	/**
	 * Attempts to authenticate using given <code>message</code> with the same contract as
	 * {@link #authenticate(AuthenticationToken)}, using {@link Realm} registered {@link AuthenticationTokenResolver}s
	 * for given message type, if any.
	 * <p>
	 * If the authentication is successful, an {@link Authentication} instance is stored in this AuthContext and can be
	 * retrieved using {@link #getAuthentication()}. That Authentication instance is also returned by this method.
	 * </p>
	 * <p>
	 * If authentication is not successful, a suitable exception is thrown.
	 * </p>
	 * @param message Authentication request message
	 * @param schemes Optional authentication schemes to use. If not null or empty, only
	 *        {@link AuthenticationTokenResolver}s bound to given scheme names will be used
	 * @return he {@link Authentication} instance result of authentication process
	 * @throws AuthenticationException Authentication failed
	 */
	Authentication authenticate(Message<?, ?> message, String... schemes) throws AuthenticationException;

	/**
	 * Unbound current {@link Authentication} from this AuthContext, if any
	 * 
	 * @return Previous Authentication bound to this context, if any. An empty Optional otherwise.
	 */
	Optional<Authentication> unauthenticate();

	/**
	 * Check if current Authentication has all specified permission/s.
	 * <p>
	 * If no Authentication in bound to this context, always returns <code>false</code>.
	 * </p>
	 * 
	 * @param permissions Permissions to check
	 * @return <code>true</code> if current Authentication has all specified permission
	 */
	boolean isPermitted(Permission... permissions);

	/**
	 * Check if current Authentication has all specified permission/s using String permission form.
	 * <p>
	 * String permission match against Authentication {@link Permission}s will be performed using
	 * {@link Permission#getPermission()} method.
	 * </p>
	 * <p>
	 * If no Authentication in bound to this context, always returns <code>false</code>.
	 * </p>
	 * 
	 * @param permissions Permissions to check
	 * @return <code>true</code> if current Authentication has all specified permission
	 */
	boolean isPermitted(String... permissions);

	/**
	 * Check if current Authentication has any of specified permission/s
	 * <p>
	 * If no Authentication in bound to this context, always returns <code>false</code>.
	 * </p>
	 * 
	 * @param permissions Permissions to check
	 * @return <code>true</code> if current Authentication has any of specified permission
	 */
	boolean isPermittedAny(Permission... permissions);

	/**
	 * Check if current Authentication has any of specified permission/s using String permission form.
	 * <p>
	 * String permission match against Authentication {@link Permission}s will be performed using
	 * {@link Permission#getPermission()} method.
	 * </p>
	 * <p>
	 * If no Authentication in bound to this context, always returns <code>false</code>.
	 * </p>
	 * 
	 * @param permissions Permissions to check
	 * @return <code>true</code> if current Authentication has any of specified permission
	 */
	boolean isPermittedAny(String... permissions);

	/**
	 * Check if current Authentication has all specified permission/s using a Collection
	 * <p>
	 * If no Authentication in bound to this context, always returns <code>false</code>.
	 * </p>
	 * 
	 * @param permissions Permissions to check
	 * @return <code>true</code> if current Authentication has all specified permission
	 */
	boolean isPermitted(Collection<Permission> permissions);

	/**
	 * Check if current Authentication has any of specified permission/s using a Collection
	 * <p>
	 * If no Authentication in bound to this context, always returns <code>false</code>.
	 * </p>
	 * 
	 * @param permissions Permissions to check
	 * @return <code>true</code> if current Authentication has any of specified permission
	 */
	boolean isPermittedAny(Collection<Permission> permissions);

	// Context resource

	/**
	 * Convenience method to obtain the current {@link AuthContext} made available as {@link Context} resource, using
	 * default {@link ClassLoader}.
	 * <p>
	 * See {@link Context#resource(String, Class)} for details about context resources availability conditions.
	 * </p>
	 * @return Optional AuthContext, empty if not available as context resource
	 */
	static Optional<AuthContext> getCurrent() {
		return Context.get().resource(CONTEXT_KEY, AuthContext.class);
	}

	/**
	 * Requires the current {@link AuthContext}. If not available using {@link #getCurrent()}, an
	 * {@link IllegalStateException} is thrown.
	 * @return Current AuthContext
	 * @throws IllegalStateException AuthContext is not available as a {@link Context} resource
	 */
	static AuthContext require() {
		return getCurrent()
				.orElseThrow(() -> new IllegalStateException("AuthContext is not available as context resource"));
	}

	// Builder

	/**
	 * Create a default {@link AuthContext} using given <code>realm</code>.
	 * @param realm Realm which acts as {@link Authenticator} and {@link Authorizer}
	 * @return New {@link AuthContext} instance
	 */
	static AuthContext create(Realm realm) {
		return new DefaultAuthContext(realm);
	}

}
