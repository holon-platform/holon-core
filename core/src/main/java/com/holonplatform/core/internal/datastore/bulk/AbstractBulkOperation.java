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
package com.holonplatform.core.internal.datastore.bulk;

import com.holonplatform.core.datastore.Datastore.OperationType;
import com.holonplatform.core.datastore.bulk.BulkOperation;
import com.holonplatform.core.datastore.bulk.BulkOperationConfiguration;

/**
 * Abstract {@link BulkOperation}.
 * 
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public abstract class AbstractBulkOperation<O extends BulkOperation<O>> implements BulkOperation<O> {

	private final BulkOperationDefinition definition;

	/**
	 * Constructor.
	 * @param operationType Operation type (not null)
	 */
	public AbstractBulkOperation(OperationType operationType) {
		super();
		this.definition = BulkOperationDefinition.create(operationType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperation#getConfiguration()
	 */
	@Override
	public BulkOperationConfiguration getConfiguration() {
		return definition;
	}

	/**
	 * Get the operation definition.
	 * @return the operation definition
	 */
	protected BulkOperationDefinition getDefinition() {
		return definition;
	}

}
