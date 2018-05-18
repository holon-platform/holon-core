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
package com.holonplatform.core.query;

import java.util.Collection;

import com.holonplatform.core.ExpressionValueConverter;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.query.DefaultCollectionConstantExpression;

/**
 * A {@link ConstantExpression} with a {@link Collection} value type.
 *
 * @param <T> Collection elements type
 *
 * @since 5.1.0
 */
public interface CollectionConstantExpression<T> extends ConstantConverterExpression<Collection<T>, T> {

	/**
	 * Get whether the {@link Collection} expression value is not <code>null</code> or empty.
	 * @return <code>true</code> if the expression value is <code>null</code> or empty, <code>false</code> otherwise
	 */
	default boolean hasValues() {
		return getValue() != null && !getValue().isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.ConstantConverterExpression#getModelValue()
	 */
	@Override
	Collection<?> getModelValue();

	/**
	 * Create a {@link ConstantExpression} which represents a collection of constant values.
	 * @param <T> Expression type
	 * @param values Expression values (not null)
	 * @return A new collection expression
	 */
	static <T> CollectionConstantExpression<T> create(Collection<? extends T> values) {
		return new DefaultCollectionConstantExpression<>(values);
	}

	/**
	 * Create a {@link ConstantExpression} which represents a collection of constant values.
	 * @param <T> Expression type
	 * @param values Expression values (not null)
	 * @return A new collection expression
	 */
	@SafeVarargs
	static <T> CollectionConstantExpression<T> create(T... values) {
		return new DefaultCollectionConstantExpression<>(values);
	}

	/**
	 * Create a {@link ConstantExpression} which represents a collection of constant values, using given
	 * <code>expression</code> to inherit an {@link ExpressionValueConverter}, if available.
	 * @param <T> Expression type
	 * @param expression Expression form which to inherit an {@link ExpressionValueConverter}, if available
	 * @param values Expression values (not null)
	 * @return A new collection expression
	 */
	static <T> CollectionConstantExpression<T> create(TypedExpression<T> expression, Collection<? extends T> values) {
		return new DefaultCollectionConstantExpression<>(expression, values);
	}

	/**
	 * Create a {@link ConstantExpression} which represents a collection of constant values, using given
	 * <code>expression</code> to inherit an {@link ExpressionValueConverter}, if available.
	 * @param <T> Expression type
	 * @param expression Expression form which to inherit an {@link ExpressionValueConverter}, if available
	 * @param values Expression values (not null)
	 * @return A new collection expression
	 */
	@SafeVarargs
	static <T> CollectionConstantExpression<T> create(TypedExpression<T> expression, T... values) {
		return new DefaultCollectionConstantExpression<>(expression, values);
	}

}
