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
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QueryFilter.QueryFilterSupport;

/**
 * Update operation configuration.
 *
 * @since 5.1.0
 */
public interface UpdateOperationConfiguration extends DatastoreOperationConfiguration {

	/**
	 * Get the values to update as a {@link Path} - {@link TypedExpression} map.
	 * @return Map of the values to update associated with their paths, empty if none
	 */
	Map<Path<?>, TypedExpression<?>> getValues();

	/**
	 * Get the optional operation restrictions, expressed as a {@link QueryFilter}.
	 * @return Optional operation filter
	 */
	Optional<QueryFilter> getFilter();

	/**
	 * Get a builder to create {@link UpdateOperationConfiguration} instances.
	 * @return A {@link UpdateOperationConfiguration} builder
	 */
	static Builder builder() {
		return new DefaultUpdateOperationConfiguration.DefaultBuilder();
	}

	/**
	 * {@link UpdateOperationConfiguration} builder.
	 */
	public interface Builder extends DatastoreOperationConfiguration.Builder<Builder>, QueryFilterSupport<Builder> {

		/**
		 * Set the operation values.
		 * @param values The values to set (not null)
		 * @return this
		 */
		Builder values(Map<Path<?>, TypedExpression<?>> values);

		/**
		 * Build the configuration instance.
		 * @return the configuration instance
		 */
		UpdateOperationConfiguration build();

	}

}
