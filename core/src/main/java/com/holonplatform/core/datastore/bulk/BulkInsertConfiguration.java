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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration;
import com.holonplatform.core.query.ConstantExpression;

/**
 * Bulk <code>INSERT</code> operation configuration.
 *
 * @since 5.1.0
 */
public interface BulkInsertConfiguration extends DatastoreOperationConfiguration {

	/**
	 * Get the operation values, expressed as a List of {@link Path} - {@link ConstantExpression} maps.
	 * @return The path-value expression map list, empty if none
	 */
	List<Map<Path<?>, ConstantExpression<?>>> getValues();

	/**
	 * Get the optional {@link Path}s which has to be used when configuring operation values.
	 * @return Optional operation value paths
	 */
	Optional<Path<?>[]> getOperationPaths();

}
