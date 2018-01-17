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

import java.util.Optional;

import com.holonplatform.core.TypedExpression;

/**
 * A {@link TypedExpression} which supports an optional {@link ExpressionValueConverter} to perform expression type
 * conversion.
 * 
 * @param <T> Expression type
 *
 * @since 5.1.0
 */
public interface ConverterExpression<T> extends TypedExpression<T> {

	/**
	 * Get the expression value type converter, if available.
	 * @return Optional {@link ExpressionValueConverter}
	 */
	Optional<ExpressionValueConverter<T, ?>> getExpressionValueConverter();

	/**
	 * Get the model expression type.
	 * @return The model expression type if an {@link ExpressionValueConverter} is available, otherwise returns the
	 *         expression type
	 */
	@SuppressWarnings("rawtypes")
	default Class<?> getModelType() {
		return getExpressionValueConverter().map(converter -> (Class) converter.getModelType()).orElse(getType());
	}

}
