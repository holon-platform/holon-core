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

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.DataTarget.DataTargetSupport;
import com.holonplatform.core.query.QueryAggregation.QueryAggregationSupport;
import com.holonplatform.core.query.QueryFilter.QueryFilterSupport;
import com.holonplatform.core.query.QuerySort.QuerySortSupport;

/**
 * Builder to configure a {@link Query}, managing query {@link DataTarget} and query clauses such as {@link QueryFilter}
 * and {@link QuerySort} and handling {@link ExpressionResolver}s registration.
 * 
 * @since 5.0.0
 * 
 * @param <Q> Concrete QueryBuilder
 *
 * @see Query
 */
public interface QueryBuilder<Q extends QueryBuilder<Q>> extends QueryFilterSupport<Q>, QuerySortSupport<Q>,
		DataTargetSupport<Q>, QueryAggregationSupport<Q>, ExpressionResolverBuilder<Q> {

	/**
	 * Get the current {@link QueryConfiguration}.
	 * @return the current {@link QueryConfiguration}
	 */
	QueryConfiguration getQueryConfiguration();

	/**
	 * Limit the fetched result set
	 * @param limit Results limit. Must be &gt; 0. A value &lt;= 0 indicates no limit.
	 * @return this
	 */
	Q limit(int limit);

	/**
	 * Starts the query results at a particular zero-based offset.
	 * @param offset Results offset 0-based index. Must be &gt;= 0.
	 * @return this
	 */
	Q offset(int offset);

	/**
	 * Convenience method to set {@link #limit(int)} and {@link #offset(int)} of query results both in one call
	 * @param limit limit Results limit. Must be &gt; 0. A value &lt;= 0 indicates no limit.
	 * @param offset offset Results offset 0-based index. Must be &gt;= 0.
	 * @return this
	 */
	Q restrict(int limit, int offset);

	/**
	 * Add a generic parameter to query
	 * @param name Parameter name
	 * @param value Parameter value
	 * @return this
	 */
	Q parameter(String name, Object value);

}
