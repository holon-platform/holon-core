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

import java.util.Map;

import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.property.PropertyBox;

/**
 * A <code>INSERT</code> {@link BulkOperation}.
 * 
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public interface BulkInsertOperation<O extends BulkInsertOperation<O>> extends BulkOperation<O> {

	/**
	 * Add a path - value expression map to insert.
	 * @param values Value map to add to the bulk insert operation (not null)
	 * @return this
	 */
	O add(Map<Path<?>, TypedExpression<?>> values);

	/**
	 * Add a {@link PropertyBox} to insert.
	 * @param propertyBox PropertyBox to add to the bulk insert operation (not null)
	 * @return this
	 */
	O add(PropertyBox propertyBox);

}
