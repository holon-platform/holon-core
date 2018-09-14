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
package com.holonplatform.http.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.exceptions.HttpClientInvocationException;
import com.holonplatform.http.exceptions.RestClientCreationException;
import com.holonplatform.http.exceptions.UnsuccessfulResponseException;
import com.holonplatform.http.internal.rest.RestClientFactoryRegistry;
import com.holonplatform.http.rest.RestClient.RequestDefinition;
import com.holonplatform.http.rest.RestClientOperations.InvocationOperations;
import com.holonplatform.http.rest.RestClientOperations.RequestConfiguration;

/**
 * HTTP REST client to build and execute client requests in order to consume the responses returned.
 *
 * @since 5.0.0
 */
public interface RestClient extends RestClientOperations<RestClient, RequestDefinition> {

	/**
	 * Invocation operations
	 */
	public interface Invocation
			extends InvocationOperations<ResponseEntity<?>, Optional<?>, InputStream, List<?>, Optional<URI>> {

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
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		@Override
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
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> Optional<T> getForEntity(ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.GET, null, responseType);
		}

		/**
		 * Invoke the request using <code>GET</code> method and receive the response entity {@link InputStream} back.
		 * @return the response payload {@link InputStream}, or an empty stream for empty responses
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		@Override
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
		@Override
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
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		@Override
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
		@Override
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
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		@Override
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
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		@Override
		default ResponseEntity<Void> delete() {
			return invoke(HttpMethod.DELETE, null, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>DELETE</code> method. If the returned response is not a <em>success</em>
		 * response (i.e. with a <code>2xx</code> status code), a {@link UnsuccessfulResponseException} is thrown.
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		default void deleteOrFail() {
			invokeForSuccess(HttpMethod.DELETE, null, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return {@link ResponseEntity} object as a result of the request invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
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
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		@Override
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
		@Override
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
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		@Override
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
		@Override
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
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
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
		@Override
		default ResponseEntity<Void> head() {
			return invoke(HttpMethod.HEAD, null, ResponseType.of(Void.class));
		}

	}

	/**
	 * Request message definition and response invocation.
	 */
	public interface RequestDefinition extends RequestConfiguration<RequestDefinition>, Invocation {

	}

	// Builders

	/**
	 * Create a new {@link RestClient} instance using default {@link ClassLoader} and default implementation, setting
	 * given <code>baseUri</code> as default {@link RestClient} target, which will be used as base URI for every request
	 * configured using {@link #request()}, if not overridden using {@link RequestDefinition#target(URI)}.
	 * @param baseUri The base target URI of the returned {@link RestClient}
	 * @return A new {@link RestClient} instance
	 */
	static RestClient forTarget(String baseUri) {
		return create().defaultTarget(URI.create(baseUri));
	}

	/**
	 * Create a new {@link RestClient} instance using default {@link ClassLoader} and default implementation, setting
	 * given <code>baseUri</code> as default {@link RestClient} target, which will be used as base URI for every request
	 * configured using {@link #request()}, if not overridden using {@link RequestDefinition#target(URI)}.
	 * @param baseUri The base target URI of the returned {@link RestClient}
	 * @return A new {@link RestClient} instance
	 */
	static RestClient forTarget(URI baseUri) {
		return create().defaultTarget(baseUri);
	}

	/**
	 * Create a new {@link RestClient} instance using default {@link ClassLoader} and default implementation, if
	 * available. If more than one {@link RestClient} implementation is found using given ClassLoader, the one returned
	 * by the {@link RestClientFactory} with the higher priority is returned.
	 * @return A new {@link RestClient} instance
	 * @throws RestClientCreationException If a {@link RestClient} implementation is not available or a instance
	 *         creation error occurred
	 */
	static RestClient create() {
		return create(null, ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Create a new {@link RestClient} instance using given <code>classLoder</code> and default implementation, if
	 * available. If more than one {@link RestClient} implementation is found using given ClassLoader, the one returned
	 * by the {@link RestClientFactory} with the higher priority is returned.
	 * @param classLoader The {@link ClassLoader} to use
	 * @return A new {@link RestClient} instance
	 * @throws RestClientCreationException If a {@link RestClient} implementation is not available or a instance
	 *         creation error occurred
	 */
	static RestClient create(ClassLoader classLoader) {
		return create(null, classLoader);
	}

	/**
	 * Create a new {@link RestClient} instance using default {@link ClassLoader} and the implementation whith given
	 * fully qualified class name.
	 * @param fullyQualifiedClassName The {@link RestClient} implementation fully qualified class name to obtain
	 * @return A new {@link RestClient} instance
	 * @throws RestClientCreationException If the implementation which corresponds to given fully qualified class name
	 *         is not available or a instance creation error occurred
	 */
	static RestClient create(String fullyQualifiedClassName) {
		return create(fullyQualifiedClassName, ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Create a new {@link RestClient} instance using given <code>classLoder</code> and the implementation whith given
	 * fully qualified class name.
	 * @param fullyQualifiedClassName The {@link RestClient} implementation fully qualified class name to obtain
	 * @param classLoader The {@link ClassLoader} to use
	 * @return A new {@link RestClient} instance
	 * @throws RestClientCreationException If the implementation which corresponds to given fully qualified class name
	 *         is not available or a instance creation error occurred
	 */
	static RestClient create(String fullyQualifiedClassName, ClassLoader classLoader) {
		return RestClientFactoryRegistry.INSTANCE.createRestClient(fullyQualifiedClassName, classLoader);
	}

}
