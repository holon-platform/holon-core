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

import java.util.Optional;

import com.holonplatform.core.Expression;
import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ResolutionContext;
import com.holonplatform.core.ExpressionResolverRegistry;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.internal.DefaultParameterSet;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryAggregation;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QuerySort;

/**
 * Default {@link QueryDefinition} implementation.
 * 
 * @since 5.0.0
 */
public class DefaultQueryDefinition extends DefaultParameterSet implements QueryDefinition {

	private static final long serialVersionUID = 6484896543047734331L;

	/*
	 * Query target
	 */
	protected DataTarget<?> target;

	/*
	 * Results limit
	 */
	protected Integer limit;
	/*
	 * Results offset
	 */
	protected Integer offset;

	/*
	 * Query sort
	 */
	protected QuerySort sort;
	/*
	 * Query filter
	 */
	protected QueryFilter filter;

	/*
	 * Query aggregation
	 */
	protected QueryAggregation aggregation;

	/*
	 * Expression resolvers
	 */
	protected ExpressionResolverRegistry expressionResolverRegistry = ExpressionResolverRegistry.create();

	/**
	 * Construct a new DefaultQueryDefinition.
	 */
	public DefaultQueryDefinition() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.QueryDefinition#setTarget(com.holonplatform.core.datastore.DataTarget)
	 */
	@Override
	public <T> void setTarget(DataTarget<T> target) {
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.QueryDefinition#setLimit(java.lang.Integer)
	 */
	@Override
	public void setLimit(Integer limit) {
		this.limit = (limit != null && limit.intValue() > 0) ? limit : null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.QueryDefinition#setOffset(java.lang.Integer)
	 */
	@Override
	public void setOffset(Integer offset) {
		this.offset = (offset != null && offset.intValue() >= 0) ? offset : null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfiguration#getTarget()
	 */
	@Override
	public Optional<DataTarget<?>> getTarget() {
		return Optional.ofNullable(target);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Integer> getLimit() {
		return Optional.ofNullable(limit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Integer> getOffset() {
		return Optional.ofNullable(offset);
	}

	/**
	 * Add a sort to query. If any sort was present, sort will be appended in specified order
	 * @param sort Sort to add
	 */
	@Override
	public void addSort(QuerySort sort) {
		ObjectUtils.argumentNotNull(sort, "QuerySort must be not null");
		if (this.sort == null) {
			this.sort = sort;
		} else {
			this.sort = this.sort.and(sort);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<QuerySort> getSort() {
		return Optional.ofNullable(sort);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFilter(QueryFilter filter) {
		ObjectUtils.argumentNotNull(filter, "QueryFilter must be not null");
		if (this.filter == null) {
			this.filter = filter;
		} else {
			this.filter = this.filter.and(filter);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<QueryFilter> getFilter() {
		return Optional.ofNullable(filter);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfiguration#getAggregation()
	 */
	@Override
	public Optional<QueryAggregation> getAggregation() {
		return Optional.ofNullable(aggregation);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.QueryDefinition#setAggregation(com.holonplatform.core.query.
	 * QueryAggregation)
	 */
	@Override
	public void setAggregation(QueryAggregation aggregation) {
		this.aggregation = aggregation;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport#addExpressionResolver(com.holonplatform.core.
	 * ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> void addExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		expressionResolverRegistry.addExpressionResolver(expressionResolver);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport#removeExpressionResolver(com.holonplatform.
	 * core.ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> void removeExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		expressionResolverRegistry.removeExpressionResolver(expressionResolver);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport#getExpressionResolvers()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Iterable<ExpressionResolver> getExpressionResolvers() {
		return expressionResolverRegistry.getExpressionResolvers();
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
		return expressionResolverRegistry.resolve(expression, resolutionType, context);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultQueryDefinition [target=" + target + ", limit=" + limit + ", offset=" + offset + ", sort=" + sort
				+ ", filter=" + filter + ", aggregation=" + aggregation + "]";
	}

}
