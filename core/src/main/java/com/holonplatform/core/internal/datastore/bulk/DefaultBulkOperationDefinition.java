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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.holonplatform.core.Expression;
import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ResolutionContext;
import com.holonplatform.core.ExpressionResolverRegistry;
import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.Datastore.OperationType;
import com.holonplatform.core.datastore.Datastore.WriteOption;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.ConstantExpression;
import com.holonplatform.core.query.NullExpression;
import com.holonplatform.core.query.QueryFilter;

/**
 * Default {@link BulkOperationDefinition} implementation.
 *
 * @since 5.1.0
 */
public class DefaultBulkOperationDefinition implements BulkOperationDefinition {

	/*
	 * Operation type
	 */
	private final OperationType operationType;

	/*
	 * Data target
	 */
	private DataTarget<?> target;

	/*
	 * Filter
	 */
	private QueryFilter filter;

	/*
	 * Values
	 */
	private final List<Map<Path<?>, TypedExpression<?>>> values = new LinkedList<>();

	/*
	 * Property set
	 */
	private PropertySet<?> propertySet;

	/*
	 * Write options
	 */
	private final Set<WriteOption> writeOptions = new HashSet<>(4);

	/*
	 * Expression resolvers
	 */
	protected ExpressionResolverRegistry expressionResolverRegistry = ExpressionResolverRegistry.create();

	/**
	 * Constructor
	 * @param operationType Operation type (not null)
	 */
	public DefaultBulkOperationDefinition(OperationType operationType) {
		super();
		ObjectUtils.argumentNotNull(operationType, "OperationType must be not null");
		this.operationType = operationType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperationConfiguration#getOperationType()
	 */
	@Override
	public OperationType getOperationType() {
		return operationType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperationConfiguration#getTarget()
	 */
	@Override
	public Optional<DataTarget<?>> getTarget() {
		return Optional.ofNullable(target);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperationConfiguration#getFilter()
	 */
	@Override
	public Optional<QueryFilter> getFilter() {
		return Optional.ofNullable(filter);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperationConfiguration#getValues()
	 */
	@Override
	public List<Map<Path<?>, TypedExpression<?>>> getValues() {
		return values;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperationConfiguration#getWriteOptions()
	 */
	@Override
	public Set<WriteOption> getWriteOptions() {
		return writeOptions;
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
		expressionResolverRegistry.addExpressionResolver(expressionResolver);
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
		expressionResolverRegistry.removeExpressionResolver(expressionResolver);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ExpressionResolver.ExpressionResolverHandler#getExpressionResolvers()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Iterable<ExpressionResolver> getExpressionResolvers() {
		return expressionResolverRegistry.getExpressionResolvers();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverHandler#resolve(com.holonplatform.core.Expression,
	 * java.lang.Class, com.holonplatform.core.ExpressionResolver.ResolutionContext)
	 */
	@Override
	public <E extends Expression, R extends Expression> Optional<R> resolve(E expression, Class<R> resolutionType,
			ResolutionContext context) throws InvalidExpressionException {
		return expressionResolverRegistry.resolve(expression, resolutionType, context);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.bulk.BulkOperationDefinition#setTarget(com.holonplatform.core.datastore
	 * .DataTarget)
	 */
	@Override
	public <T> void setTarget(DataTarget<T> target) {
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.bulk.BulkOperationDefinition#addFilter(com.holonplatform.core.query.
	 * QueryFilter)
	 */
	@Override
	public void addFilter(QueryFilter filter) {
		ObjectUtils.argumentNotNull(filter, "QueryFilter must be not null");
		if (this.filter == null) {
			this.filter = filter;
		} else {
			this.filter = this.filter.and(filter);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.bulk.BulkOperationDefinition#setPropertySet(com.holonplatform.core.
	 * property.PropertySet)
	 */
	@Override
	public void setPropertySet(PropertySet<?> propertySet) {
		this.propertySet = propertySet;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.bulk.BulkOperationDefinition#addValue(java.util.Map)
	 */
	@Override
	public void addValue(Map<Path<?>, TypedExpression<?>> value) {
		ObjectUtils.argumentNotNull(value, "Value must be not null");
		this.values.add(value);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.bulk.BulkOperationDefinition#addValue(com.holonplatform.core.property.
	 * PropertyBox, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void addValue(PropertyBox value, boolean includeNullValues) {
		ObjectUtils.argumentNotNull(value, "PropertyBox must be not null");

		final Map<Path<?>, TypedExpression<?>> values = new HashMap<>(value.size());

		getPropertySet().orElse(value).stream().filter(p -> Path.class.isAssignableFrom(p.getClass()))
				.collect(Collectors.toList()).forEach(p -> {
					final Path<?> path = (Path<?>) p;
					// check property present
					if (!value.contains(p)) {
						values.put(path, NullExpression.NULL);
					} else {
						// check null value
						final Object v = value.getValue(p);
						if (v != null) {
							values.put((Path<?>) p, (value instanceof TypedExpression) ? (TypedExpression<?>) v
									: ConstantExpression.create(v));
						} else if (includeNullValues) {
							values.put(path, NullExpression.NULL);
						}
					}
				});

		addValue(values);
	}

	/**
	 * Get the optional {@link PropertySet} to use to extract operation path-value bindings from a {@link PropertyBox}.
	 * @return Optional property set
	 */
	protected Optional<PropertySet<?>> getPropertySet() {
		return Optional.ofNullable(propertySet);
	}

}
