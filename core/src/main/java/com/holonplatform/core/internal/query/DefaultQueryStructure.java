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

import com.holonplatform.core.query.QueryConfiguration;
import com.holonplatform.core.query.QueryProjection;

/**
 * Default {@link QueryStructure} implementation.
 *
 * @param <T> Projection result type
 *
 * @since 5.0.0
 */
public class DefaultQueryStructure<T> implements QueryStructure<T> {

	/**
	 * Query configuration
	 */
	private QueryConfiguration configuration;

	/**
	 * Query projection
	 */
	private QueryProjection<T> projection;

	/**
	 * Default constructor.
	 */
	public DefaultQueryStructure() {
		super();
	}

	/**
	 * Constructor.
	 * @param configuration Query configuration
	 * @param projection Query projection
	 */
	public DefaultQueryStructure(QueryConfiguration configuration, QueryProjection<T> projection) {
		super();
		this.configuration = configuration;
		this.projection = projection;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.QueryStructure#getConfiguration()
	 */
	@Override
	public QueryConfiguration getConfiguration() {
		return configuration;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.QueryStructure#getProjection()
	 */
	@Override
	public QueryProjection<T> getProjection() {
		return projection;
	}

	/**
	 * Set the {@link QueryConfiguration}.
	 * @param configuration the query configuration to set
	 */
	public void setConfiguration(QueryConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Set the {@link QueryProjection}.
	 * @param projection the query projection to set
	 */
	public void setProjection(QueryProjection<T> projection) {
		this.projection = projection;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getConfiguration() == null) {
			throw new InvalidExpressionException("Missing query configuration");
		}
		if (getProjection() == null) {
			throw new InvalidExpressionException("Missing query projection");
		}
	}

}
