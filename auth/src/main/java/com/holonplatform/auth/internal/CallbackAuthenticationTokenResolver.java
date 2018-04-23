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

import java.util.Optional;
import java.util.function.Function;

import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.messaging.Message;

/**
 * An {@link AuthenticationTokenResolver} implementation which uses a callback function for message token resolution.
 *
 * @param <M> Message type
 *
 * @since 5.1.0
 */
@SuppressWarnings("rawtypes")
public class CallbackAuthenticationTokenResolver<M extends Message> implements AuthenticationTokenResolver<M> {

	private final Class<? extends Message> messageType;
	private final Function<M, Optional<AuthenticationToken>> resolverFunction;
	private final String scheme;

	/**
	 * Constructor.
	 * @param messageType Message type (not null)
	 * @param resolverFunction Resolver function (not null)
	 * @param scheme Optional authentication scheme
	 */
	public CallbackAuthenticationTokenResolver(Class<? extends Message> messageType,
			Function<M, Optional<AuthenticationToken>> resolverFunction, String scheme) {
		super();
		ObjectUtils.argumentNotNull(messageType, "Message type must be not null");
		ObjectUtils.argumentNotNull(resolverFunction, "Resolver function must be not null");
		this.messageType = messageType;
		this.resolverFunction = resolverFunction;
		this.scheme = scheme;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver#getMessageType()
	 */
	@Override
	public Class<? extends Message> getMessageType() {
		return messageType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver#getScheme()
	 */
	@Override
	public Optional<String> getScheme() {
		return Optional.ofNullable(scheme);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver#getAuthenticationToken(com.holonplatform.
	 * core.messaging.Message)
	 */
	@Override
	public Optional<AuthenticationToken> getAuthenticationToken(M request) throws AuthenticationException {
		return resolverFunction.apply(request);
	}

}
