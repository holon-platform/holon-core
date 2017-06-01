/*
 * Copyright 2000-2016 Holon TDCN.
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
package com.holonplatform.core.internal.query;

import com.holonplatform.core.query.ConstantExpression;
import com.holonplatform.core.query.QueryProjection;

/**
 * A {@link QueryProjection} using a {@link ConstantExpression} as selection.
 *
 * @param <T> Expression type
 *
 * @since 5.0.0
 */
public interface ConstantExpressionProjection<T> extends ConstantExpression<T, T>, QueryProjection<T> {

	/**
	 * Create a {@link ConstantExpressionProjection} using given constant value.
	 * @param <T> Expression type
	 * @param value Constant value (not null)
	 * @return A new {@link ConstantExpressionProjection}
	 */
	static <T> ConstantExpressionProjection<T> create(T value) {
		return new DefaultConstantExpressionProjection<>(value);
	}

}
