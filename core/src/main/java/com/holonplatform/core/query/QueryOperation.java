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
package com.holonplatform.core.query;

import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.query.DefaultQueryOperation;

/**
 * Represent a <em>query</em> operation expression, providing query configuration and projection.
 *
 * @param <C> Query configuration type
 * @param <R> Query result type
 * 
 * @since 5.1.0
 */
public interface QueryOperation<C extends QueryConfiguration, R> extends TypedExpression<R> {

	/**
	 * Get the query configuration.
	 * @return the query configuration
	 */
	C getConfiguration();

	/**
	 * Get the query projection.
	 * @return the query projection
	 */
	QueryProjection<R> getProjection();

	/**
	 * Create a new {@link QueryOperation}.
	 * @param <C> Query configuration type
	 * @param <R> Query result type
	 * @param configuration Query configuration (not null)
	 * @param projection Query projection (not null)
	 * @return A new {@link QueryOperation}
	 */
	static <C extends QueryConfiguration, R> QueryOperation<C, R> create(C configuration,
			QueryProjection<R> projection) {
		return new DefaultQueryOperation<>(configuration, projection);
	}

}
