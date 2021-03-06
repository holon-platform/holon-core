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

import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.http.ErrorResponse;

/**
 * Exception for authentication failure: {@link AuthenticationToken} type not supported by current {@link Authenticator}
 * 
 * @since 5.0.0
 */
public class UnsupportedTokenException extends AuthenticationException {

	private static final long serialVersionUID = -8805934594926414377L;

	/**
	 * Constructor
	 * @param description Error description
	 */
	public UnsupportedTokenException(String description) {
		super(ErrorResponse.SERVER_ERROR, description, 500);
	}

}
