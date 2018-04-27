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
package com.holonplatform.core.datastore.async.operation;

import java.util.concurrent.CompletableFuture;

import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.DatastoreCommodity;
import com.holonplatform.core.datastore.DatastoreOperations;
import com.holonplatform.core.datastore.operation.ExecutableOperation;
import com.holonplatform.core.datastore.operation.PropertyBoxOperation;
import com.holonplatform.core.datastore.operation.PropertyBoxOperationConfiguration;
import com.holonplatform.core.property.PropertyBox;

/**
 * Asynchronous <em>delete</em> Datastore operation.
 *
 * @since 5.2.0
 * 
 * @see DatastoreOperations#delete(com.holonplatform.core.datastore.DataTarget, PropertyBox,
 *      com.holonplatform.core.datastore.DatastoreOperations.WriteOption...)
 */
public interface AsyncDeleteOperation
		extends PropertyBoxOperation<AsyncDeleteOperation, PropertyBoxOperationConfiguration>,
		ExecutableOperation<CompletableFuture<OperationResult>>, DatastoreCommodity {

}
