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

import com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryAggregation;
import com.holonplatform.core.query.QueryConfiguration;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QuerySort;

/**
 * Extend {@link QueryConfiguration} to store all query configuration elements, such as filters, sorts, paging options
 * and parameters.
 * 
 * @since 5.0.0
 */
public interface QueryDefinition extends QueryConfiguration, ExpressionResolverSupport {

	/**
	 * Set query {@link DataTarget}.
	 * @param <T> Target type
	 * @param target Target to set
	 */
	<T> void setTarget(DataTarget<T> target);

	/**
	 * Set query results limit. If not null, must be greater than <code>0</code>.
	 * @param limit Results limit, or <code>null</code> for no limit.
	 */
	void setLimit(Integer limit);

	/**
	 * Starts the query results at a particular zero-based offset.
	 * @param offset Results offset 0-based index. If not null, must be greater or equal to <code>0</code>.
	 */
	void setOffset(Integer offset);

	/**
	 * Add a sort to query. If any sort was present, sort will be appended in specified order
	 * @param sort Sort to add (not null)
	 */
	void addSort(QuerySort sort);

	/**
	 * Add a filter to query
	 * @param filter Filter to add (not null)
	 */
	void addFilter(QueryFilter filter);

	/**
	 * Set the query results aggregation clause.
	 * @param aggregation the aggregation clause to set
	 */
	void setAggregation(QueryAggregation aggregation);

	/**
	 * Set whether the query should return <em>distinct</em> query projection result values.
	 * @param distinct <code>true</code> if the query should return <em>distinct</em> query projection result values,
	 *        <code>false</code> otherwise
	 * @since 5.2.0
	 */
	void setDistinct(boolean distinct);

	/**
	 * Add a parameter. If parameter with <code>name</code> already exists, its value will be replaced by new
	 * <code>value</code>.
	 * @param name Parameter name (not null)
	 * @param value Parameter value
	 */
	void addParameter(String name, Object value);

	/**
	 * Add a parameter using a {@link ConfigProperty}, with {@link ConfigProperty#getKey()} as parameter name.
	 * @param <T> Configuration property type
	 * @param property Configuration property (not null)
	 * @param value Property value
	 */
	default <T> void addParameter(ConfigProperty<T> property, T value) {
		ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
		addParameter(property.getKey(), value);
	}

	/**
	 * Create a new {@link QueryDefinition}.
	 * @return A new {@link QueryDefinition} instance
	 */
	static QueryDefinition create() {
		return new DefaultQueryDefinition();
	}

}
