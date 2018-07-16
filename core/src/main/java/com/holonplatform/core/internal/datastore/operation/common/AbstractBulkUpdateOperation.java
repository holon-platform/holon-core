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

import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.operation.commons.BulkUpdateOperation;
import com.holonplatform.core.datastore.operation.commons.BulkUpdateOperationConfiguration;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.query.QueryFilter;

/**
 * Abstract {@link BulkUpdateOperation} implementation.
 * 
 * @param <R> Operation result type
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public abstract class AbstractBulkUpdateOperation<R, O extends BulkUpdateOperation<R, O>>
		extends AbstractDatastoreOperation<O, BulkUpdateOperationConfiguration, BulkUpdateDefinition>
		implements BulkUpdateOperation<R, O> {

	private static final long serialVersionUID = 9003730522004258398L;

	public AbstractBulkUpdateOperation() {
		super(new DefaultBulkUpdateDefinition());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperation#getConfiguration()
	 */
	@Override
	public BulkUpdateOperationConfiguration getConfiguration() {
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

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkUpdateOperation#set(com.holonplatform.core.Path,
	 * com.holonplatform.core.TypedExpression)
	 */
	@Override
	public <T> O set(Path<T> path, TypedExpression<? super T> expression) {
		getDefinition().addValue(path, expression);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkUpdateOperation#set(com.holonplatform.core.property.PropertyBox,
	 * boolean)
	 */
	@Override
	public O set(PropertyBox propertyBox, boolean includeNullValues) {
		getDefinition().setValue(propertyBox, includeNullValues);
		return getActualOperation();
	}

}
