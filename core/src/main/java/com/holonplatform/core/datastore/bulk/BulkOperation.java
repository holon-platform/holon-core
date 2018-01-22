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
package com.holonplatform.core.datastore.bulk;

import com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder;
import com.holonplatform.core.Path;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.property.PropertySet;

/**
 * Represents a bulk operation.
 * 
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public interface BulkOperation<O extends BulkOperation<O>> extends ExpressionResolverBuilder<O> {

	/**
	 * Set the operation {@link DataTarget}.
	 * @param target the operation data target to set
	 * @return this
	 */
	O target(DataTarget<?> target);

	/**
	 * Set the paths to be used for operation values.
	 * @param paths Operation value paths
	 * @return this
	 */
	O operationPaths(Path<?>[] paths);

	/**
	 * Set the paths to be used for operation values using a {@link PropertySet}.
	 * <p>
	 * Each property of the property set which corresponds to a {@link Path} will be used as operation path.
	 * </p>
	 * @param propertySet The property set to set (not null)
	 * @return this
	 */
	O operationPaths(PropertySet<?> propertySet);

	/**
	 * Add a {@link WriteOption} to this operation.
	 * @param writeOption The write option to add (not null)
	 * @return this
	 */
	O withWriteOption(WriteOption writeOption);

	/**
	 * Add a set of {@link WriteOption}s to this operation.
	 * @param writeOption The write options to add (not null)
	 * @return this
	 */
	O withWriteOptions(WriteOption... writeOptions);

	/**
	 * Get the bulk operation configuration.
	 * @return the bulk operation configuration
	 */
	BulkOperationConfiguration getConfiguration();

}
