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
package com.holonplatform.core.datastore;

import java.io.Serializable;

/**
 * Base interface to represent a {@link DatastoreCommodity} configuration context to be used by a
 * {@link DatastoreCommodityFactory}, typically extended by concrete {@link Datastore} implementations with specific
 * informations and methods.
 * 
 * @since 5.0.0
 *
 * @see DatastoreCommodityFactory
 */
public interface DatastoreCommodityContext extends Serializable {

	/**
	 * Exception to notify a {@link DatastoreCommodity} type is not available for a given {@link Datastore}.
	 */
	@SuppressWarnings("serial")
	public class CommodityNotAvailableException extends RuntimeException {

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public CommodityNotAvailableException(String message) {
			super(message);
		}

	}

	/**
	 * Exception to notify a {@link DatastoreCommodity} configuration error.
	 */
	@SuppressWarnings("serial")
	public class CommodityConfigurationException extends RuntimeException {

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public CommodityConfigurationException(String message) {
			super(message);
		}

		/**
		 * Constructor with nested exception
		 * @param cause Nested exception
		 */
		public CommodityConfigurationException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public CommodityConfigurationException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
