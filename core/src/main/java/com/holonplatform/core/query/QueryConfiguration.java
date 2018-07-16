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

import java.util.Optional;

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverProvider;
import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.datastore.DataTarget;

/**
 * Represents the configuration of a <em>query</em>, providing configuration elements and supporting
 * {@link ExpressionResolver}s.
 * 
 * @since 5.0.0
 */
public interface QueryConfiguration extends ParameterSet, Expression, ExpressionResolverProvider {

	/**
	 * Get the data target.
	 * @return Optional {@link DataTarget} of the query
	 */
	Optional<DataTarget<?>> getTarget();

	/**
	 * Get result set limit.
	 * @return Results limit, an empty Optional indicates no limit.
	 */
	Optional<Integer> getLimit();

	/**
	 * Get 0-based results offset.
	 * @return Results offset 0-based index. an empty Optional indicates no offset.
	 */
	Optional<Integer> getOffset();

	/**
	 * Get the query filter.
	 * @return Optional query filter
	 */
	Optional<QueryFilter> getFilter();

	/**
	 * Get the query sort.
	 * @return Optional query sort
	 */
	Optional<QuerySort> getSort();

	/**
	 * Get the query results aggregation clause.
	 * @return Optional query results aggregation clause
	 */
	Optional<QueryAggregation> getAggregation();

	/**
	 * Get whether the query should return <em>distinct</em> query projection result values.
	 * @return <code>true</code> if the query should return <em>distinct</em> query projection result values,
	 *         <code>false</code> otherwise
	 * @since 5.2.0
	 */
	boolean isDistinct();

}
