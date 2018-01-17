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
package com.holonplatform.core.internal.query;

import java.util.stream.Stream;

import com.holonplatform.core.query.Query;
import com.holonplatform.core.query.QueryConfiguration;
import com.holonplatform.core.query.QueryExecution;
import com.holonplatform.core.query.QueryProjection;
import com.holonplatform.core.query.QueryResults.QueryExecutionException;

/**
 * Adapter to connect concrete {@link Query} implementations.
 * 
 * @since 5.0.0
 * 
 * @param <C> Query configuration type
 * 
 * @see QueryAdapterQuery
 */
public interface QueryAdapter<C extends QueryConfiguration> {

	/**
	 * Execute query using provided <code>queryDefinition</code> and return a results {@link Stream} typed on
	 * <code>projection</code> type.
	 * @param <R> Results type
	 * @param queryConfiguration Query configuration
	 * @param projection Query results projection
	 * @return Query results stream
	 * @throws QueryExecutionException Error during query execution
	 * @deprecated Use {@link #stream(QueryExecution)}
	 */
	@Deprecated
	default <R> Stream<R> stream(C queryConfiguration, QueryProjection<R> projection) throws QueryExecutionException {
		return stream(QueryExecution.create(queryConfiguration, projection));
	}

	/**
	 * Execute query using provided <code>queryDefinition</code> and return a results {@link Stream} typed on
	 * <code>projection</code> type.
	 * @param <R> Results type
	 * @param query Query execution
	 * @return Query results stream
	 * @throws QueryExecutionException If a query execution error occurred
	 */
	<R> Stream<R> stream(QueryExecution<C, R> query) throws QueryExecutionException;

}
