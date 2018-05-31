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
package com.holonplatform.core.internal.datastore.operation.common;

import com.holonplatform.core.datastore.operation.commons.BulkDeleteOperation;
import com.holonplatform.core.datastore.operation.commons.BulkDeleteOperationConfiguration;
import com.holonplatform.core.query.QueryFilter;

/**
 * Abstract {@link BulkDeleteOperation} implementation.
 * 
 * @param <R> Operation result type
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public abstract class AbstractBulkDeleteOperation<R, O extends BulkDeleteOperation<R, O>>
		extends AbstractDatastoreOperation<O, BulkDeleteOperationConfiguration, BulkDeleteDefinition>
		implements BulkDeleteOperation<R, O> {

	private static final long serialVersionUID = -2715411015026648240L;

	public AbstractBulkDeleteOperation() {
		super(new DefaultBulkDeleteDefinition());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperation#getConfiguration()
	 */
	@Override
	public BulkDeleteOperationConfiguration getConfiguration() {
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
