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
package com.holonplatform.http;

import java.util.List;
import java.util.Map;

import com.holonplatform.core.internal.utils.ObjectUtils;
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
	 * Get a builder to create a default {@link HttpResponse} instance.
	 * @param <T> Payload type
	 * @param statusCode Response status code
	 * @param payloadType Payload type
	 * @return A new {@link HttpResponse} builder
	 */
	static <T> Builder<T> builder(int statusCode, Class<? extends T> payloadType) {
		return new DefaultHttpResponse.DefaultBuilder<>(statusCode, payloadType);
	}

	/**
	 * Get a builder to create a default {@link HttpResponse} instance.
	 * @param <T> Payload type
	 * @param status Response status (not null)
	 * @param payloadType Payload type
	 * @return A new {@link HttpResponse} builder
	 */
	static <T> Builder<T> builder(HttpStatus status, Class<? extends T> payloadType) {
		ObjectUtils.argumentNotNull(status, "HTTP status must be not null");
		return new DefaultHttpResponse.DefaultBuilder<>(status.getCode(), payloadType);
	}

	/**
	 * Default HttpResponse builder.
	 * @param <T> Payload type
	 */
	public interface Builder<T> {

		/**
		 * Set response headers. Any previously set header value will be replaced by the new ones.
		 * @param headers The headers to set as a name - values map
		 * @return this
		 */
		Builder<T> headers(Map<String, List<String>> headers);

		/**
		 * Add a response header, providing the header values.
		 * @param name The header name (not null)
		 * @param values The header values
		 * @return this
		 */
		Builder<T> header(String name, List<String> values);

		/**
		 * Add a single value response header.
		 * @param name The header name (not null)
		 * @param value The header value
		 * @return this
		 */
		Builder<T> header(String name, String value);

		/**
		 * Set the response payload.
		 * @param payload the payload to set
		 * @return this
		 */
		Builder<T> payload(T payload);

		/**
		 * Build the {@link HttpResponse} instance.
		 * @return A new {@link HttpResponse} instance
		 */
		HttpResponse<T> build();

	}

}
