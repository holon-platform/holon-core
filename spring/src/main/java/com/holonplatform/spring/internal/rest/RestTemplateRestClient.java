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
package com.holonplatform.spring.internal.rest;

import java.lang.reflect.Type;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.HttpResponse;
import com.holonplatform.http.HttpStatus;
import com.holonplatform.http.MediaType;
import com.holonplatform.http.RequestEntity;
import com.holonplatform.http.ResponseType;
import com.holonplatform.http.internal.AbstractRestClient;
import com.holonplatform.http.internal.DefaultRequestDefinition;
import com.holonplatform.http.internal.HttpUtils;
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
	 * Construct a new SpringRestClient using given {@link RestTemplate} as concrete implementation.
	 * @param restTemplate RestTemplate
	 */
	public RestTemplateRestClient(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.spring.SpringRestClient#getRestTemplate()
	 */
	@Override
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RestClient.Invoker#invoke(com.holonplatform.http.RestClient.RequestDefinition,
	 * com.holonplatform.http.HttpMethod, com.holonplatform.http.RequestEntity, com.holonplatform.http.ResponseType)
	 */
	@Override
	public <T, R> HttpResponse<T> invoke(RequestDefinition requestDefinition, HttpMethod method,
			RequestEntity<R> requestEntity, ResponseType<T> responseType) {

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
				.resolve(method.getMethodName());
		if (requestMethod == null) {
			throw new RestClientException("Unsupported HTTP method: " + method.getMethodName());
		}

		// get response, checking propertySet
		ResponseEntity<T> response = requestDefinition.getPropertySet()
				.map(ps -> ps.execute(() -> invoke(uri, requestMethod, entity, responseType)))
				.orElseGet(() -> invoke(uri, requestMethod, entity, responseType));

		// check error status code
		if (HttpStatus.isErrorStatusCode(response.getStatusCodeValue())) {
			throw new UnsuccessfulInvocationException(new SpringHttpResponse<>(response, responseType));
		}

		return new SpringHttpResponse<>(response, responseType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.internal.AbstractRestClient#buildDefinition()
	 */
	@Override
	protected RequestDefinition buildDefinition() {
		return new DefaultRequestDefinition(this);
	}

	/**
	 * Invoke for a response
	 * @param <T> Response payload type
	 * @param uri URI
	 * @param requestMethod Method
	 * @param request Request entity
	 * @param responseType Expected response payload type
	 * @return Response entity
	 */
	@SuppressWarnings("unchecked")
	protected <T> ResponseEntity<T> invoke(String uri, org.springframework.http.HttpMethod requestMethod,
			HttpEntity<?> request, ResponseType<T> responseType) {
		// get response
		ResponseEntity<T> response = null;
		try {
			if (responseType.isSimpleType()) {
				response = getRestTemplate().exchange(uri, requestMethod, request, (Class<T>) responseType.getType());
			} else {
				response = getRestTemplate().exchange(uri, requestMethod, request,
						new ParameterizedResponseType<>(responseType));
			}
		} catch (Exception e) {
			throw new RestClientException(e);
		}

		if (response == null) {
			throw new RestClientException("Invocation returned a null Response");
		}

		return response;
	}

	private static final String APPLICATION_FORM_URLENCODED_MEDIA_TYPE = MediaType.APPLICATION_FORM_URLENCODED
			.toString();

	/**
	 * Get the request entity payload
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
		return Optional.empty();
	}

	private static class ParameterizedResponseType<T> extends ParameterizedTypeReference<T> {

		final ResponseType<T> responseType;

		protected ParameterizedResponseType(ResponseType<T> responseType) {
			this.responseType = responseType;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.ParameterizedTypeReference#getType()
		 */
		@Override
		public Type getType() {
			return responseType.getType();
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.ParameterizedTypeReference#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			return (this == obj || (obj instanceof ParameterizedResponseType && this.responseType.getType()
					.equals(((ParameterizedResponseType<?>) obj).responseType.getType())));
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.ParameterizedTypeReference#hashCode()
		 */
		@Override
		public int hashCode() {
			return this.responseType.getType().hashCode();
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.ParameterizedTypeReference#toString()
		 */
		@Override
		public String toString() {
			return "ParameterizedResponseType<" + this.responseType.getType() + ">";
		}

	}

}
