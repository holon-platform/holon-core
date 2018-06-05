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
package com.holonplatform.core.datastore.beans;

import com.holonplatform.core.ConstantConverterExpression;
import com.holonplatform.core.NullExpression;
import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.operation.commons.BulkUpdateOperationConfiguration;
import com.holonplatform.core.datastore.operation.commons.DatastoreOperation;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.property.Property.PropertyNotFoundException;
import com.holonplatform.core.query.QueryFilter.QueryFilterSupport;

/**
 * Bulk <code>UPDATE</code> bean {@link DatastoreOperation} configuration.
 * 
 * @param <B> Bean type
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public interface BeanBulkUpdateOperation<B, O extends BeanBulkUpdateOperation<B, O>>
		extends BeanDatastoreOperation<O, BulkUpdateOperationConfiguration>, QueryFilterSupport<O> {

	/**
	 * Set the bean property with given <code>propertyName</code> to given value.
	 * <p>
	 * The bean property name must be present in bean property set and the value type must be compatible with property
	 * type.
	 * </p>
	 * @param propertyName Bean property name (not null)
	 * @param value The value to set
	 * @return this
	 * @throws TypeMismatchException If given value is not compatible with property value
	 * @throws PropertyNotFoundException If given property name is not part of the bean property set
	 */
	O set(String propertyName, Object value);

	/**
	 * Set the bean property with given <code>propertyName</code> to <code>null</code> value.
	 * <p>
	 * The bean property name must be present in bean property set.
	 * </p>
	 * @param propertyName Bean property name (not null)
	 * @return this
	 * @throws PropertyNotFoundException If given property name is not part of the bean property set
	 */
	O setNull(String propertyName);

	/**
	 * Set given <code>path</code> to given <code>expression</code> value.
	 * @param <T> Path type
	 * @param path Path to be updated (not null)
	 * @param expression Expression value to set (not null)
	 * @return this
	 * @see #setNull(Path)
	 */
	<T> O set(Path<T> path, TypedExpression<? super T> expression);

	/**
	 * Set given <code>path</code> to given constant value.
	 * @param <T> Path type
	 * @param path Path to be updated (not null)
	 * @param value value to set
	 * @return this
	 * @see #setNull(Path)
	 */
	default <T> O set(Path<T> path, T value) {
		return set(path, ConstantConverterExpression.create(path, value));
	}

	/**
	 * Set the given {@link Path} to <code>null</code> value.
	 * @param <P> Path type
	 * @param path Path to be updated (not null)
	 * @return this
	 */
	default <P> O setNull(Path<P> path) {
		return set(path, NullExpression.create(path));
	}

}
