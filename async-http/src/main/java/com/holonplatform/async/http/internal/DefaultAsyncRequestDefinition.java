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

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.holonplatform.async.http.AsyncRestClient.AsyncRequestDefinition;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.exceptions.HttpClientInvocationException;
import com.holonplatform.http.internal.rest.AbstractRequestDefinition;
import com.holonplatform.http.rest.RequestEntity;
import com.holonplatform.http.rest.ResponseEntity;
import com.holonplatform.http.rest.ResponseType;

/**
 * Default {@link AsyncRequestDefinition} implementation.
 *
 * @since 5.2.0
 */
public class DefaultAsyncRequestDefinition extends AbstractRequestDefinition<AsyncRequestDefinition>
		implements AsyncRequestDefinition {

	/**
	 * Invoker
	 */
	protected final AsyncInvoker invoker;

	/**
	 * Constructor.
	 * @param invoker Invoker to use to invoke for response
	 */
	public DefaultAsyncRequestDefinition(AsyncInvoker invoker) {
		super();
		ObjectUtils.argumentNotNull(invoker, "Invoker must be not null");
		this.invoker = invoker;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.internal.rest.AbstractRequestDefinition#getActualDefinition()
	 */
	@Override
	protected AsyncRequestDefinition getActualDefinition() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.async.http.AsyncRestClient.AsyncInvocation#invoke(com.holonplatform.http.HttpMethod,
	 * com.holonplatform.http.rest.RequestEntity, com.holonplatform.http.rest.ResponseType)
	 */
	@Override
	public <T, R> CompletionStage<ResponseEntity<T>> invoke(HttpMethod method, RequestEntity<R> requestEntity,
			ResponseType<T> responseType) {
		return invoker.invoke(this, method, requestEntity, responseType, false);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.async.http.AsyncRestClient.AsyncInvocation#invokeForSuccess(com.holonplatform.http.HttpMethod,
	 * com.holonplatform.http.rest.RequestEntity, com.holonplatform.http.rest.ResponseType)
	 */
	@Override
	public <T, R> CompletionStage<ResponseEntity<T>> invokeForSuccess(HttpMethod method, RequestEntity<R> requestEntity,
			ResponseType<T> responseType) {
		return invoker.invoke(this, method, requestEntity, responseType, true);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.async.http.AsyncRestClient.AsyncInvocation#invokeForEntity(com.holonplatform.http.HttpMethod,
	 * com.holonplatform.http.rest.RequestEntity, com.holonplatform.http.rest.ResponseType)
	 */
	@Override
	public <T, R> CompletionStage<Optional<T>> invokeForEntity(HttpMethod method, RequestEntity<R> requestEntity,
			ResponseType<T> responseType) {
		return invoker.invoke(this, method, requestEntity, responseType, true).thenApply(response -> {
			final Optional<T> payload;
			try {
				payload = response.getPayload();
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
			return payload;
		});
	}

}
