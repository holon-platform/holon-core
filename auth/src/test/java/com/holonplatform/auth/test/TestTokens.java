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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.auth.token.BearerAuthenticationToken;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.http.Cookie;
import com.holonplatform.http.HttpHeaders;
import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.HttpRequest;
import com.holonplatform.http.internal.AbstractHttpRequest;

public class TestTokens {

	@Test
	public void testAccountCredentialsToken() {

		AccountCredentialsToken tkn = new AccountCredentialsToken();
		Assert.assertNull(tkn.getPrincipal());
		Assert.assertNull(tkn.getCredentials());

		tkn = new AccountCredentialsToken("usr", "pwd");
		Assert.assertEquals("usr", tkn.getPrincipal());
		Assert.assertEquals("pwd", new String((byte[]) tkn.getCredentials()));

		tkn = AccountCredentialsToken.create("usr", "pwd");
		Assert.assertEquals("usr", tkn.getPrincipal());
		Assert.assertEquals("pwd", new String((byte[]) tkn.getCredentials()));

		byte[] cb = ConversionUtils.toBytes("pwd");

		tkn = new AccountCredentialsToken("usr", cb);
		Assert.assertEquals("usr", tkn.getPrincipal());
		Assert.assertEquals("pwd", new String((byte[]) tkn.getCredentials()));

		tkn = AccountCredentialsToken.create("usr", cb);
		Assert.assertEquals("usr", tkn.getPrincipal());
		Assert.assertEquals("pwd", new String((byte[]) tkn.getCredentials()));

		tkn = new AccountCredentialsToken();
		tkn.setAccountId("usr");
		tkn.setSecret("pwd");
		Assert.assertEquals("usr", tkn.getPrincipal());
		Assert.assertEquals("pwd", new String((byte[]) tkn.getCredentials()));

		tkn = new AccountCredentialsToken();
		tkn.setAccountId("usr");
		tkn.setSecret(cb);
		Assert.assertEquals("usr", tkn.getPrincipal());
		Assert.assertEquals("pwd", new String((byte[]) tkn.getCredentials()));

		AuthenticationTokenResolver<HttpRequest> resolver = AuthenticationToken.httpBasicResolver();

		AuthenticationToken bt = resolver.getAuthenticationToken(new TestHttpRequest("usr", "pwd")).orElse(null);
		Assert.assertNotNull(bt);
		Assert.assertEquals("usr", bt.getPrincipal());
		Assert.assertEquals("pwd", new String((byte[]) bt.getCredentials()));

	}

	@Test
	public void testBearerToken() {

		final String bearer = "abc";

		BearerAuthenticationToken tkn = new BearerAuthenticationToken(bearer);
		Assert.assertEquals(bearer, tkn.getCredentials());
		Assert.assertNull(tkn.getPrincipal());

		tkn = BearerAuthenticationToken.create(bearer);
		Assert.assertEquals(bearer, tkn.getCredentials());
		Assert.assertNull(tkn.getPrincipal());

		AuthenticationToken tkn2 = AuthenticationToken.bearer(bearer);
		Assert.assertEquals(bearer, tkn2.getCredentials());
		Assert.assertNull(tkn2.getPrincipal());

		AuthenticationTokenResolver<HttpRequest> resolver = AuthenticationToken.httpBearerResolver();

		AuthenticationToken bt = resolver.getAuthenticationToken(new TestHttpRequest(bearer)).orElse(null);
		Assert.assertNotNull(bt);
		Assert.assertEquals(bearer, tkn.getCredentials());

	}

	private class TestHttpRequest extends AbstractHttpRequest {

		private final String bearer;

		private final String username;
		private final String password;

		public TestHttpRequest(String bearer) {
			super();
			this.bearer = bearer;
			this.username = null;
			this.password = null;
		}

		public TestHttpRequest(String username, String password) {
			super();
			this.bearer = null;
			this.username = username;
			this.password = password;
		}

		@Override
		public HttpMethod getMethod() {
			return HttpMethod.GET;
		}

		@Override
		public String getRequestPath() {
			return "/";
		}

		@Override
		public String getRequestHost() {
			return null;
		}

		@Override
		public Optional<String> getRequestParameter(String name) {
			return Optional.empty();
		}

		@Override
		public Optional<List<String>> getMultiValueRequestParameter(String name) {
			return Optional.empty();
		}

		@Override
		public Map<String, List<String>> getRequestParameters() {
			return Collections.emptyMap();
		}

		@Override
		public Optional<Cookie> getRequestCookie(String name) {
			return Optional.empty();
		}

		@Override
		public InputStream getBody() throws IOException, UnsupportedOperationException {
			return null;
		}

		@Override
		public Map<String, List<String>> getHeaders() {
			Map<String, List<String>> headers = new HashMap<>();

			if (bearer != null) {
				headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + bearer));
			}

			if (username != null && password != null) {
				try {
					headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Basic " + Base64.getEncoder()
							.encodeToString(new String((username + ":" + password)).getBytes("ISO-8859-1"))));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}

			return headers;
		}

	}

}
