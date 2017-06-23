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
package com.holonplatform.core.internal.i18n;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.i18n.Localizable.LocalizationException;
import com.holonplatform.core.i18n.Localization;
import com.holonplatform.core.i18n.LocalizationContext;
import com.holonplatform.core.i18n.MessageProvider;
import com.holonplatform.core.i18n.NumberFormatFeature;
import com.holonplatform.core.i18n.TemporalFormat;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.FormatUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Default {@link LocalizationContext} implementation.
 * 
 * <p>
 * By default, a cache is used to hold DateFormat and NumberFormat instances. Use
 * {@link #setUseDateTimeFormatsCache(boolean)} method to disable caching.
 * </p>
 * 
 * <p>
 * To enable messages localization, at least one {@link MessageProvider} has to be provided using
 * {@link #addMessageProvider(MessageProvider)}.
 * </p>
 * 
 * @since 5.0.0
 */
public class DefaultLocalizationContext implements LocalizationContext {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = I18nLogger.create();

	/**
	 * Enable date formats cache
	 */
	private boolean useDateTimeFormatsCache = true;

	/*
	 * Caches
	 */
	private final ConcurrentMap<TemporalFormat, DateFormat> dateFormatCache = new ConcurrentHashMap<>(2, 0.9f, 1);
	private final ConcurrentMap<TemporalFormat, DateFormat> timeFormatCache = new ConcurrentHashMap<>(2, 0.9f, 1);
	private final ConcurrentMap<DateTimeFormat, DateFormat> dateTimeFormatCache = new ConcurrentHashMap<>(2, 0.9f, 1);
	private final ConcurrentMap<TemporalFormat, DateTimeFormatter> dateFormatterCache = new ConcurrentHashMap<>(2, 0.9f,
			1);
	private final ConcurrentMap<TemporalFormat, DateTimeFormatter> timeFormatterCache = new ConcurrentHashMap<>(2, 0.9f,
			1);
	private final ConcurrentMap<DateTimeFormat, DateTimeFormatter> dateTimeFormatterCache = new ConcurrentHashMap<>(2,
			0.9f, 1);

	/**
	 * Default dates TemporalFormat style
	 */
	private TemporalFormat defaultDateFormatStyle;

	/**
	 * Default times TemporalFormat style
	 */
	private TemporalFormat defaultTimeFormatStyle;

	/**
	 * Default boolean localization
	 */
	private Map<Boolean, Localizable> defaultBooleanLocalizations;

	/**
	 * Message providers
	 */
	private List<MessageProvider> messageProviders = new LinkedList<>();

	/**
	 * Message arguments placeholder
	 */
	private String messageArgumentsPlaceholder = MessageProvider.DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER;

	/**
	 * Current Localization
	 */
	private Localization localization;

	/**
	 * Default constructor
	 */
	public DefaultLocalizationContext() {
		super();
	}

	/**
	 * Constructor with Locale initialization
	 * @param locale Initial Locale
	 */
	public DefaultLocalizationContext(Locale locale) {
		super();
		if (locale != null) {
			setLocalization(Localization.builder(locale).build());
		}
	}

	/**
	 * Constructor with Localization initialization
	 * @param localization Initial Localization
	 */
	public DefaultLocalizationContext(Localization localization) {
		super();
		if (localization != null) {
			setLocalization(localization);
		}
	}

	/**
	 * Current Localization
	 * @return Current localization for context, or <code>null</code> if none
	 */
	protected Localization getLocalization() {
		return localization;
	}

	/**
	 * Set current localization
	 * @param localization the localization to set
	 */
	protected void setLocalization(Localization localization) {
		this.localization = localization;

		LOGGER.debug(() -> "DefaultLocalizationContext "
				+ ((localization == null) ? "delocalized" : "localized: [" + localization + "]"));
	}

	/**
	 * Whether to cache date and time format instances
	 * @return <code>true</code> if date and time format instances cache is enabled
	 */
	protected boolean isUseDateTimeFormatsCache() {
		return useDateTimeFormatsCache;
	}

	/**
	 * Set whether to cache date and time format instances. Please note that standard Java {@link DateFormat} is not
	 * thread-safe, so you must take care of any concurrency issue using cached date/time format instances.
	 * @param useDateTimeFormatsCache <code>true</code> to cache date and time format instances
	 */
	public void setUseDateTimeFormatsCache(boolean useDateTimeFormatsCache) {
		this.useDateTimeFormatsCache = useDateTimeFormatsCache;
	}

	/**
	 * Add a message provider for messages localization. MessageProviders will be invoked in the order they were added
	 * to LocalizationContext.
	 * @param messageProvider MessageProvider to add
	 */
	public void addMessageProvider(MessageProvider messageProvider) {
		this.messageProviders.add(messageProvider);
	}

	/**
	 * Message providers for messages localization
	 * @return Message providers
	 */
	protected List<MessageProvider> getMessageProviders() {
		return messageProviders;
	}

	/**
	 * Message arguments placeholder symbol
	 * @return Message arguments placeholder
	 */
	protected String getMessageArgumentsPlaceholder() {
		return messageArgumentsPlaceholder;
	}

	/**
	 * Set message arguments placeholder symbol. Default is
	 * {@link MessageProvider#DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER}.
	 * @param messageArgumentsPlaceholder Message arguments placeholder to set
	 */
	public void setMessageArgumentsPlaceholder(String messageArgumentsPlaceholder) {
		this.messageArgumentsPlaceholder = messageArgumentsPlaceholder;
	}

	/**
	 * Clear all caches
	 */
	protected void clearCaches() {
		dateFormatCache.clear();
		timeFormatCache.clear();
		dateTimeFormatCache.clear();
		dateFormatterCache.clear();
		timeFormatterCache.clear();
		dateTimeFormatterCache.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#isLocalized()
	 */
	@Override
	public boolean isLocalized() {
		return getLocalization() != null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#localize(com.holonplatform.core.i18n.Localization)
	 */
	@Override
	public void localize(Localization localization) {
		clearCaches();
		if (localization != null && localization.getLocale() == null) {
			throw new LocalizationException("Invalid Localization: missing Locale");
		}
		setLocalization(localization);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#getLocale()
	 */
	@Override
	public Optional<Locale> getLocale() {
		return Optional.ofNullable(isLocalized() ? getLocalization().getLocale() : null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#setDefaultBooleanLocalization(boolean,
	 * com.holonplatform.core.i18n.Localizable)
	 */
	@Override
	public void setDefaultBooleanLocalization(boolean value, Localizable message) {
		ObjectUtils.argumentNotNull(message, "Localizable message must be not null");
		if (defaultBooleanLocalizations == null) {
			defaultBooleanLocalizations = new HashMap<>(2);
		}
		defaultBooleanLocalizations.put(Boolean.valueOf(value), message);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#getDefaultBooleanLocalization(boolean)
	 */
	@Override
	public Optional<Localizable> getDefaultBooleanLocalization(boolean value) {
		if (defaultBooleanLocalizations != null) {
			return Optional.ofNullable(defaultBooleanLocalizations.get(Boolean.valueOf(value)));
		}
		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#format(java.lang.Number, int,
	 * com.holonplatform.core.i18n.NumberFormatFeature[])
	 */
	@Override
	public String format(Number number, int decimalPositions, NumberFormatFeature... features) {
		if (number != null) {

			int decimals = decimalPositions;
			if (decimals < 0) {
				// use default, if any
				if (getLocalization() != null) {
					decimals = getLocalization().getDefaultDecimalPositions().orElse(-1);
				}
			}

			NumberFormat format = null;

			if (NumberFormatFeature.hasFeature(NumberFormatFeature.PERCENT_STYLE, features)) {
				format = NumberFormat.getPercentInstance(checkLocalized());

				if (NumberFormatFeature.hasFeature(NumberFormatFeature.HIDE_DECIMALS_WHEN_ALL_ZERO, features)
						&& !FormatUtils.hasDecimals(number.doubleValue())) {
					format.setMaximumFractionDigits(0);
				} else if (decimals > -1) {
					format.setMinimumFractionDigits(decimals);
					format.setMaximumFractionDigits(decimals);
				}

			} else if (TypeUtils.isDecimalNumber(number.getClass())) {
				format = NumberFormat.getInstance(checkLocalized());

				if (NumberFormatFeature.hasFeature(NumberFormatFeature.HIDE_DECIMALS_WHEN_ALL_ZERO, features)
						&& !FormatUtils.hasDecimals(number.doubleValue())) {
					format.setMaximumFractionDigits(0);
				} else if (decimals > -1) {
					format.setMinimumFractionDigits(decimals);
					format.setMaximumFractionDigits(decimals);
				}

			} else {
				format = NumberFormat.getIntegerInstance(checkLocalized());
			}

			if (NumberFormatFeature.hasFeature(NumberFormatFeature.DISABLE_GROUPING, features)) {
				format.setGroupingUsed(false);
			}

			return format.format(number);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#getNumberFormat(java.lang.Class, int, boolean)
	 */
	@Override
	public NumberFormat getNumberFormat(Class<? extends Number> numberType, int decimalPositions,
			boolean disableGrouping) {

		int decimals = decimalPositions;
		if (decimals < 0) {
			// use default, if any
			if (getLocalization() != null) {
				decimals = getLocalization().getDefaultDecimalPositions().orElse(-1);
			}
		}

		NumberFormat format = null;

		if (TypeUtils.isDecimalNumber(numberType)) {
			format = NumberFormat.getInstance(checkLocalized());
			if (decimals > -1) {
				format.setMinimumFractionDigits(decimals);
				format.setMaximumFractionDigits(decimals);
			}
		} else {
			format = NumberFormat.getIntegerInstance(checkLocalized());
		}

		if (disableGrouping) {
			format.setGroupingUsed(false);
		}
		return format;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#setDefaultDateFormatStyle(com.holonplatform.core.i18n.
	 * TemporalFormat)
	 */
	@Override
	public void setDefaultDateFormatStyle(TemporalFormat format) {
		this.defaultDateFormatStyle = format;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#setDefaultTimeFormatStyle(com.holonplatform.core.i18n.
	 * TemporalFormat)
	 */
	@Override
	public void setDefaultTimeFormatStyle(TemporalFormat format) {
		this.defaultTimeFormatStyle = format;
	}

	/**
	 * Gets the default dates format style
	 * @return Default dates format style
	 */
	protected Optional<TemporalFormat> getDefaultDateFormatStyle() {
		return Optional.ofNullable(defaultDateFormatStyle);
	}

	/**
	 * Gets the default times format style
	 * @return Default times format style
	 */
	protected Optional<TemporalFormat> getDefaultTimeFormatStyle() {
		return Optional.ofNullable(defaultTimeFormatStyle);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#format(java.util.Date,
	 * com.holonplatform.core.i18n.TemporalFormat, com.holonplatform.core.i18n.TemporalFormat)
	 */
	@Override
	public String format(Date date, TemporalType type, TemporalFormat dateFormat, TemporalFormat timeFormat) {
		if (date != null) {
			return getDateFormat(type, dateFormat, timeFormat).format(date);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#getDateFormat(com.holonplatform.core.temporal.TemporalType,
	 * com.holonplatform.core.i18n.TemporalFormat, com.holonplatform.core.i18n.TemporalFormat)
	 */
	@Override
	public DateFormat getDateFormat(TemporalType type, TemporalFormat dateFormat, TemporalFormat timeFormat) {
		TemporalType temporalType = (type != null) ? type : TemporalType.DATE;

		Localization lzn = getLocalization();
		if (lzn == null) {
			throw new LocalizationException("Context is not localized");
		}

		TemporalFormat df = (dateFormat != null) ? dateFormat : TemporalFormat.DEFAULT;
		TemporalFormat tf = (timeFormat != null) ? timeFormat : TemporalFormat.DEFAULT;

		if (df == TemporalFormat.DEFAULT) {
			df = lzn.getDefaultDateTemporalFormat().orElse(getDefaultDateFormatStyle().orElse(TemporalFormat.SHORT));
		}
		if (tf == TemporalFormat.DEFAULT) {
			tf = lzn.getDefaultTimeTemporalFormat().orElse(getDefaultTimeFormatStyle().orElse(TemporalFormat.SHORT));
		}

		DateFormat formatter;

		switch (temporalType) {
		case TIME: {
			if (isUseDateTimeFormatsCache() && timeFormatCache.containsKey(tf)) {
				formatter = timeFormatCache.get(tf);
			} else {
				formatter = DateFormat.getTimeInstance(tf.getTimeStyle(), checkLocalized());
				if (isUseDateTimeFormatsCache()) {
					timeFormatCache.put(tf, formatter);
				}
			}
		}
			break;
		case DATE_TIME: {
			DateTimeFormat dtf = new DateTimeFormat(df, tf);
			if (isUseDateTimeFormatsCache() && dateTimeFormatCache.containsKey(dtf)) {
				formatter = dateTimeFormatCache.get(dtf);
			} else {
				formatter = DateFormat.getDateTimeInstance(df.getDateStyle(), tf.getTimeStyle(), checkLocalized());
				if (isUseDateTimeFormatsCache()) {
					dateTimeFormatCache.put(dtf, formatter);
				}
			}
		}
			break;
		case DATE:
		default: {
			if (isUseDateTimeFormatsCache() && dateFormatCache.containsKey(df)) {
				formatter = dateFormatCache.get(df);
			} else {
				formatter = DateFormat.getDateInstance(df.getDateStyle(), checkLocalized());
				if (isUseDateTimeFormatsCache()) {
					dateFormatCache.put(df, formatter);
				}
			}
		}
			break;
		}

		return formatter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#format(java.time.temporal.Temporal,
	 * com.holonplatform.core.i18n.TemporalFormat, com.holonplatform.core.i18n.TemporalFormat)
	 */
	@Override
	public String format(Temporal temporal, TemporalFormat dateFormat, TemporalFormat timeFormat) {
		if (temporal != null) {
			return getDateTimeFormatter(TemporalType.getTemporalType(temporal), dateFormat, timeFormat)
					.format(temporal);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#getDateTimeFormatter(com.holonplatform.core.temporal.
	 * TemporalType, com.holonplatform.core.i18n.TemporalFormat, com.holonplatform.core.i18n.TemporalFormat)
	 */
	@Override
	public DateTimeFormatter getDateTimeFormatter(TemporalType type, TemporalFormat dateFormat,
			TemporalFormat timeFormat) {

		Localization lzn = getLocalization();
		if (lzn == null) {
			throw new LocalizationException("Context is not localized");
		}

		TemporalFormat df = (dateFormat != null) ? dateFormat : TemporalFormat.DEFAULT;
		TemporalFormat tf = (timeFormat != null) ? timeFormat : TemporalFormat.DEFAULT;

		if (df == TemporalFormat.DEFAULT) {
			df = lzn.getDefaultDateTemporalFormat().orElse(getDefaultDateFormatStyle().orElse(TemporalFormat.SHORT));
		}
		if (tf == TemporalFormat.DEFAULT) {
			tf = lzn.getDefaultTimeTemporalFormat().orElse(getDefaultTimeFormatStyle().orElse(TemporalFormat.SHORT));
		}

		DateTimeFormatter formatter;

		TemporalType temporalType = (type != null) ? type : TemporalType.DATE;

		switch (temporalType) {
		case TIME: {
			if (isUseDateTimeFormatsCache() && timeFormatterCache.containsKey(tf)) {
				formatter = timeFormatterCache.get(tf);
			} else {
				formatter = DateTimeFormatter.ofLocalizedTime(tf.getTimeFormatStyle()).withLocale(checkLocalized());
				if (isUseDateTimeFormatsCache()) {
					timeFormatterCache.put(tf, formatter);
				}
			}
		}
			break;
		case DATE_TIME: {
			DateTimeFormat dtf = new DateTimeFormat(df, tf);
			if (isUseDateTimeFormatsCache() && dateTimeFormatterCache.containsKey(dtf)) {
				formatter = dateTimeFormatterCache.get(dtf);
			} else {
				formatter = DateTimeFormatter.ofLocalizedDateTime(df.getDateFormatStyle(), tf.getTimeFormatStyle())
						.withLocale(checkLocalized());
				if (isUseDateTimeFormatsCache()) {
					dateTimeFormatterCache.put(dtf, formatter);
				}
			}
		}
			break;
		case DATE:
		default: {
			if (isUseDateTimeFormatsCache() && dateFormatterCache.containsKey(df)) {
				formatter = dateFormatterCache.get(df);
			} else {
				formatter = DateTimeFormatter.ofLocalizedDate(df.getDateFormatStyle()).withLocale(checkLocalized());
				if (isUseDateTimeFormatsCache()) {
					dateFormatterCache.put(df, formatter);
				}
			}
		}
			break;
		}
		return formatter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.LocalizationContext#getMessage(java.lang.String, java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public String getMessage(String code, String defaultMessage, Object... arguments) {

		ObjectUtils.argumentNotNull(code, "Message code must be not null");

		Locale locale = checkLocalized();

		LOGGER.debug(
				() -> "DefaultLocalizationContext: get message with code [" + code + "] for Locale [" + locale + "]");

		for (MessageProvider provider : getMessageProviders()) {
			Optional<String> value = getMessageFromProvider(locale, provider, code);
			if (value.isPresent()) {

				LOGGER.debug(() -> "DefaultLocalizationContext: message with code [" + code + "] for Locale [" + locale
						+ "] found from provider [" + provider + "]: [" + value.get() + "]");

				return resolveMessageArguments(value.get(), arguments);
			}
		}

		LOGGER.debug(() -> "DefaultLocalizationContext: message with code [" + code + "] for Locale [" + locale
				+ "] not found. Use default message [" + defaultMessage + "]");

		return resolveMessageArguments(defaultMessage, arguments);
	}

	/**
	 * Check the LocalizationContext is localized and return current {@link Locale}. If LocalizationContext is not
	 * localized, an {@link LocalizationException} is thrown.
	 * @return Current Locale
	 * @throws LocalizationException LocalizationContext is not localized
	 */
	protected Locale checkLocalized() throws LocalizationException {
		return getLocale().orElseThrow(() -> new LocalizationException("Context is not localized"));
	}

	/**
	 * Get message value using MessageProvider for given code. Parent localizations are used as fallback, if any.
	 * @param locale Locale for which to obtain the message localization
	 * @param provider MessageProvider
	 * @param code Message code
	 * @return Optional message value
	 */
	protected Optional<String> getMessageFromProvider(Locale locale, MessageProvider provider, String code) {
		Optional<String> value = provider.getMessage(locale, code);
		if (!value.isPresent()) {
			// check parent
			Optional<Localization> parent = getLocalization().getParent();
			while (parent.isPresent()) {
				value = provider.getMessage(parent.get().getLocale(), code);
				if (value.isPresent()) {
					return value;
				}
				parent = parent.get().getParent();
			}
		}
		return value;
	}

	/**
	 * Replace any message argument identified by {@link #getMessageArgumentsPlaceholder()} with given argument values.
	 * @param message Message to process
	 * @param arguments Arguments
	 * @return Message with resolved arguments.
	 */
	protected String resolveMessageArguments(String message, Object[] arguments) {
		if (message != null && arguments != null && arguments.length > 0 && getMessageArgumentsPlaceholder() != null) {
			Pattern pattern = Pattern.compile(FormatUtils.escapeRegexCharacters(getMessageArgumentsPlaceholder()));
			String resolved = message;
			for (Object argument : arguments) {
				String value = "";
				if (argument != null) {
					value = argument.toString();
				}
				resolved = pattern.matcher(resolved).replaceFirst(value);
			}
			return resolved;
		}
		return message;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultLocalizationContext [defaultDateFormatStyle=" + defaultDateFormatStyle
				+ ", defaultTimeFormatStyle=" + defaultTimeFormatStyle + ", messageProviders=" + messageProviders
				+ ", messageArgumentsPlaceholder=" + messageArgumentsPlaceholder + ", localization=" + localization
				+ "]";
	}

	/*
	 * Support class for dateTime format cache
	 */
	private static final class DateTimeFormat {

		TemporalFormat dateFormat;
		TemporalFormat timeFormat;

		public DateTimeFormat(TemporalFormat dateFormat, TemporalFormat timeFormat) {
			super();
			this.dateFormat = dateFormat;
			this.timeFormat = timeFormat;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dateFormat == null) ? 0 : dateFormat.hashCode());
			result = prime * result + ((timeFormat == null) ? 0 : timeFormat.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DateTimeFormat other = (DateTimeFormat) obj;
			if (dateFormat != other.dateFormat)
				return false;
			if (timeFormat != other.timeFormat)
				return false;
			return true;
		}

	}

	// Builder

	/**
	 * Builder for {@link LocalizationContext} creation
	 */
	public static class DefaultBuilder implements Builder {

		private final DefaultLocalizationContext context;

		public DefaultBuilder() {
			super();
			this.context = new DefaultLocalizationContext();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Builder#withInitialLocale(java.util.Locale)
		 */
		@Override
		public Builder withInitialLocale(Locale locale) {
			context.setLocalization(Localization.builder(locale).build());
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Builder#withInitialLocalization(com.holonplatform.core.i18n.Localization)
		 */
		@Override
		public Builder withInitialLocalization(Localization localization) {
			context.setLocalization(localization);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Builder#withInitialSystemLocale()
		 */
		@Override
		public Builder withInitialSystemLocale() {
			context.setLocalization(Localization.builder(Locale.getDefault()).build());
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.LocalizationContext.Builder#withDefaultBooleanLocalization(boolean,
		 * com.holonplatform.core.i18n.Localizable)
		 */
		@Override
		public Builder withDefaultBooleanLocalization(boolean value, Localizable message) {
			context.setDefaultBooleanLocalization(value, message);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.LocalizationContext.Builder#withDefaultDateTemporalFormat(com.holonplatform.
		 * core.i18n.TemporalFormat)
		 */
		@Override
		public Builder withDefaultDateTemporalFormat(TemporalFormat format) {
			context.setDefaultDateFormatStyle(format);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.LocalizationContext.Builder#withDefaultTimeTemporalFormat(com.holonplatform.
		 * core.i18n.TemporalFormat)
		 */
		@Override
		public Builder withDefaultTimeTemporalFormat(TemporalFormat format) {
			context.setDefaultTimeFormatStyle(format);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Builder#disableDateTimeFormatsCache()
		 */
		@Override
		public Builder disableDateTimeFormatsCache() {
			context.setUseDateTimeFormatsCache(false);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Builder#messageArgumentsPlaceholder(java.lang.String)
		 */
		@Override
		public Builder messageArgumentsPlaceholder(String placeholder) {
			context.setMessageArgumentsPlaceholder(placeholder);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Builder#messageProvider(com.holonplatform.core.i18n.MessageProvider)
		 */
		@Override
		public Builder messageProvider(MessageProvider messageProvider) {
			context.addMessageProvider(messageProvider);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Builder#build()
		 */
		@Override
		public LocalizationContext build() {
			return context;
		}

	}

}
