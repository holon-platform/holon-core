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
package com.holonplatform.auth.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.auth.exceptions.UnsupportedTokenException;
import com.holonplatform.auth.internal.DefaultPermission;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.core.internal.utils.TestUtils;

public class TestRealm {

	@SuppressWarnings("serial")
	@Test
	public void testRealm() {

		final Realm realm = Realm.builder().name("rlm").withDefaultAuthorizer().build();

		assertEquals("rlm", realm.getName().get());

		assertTrue(realm.supportsPermission(DefaultPermission.class));

		final Permission p1 = Permission.create("p1");
		final Permission p2 = Permission.create("p2");
		final Permission p3 = Permission.create("p3");

		final Authenticator<AccountCredentialsToken> resolver = new Authenticator<AccountCredentialsToken>() {

			@Override
			public Class<? extends AccountCredentialsToken> getTokenType() {
				return AccountCredentialsToken.class;
			}

			@Override
			public Authentication authenticate(AccountCredentialsToken authenticationToken)
					throws AuthenticationException {
				if ("usr".equals(authenticationToken.getPrincipal())) {
					return Authentication.builder("usr").permission("p1").permission("p2").build();
				}
				throw new UnknownAccountException("usr");
			}
		};

		realm.addAuthenticator(resolver);

		final AuthenticationToken token = AuthenticationToken.accountCredentials("usr", "pwd");

		assertTrue(realm.supportsToken(AccountCredentialsToken.class));

		final Authentication authc = realm.authenticate(token);

		assertNotNull(authc);
		assertEquals(token.getPrincipal(), authc.getName());

		final Set<Permission> permissions = new HashSet<>(2);
		permissions.add(p1);
		permissions.add(p2);

		assertFalse(realm.isPermitted(null, permissions));
		assertFalse(realm.isPermitted(null, p1));

		assertFalse(realm.isPermitted(authc, (Permission[]) null));

		assertTrue(realm.isPermitted(authc, permissions));
		assertTrue(realm.isPermitted(authc, p1));
		assertTrue(realm.isPermitted(authc, p1, p2));
		assertTrue(realm.isPermittedAny(authc, p1, p2));
		assertTrue(realm.isPermittedAny(authc, permissions));
		assertTrue(realm.isPermittedAny(authc, p2));

		assertTrue(realm.isPermittedAny(authc, p1, p3));

		assertFalse(realm.isPermitted(authc, p3));
		assertFalse(realm.isPermitted(authc, p1, p3));

		assertTrue(realm.isPermitted(authc, "p1"));
		assertTrue(realm.isPermitted(authc, "p1", "p2"));
		assertTrue(realm.isPermittedAny(authc, "p1", "p3"));

		TestUtils.expectedException(UnexpectedAuthenticationException.class, new Runnable() {

			@Override
			public void run() {
				realm.authenticate((AuthenticationToken) null);
			}
		});

		Realm xrealm = Realm.builder().build();

		assertNotNull(xrealm.toString());

		final AuthenticationToken at = new AuthenticationToken() {

			@Override
			public Object getPrincipal() {
				return null;
			}

			@Override
			public Object getCredentials() {
				return null;
			}
		};

		TestUtils.expectedException(UnsupportedTokenException.class, new Runnable() {

			@Override
			public void run() {
				realm.authenticate(at);
			}
		});

	}

}
