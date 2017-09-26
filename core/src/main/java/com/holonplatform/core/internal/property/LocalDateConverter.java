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

import java.time.LocalDate;
import java.util.Date;

import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link PropertyValueConverter} to convert {@link Date} type model values into {@link LocalDate} and back.
 *
 * @since 5.0.0
 */
public class LocalDateConverter implements PropertyValueConverter<LocalDate, Date> {

	private static final long serialVersionUID = -3386340710512918643L;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#fromModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public LocalDate fromModel(Date value, Property<LocalDate> property)
			throws com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException {
		return ConversionUtils.toLocalDate(value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#toModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public Date toModel(LocalDate value, Property<LocalDate> property)
			throws com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException {
		return ConversionUtils.fromLocalDate(value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getPropertyType()
	 */
	@Override
	public Class<LocalDate> getPropertyType() {
		return LocalDate.class;
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
		return "LocalDateConverter";
	}

}
