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

import com.holonplatform.core.ConverterExpression;
import com.holonplatform.core.ExpressionValueConverter;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.query.DefaultConstantExpression;

/**
 * Constant value {@link QueryExpression}, with {@link ExpressionValueConverter} support.
 * 
 * @param <T> Expression type
 * @param <E> Concrete value type
 * 
 * @since 5.0.0
 * 
 * @see CollectionExpression
 */
public interface ConstantExpression<T, E> extends QueryExpression<E>, ConverterExpression<E> {

	/**
	 * Get the constant expression value
	 * @return The expression value
	 */
	T getValue();

	/**
	 * Get the model data type value, using the {@link ExpressionValueConverter} to convert constant expression value,
	 * if available. If an {@link ExpressionValueConverter} is not available, the original expression value is returned.
	 * @return The model-converted expression value if an {@link ExpressionValueConverter} is available, otherwise
	 *         {@link #getValue()} is returned.
	 */
	Object getModelValue();

	// builders

	/**
	 * Create a {@link ConstantExpression} which represents a constant value.
	 * @param <T> Expression type
	 * @param value Constant value
	 * @return A new constant expression
	 */
	static <T> ConstantExpression<T, T> create(T value) {
		return new DefaultConstantExpression<>(value);
	}

	/**
	 * Create a {@link ConstantExpression} which represents a constant value, using given <code>expression</code> to
	 * inherit an {@link ExpressionValueConverter}, if available.
	 * @param <T> Expression type
	 * @param expression Expression form which to inherit an {@link ExpressionValueConverter}, if available
	 * @param value Constant value
	 * @return A new constant expression
	 */
	static <T> ConstantExpression<T, T> create(TypedExpression<T> expression, T value) {
		return new DefaultConstantExpression<>(expression, value);
	}

}
