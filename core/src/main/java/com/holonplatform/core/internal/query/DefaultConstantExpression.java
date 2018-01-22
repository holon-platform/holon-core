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
package com.holonplatform.core.internal.query;

import java.util.Optional;

import com.holonplatform.core.ConverterExpression;
import com.holonplatform.core.ExpressionValueConverter;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.AbstractConverterExpression;
import com.holonplatform.core.query.ConstantExpression;

/**
 * Default {@link ConstantExpression} implementation.
 * 
 * @param <T> Value type
 * 
 * @since 5.0.0
 */
public class DefaultConstantExpression<T> extends AbstractConverterExpression<T> implements ConstantExpression<T, T> {

	/*
	 * Constant value (immutable)
	 */
	private final T value;

	/**
	 * Constructor
	 * @param value Constant value
	 */
	public DefaultConstantExpression(T value) {
		this(null, value);
	}

	/**
	 * Constructor
	 * @param expression Optional expression from which to inherit an {@link ExpressionValueConverter}, if available.
	 * @param value Constant value (not null)
	 */
	public DefaultConstantExpression(TypedExpression<T> expression, T value) {
		super((expression instanceof ConverterExpression)
				? ((ConverterExpression<T>) expression).getExpressionValueConverter().orElse(null) : null);
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryExpression.ConstantExpression#getValue()
	 */
	@Override
	public T getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.ConstantExpression#getModelValue()
	 */
	@Override
	public Object getModelValue() {
		Optional<?> modelValue = getExpressionValueConverter().map(converter -> converter.toModel(getValue()));
		if (modelValue.isPresent()) {
			return modelValue.get();
		}
		return getValue();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryDataExpression#getType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends T> getType() {
		return (value == null) ? (Class<? extends T>) Void.class : (Class<? extends T>) value.getClass();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultConstantExpression [value=" + value + "]";
	}

}
