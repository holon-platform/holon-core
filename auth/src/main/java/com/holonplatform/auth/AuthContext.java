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

import java.util.Optional;

import com.holonplatform.auth.Authentication.AuthenticationListener;
import com.holonplatform.auth.Authentication.AuthenticationNotifier;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.internal.DefaultAuthContext;
import com.holonplatform.core.Context;
import com.holonplatform.core.messaging.Message;

/**
 * Represents the current authentication/authorization context.
 * <p>
 * As an {@link AuthenticationInspector}, provides methods to check if an authentication is currently available and
 * obtain the {@link Authentication} reference.
 * </p>
 * <p>
 * Additionally, provides methods to perform authentication operations using an {@link AuthenticationToken}. As an
 * {@link AuthenticationNotifier}, supports {@link AuthenticationListener} registration to be notified when an
 * authentication request is successful, providing the {@link Authentication} reference.
 * </p>
 * <p>
 * An AuthContext is tipically bound to a {@link Realm}, to which authentication operations and permission control
 * strategies are delegated. In such situation, the authentication and authorization semantics are the same as those
 * implemented by the {@link Realm} through the registered {@link Authenticator}s and {@link Authorizer}s. The
 * {@link #create(Realm)} static methods can be used to create a default {@link AuthContext} instance using a
 * {@link Realm}. The {@link AuthenticationHolder} interface can be used to customize how the {@link Authentication}
 * reference is managed by the AuthContext.
 * </p>
 * 
 * @since 3.0.0
 */
public interface AuthContext extends AuthenticationInspector, AuthenticationNotifier {

	/**
	 * Default {@link Context} resource reference
	 */
	public static final String CONTEXT_KEY = AuthContext.class.getName();

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

	// ------- Context resource

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

	// ------- Builders

	/**
	 * Create a default {@link AuthContext} using given <code>realm</code>.
	 * @param realm The {@link Realm} which acts as {@link Authenticator} and {@link Authorizer} (not null)
	 * @return A new {@link AuthContext} instance
	 */
	static AuthContext create(Realm realm) {
		return new DefaultAuthContext(realm);
	}

	/**
	 * Create a default {@link AuthContext} using given <code>realm</code> and a custom {@link AuthenticationHolder} to
	 * handle the current {@link Authentication} reference.
	 * @param realm The {@link Realm} Realm which acts as {@link Authenticator} and {@link Authorizer} (not null)
	 * @param authenticationHolder The {@link AuthenticationHolder} to which the current {@link Authentication} handling
	 *        is delegated (not null)
	 * @return A new {@link AuthContext} instance
	 */
	static AuthContext create(Realm realm, AuthenticationHolder authenticationHolder) {
		return new DefaultAuthContext(realm, authenticationHolder);
	}

	/**
	 * Auth context {@link Authentication} holder, which handles the current {@link Authentication} reference on behalf
	 * of the Auth context.
	 * 
	 * @since 5.1.0
	 */
	public interface AuthenticationHolder {

		/**
		 * Get the current {@link Authentication}.
		 * @return Optional {@link Authentication}, empty if not present
		 */
		Optional<Authentication> getAuthentication();

		/**
		 * Set the current {@link Authentication}.
		 * @param authentication The authentication to set (may be null)
		 */
		void setAuthentication(Authentication authentication);

	}

}
