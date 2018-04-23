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
package com.holonplatform.core;

import com.holonplatform.core.internal.DefaultNullExpression;

/**
 * A {@link TypedExpression} which represents a <code>null</code> value, with expression value conversion support.
 * 
 * @param <T> Expression type
 * 
 * @since 5.1.0
 */
public interface NullExpression<T> extends ConverterExpression<T> {

	/**
	 * Get the model data type value, using the {@link ExpressionValueConverter} to convert constant expression value,
	 * if available. If an {@link ExpressionValueConverter} is not available, <code>null</code> is returned.
	 * @return The model-converted expression value if an {@link ExpressionValueConverter} is available, or
	 *         <code>null</code> otherwise
	 */
	Object getModelValue();

	/**
	 * Create a new {@link NullExpression} instance.
	 * @param <T> Expression type
	 * @param type Expression type (not null)
	 * @return A new {@link NullExpression}
	 */
	static <T> NullExpression<T> create(Class<? extends T> type) {
		return new DefaultNullExpression<>(type);
	}

	/**
	 * Create a new {@link NullExpression} instance, using given <code>expression</code> to inherit an
	 * {@link ExpressionValueConverter}, if available.
	 * @param <T> Expression type
	 * @param expression Expression form which to inherit an {@link ExpressionValueConverter}, if available
	 * @return A new {@link NullExpression}
	 */
	static <T> NullExpression<T> create(TypedExpression<T> expression) {
		return new DefaultNullExpression<>(expression);
	}

}
