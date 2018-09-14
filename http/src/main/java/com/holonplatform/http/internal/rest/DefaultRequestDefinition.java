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
package com.holonplatform.http.internal.rest;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.http.CacheControl;
import com.holonplatform.http.HttpHeaders;
import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.MediaType;
import com.holonplatform.http.exceptions.HttpClientInvocationException;
import com.holonplatform.http.internal.HttpLogger;
import com.holonplatform.http.rest.RequestEntity;
import com.holonplatform.http.rest.ResponseEntity;
import com.holonplatform.http.rest.ResponseType;
import com.holonplatform.http.rest.RestClient.RequestDefinition;

/**
 * Default {@link RequestDefinition} implementation.
 *
 * @since 5.0.0
 */
public class DefaultRequestDefinition implements RequestDefinition {

	private final static Logger LOGGER = HttpLogger.create();

	/**
	 * Base request URI
	 */
	protected URI baseRequestURI;

	/**
	 * Request paths
	 */
	protected final List<String> requestPaths = new LinkedList<>();

	/**
	 * Template parameters
	 */
	protected final Map<String, Object> templateParameters = new HashMap<>(8);

	/**
	 * Query parameters
	 */
	protected final Map<String, Object[]> queryParameters = new HashMap<>(8);

	/**
	 * Headers
	 */
	protected final Map<String, String> headers = new HashMap<>(8);

	/**
	 * PropertySet
	 */
	protected PropertySet<?> propertySet;

	/**
	 * Invoker
	 */
	protected final Invoker invoker;

