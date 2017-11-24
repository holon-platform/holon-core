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

import java.util.List;
import java.util.function.Function;

import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.DisabledAccountException;
import com.holonplatform.auth.exceptions.ExpiredCredentialsException;
import com.holonplatform.auth.exceptions.InvalidCredentialsException;
import com.holonplatform.auth.exceptions.LockedAccountException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.auth.exceptions.UnsupportedTokenException;
import com.holonplatform.auth.internal.DefaultCallbackAuthenticator;
import com.holonplatform.core.messaging.Message;

/**
 * Authenticator is responsible for authenticating accounts using an {@link AuthenticationToken}.
 * <p>
 * Every Authenticator declares supported {@link AuthenticationToken} type though {@link #getTokenType()} method.
 * </p>
 * 
 * @param <T> Support token type
 * 
 * @since 5.0.0
 */
public interface Authenticator<T extends AuthenticationToken> {

	/**
	 * Get supported {@link AuthenticationToken} type
	 * @return Supported {@link AuthenticationToken} type
	 */
	Class<? extends T> getTokenType();

	/**
	 * Attempts to perform authentication using given {@link AuthenticationToken}.
	 * <p>
	 * If the authentication is successful, an {@link Authentication} instance is returned that represents the user's
	 * account data and provides authorization operations.
	 * </p>
	 * <p>
	 * If authentication is not successful, a suitable exception should be thrown. See the specific exceptions listed
	 * below for builtin available authentication errors.
	 * </p>
	 * @param authenticationToken the authentication request token
	 * @return the Authentication that represents principal's account and authorization data
	 * @throws AuthenticationException Authentication failed
	 * 
	 * @see DisabledAccountException
	 * @see LockedAccountException
	 * @see UnknownAccountException
	 * @see ExpiredCredentialsException
	 * @see InvalidCredentialsException
	 * @see UnsupportedTokenException
	 * @see UnexpectedAuthenticationException
	 */
	Authentication authenticate(T authenticationToken) throws AuthenticationException;

	/**
	 * Create an {@link Authenticator} bound to the given token type and which uses the provided callback
	 * {@link Function} to perform the authentication strategy. See {@link #authenticate(AuthenticationToken)}.
	 * @param tokenType Authentication token type (not null)
	 * @param authenticationFunction Authentication strategy function (not null)
	 * @return The {@link Authenticator} instance
	 */
	static <T extends AuthenticationToken> Authenticator<T> create(Class<? extends T> tokenType,
			Function<T, Authentication> authenticationFunction) {
		return new DefaultCallbackAuthenticator<>(tokenType, authenticationFunction);
	}

	/**
	 * Authenticator which support authentication using generic authentication request {@link Message}s.
	 * <p>
	 * Authentication is performed using {@link AuthenticationTokenResolver}s to parse request message into standard
	 * {@link AuthenticationToken}.
	 * </p>
	 */
	public interface MessageAuthenticator {

		/**
		 * Add an {@link AuthenticationTokenResolver} to translate messages into {@link AuthenticationToken}s.
		 * @param authenticationTokenResolver Resolver to add
		 */
		void addAuthenticationTokenResolver(AuthenticationTokenResolver<?> authenticationTokenResolver);

		/**
		 * Check whether this MessageAuthenticator supports given message type
		 * @param messageType Message type to check
		 * @return <code>true</code> if message type is supported
		 */
		boolean supportsMessage(Class<? extends Message<?, ?>> messageType);

		/**
		 * Get available {@link AuthenticationTokenResolver}s for given message type
		 * @param <T> Message type
		 * @param messageType Message type
		 * @return List of AuthenticationTokenResolvers, in the order they were registered, or an empty list if none
		 *         available
		 */
		@SuppressWarnings("rawtypes")
		<T extends Message> List<AuthenticationTokenResolver<T>> getResolversForMessageType(Class<T> messageType);

		/**
		 * Try to authenticate given <code>message</code> with the same contract as
		 * {@link Authenticator#authenticate(AuthenticationToken)}.
		 * <p>
		 * Message-based authentication is performed using a resolvers chain: all available resolvers for given message
		 * type are called in the order they where registered, and the first not null {@link AuthenticationToken}
		 * returned by a resolver is used for attempting authentication.
		 * </p>
		 * @param message Request message
		 * @param schemes Optional authentication schemes to use. If not null or empty, only
		 *        {@link AuthenticationTokenResolver}s bound to given scheme names will be used
		 * @return the Authentication that represents principal's account and authorization data
		 * @throws AuthenticationException Authentication failed
		 */
		Authentication authenticate(Message<?, ?> message, String... schemes) throws AuthenticationException;

	}

}
