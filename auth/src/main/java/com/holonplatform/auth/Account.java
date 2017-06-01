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
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.auth.internal.AccountAuthenticator;
import com.holonplatform.auth.internal.DefaultAccount;
import com.holonplatform.auth.token.AccountCredentialsToken;

/**
 * Provides account informations for authentication and authorization
 * 
 * @since 5.0.0
 * 
 * @see AccountProvider
 */
public interface Account extends CredentialsContainer, Serializable {

	/**
	 * Account id (required). For example, the <code>username</code> if account represents a user.
	 * @return Account id
	 */
	String getId();

	/**
	 * Account stored credentials data
	 * @return Account credentials
	 */
	@Override
	Object getCredentials();

	/**
	 * Whether account is <code>root</code>, i.e. has any permission. If this method returns true,
	 * {@link #getPermissions()} is ignored.
	 * @return <code>true</code> if this account is a <code>root</code> account
	 */
	boolean isRoot();

	/**
	 * Optional account details (e.g. user name, surname, language etc.)
	 * @return Account details key-value map, an empty map if none
	 */
	Map<String, Object> getDetails();

	/**
	 * Optional permissions granted to account
	 * @return Account permissions, an empty collection if none
	 */
	Collection<Permission> getPermissions();

	/**
	 * Indicates whether this account is enabled
	 * @return <code>true</code> if enabled
	 */
	boolean isEnabled();

	/**
	 * Indicates whether this account is locked
	 * @return <code>true</code> if locked
	 */
	boolean isLocked();

	/**
	 * Indicates whether this account has expired
	 * @return <code>true</code> if expired
	 */
	boolean isExpired();

	/**
	 * Builder to create {@link Account} instances
	 * @param accountId Account id (not null)
	 * @return Builder
	 */
	static Builder builder(String accountId) {
		return new DefaultAccount.AccountBuilder(accountId);
	}

	/**
	 * Build an {@link Authenticator} using {@link AccountProvider} to load {@link Account} data and accepting
	 * {@link AccountCredentialsToken} as credentials.
	 * <p>
	 * Default CredentialsMatcher is used for AuthenticationToken credentials validation.
	 * </p>
	 * @param accountProvider {@link AccountProvider} to load {@link Account} data
	 * @return Authenticator
	 */
	static Authenticator<AccountCredentialsToken> authenticator(AccountProvider accountProvider) {
		return new AccountAuthenticator(accountProvider, CredentialsContainer.defaultMatcher());
	}

	/**
	 * Build an {@link Authenticator} using {@link AccountProvider} to load {@link Account} data and accepting
	 * {@link AccountCredentialsToken} as credentials.
	 * @param accountProvider {@link AccountProvider} to load {@link Account} data
	 * @param credentialsMatcher {@link CredentialsMatcher} to use for AuthenticationToken credentials validation
	 * @return Authenticator
	 */
	static Authenticator<AccountCredentialsToken> authenticator(AccountProvider accountProvider,
			CredentialsMatcher credentialsMatcher) {
		return new AccountAuthenticator(accountProvider, credentialsMatcher);
	}

	/**
	 * Builder to create {@link Account} instances.
	 */
	public interface Builder {

		/**
		 * Set whether Account is considered a <code>root</code> principal, for which permission checking is always
		 * skipped, assuming that any permission is granted.
		 * @param root <code>true</code> to set Account as <code>root</code> principal
		 * @return this
		 */
		Builder root(boolean root);

		/**
		 * Set Account details. Any previously setted detail will be discarded.
		 * @param details the details to set
		 * @return this
		 */
		Builder details(Map<String, Object> details);

		/**
		 * Add (or replace if given key already exists) an Account detail
		 * @param key Key
		 * @param value Value
		 * @return this
		 */
		Builder detail(String key, Object value);

		/**
		 * Set permissions granted to Account. Any previously setted Permission will be discarded.
		 * @param permissions the permissions to set
		 * @return this
		 */
		Builder permissions(Collection<Permission> permissions);

		/**
		 * Add a permission granted to Account
		 * @param permission Permission to add
		 * @return this
		 */
		Builder permission(Permission permission);

		/**
		 * Set permissions granted to Account using String representations. Any previously setted Permission will be
		 * discarded.
		 * @param permissions the permissions strings to set
		 * @return this
		 */
		Builder permissionStrings(Collection<String> permissions);

		/**
		 * Add a permission granted to Account using String representation.
		 * @param permission Permission string to add
		 * @return this
		 */
		Builder permission(String permission);

		/**
		 * Set whether is enabled (default is <code>true</code>)
		 * @param enabled <code>true</code> if enabled
		 * @return this
		 */
		Builder enabled(boolean enabled);

		/**
		 * Set whether is locked
		 * @param locked <code>true</code> if locked
		 * @return this
		 */
		Builder locked(boolean locked);

		/**
		 * Set whether has expired
		 * @param expired <code>true</code> if expired
		 * @return this
		 */
		Builder expired(boolean expired);

		/**
		 * Set Account credentials as a {@link Credentials} object
		 * @param credentials Credentials to set
		 * @return this
		 * @see Credentials#builder()
		 */
		Builder credentials(Credentials credentials);

		/**
		 * Set Account credentials
		 * @param credentials Credentials to set
		 * @return this
		 */
		Builder credentials(Object credentials);

		/**
		 * Build Account instance
		 * @return Account
		 */
		Account build();

	}

	/**
	 * Provider which can be used to load {@link Account} data.
	 */
	@FunctionalInterface
	public interface AccountProvider {

		/**
		 * Load an {@link Account} based on account <code>id</code>
		 * @param id Account id
		 * @return Account associated to given <code>id</code>, or an empty Optional if not found
		 */
		Optional<Account> loadAccountById(String id);

	}

}
