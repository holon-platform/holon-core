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
package com.holonplatform.core.internal.async.operation;

import com.holonplatform.core.datastore.async.operation.AsyncDeleteOperation;
import com.holonplatform.core.datastore.operation.PropertyBoxOperationConfiguration;
import com.holonplatform.core.internal.datastore.operation.AbstractPropertyBoxOperation;
import com.holonplatform.core.internal.datastore.operation.DefaultPropertyBoxOperationDefinition;
import com.holonplatform.core.internal.datastore.operation.PropertyBoxOperationDefinition;

/**
 * Abstract {@link AsyncDeleteOperation}.
 *
 * @since 5.2.0
 */
public abstract class AbstractAsyncDeleteOperation extends
		AbstractPropertyBoxOperation<AsyncDeleteOperation, PropertyBoxOperationConfiguration, PropertyBoxOperationDefinition>
		implements AsyncDeleteOperation {

	private static final long serialVersionUID = 4518358805871128279L;

	public AbstractAsyncDeleteOperation() {
		super(new DefaultPropertyBoxOperationDefinition());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.DatastoreOperation#getConfiguration()
	 */
	@Override
	public PropertyBoxOperationConfiguration getConfiguration() {
		return getDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.operation.AbstractDatastoreOperation#getActualOperation()
	 */
	@Override
	protected AsyncDeleteOperation getActualOperation() {
		return this;
	}

}
