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

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.holonplatform.http.ErrorResponse;

/**
 * Exception for a generic authentication failure
 * 
 * @since 5.0.0
 */
public class UnexpectedAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 708760349389852814L;

	/**
	 * Constructor
	 * @param description Error description
	 */
	public UnexpectedAuthenticationException(String description) {
		super(ErrorResponse.SERVER_ERROR, description, 500);
	}

	/**
	 * Constructor
	 * @param description Error message
	 * @param cause Nested exception
	 */
	public UnexpectedAuthenticationException(String description, Throwable cause) {
		super(ErrorResponse.SERVER_ERROR,
				ExceptionUtils.getRootCauseMessage(cause) + ((description != null) ? (": " + description) : ""), 500);
	}

}
