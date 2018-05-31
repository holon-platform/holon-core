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

import com.holonplatform.core.datastore.beans.BeanBulkInsert;
import com.holonplatform.core.datastore.bulk.BulkInsert;
import com.holonplatform.core.datastore.operation.commons.BulkInsertOperationConfiguration;

/**
 * Default {@link BeanBulkInsert} operation.
 * 
 * @param <B> Bean type
 *
 * @since 5.1.0
 */
public class DefaultBeanBulkInsert<B> extends AbstractBeanDatastoreOperation<B, BulkInsert, BeanBulkInsert<B>>
		implements BeanBulkInsert<B> {

	public DefaultBeanBulkInsert(Class<? extends B> beanClass, BulkInsert operation) {
		super(beanClass, operation);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.beans.AbstractBeanDatastoreOperation#getBuilder()
	 */
	@Override
	protected BeanBulkInsert<B> getBuilder() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanBulkInsertOperation#add(java.lang.Object)
	 */
	@Override
	public BeanBulkInsert<B> add(B value) {
		getExecutor().add(asPropertyBox(value));
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.DatastoreOperation#getConfiguration()
	 */
	@Override
	public BulkInsertOperationConfiguration getConfiguration() {
		return getExecutor().getConfiguration();
	}

}
