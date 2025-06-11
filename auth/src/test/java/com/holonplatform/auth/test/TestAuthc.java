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
package com.holonplatform.auth.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.core.internal.utils.ConversionUtils;

public class TestAuthc {

	@Test
	public void testAuthentication() {

		Authentication a1 = Authentication.builder("a1").build();
		assertEquals("a1", a1.getName());
		assertFalse(a1.isRoot());

		Authentication a2 = Authentication.builder("a2").root(true).build();
		assertEquals("a2", a2.getName());
		assertTrue(a2.isRoot());

		assertEquals(a1, a1);
		assertNotEquals(a1, null);
		assertNotEquals(a1, "x");
		assertNotEquals(a1, a2);

		assertNotEquals(a1.hashCode(), a2.hashCode());

		Authentication a3 = Authentication.builder("a1").root(true).build();

		assertEquals(a1, a3);

		assertEquals(a1.hashCode(), a3.hashCode());

		assertNotNull(a1.toString());

		Authentication authc = Authentication.builder("test").withParameter("d1", "test")
				.withParameter("d2", Long.valueOf(1L)).build();

		assertTrue(authc.getParameter("d1", String.class).isPresent());
		assertEquals("test", authc.getParameter("d1", String.class).get());
		assertTrue(authc.getParameter("d2", long.class).isPresent());
		assertEquals(Long.valueOf(1), authc.getParameter("d2", long.class).get());
		assertFalse(authc.getParameter("xxx", Object.class).isPresent());

	}

	@Test
	public void testUPToken() {

		AuthenticationToken token = AuthenticationToken.accountCredentials("usr", "pwd");
		assertEquals("usr", token.getPrincipal());
		assertTrue(Arrays.equals(ConversionUtils.toBytes("pwd"), (byte[]) token.getCredentials()));

	}

}
