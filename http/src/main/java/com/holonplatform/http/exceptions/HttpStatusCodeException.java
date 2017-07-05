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
package com.holonplatform.http.exceptions;

import java.util.Optional;

import com.holonplatform.http.HttpStatus;

/**
 * {@link InvalidHttpMessageException} with HTTP status code.
 *
 * @since 5.0.0
 */
public class HttpStatusCodeException extends HttpException {

	private static final long serialVersionUID = 472896542459281565L;

	/**
	 * HTTP status code
	 */
	private final int statusCode;

	/**
	 * Constructor with status code
	 * @param statusCode HTTP status code
	 */
	public HttpStatusCodeException(int statusCode) {
		super(HttpStatus.of(statusCode).map(s -> s.getCode() + " - " + s.getDescription())
				.orElse("HTTP status code: " + statusCode));
		this.statusCode = statusCode;
	}

	/**
	 * Constructor with error message
	 * @param statusCode HTTP status code
	 * @param message Error message
	 */
	public HttpStatusCodeException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	/**
	 * Constructor with nested exception
	 * @param statusCode HTTP status code
	 * @param cause Nested exception
	 */
	public HttpStatusCodeException(int statusCode, Throwable cause) {
		super(cause);
		this.statusCode = statusCode;
	}

	/**
	 * Constructor with error message and nested exception
	 * @param statusCode HTTP status code
	 * @param message Error message
	 * @param cause Nested exception
	 */
	public HttpStatusCodeException(int statusCode, String message, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	/**
	 * Get the HTTP status code.
	 * @return the HTTP status code
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Get HTTP response status associated with this exception as {@link HttpStatus}, if available and known.
	 * @return Optional HTTP response status
	 */
	public Optional<HttpStatus> getStatus() {
		return HttpStatus.of(getStatusCode());
	}

}
