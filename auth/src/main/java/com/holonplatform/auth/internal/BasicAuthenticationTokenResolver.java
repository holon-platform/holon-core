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
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.http.HttpHeaders;
import com.holonplatform.http.HttpRequest;

/**
 * {@link AuthenticationTokenResolver} for HTTP requests providing {@link HttpHeaders#SCHEME_BASIC} authentication
 * scheme using {@link HttpHeaders#AUTHORIZATION} header.
 * 
 * <p>
 * Authorization header must provide a Base64 encoded String containing username and password separated by a colon.
 * </p>
 * 
 * @since 5.0.0
 */
public class BasicAuthenticationTokenResolver extends AbstractHttpAuthenticationTokenResolver {

	public BasicAuthenticationTokenResolver() {
		super(HttpHeaders.SCHEME_BASIC);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.http.AuthenticationTokenResolver#getAuthenticationToken(com.holonplatform.core.http.
	 * HttpRequest)
	 */
	@Override
	public Optional<AuthenticationToken> getAuthenticationToken(HttpRequest request) throws AuthenticationException {
		return request.getAuthorizationBasicCredentials().map(c -> new AccountCredentialsToken(c[0], c[1]));
	}

}
