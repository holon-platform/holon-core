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
package com.holonplatform.core.datastore;

import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;

/**
 * Default Datastore {@link WriteOption}s enumeration.
 *
 * @since 5.0.0
 */
public enum DefaultWriteOption implements WriteOption {

	/**
	 * Bring back any auto-generated id value into the {@link PropertyBox} which was subject of a data manipulation
	 * operation, if a corresponding {@link Property} (using the property name) is available in the box property set.
	 */
	BRING_BACK_GENERATED_IDS,

	/**
	 * By default, the {@link DatastoreOperations#save(DataTarget, PropertyBox, WriteOption...)} operation should
	 * fallback to an <code>INSERT</code> type operation when the value existence cannot be consistently verified in the
	 * persistence source (for example, is the persistence source entity supports a primary key and the primary key
	 * cannot be obtained or the primary key values to use are not provided) to determine whether an <code>UPDATE</code>
	 * type operation should by performed. This write option disables the default behaviour, forcing to throw an error
	 * in such kind of situations.
	 */
	SAVE_DISABLE_INSERT_FALLBACK;

}
