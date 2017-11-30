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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.Test;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Authorizer;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.auth.exceptions.UnsupportedMessageException;
import com.holonplatform.auth.exceptions.UnsupportedTokenException;
import com.holonplatform.auth.internal.DefaultPermission;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.core.Context;
import com.holonplatform.core.internal.utils.TestUtils;
import com.holonplatform.core.messaging.Message;

public class TestRealm {

	@SuppressWarnings("serial")
	@Test
	public void testRealm() {

		final Realm realm = Realm.builder().name("rlm").withDefaultAuthorizer().build();

		assertEquals(AuthenticationToken.class, realm.getTokenType());
		assertEquals(Permission.class, realm.getPermissionType());

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

	@Test
	public void testRealmContext() {

		TestUtils.expectedException(IllegalStateException.class, new Runnable() {

			@Override
			public void run() {
				Realm.require();
			}
		});

		String name = Context.get().executeThreadBound(Realm.CONTEXT_KEY,
				Realm.builder().name("rlm").withDefaultAuthorizer().build(), () -> {
					return Realm.getCurrent().orElseThrow(() -> new IllegalStateException("Missing Realm")).getName()
							.orElse(null);
				});

		assertEquals("rlm", name);

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

		realm.addAuthenticationListener(authc -> counter.incrementAndGet());

		AccountCredentialsToken tkn = AccountCredentialsToken.create("myself", "pwd");

		Authentication authc = realm.authenticate(tkn);

		assertNotNull(authc);
		assertEquals(1, counter.get());

		authc = realm.authenticate(tkn);

		assertNotNull(authc);
		assertEquals(2, counter.get());

	}

	@Test
	public void testMessageAuthentication() {

		final Realm realmx = Realm.builder().build();

		TestUtils.expectedException(UnsupportedMessageException.class, new Runnable() {

			@Override
			public void run() {
				realmx.authenticate(new TestMessage("myself"));
			}
		});

		final AtomicInteger counter = new AtomicInteger(0);

		final Realm realm = Realm.builder().resolver(new AuthenticationTokenResolver<TestMessage>() {

			@SuppressWarnings("rawtypes")
			@Override
			public Class<? extends Message> getMessageType() {
				return TestMessage.class;
			}

			@Override
			public Optional<String> getScheme() {
				return Optional.of("myscheme");
			}

			@Override
			public Optional<AuthenticationToken> getAuthenticationToken(TestMessage request)
					throws AuthenticationException {
				return Optional.of(AccountCredentialsToken.create(request.getPayload().orElse(null), ""));
			}

		}).authenticator(Authenticator.create(AccountCredentialsToken.class, token -> {
			if ("myself".equals(token.getPrincipal())) {
				return Authentication.builder("myself").build();
			}
			throw new UnknownAccountException("" + token.getPrincipal());
		})).listener(authc -> counter.incrementAndGet()).build();

		TestUtils.expectedException(UnexpectedAuthenticationException.class, new Runnable() {

			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				realm.authenticate((Message) null);
			}
		});

		assertTrue(realm.supportsMessage(TestMessage.class));

		Authentication authc = realm.authenticate(new TestMessage("myself"));

		assertNotNull(authc);
		assertEquals(1, counter.get());

		authc = realm.authenticate(new TestMessage("myself"), "myscheme");

		assertNotNull(authc);
		assertEquals(2, counter.get());

		TestUtils.expectedException(UnsupportedMessageException.class, new Runnable() {

			@Override
			public void run() {
				realm.authenticate(new TestMessage("myself"), "xxx");
			}
		});

	}

	@Test
	public void testAuthorization() {

		Authorizer<TestPermission> atz = new Authorizer<TestRealm.TestPermission>() {

			@Override
			public Class<? extends TestPermission> getPermissionType() {
				return TestPermission.class;
			}

			@Override
			public boolean isPermitted(Authentication authentication, TestPermission... permissions) {
				return check(authentication, Arrays.asList(permissions));
			}

			@Override
			public boolean isPermitted(Authentication authentication, String... permissions) {
				return false;
			}

			@Override
			public boolean isPermittedAny(Authentication authentication, TestPermission... permissions) {
				return check(authentication, Arrays.asList(permissions));
			}

			@Override
			public boolean isPermittedAny(Authentication authentication, String... permissions) {
				return false;
			}

			@Override
			public boolean isPermitted(Authentication authentication,
					Collection<? extends TestPermission> permissions) {
				return check(authentication, permissions);
			}

			@Override
			public boolean isPermittedAny(Authentication authentication,
					Collection<? extends TestPermission> permissions) {
				return check(authentication, permissions);
			}

			protected boolean check(Authentication authentication, Collection<? extends TestPermission> permissions) {
				List<TestPermission> ps = authentication.getPermissions().stream()
						.filter(p -> p instanceof TestPermission).map(p -> (TestPermission) p)
						.collect(Collectors.toList());
				for (TestPermission p : permissions) {
					if (!ps.contains(p)) {
						return false;
					}
				}
				return true;
			}
		};

		final Realm realm = Realm.builder().authorizer(atz).build();

		Authentication a = Authentication.builder("test").permission(new TestPermission(1)).build();

		Collection<Permission> toCheck = Collections.singletonList(new TestPermission(1));

		assertTrue(realm.isPermitted(a, toCheck));
		assertFalse(realm.isPermitted(a, new TestPermission(2)));

	}

	@SuppressWarnings("serial")
	private class TestPermission implements Permission {

		private final int id;

		public TestPermission(int id) {
			super();
			this.id = id;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.Permission#getPermission()
		 */
		@Override
		public Optional<String> getPermission() {
			return Optional.empty();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + id;
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
			TestPermission other = (TestPermission) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (id != other.id)
				return false;
			return true;
		}

		private TestRealm getOuterType() {
			return TestRealm.this;
		}

	}

	private class TestMessage implements Message<String, String> {

		private final String payload;

		public TestMessage(String payload) {
			super();
			this.payload = payload;
		}

		@Override
		public Map<String, String> getHeaders() {
			return Collections.emptyMap();
		}

		@Override
		public Optional<String> getProtocol() {
			return Optional.empty();
		}

		@Override
		public Optional<String> getPayload() throws UnsupportedOperationException {
			return Optional.ofNullable(payload);
		}

		@Override
		public Class<? extends String> getPayloadType() throws UnsupportedOperationException {
			return String.class;
		}

	}

}
