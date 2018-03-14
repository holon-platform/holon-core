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
package com.holonplatform.core.internal.datastore.beans;

import java.util.stream.Stream;

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.datastore.beans.BeanQuery;
import com.holonplatform.core.query.BeanProjection;
import com.holonplatform.core.query.Query;
import com.holonplatform.core.query.QueryAggregation;
import com.holonplatform.core.query.QueryConfiguration;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QuerySort;

/**
 * Default {@link BeanQuery} implementation.
 *
 * @param <T> Bean type
 *
 * @since 5.1.0
 */
public class DefaultBeanQuery<T> extends AbstractBeanDatastoreAdapter<Query> implements BeanQuery<T> {

	private final Class<T> beanClass;

	public DefaultBeanQuery(Class<T> beanClass, Query executor) {
		super(executor);
		this.beanClass = beanClass;
		executor.target(getDataTarget(beanClass));
	}

	/**
	 * Get the bean class.
	 * @return the bean class
	 */
	protected Class<T> getBeanClass() {
		return beanClass;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryBuilder#getQueryConfiguration()
	 */
	@Override
	public QueryConfiguration getQueryConfiguration() {
		return getExecutor().getQueryConfiguration();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryBuilder#limit(int)
	 */
	@Override
	public BeanQuery<T> limit(int limit) {
		getExecutor().limit(limit);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryBuilder#offset(int)
	 */
	@Override
	public BeanQuery<T> offset(int offset) {
		getExecutor().offset(offset);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryBuilder#restrict(int, int)
	 */
	@Override
	public BeanQuery<T> restrict(int limit, int offset) {
		getExecutor().restrict(limit, offset);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryBuilder#parameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public BeanQuery<T> parameter(String name, Object value) {
		getExecutor().parameter(name, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryBuilder#parameter(com.holonplatform.core.config.ConfigProperty,
	 * java.lang.Object)
	 */
	@Override
	public <P> BeanQuery<T> parameter(ConfigProperty<P> property, P value) {
		getExecutor().parameter(property, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilter.QueryFilterSupport#filter(com.holonplatform.core.query.QueryFilter)
	 */
	@Override
	public BeanQuery<T> filter(QueryFilter filter) {
		getExecutor().filter(filter);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QuerySort.QuerySortSupport#sort(com.holonplatform.core.query.QuerySort)
	 */
	@Override
	public BeanQuery<T> sort(QuerySort sort) {
		getExecutor().sort(sort);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.query.QueryAggregation.QueryAggregationSupport#aggregate(com.holonplatform.core.query.
	 * QueryAggregation)
	 */
	@Override
	public BeanQuery<T> aggregate(QueryAggregation aggregation) {
		getExecutor().aggregate(aggregation);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder#withExpressionResolver(com.holonplatform.core
	 * .ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> BeanQuery<T> withExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		getExecutor().withExpressionResolver(expressionResolver);
		return this;
	}

	// ------- Results

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanQuery#stream()
	 */
	@Override
	public Stream<T> stream() {
		return getExecutor().stream(BeanProjection.of(getBeanClass()));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanQuery#count()
	 */
	@Override
	public long count() {
		return getExecutor().count();
	}

}
