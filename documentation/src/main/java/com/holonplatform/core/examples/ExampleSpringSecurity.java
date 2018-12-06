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

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.holonplatform.auth.Account;
import com.holonplatform.auth.Account.AccountProvider;
import com.holonplatform.auth.AuthContext;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.spring.security.SpringSecurity;
import com.holonplatform.spring.security.SpringSecurityAuthenticationToken;

@SuppressWarnings("unused")
public class ExampleSpringSecurity {

	public void authContext1() {
		// tag::authcontext1[]
		AuthContext authContext = SpringSecurity.authContext(); // <1>

		UsernamePasswordAuthenticationToken tkn = new UsernamePasswordAuthenticationToken("user", "pwd",
				Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("role1") }));
		SecurityContextHolder.getContext().setAuthentication(tkn); // <2>

		Authentication authc = authContext.requireAuthentication(); // <3>

		String name = authc.getName(); // <4>

		boolean permitted = authContext.isPermitted("role1"); // <5>

		SecurityContextHolder.getContext().setAuthentication(null); // <6>
		boolean notAnymore = authContext.isAuthenticated();
		// end::authcontext1[]
	}

	public void authContext2() {
		// tag::authcontext2[]

		final Realm realm = Realm.builder().withDefaultAuthorizer().withAuthenticator(Account.authenticator(id -> { // <1>
			if ("usr".equals(id)) {
				return Optional.of(Account.builder(id).credentials(Credentials.builder().secret("pwd").build())
						.withPermission("role1").build());
			}
			return Optional.empty();
		})).build();

		AuthContext authContext = SpringSecurity.authContext(realm); // <2>

		authContext.authenticate(Account.accountCredentialsToken("usr", "pwd")); // <3>

		org.springframework.security.core.Authentication authc = SecurityContextHolder.getContext().getAuthentication(); // <4>

		String name = authc.getName(); // <5>
		Collection<? extends GrantedAuthority> authorities = authc.getAuthorities(); // <6>
		// end::authcontext2[]
	}

	public void token() {
		// tag::token[]
		SpringSecurityAuthenticationToken token = SpringSecurityAuthenticationToken
				.create(new UsernamePasswordAuthenticationToken("usr", "pwd")); // <1>

		token = SpringSecurity.asAuthenticationToken(new UsernamePasswordAuthenticationToken("usr", "pwd")); // <2>
		// end::token[]
	}

	public void authenticator1() {
		// tag::authenticator1[]
		AuthenticationManager authenticationManager = getAuthenticationManager(); // <1>

		Authenticator<SpringSecurityAuthenticationToken> authenticator = SpringSecurity
				.authenticator(authenticationManager); // <2>
		// end::authenticator1[]
	}

	public void authenticator2() {
		// tag::authenticator2[]
		AuthenticationManager authenticationManager = getAuthenticationManager(); // <1>

		Realm realm = Realm.builder().withDefaultAuthorizer()
				.withAuthenticator(SpringSecurity.authenticator(authenticationManager)) // <2>
				.build();

		Authentication authc = realm.authenticate(SpringSecurityAuthenticationToken.account("user", "pwd1")); // <3>
		// end::authenticator2[]
	}

	public void authContext3() {
		// tag::authcontext3[]
		AuthenticationManager authenticationManager = getAuthenticationManager(); // <1>

		AuthContext authContext = SpringSecurity.authContext(authenticationManager); // <2>

		authContext = SpringSecurity.authContext(authenticationManager, true); // <3>
		// end::authcontext3[]
	}

	// tag::authenticator3[]
	@Configuration
	@EnableGlobalAuthentication
	class Config {

		@Bean
		public AccountProvider accountProvider() { // <1>
			return id -> {
				if ("usr1".equals(id)) {
					return Optional.of(Account.builder(id).credentials(Credentials.builder().secret("pwd1").build())
							.withPermission("view").build());
				}
				if ("usr2".equals(id)) {
					return Optional.of(Account.builder(id).credentials(Credentials.builder().secret("pwd2").build())
							.withPermission("view").withPermission("manage").build());
				}
				return Optional.empty();
			};
		}

		@Bean
		public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth,
				AccountProvider accountProvider) throws Exception { // <2>
			return auth.authenticationProvider( // <3>
					SpringSecurity.authenticationProvider(Account.authenticator(accountProvider), // <4>
							UsernamePasswordAuthenticationToken.class, // <5>
							upt -> AccountCredentialsToken.create(upt.getPrincipal().toString(),
									upt.getCredentials().toString()) // <6>
					)).build();
		}

	}
	// end::authenticator3[]

	public void permissions() {
		// tag::permissions[]
		GrantedAuthority ga = new SimpleGrantedAuthority("role1");
		Permission permission = SpringSecurity.asPermission(ga); // <1>

		Permission p = Permission.create("role2");
		GrantedAuthority grantedAuthority = SpringSecurity.asAuthority(p); // <2>
		// end::permissions[]
	}

	private static AuthenticationManager getAuthenticationManager() {
		return null;
	}

}
