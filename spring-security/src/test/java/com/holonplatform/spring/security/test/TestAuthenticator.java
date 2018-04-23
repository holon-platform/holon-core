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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.spring.security.SpringSecurity;
import com.holonplatform.spring.security.SpringSecurityAuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAuthenticator.Config.class)
public class TestAuthenticator {

	@Configuration
	@EnableGlobalAuthentication
	protected static class Config {

		@Bean
		@Primary
		public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
			return auth.inMemoryAuthentication().withUser("user").password("pwd1").authorities("USER").and()
					.withUser("admin").password("pwd2").authorities("USER", "ADMIN").and().and().build();
		}

	}

	@Autowired
	private AuthenticationManager authenticationManager;

	@Test
	public void testAuthenticator() {
		Authenticator<SpringSecurityAuthenticationToken> a = SpringSecurity.authenticator(authenticationManager);

		Authentication authc = a.authenticate(SpringSecurityAuthenticationToken.account("user", "pwd1"));

		assertNotNull(authc);
		assertEquals("user", authc.getName());
		assertEquals(1, authc.getPermissions().size());
		assertEquals("USER", authc.getPermissions().iterator().next().getPermission().orElse(null));
	}

	@Test
	public void testDefaultAuthenticator() {

		Authenticator<AccountCredentialsToken> a = SpringSecurity
				.accountCredentialsAuthenticator(authenticationManager);

		Authentication authc = a.authenticate(AccountCredentialsToken.create("user", "pwd1"));

		assertNotNull(authc);
		assertEquals("user", authc.getName());
		assertEquals(1, authc.getPermissions().size());
		assertEquals("USER", authc.getPermissions().iterator().next().getPermission().orElse(null));

	}

}
