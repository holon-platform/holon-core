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
package com.holonplatform.core.internal.datastore.operation.common;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.query.QueryFilter;

/**
 * A {@link DatastoreOperationDefinition} with values and restriction support.
 *
 * @since 5.1.0
 */
public abstract class FilterableValueOperationDefinition extends AbstractDatastoreOperationDefinition {

	/*
	 * Filter
	 */
	private QueryFilter filter;

	/*
	 * Operation values
	 */
	private Map<Path<?>, TypedExpression<?>> values;

	/**
	 * Get the optional restriction filter.
	 * @return the optional restriction filter
	 */
	public Optional<QueryFilter> getFilter() {
		return Optional.ofNullable(filter);
	}

	/**
	 * Set the restriction filter.
	 * @param filter the filter to set
	 */
	public void setFilter(QueryFilter filter) {
		this.filter = filter;
	}

	/**
	 * Get the operation values.
	 * @return the operation values
	 */
	public Map<Path<?>, TypedExpression<?>> getValues() {
		return (values != null) ? Collections.unmodifiableMap(values) : Collections.emptyMap();
	}

	/**
	 * Set the operation values.
	 * @param values the values to set
	 */
	public void setValues(Map<Path<?>, TypedExpression<?>> values) {
		this.values = values;
	}

}
