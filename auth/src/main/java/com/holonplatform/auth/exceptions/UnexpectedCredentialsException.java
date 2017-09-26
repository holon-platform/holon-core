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
package com.holonplatform.auth.exceptions;

import com.holonplatform.auth.CredentialsContainer.CredentialsMatcher;

/**
 * Exception thrown when an unexpected internal error occurs during credentials match from {@link CredentialsMatcher}
 * 
 * @since 5.0.0
 */
public class UnexpectedCredentialsException extends RuntimeException {

	private static final long serialVersionUID = 7344839441440107489L;

	/**
	 * Constructor with error message
	 * @param message Error message
	 */
	public UnexpectedCredentialsException(String message) {
		super(message);
	}

	/**
	 * Constructor with nested exception
	 * @param cause Nested exception
	 */
	public UnexpectedCredentialsException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor with error message and nested exception
	 * @param message Error message
	 * @param cause Nested exception
	 */
	public UnexpectedCredentialsException(String message, Throwable cause) {
		super(message, cause);
	}

}
