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
package com.holonplatform.auth.token;

import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.core.internal.utils.ConversionUtils;

/**
 * Basic {@link AuthenticationToken} to represent a generic account authentication. Account is identified by an id and a
 * secret (of String type).
 * <p>
 * This AuthenticationToken could for example represent the widely-used user authentication mechanism providing username
 * and password.
 * </p>
 * <p>
 * This token returns a String from {@link #getPrincipal()} representing account id and a byte[] from
 * {@link #getCredentials()} representing provided secret.
 * </p>
 * 
 * @since 5.0.0
 */
public class AccountCredentialsToken implements AuthenticationToken {

	private static final long serialVersionUID = 5688193606746720812L;

	/*
	 * Account id
	 */
	private String accountId;

	/*
	 * Account secret
	 */
	private byte[] secret;

	/**
	 * Constructor
	 */
	public AccountCredentialsToken() {
		this(null, null);
	}

	/**
	 * Constructor with account id and secret.
	 * @param accountId Account id
	 * @param secret Secret (for example a password)
	 */
	public AccountCredentialsToken(String accountId, String secret) {
		super();
		this.accountId = accountId;
		this.secret = ConversionUtils.toBytes(secret);
	}

	/**
	 * Set account id
	 * @param accountId the account id to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * Set provided secret
	 * @param secret the secret to set
	 */
	public void setSecret(byte[] secret) {
		this.secret = secret;
	}

	/**
	 * Set provided secret as String
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = ConversionUtils.toBytes(secret);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationToken#getPrincipal()
	 */
	@Override
	public Object getPrincipal() {
		return accountId;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationToken#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return secret;
	}

}
