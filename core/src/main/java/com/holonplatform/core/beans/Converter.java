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
package com.holonplatform.core.beans;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.holonplatform.core.internal.property.EnumByNameConverter;
import com.holonplatform.core.internal.property.EnumByOrdinalConverter;
import com.holonplatform.core.internal.property.LocalDateConverter;
import com.holonplatform.core.internal.property.LocalDateTimeConverter;
import com.holonplatform.core.internal.property.NumericBooleanConverter;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * Annotation which can be used on a bean property to specify the {@link PropertyValueConverter} for the property
 * generated using bean property introspection.
 * 
 * @since 5.0.0
 * 
 * @see BeanIntrospector
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
@SuppressWarnings("rawtypes")
public @interface Converter {

	/**
	 * Builtin {@link PropertyValueConverter}s which can be used with {@link Converter#builtin()} attribute.
	 */
	public enum BUILTIN {

		/**
		 * Placeholder to represent a value not setted for builtin converter.
		 */
		NONE(null),

		/**
		 * Builtin converter to treat a property as a <em>numeric boolean</em> (i.e. converting <code>null</code> or
		 * <code>0</code> numeric values into {@link Boolean#FALSE} value and any other value into
		 * {@link Boolean#TRUE}), expecting {@link Integer} type as model type. value.
		 */
		NUMERIC_BOOLEAN(NumericBooleanConverter.class),

		/**
		 * Builtin converter to convert {@link Enum} property values into their {@link Integer} <em>ordinal</em> value
		 * and back. The property type must be an {@link Enum} type.
		 */
		ENUM_BY_ORDINAL(EnumByOrdinalConverter.class),

		/**
		 * Builtin converter to convert {@link Enum} property values into their {@link String} <em>name</em> value and
		 * back. The property type must be an {@link Enum} type.
		 */
		ENUM_BY_NAME(EnumByNameConverter.class),

		/**
		 * Builtin converter to convert {@link Date} property values into {@link LocalDate}s and back.
		 */
		LOCALDATE(LocalDateConverter.class),

		/**
		 * Builtin converter to convert {@link Date} property values into {@link LocalDateTime}s and back.
		 */
		LOCALDATETIME(LocalDateTimeConverter.class);

		private final Class<? extends PropertyValueConverter> converter;

		private BUILTIN(Class<? extends PropertyValueConverter> converter) {
			this.converter = converter;
		}

		/**
		 * Get the {@link PropertyValueConverter} class
		 * @return PropertyValueConverter class
		 */
		public Class<? extends PropertyValueConverter> getConverter() {
			return converter;
		}

	}

	/**
	 * Get the {@link PropertyValueConverter} class to use for the generated {@link Property}.
	 * <p>
	 * The specified class must provide a default empty constructor.
	 * </p>
	 * @return PropertyValueConverter class
	 */
	Class<? extends PropertyValueConverter> value() default PropertyValueConverter.class;

	/**
	 * Use one of the {@link BUILTIN} available converters.
	 * @return Builtin converter. {@link BUILTIN#NONE} indicates no value.
	 */
	BUILTIN builtin() default BUILTIN.NONE;

}
