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
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import com.holonplatform.auth.AuthContext;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.Authentication.AuthenticationListener;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.auth.token.AccountCredentialsToken;

public class TestContext {

	@Test
	public void testContext() {

		final Permission p1 = Permission.create("p1");
		final Permission p2 = Permission.create("p2");
		final Permission p3 = Permission.create("p3");

		final Authenticator<AccountCredentialsToken> authenticator = new Authenticator<AccountCredentialsToken>() {

			@Override
			public Class<? extends AccountCredentialsToken> getTokenType() {
				return AccountCredentialsToken.class;
			}

			@Override
			public Authentication authenticate(AccountCredentialsToken authenticationToken)
					throws AuthenticationException {
				if ("usr".equals(authenticationToken.getPrincipal())) {
					return Authentication.builder("usr").permission(p1).permission(p2).build();
				}
				throw new UnknownAccountException("usr");
			}
		};

		Realm realm = Realm.builder().name("realm").authenticator(authenticator).withDefaultAuthorizer().build();

		AuthContext ctx = AuthContext.create(realm);

		final AuthenticationToken token = AuthenticationToken.accountCredentials("usr", "pwd");

		ctx.authenticate(token);

		final Optional<Authentication> authc = ctx.getAuthentication();

		assertNotNull(authc);
		assertTrue(authc.isPresent());
		assertEquals(token.getPrincipal(), authc.get().getName());

		final Set<Permission> permissions = new HashSet<>(2);
		permissions.add(p1);
		permissions.add(p2);

		assertTrue(ctx.isPermitted(permissions));
		assertTrue(ctx.isPermitted(p1));
		assertTrue(ctx.isPermitted(p1, p2));
		assertTrue(ctx.isPermittedAny(p1, p2));
		assertTrue(ctx.isPermittedAny(permissions));
		assertTrue(ctx.isPermittedAny(p2));
		assertTrue(ctx.isPermittedAny(p1, p3));

		assertFalse(ctx.isPermitted(p3));
		assertFalse(ctx.isPermitted(p1, p3));

		assertTrue(ctx.isPermitted("p1"));
		assertTrue(ctx.isPermitted("p1", "p2"));
		assertTrue(ctx.isPermittedAny("p1", "p3"));

		ctx.unauthenticate();

		assertFalse(ctx.isPermitted(permissions));
		assertFalse(ctx.isPermitted(p1));
		assertFalse(ctx.isPermitted(p1, p2));
		assertFalse(ctx.isPermittedAny(p1, p2));
		assertFalse(ctx.isPermittedAny(permissions));
		assertFalse(ctx.isPermittedAny(p2));
		assertFalse(ctx.isPermittedAny(p1, p3));
		assertFalse(ctx.isPermitted("p1"));
		assertFalse(ctx.isPermitted("p1", "p2"));
		assertFalse(ctx.isPermittedAny("p1", "p3"));

		final AuthContext ctx2 = AuthContext.create(realm);

		AuthenticationListener ls = new AuthenticationListener() {

			@Override
			public void onAuthentication(Authentication authentication) {
				if (authentication != null) {
					assertNotNull(authentication.getName());
				}
			}
		};

		ctx2.addAuthenticationListener(ls);

		ctx2.authenticate(token);

		ctx2.unauthenticate();
		ctx2.unauthenticate();

		assertFalse(ctx2.getAuthentication().isPresent());

		ctx2.removeAuthenticationListener(ls);

		realm = Realm.builder().authenticator(authenticator).build();

		final AuthContext dctx = AuthContext.create(realm);

		final AccountCredentialsToken token2 = new AccountCredentialsToken("usr", "pwd");
		dctx.authenticate(token2);

	}

}
