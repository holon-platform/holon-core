/*
 * Copyright 2016-2018 Axioma srl.
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
package com.holonplatform.core.internal;

import com.holonplatform.core.ConstantConverterExpression;
import com.holonplatform.core.ConverterExpression;
import com.holonplatform.core.ExpressionValueConverter;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link ConstantConverterExpression} implementation.
 * 
 * @param <T> Expression and value type
 *
 * @since 5.2.0
 */
public class DefaultConstantConverterExpression<T> extends AbstractConverterExpression<T>
		implements ConstantConverterExpression<T, T> {

	/**
	 * Constant value
	 */
	private final T value;

	/**
	 * Value type
	 */
	private final Class<? extends T> type;

	/**
	 * Constructor.
	 * @param value Constant value
	 * @param type Value type
	 */
	public DefaultConstantConverterExpression(T value, Class<? extends T> type) {
		this(value, type, null);
	}

	/**
	 * Constructor.
	 * @param value Constant value
	 * @param type Value type
	 * @param expressionValueConverter Expression value converter (may be null)
	 */
	public DefaultConstantConverterExpression(T value, Class<? extends T> type,
			ExpressionValueConverter<T, ?> expressionValueConverter) {
		super(expressionValueConverter);
		this.value = value;
		this.type = type;
	}

	/**
	 * Constructor
	 * @param expression Expression from which to inherit an {@link ExpressionValueConverter}, if available (not null)
	 * @param value Constant value
	 */
	public DefaultConstantConverterExpression(TypedExpression<T> expression, T value) {
		super((expression instanceof ConverterExpression)
				? ((ConverterExpression<T>) expression).getExpressionValueConverter().orElse(null)
				: null);
		ObjectUtils.argumentNotNull(expression, "Expression must be not null");
		this.value = value;
		this.type = expression.getType();
		expression.getTemporalType().ifPresent(t -> setTemporalType(t));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.TypedExpression#getType()
	 */
	@Override
	public Class<? extends T> getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ConstantConverterExpression#getValue()
	 */
	@Override
	public T getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ConstantConverterExpression#getModelValue()
	 */
	@Override
	public Object getModelValue() {
		if (getExpressionValueConverter().isPresent()) {
			return getExpressionValueConverter().get().toModel(getValue());
		}
		return getValue();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getType() == null) {
			throw new InvalidExpressionException("Null expression type");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultConstantConverterExpression [value=" + value + ", type=" + type + "]";
	}

}
