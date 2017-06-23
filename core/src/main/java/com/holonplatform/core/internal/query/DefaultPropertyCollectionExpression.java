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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.query.PropertyConstantExpression;

/**
 * Default {@link PropertyConstantExpression} implementation for value collections.
 *
 * @param <T> Value type
 *
 * @since 5.0.0
 */
public class DefaultPropertyCollectionExpression<T> extends ArrayList<T>
		implements PropertyConstantExpression<Collection<T>, T> {

	private static final long serialVersionUID = -4586355144951469596L;

	/**
	 * Property
	 */
	private final Property<T> property;

	/**
	 * Constructor.
	 * @param property Property (not null)
	 * @param values Constant values
	 */
	public DefaultPropertyCollectionExpression(Property<T> property, Collection<? extends T> values) {
		super(values);
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		this.property = property;
	}

	/**
	 * Constructor.
	 * @param property Property (not null)
	 * @param values Constant values
	 */
	@SafeVarargs
	public DefaultPropertyCollectionExpression(Property<T> property, T... values) {
		super(values.length);
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		this.property = property;
		for (T value : values) {
			add(value);
		}
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

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryExpression#getType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends T> getType() {
		return (Class<? extends T>) Collection.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.PropertyConstantExpression#getValue()
	 */
	@Override
	public Collection<T> getValue() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.PropertyConstantExpression#getModelValue()
	 */
	@Override
	public Object getModelValue() {
		final Collection<T> values = getValue();
		if (values != null && property.getConverter().isPresent()) {
			List<Object> modelValues = new ArrayList<>(values.size());
			for (T value : values) {
				modelValues.add(property.getConvertedValue(value));
			}
			return modelValues;
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.PropertyConstantExpression#getProperty()
	 */
	@Override
	public Property<T> getProperty() {
		return property;
	}

}
