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

import com.holonplatform.auth.Account;
import com.holonplatform.auth.Account.AccountProvider;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.CredentialsContainer;
import com.holonplatform.auth.CredentialsContainer.CredentialsMatcher;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.DisabledAccountException;
import com.holonplatform.auth.exceptions.ExpiredCredentialsException;
import com.holonplatform.auth.exceptions.InvalidCredentialsException;
import com.holonplatform.auth.exceptions.LockedAccountException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * {@link Authenticator} which uses an {@link AccountProvider} to load {@link Account} data for authentication and a
 * {@link CredentialsMatcher} for credentials validation.
 * 
 * <p>
 * This Authenticator supports {@link AccountCredentialsToken} token type.
 * </p>
 * 
 * @since 5.0.0
 */
public class AccountAuthenticator implements Authenticator<AccountCredentialsToken> {

	/*
	 * Account service to load Account data
	 */
	private final AccountProvider accountService;

	/*
	 * CredentialsMatcher to validate provided and account credentials
	 */
	private final CredentialsMatcher credentialsMatcher;

	/**
	 * Constructor using default {@link CredentialsMatcher}
	 * @param accountService Account service to load {@link Account} data (not null)
	 */
	public AccountAuthenticator(AccountProvider accountService) {
		this(accountService, CredentialsContainer.defaultMatcher());
	}

	/**
	 * Constructor
	 * @param accountService Account service to load {@link Account} data (not null)
	 * @param credentialsMatcher CredentialsMatcher to use for AuthenticationToken credentials validation
	 */
	public AccountAuthenticator(AccountProvider accountService, CredentialsMatcher credentialsMatcher) {
		super();
		ObjectUtils.argumentNotNull(accountService, "AccountService must be not null");
		this.accountService = accountService;
		this.credentialsMatcher = (credentialsMatcher != null) ? credentialsMatcher
				: CredentialsContainer.defaultMatcher();
	}

	/**
	 * Account service to load {@link Account} data
	 * @return Account service
	 */
	public AccountProvider getAccountService() {
		return accountService;
	}

	/**
	 * CredentialsMatcher to use for AuthenticationToken credentials validation
	 * @return CredentialsMatcher
	 */
	public CredentialsMatcher getCredentialsMatcher() {
		return credentialsMatcher;
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
	 * @see com.holonplatform.auth.Authenticator#authenticate(com.holonplatform.auth.AuthenticationToken)
	 */
	@Override
	public Authentication authenticate(AccountCredentialsToken authenticationToken) throws AuthenticationException {
		if (authenticationToken == null) {
			throw new UnexpectedAuthenticationException("Null AuthenticationToken");
		}

		// checkup
		if (getAccountService() == null) {
			throw new UnexpectedAuthenticationException("Missing AccountService");
		}

		// get principal
		String accountId = (String) authenticationToken.getPrincipal();
		if (accountId == null) {
			throw new InvalidCredentialsException("Null account id");
		}

		try {

			// load account

			Account account = accountService.loadAccountById(accountId)
					.orElseThrow(() -> new UnknownAccountException("Unknown account: " + accountId));

			if (!account.isEnabled()) {
				throw new DisabledAccountException("Account " + accountId + " is disabled");
			}

			if (account.isLocked()) {
				throw new LockedAccountException("Account " + accountId + " is locked");
			}

			if (account.isExpired()) {
				throw new ExpiredCredentialsException("Account " + accountId + " is expired");
			}

			// validate credentials
			if (!getCredentialsMatcher().credentialsMatch(authenticationToken, account)) {
				throw new InvalidCredentialsException("Invalid credentials");
			}

			Authentication.Builder authc = buildAuthentication(account).scheme("Basic"); // set the Basic scheme as
																							// default

			if (authc == null) {
				throw new UnexpectedAuthenticationException("Failed to build Authentication: null result");
			}

			processAuthentication(authc, account);

			return authc.build();

		} catch (AuthenticationException e) {
			throw e;
		} catch (Exception e) {
			throw new UnexpectedAuthenticationException("Failed to load Account", e);
		}

	}

	/**
	 * Get an {@link Authentication.Builder} instance to renturn from {@link #authenticate(AuthenticationToken)} method
	 * when account loading a credentials matching were successful.
	 * @param account Account data
	 * @return Authentication builder
	 */
	protected Authentication.Builder buildAuthentication(Account account) {
		return Authentication.builder(account.getId()).root(account.isRoot());
	}

	/**
	 * Process authentication to set data from {@link Account}. By default copy Account details and permissions into
	 * Authentication instance.
	 * @param authentication Authentication builder
	 * @param account Account data
	 */
	protected void processAuthentication(Authentication.Builder authentication, Account account) {
		account.getDetails().forEach((k, v) -> authentication.parameter(k, v));
		account.getPermissions().forEach(p -> authentication.permission(p));
	}

}
