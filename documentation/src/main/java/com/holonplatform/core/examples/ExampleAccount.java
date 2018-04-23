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
package com.holonplatform.core.examples;

import java.util.Optional;

import com.holonplatform.auth.Account;
import com.holonplatform.auth.Account.AccountProvider;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.CredentialsContainer;
import com.holonplatform.auth.CredentialsContainer.CredentialsMatcher;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.token.AccountCredentialsToken;

@SuppressWarnings("unused")
public class ExampleAccount {

	@SuppressWarnings("serial")
	private static class MyPermission implements Permission {

		@Override
		public Optional<String> getPermission() {
			return Optional.empty();
		}

	}

	public void builder() {
		// tag::builder[]
		Account.builder("accountId") // <1>
				.enabled(true) // <2>
				.locked(false) // <3>
				.expired(false) // <4>
				.credentials(Credentials.builder().secret("pwd").hashAlgorithm(Credentials.Encoder.HASH_SHA_256)
						.base64Encoded().build()) // <5>
				.root(false) // <6>
				.permission(new MyPermission()) // <7>
				.permission("role1") // <8>
				.detail("name", "TheName").detail("surname", "TheSurname") // <9>
				.build();
		// end::builder[]
	}

	public void provider() {
		// tag::provider[]
		AccountProvider accountProvider = accountId -> { // <1>
			if ("test".equals(accountId)) {
				return Optional.of(Account.builder(accountId)
						// configure account
						// ...
						.build());
			}
			return Optional.empty();
		};
		// end::provider[]
	}

	public void authenticator() {
		// tag::authenticator[]
		AccountProvider accountProvider = getAccountProvider(); // build or obtain the AccountProvider to use

		Authenticator<AccountCredentialsToken> authenticator = Account.authenticator(accountProvider); // <1>
		// end::authenticator[]
	}

	public void authenticator2() {
		// tag::authenticator2[]
		Authenticator<AccountCredentialsToken> authenticator = Account.authenticator(getAccountProvider(),
				new MyCredentialsMatcher() // <1>
		);
		// end::authenticator2[]
	}

	public void token() {
		// tag::token[]
		AuthenticationToken token = Account.accountCredentialsToken("accountId", "secret"); // <1>

		token = Account.accountCredentialsToken("accountId", new byte[] { 1, 2, 3 }); // <2>
		// end::token[]
	}

	private static class MyCredentialsMatcher implements CredentialsMatcher {

		@Override
		public boolean credentialsMatch(CredentialsContainer provided, CredentialsContainer stored)
				throws AuthenticationException {
			return true;
		}

	}

	private static AccountProvider getAccountProvider() {
		return null;
	}

	public void auth() {
		// tag::auth[]
		AccountProvider provider = id -> Optional.of(Account.builder(id).enabled(true)
				.credentials(Credentials.builder().secret("pwd").base64Encoded().build()).permission("role1").build()); // <1>

		Realm realm = Realm.builder() //
				.authenticator(Account.authenticator(provider)) // <2>
				.withDefaultAuthorizer().build();

		try {
			Authentication authc = realm.authenticate(AuthenticationToken.accountCredentials("test", "pwd")); // <3>
		} catch (AuthenticationException e) {
			// handle authentication failures
		}
		// end::auth[]
	}

}
