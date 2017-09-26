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
package com.holonplatform.core.datastore.relational;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.internal.datastore.relational.DefaultSubQuery;
import com.holonplatform.core.internal.query.ConstantExpressionProjection;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryBuilder;
import com.holonplatform.core.query.QueryExpression;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QueryProjection;
import com.holonplatform.core.query.QuerySort;

/**
 * Relational sub query {@link QueryExpression}.
 * 
 * <p>
 * This class implements {@link QueryBuilder} to build sub query using {@link QueryFilter}s, {@link QuerySort}s and
 * providing {@link ExpressionResolver} registration support.
 * </p>
 * 
 * @param <T> the type of the selection
 * 
 * @since 5.0.0
 */
public interface SubQuery<T> extends QueryBuilder<SubQuery<T>>, QueryExpression<T> {

	/**
	 * Specify the projection to use as the subquery result.
	 * @param projection Projection (not null)
	 * @return this
	 */
	SubQuery<T> select(QueryProjection<T> projection);

	/**
	 * Gets the projection to use as the subquery result
	 * @return Selection projection
	 */
	QueryProjection<T> getSelection();

	/**
	 * Build a EXISTS filter predicate using this sub query
	 * @return Exist filter
	 */
	QueryFilter exists();

	/**
	 * Build a NOT EXISTS filter predicate using this sub query
	 * @return Not exists filter
	 */
	QueryFilter notExists();

	/**
	 * Build a sub query with given selection type.
	 * @param <T> Subquery selection type
	 * @param datastore Datastore (not null)
	 * @param selectionType Sub query selection type (not null)
	 * @return Sub query instance
	 */
	@SuppressWarnings("unchecked")
	static <T> SubQuery<T> create(Datastore datastore, Class<? extends T> selectionType) {
		ObjectUtils.argumentNotNull(datastore, "Datastore must be not null");
		ObjectUtils.argumentNotNull(selectionType, "Selection type must be not null");
		// inherit resolvers
		DefaultSubQuery<T> subQuery = new DefaultSubQuery<>(selectionType);
		datastore.getExpressionResolvers().forEach(r -> subQuery.withExpressionResolver(r));
		return subQuery;
	}

	/**
	 * Build a sub query with given property as selection.
	 * @param <T> Subquery selection type
	 * @param datastore Datastore (not null)
	 * @param selection Query selection (not null)
	 * @return Sub query instance
	 */
	@SuppressWarnings("unchecked")
	static <T> SubQuery<T> create(Datastore datastore, QueryProjection<T> selection) {
		ObjectUtils.argumentNotNull(datastore, "Datastore must be not null");
		ObjectUtils.argumentNotNull(selection, "Selection property must be not null");
		return create(datastore, (Class<T>) selection.getType()).select(selection);
	}

	/**
	 * Build a sub query with default <code>select 1</code> projection as selection.
	 * @param datastore Datastore (not null)
	 * @return Sub query instance
	 */
	static SubQuery<Integer> create(Datastore datastore) {
		return create(datastore, Integer.class).select(ConstantExpressionProjection.create(Integer.valueOf(1)));
	}

}
