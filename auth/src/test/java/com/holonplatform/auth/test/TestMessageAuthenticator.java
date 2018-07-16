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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.auth.exceptions.UnsupportedMessageException;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.core.internal.utils.TestUtils;
import com.holonplatform.core.messaging.Message;

public class TestMessageAuthenticator {

	@Test
	public void testMessageResolver() {
		AuthenticationTokenResolver<TestMessage> r1 = AuthenticationTokenResolver.create(TestMessage.class, msg -> {
			return msg.getHeader("MY_HDR1").map(h -> new MyHeaderAuthenticationToken(h));
		});

		assertFalse(r1.getScheme().isPresent());
		assertEquals(TestMessage.class, r1.getMessageType());

		TestMessage m = new TestMessage("content");
		assertFalse(r1.getAuthenticationToken(m).isPresent());

		m.setHeader("MY_HDR1", "usr");
		AuthenticationToken token = r1.getAuthenticationToken(m).orElse(null);
		assertNotNull(token);

		assertEquals("usr", token.getPrincipal());

		r1 = AuthenticationTokenResolver.create(TestMessage.class, msg -> {
			return msg.getHeader("MY_HDR1").map(h -> new MyHeaderAuthenticationToken(h));
		}, "myscheme");

		assertTrue(r1.getScheme().isPresent());
		assertEquals("myscheme", r1.getScheme().get());
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

	private class TestMessage implements Message<String, String> {

		private final String payload;
		private final Map<String, String> headers = new HashMap<>();

		public TestMessage(String payload) {
			super();
			this.payload = payload;
		}

		public void setHeader(String name, String value) {
			headers.put(name, value);
		}

		@Override
		public Map<String, String> getHeaders() {
			return headers;
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

	@SuppressWarnings("serial")
	private class MyHeaderAuthenticationToken implements AuthenticationToken {

		private final String principal;

		public MyHeaderAuthenticationToken(String principal) {
			super();
			this.principal = principal;
		}

		@Override
		public Object getPrincipal() {
			return principal;
		}

		@Override
		public Object getCredentials() {
			return null;
		}

	}

}
