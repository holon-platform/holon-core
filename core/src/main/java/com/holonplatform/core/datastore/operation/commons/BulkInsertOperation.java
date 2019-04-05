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
package com.holonplatform.core.datastore.operation.commons;

import java.util.Arrays;
import java.util.Map;

import com.holonplatform.core.Path;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;

/**
 * Bulk <code>INSERT</code> {@link ExecutableBulkOperation}.
 * 
 * @param <R> Operation result type
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public interface BulkInsertOperation<R, O extends BulkInsertOperation<R, O>>
		extends ExecutableBulkOperation<R, BulkInsertOperationConfiguration, O> {

	/**
	 * Set the operation property set, i.e. the properties which must be included in the bulk insert operation.
	 * @param <P> Property type
	 * @param properties Property set (not null)
	 * @return this
	 */
	@SuppressWarnings("rawtypes")
	<P extends Property> O propertySet(Iterable<P> properties);

	/**
	 * Set the operation property set, i.e. the properties which must be included in the bulk insert operation.
	 * @param <P> Property type
	 * @param properties Property set (not null)
	 * @return this
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	default <P extends Property> O propertySet(P... properties) {
		return propertySet(PropertySet.of(properties));
	}

	/**
	 * Add one or more {@link PropertyBox} values to insert.
	 * @param values The values to add to the bulk insert operation (not null)
	 * @return this
	 */
	O add(Iterable<PropertyBox> values);

	/**
	 * Add one or more {@link PropertyBox} values to insert.
	 * @param values The values to add to the bulk insert operation (not null)
	 * @return this
	 */
	default O add(PropertyBox... values) {
		return add(Arrays.asList(values));
	}

	/**
	 * Add a path - value map to insert.
	 * @param values Value map to add to the bulk insert operation (not null)
	 * @return this
	 * @deprecated Use {@link #add(Iterable)}
	 */
	@Deprecated
	O add(Map<Path<?>, Object> values);

	/**
	 * Set the paths to be used for operation values.
	 * @param paths Operation value paths
	 * @return this
	 * @deprecated Use {@link #propertySet(Iterable)}
	 */
	@Deprecated
	O operationPaths(Path<?>[] paths);

	/**
	 * Set the paths to be used for operation values using a {@link PropertySet}.
	 * <p>
	 * Each property of the property set which corresponds to a {@link Path} will be used as operation path.
	 * </p>
	 * @param propertySet The property set to set (not null)
	 * @return this
	 * @deprecated Use {@link #propertySet(Iterable)}
	 */
	@Deprecated
	O operationPaths(PropertySet<?> propertySet);

}
