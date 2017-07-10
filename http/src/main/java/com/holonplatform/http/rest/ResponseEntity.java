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
package com.holonplatform.http.rest;

import java.util.Optional;

import com.holonplatform.http.HttpResponse;
import com.holonplatform.http.exceptions.HttpEntityProcessingException;

/**
 * {@link HttpResponse} extension that adds HTTP <em>entity</em> handling, dealing with HTTP response body processing
 * and conversion into a supported Java type, besides the one expected as declared response type.
 * 
 * @param <T> Response entity expected type
 * 
 * @since 5.0.0
 */
public interface ResponseEntity<T> extends HttpResponse<T> {

	/**
	 * Read the message entity as an instance of specified type.
	 * @param <E> Entity instance type
	 * @param entityType Entity type (not null)
	 * @return the message entity; for a zero-length response entities an empty Optional is returned
	 * @throws HttpEntityProcessingException If the content of the message cannot be mapped to an entity of the
	 *         requested type
	 */
	<E> Optional<E> as(Class<E> entityType);

	/**
	 * Read the message entity as an instance of specified type, using a {@link ResponseType} representation to allow
	 * generic types support.
	 * @param <E> Entity instance type
	 * @param entityType Entity response type (not null)
	 * @return the message entity; for a zero-length response entities an empty Optional is returned
	 * @throws HttpEntityProcessingException If the content of the message cannot be mapped to an entity of the
	 *         requested type
	 */
	<E> Optional<E> as(ResponseType<E> entityType);

	/**
	 * If supported by the underlying implementation, close the message entity input stream (if available and open) as
	 * well as releases any other resources associated with the response.
	 * @throws HttpEntityProcessingException If there is an error closing the response
	 */
	void close();

}
