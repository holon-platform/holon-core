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
package com.holonplatform.http.internal;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.http.HttpResponse;

/**
 * Default {@link HttpResponse} implementation.
 * 
 * @param <T> Response payload type
 *
 * @since 5.0.0
 */
public class DefaultHttpResponse<T> implements HttpResponse<T> {

	/**
	 * HTTP status code
	 */
	private final int statusCode;

	/**
	 * Payload type
	 */
	private final Class<? extends T> payloadType;

	/**
	 * Headers
	 */
	private Map<String, List<String>> headers;

	/**
	 * Payload
	 */
	private T payload;

	/**
	 * Constructor
	 * @param statusCode Response status code
	 * @param payloadType Payload type
	 */
	public DefaultHttpResponse(int statusCode, Class<? extends T> payloadType) {
		super();
		this.statusCode = statusCode;
		this.payloadType = payloadType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpResponse#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		return statusCode;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.messaging.MessageHeaders#getHeaders()
	 */
	@Override
	public Map<String, List<String>> getHeaders() {
		return (headers != null) ? headers : Collections.emptyMap();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.messaging.Message#getPayloadType()
	 */
	@Override
	public Class<? extends T> getPayloadType() throws UnsupportedOperationException {
		return payloadType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.messaging.Message#getPayload()
	 */
	@Override
	public Optional<T> getPayload() throws UnsupportedOperationException {
		return Optional.ofNullable(payload);
	}

	/**
	 * Set response headers
	 * @param headers the headers to set
	 */
	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	/**
	 * Set response payload
	 * @param payload the payload to set
	 */
	public void setPayload(T payload) {
		this.payload = payload;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultHttpResponse [statusCode=" + statusCode + ", payloadType=" + payloadType + ", headers=" + headers
				+ ", payload=" + payload + "]";
	}

	/**
	 * Response builder.
	 *
	 * @param <T> Response payload type
	 */
	public static class DefaultBuilder<T> implements Builder<T> {

		/**
		 * Response to build
		 */
		private final DefaultHttpResponse<T> response;

		/**
		 * Constructor
		 * @param statusCode Response status code
		 * @param payloadType Payload type
		 */
		public DefaultBuilder(int statusCode, Class<? extends T> payloadType) {
			super();
			this.response = new DefaultHttpResponse<>(statusCode, payloadType);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.HttpResponse.Builder#headers(java.util.Map)
		 */
		@Override
		public com.holonplatform.http.HttpResponse.Builder<T> headers(Map<String, List<String>> headers) {
			this.response.setHeaders(headers);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.HttpResponse.Builder#payload(java.lang.Object)
		 */
		@Override
		public com.holonplatform.http.HttpResponse.Builder<T> payload(T payload) {
			this.response.setPayload(payload);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.HttpResponse.Builder#build()
		 */
		@Override
		public HttpResponse<T> build() {
			return response;
		}

	}

}
