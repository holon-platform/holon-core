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

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import com.holonplatform.core.Expression;
import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ResolutionContext;
import com.holonplatform.core.datastore.DataTarget;
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
		implements QueryConfiguration, QueryBuilder<Q> {

	private static final long serialVersionUID = 200624247804275286L;

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
	@SuppressWarnings("unchecked")
	@Override
	public Q target(DataTarget<?> target) {
		getQueryDefinition().setTarget(target);
		return (Q) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterClause#filter(com.holonplatform.core.query.QueryFilter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Q filter(QueryFilter filter) {
		if (filter != null) {
			getQueryDefinition().addFilter(filter);
		}
		return (Q) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QuerySort.QuerySortSupport#sort(com.holonplatform.core.query.QuerySort)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Q sort(QuerySort sort) {
		if (sort != null) {
			getQueryDefinition().addSort(sort);
		}
		return (Q) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.Query#limit(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Q limit(int limit) {
		getQueryDefinition().setLimit(Integer.valueOf(limit));
		return (Q) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.Query#offset(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Q offset(int offset) {
		getQueryDefinition().setOffset(Integer.valueOf(offset));
		return (Q) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.Query#restrict(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Q restrict(int limit, int offset) {
		getQueryDefinition().setLimit(Integer.valueOf(limit));
		getQueryDefinition().setOffset(Integer.valueOf(offset));
		return (Q) this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.query.QueryAggregation.QueryAggregationSupport#aggregate(com.holonplatform.core.query.
	 * QueryAggregation)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Q aggregate(QueryAggregation aggregation) {
		getQueryDefinition().setAggregation(aggregation);
		return (Q) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.Query#parameter(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Q parameter(String name, Object value) {
		getQueryDefinition().addParameter(name, value);
		return (Q) this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder#withExpressionResolver(com.holonplatform.core
	 * .ExpressionResolver)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E extends Expression, R extends Expression> Q withExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		getQueryDefinition().addExpressionResolver(expressionResolver);
		return (Q) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#hasParameters()
	 */
	@Override
	public boolean hasParameters() {
		return getQueryDefinition().hasParameters();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#hasParameter(java.lang.String)
	 */
	@Override
	public boolean hasParameter(String name) {
		return getQueryDefinition().hasParameter(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#hasNotNullParameter(java.lang.String)
	 */
	@Override
	public boolean hasNotNullParameter(String name) {
		return getQueryDefinition().hasNotNullParameter(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#getParameter(java.lang.String)
	 */
	@Override
	public Optional<Object> getParameter(String name) {
		return getQueryDefinition().getParameter(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#getParameter(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> Optional<T> getParameter(String name, Class<T> type) throws ClassCastException {
		return getQueryDefinition().getParameter(name, type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#getParameterIf(java.lang.String, java.lang.Class,
	 * java.util.function.Predicate)
	 */
	@Override
	public <T> Optional<T> getParameterIf(String name, Class<T> type, Predicate<T> condition) {
		return getQueryDefinition().getParameterIf(name, type, condition);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ParameterSet#forEach(java.util.function.BiConsumer)
	 */
	@Override
	public void forEachParameter(BiConsumer<String, Object> action) {
		getQueryDefinition().forEachParameter(action);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfiguration#getTarget()
	 */
	@Override
	public Optional<DataTarget<?>> getTarget() {
		return getQueryDefinition().getTarget();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfiguration#getLimit()
	 */
	@Override
	public Optional<Integer> getLimit() {
		return getQueryDefinition().getLimit();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfiguration#getOffset()
	 */
	@Override
	public Optional<Integer> getOffset() {
		return getQueryDefinition().getOffset();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfiguration#getFilter()
	 */
	@Override
	public Optional<QueryFilter> getFilter() {
		return getQueryDefinition().getFilter();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfiguration#getSort()
	 */
	@Override
	public Optional<QuerySort> getSort() {
		return getQueryDefinition().getSort();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfiguration#getAggregation()
	 */
	@Override
	public Optional<QueryAggregation> getAggregation() {
		return getQueryDefinition().getAggregation();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverHandler#resolve(com.holonplatform.core.Expression,
	 * java.lang.Class, com.holonplatform.core.ExpressionResolver.ResolutionContext)
	 */
	@Override
	public <E extends Expression, R extends Expression> Optional<R> resolve(E expression, Class<R> resolutionType,
			ResolutionContext context) throws InvalidExpressionException {
		return getQueryDefinition().resolve(expression, resolutionType, context);
	}

}
