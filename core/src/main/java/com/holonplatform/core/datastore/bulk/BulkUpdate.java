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

import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.DatastoreCommodity;
import com.holonplatform.core.datastore.ExecutableOperation;

/**
 * A {@link BulkOperation} builder to configure a bulk <code>UPDATE</code> operation and execute it using the
 * {@link ExecutableOperation} interface methods.
 * <p>
 * Extends {@link DatastoreCommodity} to allow query definition and registration using the {@link Datastore} commodities
 * paradigm.
 * </p>
 * 
 * @since 5.0.0
 */
public interface BulkUpdate extends BulkUpdateOperation<BulkUpdate>, ExecutableOperation, DatastoreCommodity {

}
