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
package com.holonplatform.async.datastore.operation;

import java.util.concurrent.CompletionStage;

import com.holonplatform.async.datastore.AsyncDatastore;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.operation.commons.BulkUpdateOperation;

/**
 * Asynchronous {@link BulkUpdateOperation} API, using {@link CompletionStage} as execution result type to handle the
 * operation result asynchronously.
 * 
 * @since 5.2.0
 * 
 * @see AsyncDatastore
 */
public interface AsyncBulkUpdate extends BulkUpdateOperation<CompletionStage<OperationResult>, AsyncBulkUpdate> {

}
