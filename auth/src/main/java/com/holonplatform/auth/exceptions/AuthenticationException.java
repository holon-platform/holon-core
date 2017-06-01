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
package com.holonplatform.auth.exceptions;

import com.holonplatform.auth.AuthenticationError;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.http.ErrorResponse.ErrorResponseException;

/**
 * Base class for exceptions which may be thrown during authentication
 * 
 * @since 5.0.0
 * 
 * @see Authenticator
 */
public abstract class AuthenticationException extends ErrorResponseException implements AuthenticationError {

	private static final long serialVersionUID = 2334548918524878853L;

	/*
	 * Authentication scheme (optional)
	 */
	private String scheme;

	/**
	 * Constructor
	 * @param errorCode Error code
	 * @param errorDescription Error description, i.e. a human-readable explanation of this error
	 */
	public AuthenticationException(String errorCode, String errorDescription) {
		super(errorCode, errorDescription);
	}

	/**
	 * Constructor
	 * @param errorCode Error code
	 * @param errorDescription Error description, i.e. a human-readable explanation of this error
	 * @param httpStatus HTTP status code to represent error as a HTTP response
	 */
	public AuthenticationException(String errorCode, String errorDescription, int httpStatus) {
		super(errorCode, errorDescription, httpStatus);
	}

	/**
	 * Constructor
	 * @param errorCode Error code
	 * @param errorDescription Error description, i.e. a human-readable explanation of this error
	 * @param errorURI Error URI that leads to further details about this error
	 * @param httpStatus HTTP status code to represent error as a HTTP response
	 */
	public AuthenticationException(String errorCode, String errorDescription, String errorURI, int httpStatus) {
		super(errorCode, errorDescription, errorURI, httpStatus);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthenticationError#getScheme()
	 */
	@Override
	public String getScheme() {
		return scheme;
	}

	/**
	 * Set authentication scheme
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

}
