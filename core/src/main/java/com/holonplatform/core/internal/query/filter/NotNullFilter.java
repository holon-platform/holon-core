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
package com.holonplatform.core.internal.query.filter;

import com.holonplatform.core.internal.query.QueryFilterVisitor;
import com.holonplatform.core.query.QueryExpression;
import com.holonplatform.core.query.QueryFilter;

/**
 * Filter which represents a "IS NOT NULL" predicate.
 * 
 * @since 4.4.0
 * 
 * @see QueryFilter
 */
@SuppressWarnings("rawtypes")
public class NotNullFilter extends AbstractOperationQueryFilter {

	private static final long serialVersionUID = 2882153798854776520L;

	/**
	 * Constructor.
	 * @param expression Operand expression
	 */
	@SuppressWarnings("unchecked")
	public NotNullFilter(QueryExpression<?> expression) {
		super(expression, FilterOperator.NOT_NULL);
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
