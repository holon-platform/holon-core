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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.holonplatform.core.internal.query.QueryFilterVisitor.VisitableQueryFilter;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QueryFilter.CompositeQueryFilter;

/**
 * {@link CompositeQueryFilter} abstract base class.
 * 
 * @since 5.0.0
 * 
 * @see QueryFilter
 */
public abstract class AbstractCompositeQueryFilter implements CompositeQueryFilter, VisitableQueryFilter {

	private static final long serialVersionUID = 3408221394416069608L;

	/*
	 * Sub filters
	 */
	private List<QueryFilter> composition;

	/**
	 * Constructor
	 */
	public AbstractCompositeQueryFilter() {
		this((List<QueryFilter>) null);
	}

	/**
	 * Constructor with sub filters.
	 * @param <Q> Actual filter type
	 * @param composition Composition filters
	 */
	@SuppressWarnings("unchecked")
	public <Q extends QueryFilter> AbstractCompositeQueryFilter(List<Q> composition) {
		super();
		this.composition = (List<QueryFilter>) composition;
	}

	/**
	 * Constructor with sub filters
	 * @param composition Composition filters
	 */
	public AbstractCompositeQueryFilter(QueryFilter... composition) {
		super();
		if (composition != null && composition.length > 0) {
			this.composition = Arrays.asList(composition);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.filter.CompositeQueryFilter#getComposition()
	 */
	@Override
	public List<QueryFilter> getComposition() {
		return composition;
	}

	/**
	 * Set composition
	 * @param composition Composition sub filters
	 */
	public void setComposition(List<QueryFilter> composition) {
		this.composition = composition;
	}

	/**
	 * Adds a filter to sub filters composition
	 * @param filter Filter to add
	 */
	public void addFilter(QueryFilter filter) {
		if (filter != null) {
			if (this.composition == null) {
				composition = new ArrayList<>(4);
			}
			composition.add(filter);
		}
	}

}
