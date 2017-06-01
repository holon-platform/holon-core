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

import java.util.List;

import com.holonplatform.core.internal.query.QueryFilterVisitor;
import com.holonplatform.core.query.QueryFilter;

/**
 * Filter which represents the disjunction of other filter conditions.
 * 
 * @since 4.4.0
 * 
 * @see QueryFilter
 */
public class OrFilter extends AbstractCompositeQueryFilter {

	private static final long serialVersionUID = 4449101711061631364L;

	/**
	 * Constructor
	 */
	public OrFilter() {
		super();
	}

	/**
	 * Constructor with sub filters composition.
	 * @param <Q> Actual filter type
	 * @param composition Sub filters list
	 */
	public <Q extends QueryFilter> OrFilter(List<Q> composition) {
		super(composition);
	}

	/**
	 * Constructor with sub filters composition
	 * @param composition Sub filters list
	 */
	public OrFilter(QueryFilter... composition) {
		super(composition);
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
		if (getComposition() != null && !getComposition().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < getComposition().size(); i++) {
				sb.append("[");
				sb.append(getComposition().get(i).toString());
				sb.append("]");
				if (i < (getComposition().size() - 1)) {
					sb.append(" OR ");
				}
			}
			return sb.toString();
		} else {
			return "OrFilter";
		}
	}

}
