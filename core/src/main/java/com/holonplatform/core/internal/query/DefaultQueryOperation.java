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

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryConfiguration;
import com.holonplatform.core.query.QueryOperation;
import com.holonplatform.core.query.QueryProjection;

/**
 * Default {@link QueryOperation} implementation.
 * 
 * @param <C> Query configuration type
 * @param <R> Query result type
 * 
 * @since 5.1.0
 */
public class DefaultQueryOperation<C extends QueryConfiguration, R> implements QueryOperation<C, R> {

	/**
	 * Query configuration
	 */
	private final C configuration;

	/**
	 * Query projection
	 */
	private final QueryProjection<R> projection;

	/**
	 * Constructor.
	 * @param configuration Query configuration (not null)
	 * @param projection Query projection (not null)
	 */
	public DefaultQueryOperation(C configuration, QueryProjection<R> projection) {
		super();
		ObjectUtils.argumentNotNull(configuration, "Query configuration must be not null");
		ObjectUtils.argumentNotNull(projection, "Query projection must be not null");
		this.configuration = configuration;
		this.projection = projection;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryOperation#getConfiguration()
	 */
	@Override
	public C getConfiguration() {
		return configuration;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryOperation#getProjection()
	 */
	@Override
	public QueryProjection<R> getProjection() {
		return projection;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.TypedExpression#getType()
	 */
	@Override
	public Class<? extends R> getType() {
		return getProjection().getType();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getConfiguration() == null) {
			throw new InvalidExpressionException("Null query configuration");
		}
		if (getProjection() == null) {
			throw new InvalidExpressionException("Null query projection");
		}
	}

}
