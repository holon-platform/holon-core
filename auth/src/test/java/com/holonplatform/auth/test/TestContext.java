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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.holonplatform.auth.AuthContext;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Authentication.AuthenticationListener;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.core.Context;

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

	@Test
	public void testAuthContextResource() {

		boolean ia = Context.get().executeThreadBound(AuthContext.CONTEXT_KEY,
				AuthContext.create(Realm.builder().withDefaultAuthorizer().build()), () -> {
					return AuthContext.getCurrent().orElseThrow(() -> new IllegalStateException("Missing AuthContext"))
							.isAuthenticated();
				});

		assertFalse(ia);

		ia = Context.get().executeThreadBound(AuthContext.CONTEXT_KEY,
				AuthContext.create(Realm.builder().withDefaultAuthorizer().build()), () -> {
					return AuthContext.require().isAuthenticated();
				});

		assertFalse(ia);

	}

	@Test
	public void testAuthenticationListeners() {

		final AtomicInteger counter = new AtomicInteger(0);

		final Realm realm = Realm.builder().authenticator(Authenticator.create(AccountCredentialsToken.class, token -> {
			if ("myself".equals(token.getPrincipal())) {
				return Authentication.builder("myself").build();
			}
			throw new UnknownAccountException("" + token.getPrincipal());
		})).build();

		final AuthContext ctx = AuthContext.create(realm);

		ctx.addAuthenticationListener(authc -> {
			counter.incrementAndGet();

			if (authc != null) {
				assertTrue(ctx.isAuthenticated());
				assertNotNull(ctx.getAuthentication().orElse(null));
			}
		});

		AccountCredentialsToken tkn = AccountCredentialsToken.create("myself", "pwd");

		Authentication authc = ctx.authenticate(tkn);

		assertNotNull(authc);
		assertEquals(1, counter.get());

		ctx.unauthenticate();

		assertNotNull(authc);
		assertEquals(2, counter.get());

	}

	@Test
	public void testPermissionExtension() {

		final Realm realm = Realm.builder().withDefaultAuthorizer().build();

		final Authentication authc = Authentication.builder("myself").permission(new MyPermission("r1"))
				.permission(new MyPermission("r2")).build();

		final MyPermission mp1 = new MyPermission("r1");
		final MyPermission mp2 = new MyPermission("r2");
		final MyPermission mp3 = new MyPermission("r3");

		assertTrue(realm.isPermitted(authc, mp1));
		assertTrue(realm.isPermitted(authc, mp2));
		assertFalse(realm.isPermitted(authc, mp3));

		Set<MyPermission> permissions = new HashSet<>();
		permissions.add(mp1);
		permissions.add(mp3);

		assertFalse(realm.isPermitted(authc, permissions));
		assertTrue(realm.isPermittedAny(authc, permissions));

		final AuthContext ctx = AuthContext
				.create(Realm.builder().authenticator(Authenticator.create(AccountCredentialsToken.class, token -> {
					if ("myself".equals(token.getPrincipal())) {
						return Authentication.builder("myself").permission(new MyPermission("r1"))
								.permission(new MyPermission("r2")).build();
					}
					throw new UnknownAccountException("" + token.getPrincipal());
				})).withDefaultAuthorizer().build());

		ctx.authenticate(AccountCredentialsToken.create("myself", ""));

		assertTrue(ctx.isPermitted(mp1));
		assertTrue(ctx.isPermitted(mp2));
		assertFalse(ctx.isPermitted(mp3));

		assertFalse(ctx.isPermitted(permissions));
		assertTrue(ctx.isPermittedAny(permissions));
		
		assertTrue(ctx.isPermitted(mp1));

	}

	@SuppressWarnings("serial")
	class MyPermission implements Permission {

		private final String role;

		public MyPermission(String role) {
			super();
			this.role = role;
		}

		@Override
		public Optional<String> getPermission() {
			return Optional.ofNullable(role);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((role == null) ? 0 : role.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MyPermission other = (MyPermission) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (role == null) {
				if (other.role != null)
					return false;
			} else if (!role.equals(other.role))
				return false;
			return true;
		}

		private TestContext getOuterType() {
			return TestContext.this;
		}

	}

}
