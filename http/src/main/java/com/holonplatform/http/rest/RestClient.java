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
package com.holonplatform.http.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.http.CacheControl;
import com.holonplatform.http.HttpHeaders;
import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.MediaType;
import com.holonplatform.http.exceptions.HttpClientInvocationException;
import com.holonplatform.http.exceptions.UnsuccessfulResponseException;

/**
 * HTTP REST client to build and execute client requests in order to consume responses returned.
 *
 * @since 5.0.0
 */
public interface RestClient {

	/**
	 * Invocation operations
	 */
	public interface Invocation {

		/**
		 * Invoke the request and receive a response back.
		 * <p>
		 * The response payload is processed and possibly converted by concrete client implementation.
		 * </p>
		 * @param <T> Response type
		 * @param <R> Request entity type
		 * @param method Request method
		 * @param requestEntity Request entity
		 * @param responseType Expected response payload type
		 * @return the {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		<T, R> ResponseEntity<T> invoke(HttpMethod method, RequestEntity<R> requestEntity,
				ResponseType<T> responseType);

		/**
		 * Invoke the request and receive a response back only if the response has a <em>success</em> (<code>2xx</code>)
		 * status code.
		 * @param <T> Response type
		 * @param <R> Request entity type
		 * @param method Request method
		 * @param requestEntity Request entity
		 * @param responseType Expected response payload type
		 * @return the {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		<T, R> ResponseEntity<T> invokeForSuccess(HttpMethod method, RequestEntity<R> requestEntity,
				ResponseType<T> responseType);

		/**
		 * Invoke the request and receive back the response content entity.
		 * @param <T> Response type
		 * @param <R> Request entity type
		 * @param method Request method
		 * @param requestEntity Request entity
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		<T, R> Optional<T> invokeForEntity(HttpMethod method, RequestEntity<R> requestEntity,
				ResponseType<T> responseType);

		// GET

		/**
		 * Invoke the request using <code>GET</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> get(Class<T> responseType) {
			return invoke(HttpMethod.GET, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>GET</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> get(ResponseType<T> responseType) {
			return invoke(HttpMethod.GET, null, responseType);
		}

		/**
		 * Invoke the request using <code>GET</code> method and receive the response entity payload back.
		 * @param <T> Response entity type
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> getForEntity(Class<T> responseType) {
			return invokeForEntity(HttpMethod.GET, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>GET</code> method and receive the response entity payload of given generic
		 * type back.
		 * @param <T> Response entity type
		 * @param responseType Response payload generic type representation
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> getForEntity(ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.GET, null, responseType);
		}

		/**
		 * Invoke the request using <code>GET</code> method and receive the response entity {@link InputStream} back.
		 * @return the response payload {@link InputStream}, or an empty stream for empty responses
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default InputStream getForStream() {
			return invokeForEntity(HttpMethod.GET, null, ResponseType.of(InputStream.class))
					.orElse(new ByteArrayInputStream(new byte[0]));
		}

		/**
		 * Convenience method to invoke the request using <code>GET</code> method and receive a response entity payload
		 * of {@link List} type back.
		 * @param <T> Response entity type
		 * @param responseType Expected {@link List} response type
		 * @return the response payload of expected type as the result of the request invocation, or an empty List if
		 *         not present
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> List<T> getAsList(Class<T> responseType) {
			final ResponseType<List<T>> rt = ResponseType.of(responseType, List.class);
			return getForEntity(rt).orElse(Collections.emptyList());
		}

		// POST

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and receive
		 * a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @param entity Request payload
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default ResponseEntity<Void> post(RequestEntity<?> entity) {
			return invoke(HttpMethod.POST, entity, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and receive
		 * a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> post(RequestEntity<?> entity, Class<T> responseType) {
			return invoke(HttpMethod.POST, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and receive
		 * a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> post(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invoke(HttpMethod.POST, entity, responseType);
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and receive
		 * the response payload back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> postForEntity(RequestEntity<?> entity, Class<T> responseType) {
			return invokeForEntity(HttpMethod.POST, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and receive
		 * the response payload back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> postForEntity(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.POST, entity, responseType);
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and receive
		 * the value of the <code>LOCATION</code> header back, if present.
		 * @param entity Request payload
		 * @return the value of the <code>LOCATION</code> header back, if present
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default Optional<URI> postForLocation(RequestEntity<?> entity) {
			return invokeForSuccess(HttpMethod.POST, entity, ResponseType.of(Void.class)).getLocation();
		}

		// PUT

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and receive a
		 * response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @param entity Request payload
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default ResponseEntity<Void> put(RequestEntity<?> entity) {
			return invoke(HttpMethod.PUT, entity, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and receive a
		 * response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> put(RequestEntity<?> entity, Class<T> responseType) {
			return invoke(HttpMethod.PUT, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and receive a
		 * response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> put(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invoke(HttpMethod.PUT, entity, responseType);
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and receive
		 * the response entity payload back.
		 * @param <T> Response entity type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> putForEntity(RequestEntity<?> entity, Class<T> responseType) {
			return invokeForEntity(HttpMethod.PUT, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request entity payload and
		 * receive the response payload back.
		 * @param <T> Response entity type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> putForEntity(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.PUT, entity, responseType);
		}

		// PATCH

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and receive
		 * a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @param entity Request payload
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default ResponseEntity<Void> patch(RequestEntity<?> entity) {
			return invoke(HttpMethod.PATCH, entity, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and receive
		 * a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> ResponseEntity<T> patch(RequestEntity<?> entity, Class<T> responseType) {
			return invoke(HttpMethod.PATCH, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and receive
		 * a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> ResponseEntity<T> patch(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invoke(HttpMethod.PATCH, entity, responseType);
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and receive
		 * the response entity payload back.
		 * @param <T> Response entity type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> patchForEntity(RequestEntity<?> entity, Class<T> responseType) {
			return invokeForEntity(HttpMethod.PATCH, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and receive
		 * the response entity payload back.
		 * @param <T> Response entity type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> patchForEntity(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.PATCH, entity, responseType);
		}

		// DELETE

		/**
		 * Invoke the request using <code>DELETE</code> method and receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel)
		 */
		default ResponseEntity<Void> delete() {
			return invoke(HttpMethod.DELETE, null, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> delete(Class<T> responseType) {
			return invoke(HttpMethod.DELETE, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> delete(ResponseType<T> responseType) {
			return invoke(HttpMethod.DELETE, null, responseType);
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and receive the response entity payload back.
		 * @param <T> Response entity type
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> deleteForEntity(Class<T> responseType) {
			return invokeForEntity(HttpMethod.DELETE, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and receive the response entity payload of given generic
		 * type back.
		 * @param <T> Response entity type
		 * @param responseType Response payload generic type representation
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> deleteForEntity(ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.DELETE, null, responseType);
		}

		// OPTIONS

		/**
		 * Invoke the request using <code>OPTIONS</code> method and receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel)
		 */
		default ResponseEntity<Void> options() {
			return invoke(HttpMethod.OPTIONS, null, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> options(Class<T> responseType) {
			return invoke(HttpMethod.OPTIONS, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> options(ResponseType<T> responseType) {
			return invoke(HttpMethod.OPTIONS, null, responseType);
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and receive the response entity payload back.
		 * @param <T> Response entity type
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> optionsForEntity(Class<T> responseType) {
			return invokeForEntity(HttpMethod.OPTIONS, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and receive the response entity payload of given generic
		 * type back.
		 * @param <T> Response entity type
		 * @param responseType Response payload generic type representation
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> optionsForEntity(ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.OPTIONS, null, responseType);
		}

		// TRACE

		/**
		 * Invoke the request using <code>TRACE</code> method and receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default ResponseEntity<Void> trace() {
			return invoke(HttpMethod.TRACE, null, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> trace(Class<T> responseType) {
			return invoke(HttpMethod.TRACE, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default <T> ResponseEntity<T> trace(ResponseType<T> responseType) {
			return invoke(HttpMethod.TRACE, null, responseType);
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and receive the response entity payload back.
		 * @param <T> Response entity type
		 * @param responseType Expected response payload type
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> traceForEntity(Class<T> responseType) {
			return invokeForEntity(HttpMethod.TRACE, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and receive the response entity payload of given generic
		 * type back.
		 * @param <T> Response entity type
		 * @param responseType Response payload generic type representation
		 * @return the response payload of expected type as the result of the request invocation, or an empty Optional
		 *         if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> traceForEntity(ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.TRACE, null, responseType);
		}

		// HEAD

		/**
		 * Invoke the request using <code>HEAD</code> method and receive a response back.
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		default ResponseEntity<Void> head() {
			return invoke(HttpMethod.HEAD, null, ResponseType.of(Void.class));
		}

	}

	/**
	 * Request message definition and response invocation.
	 */
	public interface RequestDefinition extends Invocation, Serializable {

		/**
		 * Set request base URI. URI template parameters are not supported, use {@link #path(String)} to add URI paths
		 * to base uri with template parameters support.
		 * @param baseUri Request base URI (not null)
		 * @return this
		 */
		RequestDefinition target(URI baseUri);

		/**
		 * Append given <code>path</code> to the request URI.
		 * <p>
		 * May contain URI template parameters, resolved using the values supplied by {@link #resolve(String, Object)}
		 * methods.
		 * </p>
		 * <p>
		 * A '/' separator will be inserted between the existing path and the supplied path if necessary.
		 * </p>
		 * @param path The path to append, may contain URI template parameters
		 * @return this
		 */
		RequestDefinition path(String path);

		/**
		 * Add a query parameter to the request URI. If multiple values are supplied the parameter will be added once
		 * per value.
		 * @param name Query parameter name (not null)
		 * @param values Query parameter value(s)
		 * @return this
		 * @throws IllegalArgumentException If the supplied query parameter name is <code>null</code>
		 */
		RequestDefinition queryParameter(String name, Object... values);

		/**
		 * Resolve a URI template with given <code>name</code> in the request URI using a supplied value.
		 * @param name Name of the URI template (not null)
		 * @param value Value to be used to resolve the template (not null)
		 * @return this
		 * @throws IllegalArgumentException If the supplied name or value is <code>null</code>
		 */
		RequestDefinition resolve(String name, Object value);

		/**
		 * Resolve one or more a URI templates in the request URI using supplied name-value pairs.
		 * @param nameAndValues A map of URI template names and their values
		 * @return this
		 * @throws IllegalArgumentException If the name-value map or any of the names or values in the map is
		 *         <code>null</code>
		 */
		RequestDefinition resolve(Map<String, Object> nameAndValues);

		/**
		 * Set the accepted response media types. Any previous value will be replaced.
		 * <p>
		 * A {@link HttpHeaders#ACCEPT} header will be added to request with given values.
		 * </p>
		 * @param mediaTypes Accepted response media types
		 * @return this
		 */
		RequestDefinition accept(String... mediaTypes);

		/**
		 * Set the accepted response media types using {@link MediaType} enumeration. Any previous value will be
		 * replaced.
		 * <p>
		 * A {@link HttpHeaders#ACCEPT} header will be added to request with given values.
		 * </p>
		 * @param mediaTypes Accepted response media types.
		 * @return this
		 */
		RequestDefinition accept(MediaType... mediaTypes);

		/**
		 * Set the acceptable languages. Any previous value will be replaced.
		 * <p>
		 * A {@link HttpHeaders#ACCEPT_LANGUAGE} header will be added to request with given values.
		 * </p>
		 * @param locales Acceptable languages {@link Locale}
		 * @return this
		 */
		RequestDefinition acceptLanguage(Locale... locales);

		/**
		 * Set the acceptable languages. Any previous value will be replaced.
		 * <p>
		 * A {@link HttpHeaders#ACCEPT_LANGUAGE} header will be added to request with given values.
		 * </p>
		 * @param locales Acceptable languages
		 * @return this
		 */
		RequestDefinition acceptLanguage(String... locales);

		/**
		 * Set the acceptable encodings. Any previous value will be replaced.
		 * <p>
		 * A {@link HttpHeaders#ACCEPT_ENCODING} header will be added to request with given values.
		 * </p>
		 * @param encodings Acceptable encodings
		 * @return this
		 */
		RequestDefinition acceptEncoding(String... encodings);

		/**
		 * Set the acceptable charsets. Any previous value will be replaced.
		 * <p>
		 * A {@link HttpHeaders#ACCEPT_CHARSET} header will be added to request with given values.
		 * </p>
		 * @param charsets Acceptable charsets
		 * @return this
		 */
		RequestDefinition acceptCharset(String... charsets);

		/**
		 * Set the acceptable charsets. Any previous value will be replaced.
		 * <p>
		 * A {@link HttpHeaders#ACCEPT_CHARSET} header will be added to request with given values.
		 * </p>
		 * @param charsets Acceptable charsets
		 * @return this
		 */
		RequestDefinition acceptCharset(Charset... charsets);

		/**
		 * Set the Cache-Control header. Any previous value will be replaced.
		 * @param cacheControl the cache control directives
		 * @return this
		 */
		RequestDefinition cacheControl(CacheControl cacheControl);

		/**
		 * Add an {@link HttpHeaders#AUTHORIZATION} header to request with scheme {@link HttpHeaders#SCHEME_BEARER}
		 * using given bearer token.
		 * @param bearerToken Bearer token to set in authorization header (not null)
		 * @return this
		 */
		RequestDefinition authorizationBearer(String bearerToken);

		/**
		 * Add an {@link HttpHeaders#AUTHORIZATION} header to request with scheme {@link HttpHeaders#SCHEME_BASIC} using
		 * given username and password.
		 * @param username Username (not null)
		 * @param password Password (not null)
		 * @return this
		 */
		RequestDefinition authorizationBasic(String username, String password);

		/**
		 * Add a request header. If an header with the same <code>name</code> is already defined, it will be replaced by
		 * the new value.
		 * @param name Name of the header
		 * @param values Value of the header. If multiple values are supplied, the actual header value will be composed
		 *        by the given values in the specified order separated by a <code>,</code>
		 * @return this
		 */
		RequestDefinition header(String name, String... values);

		/**
		 * Use given {@link Property} set to perform invocation. Can be used for {@link PropertyBox} deserialization
		 * when expected response payload contains serialized {@link PropertyBox} data.
		 * @param <P> Actual property type
		 * @param properties Property set to use (not null)
		 * @return this
		 */
		@SuppressWarnings("rawtypes")
		<P extends Property> RequestDefinition propertySet(Iterable<P> properties);

		/**
		 * Use given {@link Property} set to perform invocation. Can be used for {@link PropertyBox} deserialization
		 * when expected response payload contains serialized {@link PropertyBox} data.
		 * @param <P> Actual property type
		 * @param properties Property set to use (not null)
		 * @return this
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		<P extends Property> RequestDefinition propertySet(P... properties);

		// getters

		/**
		 * Get the request base URI
		 * @return Request base URI
		 */
		Optional<URI> getBaseRequestURI();

		/**
		 * Get the request path to append to the base URI. May contain template parameters.
		 * @return Request path
		 */
		Optional<String> getRequestPath();

		/**
		 * Get the full request URI, composed by {@link #getBaseRequestURI()} and {@link #getRequestPath()}, including
		 * any (not resolved) template parameter.
		 * @return Full request URI
		 */
		String getRequestURI();

		/**
		 * Get the URI template parameters to resolve
		 * @return URI template parameters name-value map, an empty map if none
		 */
		Map<String, Object> getTemplateParameters();

		/**
		 * Get the URI query parameters
		 * @return URI query parameters name-(multi)value map, an empty map if none
		 */
		Map<String, Object[]> getQueryParameters();

		/**
		 * Get the request headers
		 * @return Request headers name-value map, an empty map if none
		 */
		Map<String, String> getHeaders();

		/**
		 * Get the {@link PropertySet} to use to deserialize any {@link PropertyBox} contained in the response payload
		 * @return Optional PropertySet
		 */
		Optional<PropertySet<?>> getPropertySet();

	}

	/**
	 * Invoker to perform a client request in order to consume a response using a {@link RequestDefinition}.
	 */
	public interface Invoker {

		/**
		 * Invoke request using current given <code>request</code> definition.
		 * @param <T> Response type
		 * @param <R> Request entity type
		 * @param requestDefinition Request definition
		 * @param method Request method
		 * @param requestEntity Request message payload
		 * @param responseType Expected response payload type
		 * @param onlySuccessfulStatusCode <code>true</code> to return only <code>2xx</code> status code response and
		 *        throw an {@link UnsuccessfulResponseException} otherwise, <code>false</code> to return any status code
		 *        responses
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 */
		<T, R> ResponseEntity<T> invoke(RequestDefinition requestDefinition, HttpMethod method,
				RequestEntity<R> requestEntity, ResponseType<T> responseType, boolean onlySuccessfulStatusCode);

	}

	/**
	 * Set the default target request base URI, which will be used as target URI for every request configured using
	 * {@link #request()}, if not overridden using {@link RequestDefinition#target(URI)}.
	 * <p>
	 * URI template parameters are not supported for the base target URI.
	 * </p>
	 * @param baseUri Default target request base URI
	 * @return The updated RestClient
	 */
	RestClient defaultTarget(URI baseUri);

	/**
	 * Get the default target request base URI, which will be used as target URI for every request configured using
	 * {@link #request()}, if not overridden using {@link RequestDefinition#target(URI)}.
	 * @return Default target request base URI, if setted
	 */
	Optional<URI> getDefaultTarget();

	/**
	 * Add a default request header which will be automatically added to every invocation request message, if an header
	 * with the same name is not already present.
	 * @param name Header name (not null)
	 * @param value Header value
	 * @return The updated RestClient
	 */
	RestClient withDefaultHeader(String name, String value);

	/**
	 * Removes the default header with given <code>name</code>, if present
	 * @param name Header name to remove (not null)
	 * @return The updated RestClient
	 */
	RestClient removeDefaultHeader(String name);

	/**
	 * Removes all default headers
	 * @return The updated RestClient
	 */
	RestClient clearDefaultHeaders();

	/**
	 * Create a new request definition, to be used to configure request and invoke response.
	 * <p>
	 * If a default target request URI is configured using {@link #defaultTarget(URI)}, this will be used as request
	 * base URI. The target URI can be overridden using {@link RequestDefinition#target(URI)}.
	 * </p>
	 * @return Request definition builder
	 */
	RequestDefinition request();

}
