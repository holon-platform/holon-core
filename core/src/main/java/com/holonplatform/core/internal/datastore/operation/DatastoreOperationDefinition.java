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
package com.holonplatform.core.internal.datastore.operation;

import com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration;

/**
 * {@link DatastoreOperationConfiguration} definition with configuration setters.
 *
 * @since 5.1.0
 */
public interface DatastoreOperationDefinition extends DatastoreOperationConfiguration, ExpressionResolverSupport {

	/**
	 * Set the operation {@link DataTarget}.
	 * @param <T> Data target type
	 * @param target the target to set
	 */
	<T> void setTarget(DataTarget<T> target);

	/**
	 * Add a {@link WriteOption} to this operation.
	 * @param writeOption The write option to add (not null)
	 */
	void addWriteOption(WriteOption writeOption);

}
