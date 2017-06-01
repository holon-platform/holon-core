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
package com.holonplatform.core.internal.query.sort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.holonplatform.core.internal.query.QuerySortVisitor;
import com.holonplatform.core.internal.query.QuerySortVisitor.VisitableQuerySort;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.query.QuerySort.CompositeQuerySort;

/**
 * Default {@link CompositeQuerySort} implementation.
 * 
 * @since 4.5.0
 */
public class MultiSort implements CompositeQuerySort, VisitableQuerySort {

	private static final long serialVersionUID = -2477940296888448572L;

	/**
	 * Sorts list
	 */
	private final LinkedList<QuerySort> sorts;

	/**
	 * Default constructor
	 */
	public MultiSort() {
		this((List<? extends QuerySort>) null);
	}

	/**
	 * Constructor with initial value
	 * @param sorts Build MultiSort including this sorts list as initial value
	 */
	public MultiSort(QuerySort... sorts) {
		this((sorts != null) ? Arrays.asList(sorts) : null);
	}

	/**
	 * Constructor with initial value
	 * @param sorts Build MultiSort including this sorts list as initial value
	 */
	public MultiSort(List<? extends QuerySort> sorts) {
		super();
		this.sorts = new LinkedList<>();
		if (sorts != null) {
			this.sorts.addAll(sorts);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.sort.CompositeQuerySort#getComposition()
	 */
	@Override
	public List<QuerySort> getComposition() {
		return sorts;
	}

	/**
	 * Append a sort expression
	 * @param sort Sort to append to current sorts
	 */
	public void addSort(QuerySort sort) {
		ObjectUtils.argumentNotNull(sort, "Sort to append must be not null");
		sorts.add(sort);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (sorts.isEmpty()) {
			throw new InvalidExpressionException("Sort list is empty");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.VisitableQuerySort#accept(com.holonplatform.core.query.QuerySortVisitor,
	 * java.lang.Object)
	 */
	@Override
	public <R, C> R accept(QuerySortVisitor<R, C> visitor, C context) {
		return visitor.visit(this, context);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MultiSort of " + sorts.size() + " sorts");
		if (!sorts.isEmpty()) {
			sb.append(" [");
			for (QuerySort sort : sorts) {
				sb.append(sort);
				sb.append(" ");
			}
			sb.append("]");
		}
		return sb.toString();
	}

}
