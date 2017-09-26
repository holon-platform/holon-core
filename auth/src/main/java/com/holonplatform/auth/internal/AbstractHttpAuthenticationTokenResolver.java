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

import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.core.messaging.Message;
import com.holonplatform.http.HttpRequest;

/**
 * Base HTTP {@link AuthenticationTokenResolver} implementation, using a {@link HttpRequest} message type.
 *
 * @since 5.0.0
 */
public abstract class AbstractHttpAuthenticationTokenResolver implements AuthenticationTokenResolver<HttpRequest> {

	/*
	 * Authentication scheme
	 */
	private final String scheme;

	/**
	 * Constructor
	 * @param scheme Authentication scheme
	 */
	public AbstractHttpAuthenticationTokenResolver(String scheme) {
		super();
		this.scheme = scheme;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationTokenResolver#getScheme()
	 */
	@Override
	public Optional<String> getScheme() {
		return Optional.ofNullable(scheme);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationTokenResolver#getMessageType()
	 */
	@Override
	public Class<? extends Message<?, ?>> getMessageType() {
		return HttpRequest.class;
	}

}
