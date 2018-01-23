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

import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.query.QueryFilterVisitor;
import com.holonplatform.core.query.QueryFilter;

/**
 * Filter which represents a "IS IN a set of values" predicate.
 * 
 * @param <T> Expression type
 * 
 * @since 4.4.0
 * 
 * @see QueryFilter
 */
public class InFilter<T> extends AbstractOperationQueryFilter<T> {

	private static final long serialVersionUID = 3781532721579394627L;

	/**
	 * Constructor.
	 * @param left Left operand
	 * @param right Right operand
	 */
	public InFilter(TypedExpression<T> left, TypedExpression<? super T> right) {
		super(left, FilterOperator.IN, right);
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
