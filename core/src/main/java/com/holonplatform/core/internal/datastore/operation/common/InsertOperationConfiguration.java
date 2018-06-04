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

import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.operation.commons.DatastoreOperationConfiguration;
import com.holonplatform.core.property.PropertySet;

/**
 * Insert operation configuration.
 *
 * @since 5.1.0
 */
public interface InsertOperationConfiguration extends DatastoreOperationConfiguration {

	/**
	 * Get the values to insert as a {@link Path} - {@link TypedExpression} map.
	 * @return Map of the values to insert associated with their paths, empty if none
	 */
	Map<Path<?>, TypedExpression<?>> getValues();

	/**
	 * Get the optional {@link Path}s which has to be used when configuring operation values.
	 * @return Optional operation value paths
	 */
	Optional<Path<?>[]> getOperationPaths();

	/**
	 * Get a builder to create {@link InsertOperationConfiguration} instances.
	 * @return A {@link InsertOperationConfiguration} builder
	 */
	static Builder builder() {
		return new DefaultInsertOperationConfiguration.DefaultBuilder();
	}

	/**
	 * {@link InsertOperationConfiguration} builder.
	 */
	public interface Builder extends DatastoreOperationConfiguration.Builder<Builder> {

		/**
		 * Set the operation values.
		 * @param values The values to set (not null)
		 * @return this
		 */
		Builder values(Map<Path<?>, TypedExpression<?>> values);

		/**
		 * Set the paths to be used for operation values.
		 * @param paths Operation value paths
		 * @return this
		 */
		Builder operationPaths(Path<?>[] paths);

		/**
		 * Set the paths to be used for operation values using a {@link PropertySet}.
		 * <p>
		 * Each property of the property set which corresponds to a {@link Path} will be used as operation path.
		 * </p>
		 * @param propertySet The property set to set (not null)
		 * @return this
		 */
		Builder operationPaths(PropertySet<?> propertySet);

		/**
		 * Build the configuration instance.
		 * @return the configuration instance
		 */
		InsertOperationConfiguration build();

	}

}
