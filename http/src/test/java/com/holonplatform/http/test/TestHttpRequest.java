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
package com.holonplatform.http.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.holonplatform.http.HttpHeaders;
import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.servlet.ServletHttpRequest;

public class TestHttpRequest {

	@Test
	public void testServletHttpRequest() throws IOException {

		HttpServletRequest servletRequest = buildHttpServletRequest("test");

		ServletHttpRequest request = ServletHttpRequest.create(servletRequest);
		assertNotNull(request);

		assertEquals("/", request.getContextPath());
		assertEquals("application/json", request.getHeaderValue(HttpHeaders.ACCEPT).orElse(null));
		assertEquals("en-us", request.getHeaderValue(HttpHeaders.ACCEPT_LANGUAGE).orElse(null));
		assertNotNull(request.getHeader(HttpHeaders.ACCEPT).orElse(null));
		assertEquals(1, request.getHeader(HttpHeaders.ACCEPT_LANGUAGE).get().size());
		assertEquals("en-us", request.getHeader(HttpHeaders.ACCEPT_LANGUAGE).get().get(0));
		assertEquals(HttpMethod.GET, request.getMethod());
		assertEquals("http", request.getProtocol().orElse(null));
		assertEquals("localhost", request.getRequestHost());
		assertEquals("/", request.getRequestPath());
		assertEquals("/test", request.getRequestURI());
		assertEquals("test-session-id", request.getSessionId().orElse(null));
		assertEquals("v1", request.getRequestParameter("p1").orElse(null));
		assertEquals("v2a,v2b", request.getRequestParameter("p2").orElse(null));
		assertNotNull(request.getMultiValueRequestParameter("p2").orElse(null));
		assertEquals(2, request.getMultiValueRequestParameter("p2").get().size());
		assertEquals("v2a", request.getMultiValueRequestParameter("p2").get().get(0));
		assertEquals("v2b", request.getMultiValueRequestParameter("p2").get().get(1));

		InputStream is = request.getBody();
		assertNotNull(is);

		StringBuilder textBuilder = new StringBuilder();
		try (Reader reader = new BufferedReader(
				new InputStreamReader(is, Charset.forName(StandardCharsets.UTF_8.name())))) {
			int c = 0;
			while ((c = reader.read()) != -1) {
				textBuilder.append((char) c);
			}
		}
		assertEquals("test", textBuilder.toString());
	}

	@SuppressWarnings("resource")
	private static HttpServletRequest buildHttpServletRequest(String content) throws IOException {

		HttpSession session = mock(HttpSession.class);
		when(session.getId()).thenReturn("test-session-id");

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getMethod()).thenReturn("GET");
		when(request.getProtocol()).thenReturn("http");
		when(request.getScheme()).thenReturn("http");
		when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		when(request.getRemoteHost()).thenReturn("localhost");
		when(request.getRemotePort()).thenReturn(80);
		when(request.getRequestURL()).thenReturn(new StringBuffer("http").append("://").append("localhost"));
		when(request.getRequestURI()).thenReturn("/test");
		when(request.getPathInfo()).thenReturn("/");
		when(request.getContextPath()).thenReturn("/");
		when(request.isSecure()).thenReturn(false);
		when(request.getAttributeNames()).thenReturn(Collections.emptyEnumeration());
		when(request.getCharacterEncoding()).thenReturn("utf-8");
		when(request.getHeaderNames()).thenAnswer(i -> buildHeaderNames());
		when(request.getHeaders(HttpHeaders.ACCEPT)).thenAnswer(i -> buildHeaderAccept());
		when(request.getHeaders(HttpHeaders.ACCEPT_LANGUAGE)).thenAnswer(i -> buildHeaderAcceptLanguage());
		when(request.getHeader(HttpHeaders.ACCEPT)).thenReturn("application/json");
		when(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE)).thenReturn("en-us");
		when(request.getParameterMap()).thenReturn(buildRequestParameters());
		when(request.getParameterValues("p1")).thenReturn(new String[] { "v1" });
		when(request.getParameterValues("p2")).thenReturn(new String[] { "v2a", "v2b" });
		when(request.getParameter("p1")).thenReturn("v1");
		when(request.getParameter("p2")).thenReturn("v2a,v2b");
		when(request.getInputStream()).thenReturn(new TestServletInputStream(content));
		when(request.getSession()).thenReturn(session);
		when(request.getSession(ArgumentMatchers.anyBoolean())).thenReturn(session);
		return request;
	}

	private static Enumeration<String> buildHeaderNames() {
		return Collections.enumeration(Arrays.asList(HttpHeaders.ACCEPT, HttpHeaders.ACCEPT_LANGUAGE));
	}

	private static Enumeration<String> buildHeaderAccept() {
		return Collections.enumeration(Arrays.asList("application/json"));
	}

	private static Enumeration<String> buildHeaderAcceptLanguage() {
		return Collections.enumeration(Arrays.asList("en-us"));
	}

	private static Map<String, String[]> buildRequestParameters() {
		Map<String, String[]> ps = new HashMap<>();
		ps.put("p1", new String[] { "v1" });
		ps.put("p2", new String[] { "v2a", "v2b" });
		return ps;
	}

	private static class TestServletInputStream extends ServletInputStream {

		private final ByteArrayInputStream bis;
		private int last = 0;

		public TestServletInputStream(String content) {
			bis = new ByteArrayInputStream(content.getBytes());
		}

		@Override
		public boolean isFinished() {
			return last < 0;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
		}

		@Override
		public int read() throws IOException {
			last = bis.read();
			return last;
		}

	}

}
