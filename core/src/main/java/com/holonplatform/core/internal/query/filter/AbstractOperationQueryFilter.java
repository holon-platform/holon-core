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

import java.util.Optional;

import com.holonplatform.core.internal.query.QueryFilterVisitor.VisitableQueryFilter;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryExpression;

/**
 * Abstract {@link OperationQueryFilter} implementation.
 *
 * @param <T> Operation type
 *
 * @since 5.0.0
 */
public abstract class AbstractOperationQueryFilter<T> implements OperationQueryFilter<T>, VisitableQueryFilter {

	private static final long serialVersionUID = 8118982057848388480L;

	/**
	 * Left hand operand
	 */
	private final QueryExpression<T> left;

	/**
	 * Operator
	 */
	private final FilterOperator operator;

	/**
	 * Expression argument
	 */
	private final QueryExpression<? super T> right;

	/**
	 * Constructor with left hand operand only.
	 * @param left Left hand operand (not null)
	 * @param operator Operator (not null)
	 */
	public AbstractOperationQueryFilter(QueryExpression<T> left, FilterOperator operator) {
		this(left, operator, null);
	}

	/**
	 * Constructor with both operands.
	 * @param left Left hand operand (not null)
	 * @param operator Operator (not null)
	 * @param right Right hand operand
	 */
	public AbstractOperationQueryFilter(QueryExpression<T> left, FilterOperator operator,
			QueryExpression<? super T> right) {
		super();
		ObjectUtils.argumentNotNull(left, "Left hand operand must be not null");
		ObjectUtils.argumentNotNull(operator, "Operator must be not null");
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilter.OperationQueryFilter#getLeftOperand()
	 */
	@Override
	public QueryExpression<T> getLeftOperand() {
		return left;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilter.OperationQueryFilter#getRightOperand()
	 */
	@Override
	public Optional<QueryExpression<? super T>> getRightOperand() {
		return Optional.ofNullable(right);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilter.OperationQueryFilter#getOperator()
	 */
	@Override
	public FilterOperator getOperator() {
		return operator;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getLeftOperand() == null) {
			throw new InvalidExpressionException("Null left hand operand");
		}
		if (getOperator() == null) {
			throw new InvalidExpressionException("Null operator");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.filter.AbstractPropertyQueryFilter#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QueryFilter");
		sb.append("[");
		sb.append(getLeftOperand());
		sb.append(" ");
		sb.append(getOperator().name());
		getRightOperand().ifPresent(o -> {
			sb.append(" ");
			sb.append(o);
		});
		sb.append("]");
		return sb.toString();
	}

}
