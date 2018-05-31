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

import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.beans.BeanBulkUpdate;
import com.holonplatform.core.datastore.bulk.BulkUpdate;
import com.holonplatform.core.datastore.operation.commons.BulkUpdateOperationConfiguration;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.query.QueryFilter;

/**
 * Default {@link BeanBulkUpdate} operation.
 * 
 * @param <B> Bean type
 *
 * @since 5.1.0
 */
public class DefaultBeanBulkUpdate<B> extends AbstractBeanDatastoreOperation<B, BulkUpdate, BeanBulkUpdate<B>>
		implements BeanBulkUpdate<B> {

	public DefaultBeanBulkUpdate(Class<? extends B> beanClass, BulkUpdate operation) {
		super(beanClass, operation);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.beans.AbstractBeanDatastoreOperation#getBuilder()
	 */
	@Override
	protected BeanBulkUpdate<B> getBuilder() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilter.QueryFilterSupport#filter(com.holonplatform.core.query.QueryFilter)
	 */
	@Override
	public BeanBulkUpdate<B> filter(QueryFilter filter) {
		getExecutor().filter(filter);
		return getBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanBulkUpdateOperation#set(java.lang.String, java.lang.Object)
	 */
	@Override
	public BeanBulkUpdate<B> set(String propertyName, Object value) {
		ObjectUtils.argumentNotNull(propertyName, "Property name must be not null");
		if (value == null) {
			return setNull(propertyName);
		}
		final PathProperty<Object> beanProperty = getBeanPropertySet(getBeanClass()).property(propertyName);
		if (!TypeUtils.isAssignable(value.getClass(), beanProperty.getType())) {
			throw new TypeMismatchException(
					"Value type [" + value.getClass().getName() + "] doesn't match the bean property [" + propertyName
							+ "] type [" + beanProperty.getType().getName() + "]");
		}
		return set(beanProperty, value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanBulkUpdateOperation#setNull(java.lang.String)
	 */
	@Override
	public BeanBulkUpdate<B> setNull(String propertyName) {
		ObjectUtils.argumentNotNull(propertyName, "Property name must be not null");
		return setNull(getBeanPropertySet(getBeanClass()).property(propertyName));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanBulkUpdateOperation#set(com.holonplatform.core.Path,
	 * com.holonplatform.core.TypedExpression)
	 */
	@Override
	public <T> BeanBulkUpdate<B> set(Path<T> path, TypedExpression<? super T> expression) {
		getExecutor().set(path, expression);
		return getBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.DatastoreOperation#getConfiguration()
	 */
	@Override
	public BulkUpdateOperationConfiguration getConfiguration() {
		return getExecutor().getConfiguration();
	}

}
