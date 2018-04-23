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
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.holonplatform.auth.Authentication;
import com.holonplatform.spring.security.SpringSecurityAuthentication;
import com.holonplatform.spring.security.SpringSecurityAuthenticationToken;
import com.holonplatform.spring.security.internal.SpringSecurityAuthenticationAdapter;

public class TestAuthentication {

	@Test
	public void testAuthenticationToken() {

		UsernamePasswordAuthenticationToken at1 = new UsernamePasswordAuthenticationToken("usr", "pwd",
				Collections.singleton(new SimpleGrantedAuthority("myrole")));

		SpringSecurityAuthenticationToken st1 = SpringSecurityAuthenticationToken.create(at1);

		assertEquals(at1, st1.getAuthentication());
		assertEquals(at1.getPrincipal(), st1.getPrincipal());
		assertEquals(at1.getCredentials(), st1.getCredentials());

	}

	@Test
	public void testAuthentication() {

		UsernamePasswordAuthenticationToken at1 = new UsernamePasswordAuthenticationToken("usr", "pwd",
				Collections.singleton(new SimpleGrantedAuthority("myrole")));
		at1.setDetails("testdetails");

		SpringSecurityAuthentication sa1 = SpringSecurityAuthentication.create(at1);
		
		assertEquals(at1.getPrincipal(), sa1.getPrincipal());
		assertEquals(at1.getCredentials(), sa1.getCredentials());
		
		assertEquals("usr", sa1.getName());
		assertNotNull(sa1.getAuthorities());
		assertEquals(1, sa1.getAuthorities().size());
		assertEquals("myrole", sa1.getAuthorities().iterator().next().getAuthority());
		assertEquals(1, sa1.getPermissions().size());
		assertEquals("myrole", sa1.getPermissions().iterator().next().getPermission().orElse(null));
		assertTrue(sa1.getParameter(SpringSecurityAuthentication.AUTHENTICATION_DETAILS_KEY).isPresent());
		assertEquals("testdetails", sa1.getParameter(SpringSecurityAuthentication.AUTHENTICATION_DETAILS_KEY).get());
	}
	
	@Test
	public void testAuthenticationAdapter() {
		
		final Authentication a = Authentication.builder("myname").permission("myrole").build();
		
		org.springframework.security.core.Authentication sa = new SpringSecurityAuthenticationAdapter(a);
		
		assertEquals("myname", sa.getName());
		assertNotNull(sa.getAuthorities());
		assertEquals(1, sa.getAuthorities().size());
		assertEquals("myrole", sa.getAuthorities().iterator().next().getAuthority());
		
	}

}
