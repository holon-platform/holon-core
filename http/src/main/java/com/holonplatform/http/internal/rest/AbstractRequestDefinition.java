/*
 * Copyright 2016-2018 Axioma srl.
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
import com.holonplatform.http.MediaType;
import com.holonplatform.http.exceptions.HttpClientInvocationException;
import com.holonplatform.http.internal.HttpLogger;
import com.holonplatform.http.rest.RestClientOperations.RequestConfiguration;

/**
 * Abstract request definition.
 * 
 * @param <R> Concrete RequestConfiguration type
 *
 * @since 5.2.0
 */
public abstract class AbstractRequestDefinition<R extends RequestConfiguration<R>> implements RequestConfiguration<R> {

	protected final static Logger LOGGER = HttpLogger.create();

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
	 * Get the actual request definition instance.
	 * @return the actual request definition instance
	 */
	protected abstract R getActualDefinition();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#target(java.net.URI)
	 */
	@Override
	public R target(URI baseUri) {
		ObjectUtils.argumentNotNull(baseUri, "Base URI target must be not null");
		this.baseRequestURI = baseUri;
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#path(java.lang.String)
	 */
	@Override
	public R path(String path) {
		ObjectUtils.argumentNotNull(path, "Request path must be not null");
		requestPaths.add(path);
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#queryParameter(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public R queryParameter(String name, Object... values) {
		ObjectUtils.argumentNotNull(name, "Query parameter name must be not null");
		Object[] v = (values != null) ? values : new Object[0];
		queryParameters.put(name, v);
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#resolve(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public R resolve(String name, Object value) {
		ObjectUtils.argumentNotNull(name, "Template variable name must be not null");
		ObjectUtils.argumentNotNull(value, "Template variable value must be not null");
		templateParameters.put(name, value);
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#resolve(java.util.Map)
	 */
	@Override
	public R resolve(Map<String, Object> nameAndValues) {
		if (nameAndValues != null) {
			for (Entry<String, Object> entry : nameAndValues.entrySet()) {
				resolve(entry.getKey(), entry.getValue());
			}
		}
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#accept(java.lang.String[])
	 */
	@Override
	public R accept(String... mediaTypes) {
		if (mediaTypes != null) {
			header(HttpHeaders.ACCEPT, mediaTypes);
		}
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#accept(com.holonplatform.http.MediaType[])
	 */
	@Override
	public R accept(MediaType... mediaTypes) {
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
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#acceptLanguage(java.util.Locale[])
	 */
	@Override
	public R acceptLanguage(Locale... locales) {
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
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#acceptLanguage(java.lang.String[])
	 */
	@Override
	public R acceptLanguage(String... locales) {
		if (locales != null) {
			header(HttpHeaders.ACCEPT_LANGUAGE, locales);
		}
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#acceptEncoding(java.lang.String[])
	 */
	@Override
	public R acceptEncoding(String... encodings) {
		if (encodings != null) {
			header(HttpHeaders.ACCEPT_ENCODING, encodings);
		}
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#acceptCharset(java.lang.String[])
	 */
	@Override
	public R acceptCharset(String... charsets) {
		if (charsets != null) {
			header(HttpHeaders.ACCEPT_CHARSET, charsets);
		}
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#acceptCharset(java.nio.charset.Charset[])
	 */
	@Override
	public R acceptCharset(Charset... charsets) {
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
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#cacheControl(com.holonplatform.http.
	 * CacheControl)
	 */
	@Override
	public R cacheControl(CacheControl cacheControl) {
		ObjectUtils.argumentNotNull(cacheControl, "CacheControl must be not null");
		cacheControl.asHeader().ifPresent(c -> header(HttpHeaders.CACHE_CONTROL, c));
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#authorizationBearer(java.lang.String)
	 */
	@Override
	public R authorizationBearer(String bearerToken) {
		ObjectUtils.argumentNotNull(bearerToken, "Authorization bearer token must be not null");
		return header(HttpHeaders.AUTHORIZATION, HttpHeaders.SCHEME_BEARER + " " + bearerToken);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#authorizationBasic(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public R authorizationBasic(String username, String password) {
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
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#header(java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	public R header(String name, String... values) {
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
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#propertySet(java.lang.Iterable)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <P extends Property> R propertySet(Iterable<P> properties) {
		this.propertySet = PropertySet.of(properties);
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#propertySet(com.holonplatform.core.property
	 * .Property[])
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <P extends Property> R propertySet(P... properties) {
		this.propertySet = PropertySet.of(properties);
		return getActualDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#getBaseRequestURI()
	 */
	@Override
	public Optional<URI> getBaseRequestURI() {
		return Optional.ofNullable(baseRequestURI);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#getRequestPath()
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
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#getRequestURI()
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
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#getTemplateParameters()
	 */
	@Override
	public Map<String, Object> getTemplateParameters() {
		return templateParameters;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#getQueryParameters()
	 */
	@Override
	public Map<String, Object[]> getQueryParameters() {
		return queryParameters;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#getHeaders()
	 */
	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations.RequestConfiguration#getPropertySet()
	 */
	@Override
	public Optional<PropertySet<?>> getPropertySet() {
		return Optional.ofNullable(propertySet);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RequestDefinition [baseRequestURI=" + baseRequestURI + ", requestPaths=" + requestPaths
				+ ", templateParameters=" + templateParameters + ", queryParameters=" + queryParameters + ", headers="
				+ headers + ", propertySet=" + propertySet + "]";
	}

}
