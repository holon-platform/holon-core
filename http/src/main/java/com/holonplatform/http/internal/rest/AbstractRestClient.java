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
package com.holonplatform.http.internal.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.http.internal.HttpLogger;
import com.holonplatform.http.rest.RestClient;
import com.holonplatform.http.rest.RestClient.Invoker;

/**
 * Abstract {@link RestClient} implementation.
 *
 * @since 5.0.0
 */
public abstract class AbstractRestClient implements RestClient, Invoker {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = HttpLogger.create();

	/**
	 * Default request target URI
	 */
	private URI defaultTargetURI;

	/**
	 * Default headers
	 */
	private final Map<String, String> defaultHeaders = new HashMap<>(4);

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient#setDefaultTarget(java.net.URI)
	 */
	@Override
	public RestClient defaultTarget(URI baseUri) {
		this.defaultTargetURI = baseUri;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient#getDefaultTarget()
	 */
	@Override
	public Optional<URI> getDefaultTarget() {
		return Optional.ofNullable(defaultTargetURI);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient#withDefaultHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public RestClient withDefaultHeader(String name, String value) {
		ObjectUtils.argumentNotNull(name, "Header name must be not null");
		defaultHeaders.put(name, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient#removeDefaultHeader(java.lang.String)
	 */
	@Override
	public RestClient removeDefaultHeader(String name) {
		ObjectUtils.argumentNotNull(name, "Header name must be not null");
		defaultHeaders.remove(name);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient#clearDefaultHeaders()
	 */
	@Override
	public RestClient clearDefaultHeaders() {
		defaultHeaders.clear();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient#request()
	 */
	@Override
	public RequestDefinition request() {
		RequestDefinition definition = buildDefinition();

		// set default target URI, if configured
		getDefaultTarget().ifPresent(t -> definition.target(t));

		// set default headers
		defaultHeaders.forEach((n, v) -> {
			if (!definition.getHeaders().containsKey(n)) {
				definition.getHeaders().put(n, v);
			}
		});

		LOGGER.debug(() -> "RestClient: build request[" + definition + "]");

		return definition;
	}

	/**
	 * Build a new RequestDefinition to be used for request configuration.
	 * @return Request definition of expected type
	 */
	protected abstract RequestDefinition buildDefinition();

}
