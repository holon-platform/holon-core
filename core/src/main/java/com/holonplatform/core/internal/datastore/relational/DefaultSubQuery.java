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
import com.holonplatform.core.internal.query.AbstractQueryBuilder;
import com.holonplatform.core.internal.query.DefaultQueryDefinition;
import com.holonplatform.core.internal.query.QueryDefinition;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QueryProjection;

/**
 * Default {@link SubQuery} implementation.
 * 
 * @since 5.0.0
 */
public class DefaultSubQuery<T> extends AbstractQueryBuilder<SubQuery<T>, QueryDefinition> implements SubQuery<T> {

	/**
	 * Selection type
	 */
	private final Class<? extends T> expressionType;

	/**
	 * Selection projection
	 */
	private QueryProjection<T> projection;

	/**
	 * Constructor
	 * @param selectionType Sub query selection type
	 */
	public DefaultSubQuery(Class<? extends T> selectionType) {
		super(new DefaultQueryDefinition());
		this.expressionType = selectionType;
	}

	/* (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.AbstractQueryBuilder#getActualBuilder()
	 */
	@Override
	protected SubQuery<T> getActualBuilder() {
		return this;
	}

	@Override
	public SubQuery<T> select(QueryProjection<T> projection) {
		ObjectUtils.argumentNotNull(projection, "Select projection must be not null");
		this.projection = projection;
		return this;
	}

	@Override
	public QueryProjection<T> getSelection() {
		return projection;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryExpression#getType()
	 */
	@Override
	public Class<? extends T> getType() {
		return expressionType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.datastore.jdbc.JdbcSubQuery#exists()
	 */
	@Override
	public QueryFilter exists() {
		return new ExistsFilter(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.datastore.jdbc.JdbcSubQuery#notExists()
	 */
	@Override
	public QueryFilter notExists() {
		return new NotExistsFilter(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getSelection() == null) {
			throw new InvalidExpressionException("Missing sub query projection");
		}
	}

}
