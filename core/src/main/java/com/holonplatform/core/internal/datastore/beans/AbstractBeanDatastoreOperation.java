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

import java.util.Set;

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.datastore.beans.BeanDatastore.BeanOperationResult;
import com.holonplatform.core.datastore.operation.commons.DatastoreOperationConfiguration;
import com.holonplatform.core.datastore.operation.commons.ExecutableOperation;
import com.holonplatform.core.datastore.beans.BeanDatastoreOperationBuilder;

/**
 * Abstract bean datastore operation class.
 * 
 * @param <T> Bean type
 * @param <E> Executor type
 * @param <B> Concrete builder type
 * 
 * @since 5.1.0
 */
public abstract class AbstractBeanDatastoreOperation<T, E extends DatastoreOperationConfiguration.Builder<E> & ExecutableOperation<OperationResult>, B extends BeanDatastoreOperationBuilder<B>>
		extends AbstractBeanDatastoreAdapter<E>
		implements BeanDatastoreOperationBuilder<B>, ExecutableOperation<BeanOperationResult<T>> {

	private final Class<? extends T> beanClass;

	/**
	 * Constructor.
	 * @param beanClass Bean class (not null)
	 * @param executor Concrete executor (not null)
	 */
	public AbstractBeanDatastoreOperation(Class<? extends T> beanClass, E executor) {
		super(executor);
		this.beanClass = beanClass;
	}

	/**
	 * Get the bean class.
	 * @return the bean class
	 */
	protected Class<? extends T> getBeanClass() {
		return beanClass;
	}

	/**
	 * Get the concrete operation builder.
	 * @return the concrete operation builder
	 */
	protected abstract B getBuilder();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#withWriteOption(com.
	 * holonplatform.core.datastore.DatastoreOperations.WriteOption)
	 */
	@Override
	public B withWriteOption(WriteOption writeOption) {
		getExecutor().withWriteOption(writeOption);
		return getBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#withWriteOptions(com.
	 * holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	public B withWriteOptions(WriteOption... writeOptions) {
		getExecutor().withWriteOptions(writeOptions);
		return getBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#withWriteOptions(java.util.
	 * Set)
	 */
	@Override
	public B withWriteOptions(Set<WriteOption> writeOptions) {
		getExecutor().withWriteOptions(writeOptions);
		return getBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#withExpressionResolvers(java.
	 * lang.Iterable)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public B withExpressionResolvers(Iterable<? extends ExpressionResolver> expressionResolvers) {
		getExecutor().withExpressionResolvers(expressionResolvers);
		return getBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder#withExpressionResolver(com.holonplatform.core
	 * .ExpressionResolver)
	 */
	@Override
	public <X extends Expression, R extends Expression> B withExpressionResolver(
			ExpressionResolver<X, R> expressionResolver) {
		getExecutor().withExpressionResolver(expressionResolver);
		return getBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.ExecutableOperation#execute()
	 */
	@Override
	public BeanOperationResult<T> execute() {
		return convert(getExecutor().execute(), null);
	}

}
