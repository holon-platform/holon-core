/*
 * Copyright 2016-2018 Axioma srl.
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
package com.holonplatform.core.query.lock;

import com.holonplatform.core.exceptions.DataAccessException;

/**
 * Exception used by Datastore operations indicating a problem acquiring a lock.
 *
 * @since 5.2.0
 */
public class LockAcquisitionException extends DataAccessException {

	private static final long serialVersionUID = -3658625923879951053L;

	/**
	 * Constructor with error message
	 * @param message Error message
	 */
	public LockAcquisitionException(String message) {
		super(message);
	}

	/**
	 * Constructor with error message and nested exception
	 * @param message Error message
	 * @param cause Nested exception
	 */
	public LockAcquisitionException(String message, Throwable cause) {
		super(message, cause);
	}

}
