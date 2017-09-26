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

import com.holonplatform.core.internal.query.QueryFilterVisitor;
import com.holonplatform.core.query.QueryExpression;
import com.holonplatform.core.query.QueryFilter;

/**
 * Filter which represents a "IS LESS (OR EQUAL)" predicate.
 * 
 * @param <T> Expression type
 * 
 * @since 4.4.0
 * 
 * @see QueryFilter
 */
public class LessFilter<T> extends AbstractOperationQueryFilter<T> {

	private static final long serialVersionUID = 5585742436373530129L;

	/**
	 * Constructor.
	 * @param left Left operand
	 * @param right Right operand
	 * @param includeEquals if <code>true</code>, build a LESS OR EQUAL filter condition
	 */
	public LessFilter(QueryExpression<T> left, QueryExpression<? super T> right, boolean includeEquals) {
		super(left, includeEquals ? FilterOperator.LESS_OR_EQUAL : FilterOperator.LESS_THAN, right);
	}

	/**
	 * Check if equal condition must be included
	 * @return <code>true</code> if includes equal condition
	 */
	public boolean isIncludeEquals() {
		return getOperator() == FilterOperator.LESS_OR_EQUAL;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.filter.AbstractOperationQueryFilter#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		super.validate();
		if (!getRightOperand().isPresent()) {
			throw new InvalidExpressionException("Missing right hand operand");
		}
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
