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
package com.holonplatform.core.internal.query;

import com.holonplatform.core.Expression;
import com.holonplatform.core.query.QueryConfiguration;
import com.holonplatform.core.query.QueryProjection;

/**
 * Represents a query structure {@link Expression}, providing {@link QueryConfiguration} and {@link QueryProjection}.
 *
 * @param <T> Projection result type
 *
 * @since 5.0.0
 */
public interface QueryStructure<T> extends Expression {

	/**
	 * Get the {@link QueryConfiguration}.
	 * @return the {@link QueryConfiguration}
	 */
	QueryConfiguration getConfiguration();

	/**
	 * Get the {@link QueryProjection}.
	 * @return the {@link QueryProjection}
	 */
	QueryProjection<T> getProjection();

	/**
	 * Create a new {@link QueryStructure}.
	 * @param <T> Projection result type
	 * @param configuration Query configuration
	 * @param projection Query projection
	 * @return A new {@link QueryStructure}
	 */
	static <T> QueryStructure<T> create(QueryConfiguration configuration, QueryProjection<T> projection) {
		return new DefaultQueryStructure<>(configuration, projection);
	}

}
