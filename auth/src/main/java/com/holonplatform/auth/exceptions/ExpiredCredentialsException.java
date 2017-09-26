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

/**
 * Exception for authentication failure: Credentials are expired
 * 
 * @since 5.0.0
 */
public class ExpiredCredentialsException extends AuthenticationException {

	private static final long serialVersionUID = 1773853389734053150L;

	/**
	 * Constructor
	 */
	public ExpiredCredentialsException() {
		this("Credentials are expired");
	}

	/**
	 * Constructor
	 * @param description Error description
	 */
	public ExpiredCredentialsException(String description) {
		super(INVALID_CLIENT, description, 401);
	}

}
