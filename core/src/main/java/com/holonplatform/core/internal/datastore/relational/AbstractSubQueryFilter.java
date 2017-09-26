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
package com.holonplatform.core.internal.datastore.relational;

import com.holonplatform.core.datastore.relational.SubQuery;
import com.holonplatform.core.query.QueryFilter;

/**
 * Base class for filters involving a sub query.
 * 
 * @since 5.0.0
 */
public abstract class AbstractSubQueryFilter implements QueryFilter {

	private static final long serialVersionUID = -6991633374305704430L;

	/**
	 * Sub query
	 */
	private final SubQuery<?> subQuery;

	/**
	 * Constructor
	 * @param subQuery Sub query
	 */
	public AbstractSubQueryFilter(SubQuery<?> subQuery) {
		super();
		this.subQuery = subQuery;
	}

	/**
	 * Gets the sub query bound to this filter
	 * @return Sub query bound to this filter
	 */
	public SubQuery<?> getSubQuery() {
		return subQuery;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getSubQuery() == null) {
			throw new InvalidExpressionException("Missing filter sub query");
		}
	}

}
