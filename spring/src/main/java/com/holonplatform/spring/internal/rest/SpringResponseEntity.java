/*
 * Copyright 2000-2017 Holon TDCN.
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
package com.holonplatform.spring.internal.rest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseExtractor;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.http.exceptions.HttpEntityProcessingException;
import com.holonplatform.http.exceptions.InvalidHttpMessageException;
import com.holonplatform.http.rest.ResponseEntity;
import com.holonplatform.http.rest.ResponseType;

/**
 * Spring {@link ResponseEntity} implementation using a {@link ClientHttpResponse}.
 * 
 * @param <T> Response entity type
 *
 * @since 5.0.0
 */
public class SpringResponseEntity<T> implements ResponseEntity<T> {

	private final org.springframework.http.ResponseEntity<Resource> response;
	private final ResponseType<T> type;
	private final List<HttpMessageConverter<?>> messageConverters;

	private final StreamHttpResponse httpResponse;

	/**
	 * Constructor
	 * @param response Spring response entity (not null)
	 * @param type Response type (not null)
	 * @param messageConverters HTTP message converters
	 */
	public SpringResponseEntity(org.springframework.http.ResponseEntity<Resource> response, ResponseType<T> type,
			List<HttpMessageConverter<?>> messageConverters) {
		super();
		ObjectUtils.argumentNotNull(response, "ClientHttpResponse must be not null");
		ObjectUtils.argumentNotNull(type, "Response type must be not null");
		this.response = response;
		this.type = type;
		this.messageConverters = (messageConverters != null) ? messageConverters : Collections.emptyList();
		this.httpResponse = new StreamHttpResponse(response);
	}

	/**
	 * Get the available HTTP message converters
	 * @return the HTTP message converters list, empty if none
	 */
	protected List<HttpMessageConverter<?>> getMessageConverters() {
		return messageConverters;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.messaging.MessageHeaders#getHeaders()
	 */
	@Override
	public Map<String, List<String>> getHeaders() {
		return response.getHeaders();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.HttpResponse#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		try {
			return response.getStatusCode().value();
		} catch (Exception e) {
			throw new InvalidHttpMessageException("Failed to read ClientHttpResponse status code", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.messaging.Message#getPayload()
	 */
	@Override
	public Optional<T> getPayload() throws UnsupportedOperationException {
		return readAs(type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.messaging.Message#getPayloadType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends T> getPayloadType() throws UnsupportedOperationException {
		return (Class<? extends T>) type.getType();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.ResponseEntity#as(java.lang.Class)
	 */
	@Override
	public <E> Optional<E> as(Class<E> entityType) {
		ObjectUtils.argumentNotNull(entityType, "Entity type must be not null");
		return readAs(ResponseType.of(entityType));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.ResponseEntity#as(com.holonplatform.http.rest.ResponseType)
	 */
	@Override
	public <E> Optional<E> as(ResponseType<E> entityType) {
		return readAs(entityType);
	}

	/**
	 * Read the message entity as an instance of the type represented by given <code>type</code> {@link ResponseType}.
	 * @param <E> Response entity type
	 * @param type Response entity type to read
	 * @return the message entity converted to given type, or an empty Optional for empty or zero-length responses
	 * @throws HttpEntityProcessingException If a entity processing error occurred (e.g. no message body reader
	 *         available for the requested type)
	 */
	@SuppressWarnings("unchecked")
	protected <E> Optional<E> readAs(ResponseType<E> type) {
		ObjectUtils.argumentNotNull(type, "Response type must be not null");

		final Type responseType = type.getType();

		try {

			// check InputStream
			if (InputStream.class == responseType) {
				return (Optional<E>) Optional.ofNullable(response.getBody().getInputStream());
			}

			if (Void.class != responseType) {
				ResponseExtractor<E> extractor = new HttpMessageConverterExtractor<>(responseType,
						getMessageConverters());
				return Optional.ofNullable(extractor.extractData(httpResponse));
			}
		} catch (Exception e) {
			throw new HttpEntityProcessingException("Failed to read HTTP entity as [" + type + "]", e);
		}

		return Optional.empty();
	}

	static class StreamHttpResponse implements ClientHttpResponse {

		private final org.springframework.http.ResponseEntity<Resource> responseEntity;

		public StreamHttpResponse(org.springframework.http.ResponseEntity<Resource> responseEntity) {
			super();
			this.responseEntity = responseEntity;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.http.HttpInputMessage#getBody()
		 */
		@Override
		public InputStream getBody() throws IOException {
			return responseEntity.getBody().getInputStream();
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.http.HttpMessage#getHeaders()
		 */
		@Override
		public HttpHeaders getHeaders() {
			return responseEntity.getHeaders();
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.http.client.ClientHttpResponse#getStatusCode()
		 */
		@Override
		public HttpStatus getStatusCode() throws IOException {
			return responseEntity.getStatusCode();
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.http.client.ClientHttpResponse#getRawStatusCode()
		 */
		@Override
		public int getRawStatusCode() throws IOException {
			return responseEntity.getStatusCodeValue();
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.http.client.ClientHttpResponse#getStatusText()
		 */
		@Override
		public String getStatusText() throws IOException {
			return responseEntity.getStatusCode().getReasonPhrase();
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.http.client.ClientHttpResponse#close()
		 */
		@Override
		public void close() {
			// noop
		}

	}

}
