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
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		<T, R> HttpResponse<T> invoke(HttpMethod method, RequestEntity<R> requestEntity, ResponseType<T> responseType);

		/**
		 * Invoke the request and receive a response back.
		 * <p>
		 * The response payload is processed and possibly converted by concrete client implementation.
		 * </p>
		 * @param <T> Response type
		 * @param method Request method
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		default <T> HttpResponse<T> invoke(HttpMethod method, ResponseType<T> responseType) {
			return invoke(method, null, responseType);
		}

		// GET

		/**
		 * Invoke the request using <code>GET</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		default <T> HttpResponse<T> getResponse(Class<T> responseType) {
			return invoke(HttpMethod.GET, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>GET</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		default <T> HttpResponse<T> getResponse(ResponseType<T> responseType) {
			return invoke(HttpMethod.GET, responseType);
		}

		/**
		 * Invoke the request using <code>GET</code> method and receive the response payload back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> get(Class<T> responseType) {
			return getResponseContent(invoke(HttpMethod.GET, ResponseType.of(responseType)));
		}

		/**
		 * Invoke the request using <code>GET</code> method and receive the response payload of given generic type back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> get(ResponseType<T> responseType) {
			return getResponseContent(invoke(HttpMethod.GET, responseType));
		}

		/**
		 * Convenience method to invoke the request using <code>GET</code> method and receive a response payload of
		 * {@link List} type back.
		 * @param <T> Response type
		 * @param responseType Expected {@link List} response type
		 * @return Response payload object of the specified type as a result of the request invocation, or an empty List
		 *         if not present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> List<T> getAsList(Class<T> responseType) {
			final ResponseType<List<T>> rt = ResponseType.of(responseType, List.class);
			return get(rt).orElse(Collections.emptyList());
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
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		default HttpResponse<Void> post(RequestEntity<?> entity) {
			return invoke(HttpMethod.POST, entity, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and receive
		 * a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> postForResponse(RequestEntity<?> entity, Class<T> responseType) {
			return invoke(HttpMethod.POST, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and receive
		 * a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> postForResponse(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invoke(HttpMethod.POST, entity, responseType);
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and receive
		 * the response payload back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> post(RequestEntity<?> entity, Class<T> responseType) {
			return getResponseContent(invoke(HttpMethod.POST, entity, ResponseType.of(responseType)));
		}

		/**
		 * Invoke the request using <code>POST</code> method with given <code>entity</code> request payload and receive
		 * the response payload back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> post(RequestEntity<?> entity, ResponseType<T> responseType) {
			return getResponseContent(invoke(HttpMethod.POST, entity, responseType));
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
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		default HttpResponse<Void> put(RequestEntity<?> entity) {
			return invoke(HttpMethod.PUT, entity, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and receive a
		 * response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> putForResponse(RequestEntity<?> entity, Class<T> responseType) {
			return invoke(HttpMethod.PUT, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and receive a
		 * response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> putForResponse(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invoke(HttpMethod.PUT, entity, responseType);
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and receive
		 * the response payload back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> put(RequestEntity<?> entity, Class<T> responseType) {
			return getResponseContent(invoke(HttpMethod.PUT, entity, ResponseType.of(responseType)));
		}

		/**
		 * Invoke the request using <code>PUT</code> method with given <code>entity</code> request payload and receive
		 * the response payload back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> put(RequestEntity<?> entity, ResponseType<T> responseType) {
			return getResponseContent(invoke(HttpMethod.PUT, entity, responseType));
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
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		default HttpResponse<Void> patch(RequestEntity<?> entity) {
			return invoke(HttpMethod.PATCH, entity, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and receive
		 * a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> patchForResponse(RequestEntity<?> entity, Class<T> responseType) {
			return invoke(HttpMethod.PATCH, entity, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and receive
		 * a response back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> patchForResponse(RequestEntity<?> entity, ResponseType<T> responseType) {
			return invoke(HttpMethod.PATCH, entity, responseType);
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and receive
		 * the response payload back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> patch(RequestEntity<?> entity, Class<T> responseType) {
			return getResponseContent(invoke(HttpMethod.PATCH, entity, ResponseType.of(responseType)));
		}

		/**
		 * Invoke the request using <code>PATCH</code> method with given <code>entity</code> request payload and receive
		 * the response payload back.
		 * @param <T> Response type
		 * @param entity Request payload
		 * @param responseType Expected response payload type
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> patch(RequestEntity<?> entity, ResponseType<T> responseType) {
			return getResponseContent(invoke(HttpMethod.PATCH, entity, responseType));
		}

		// DELETE

		/**
		 * Invoke the request using <code>DELETE</code> method and receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		default HttpResponse<Void> delete() {
			return invoke(HttpMethod.DELETE, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> deleteForResponse(Class<T> responseType) {
			return invoke(HttpMethod.DELETE, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> deleteForResponse(ResponseType<T> responseType) {
			return invoke(HttpMethod.DELETE, responseType);
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and receive the response payload back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> delete(Class<T> responseType) {
			return getResponseContent(invoke(HttpMethod.DELETE, ResponseType.of(responseType)));
		}

		/**
		 * Invoke the request using <code>DELETE</code> method and receive the response payload of given generic type
		 * back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> delete(ResponseType<T> responseType) {
			return getResponseContent(invoke(HttpMethod.DELETE, responseType));
		}

		// OPTIONS

		/**
		 * Invoke the request using <code>OPTIONS</code> method and receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		default HttpResponse<Void> options() {
			return invoke(HttpMethod.OPTIONS, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> optionsForResponse(Class<T> responseType) {
			return invoke(HttpMethod.OPTIONS, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> optionsForResponse(ResponseType<T> responseType) {
			return invoke(HttpMethod.OPTIONS, responseType);
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and receive the response payload back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> options(Class<T> responseType) {
			return getResponseContent(invoke(HttpMethod.OPTIONS, ResponseType.of(responseType)));
		}

		/**
		 * Invoke the request using <code>OPTIONS</code> method and receive the response payload of given generic type
		 * back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> options(ResponseType<T> responseType) {
			return getResponseContent(invoke(HttpMethod.OPTIONS, responseType));
		}

		// TRACE

		/**
		 * Invoke the request using <code>TRACE</code> method and receive a response back.
		 * <p>
		 * The response type is conventionally of {@link Void} type, because no response paylod is expected from this
		 * invocation. Refer to the other <code>post</code> methods to obtain a response payload.
		 * </p>
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		default HttpResponse<Void> trace() {
			return invoke(HttpMethod.TRACE, ResponseType.of(Void.class));
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> traceForResponse(Class<T> responseType) {
			return invoke(HttpMethod.TRACE, ResponseType.of(responseType));
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and receive a response back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> HttpResponse<T> traceForResponse(ResponseType<T> responseType) {
			return invoke(HttpMethod.TRACE, responseType);
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and receive the response payload back.
		 * @param <T> Response type
		 * @param responseType Expected response payload type
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> trace(Class<T> responseType) {
			return getResponseContent(invoke(HttpMethod.TRACE, ResponseType.of(responseType)));
		}

		/**
		 * Invoke the request using <code>TRACE</code> method and receive the response payload of given generic type
		 * back.
		 * @param <T> Response type
		 * @param responseType Response payload generic type representation
		 * @return Response payload object of the specified type as a result of the request invocation, if present
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		default <T> Optional<T> trace(ResponseType<T> responseType) {
			return getResponseContent(invoke(HttpMethod.TRACE, responseType));
		}

		// HEAD

		/**
		 * Invoke the request using <code>HEAD</code> method and receive a response back.
		 * @return {@link HttpResponse} object as a result of the request invocation
		 * @throws RestClientException Internal invocation failure (for example, an I/O error on communication channel
		 *         or expected and actual payload type mismatch)
		 */
		default HttpResponse<Void> head() {
			return invoke(HttpMethod.HEAD, ResponseType.of(Void.class));
		}

		// utils

		/**
		 * Try to obtain the given <code>response</code> payload, only if the response status code is a <em>success</em>
		 * status code (i.e. a <code>2xx</code> status code).
		 * @param <T> Response type
		 * @param response Response from which to obtain the context
		 * @return Response content
		 * @throws UnsuccessfulInvocationException In case the status code of the response returned by the server is not
		 *         a successful type status code, i.e. it is not a <code>2xx</code> status type
		 */
		static <T> Optional<T> getResponseContent(HttpResponse<T> response) {
			if (!HttpStatus.isSuccessStatusCode(response.getStatusCode())) {
				throw new UnsuccessfulInvocationException(response.getStatusCode());
			}
			try {
				return response.getPayload();
			} catch (Exception e) {
				throw new RestClientException(e);
			}
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
		 * @throws RestClientException If base URI is null
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
		 * @return {@link HttpResponse} object as a result of the request invocation
		 */
		<T, R> HttpResponse<T> invoke(RequestDefinition requestDefinition, HttpMethod method,
				RequestEntity<R> requestEntity, ResponseType<T> responseType);

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

	// Exceptions

	/**
	 * Exception thrown by {@link RestClient} when a request invocation fails.
	 */
	public class RestClientException extends RuntimeException {

		private static final long serialVersionUID = -4960829355269535887L;

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public RestClientException(String message) {
			super(message);
		}

		/**
		 * Constructor with nested exception
		 * @param cause Nested exception
		 */
		public RestClientException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public RestClientException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	/**
	 * Exception thrown by {@link RestClient} invocation methods when the response status is not a <em>successful</em>
	 * status code, i.e. a <code>2xx</code> HTTP status code.
	 * <p>
	 * The response status code is available through {@link #getStatusCode()} or {@link #getStatus()} methods.
	 * </p>
	 */
	public class UnsuccessfulInvocationException extends RuntimeException {

		private static final long serialVersionUID = 8453089509569188885L;

		/**
		 * Response status code
		 */
		private final int statusCode;

		/**
		 * Constructor with default error message
		 * @param statusCode HTTP status code
		 */
		public UnsuccessfulInvocationException(int statusCode) {
			this(statusCode,
					Optional.ofNullable(HttpStatus.of(statusCode)).map(s -> statusCode + " - " + s.getDescription())
							.orElse("Obtained a response with an unsuccessful status code: " + statusCode));
		}

		/**
		 * Constructor with error message
		 * @param statusCode HTTP status code
		 * @param message Error message
		 */
		public UnsuccessfulInvocationException(int statusCode, String message) {
			super(message);
			this.statusCode = statusCode;
		}

		/**
		 * Constructor with nested exception
		 * @param statusCode HTTP status code
		 * @param cause Nested exception
		 */
		public UnsuccessfulInvocationException(int statusCode, Throwable cause) {
			super(cause);
			this.statusCode = statusCode;
		}

		/**
		 * Constructor with error message and nested exception
		 * @param statusCode HTTP status code
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public UnsuccessfulInvocationException(int statusCode, String message, Throwable cause) {
			super(message, cause);
			this.statusCode = statusCode;
		}

		/**
		 * Get the HTTP response status code associated with this exception
		 * @return HTTP response status code
		 */
		public int getStatusCode() {
			return statusCode;
		}

		/**
		 * Get HTTP response status associated with this exception as {@link HttpStatus}, if available and known
		 * @return Optional HTTP response status
		 */
		public Optional<HttpStatus> getStatus() {
			return Optional.ofNullable(HttpStatus.of(getStatusCode()));
		}

	}

}
