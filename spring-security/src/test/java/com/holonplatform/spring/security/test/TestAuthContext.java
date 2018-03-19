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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.holonplatform.auth.AuthContext;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Realm;
import com.holonplatform.spring.security.SpringSecurity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAuthContext.Config.class)
public class TestAuthContext {

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
	public void testAuthContext() {

		final AuthContext ac = SpringSecurity.authContext(Realm.builder().withDefaultAuthorizer().build());

		UsernamePasswordAuthenticationToken tkn = new UsernamePasswordAuthenticationToken("user", "pwd",
				Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("role1") }));

		SecurityContextHolder.getContext().setAuthentication(tkn);

		assertTrue(ac.isAuthenticated());

		Authentication a = ac.getAuthentication().orElse(null);

		assertNotNull(a);

		assertEquals("user", a.getName());
		assertEquals(1, a.getPermissions().size());
		assertEquals("role1", a.getPermissions().iterator().next().getPermission().orElse(null));

		SecurityContextHolder.getContext().setAuthentication(null);

		assertFalse(ac.isAuthenticated());

	}

	@Test
	public void testAuthContextAuthenticators() {

		final AuthContext ac = SpringSecurity.authContext(authenticationManager);

		ac.authenticate(SpringSecurity.asAuthenticationToken("user", "pwd1"));

		assertTrue(ac.isAuthenticated());

		Authentication a = ac.getAuthentication().orElse(null);

		assertNotNull(a);

		assertEquals("user", a.getName());
		assertEquals(1, a.getPermissions().size());
		assertEquals("USER", a.getPermissions().iterator().next().getPermission().orElse(null));

	}

}
