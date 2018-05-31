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

import com.holonplatform.core.query.QueryFilter.QueryFilterSupport;

/**
 * Bulk <code>DELETE</code> {@link ExecutableBulkOperation}.
 * 
 * @param <R> Operation result type
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public interface BulkDeleteOperation<R, O extends BulkDeleteOperation<R, O>>
		extends ExecutableBulkOperation<R, BulkDeleteOperationConfiguration, O>, QueryFilterSupport<O> {

}
