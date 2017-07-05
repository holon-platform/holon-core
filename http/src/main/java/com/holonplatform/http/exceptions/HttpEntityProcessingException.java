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

/**
 * Exception realted to HTTP entity processing errors.
 * 
 * @since 5.0.0
 */
public class HttpEntityProcessingException extends RuntimeException {

	private static final long serialVersionUID = -249437600050134142L;

	/**
	 * Constructor with error message
	 * @param message Error message
	 */
	public HttpEntityProcessingException(String message) {
		super(message);
	}

	/**
	 * Constructor with nested exception
	 * @param cause Nested exception
	 */
	public HttpEntityProcessingException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor with error message and nested exception
	 * @param message Error message
	 * @param cause Nested exception
	 */
	public HttpEntityProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

}
