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

import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link PropertyValueConverter} to convert <code>enum</code> values into their {@link String} name value and
 * vice-versa.
 * 
 * @param <E> Enum type
 *
 * @since 5.0.0
 */
public class EnumByNameConverter<E extends Enum<E>> implements PropertyValueConverter<E, String> {

	private static final long serialVersionUID = 7866013383369739077L;

	private final Class<E> enumType;

	/**
	 * Constructor using the provided property time at each conversion as enum type.
	 */
	public EnumByNameConverter() {
		this(null);
	}

	/**
	 * Constructor
	 * @param enumType Concrete enum type
	 */
	public EnumByNameConverter(Class<E> enumType) {
		super();
		this.enumType = enumType;
		if (enumType != null && !enumType.isEnum()) {
			throw new IllegalArgumentException("Property type to convert must be an Enum type");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#fromModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E fromModel(String value, Property<E> property)
			throws com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException {
		if (value != null) {
			try {
				return Enum.valueOf((Class<E>) ((enumType != null) ? enumType : property.getType()), value);
			} catch (@SuppressWarnings("unused") IllegalArgumentException e) {
				throw new PropertyConversionException(property, "Unable to convert value to required Enum type "
						+ ((enumType != null) ? enumType : property.getType()) + "invalid enum value: " + value);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#toModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public String toModel(E value, Property<E> property)
			throws com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException {
		if (value != null) {
			return value.name();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getPropertyType()
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class<E> getPropertyType() {
		return (enumType != null) ? enumType : (Class<E>) (Class) Enum.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getModelType()
	 */
	@Override
	public Class<String> getModelType() {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EnumByNameConverter [enumType=" + enumType + "]";
	}

}
