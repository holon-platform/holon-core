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
package com.holonplatform.http.internal.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.http.Cookie;
import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.HttpRequest;
import com.holonplatform.http.internal.AbstractHttpRequest;
import com.holonplatform.http.servlet.ServletHttpRequest;

/**
 * {@link HttpRequest} implementation using {@link HttpServletRequest} as concrete request.
 * 
 * @since 5.0.6
 */
public class DefaultServletHttpRequest extends AbstractHttpRequest implements ServletHttpRequest {

	protected final HttpServletRequest request;

	/**
	 * Constructor
	 * @param request HttpServletRequest (not null)
	 */
	public DefaultServletHttpRequest(HttpServletRequest request) {
		super();
		ObjectUtils.argumentNotNull(request, "HttpServletRequest must be not null");
		this.request = request;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpMessage#getProtocol()
	 */
	@Override
	public Optional<String> getProtocol() {
		return Optional.ofNullable(request.getProtocol());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpRequest#getMethod()
	 */
	@Override
	public HttpMethod getMethod() {
		return HttpMethod.from(request.getMethod());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpRequest#getRequestPath()
	 */
	@Override
	public String getRequestPath() {
		return request.getPathInfo();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpRequest#getRequestHost()
	 */
	@Override
	public String getRequestHost() {
		return request.getRemoteHost();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpRequest#getRequestParameter(java.lang.String)
	 */
	@Override
	public Optional<String> getRequestParameter(String name) {
		ObjectUtils.argumentNotNull(name, "Parameter name must be not null");
		return Optional.ofNullable(request.getParameter(name));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpRequest#getMultiValueRequestParameter(java.lang.String)
	 */
	@Override
	public Optional<List<String>> getMultiValueRequestParameter(String name) {
		ObjectUtils.argumentNotNull(name, "Parameter name must be not null");
		String[] values = request.getParameterValues(name);
		if (values != null) {
			return Optional.of(Arrays.asList(values));
		}
		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpRequest#getRequestParameters()
	 */
	@Override
	public Map<String, List<String>> getRequestParameters() {
		Map<String, String[]> params = request.getParameterMap();
		if (params != null && !params.isEmpty()) {
			final Map<String, List<String>> rqp = new HashMap<>(params.size());
			params.entrySet().forEach(e -> {
				if (e.getValue() != null) {
					rqp.put(e.getKey(), Arrays.asList(e.getValue()));
				}
			});
			return rqp;
		}
		return Collections.emptyMap();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpRequest#getRequestCookie(java.lang.String)
	 */
	@Override
	public Optional<Cookie> getRequestCookie(String name) {
		ObjectUtils.argumentNotNull(name, "Cookie name must be not null");
		jakarta.servlet.http.Cookie[] rcookies = request.getCookies();
		if (rcookies != null) {
			jakarta.servlet.http.Cookie rcookie = null;
			for (jakarta.servlet.http.Cookie cookie : rcookies) {
				if (name.equals(cookie.getName())) {
					rcookie = cookie;
					break;
				}
			}

			if (rcookie != null) {
				return Optional.of(com.holonplatform.http.Cookie.builder().name(rcookie.getName())
						.value(rcookie.getValue()).version(rcookie.getVersion()).path(rcookie.getPath())
						.domain(rcookie.getDomain()).build());
			}
		}
		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpRequest#getBody()
	 */
	@Override
	public InputStream getBody() throws IOException, UnsupportedOperationException {
		return request.getInputStream();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.messaging.MessageHeaders#getHeaders()
	 */
	@Override
	public Map<String, List<String>> getHeaders() {
		final Map<String, List<String>> headers = new HashMap<>();
		final Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			final String headerName = headerNames.nextElement();
			final List<String> values = new ArrayList<>();
			final Enumeration<String> headerValues = request.getHeaders(headerName);
			while (headerValues.hasMoreElements()) {
				values.add(headerValues.nextElement());
			}
			headers.put(headerName, values);
		}
		return headers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.messaging.MessageHeaders#getHeader(java.lang.String)
	 */
	@Override
	public Optional<List<String>> getHeader(String name) {
		ObjectUtils.argumentNotNull(name, "Header name must be not null");
		final Enumeration<String> headerValues = request.getHeaders(name);
		if (headerValues != null) {
			final List<String> values = new LinkedList<>();
			while (headerValues.hasMoreElements()) {
				values.add(headerValues.nextElement());
			}
			return Optional.of(values);
		}
		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.servlet.ServletHttpRequest#getContextPath()
	 */
	@Override
	public String getContextPath() {
		return request.getContextPath();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.servlet.ServletHttpRequest#getRequestURI()
	 */
	@Override
	public String getRequestURI() {
		return request.getRequestURI();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.servlet.ServletHttpRequest#getSessionId()
	 */
	@Override
	public Optional<String> getSessionId() {
		HttpSession session = request.getSession(false);
		return Optional.ofNullable((session == null) ? null : session.getId());
	}

}
