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

import com.holonplatform.http.rest.ResponseEntity;

/**
 * HTTP exception related to a client {@link ResponseEntity} with a not successful status code.
 *
 * @since 5.0.0
 */
public class UnsuccessfulResponseException extends HttpStatusCodeException {

	private static final long serialVersionUID = -4980257011839440253L;

	/**
	 * Response reference
	 */
	private final ResponseEntity<?> response;

	/**
	 * Constructor
	 * @param response Response entity associated to this exception
	 */
	public UnsuccessfulResponseException(ResponseEntity<?> response) {
		super(response.getStatusCode());
		this.response = response;
	}

	/**
	 * Constructor with error message
	 * @param response Response entity associated to this exception
	 * @param message Error message
	 */
	public UnsuccessfulResponseException(ResponseEntity<?> response, String message) {
		super(response.getStatusCode(), message);
		this.response = response;
	}

	/**
	 * Get the response entity associated with to exception.
	 * @return the response entity associated to this exception
	 */
	public ResponseEntity<?> getResponse() {
		return response;
	}

}
