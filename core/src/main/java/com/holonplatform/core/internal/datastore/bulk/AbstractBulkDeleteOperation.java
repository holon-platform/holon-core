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

import com.holonplatform.core.datastore.bulk.BulkDeleteConfiguration;
import com.holonplatform.core.datastore.bulk.BulkDeleteOperation;
import com.holonplatform.core.internal.datastore.operation.AbstractDatastoreOperation;
import com.holonplatform.core.query.QueryFilter;

/**
 * Abstract {@link BulkDeleteOperation} operation.
 * 
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public abstract class AbstractBulkDeleteOperation<O extends BulkDeleteOperation<O>> extends
		AbstractDatastoreOperation<O, BulkDeleteConfiguration, BulkDeleteDefinition> implements BulkDeleteOperation<O> {

	public AbstractBulkDeleteOperation() {
		super(new DefaultBulkDeleteDefinition());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperation#getConfiguration()
	 */
	@Override
	public BulkDeleteConfiguration getConfiguration() {
		return getDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilter.QueryFilterSupport#filter(com.holonplatform.core.query.QueryFilter)
	 */
	@Override
	public O filter(QueryFilter filter) {
		getDefinition().addFilter(filter);
		return getActualOperation();
	}

}