	/**
	 * Construct a new DefaultRestRequestDefinition
	 * @param invoker Invoker to use to invoke for response
	 */
	public DefaultRequestDefinition(Invoker invoker) {
		super();
		ObjectUtils.argumentNotNull(invoker, "Invoker must be not null");
		this.invoker = invoker;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#target(java.net.URI)
	 */
	@Override
	public RequestDefinition target(URI baseUri) {
		ObjectUtils.argumentNotNull(baseUri, "Base URI target must be not null");
		this.baseRequestURI = baseUri;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#path(java.lang.String)
	 */
	@Override
	public RequestDefinition path(String path) {
		ObjectUtils.argumentNotNull(path, "Request path must be not null");
		requestPaths.add(path);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#queryParameter(java.lang.String, java.lang.Object[])
	 */
	@Override
	public RequestDefinition queryParameter(String name, Object... values) {
		ObjectUtils.argumentNotNull(name, "Query parameter name must be not null");
		Object[] v = (values != null) ? values : new Object[0];
		queryParameters.put(name, v);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#resolve(java.lang.String, java.lang.Object)
	 */
	@Override
	public RequestDefinition resolve(String name, Object value) {
		ObjectUtils.argumentNotNull(name, "Template variable name must be not null");
		ObjectUtils.argumentNotNull(value, "Template variable value must be not null");
		templateParameters.put(name, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#resolve(java.util.Map)
	 */
	@Override
	public RequestDefinition resolve(Map<String, Object> nameAndValues) {
		if (nameAndValues != null) {
			for (Entry<String, Object> entry : nameAndValues.entrySet()) {
				resolve(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#accept(java.lang.String[])
	 */
	@Override
	public RequestDefinition accept(String... mediaTypes) {
		if (mediaTypes != null) {
			header(HttpHeaders.ACCEPT, mediaTypes);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#accept(com.holonplatform.http.MediaType[])
	 */
	@Override
	public RequestDefinition accept(MediaType... mediaTypes) {
		String[] values = null;
		if (mediaTypes != null) {
			values = new String[mediaTypes.length];
			for (int i = 0; i < mediaTypes.length; i++) {
				values[i] = mediaTypes[i].toString();
			}
		}
		return accept(values);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#acceptLanguage(java.util.Locale[])
	 */
	@Override
	public RequestDefinition acceptLanguage(Locale... locales) {
		String[] values = null;
		if (locales != null) {
			values = new String[locales.length];
			for (int i = 0; i < locales.length; i++) {
				values[i] = locales[i].toLanguageTag();
			}
		}
		return acceptLanguage(values);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#acceptLanguage(java.lang.String[])
	 */
	@Override
	public RequestDefinition acceptLanguage(String... locales) {
		if (locales != null) {
			header(HttpHeaders.ACCEPT_LANGUAGE, locales);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#acceptEncoding(java.lang.String[])
	 */
	@Override
	public RequestDefinition acceptEncoding(String... encodings) {
		if (encodings != null) {
			header(HttpHeaders.ACCEPT_ENCODING, encodings);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#acceptCharset(java.lang.String[])
	 */
	@Override
	public RequestDefinition acceptCharset(String... charsets) {
		if (charsets != null) {
			header(HttpHeaders.ACCEPT_CHARSET, charsets);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#acceptCharset(java.nio.charset.Charset[])
	 */
	@Override
	public RequestDefinition acceptCharset(Charset... charsets) {
		String[] values = null;
		if (charsets != null) {
			values = new String[charsets.length];
			for (int i = 0; i < charsets.length; i++) {
				values[i] = charsets[i].name();
			}
		}
		return acceptCharset(values);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#cacheControl(com.holonplatform.http.CacheControl)
	 */
	@Override
	public RequestDefinition cacheControl(CacheControl cacheControl) {
		ObjectUtils.argumentNotNull(cacheControl, "CacheControl must be not null");
		cacheControl.asHeader().ifPresent(c -> header(HttpHeaders.CACHE_CONTROL, c));
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#authorizationBearer(java.lang.String)
	 */
	@Override
	public RequestDefinition authorizationBearer(String bearerToken) {
		ObjectUtils.argumentNotNull(bearerToken, "Authorization bearer token must be not null");
		return header(HttpHeaders.AUTHORIZATION, HttpHeaders.SCHEME_BEARER + " " + bearerToken);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestDefinition#authorizationBasic(java.lang.String, java.lang.String)
	 */
	@Override
	public RequestDefinition authorizationBasic(String username, String password) {
		ObjectUtils.argumentNotNull(username, "Username must be not null");
		ObjectUtils.argumentNotNull(password, "Password must be not null");
		try {
			return header(HttpHeaders.AUTHORIZATION, HttpHeaders.SCHEME_BASIC + " " + Base64.getEncoder()
					.encodeToString(new String((username + ":" + password)).getBytes("ISO-8859-1")));
		} catch (UnsupportedEncodingException e) {
			throw new HttpClientInvocationException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#header(java.lang.String, java.lang.String[])
	 */
	@Override
	public RequestDefinition header(String name, String... values) {
		ObjectUtils.argumentNotNull(name, "Header name must be not null");
		StringBuilder sb = new StringBuilder();
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(values[i]);
			}
		}
		headers.put(name, sb.toString());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#propertySet(java.lang.Iterable)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <P extends Property> RequestDefinition propertySet(Iterable<P> properties) {
		this.propertySet = PropertySet.of(properties);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.RequestBuilder#propertySet(com.holonplatform.core.property.Property[])
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <P extends Property> RequestDefinition propertySet(P... properties) {
		this.propertySet = PropertySet.of(properties);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.internal.RestRequestConfiguration#getBaseRequestURI()
	 */
	@Override
	public Optional<URI> getBaseRequestURI() {
		return Optional.ofNullable(baseRequestURI);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.internal.RestRequestConfiguration#getRequestPath()
	 */
	@Override
	public Optional<String> getRequestPath() {
		if (!requestPaths.isEmpty()) {
			StringBuilder sb = new StringBuilder("");
			for (String path : requestPaths) {
				String p = path;
				if (p.startsWith("/") && sb.toString().endsWith("/")) {
					p = (p.length() > 1) ? p.substring(1) : "";
				}
				if (!p.startsWith("/") && !sb.toString().endsWith("/")) {
					sb.append('/');
				}
				sb.append(p);
			}
			if (sb.length() > 0) {
				return Optional.of(sb.toString());
			}
		}
		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.internal.RestRequestConfiguration#getRequestURI()
	 */
	@Override
	public String getRequestURI() {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseRequestURI().orElseThrow(() -> new HttpClientInvocationException("Missing target base URI"))
				.toString());
		sb.append(getRequestPath().map(p -> (sb.toString().endsWith("/") && p.startsWith("/")) ? p.substring(1) : p)
				.orElse(""));
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.internal.RestRequestConfiguration#getTemplateParameters()
	 */
	@Override
	public Map<String, Object> getTemplateParameters() {
		return templateParameters;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.internal.RestRequestConfiguration#getQueryParameters()
	 */
	@Override
	public Map<String, Object[]> getQueryParameters() {
		return queryParameters;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.internal.RestRequestConfiguration#getHeaders()
	 */
	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.internal.RestRequestConfiguration#getPropertySet()
	 */
	@Override
	public Optional<PropertySet<?>> getPropertySet() {
		return Optional.ofNullable(propertySet);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.Invocation#invoke(com.holonplatform.http.HttpMethod,
	 * com.holonplatform.http.RequestEntity, com.holonplatform.http.ResponseType)
	 */
	@Override
	public <T, R> ResponseEntity<T> invoke(HttpMethod method, RequestEntity<R> requestEntity,
			ResponseType<T> responseType) {
		return invoker.invoke(this, method, requestEntity, responseType, false);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.Invocation#invokeForSuccess(com.holonplatform.http.HttpMethod,
	 * com.holonplatform.http.rest.RequestEntity, com.holonplatform.http.rest.ResponseType)
	 */
	@Override
	public <T, R> ResponseEntity<T> invokeForSuccess(HttpMethod method, RequestEntity<R> requestEntity,
			ResponseType<T> responseType) {
		return invoker.invoke(this, method, requestEntity, responseType, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.Invocation#invokeForEntity(com.holonplatform.http.HttpMethod,
	 * com.holonplatform.http.rest.RequestEntity, com.holonplatform.http.rest.ResponseType)
	 */
	@Override
	public <T, R> Optional<T> invokeForEntity(HttpMethod method, RequestEntity<R> requestEntity,
			ResponseType<T> responseType) {
		ResponseEntity<T> response = invoker.invoke(this, method, requestEntity, responseType, true);
		if (response == null) {
			throw new HttpClientInvocationException("The invoker [" + invoker + "] returned a null response");
		}
		try {
			return response.getPayload();
		} catch (Exception e) {
			throw new HttpClientInvocationException(e);
		} finally {
			if (responseType != null && InputStream.class != responseType.getType()) {
				try {
					response.close();
				} catch (Exception e) {
					LOGGER.debug(() -> "Failed to close the response", e);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultRequestDefinition [baseRequestURI=" + baseRequestURI + ", requestPaths=" + requestPaths
				+ ", templateParameters=" + templateParameters + ", queryParameters=" + queryParameters + ", headers="
				+ headers + ", propertySet=" + propertySet + "]";
	}

}
