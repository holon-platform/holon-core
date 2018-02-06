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
package com.holonplatform.core.internal;

import com.holonplatform.core.ConverterExpression;
import com.holonplatform.core.ExpressionValueConverter;
import com.holonplatform.core.NullExpression;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link NullExpression} implementation.
 * 
 * @param <T> Expression type
 *
 * @since 5.1.0
 */
public class DefaultNullExpression<T> extends AbstractConverterExpression<T> implements NullExpression<T> {

	private final Class<? extends T> type;

	/**
	 * Default constructor.
	 * @param type Expression type (not null)
	 */
	public DefaultNullExpression(Class<? extends T> type) {
		super();
		ObjectUtils.argumentNotNull(type, "Expression type must be not null");
		this.type = type;
	}

	/**
	 * Constructor with associated expression.
	 * @param expression Optional expression from which to inherit an {@link ExpressionValueConverter}, if available.
	 */
	public DefaultNullExpression(TypedExpression<T> expression) {
		super((expression instanceof ConverterExpression)
				? ((ConverterExpression<T>) expression).getExpressionValueConverter().orElse(null) : null);
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
	 * @see com.holonplatform.core.NullExpression#getModelValue()
	 */
	@Override
	public Object getModelValue() {
		return getExpressionValueConverter().map(converter -> converter.toModel(null)).orElse(null);
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
		return "DefaultNullExpression [expressionValueConverter=" + getExpressionValueConverter().orElse(null) + "]";
	}

}
