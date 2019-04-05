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

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.holonplatform.core.Path;
import com.holonplatform.core.datastore.operation.commons.BulkInsertOperation;
import com.holonplatform.core.datastore.operation.commons.BulkInsertOperationConfiguration;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PathPropertySetAdapter;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;

/**
 * Abstract {@link BulkInsertOperation} implementation.
 * 
 * @param <R> Operation result type
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public abstract class AbstractBulkInsertOperation<R, O extends BulkInsertOperation<R, O>>
		extends AbstractDatastoreOperation<O, BulkInsertOperationConfiguration, BulkInsertDefinition>
		implements BulkInsertOperation<R, O> {

	private static final long serialVersionUID = 7915272708400742596L;

	public AbstractBulkInsertOperation() {
		super(new DefaultBulkInsertDefinition());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkOperation#getConfiguration()
	 */
	@Override
	public BulkInsertOperationConfiguration getConfiguration() {
		return getDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.commons.BulkInsertOperation#propertySet(java.lang.Iterable)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <P extends Property> O propertySet(Iterable<P> properties) {
		ObjectUtils.argumentNotNull(properties, "Properties must be not null");
		getDefinition().setPropertySet(
				(properties instanceof PropertySet) ? (PropertySet) properties : PropertySet.of(properties));
		return getActualOperation();
	}

	@Override
	public O add(Iterable<PropertyBox> values) {
		ObjectUtils.argumentNotNull(values, "Values to add must be not null");
		for (PropertyBox value : values) {
			if (value != null) {
				getDefinition().addValue(value);
			}
		}
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkInsertOperation#add(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public O add(Map<Path<?>, Object> values) {
		if (values != null && !values.isEmpty()) {
			final PropertySet<?> propertySet = getDefinition().getPropertySet().orElse(asPropertySet(values.keySet()));
			final PropertyBox propertyBox = PropertyBox.builder(propertySet).invalidAllowed(true).build();
			final PathPropertySetAdapter adapter = PathPropertySetAdapter.create(propertySet);
			for (Entry<Path<?>, Object> entry : values.entrySet()) {
				adapter.getProperty(entry.getKey()).ifPresent(p -> {
					if (entry.getValue() != null) {
						propertyBox.setValue((Property<Object>) p, entry.getValue());
					}
				});
			}
			getDefinition().addValue(propertyBox);
			return getActualOperation();
		}
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkInsertOperation#operationPaths(com.holonplatform.core.property.
	 * PropertySet)
	 */
	@Override
	@Deprecated
	public O operationPaths(PropertySet<?> propertySet) {
		getDefinition().setPropertySet(propertySet);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkInsertOperation#operationPaths(com.holonplatform.core.Path[])
	 */
	@Override
	@Deprecated
	public O operationPaths(Path<?>[] paths) {
		ObjectUtils.argumentNotNull(paths, "Paths must be not null");
		getDefinition().setPropertySet(asPropertySet(Arrays.asList(paths)));
		return getActualOperation();
	}

	/**
	 * Convert given {@link Path} set to a {@link PropertySet}, including only {@link Property} type paths.
	 * @param paths Path collection
	 * @return A {@link PropertySet} which includes the {@link Property} type paths, in the given order
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static PropertySet<?> asPropertySet(Iterable<Path<?>> paths) {
		final PropertySet.Builder builder = PropertySet.builder();
		for (Path<?> path : paths) {
			if (path != null && Property.class.isAssignableFrom(path.getClass())) {
				builder.add((Property) path);
			}
		}
		return builder.build();
	}

}
