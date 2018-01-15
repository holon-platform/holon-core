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
import com.holonplatform.auth.AuthContext;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.AuthenticationException;

public class ExampleAuthContext {

	@SuppressWarnings("unused")
	public void authContext() {
		// tag::authctx[]
		AccountProvider provider = id -> Optional.of(Account.builder(id).enabled(true)
				.credentials(Credentials.builder().secret("pwd").base64Encoded().build()).permission("role1").build()); // <1>

		Realm realm = Realm.builder().authenticator(Account.authenticator(provider)).withDefaultAuthorizer().build(); // <2>

		AuthContext context = AuthContext.create(realm); // <3>

		context.addAuthenticationListener(
				a -> System.out.println((a != null) ? "Authenticated: " + a.getName() : "Unauthenticated")); // <4>

		try {

			context.authenticate(AuthenticationToken.accountCredentials("test", "pwd")); // <5>

			Authentication authc = context.getAuthentication()
					.orElseThrow(() -> new IllegalStateException("Context not authenticated")); // <6>

			boolean permitted = context.isPermitted("role1"); // <7>

			context.unauthenticate(); // <8>

		} catch (AuthenticationException e) {
			// handle authentication failures
		}
		// end::authctx[]
	}

}
