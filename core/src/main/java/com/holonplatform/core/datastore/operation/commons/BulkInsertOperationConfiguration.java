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
package com.holonplatform.core.datastore.operation.commons;

import java.util.List;
import java.util.Optional;

import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;

/**
 * Bulk <code>INSERT</code> operation configuration.
 *
 * @since 5.1.0
 */
public interface BulkInsertOperationConfiguration extends DatastoreOperationConfiguration {

	/**
	 * Get the values to insert.
	 * @return The operation values, an empty List if none
	 */
	List<PropertyBox> getValues();

	/**
	 * Get the operation property set.
	 * @return Optional operation property set
	 */
	Optional<PropertySet<?>> getPropertySet();

}
