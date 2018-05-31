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
package com.holonplatform.core.internal.datastore.beans;

import com.holonplatform.core.datastore.beans.BeanBulkDelete;
import com.holonplatform.core.datastore.bulk.BulkDelete;
import com.holonplatform.core.datastore.operation.commons.BulkDeleteOperationConfiguration;
import com.holonplatform.core.query.QueryFilter;

/**
 * Default {@link BeanBulkDelete} operation.
 * 
 * @param <B> Bean type
 *
 * @since 5.1.0
 */
public class DefaultBeanBulkDelete<B> extends AbstractBeanDatastoreOperation<B, BulkDelete, BeanBulkDelete<B>>
		implements BeanBulkDelete<B> {

	public DefaultBeanBulkDelete(Class<? extends B> beanClass, BulkDelete executor) {
		super(beanClass, executor);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.beans.AbstractBeanDatastoreOperation#getBuilder()
	 */
	@Override
	protected BeanBulkDelete<B> getBuilder() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastoreOperation#getConfiguration()
	 */
	@Override
	public BulkDeleteOperationConfiguration getConfiguration() {
		return getExecutor().getConfiguration();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilter.QueryFilterSupport#filter(com.holonplatform.core.query.QueryFilter)
	 */
	@Override
	public BeanBulkDelete<B> filter(QueryFilter filter) {
		getExecutor().filter(filter);
		return getBuilder();
	}

}
