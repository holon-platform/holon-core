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

/**
 * Default {@link ConstantExpressionProjection} implementation.
 *
 * @param <T> Expression type
 *
 * @since 5.0.0
 */
public class DefaultConstantExpressionProjection<T> extends DefaultConstantExpression<T>
		implements ConstantExpressionProjection<T>, VisitableQueryProjection<T> {

	/**
	 * Constructor
	 * @param value Constant value (not null)
	 */
	public DefaultConstantExpressionProjection(T value) {
		super(value);
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

}
