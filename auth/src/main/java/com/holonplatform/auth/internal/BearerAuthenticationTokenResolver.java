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
package com.holonplatform.auth.internal;

import java.util.Optional;

import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.token.BearerAuthenticationToken;
import com.holonplatform.http.HttpHeaders;
import com.holonplatform.http.HttpRequest;

/**
 * OAuth2 {@link AuthenticationTokenResolver} for HTTP requests providing {@link HttpHeaders#SCHEME_BEARER}
 * authentication scheme using {@link HttpHeaders#AUTHORIZATION} header.
 *
 * @since 5.0.0
 */
public class BearerAuthenticationTokenResolver extends AbstractHttpAuthenticationTokenResolver {

	/**
	 * Constructor
	 */
	public BearerAuthenticationTokenResolver() {
		super(HttpHeaders.SCHEME_BEARER);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver#getAuthenticationToken(com.holonplatform
	 * .core.messaging.Message)
	 */
	@Override
	public Optional<AuthenticationToken> getAuthenticationToken(HttpRequest request) throws AuthenticationException {
		return request.getAuthorizationBearer().map(t -> new BearerAuthenticationToken(t));
	}

}
