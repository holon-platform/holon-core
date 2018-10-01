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
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Authorizer;
import com.holonplatform.auth.Permission;
import com.holonplatform.core.internal.utils.TestUtils;

public class TestAuthz {

	@Test
	public void testPermission() {

		Permission p1 = Permission.create("p1");
		assertEquals("p1", p1.getPermission().orElse(null));

		Permission p2 = Permission.create("p2");

		Permission p3 = Permission.create("p1");

		assertEquals(p1, p1);
		assertEquals(p1, p3);
		assertEquals(p1.hashCode(), p3.hashCode());

		assertNotEquals(p1, null);
		assertNotEquals(p1, "x");

		assertNotEquals(p1, p2);

		assertNotNull(p1.toString());

		Set<Permission> permissions = new HashSet<>(3);
		permissions.add(p1);
		permissions.add(p2);
		permissions.add(p3);

		Authentication auth = Authentication.builder("test").permission(p1).permission(p2).permission(p3).build();

		assertTrue(Arrays.equals(permissions.toArray(new Permission[0]),
				auth.getPermissions().toArray(new Permission[0])));

		TestUtils.expectedException(IllegalArgumentException.class, () -> Permission.create(null));

	}

	@Test
	public void testAuthorizer() {
		Permission p1 = Permission.create("p1");
		Permission p2 = Permission.create("p2");

		Permission p3 = Permission.create("p3");

		Set<Permission> permissions = new HashSet<>(2);
		permissions.add(p1);
		permissions.add(p2);

		Authentication authc = Authentication.builder("test").permission(p1).permission(p2).build();

		Authorizer<Permission> authz = Authorizer.create();

		assertEquals(Permission.class, authz.getPermissionType());

		assertFalse(authz.isPermitted(null, permissions));
		assertFalse(authz.isPermitted(null, p1));

		assertFalse(authz.isPermitted(authc, (Permission[]) null));

		assertTrue(authz.isPermitted(authc, permissions));
		assertTrue(authz.isPermitted(authc, p1));
		assertTrue(authz.isPermitted(authc, p1, p2));
		assertTrue(authz.isPermittedAny(authc, p1, p2));
		assertTrue(authz.isPermittedAny(authc, permissions));
		assertTrue(authz.isPermittedAny(authc, p2));

		assertTrue(authz.isPermittedAny(authc, p1, p3));

		assertFalse(authz.isPermitted(authc, p3));
		assertFalse(authz.isPermitted(authc, p1, p3));

		assertTrue(authz.isPermitted(authc, "p1"));
		assertTrue(authz.isPermitted(authc, "p1", "p2"));
		assertTrue(authz.isPermittedAny(authc, "p1", "p3"));

		// root

		Authentication root = Authentication.builder("admin").root(true).build();

		assertTrue(authz.isPermitted(root, permissions));
		assertTrue(authz.isPermitted(root, p1));
		assertTrue(authz.isPermitted(root, p1, p2));
		assertTrue(authz.isPermittedAny(root, p1, p2));
		assertTrue(authz.isPermittedAny(root, permissions));
		assertTrue(authz.isPermittedAny(root, p2));

	}

}
