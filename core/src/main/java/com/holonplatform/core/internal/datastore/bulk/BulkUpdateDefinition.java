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
package com.holonplatform.core.internal.datastore.bulk;

import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.bulk.BulkUpdateConfiguration;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.query.QueryFilter;

/**
 * Bulk <code>UPDATE</code> operation definition.
 *
 * @since 5.1.0
 */
public interface BulkUpdateDefinition extends BulkOperationDefinition, BulkUpdateConfiguration {

	/**
	 * Add an operation restriction filter.
	 * @param filter the filter to add (not null)
	 */
	void addFilter(QueryFilter filter);

	/**
	 * Add an operation value.
	 * @param path Path (not null)
	 * @param value Value (not null)
	 */
	void addValue(Path<?> path, TypedExpression<?> value);

	/**
	 * Set update operation values using given {@link PropertyBox}.
	 * <p>
	 * Each {@link Path} type property of the PropertyBox property set will be setted to its corresponding value in the
	 * PropertyBox. Any previously path binding will be replaced by a new path binding for matching paths.
	 * </p>
	 * @param propertyBox PropertyBox to use (not null)
	 * @param includeNullValues <code>true</code> to update to <code>null</code> any path without a value in the
	 *        PropertyBox, <code>false</code> to ignore them
	 */
	void setValue(PropertyBox propertyBox, boolean includeNullValues);

}
