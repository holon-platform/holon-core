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

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.holonplatform.auth.Permission;
import com.holonplatform.spring.security.SpringSecurity;
import com.holonplatform.spring.security.internal.PermissionGrantedAuthority;
import com.holonplatform.spring.security.internal.SpringSecurityPermission;

public class TestPermission {

	@Test
	public void testPermission() {

		final GrantedAuthority ga1 = new SimpleGrantedAuthority("ga1");
		assertEquals("ga1", ga1.getAuthority());

		Permission p1 = new SpringSecurityPermission(ga1);
		assertEquals("ga1", p1.getPermission().orElse(null));

		p1 = SpringSecurity.asPermission(ga1);
		assertEquals("ga1", p1.getPermission().orElse(null));

		assertEquals("ga1", p1.toString());

	}

	@Test
	public void testAuthority() {

		final Permission p1 = Permission.create("p1");

		GrantedAuthority ga1 = new PermissionGrantedAuthority(p1);
		assertEquals("p1", ga1.getAuthority());
		assertEquals("p1", ga1.toString());

		ga1 = SpringSecurity.asAuthority(p1);
		assertEquals("p1", ga1.getAuthority());
		assertEquals("p1", ga1.toString());

	}

}
