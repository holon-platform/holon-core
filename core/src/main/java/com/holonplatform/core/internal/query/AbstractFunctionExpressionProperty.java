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

import com.holonplatform.core.internal.property.AbstractProperty;
import com.holonplatform.core.internal.query.QueryProjectionVisitor.VisitableQueryProjection;
import com.holonplatform.core.query.FunctionExpression.FunctionExpressionProperty;
import com.holonplatform.core.query.QueryFunction;

/**
 * Abstract {@link FunctionExpressionProperty} implementation.
 *
 * @param <T> Function and property type
 * 
 * @since 5.0.0
 */
public abstract class AbstractFunctionExpressionProperty<T>
		extends AbstractProperty<T, AbstractFunctionExpressionProperty<T>>
		implements FunctionExpressionProperty<T>, VisitableQueryProjection<T> {

	private static final long serialVersionUID = 5578879579540045987L;

	private final QueryFunction<T> function;

	public AbstractFunctionExpressionProperty(QueryFunction<T> function) {
		super(function.getResultType());
		this.function = function;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return true;
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
		return "AbstractFunctionExpressionProperty [function=" + function + "]";
	}

}
