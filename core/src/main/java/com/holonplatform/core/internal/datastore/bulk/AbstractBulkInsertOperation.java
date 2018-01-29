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

import java.util.Map;

import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.bulk.BulkInsertConfiguration;
import com.holonplatform.core.datastore.bulk.BulkInsertOperation;
import com.holonplatform.core.datastore.bulk.BulkUpdate;
import com.holonplatform.core.internal.datastore.operation.AbstractDatastoreOperation;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;

/**
 * Abstract {@link BulkUpdate} operation.
 * 
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public abstract class AbstractBulkInsertOperation<O extends BulkInsertOperation<O>> extends
		AbstractDatastoreOperation<O, BulkInsertConfiguration, BulkInsertDefinition> implements BulkInsertOperation<O> {

	public AbstractBulkInsertOperation() {
		super(new DefaultBulkInsertDefinition());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperation#getConfiguration()
	 */
	@Override
	public BulkInsertConfiguration getConfiguration() {
		return getDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkInsertOperation#add(java.util.Map)
	 */
	@Override
	public O add(Map<Path<?>, TypedExpression<?>> values) {
		getDefinition().addValue(values);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkInsertOperation#add(com.holonplatform.core.property.PropertyBox)
	 */
	@Override
	public O add(PropertyBox propertyBox) {
		getDefinition().addValue(propertyBox, false);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkInsertOperation#operationPaths(com.holonplatform.core.Path[])
	 */
	@Override
	public O operationPaths(Path<?>[] paths) {
		getDefinition().setOperationPaths(paths);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkInsertOperation#operationPaths(com.holonplatform.core.property.
	 * PropertySet)
	 */
	@Override
	public O operationPaths(PropertySet<?> propertySet) {
		getDefinition().setOperationPaths(propertySet);
		return getActualOperation();
	}

}
