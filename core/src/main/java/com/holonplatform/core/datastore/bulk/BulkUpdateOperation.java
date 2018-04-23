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
package com.holonplatform.core.datastore.bulk;

import com.holonplatform.core.NullExpression;
import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.operation.DatastoreOperation;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.query.ConstantExpression;
import com.holonplatform.core.query.QueryFilter.QueryFilterSupport;

/**
 * Bulk <code>UPDATE</code> {@link DatastoreOperation} configuration.
 * 
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public interface BulkUpdateOperation<O extends BulkUpdateOperation<O>>
		extends DatastoreOperation<O, BulkUpdateConfiguration>, QueryFilterSupport<O> {

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
	 * Set update operation values using given {@link PropertyBox}.
	 * <p>
	 * Each {@link Path} type property of the PropertyBox property set will be setted to its corresponding value in the
	 * PropertyBox. Any previously path binding will be replaced by a new path binding for matching paths.
	 * </p>
	 * @param propertyBox PropertyBox to use (not null)
	 * @param includeNullValues <code>true</code> to update to <code>null</code> any path without a value in the
	 *        PropertyBox, <code>false</code> to ignore them
	 * @return this
	 */
	O set(PropertyBox propertyBox, boolean includeNullValues);

	/**
	 * Set update operation values using given {@link PropertyBox}.
	 * <p>
	 * Each {@link Path} type property of the PropertyBox property set will be setted to its corresponding value in the
	 * PropertyBox. Any previously path binding will be replaced by a new path binding for matching paths.
	 * </p>
	 * <p>
	 * Any path without a value in the PropertyBox will be updated to the <code>null</code> value.
	 * </p>
	 * @param propertyBox propertyBox PropertyBox to use (not null)
	 * @return this
	 * @see #set(PropertyBox, boolean)
	 */
	default O set(PropertyBox propertyBox) {
		return set(propertyBox, true);
	}

	/**
	 * Set given <code>path</code> to given constant value.
	 * @param <T> Path type
	 * @param path Path to be updated (not null)
	 * @param value value to set
	 * @return this
	 * @see #setNull(Path)
	 */
	default <T> O set(Path<T> path, T value) {
		return set(path, ConstantExpression.create(path, value));
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
