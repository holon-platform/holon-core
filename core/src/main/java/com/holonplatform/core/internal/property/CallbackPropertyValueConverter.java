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
package com.holonplatform.core.internal.property;

import java.util.function.Function;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link PropertyValueConverter} implementation which uses two provided {@link Function}s to perform actual
 * conversions.
 *
 * @param <TYPE> Property value type
 * @param <MODEL> Model data type
 *
 * @since 5.0.0
 */
public class CallbackPropertyValueConverter<TYPE, MODEL> implements PropertyValueConverter<TYPE, MODEL> {

	private static final long serialVersionUID = -4226232700674616194L;

	private final Class<TYPE> propertyType;
	private final Class<MODEL> modelType;
	private final Function<MODEL, TYPE> fromModel;
	private final Function<TYPE, MODEL> toModel;

	public CallbackPropertyValueConverter(Class<TYPE> propertyType, Class<MODEL> modelType,
			Function<MODEL, TYPE> fromModel, Function<TYPE, MODEL> toModel) {
		super();
		ObjectUtils.argumentNotNull(propertyType, "Property type must be not null");
		ObjectUtils.argumentNotNull(modelType, "Model type must be not null");
		ObjectUtils.argumentNotNull(fromModel, "Model to property type conversion function must be not null");
		ObjectUtils.argumentNotNull(toModel, "Property type to model conversion  must be not null");
		this.propertyType = propertyType;
		this.modelType = modelType;
		this.fromModel = fromModel;
		this.toModel = toModel;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#fromModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public TYPE fromModel(MODEL value, Property<TYPE> property)
			throws com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException {
		return fromModel.apply(value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#toModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public MODEL toModel(TYPE value, Property<TYPE> property)
			throws com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException {
		return toModel.apply(value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getPropertyType()
	 */
	@Override
	public Class<TYPE> getPropertyType() {
		return propertyType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getModelType()
	 */
	@Override
	public Class<MODEL> getModelType() {
		return modelType;
	}

}
