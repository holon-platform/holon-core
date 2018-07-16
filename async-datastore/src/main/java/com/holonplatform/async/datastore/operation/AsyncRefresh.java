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
import com.holonplatform.core.datastore.DatastoreOperations;
import com.holonplatform.core.datastore.operation.commons.ExecutablePropertyBoxOperation;
import com.holonplatform.core.datastore.operation.commons.PropertyBoxOperation;
import com.holonplatform.core.property.PropertyBox;

/**
 * Asynchronous <em>refresh</em> {@link PropertyBoxOperation} operation, using {@link CompletionStage} to handle the
 * operation result asynchronously.
 *
 * @since 5.2.0
 * 
 * @see DatastoreOperations#refresh(com.holonplatform.core.datastore.DataTarget, PropertyBox)
 * @see AsyncDatastore
 */
public interface AsyncRefresh
		extends ExecutablePropertyBoxOperation<CompletionStage<PropertyBox>, AsyncRefresh> {

}
