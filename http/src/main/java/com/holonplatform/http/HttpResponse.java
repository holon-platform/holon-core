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
package com.holonplatform.http;

import java.util.List;
import java.util.Map;

import com.holonplatform.http.exceptions.InvalidHttpMessageException;
import com.holonplatform.http.internal.DefaultHttpResponse;

/**
 * Represents a generic HTTP response
 * 
 * @param <T> Payload type
 * 
 * @since 5.0.0
 */
public interface HttpResponse<T> extends HttpMessage<T> {

	/**
	 * Get the response status code
	 * @return the response status code
	 */
	int getStatusCode();

	/**
	 * Get the response status as {@link HttpStatus}
	 * @return the response HttpStatus
	 * @throws InvalidHttpMessageException If the status code of the message does not corresponds to any of the
	 *         {@link HttpStatus} values
	 */
	default HttpStatus getStatus() {
		return HttpStatus.of(getStatusCode())
				.orElseThrow(() -> new InvalidHttpMessageException("Unknown status code: " + getStatusCode()));
	}

	/**
	 * Builder to create default {@link HttpResponse} instances.
	 * @param <T> Payload type
	 * @param statusCode Response status code
	 * @param payloadType Payload type
	 * @return Response builder
	 */
	static <T> Builder<T> builder(int statusCode, Class<? extends T> payloadType) {
		return new DefaultHttpResponse.DefaultBuilder<>(statusCode, payloadType);
	}

	/**
	 * Default HttpResponse builder.
	 * @param <T> Payload type
	 */
	public interface Builder<T> {

		/**
		 * Set response headers
		 * @param headers the headers to set
		 * @return this
		 */
		Builder<T> headers(Map<String, List<String>> headers);

		/**
		 * Set response payload
		 * @param payload the payload to set
		 * @return this
		 */
		Builder<T> payload(T payload);

		/**
		 * Build the response
		 * @return HttpResponse
		 */
		HttpResponse<T> build();

	}

}
