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

import java.util.Set;

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.datastore.operation.commons.DatastoreOperation;
import com.holonplatform.core.datastore.operation.commons.DatastoreOperationConfiguration;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Abstract {@link DatastoreOperation}.
 * 
 * @param <O> Actual operation type
 * @param <C> Actual operation configuration type
 * @param <D> Actual operation definition type
 *
 * @since 5.1.0
 */
public abstract class AbstractDatastoreOperation<O extends DatastoreOperation<O, C>, C extends DatastoreOperationConfiguration, D extends DatastoreOperationDefinition>
		implements DatastoreOperation<O, C> {

	/**
	 * Operation definition
	 */
	private final D definition;

	/**
	 * Constructor.
	 * @param definition Operation definition (not null)
	 */
	public AbstractDatastoreOperation(D definition) {
		super();
		ObjectUtils.argumentNotNull(definition, "Operation definition must be not null");
		this.definition = definition;
	}

	/**
	 * Get the actual operation object.
	 * @return actual operation object
	 */
	protected abstract O getActualOperation();

	/**
	 * Get the operation definition.
	 * @return the operation definition
	 */
	protected D getDefinition() {
		return definition;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder#withExpressionResolver(com.holonplatform.core
	 * .ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> O withExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		getDefinition().addExpressionResolver(expressionResolver);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.DatastoreOperation#withExpressionResolvers(java.lang.Iterable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public O withExpressionResolvers(Iterable<? extends ExpressionResolver> expressionResolvers) {
		ObjectUtils.argumentNotNull(expressionResolvers, "Expression resolvers must be not null");
		expressionResolvers.forEach(er -> getDefinition().addExpressionResolver(er));
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.datastore.operation.DatastoreOperation#target(com.holonplatform.core.datastore.DataTarget)
	 */
	@Override
	public O target(DataTarget<?> target) {
		getDefinition().setTarget(target);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#parameter(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public O parameter(String name, Object value) {
		getDefinition().addParameter(name, value);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#parameter(com.holonplatform.
	 * core.config.ConfigProperty, java.lang.Object)
	 */
	@Override
	public <T> O parameter(ConfigProperty<T> property, T value) {
		ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
		getDefinition().addParameter(property.getKey(), value);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.datastore.operation.DatastoreOperation#withWriteOption(com.holonplatform.core.datastore.
	 * DatastoreOperations.WriteOption)
	 */
	@Override
	public O withWriteOption(WriteOption writeOption) {
		getDefinition().addWriteOption(writeOption);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.datastore.operation.DatastoreOperation#withWriteOptions(com.holonplatform.core.datastore.
	 * DatastoreOperations.WriteOption[])
	 */
	@Override
	public O withWriteOptions(WriteOption... writeOptions) {
		ObjectUtils.argumentNotNull(writeOptions, "Write options must be not null");
		for (WriteOption writeOption : writeOptions) {
			getDefinition().addWriteOption(writeOption);
		}
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.DatastoreOperation#withWriteOptions(java.util.Set)
	 */
	@Override
	public O withWriteOptions(Set<WriteOption> writeOptions) {
		ObjectUtils.argumentNotNull(writeOptions, "Write options must be not null");
		writeOptions.forEach(wo -> getDefinition().addWriteOption(wo));
		return getActualOperation();
	}

}
