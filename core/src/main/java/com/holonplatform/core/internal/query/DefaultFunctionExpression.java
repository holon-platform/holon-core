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

import com.holonplatform.core.internal.query.QueryProjectionVisitor.VisitableQueryProjection;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.FunctionExpression;
import com.holonplatform.core.query.QueryFunction;

/**
 * Default {@link FunctionExpression} implementation.
 * 
 * @param <T> Function result type
 *
 * @since 5.0.0
 */
public class DefaultFunctionExpression<T> implements FunctionExpression<T>, VisitableQueryProjection<T> {

	private final QueryFunction<T> function;

	/**
	 * Constructor
	 * @param function The function associated with this expression (not null)
	 */
	public DefaultFunctionExpression(QueryFunction<T> function) {
		super();
		ObjectUtils.argumentNotNull(function, "Function must be not null");
		this.function = function;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryExpression#getType()
	 */
	@Override
	public Class<? extends T> getType() {
		return function.getResultType();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryExpression.FunctionExpression#getFunction()
	 */
	@Override
	public QueryFunction<T> getFunction() {
		return function;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getFunction() == null) {
			throw new InvalidExpressionException("Null function");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.query.QueryProjectionVisitor.VisitableQueryProjection#accept(com.holonplatform.
	 * core.internal.query.QueryProjectionVisitor, java.lang.Object)
	 */
	@Override
	public <R, C> R accept(QueryProjectionVisitor<R, C> visitor, C context) {
		return visitor.visit(this, context);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultFunctionExpression [function=" + function + "]";
	}

}
