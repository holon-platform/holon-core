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

/**
 * An expression which represents a constant value and supports value conversion using an
 * {@link ExpressionValueConverter}.
 * 
 * @param <T> Expression type
 * @param <E> Constant value type
 *
 * @since 5.1.0
 */
public interface ConstantConverterExpression<T, E> extends ConverterExpression<E> {

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

}
