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

import com.holonplatform.auth.AuthenticationToken;

/**
 * Exception for authentication failure: {@link AuthenticationToken} is not valid or malformed
 * 
 * @since 5.0.0
 */
public class InvalidTokenException extends AuthenticationException {

	private static final long serialVersionUID = -1860383530520088658L;

	/**
	 * Constructor
	 * @param description Error description
	 */
	public InvalidTokenException(String description) {
		super(INVALID_TOKEN, description, 401);
	}

	/**
	 * Constructor
	 * @param description Error description
	 * @param scheme Authentication scheme
	 */
	public InvalidTokenException(String description, String scheme) {
		super(INVALID_TOKEN, description, 401);
		setScheme(scheme);
	}

}
