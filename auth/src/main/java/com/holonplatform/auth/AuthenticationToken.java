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

import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.internal.BasicAuthenticationTokenResolver;
import com.holonplatform.auth.internal.BearerAuthenticationTokenResolver;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.auth.token.BearerAuthenticationToken;
import com.holonplatform.core.messaging.Message;
import com.holonplatform.http.HttpHeaders;
import com.holonplatform.http.HttpRequest;

/**
 * Represents the token for an authentication request, to be processed by
 * {@link Authenticator#authenticate(AuthenticationToken)} to obtain a valid {@link Authentication} for a principal (for
 * example a user).
 * 
 * <p>
 * Because applications represent user data and credentials in different ways, implementations of this interface are
 * application-specific.
 * </p>
 * 
 * @since 5.0.0
 */
public interface AuthenticationToken extends CredentialsContainer, Serializable {

	/**
	 * Get the principal to which this authentication token refers, i.e. the account identity submitted during the
	 * authentication process.
	 * @return Principal to authenticate (for example a username)
	 */
	Object getPrincipal();

	/**
	 * Returns the credentials submitted during the authentication process that verifies the submitted
	 * {@link #getPrincipal()} account identity.
	 * @return the credential submitted by the user during the authentication process
	 */
	@Override
	Object getCredentials();

	/**
	 * Create a basic {@link AuthenticationToken} to represent a generic account authentication. Account is identified
	 * by an id and a secret (of String type).
	 * <p>
	 * This AuthenticationToken could for example represent the widely-used user authentication mechanism providing
	 * username and password.
	 * </p>
	 * @param accountId Account id
	 * @param secret Secret (for example a password)
	 * @return AuthenticationToken
	 */
	static AuthenticationToken accountCredentials(String accountId, String secret) {
		return new AccountCredentialsToken(accountId, secret);
	}

	/**
	 * Create an {@link AuthenticationToken} to represent a {@link HttpHeaders#SCHEME_BEARER} token authentication.
	 * @param token Bearer token value
	 * @return AuthenticationToken
	 */
	static AuthenticationToken bearer(String token) {
		return new BearerAuthenticationToken(token);
	}

	// Resolvers

	/**
	 * Build a default {@link AuthenticationTokenResolver} for HTTP BASIC authentication scheme.
	 * @return BASIC {@link AuthenticationTokenResolver}
	 */
	static AuthenticationTokenResolver<HttpRequest> httpBasicResolver() {
		return new BasicAuthenticationTokenResolver();
	}

	/**
	 * Build a default {@link AuthenticationTokenResolver} for HTTP BEARER authentication scheme.
	 * @return BEARER {@link AuthenticationTokenResolver}
	 */
	static AuthenticationTokenResolver<HttpRequest> httpBearerResolver() {
		return new BearerAuthenticationTokenResolver();
	}

	/**
	 * Resolver to obtain an {@link AuthenticationToken} from an authentication request using a generic {@link Message}.
	 * @param <R> Concrete authentication request message
	 */
	@SuppressWarnings("rawtypes")
	public interface AuthenticationTokenResolver<R extends Message> {

		/**
		 * Get supported message type
		 * @return Message type
		 */
		Class<? extends Message> getMessageType();

		/**
		 * Optional authentication scheme
		 * @return Authentication scheme name, empty if not available
		 */
		Optional<String> getScheme();

		/**
		 * Parse given <code>request</code> message and try to obtain an {@link AuthenticationToken} if request is
		 * eligible for this resolver authentication mechanism.
		 * @param request Authentication request
		 * @return AuthenticationToken, or an empty Optional if request is not eligible for this resolver authentication
		 *         mechanism
		 * @throws AuthenticationException Id resolver recognized given request but it is somehow malformed or
		 *         incomplete
		 */
		Optional<AuthenticationToken> getAuthenticationToken(R request) throws AuthenticationException;

	}

}
