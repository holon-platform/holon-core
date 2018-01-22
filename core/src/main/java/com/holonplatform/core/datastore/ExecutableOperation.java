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

import com.holonplatform.core.datastore.Datastore.OperationResult;

/**
 * Represents a {@link Datastore} operation which can be executed obtaining an {@link OperationResult} type result.
 * 
 * @since 5.1.0
 */
public interface ExecutableOperation {

	/**
	 * Execute the clause and return the amount of affected data objects
	 * @return the result of the operation execution
	 */
	OperationResult execute();

}
