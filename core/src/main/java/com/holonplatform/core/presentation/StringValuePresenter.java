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
package com.holonplatform.core.presentation;

import java.util.Calendar;
import java.util.Date;

import com.holonplatform.core.Context;
import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.i18n.LocalizationContext;
import com.holonplatform.core.internal.presentation.DefaultStringValuePresenter;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Interface to present a generic value as a {@link String}.
 * 
 * @since 5.0.0
 */
@FunctionalInterface
public interface StringValuePresenter {

	static final String PRESENTATION_PARAMETERS_PREFIX = "holon.value-presenter.";

	/**
	 * Presentation parameter to set the separator to use when presenting multiple values (Arrays and Collections)
	 */
	public static final ConfigProperty<String> MULTIPLE_VALUES_SEPARATOR = ConfigProperty
			.create(PRESENTATION_PARAMETERS_PREFIX + "values-separator", String.class);

	/**
	 * Presentation parameter to limit the max length of the presented String
	 */
	public static final ConfigProperty<Integer> MAX_LENGTH = ConfigProperty
			.create(PRESENTATION_PARAMETERS_PREFIX + "max-length", Integer.class);

	/**
	 * Presentation parameter to specify the decimal positions to use to present numeric type values
	 */
	public static final ConfigProperty<Integer> DECIMAL_POSITIONS = ConfigProperty
			.create(PRESENTATION_PARAMETERS_PREFIX + "decimal-positions", Integer.class);

	/**
	 * Presentation parameter to disable the use of grouping symbol for numeric type values
	 */
	public static final ConfigProperty<Boolean> DISABLE_GROUPING = ConfigProperty
			.create(PRESENTATION_PARAMETERS_PREFIX + "disable-grouping", Boolean.class);

	/**
	 * Presentation parameter to hide number decimals when all decimal positions (if any) are equal to zero
	 */
	public static final ConfigProperty<Boolean> HIDE_DECIMALS_WHEN_ALL_ZERO = ConfigProperty
			.create(PRESENTATION_PARAMETERS_PREFIX + "hide-zero-decimals", Boolean.class);

	/**
	 * Presentation parameter to use percent-style format for numeric decimal values
	 */
	public static final ConfigProperty<Boolean> PERCENT_STYLE = ConfigProperty
			.create(PRESENTATION_PARAMETERS_PREFIX + "percent-style", Boolean.class);

	/**
	 * Presentation parameter to set the {@link TemporalType} to use to present {@link Date} and {@link Calendar} values
	 */
	public static final ConfigProperty<TemporalType> TEMPORAL_TYPE = ConfigProperty
			.create(PRESENTATION_PARAMETERS_PREFIX + "temporal-type", TemporalType.class);

	/**
	 * Present given <code>value</code> of given <code>valueType</code> as a String. Presentation parameters may be
	 * specified passing a {@link ParameterSet}.
	 * @param <T> Type of the value to present
	 * @param <V> Actual value type
	 * @param valueType Value type
	 * @param value Value to present
	 * @param parameters Optional presentation parameters
	 * @return String value
	 */
	<T, V extends T> String present(Class<T> valueType, V value, ParameterSet parameters);

	/**
	 * Present given <code>value</code> of given <code>valueType</code> as a String.
	 * @param <T> Type of the value to present
	 * @param <V> Actual value type
	 * @param valueType Value type
	 * @param value Value to present
	 * @return String value
	 */
	default <T, V extends T> String present(Class<T> valueType, V value) {
		return present(valueType, value, null);
	}

	/**
	 * Present given <code>value</code> as a String using given presentation parameters.
	 * @param value Value to present
	 * @param parameters Optional presentation parameters
	 * @return String value
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	default String present(Object value, ParameterSet parameters) {
		return present((value != null) ? (Class) value.getClass() : null, value, parameters);
	}

	/**
	 * Present given <code>value</code> as a String.
	 * @param value Value to present
	 * @return String value
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	default String present(Object value) {
		return present((value != null) ? (Class) value.getClass() : null, value, null);
	}

	/**
	 * Get the default {@link StringValuePresenter}, using the {@link LocalizationContext}, if available as
	 * {@link Context} resource, to format dates and numbers and to localize any {@link Localizable} message.
	 * @return Default {@link StringValuePresenter}
	 */
	static StringValuePresenter getDefault() {
		return DefaultStringValuePresenter.INSTANCE;
	}

}
