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

import java.util.function.Function;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * A default {@link Authenticator} implementation which uses a callback {@link Function} to perform the authentication
 * strategy.
 * 
 * @param <T> Authentication token type
 * 
 * @since 5.0.4
 *
 */
public class DefaultCallbackAuthenticator<T extends AuthenticationToken> implements Authenticator<T> {

	private final Class<? extends T> tokenType;
	private final Function<T, Authentication> callback;

	/**
	 * Constructor
	 * @param tokenType Authentication token type (not null)
	 * @param callback Authentication function (not null)
	 */
	public DefaultCallbackAuthenticator(Class<? extends T> tokenType, Function<T, Authentication> callback) {
		super();
		ObjectUtils.argumentNotNull(tokenType, "Token type must be not null");
		ObjectUtils.argumentNotNull(callback, "Authenticator callback must be not null");
		this.tokenType = tokenType;
		this.callback = callback;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authenticator#getTokenType()
	 */
	@Override
	public Class<? extends T> getTokenType() {
		return tokenType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authenticator#authenticate(com.holonplatform.auth.AuthenticationToken)
	 */
	@Override
	public Authentication authenticate(T authenticationToken) throws AuthenticationException {
		return callback.apply(authenticationToken);
	}

}
