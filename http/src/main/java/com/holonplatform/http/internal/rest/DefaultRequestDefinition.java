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
import java.util.Optional;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.exceptions.HttpClientInvocationException;
import com.holonplatform.http.rest.RequestEntity;
import com.holonplatform.http.rest.ResponseEntity;
import com.holonplatform.http.rest.ResponseType;
import com.holonplatform.http.rest.RestClient.RequestDefinition;

/**
 * Default {@link RequestDefinition} implementation.
 *
 * @since 5.0.0
 */
public class DefaultRequestDefinition extends AbstractRequestDefinition<RequestDefinition>
		implements RequestDefinition {

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
	 * @see com.holonplatform.http.internal.rest.AbstractRequestDefinition#getActualDefinition()
	 */
	@Override
	protected RequestDefinition getActualDefinition() {
		return this;
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

}
