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
package com.holonplatform.core.internal.presentation;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.holonplatform.core.Context;
import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.i18n.Caption;
import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.i18n.LocalizationContext;
import com.holonplatform.core.i18n.NumberFormatFeature;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.AnnotationUtils;
import com.holonplatform.core.internal.utils.FormatUtils;
import com.holonplatform.core.internal.utils.TypeUtils;
import com.holonplatform.core.presentation.StringValuePresenter;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Default {@link StringValuePresenter} implementation.
 * <p>
 * This implementation uses a {@link LocalizationContext}, if available as {@link Context} resource to format dates and
 * numbers and to localize any {@link Localizable} message.
 * </p>
 * 
 * @since 5.0.0
 */
public enum DefaultStringValuePresenter implements StringValuePresenter {

	INSTANCE;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = PresentationLogger.create();

	/**
	 * Default multiple values separator
	 */
	private static final String DEFAULT_MULTIPLE_VALUES_SEPARATOR = ";";

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.presentation.StringValuePresenter#present(java.lang.Class, java.lang.Object,
	 * com.holonplatform.core.parameters.ParameterSet)
	 */
	@Override
	public <T, V extends T> String present(Class<? extends T> valueType, V value, ParameterSet parameters) {

		final Object valueToPresent;

		// treat null boolean types as false
		if (value == null && valueType != null && TypeUtils.isBoolean(valueType)) {
			valueToPresent = Boolean.FALSE;
		} else {
			valueToPresent = value;
		}

		// always return null for null values
		if (valueToPresent == null) {
			return null;
		}

		// check type
		if (valueType != null && !TypeUtils.isAssignable(valueToPresent.getClass(), valueType)) {
			throw new TypeMismatchException("Value type " + valueToPresent.getClass().getName()
					+ " doesn't match given type " + valueType.getName());
		}

		final Class<?> type = (valueType != null) ? valueType : ((value != null) ? value.getClass() : Object.class);

		LOGGER.debug(() -> "Present value [" + valueToPresent + "] of type [" + type + "]");

		// present

		final ParameterSet presentationParameters = (parameters != null) ? parameters : ParameterSet.builder().build();

		final Optional<String> stringValue;

		if (type.isArray()) {
			// array
			stringValue = Optional.ofNullable(convertArray((Object[]) valueToPresent, presentationParameters));
		} else if (TypeUtils.isAssignable(type, Collection.class)) {
			// collection
			stringValue = Optional
					.ofNullable(convertArray(((Collection<?>) valueToPresent).toArray(), presentationParameters));
		} else {
			// single value
			stringValue = Optional.ofNullable(convertValue(valueToPresent, valueType, presentationParameters));
		}

		LOGGER.debug(() -> "Presented value [" + valueToPresent + "] of type [" + type + "] is ["
				+ stringValue.orElse(null) + "]");

		// check limit String length
		return stringValue.map(v -> presentationParameters.getParameterIf(MAX_LENGTH, ml -> ml > 0)
				.map(ml -> FormatUtils.limit(v, ml, true)).orElse(v)).orElse(null);
	}

	/**
	 * Convert an array of values
	 * @param values Values to convert
	 * @param parameters Presentation parameters
	 * @return String converted value
	 */
	private static String convertArray(Object[] values, ParameterSet parameters) {
		if (values.length == 0) {
			return null;
		}

		String separator = parameters.getParameter(MULTIPLE_VALUES_SEPARATOR, DEFAULT_MULTIPLE_VALUES_SEPARATOR);

		StringBuilder sb = new StringBuilder();
		for (Object v : values) {
			if (v != null) {
				String rv = convertValue(v, v.getClass(), parameters);
				if (rv != null) {
					if (sb.length() > 0) {
						sb.append(separator);
					}
					sb.append(rv);
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Convert a value to String
	 * @param value Value to convert
	 * @param valueType Value type
	 * @param parameters Presentation parameters
	 * @return String converted value
	 */
	private static String convertValue(Object value, Class<?> valueType, ParameterSet parameters) {
		if (TypeUtils.isBoolean(valueType)) {
			// Boolean
			return convertBoolean((Boolean) value);
		}
		// Try to render value according to a supported property type
		if (TypeUtils.isCharSequence(valueType)) {
			// String
			return convertCharSequence((CharSequence) value);
		}
		if (TypeUtils.isEnum(valueType)) {
			// Enum
			return convertEnum((Enum<?>) value);
		}
		if (TypeUtils.isTemporal(valueType)) {
			// Temporal
			return convertTemporal((Temporal) value);
		}
		if (TypeUtils.isDate(valueType) || TypeUtils.isCalendar(valueType)) {
			// Date
			return convertDate(TypeUtils.isCalendar(valueType) ? ((Calendar) value).getTime() : (Date) value,
					parameters.getParameter(TEMPORAL_TYPE, TemporalType.DATE));
		}
		if (TypeUtils.isNumber(valueType)) {
			// Number
			final List<NumberFormatFeature> features = new LinkedList<>();
			parameters.getParameterIf(DISABLE_GROUPING, v -> v)
					.ifPresent(v -> features.add(NumberFormatFeature.DISABLE_GROUPING));
			parameters.getParameterIf(HIDE_DECIMALS_WHEN_ALL_ZERO, v -> v)
					.ifPresent(v -> features.add(NumberFormatFeature.HIDE_DECIMALS_WHEN_ALL_ZERO));
			parameters.getParameterIf(PERCENT_STYLE, v -> v)
					.ifPresent(v -> features.add(NumberFormatFeature.PERCENT_STYLE));

			LOGGER.debug(() -> "Present numeric value [" + value + "] using features [" + features + "]");

			return convertNumber((Number) value, parameters.getParameter(DECIMAL_POSITIONS, -1),
					features.toArray(new NumberFormatFeature[0]));
		}
		if (TypeUtils.isAssignable(value.getClass(), Localizable.class)) {
			// Generic Localizable
			return convertLocalizable((Localizable) value);
		}

		// fallback to default toString()
		return value.toString();
	}

	/**
	 * Convert a {@link CharSequence} value
	 * @param value Value to convert
	 * @return String value
	 */
	private static String convertCharSequence(CharSequence value) {
		return value.toString();
	}

	/**
	 * Convert a {@link Localizable} value
	 * @param value Value to convert
	 * @return String value
	 */
	private static String convertLocalizable(Localizable value) {
		return LocalizationContext.translate(value, true);
	}

	/**
	 * Convert a {@link Boolean} value
	 * @param value Value to convert
	 * @return String value
	 */
	private static String convertBoolean(Boolean value) {
		return LocalizationContext.getCurrent().flatMap(u -> u.getDefaultBooleanLocalization(value))
				.map(m -> LocalizationContext.translate(m, true)).orElse(String.valueOf(value));
	}

	/**
	 * Convert a {@link Enum} value
	 * @param value Value to convert
	 * @return String value
	 */
	private static String convertEnum(Enum<?> value) {
		Localizable lv = null;
		if (value instanceof Localizable) {
			lv = (Localizable) value;
		}
		// check Caption annotation on value
		try {
			final Field fld = value.getClass().getField(value.name());
			if (fld.isAnnotationPresent(Caption.class)) {
				lv = Localizable.builder().message(fld.getAnnotation(Caption.class).value())
						.messageCode(AnnotationUtils.getStringValue(fld.getAnnotation(Caption.class).messageCode()))
						.build();
			}
		} catch (@SuppressWarnings("unused") Exception e) {
			// ignore
		}
		return (lv != null) ? convertLocalizable(lv) : value.name();
	}

	/**
	 * Convert a {@link Temporal} value
	 * @param value Value to convert
	 * @return String value
	 */
	private static String convertTemporal(Temporal value) {
		if (value != null) {
			return LocalizationContext.getCurrent().filter(l -> l.isLocalized()).map((c) -> c.format(value))
					.orElse(convertTemporalWithDefaultLocale(value));
		}
		return null;
	}

	/**
	 * Convert a {@link Date} value
	 * @param value Value to convert
	 * @param type Temporal type
	 * @return String value
	 */
	private static String convertDate(Date value, TemporalType type) {
		if (value != null) {
			return LocalizationContext.getCurrent().filter(l -> l.isLocalized()).map((c) -> c.format(value, type))
					.orElse(DateFormat.getDateInstance().format(value));
		}
		return null;
	}

	/**
	 * Convert a {@link Number} value
	 * @param value Value to convert
	 * @param decimals Decimal positions (-1 for default)
	 * @param features Number format features
	 * @return String value
	 */
	private static String convertNumber(Number value, int decimals, NumberFormatFeature[] features) {
		if (value != null) {
			return LocalizationContext.getCurrent().filter(l -> l.isLocalized())
					.map((c) -> c.format(value, decimals, features))
					.orElse(getNumberFormatForDefaultLocale(value,
							NumberFormatFeature.hasFeature(NumberFormatFeature.DISABLE_GROUPING, features),
							NumberFormatFeature.hasFeature(NumberFormatFeature.HIDE_DECIMALS_WHEN_ALL_ZERO, features))
									.format(value));
		}
		return null;
	}

	/**
	 * Build a {@link NumberFormat} using default {@link Locale}
	 * @param value Value to present
	 * @param disableGrouping <code>true</code> to disable groups separator
	 * @param hideZeroDecimals <code>true</code> to hide decimals when all zero
	 * @return NumberFormat
	 */
	private static NumberFormat getNumberFormatForDefaultLocale(Number value, boolean disableGrouping,
			boolean hideZeroDecimals) {
		NumberFormat numberFormat;
		if (value != null && TypeUtils.isDecimalNumber(value.getClass())) {
			numberFormat = NumberFormat.getInstance();

			if (hideZeroDecimals && !FormatUtils.hasDecimals(value.doubleValue())) {
				numberFormat.setMaximumFractionDigits(0);
			}

		} else {
			numberFormat = NumberFormat.getIntegerInstance();
		}

		if (disableGrouping) {
			numberFormat.setGroupingUsed(false);
		}

		return numberFormat;
	}

	/**
	 * Present {@link Temporal} property value using default {@link Locale}
	 * @param value Property value to present
	 * @return Formatted value
	 */
	private static String convertTemporalWithDefaultLocale(Temporal value) {
		// use default formatters
		final TemporalType type = TemporalType.getTemporalType(value).orElse(TemporalType.DATE);
		switch (type) {
		case DATE_TIME:
			return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
					.format(value);
		case TIME:
			return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault()).format(value);
		case DATE:
		default:
			return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.getDefault()).format(value);
		}
	}

}
