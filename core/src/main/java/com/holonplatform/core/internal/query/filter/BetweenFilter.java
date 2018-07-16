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
package com.holonplatform.core.internal.query.filter;

import com.holonplatform.core.CollectionConstantExpression;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.query.QueryFilterVisitor;
import com.holonplatform.core.query.QueryFilter;

/**
 * Filter which represents "IS BETWEEN a minimum and a maximum value" predicate.
 * 
 * @param <T> Expression type
 * 
 * @since 4.4.0
 * 
 * @see QueryFilter
 */
public class BetweenFilter<T> extends AbstractOperationQueryFilter<T> {

	private static final long serialVersionUID = -8040652878349000617L;

	/**
	 * Constructor.
	 * @param expression Filter expression (not null)
	 * @param from Minimum value
	 * @param to Maximum value
	 */
	public BetweenFilter(TypedExpression<T> expression, T from, T to) {
		super(expression, FilterOperator.BETWEEN, CollectionConstantExpression.create(expression, from, to));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.filter.AbstractOperationQueryFilter#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		super.validate();
		T from = getFromValue();
		if (from == null) {
			throw new InvalidExpressionException("Between filter FROM value is null");
		}
		T to = getToValue();
		if (to == null) {
			throw new InvalidExpressionException("Between filter TO value is null");
		}
	}

	/**
	 * Convenience method to get first (from) value
	 * @return From value, or <code>null</code> if not setted
	 */
	@SuppressWarnings("unchecked")
	public T getFromValue() {
		return getRightOperand().map(e -> ((Iterable<T>) e).iterator()).filter(i -> i.hasNext()).map(i -> i.next())
				.orElse(null);
	}

	/**
	 * Convenience method to get second (to) value
	 * @return To value, or <code>null</code> if not setted
	 */
	@SuppressWarnings("unchecked")
	public T getToValue() {
		return getRightOperand().map(e -> ((Iterable<T>) e).iterator()).filter(i -> i.hasNext()).map(i -> {
			i.next();
			if (i.hasNext()) {
				return i.next();
			}
			return null;
		}).orElse(null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.VisitableQueryData#accept(com.holonplatform.core.query.QueryDataVisitor,
	 * java.lang.Object)
	 */
	@Override
	public <R, C> R accept(QueryFilterVisitor<R, C> visitor, C context) {
		return visitor.visit(this, context);
	}

}
