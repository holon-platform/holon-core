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
package com.holonplatform.spring.internal.rest;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.HttpStatus;
import com.holonplatform.http.MediaType;
import com.holonplatform.http.exceptions.HttpClientInvocationException;
import com.holonplatform.http.exceptions.UnsuccessfulResponseException;
import com.holonplatform.http.internal.HttpUtils;
import com.holonplatform.http.internal.rest.AbstractRestClient;
import com.holonplatform.http.internal.rest.DefaultRequestDefinition;
import com.holonplatform.http.rest.RequestEntity;
import com.holonplatform.http.rest.ResponseEntity;
import com.holonplatform.http.rest.ResponseType;
import com.holonplatform.spring.SpringRestClient;

/**
 * Default {@link SpringRestClient} implementation.
 *
 * @since 5.0.0
 */
public class RestTemplateRestClient extends AbstractRestClient implements SpringRestClient {

	/**
	 * RestOperations
	 */
	private final RestTemplate restTemplate;

	/**
	 * Construct a new SpringRestClient using given {@link RestTemplate} as concrete
	 * implementation.
	 * 
	 * @param restTemplate RestTemplate
	 */
	public RestTemplateRestClient(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
		// exclude RestTemplate errors management
		this.restTemplate.setErrorHandler(new ResponseErrorHandler() {

			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return false;
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.holonplatform.spring.SpringRestClient#getRestTemplate()
	 */
	@Override
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	@Override
	public <T, R> ResponseEntity<T> invoke(RequestDefinition requestDefinition, HttpMethod method,
			RequestEntity<R> requestEntity, ResponseType<T> responseType, boolean onlySuccessfulStatusCode) {

		// URI
		final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(requestDefinition.getRequestURI());
		// query parameters
		requestDefinition.getQueryParameters().forEach((n, v) -> builder.queryParam(n, v));
		// template parameters
		final String uri = builder.buildAndExpand(requestDefinition.getTemplateParameters()).toUriString();

		// headers
		HttpHeaders headers = new HttpHeaders();
		requestDefinition.getHeaders().forEach((n, v) -> headers.add(n, v));

		// Entity
		HttpEntity<?> entity = new HttpEntity<>(getRequestPayload(requestEntity), headers);

		// method
		org.springframework.http.HttpMethod requestMethod = org.springframework.http.HttpMethod
				.valueOf(method.getMethodName());
		if (requestMethod == null) {
			throw new RestClientException("Unsupported HTTP method: " + method.getMethodName());
		}

		// get response, checking propertySet
		final org.springframework.http.ResponseEntity<Resource> response;
		if (requestDefinition.getPropertySet().isPresent()) {
			response = requestDefinition.getPropertySet().get()
					.execute(() -> invoke(uri, requestMethod, entity, responseType));
		} else {
			response = invoke(uri, requestMethod, entity, responseType);
		}

		// check error status code
		int statusCode = response.getStatusCode().value();

		if (onlySuccessfulStatusCode && !HttpStatus.isSuccessStatusCode(statusCode)) {
			throw new UnsuccessfulResponseException(new SpringResponseEntity<>(response, ResponseType.of(byte[].class),
					getRestTemplate().getMessageConverters(), requestDefinition.getPropertySet().orElse(null)));
		}

		return new SpringResponseEntity<>(response, responseType, getRestTemplate().getMessageConverters(),
				requestDefinition.getPropertySet().orElse(null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.holonplatform.http.internal.AbstractRestClient#buildDefinition()
	 */
	@Override
	protected RequestDefinition buildDefinition() {
		return new DefaultRequestDefinition(this);
	}

	/**
	 * Invoke for a response
	 * 
	 * @param <T>           Response payload type
	 * @param uri           URI
	 * @param requestMethod Method
	 * @param request       Request entity
	 * @param responseType  Expected response payload type
	 * @return Response entity
	 */
	protected <T> org.springframework.http.ResponseEntity<Resource> invoke(String uri,
			org.springframework.http.HttpMethod requestMethod, HttpEntity<?> request, ResponseType<T> responseType) {
		try {
			return getRestTemplate().exchange(uri, requestMethod, request, Resource.class);
		} catch (Exception e) {
			throw new HttpClientInvocationException(e);
		}
	}

	private static final String APPLICATION_FORM_URLENCODED_MEDIA_TYPE = MediaType.APPLICATION_FORM_URLENCODED
			.toString();

	/**
	 * Get the request entity payload
	 * 
	 * @param requestEntity RequestEntity
	 * @return Request entity payload, may be null
	 */
	protected Object getRequestPayload(RequestEntity<?> requestEntity) {
		if (requestEntity != null) {
			boolean form = requestEntity.getMediaType().map(m -> APPLICATION_FORM_URLENCODED_MEDIA_TYPE.equals(m))
					.orElse(Boolean.FALSE);
			return requestEntity.getPayload().map(p -> form ? new LinkedMultiValueMap<>(HttpUtils.getAsMultiMap(p)) : p)
					.orElse(null);
		}
		return null;
	}

}
