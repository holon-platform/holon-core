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
package com.holonplatform.core.internal.query;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryAggregation;
import com.holonplatform.core.query.QueryFilter;

/**
 * Default {@link QueryAggregation} implementation.
 *
 * @since 5.0.0
 */
public class DefaultQueryAggregation implements QueryAggregation {

	private static final long serialVersionUID = -6164222076580538769L;

	/**
	 * Aggregation paths
	 */
	private final Path<?>[] aggregationPaths;

	/**
	 * Aggregation filter
	 */
	private QueryFilter aggregationFilter;

	/**
	 * Constructor
	 * @param aggregationPaths Aggregation paths
	 */
	public DefaultQueryAggregation(Path<?>[] aggregationPaths) {
		super();
		this.aggregationPaths = aggregationPaths;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryAggregation#getAggregationPaths()
	 */
	@Override
	public Path<?>[] getAggregationPaths() {
		return aggregationPaths;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryAggregation#getAggregationFilter()
	 */
	@Override
	public Optional<QueryFilter> getAggregationFilter() {
		return Optional.ofNullable(aggregationFilter);
	}

	/**
	 * Set the additional aggregation query filter
	 * @param aggregationFilter the filter to set
	 */
	public void setAggregationFilter(QueryFilter aggregationFilter) {
		this.aggregationFilter = aggregationFilter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (aggregationPaths == null || aggregationPaths.length == 0) {
			throw new InvalidExpressionException("No aggregation path available");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultQueryAggregation [aggregationPaths=" + Arrays.toString(aggregationPaths) + ", aggregationFilter="
				+ aggregationFilter + "]";
	}

	// Builder

	/**
	 * Default {@link Builder} implementation.
	 */
	public static class DefaultBuilder implements Builder {

		private final List<Path<?>> paths = new LinkedList<>();
		private QueryFilter filter;

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.query.QueryAggregation.Builder#path(com.holonplatform.core.Path)
		 */
		@Override
		public Builder path(Path<?> path) {
			ObjectUtils.argumentNotNull(path, "Aggregation path must be not null");
			paths.add(path);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.query.QueryAggregation.Builder#filter(com.holonplatform.core.query.QueryFilter)
		 */
		@Override
		public Builder filter(QueryFilter filter) {
			this.filter = filter;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.query.QueryAggregation.Builder#build()
		 */
		@Override
		public QueryAggregation build() {
			final DefaultQueryAggregation a = new DefaultQueryAggregation(paths.toArray(new Path[paths.size()]));
			a.setAggregationFilter(filter);
			return a;
		}

	}

}
