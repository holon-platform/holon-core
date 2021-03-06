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
package com.holonplatform.async.http.internal;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.async.http.AsyncRestClient;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.http.internal.HttpLogger;

/**
 * Abstract {@link AsyncRestClient} implementation.
 *
 * @since 5.2.0
 */
public abstract class AbstractAsyncRestClient implements AsyncRestClient, AsyncInvoker {

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

	@Override
	public AsyncRestClient defaultTarget(URI baseUri) {
		this.defaultTargetURI = baseUri;
		return this;
	}

	@Override
	public Optional<URI> getDefaultTarget() {
		return Optional.ofNullable(defaultTargetURI);
	}

	@Override
	public AsyncRestClient withDefaultHeader(String name, String value) {
		ObjectUtils.argumentNotNull(name, "Header name must be not null");
		defaultHeaders.put(name, value);
		return this;
	}

	@Override
	public AsyncRestClient removeDefaultHeader(String name) {
		ObjectUtils.argumentNotNull(name, "Header name must be not null");
		defaultHeaders.remove(name);
		return this;
	}

	@Override
	public AsyncRestClient clearDefaultHeaders() {
		defaultHeaders.clear();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientOperations#request()
	 */
	@Override
	public AsyncRequestDefinition request() {
		AsyncRequestDefinition definition = buildDefinition();

		// set default target URI, if configured
		getDefaultTarget().ifPresent(t -> definition.target(t));

		// set default headers
		defaultHeaders.forEach((n, v) -> {
			if (!definition.getHeaders().containsKey(n)) {
				definition.getHeaders().put(n, v);
			}
		});

		LOGGER.debug(() -> "AsyncRestClient: build request[" + definition + "]");

		return definition;
	}

	/**
	 * Build a new {@link AsyncRequestDefinition} to be used for request configuration.
	 * @return The request definition
	 */
	protected abstract AsyncRequestDefinition buildDefinition();

}
