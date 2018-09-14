/*
 * Copyright 2016-2018 Axioma srl.
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
package com.holonplatform.async.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.holonplatform.async.http.AsyncRestClient.AsyncRequestDefinition;
import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.exceptions.HttpClientInvocationException;
import com.holonplatform.http.exceptions.UnsuccessfulResponseException;
import com.holonplatform.http.rest.RequestEntity;
import com.holonplatform.http.rest.ResponseEntity;
import com.holonplatform.http.rest.ResponseType;
import com.holonplatform.http.rest.RestClientOperations;
import com.holonplatform.http.rest.RestClientOperations.InvocationOperations;
import com.holonplatform.http.rest.RestClientOperations.RequestConfiguration;

/**
 * TODO
 */
public interface AsyncRestClient extends RestClientOperations<AsyncRestClient, AsyncRequestDefinition> {

	/**
	 * Invocation operations
	 */
	public interface AsyncInvocation extends
			InvocationOperations<CompletionStage<?>, CompletionStage<?>, CompletionStage<InputStream>, CompletionStage<?>, CompletionStage<Optional<URI>>> {

		/**
		 * Invoke the request and asynchronously receive a response back.
		 * <p>
		 * The response payload is processed and possibly converted by concrete client implementation.
		 * </p>
		 * @param <T> Response type
		 * @param <R> Request entity type
		 * @param method Request method
		 * @param requestEntity Request entity
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as the result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		<T, R> CompletionStage<ResponseEntity<T>> invoke(HttpMethod method, RequestEntity<R> requestEntity,
				ResponseType<T> responseType);

		/**
		 * Invoke the request and asynchronously receive a response back only if the response has a <em>success</em>
		 * (<code>2xx</code>) status code.
		 * @param <T> Response type
		 * @param <R> Request entity type
		 * @param method Request method
		 * @param requestEntity Request entity
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		<T, R> CompletionStage<ResponseEntity<T>> invokeForSuccess(HttpMethod method, RequestEntity<R> requestEntity,
				ResponseType<T> responseType);

		/**
		 * Invoke the request and asynchronously receive back the response content entity.
		 * @param <T> Response type
		 * @param <R> Request entity type
		 * @param method Request method
		 * @param requestEntity Request entity
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		<T, R> CompletionStage<Optional<T>> invokeForEntity(HttpMethod method, RequestEntity<R> requestEntity,
				ResponseType<T> responseType);

		// GET

		/**
		 * Invoke the request using <code>GET</code> method and asynchronously receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> get(Class<T> responseType) {
			return invoke(HttpMethod.GET, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>GET</code> method and asynchronously receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> get(ResponseType<T> responseType) {
			return invoke(HttpMethod.GET, null, responseType);
		}

		/**
		 * Invoke the request using <code>GET</code> method and asynchronously receive the response entity payload back.
		 * @param <T> Response entity type
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> getForEntity(Class<T> responseType) {
			return invokeForEntity(HttpMethod.GET, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>GET</code> method and asynchronously receive the response entity payload of
		 * given generic type back.
		 * @param <T> Response entity type
		 * @param responseType Response payload generic type representation
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> getForEntity(ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.GET, null, responseType);
		}

		/**
		 * Invoke the request using <code>GET</code> method and asynchronously receive the response entity
		 * {@link InputStream} back.
		 * @return A {@link CompletionStage} to handle the response payload {@link InputStream}, or an empty stream for
		 *         empty responses
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default CompletionStage<InputStream> getForStream() {
			return invokeForEntity(HttpMethod.GET, null, ResponseType.of(InputStream.class))
					.thenApply(r -> r.orElse(new ByteArrayInputStream(new byte[0])));
		}

		/**
		 * Convenience method to invoke the request using <code>GET</code> method and asynchronously receive a response
		 * entity payload of {@link List} type back.
		 * @param <T> Response entity type
		 * @param responseType Expected {@link List} response type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty List if not present
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<List<T>> getAsList(Class<T> responseType) {
			final ResponseType<List<T>> rt = ResponseType.of(responseType, List.class);
			return getForEntity(rt).thenApply(r -> r.orElse(Collections.emptyList()));
		}

		// POST

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and
		 * asynchronously receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @param entity Request payload
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default CompletionStage<ResponseEntity<Void>> post(RequestEntity<?> entity) {
			return invoke(HttpMethod.POST, entity, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and
		 * asynchronously receive a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> post(RequestEntity<?> entity, Class<T> responseType) {
			return invoke(HttpMethod.POST, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and
		 * asynchronously receive a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> post(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invoke(HttpMethod.POST, entity, responseType);
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and
		 * asynchronously receive the response payload back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> postForEntity(RequestEntity<?> entity, Class<T> responseType) {
			return invokeForEntity(HttpMethod.POST, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and
		 * asynchronously receive the response payload back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> postForEntity(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.POST, entity, responseType);
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and
		 * asynchronously receive the value of the <code>LOCATION</code> header back, if present.
		 * @param entity Request payload
		 * @return A {@link CompletionStage} to handle the value of the <code>LOCATION</code> header back, if present
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default CompletionStage<Optional<URI>> postForLocation(RequestEntity<?> entity) {
			return invokeForSuccess(HttpMethod.POST, entity, ResponseType.of(Void.class))
					.thenApply(r -> r.getLocation());
		}

		// PUT

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and
		 * asynchronously receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @param entity Request payload
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default CompletionStage<ResponseEntity<Void>> put(RequestEntity<?> entity) {
			return invoke(HttpMethod.PUT, entity, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and
		 * asynchronously receive a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> put(RequestEntity<?> entity, Class<T> responseType) {
			return invoke(HttpMethod.PUT, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and
		 * asynchronously receive a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> put(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invoke(HttpMethod.PUT, entity, responseType);
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and
		 * asynchronously receive the response entity payload back.
		 * @param <T> Response entity type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> putForEntity(RequestEntity<?> entity, Class<T> responseType) {
			return invokeForEntity(HttpMethod.PUT, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request entity payload and
		 * asynchronously receive the response payload back.
		 * @param <T> Response entity type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> putForEntity(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.PUT, entity, responseType);
		}

		// PATCH

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and
		 * asynchronously receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @param entity Request payload
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default CompletionStage<ResponseEntity<Void>> patch(RequestEntity<?> entity) {
			return invoke(HttpMethod.PATCH, entity, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and
		 * asynchronously receive a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> patch(RequestEntity<?> entity, Class<T> responseType) {
			return invoke(HttpMethod.PATCH, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and
		 * asynchronously receive a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> patch(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invoke(HttpMethod.PATCH, entity, responseType);
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and
		 * asynchronously receive the response entity payload back.
		 * @param <T> Response entity type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> patchForEntity(RequestEntity<?> entity, Class<T> responseType) {
			return invokeForEntity(HttpMethod.PATCH, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and
		 * asynchronously receive the response entity payload back.
		 * @param <T> Response entity type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> patchForEntity(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.PATCH, entity, responseType);
		}

		// DELETE

		/**
		 * Invoke the request using <code>DELETE</code> method and asynchronously receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel)
		 */
		@Override
		default CompletionStage<ResponseEntity<Void>> delete() {
			return invoke(HttpMethod.DELETE, null, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request asynchronously using the <code>DELETE</code> method. If the returned response is not a
		 * <em>success</em> response (i.e. with a <code>2xx</code> status code), a {@link UnsuccessfulResponseException}
		 * is thrown.
		 * @return A {@link CompletionStage} to handle the response
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		default CompletionStage<Void> deleteOrFail() {
			return invokeForSuccess(HttpMethod.DELETE, null, ResponseType.of(Void.class)).thenApply(r -> null);
		}

		/**
		 * Invoke the request asynchronously using the <code>DELETE</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> delete(Class<T> responseType) {
			return invoke(HttpMethod.DELETE, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and asynchronously receive a response back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> delete(ResponseType<T> responseType) {
			return invoke(HttpMethod.DELETE, null, responseType);
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and asynchronously receive the response entity payload
		 * back.
		 * @param <T> Response entity type
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> deleteForEntity(Class<T> responseType) {
			return invokeForEntity(HttpMethod.DELETE, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and asynchronously receive the response entity payload of
		 * given generic type back.
		 * @param <T> Response entity type
		 * @param responseType Response payload generic type representation
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> deleteForEntity(ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.DELETE, null, responseType);
		}

		// OPTIONS

		/**
		 * Invoke the request using <code>OPTIONS</code> method and asynchronously receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel)
		 */
		@Override
		default CompletionStage<ResponseEntity<Void>> options() {
			return invoke(HttpMethod.OPTIONS, null, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and asynchronously receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> options(Class<T> responseType) {
			return invoke(HttpMethod.OPTIONS, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and asynchronously receive a response back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> options(ResponseType<T> responseType) {
			return invoke(HttpMethod.OPTIONS, null, responseType);
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and asynchronously receive the response entity payload
		 * back.
		 * @param <T> Response entity type
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> optionsForEntity(Class<T> responseType) {
			return invokeForEntity(HttpMethod.OPTIONS, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and asynchronously receive the response entity payload
		 * of given generic type back.
		 * @param <T> Response entity type
		 * @param responseType Response payload generic type representation
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> optionsForEntity(ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.OPTIONS, null, responseType);
		}

		// TRACE

		/**
		 * Invoke the request using <code>TRACE</code> method and asynchronously receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default CompletionStage<ResponseEntity<Void>> trace() {
			return invoke(HttpMethod.TRACE, null, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and asynchronously receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> trace(Class<T> responseType) {
			return invoke(HttpMethod.TRACE, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and asynchronously receive a response back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default <T> CompletionStage<ResponseEntity<T>> trace(ResponseType<T> responseType) {
			return invoke(HttpMethod.TRACE, null, responseType);
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and asynchronously receive the response entity payload
		 * back.
		 * @param <T> Response entity type
		 * @param responseType Expected response payload type
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> traceForEntity(Class<T> responseType) {
			return invokeForEntity(HttpMethod.TRACE, null, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and asynchronously receive the response entity payload of
		 * given generic type back.
		 * @param <T> Response entity type
		 * @param responseType Response payload generic type representation
		 * @return A {@link CompletionStage} to handle the response payload of expected type as the result of the
		 *         request invocation, or an empty Optional if not present (empty response entity)
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 * @throws UnsuccessfulResponseException In case the status code of the response returned by the server is not a
		 *         successful type status code, i.e. it is not a <code>2xx</code> status code
		 */
		@Override
		default <T> CompletionStage<Optional<T>> traceForEntity(ResponseType<T> responseType) {
			return invokeForEntity(HttpMethod.TRACE, null, responseType);
		}

		// HEAD

		/**
		 * Invoke the request using <code>HEAD</code> method and asynchronously receive a response back.
		 * @return A {@link CompletionStage} to handle the {@link ResponseEntity} object as a result of the request
		 *         invocation
		 * @throws HttpClientInvocationException Internal invocation failure (for example, an I/O error on communication
		 *         channel or expected and actual payload type mismatch)
		 */
		@Override
		default CompletionStage<ResponseEntity<Void>> head() {
			return invoke(HttpMethod.HEAD, null, ResponseType.of(Void.class));
		}

	}

	/**
	 * Request message definition and response invocation.
	 */
	public interface AsyncRequestDefinition extends RequestConfiguration<AsyncRequestDefinition>, AsyncInvocation {

	}

	// Builders

	// TODO

}
