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

import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link PropertyValueConverter} to convert <code>enum</code> values into their ordinal {@link Integer} value and
 * vice-versa.
 * 
 * @param <E> Enum type
 *
 * @since 5.0.0
 */
public class EnumByOrdinalConverter<E extends Enum<E>> implements PropertyValueConverter<E, Integer> {

	private static final long serialVersionUID = 5921026823861464636L;

	private final Class<E> enumType;

	/**
	 * Constructor using the provided property time at each conversion as enum type.
	 */
	public EnumByOrdinalConverter() {
		this(null);
	}

	/**
	 * Constructor
	 * @param enumType Concrete enum type
	 */
	public EnumByOrdinalConverter(Class<E> enumType) {
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
	@Override
	public E fromModel(Integer value, Property<E> property) throws PropertyConversionException {
		if (value != null) {
			E[] values = (enumType != null) ? enumType.getEnumConstants() : property.getType().getEnumConstants();
			if (value.intValue() < 0 || value.intValue() >= values.length) {
				throw new IllegalArgumentException("Unable to convert value to required Enum type "
						+ ((enumType != null) ? enumType : property.getType()) + ": ordinal value " + value
						+ " is out of range");
			}
			return values[value.intValue()];

		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#toModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public Integer toModel(E value, Property<E> property) throws PropertyConversionException {
		if (value != null) {
			return value.ordinal();
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
	public Class<Integer> getModelType() {
		return Integer.class;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EnumByOrdinalConverter [enumType=" + enumType + "]";
	}

}
