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

import java.util.Collections;

import com.holonplatform.core.internal.query.QueryFilterVisitor;
import com.holonplatform.core.query.QueryFilter;

/**
 * Filter which represents the negation of another filter condition.
 * 
 * @since 4.4.0
 * 
 * @see QueryFilter
 */
public class NotFilter extends AbstractCompositeQueryFilter {

	private static final long serialVersionUID = 9123323673249584725L;

	/**
	 * Constructor
	 * @param toNegate Filter to negate
	 */
	public NotFilter(QueryFilter toNegate) {
		super((toNegate == null) ? null : Collections.singletonList(toNegate));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.filter.AbstractCompositeQueryFilter#addFilter(com.holonplatform.core.query.
	 * QueryFilter)
	 */
	@Override
	public void addFilter(QueryFilter filter) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getComposition() == null || getComposition().isEmpty()) {
			throw new InvalidExpressionException("Null or empty filter composition");
		}
		if (getComposition().size() > 1) {
			throw new InvalidExpressionException("NOT filter composition size must be equal to 1");
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		QueryFilter toNegate = null;
		if (getComposition() != null && !getComposition().isEmpty()) {
			toNegate = getComposition().get(0);
		}
		return "NOT [" + ((toNegate != null) ? toNegate.toString() : "NULL") + "]";
	}

}
