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

import java.util.Collection;
import java.util.Optional;

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.DatastoreCommodity;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.datastore.DefaultWriteOption;
import com.holonplatform.core.datastore.beans.BeanBulkDelete;
import com.holonplatform.core.datastore.beans.BeanBulkInsert;
import com.holonplatform.core.datastore.beans.BeanBulkUpdate;
import com.holonplatform.core.datastore.beans.BeanDatastore;
import com.holonplatform.core.datastore.beans.BeanQuery;
import com.holonplatform.core.datastore.transaction.Transactional;
import com.holonplatform.core.property.PropertyBox;

/**
 * Default {@link BeanDatastore} implementation.
 *
 * @since 5.1.0
 */
public class DefaultBeanDatastore extends AbstractBeanDatastoreAdapter<Datastore> implements BeanDatastore {

	/**
	 * Constructor.
	 * @param datastore The concrete Datastore (not null)
	 */
	public DefaultBeanDatastore(Datastore datastore) {
		super(datastore);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport#addExpressionResolver(com.holonplatform.core.
	 * ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> void addExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		getExecutor().addExpressionResolver(expressionResolver);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport#removeExpressionResolver(com.holonplatform.
	 * core.ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> void removeExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		getExecutor().removeExpressionResolver(expressionResolver);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreCommodityHandler#getAvailableCommodities()
	 */
	@Override
	public Collection<Class<? extends DatastoreCommodity>> getAvailableCommodities() {
		return getExecutor().getAvailableCommodities();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreCommodityHandler#create(java.lang.Class)
	 */
	@Override
	public <C extends DatastoreCommodity> C create(Class<C> commodityType) {
		return getExecutor().create(commodityType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DataContextBound#getDataContextId()
	 */
	@Override
	public Optional<String> getDataContextId() {
		return getExecutor().getDataContextId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore#isTransactional()
	 */
	@Override
	public Optional<Transactional> isTransactional() {
		return getExecutor().isTransactional();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore#requireTransactional()
	 */
	@Override
	public Transactional requireTransactional() {
		return getExecutor().requireTransactional();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastoreOperations#refresh(java.lang.Object)
	 */
	@Override
	public <T> T refresh(T bean) {
		return asBean(getBeanClass(bean), getExecutor().refresh(getDataTarget(bean), asPropertyBox(bean)));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore#insert(java.lang.Object,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	public <T> BeanOperationResult<T> insert(T bean, WriteOption... options) {
		final PropertyBox propertyBox = asPropertyBox(bean);
		return convert(
				getExecutor().insert(getDataTarget(bean), propertyBox,
						processWriteOptions(options, DefaultWriteOption.BRING_BACK_GENERATED_IDS)),
				asBean(getBeanClass(bean), propertyBox));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore#update(java.lang.Object,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	public <T> BeanOperationResult<T> update(T bean, WriteOption... options) {
		final PropertyBox propertyBox = asPropertyBox(bean);
		return convert(
				getExecutor().insert(getDataTarget(bean), propertyBox,
						processWriteOptions(options, DefaultWriteOption.BRING_BACK_GENERATED_IDS)),
				asBean(getBeanClass(bean), propertyBox));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore#save(java.lang.Object,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	public <T> BeanOperationResult<T> save(T bean, WriteOption... options) {
		final PropertyBox propertyBox = asPropertyBox(bean);
		return convert(
				getExecutor().insert(getDataTarget(bean), propertyBox,
						processWriteOptions(options, DefaultWriteOption.BRING_BACK_GENERATED_IDS)),
				asBean(getBeanClass(bean), propertyBox));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore#delete(java.lang.Object,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	public <T> BeanOperationResult<T> delete(T bean, WriteOption... options) {
		return convert(getExecutor().insert(getDataTarget(bean), asPropertyBox(bean), options), null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore#bulkInsert(java.lang.Class,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	public <T> BeanBulkInsert<T> bulkInsert(Class<T> beanClass, WriteOption... options) {
		return new DefaultBeanBulkInsert<>(beanClass,
				getExecutor().bulkInsert(getDataTarget(beanClass), getBeanPropertySet(beanClass), options));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore#bulkUpdate(java.lang.Class,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	public <T> BeanBulkUpdate<T> bulkUpdate(Class<T> beanClass, WriteOption... options) {
		return new DefaultBeanBulkUpdate<>(beanClass, getExecutor().bulkUpdate(getDataTarget(beanClass), options));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore#bulkDelete(java.lang.Class,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	public <T> BeanBulkDelete<T> bulkDelete(Class<T> beanClass, WriteOption... options) {
		return new DefaultBeanBulkDelete<>(beanClass, getExecutor().bulkDelete(getDataTarget(beanClass), options));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore#query(java.lang.Class)
	 */
	@Override
	public <T> BeanQuery<T> query(Class<T> beanClass) {
		return new DefaultBeanQuery<>(beanClass, getExecutor().query());
	}

}
