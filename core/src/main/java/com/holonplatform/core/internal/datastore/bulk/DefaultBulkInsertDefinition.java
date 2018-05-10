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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.datastore.operation.AbstractDatastoreOperationDefinition;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PathPropertyBoxAdapter;
import com.holonplatform.core.property.PathPropertySetAdapter;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.ConstantExpression;

/**
 * Default {@link BulkInsertDefinition}.
 *
 * @since 5.1.0
 */
public class DefaultBulkInsertDefinition extends AbstractDatastoreOperationDefinition implements BulkInsertDefinition {

	/*
	 * Operation values
	 */
	private final List<Map<Path<?>, ConstantExpression<?>>> values = new LinkedList<>();

	/*
	 * Operation paths
	 */
	private Path<?>[] operationPaths;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkInsertConfiguration#getValues()
	 */
	@Override
	public List<Map<Path<?>, ConstantExpression<?>>> getValues() {
		return Collections.unmodifiableList(values);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkInsertConfiguration#getOperationPaths()
	 */
	@Override
	public Optional<Path<?>[]> getOperationPaths() {
		return Optional.ofNullable(operationPaths);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.bulk.BulkInsertDefinition#addValue(java.util.Map)
	 */
	@Override
	public void addValue(Map<Path<?>, ConstantExpression<?>> value) {
		ObjectUtils.argumentNotNull(value, "Value must be not null");
		values.add(value);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.bulk.BulkInsertDefinition#addValue(com.holonplatform.core.property.
	 * PropertyBox, boolean)
	 */
	@Override
	public void addValue(PropertyBox value) {
		values.add(asPathConstantValues(value));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.bulk.BulkInsertDefinition#setOperationPaths(com.holonplatform.core.Path
	 * [])
	 */
	@Override
	public void setOperationPaths(Path<?>[] paths) {
		this.operationPaths = paths;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.bulk.BulkInsertDefinition#setOperationPaths(com.holonplatform.core.
	 * property.PropertySet)
	 */
	@Override
	public void setOperationPaths(PropertySet<?> propertySet) {
		ObjectUtils.argumentNotNull(propertySet, "Operation path property set must be not nulll");
		setOperationPaths(PathPropertySetAdapter.create(propertySet).paths().collect(Collectors.toList())
				.toArray(new Path<?>[0]));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.bulk.AbstractBulkOperationDefinition#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		super.validate();
		if (getValues().isEmpty()) {
			throw new InvalidExpressionException("No values to insert");
		}
	}

	/**
	 * Get given {@link PropertyBox} as a map of {@link Path} and {@link ConstantExpression} values.
	 * @param value The property box value (not null)
	 * @return Values map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map<Path<?>, ConstantExpression<?>> asPathConstantValues(PropertyBox value) {
		ObjectUtils.argumentNotNull(value, "PropertyBox must be not null");

		final Map<Path<?>, ConstantExpression<?>> values = new HashMap<>(value.size());

		final PathPropertyBoxAdapter propertyBoxAdapter = PathPropertyBoxAdapter.create(value);

		propertyBoxAdapter.paths().forEach(path -> {
			propertyBoxAdapter.getValue(path).ifPresent(val -> {
				values.put(path, ConstantExpression.create((Path) path, val));
			});
		});

		return values;
	}

}
