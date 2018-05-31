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

import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.operation.commons.BulkUpdateOperation;
import com.holonplatform.core.datastore.operation.commons.ExecutableOperation;

/**
 * A {@link BulkUpdateOperation} builder to configure a bulk <code>UPDATE</code> operation and execute it using the
 * {@link ExecutableOperation} interface methods.
 * 
 * @since 5.0.0
 */
public interface BulkUpdate extends BulkUpdateOperation<OperationResult, BulkUpdate> {

}
