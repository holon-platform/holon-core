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
package com.holonplatform.core.examples;

import java.util.Optional;

import com.holonplatform.auth.Account;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.Account.AccountProvider;
import com.holonplatform.auth.Authentication;

@SuppressWarnings("unused")
public class ExampleAccount {

	public void builder() {
		// tag::builder[]
		Account.builder("accountId").enabled(true).locked(false).expired(false)
				.credentials(Credentials.builder().secret("pwd").hashAlgorithm(Credentials.Encoder.HASH_SHA_256)
						.base64Encoded().build()) // credentials
				.root(false) // not root (default)
				.permission("role1").permission("role2") // permissions
				.detail("name", "TheName") // principal name
				.detail("surname", "TheSurname") // principal surname
				.build();
		// end::builder[]
	}

	public void auth() {
		// tag::auth[]
		AccountProvider provider = id -> Optional.of(Account.builder(id).enabled(true)
				.credentials(Credentials.builder().secret("pwd").base64Encoded().build()).permission("role1").build()); // <1>

		Realm realm = Realm.builder().authenticator(Account.authenticator(provider)).withDefaultAuthorizer().build(); // <2>

		try {
			Authentication authc = realm.authenticate(AuthenticationToken.accountCredentials("test", "pwd")); // <3>
		} catch (AuthenticationException e) {
			// handle authentication failures
		}
		// end::auth[]
	}

}
