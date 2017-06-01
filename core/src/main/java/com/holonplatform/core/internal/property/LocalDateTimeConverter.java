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

import java.time.LocalDateTime;
import java.util.Date;

import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link PropertyValueConverter} to convert {@link Date} type model values into {@link LocalDateTime} and back.
 *
 * @since 5.0.0
 */
public class LocalDateTimeConverter implements PropertyValueConverter<LocalDateTime, Date> {

	private static final long serialVersionUID = 815811104231587162L;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#fromModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public LocalDateTime fromModel(Date value, Property<LocalDateTime> property)
			throws com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException {
		return ConversionUtils.toLocalDateTime(value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#toModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public Date toModel(LocalDateTime value, Property<LocalDateTime> property)
			throws com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException {
		return ConversionUtils.fromLocalDateTime(value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getPropertyType()
	 */
	@Override
	public Class<LocalDateTime> getPropertyType() {
		return LocalDateTime.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getModelType()
	 */
	@Override
	public Class<Date> getModelType() {
		return Date.class;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocalDateTimeConverter";
	}

}
