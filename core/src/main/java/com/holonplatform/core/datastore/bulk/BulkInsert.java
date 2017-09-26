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
import com.holonplatform.core.property.PropertyBox;

/**
 * {@link BulkClause} to execute bulk INSERT operations.
 * 
 * @since 5.0.0
 */
public interface BulkInsert extends DMLClause<BulkInsert>, ExpressionResolverBuilder<BulkInsert> {

	/**
	 * Add a {@link PropertyBox} to insert.
	 * @param propertyBox PropertyBox to add to the bulk insert operation
	 * @return Modified BulkInsert
	 */
	BulkInsert add(PropertyBox propertyBox);

}
