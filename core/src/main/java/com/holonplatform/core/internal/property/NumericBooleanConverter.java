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
package com.holonplatform.core.internal.property;

import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link PropertyValueConverter} to handle numeric model values that must be considered as boolean property values,
 * using the following convention: <code>null</code> or <code>0</code> values will be treated as <code>false</code>
 * boolean values, any other value will be considered a <code>true</code> value.
 * 
 * @param <MODEL> Model numeric data type
 * 
 * @since 5.0.0
 */
public class NumericBooleanConverter<MODEL extends Number> implements PropertyValueConverter<Boolean, MODEL> {

	private static final long serialVersionUID = 839269041929894394L;

	/**
	 * Model numeric data type
	 */
	private final Class<MODEL> modelType;

	/**
	 * Empty constructor: the model type will setted as {@link Integer} type.
	 */
	@SuppressWarnings("unchecked")
	public NumericBooleanConverter() {
		this((Class<MODEL>) Integer.class);
	}

	/**
	 * Constructor
	 * @param modelType Model numeric data type
	 */
	public NumericBooleanConverter(Class<MODEL> modelType) {
		super();
		ObjectUtils.argumentNotNull(modelType, "Model type must be not null");
		this.modelType = modelType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#fromModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public Boolean fromModel(MODEL value, Property<Boolean> property) throws PropertyConversionException {
		return (value != null && value.intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#toModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public MODEL toModel(Boolean value, Property<Boolean> property) throws PropertyConversionException {
		try {
			return ConversionUtils.convertNumberToTargetClass(
					(value == null || !value.booleanValue()) ? Integer.valueOf(0) : Integer.valueOf(1), getModelType());
		} catch (Exception e) {
			throw new PropertyConversionException(property, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getPropertyType()
	 */
	@Override
	public Class<Boolean> getPropertyType() {
		return Boolean.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getModelType()
	 */
	@Override
	public Class<MODEL> getModelType() {
		return modelType;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NumericBooleanConverter [modelType=" + modelType + "]";
	}

}
