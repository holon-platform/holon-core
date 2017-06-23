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

import com.holonplatform.http.ErrorResponse;

/**
 * Exception for unsupported authentication messages
 *
 * @since 5.0.0
 */
public class UnsupportedMessageException extends AuthenticationException {

	private static final long serialVersionUID = -9105293320413992762L;

	/**
	 * Constructor
	 * @param description Error description
	 */
	public UnsupportedMessageException(String description) {
		super(ErrorResponse.SERVER_ERROR, description, 500);
	}

}
