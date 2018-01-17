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

import com.holonplatform.core.query.ConstantExpression;
import com.holonplatform.core.query.ConverterExpression;
import com.holonplatform.core.query.ExpressionValueConverter;
import com.holonplatform.core.query.QueryExpression;

/**
 * A {@link QueryExpression} which represents a constant value
 * 
 * @param <T> Value type
 * 
 * @since 5.0.0
 */
public class DefaultConstantExpression<T> implements ConstantExpression<T, T> {

	/*
	 * Constant value (immutable)
	 */
	private final T value;

	/*
	 * Optional value converter
	 */
	private final ExpressionValueConverter<T, ?> expressionValueConverter;

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
	@SuppressWarnings("unchecked")
	public DefaultConstantExpression(QueryExpression<T> expression, T value) {
		super();
		this.value = value;
		this.expressionValueConverter = (expression instanceof ConverterExpression)
				? ((ConverterExpression<T>) expression).getExpressionValueConverter().orElse(null) : null;
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
		return getExpressionValueConverter().map(converter -> (Object) converter.toModel(getValue()))
				.orElse(getValue());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.ConverterExpression#getExpressionValueConverter()
	 */
	@Override
	public Optional<ExpressionValueConverter<T, ?>> getExpressionValueConverter() {
		return Optional.ofNullable(expressionValueConverter);
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultConstantExpression<?> other = (DefaultConstantExpression<?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
