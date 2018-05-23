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

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryAggregation;
import com.holonplatform.core.query.QueryBuilder;
import com.holonplatform.core.query.QueryConfiguration;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QuerySort;

/**
 * Base {@link QueryBuilder} implementation providing support for {@link QueryDefinition} management.
 * 
 * @param <Q> Concrete QueryBuilder
 * @param <D> Query definition type
 *
 * @since 5.0.0
 */
public abstract class AbstractQueryBuilder<Q extends QueryBuilder<Q>, D extends QueryDefinition>
		implements QueryBuilder<Q> {

	/*
	 * Query definition (immutable)
	 */
	protected final D queryDefinition;

	/**
	 * Constructor
	 * @param queryDefinition Query definition. Must be not <code>null</code>.
	 */
	public AbstractQueryBuilder(D queryDefinition) {
		super();
		assert queryDefinition != null : "QueryDefinition must be not null";
		this.queryDefinition = queryDefinition;
	}

	/**
	 * Get the actual query builder.
	 * @return the actual query builder
	 */
	protected abstract Q getActualBuilder();

	/**
	 * Current query definition
	 * @return Query definition
	 */
	protected D getQueryDefinition() {
		return queryDefinition;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryBuilder#getQueryConfiguration()
	 */
	@Override
	public QueryConfiguration getQueryConfiguration() {
		return queryDefinition;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.datastore.DataTarget.DataTargetSupport#target(com.holonplatform.core.datastore.DataTarget)
	 */
	@Override
	public Q target(DataTarget<?> target) {
		getQueryDefinition().setTarget(target);
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterClause#filter(com.holonplatform.core.query.QueryFilter)
	 */
	@Override
	public Q filter(QueryFilter filter) {
		if (filter != null) {
			getQueryDefinition().addFilter(filter);
		}
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QuerySort.QuerySortSupport#sort(com.holonplatform.core.query.QuerySort)
	 */
	@Override
	public Q sort(QuerySort sort) {
		if (sort != null) {
			getQueryDefinition().addSort(sort);
		}
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.Query#limit(int)
	 */
	@Override
	public Q limit(int limit) {
		getQueryDefinition().setLimit(Integer.valueOf(limit));
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.Query#offset(int)
	 */
	@Override
	public Q offset(int offset) {
		getQueryDefinition().setOffset(Integer.valueOf(offset));
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.Query#restrict(int, int)
	 */
	@Override
	public Q restrict(int limit, int offset) {
		getQueryDefinition().setLimit(Integer.valueOf(limit));
		getQueryDefinition().setOffset(Integer.valueOf(offset));
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.query.QueryAggregation.QueryAggregationSupport#aggregate(com.holonplatform.core.query.
	 * QueryAggregation)
	 */
	@Override
	public Q aggregate(QueryAggregation aggregation) {
		getQueryDefinition().setAggregation(aggregation);
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.Query#parameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public Q parameter(String name, Object value) {
		getQueryDefinition().addParameter(name, value);
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryBuilder#parameter(com.holonplatform.core.config.ConfigProperty,
	 * java.lang.Object)
	 */
	@Override
	public <T> Q parameter(ConfigProperty<T> property, T value) {
		ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
		getQueryDefinition().addParameter(property.getKey(), value);
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryBuilder#distinct()
	 */
	@Override
	public Q distinct() {
		getQueryDefinition().setDistinct(true);
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder#withExpressionResolver(com.holonplatform.core
	 * .ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> Q withExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		getQueryDefinition().addExpressionResolver(expressionResolver);
		return getActualBuilder();
	}

}
