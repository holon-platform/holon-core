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
 * Exception for authentication failure: Account is locked
 * 
 * @since 5.0.0
 */
public class LockedAccountException extends AuthenticationException {

	private static final long serialVersionUID = 4987106837682102635L;

	/**
	 * Constructor
	 */
	public LockedAccountException() {
		this("Account is locked");
	}

	/**
	 * Constructor with error description
	 * @param description Error description
	 */
	public LockedAccountException(String description) {
		super(INVALID_CLIENT, description, 401);
	}

}
