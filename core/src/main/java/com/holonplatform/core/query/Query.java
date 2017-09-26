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
package com.holonplatform.core.query;

import com.holonplatform.core.datastore.DatastoreCommodity;

/**
 * Interface to model a query, apply restrictions, sortings and configurations and obtain query results.
 * <p>
 * Query supports {@link QueryFilter} and {@link QuerySort} clauses and provides common configuration features such as
 * limit/offset for results paging.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see QueryBuilder
 */
public interface Query extends DatastoreCommodity, QueryBuilder<Query>, QueryResults {

	/**
	 * Exception thrown for {@link Query} build errors.
	 */
	@SuppressWarnings("serial")
	public class QueryBuildException extends RuntimeException {

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public QueryBuildException(String message) {
			super(message);
		}

		/**
		 * Constructor with nested exception
		 * @param cause Nested exception
		 */
		public QueryBuildException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public QueryBuildException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
