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
package com.holonplatform.async.datastore.internal.operation;

import java.util.concurrent.CompletionStage;

import com.holonplatform.async.datastore.operation.AsyncDelete;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.internal.datastore.operation.common.AbstractExecutableDatastoreOperation;

/**
 * Abstract {@link AsyncDelete}.
 *
 * @since 5.2.0
 */
public abstract class AbstractAsyncDelete extends
		AbstractExecutableDatastoreOperation<CompletionStage<OperationResult>, AsyncDelete>
		implements AsyncDelete {

	private static final long serialVersionUID = 4518358805871128279L;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.operation.AbstractDatastoreOperation#getActualOperation()
	 */
	@Override
	protected AsyncDelete getActualOperation() {
		return this;
	}

}
