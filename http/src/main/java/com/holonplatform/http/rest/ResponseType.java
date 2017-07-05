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

import java.lang.reflect.Type;

import com.holonplatform.http.RestClient;
import com.holonplatform.http.internal.ResponseTypeImpl;

/**
 * Represents a response type to be obtained from a HTTP client request-response invocation.
 * <p>
 * Supports parameterized types, which can be declared using {@link #of(Class, Class)}.
 * </p>
 *
 * @param <T> Actual response type
 *
 * @see RestClient
 */
public interface ResponseType<T> {

	/**
	 * Get the actual response type
	 * @return Response type
	 */
	Type getType();

	/**
	 * Get whether is a simple or parameterized type
	 * @return <code>true</code> if simple type, <code>false</code> if parameterized type
	 */
	boolean isSimpleType();

	/**
	 * Build a ResponseType using given simple type.
	 * @param <T> Response type
	 * @param type Actual response type
	 * @return ResponseType
	 */
	static <T> ResponseType<T> of(Class<T> type) {
		return new ResponseTypeImpl<>(type);
	}

	/**
	 * Build a ResponseType using given a parameterized type.
	 * @param <T> Response type
	 * @param type Type argument
	 * @param rawType Raw type
	 * @return ResponseType
	 */
	static <T> ResponseType<T> of(Class<?> type, Class<?> rawType) {
		return new ResponseTypeImpl<>(type, rawType);
	}

}
