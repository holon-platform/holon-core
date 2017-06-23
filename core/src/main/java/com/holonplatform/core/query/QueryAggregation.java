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
package com.holonplatform.core.query;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.holonplatform.core.Expression;
import com.holonplatform.core.Path;
import com.holonplatform.core.internal.query.DefaultQueryAggregation;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Represents a query results aggregation clause.
 * 
 * @since 5.0.0
 */
public interface QueryAggregation extends Expression, Serializable {

	/**
	 * Get the paths for wich to aggregate the query results.
	 * @return Aggregation paths
	 */
	Path<?>[] getAggregationPaths();

	/**
	 * Get the optional restrictions to add to query aggregation, expressed as a {@link QueryFilter} clause.
	 * @return Optional additional query restriction
	 */
	Optional<QueryFilter> getAggregationFilter();

	// Builders

	/**
	 * Create a {@link QueryAggregation} using given aggregation paths
	 * @param paths Aggregation paths
	 * @return A new {@link QueryAggregation} on given paths
	 */
	static QueryAggregation create(Path<?>... paths) {
		return new DefaultQueryAggregation(paths);
	}

	/**
	 * Create a {@link QueryAggregation} using given aggregation paths
	 * @param paths Aggregation paths
	 * @return A new {@link QueryAggregation} on given paths
	 */
	@SuppressWarnings("rawtypes")
	static QueryAggregation create(List<Path> paths) {
		ObjectUtils.argumentNotNull(paths, "Aggregation paths must be not null");
		return new DefaultQueryAggregation(paths.toArray(new Path<?>[paths.size()]));
	}

	/**
	 * Get a builder to create a {@link QueryAggregation}.
	 * @return {@link QueryAggregation} builder
	 */
	static Builder builder() {
		return new DefaultQueryAggregation.DefaultBuilder();
	}

	/**
	 * {@link QueryAggregation} builder.
	 */
	public interface Builder {

		/**
		 * Add a query aggregation path.
		 * @param path Path to add (not null)
		 * @return this
		 */
		Builder path(Path<?> path);

		/**
		 * Set the query aggregation filter.
		 * @param filter Filter to set
		 * @return this
		 */
		Builder filter(QueryFilter filter);

		/**
		 * Build the {@link QueryAggregation} instance.
		 * @return QueryAggregation instance
		 */
		QueryAggregation build();

	}

	/**
	 * Interface implemented by classes which support {@link QueryAggregation}s setting.
	 * @param <C> Concrete type
	 */
	public interface QueryAggregationSupport<C extends QueryAggregationSupport<C>> {

		/**
		 * Set the aggregation clause
		 * @param aggregation Aggregation clause to set
		 * @return this
		 */
		C aggregate(QueryAggregation aggregation);

		/**
		 * Aggregate results on given paths.
		 * @param paths Path for which to aggregate
		 * @return this
		 */
		default C aggregate(Path<?>... paths) {
			return aggregate(QueryAggregation.create(paths));
		}

		/**
		 * Aggregate results on given paths.
		 * @param paths Path for which to aggregate
		 * @return this
		 */
		default C aggregate(@SuppressWarnings("rawtypes") List<Path> paths) {
			return aggregate(QueryAggregation.create(paths));
		}

	}

}
