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
package com.holonplatform.spring.security.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.holonplatform.auth.Account;
import com.holonplatform.auth.Account.AccountProvider;
import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.spring.security.SpringSecurity;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestAuthenticationProvider.Config.class)
public class TestAuthenticationProvider {

	@Configuration
	@EnableGlobalAuthentication
	protected static class Config {

		@Bean
		public AccountProvider accountProvider() {
			return id -> {
				if ("u1".equals(id)) {
					return Optional.of(Account.builder(id).credentials(Credentials.builder().secret(id).build())
							.withPermission("view").build());
				}
				if ("u2".equals(id)) {
					return Optional.of(Account.builder(id).credentials(Credentials.builder().secret(id).build())
							.withPermission("view").withPermission("manage").build());
				}
				if ("u3".equals(id)) {
					return Optional.of(Account.builder(id).credentials(Credentials.builder().secret(id).build())
							.locked(true).build());
				}
				if ("u4".equals(id)) {
					return Optional.of(Account.builder(id).credentials(Credentials.builder().secret(id).build())
							.enabled(false).build());
				}
				return Optional.empty();
			};
		}

		@Bean
		@Primary
		public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth,
				AccountProvider accountProvider) throws Exception {
			return auth
					.authenticationProvider(
							SpringSecurity.authenticationProvider(Account.authenticator(accountProvider),
									UsernamePasswordAuthenticationToken.class, upt -> AccountCredentialsToken
											.create(upt.getPrincipal().toString(), upt.getCredentials().toString())))
					.build();
		}

	}

	@Autowired
	private AuthenticationManager authenticationManager;

	@Test
	public void testAuthenticator() {

		Authentication authc = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("u1", "u1"));

		assertNotNull(authc);
		assertEquals("u1", authc.getName());
		assertEquals(1, authc.getAuthorities().size());
		assertEquals("view", authc.getAuthorities().iterator().next().getAuthority());

		authc = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("u2", "u2"));

		assertNotNull(authc);
		assertEquals("u2", authc.getName());
		assertEquals(2, authc.getAuthorities().size());
	}

	@Test
	public void testAuthenticatorExceptions() {

		assertThrows(UsernameNotFoundException.class,
				() -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("ux", "ux")));

		assertThrows(BadCredentialsException.class,
				() -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("u1", "xxx")));

		assertThrows(LockedException.class,
				() -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("u3", "u3")));

		assertThrows(DisabledException.class,
				() -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("u4", "u4")));

	}

}
