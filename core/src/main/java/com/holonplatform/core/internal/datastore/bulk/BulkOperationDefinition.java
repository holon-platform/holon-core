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

import java.util.Map;

import com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport;
import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.datastore.bulk.BulkOperationConfiguration;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.QueryFilter;

/**
 * {@link BulkOperationConfiguration} definition with configuration setters.
 *
 * @since 5.1.0
 */
public interface BulkOperationDefinition extends BulkOperationConfiguration, ExpressionResolverSupport {

	/**
	 * Set the operation {@link DataTarget}.
	 * @param <T> Data target type
	 * @param target the target to set
	 */
	<T> void setTarget(DataTarget<T> target);

	/**
	 * Add an operation restriction filter.
	 * @param filter the filter to add (not null)
	 */
	void addFilter(QueryFilter filter);

	/**
	 * Add an operation value.
	 * @param value The value to add (not null)
	 */
	void addValue(Map<Path<?>, TypedExpression<?>> value);

	/**
	 * Add an operation value using a {@link PropertyBox}.
	 * @param value The value to add (not null)
	 * @param includeNullValues Whether to set <code>null</code> path values in the PropertyBox to the <code>null</code>
	 *        value
	 */
	void addValue(PropertyBox value, boolean includeNullValues);

	/**
	 * Set the paths to be used for operation values.
	 * @param paths Operation value paths
	 */
	void setOperationPaths(Path<?>[] paths);

	/**
	 * Set the paths to be used for operation values using a {@link PropertySet}.
	 * <p>
	 * Each property of the property set which corresponds to a {@link Path} will be used as operation path.
	 * </p>
	 * @param propertySet The property set to set (not null)
	 */
	void setOperationPaths(PropertySet<?> propertySet);

	/**
	 * Add a {@link WriteOption} to this operation.
	 * @param writeOption The write option to add (not null)
	 */
	void addWriteOption(WriteOption writeOption);

	/**
	 * Create a new default {@link BulkOperationDefinition}.
	 * @return A new {@link BulkOperationDefinition}
	 */
	static BulkOperationDefinition create() {
		return new DefaultBulkOperationDefinition();
	}

}
