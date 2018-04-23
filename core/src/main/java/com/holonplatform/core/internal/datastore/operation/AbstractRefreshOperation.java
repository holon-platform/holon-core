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
package com.holonplatform.core.internal.datastore.operation;

import com.holonplatform.core.datastore.operation.PropertyBoxOperationConfiguration;
import com.holonplatform.core.datastore.operation.RefreshOperation;

/**
 * Abstract {@link RefreshOperation}.
 *
 * @since 5.1.0
 */
public abstract class AbstractRefreshOperation extends
		AbstractPropertyBoxOperation<RefreshOperation, PropertyBoxOperationConfiguration, PropertyBoxOperationDefinition>
		implements RefreshOperation {

	private static final long serialVersionUID = -8905264822507783362L;

	public AbstractRefreshOperation() {
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
	protected RefreshOperation getActualOperation() {
		return this;
	}

}
