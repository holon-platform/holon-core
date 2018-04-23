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

import com.holonplatform.core.ExpressionValueConverter;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * An {@link ExpressionValueConverter} implementation using a {@link PropertyValueConverter} to perform value
 * conversions.
 * 
 * @param <TYPE> Expression type
 * @param <MODEL> Model type
 * 
 * @since 5.1.0
 */
public class DefaultPropertyExpressionValueConverter<TYPE, MODEL> implements ExpressionValueConverter<TYPE, MODEL> {

	private final Property<TYPE> property;
	private final PropertyValueConverter<TYPE, MODEL> converter;

	/**
	 * Constructor
	 * @param property Property (not null)
	 * @param converter Property value converter (not null)
	 */
	public DefaultPropertyExpressionValueConverter(Property<TYPE> property,
			PropertyValueConverter<TYPE, MODEL> converter) {
		super();
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		ObjectUtils.argumentNotNull(converter, "PropertyValueConverter must be not null");
		this.property = property;
		this.converter = converter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.ExpressionValueConverter#fromModel(java.lang.Object)
	 */
	@Override
	public TYPE fromModel(MODEL value) {
		return converter.fromModel(value, property);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.ExpressionValueConverter#toModel(java.lang.Object)
	 */
	@Override
	public MODEL toModel(TYPE value) {
		return converter.toModel(value, property);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.ExpressionValueConverter#getModelType()
	 */
	@Override
	public Class<MODEL> getModelType() {
		return converter.getModelType();
	}

}
