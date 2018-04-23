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
package com.holonplatform.spring.security.internal;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.core.internal.utils.ConversionUtils;

/**
 * An {@link Authenticator} bound to the default {@link AccountCredentialsToken} which uses Spring Security
 * {@link AuthenticationManager} to perform the authentication operations.
 *
 * @since 5.1.0
 */
public class AccountCredentialsAuthenticationManagerAuthenticator
		extends AbstractAuthenticationManagerAuthenticator<AccountCredentialsToken> {

	/**
	 * Constructor.
	 * @param authenticationManager Spring Security {@link AuthenticationManager} (not null)
	 */
	public AccountCredentialsAuthenticationManagerAuthenticator(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authenticator#getTokenType()
	 */
	@Override
	public Class<? extends AccountCredentialsToken> getTokenType() {
		return AccountCredentialsToken.class;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.test.springsecurity.integration.AbstractAuthenticationManagerAuthenticator#getAuthentication(
	 * com.holonplatform.auth.AuthenticationToken)
	 */
	@Override
	protected Authentication getAuthentication(AccountCredentialsToken authenticationToken) {
		byte[] credentialsBytes = toBytes(authenticationToken.getCredentials());
		return new UsernamePasswordAuthenticationToken(authenticationToken.getPrincipal(),
				(credentialsBytes != null) ? new String(credentialsBytes) : null);
	}

	/**
	 * Convert the given Object into a byte array.
	 * @param o the Object to convert into a byte array
	 * @return a byte array representation of the Object, or <code>null</code> if the object was <code>null</code>
	 */
	public static byte[] toBytes(Object o) {
		if (o != null) {
			if (o instanceof byte[]) {
				return (byte[]) o;
			}
			if (o instanceof char[]) {
				return ConversionUtils.toBytes((char[]) o);
			}
			if (o instanceof Credentials) {
				return ((Credentials) o).getSecret();
			}
			if (o instanceof String) {
				return ConversionUtils.toBytes((String) o);
			}
		}
		return null;
	}

}
