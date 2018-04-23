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

import java.util.Optional;

import com.holonplatform.core.ConverterExpression;
import com.holonplatform.core.ExpressionValueConverter;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Abstract {@link ConverterExpression} implementation.
 * 
 * @param <T> Expression type
 *
 * @since 5.1.0
 */
public abstract class AbstractConverterExpression<T> implements ConverterExpression<T> {

	/**
	 * Expression temporal type
	 */
	private TemporalType temporalType;

	/**
	 * Optional value converter
	 */
	private final ExpressionValueConverter<T, ?> expressionValueConverter;

	/**
	 * Constructor without converter.
	 */
	public AbstractConverterExpression() {
		this(null);
	}

	/**
	 * Constructor with converter.
	 * @param expressionValueConverter Expresion value converter
	 */
	public AbstractConverterExpression(ExpressionValueConverter<T, ?> expressionValueConverter) {
		super();
		this.expressionValueConverter = expressionValueConverter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ConverterExpression#getExpressionValueConverter()
	 */
	@Override
	public Optional<ExpressionValueConverter<T, ?>> getExpressionValueConverter() {
		return Optional.ofNullable(expressionValueConverter);
	}

	/**
	 * Set the expression value temporal type.
	 * @param temporalType the temporal type to set
	 */
	public void setTemporalType(TemporalType temporalType) {
		this.temporalType = temporalType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.TypedExpression#getTemporalType()
	 */
	@Override
	public Optional<TemporalType> getTemporalType() {
		if (temporalType != null) {
			return Optional.of(temporalType);
		}
		return ConverterExpression.super.getTemporalType();
	}

}
