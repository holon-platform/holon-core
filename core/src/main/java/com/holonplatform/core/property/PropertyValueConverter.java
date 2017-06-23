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
package com.holonplatform.core.property;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.holonplatform.core.internal.property.EnumByNameConverter;
import com.holonplatform.core.internal.property.EnumByOrdinalConverter;
import com.holonplatform.core.internal.property.LocalDateConverter;
import com.holonplatform.core.internal.property.LocalDateTimeConverter;
import com.holonplatform.core.internal.property.NumericBooleanConverter;
import com.holonplatform.core.property.Property.PropertyAccessException;

/**
 * Performs conversion between a {@link Property} type and a data model type, in both directions.
 * <p>
 * The {@link #fromModel(Object, Property)} and {@link #toModel(Object, Property)} methods should be symmetric, so that
 * chaining these together returns the original result for all inputs.
 * </p>
 * 
 * @param <TYPE> Property value type
 * @param <MODEL> Model data type
 * 
 * @since 5.0.0
 * 
 * @see Property
 */
public interface PropertyValueConverter<TYPE, MODEL> extends Serializable {

	/**
	 * Convert given value from model data type to property value type
	 * @param value Value to convert
	 * @param property Target property
	 * @return Value converted to property value type
	 * @throws PropertyConversionException Error converting value
	 */
	TYPE fromModel(MODEL value, Property<TYPE> property) throws PropertyConversionException;

	/**
	 * Convert given value from property value type to model data type
	 * @param value Value to convert
	 * @param property Target property
	 * @return Value converted to model data type
	 * @throws PropertyConversionException Error converting value
	 */
	MODEL toModel(TYPE value, Property<TYPE> property) throws PropertyConversionException;

	/**
	 * Get the property type
	 * @return Property type
	 */
	Class<TYPE> getPropertyType();

	/**
	 * Get the model data type
	 * @return Model data type
	 */
	Class<MODEL> getModelType();

	// Builtin

	/**
	 * Create a <i>numeric boolean</i> {@link PropertyValueConverter} which converts property values of numeric type
	 * into boolean values using the following convention: <code>null</code> or <code>0</code> numeric values will be
	 * converted as <code>false</code> boolean values, any other value will be converted as boolean <code>true</code>
	 * value.
	 * @param <N> Concrete Number model type
	 * @param modelType Property value type (numeric)
	 * @return Numeric boolean converter
	 */
	static <N extends Number> PropertyValueConverter<Boolean, N> numericBoolean(Class<N> modelType) {
		return new NumericBooleanConverter<>(modelType);
	}

	/**
	 * Create a {@link PropertyValueConverter} which converts property values of given <code>enum</code> type into their
	 * corresponding ordinal {@link Integer} value and vice-versa.
	 * @param <E> Enum type
	 * @return Enum by ordinal converter
	 */
	static <E extends Enum<E>> PropertyValueConverter<E, Integer> enumByOrdinal() {
		return new EnumByOrdinalConverter<>();
	}

	/**
	 * Create a {@link PropertyValueConverter} which converts property values of given <code>enum</code> type into their
	 * corresponding name {@link String} value and vice-versa.
	 * @param <E> Enum type
	 * @return Enum by name converter
	 */
	static <E extends Enum<E>> PropertyValueConverter<E, String> enumByName() {
		return new EnumByNameConverter<>();
	}

	/**
	 * Create a {@link PropertyValueConverter} to convert {@link Date} model values into {@link LocalDate} type.
	 * @return LocalDate/Date value converter
	 */
	static PropertyValueConverter<LocalDate, Date> localDate() {
		return new LocalDateConverter();
	}

	/**
	 * Create a {@link PropertyValueConverter} to convert {@link Date} model values into {@link LocalDateTime} type.
	 * @return LocalDateTime/Date value converter
	 */
	static PropertyValueConverter<LocalDateTime, Date> localDateTime() {
		return new LocalDateTimeConverter();
	}

	// Exceptions

	/**
	 * Exception thrown when a property value conversion fails.
	 */
	@SuppressWarnings("serial")
	public class PropertyConversionException extends PropertyAccessException {

		/**
		 * Constructor with error message
		 * @param property Property to which exception is related
		 * @param message Error message
		 */
		public PropertyConversionException(Property<?> property, String message) {
			super(property, message);
		}

		/**
		 * Constructor with nested exception
		 * @param property Property to which exception is related
		 * @param cause Nested exception
		 */
		public PropertyConversionException(Property<?> property, Throwable cause) {
			super(property, cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param property Property to which exception is related
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public PropertyConversionException(Property<?> property, String message, Throwable cause) {
			super(property, message, cause);
		}

	}

}
