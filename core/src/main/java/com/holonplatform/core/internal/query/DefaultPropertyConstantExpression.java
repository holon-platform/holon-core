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

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.query.PropertyConstantExpression;

/**
 * Default {@link PropertyConstantExpression} implementation.
 *
 * @param <T> Value type
 * 
 * @since 5.0.0
 */
public class DefaultPropertyConstantExpression<T> implements PropertyConstantExpression<T, T> {

	/**
	 * Property
	 */
	private final Property<T> property;

	/**
	 * Constant value
	 */
	private final T value;

	/**
	 * Constructor
	 * @param property Property (not null)
	 * @param value Constant value (not null)
	 */
	public DefaultPropertyConstantExpression(Property<T> property, T value) {
		super();
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		this.property = property;
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.DefaultConstantExpression#getType()
	 */
	@Override
	public Class<? extends T> getType() {
		return property.getType();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.PropertyConstantExpression#getValue()
	 */
	@Override
	public T getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.PropertyConstantExpression#getModelValue()
	 */
	@Override
	public Object getModelValue() {
		return property.getConvertedValue(getValue());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.PropertyConstantExpression#getProperty()
	 */
	@Override
	public Property<T> getProperty() {
		return property;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (property == null) {
			throw new InvalidExpressionException("Null property");
		}
	}

}
