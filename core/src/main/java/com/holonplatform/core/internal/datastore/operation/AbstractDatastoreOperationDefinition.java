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
package com.holonplatform.core.internal.datastore.operation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.NullExpression;
import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PathPropertyBoxAdapter;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.query.ConstantExpression;

/**
 * Base {@link DatastoreOperationDefinition} implementation.
 *
 * @since 5.1.0
 */
public abstract class AbstractDatastoreOperationDefinition implements DatastoreOperationDefinition {

	/*
	 * Data target
	 */
	private DataTarget<?> target;

	/*
	 * Write options
	 */
	private final Set<WriteOption> writeOptions = new HashSet<>(4);

	/*
	 * Expression resolvers
	 */
	@SuppressWarnings("rawtypes")
	protected Set<ExpressionResolver> expressionResolvers = null;

	/**
	 * Constructor.
	 */
	public AbstractDatastoreOperationDefinition() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration#getTarget()
	 */
	@Override
	public DataTarget<?> getTarget() {
		return target;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration#getWriteOptions()
	 */
	@Override
	public Set<WriteOption> getWriteOptions() {
		return writeOptions;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.operation.DatastoreOperationDefinition#setTarget(com.holonplatform.core
	 * .datastore.DataTarget)
	 */
	@Override
	public <T> void setTarget(DataTarget<T> target) {
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.operation.DatastoreOperationDefinition#addWriteOption(com.holonplatform
	 * .core.datastore.DatastoreOperations.WriteOption)
	 */
	@Override
	public void addWriteOption(WriteOption writeOption) {
		ObjectUtils.argumentNotNull(writeOption, "WriteOption must be not null");
		writeOptions.add(writeOption);
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
		ObjectUtils.argumentNotNull(expressionResolver, "Expression resolver must be not null");
		if (expressionResolvers == null) {
			expressionResolvers = new HashSet<>(8);
		}
		expressionResolvers.add(expressionResolver);
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
		ObjectUtils.argumentNotNull(expressionResolver, "Expression resolver must be not null");
		if (expressionResolvers != null) {
			expressionResolvers.remove(expressionResolver);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ExpressionResolver.ExpressionResolverHandler#getExpressionResolvers()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Iterable<ExpressionResolver> getExpressionResolvers() {
		return (expressionResolvers == null) ? Collections.emptySet()
				: Collections.unmodifiableSet(expressionResolvers);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getTarget() == null) {
			throw new InvalidExpressionException("Missing operation data target");
		}
	}

	/**
	 * Get given {@link PropertyBox} as a map of {@link Path} and {@link TypedExpression} values.
	 * @param value The property box value (not null)
	 * @param includeNullValues Whether to set <code>null</code> property box values to the <code>null</code> value
	 * @return Values map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map<Path<?>, TypedExpression<?>> asPathValues(PropertyBox value, boolean includeNullValues) {
		ObjectUtils.argumentNotNull(value, "PropertyBox must be not null");

		final Map<Path<?>, TypedExpression<?>> values = new HashMap<>(value.size());

		final PathPropertyBoxAdapter propertyBoxAdapter = PathPropertyBoxAdapter.create(value);

		propertyBoxAdapter.paths().forEach(path -> {
			propertyBoxAdapter.getValueOrElse(path, pathWithNoValue -> {
				if (includeNullValues) {
					values.put(path, NullExpression.create(path));
				}
			}).ifPresent(val -> {
				values.put(path, (val instanceof TypedExpression) ? (TypedExpression<?>) val
						: ConstantExpression.create((Path) path, val));
			});
		});

		return values;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DatastoreOperationConfiguration [target=" + target + ", writeOptions=" + writeOptions
				+ ", expressionResolvers=" + expressionResolvers + "]";
	}

	/**
	 * Base {@link DatastoreOperationConfiguration} builder.
	 *
	 * @param <B> Concrete builder type
	 * @param <I> Concrete instance type
	 */
	public static abstract class DatastoreOperationConfigurationBuilder<B extends DatastoreOperationConfiguration.Builder<B>, I extends AbstractDatastoreOperationDefinition>
			implements DatastoreOperationConfiguration.Builder<B> {

		private final I instance;

		public DatastoreOperationConfigurationBuilder(I instance) {
			super();
			this.instance = instance;
		}

		protected abstract B getActualBuilder();

		protected I getInstance() {
			return instance;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder#withExpressionResolver(com.holonplatform.
		 * core.ExpressionResolver)
		 */
		@Override
		public <E extends Expression, R extends Expression> B withExpressionResolver(
				ExpressionResolver<E, R> expressionResolver) {
			getInstance().addExpressionResolver(expressionResolver);
			return getActualBuilder();
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#target(com.holonplatform.
		 * core.datastore.DataTarget)
		 */
		@Override
		public B target(DataTarget<?> target) {
			getInstance().setTarget(target);
			return getActualBuilder();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#withWriteOption(com.
		 * holonplatform.core.datastore.DatastoreOperations.WriteOption)
		 */
		@Override
		public B withWriteOption(WriteOption writeOption) {
			getInstance().addWriteOption(writeOption);
			return getActualBuilder();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#withWriteOptions(com.
		 * holonplatform.core.datastore.DatastoreOperations.WriteOption[])
		 */
		@Override
		public B withWriteOptions(WriteOption... writeOptions) {
			if (writeOptions != null) {
				for (WriteOption writeOption : writeOptions) {
					getInstance().addWriteOption(writeOption);
				}
			}
			return getActualBuilder();
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#withWriteOptions(java.util
		 * .Set)
		 */
		@Override
		public B withWriteOptions(Set<WriteOption> writeOptions) {
			if (writeOptions != null) {
				for (WriteOption writeOption : writeOptions) {
					getInstance().addWriteOption(writeOption);
				}
			}
			return getActualBuilder();
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.datastore.operation.DatastoreOperationConfiguration.Builder#withExpressionResolvers(
		 * java.lang.Iterable)
		 */
		@SuppressWarnings("rawtypes")
		@Override
		public B withExpressionResolvers(Iterable<? extends ExpressionResolver> expressionResolvers) {
			getInstance().addExpressionResolvers(expressionResolvers);
			return getActualBuilder();
		}

	}

}
