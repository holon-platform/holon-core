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

import com.holonplatform.auth.Authorizer;
import com.holonplatform.http.ErrorResponse.ErrorResponseException;

/**
 * Exception which may be thrown during authorization
 * 
 * @since 5.0.0
 * 
 * @see Authorizer
 */
public class AuthorizationException extends ErrorResponseException {

	private static final long serialVersionUID = -5122471221712808958L;

	/**
	 * Constructor
	 * @param errorCode Error code
	 * @param errorDescription Error description, i.e. a human-readable explanation of this error
	 */
	public AuthorizationException(String errorCode, String errorDescription) {
		super(errorCode, errorDescription);
	}

	/**
	 * Constructor
	 * @param errorCode Error code
	 * @param errorDescription Error description, i.e. a human-readable explanation of this error
	 * @param httpStatus HTTP status code to represent error as a HTTP response
	 */
	public AuthorizationException(String errorCode, String errorDescription, int httpStatus) {
		super(errorCode, errorDescription, httpStatus);
	}

	/**
	 * Constructor
	 * @param errorCode Error code
	 * @param errorDescription Error description, i.e. a human-readable explanation of this error
	 * @param errorURI Error URI that leads to further details about this error
	 * @param httpStatus HTTP status code to represent error as a HTTP response
	 */
	public AuthorizationException(String errorCode, String errorDescription, String errorURI, int httpStatus) {
		super(errorCode, errorDescription, errorURI, httpStatus);
	}

}
