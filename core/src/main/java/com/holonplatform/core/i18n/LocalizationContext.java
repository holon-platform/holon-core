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
package com.holonplatform.core.i18n;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import com.holonplatform.core.Context;
import com.holonplatform.core.Registration;
import com.holonplatform.core.i18n.Localizable.LocalizationException;
import com.holonplatform.core.internal.i18n.DefaultLocalizationContext;
import com.holonplatform.core.internal.utils.FormatUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Main interface to handle localization and internationalization.
 * <p>
 * A LocalizationContext must be <em>localized</em>, i.e. initialized, using {@link #localize(Localization)} or
 * {@link #localize(Locale)} before using it.
 * </p>
 * <p>
 * Provides methods to localize messages, numbers and temporal data. For messages localization, it relies on
 * {@link MessageProvider}s, which must be registered using {@link Builder#messageProvider(MessageProvider)} at context
 * build time.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see MessageProvider
 */
public interface LocalizationContext {

	/**
	 * Default {@link Context} resource key
	 */
	public static final String CONTEXT_KEY = LocalizationContext.class.getName();

	/**
	 * Returns whether this context is <i>localized</i>, i.e. it was initialized with {@link Localization} informations
	 * and ready to be used.
	 * @return <code>true</code> if context is localized
	 */
	boolean isLocalized();

	/**
	 * Initialize context with given {@link Locale}, using Locale's default settings for numbers and dates formats and
	 * symbols.
	 * @param locale The new {@link Locale} to set, may be <code>null</code>
	 */
	default void localize(Locale locale) {
		localize(locale, true);
	}

	/**
	 * Initialize context with given {@link Locale}, using Locale's default settings for numbers and dates formats and
	 * symbols.
	 * @param locale The new {@link Locale} to set, may be <code>null</code>
	 * @param fireEvent Whether to fire the localization change event
	 */
	default void localize(Locale locale, boolean fireEvent) {
		localize((locale != null) ? Localization.builder(locale).build() : null, fireEvent);
	}

	/**
	 * Initialize context with given {@link Localization} informations.
	 * @param localization The new {@link Localization} to set, may be <code>null</code>
	 */
	default void localize(Localization localization) {
		localize(localization, true);
	}

	/**
	 * Initialize context with given {@link Localization} informations.
	 * @param localization The new {@link Localization} to set, may be <code>null</code>
	 * @param fireEvent Whether to fire the localization change event
	 */
	void localize(Localization localization, boolean fireEvent);

	/**
	 * Get Locale currently bound to this context, if any
	 * @return Optional context Locale, or an empty Optional if context is not localized
	 */
	Optional<Locale> getLocale();

	/**
	 * Get the symbol to be used as message arguments placeholder.
	 * @return Optional message arguments placeholder
	 */
	Optional<String> getMessageArgumentsPlaceholder();

	/**
	 * Add a {@link LocalizationChangeListener} to listen for localization changes.
	 * @param listener The listener to add (not null)
	 * @return this
	 */
	Registration addLocalizationChangeListener(LocalizationChangeListener listener);

	/**
	 * Get a message for given <code>code</code> localized according to current Locale to which context is bound
	 * @param code Message code (not null)
	 * @param defaultMessage Default message to return if no localized message could be found
	 * @param arguments Optional translation arguments
	 * @return Localized message, or <code>defaultMessage</code> if no suitable message was found
	 * @throws LocalizationException If context is not localized
	 */
	String getMessage(String code, String defaultMessage, Object... arguments);

	/**
	 * Get the localized message for given {@link Localizable}.
	 * @param localizable Localizable message
	 * @return Localized message, or default {@link Localizable#getMessage()} if {@link Localizable#getMessageCode()} is
	 *         null
	 * @throws LocalizationException If context is not localized
	 */
	default String getMessage(Localizable localizable) {
		return getMessage(localizable, false);
	}

	/**
	 * Get a possibly localized message if the {@link LocalizationContext} is localized. If not and <code>lenient</code>
	 * is set to <code>true</code>, default {@link Localizable#getMessage()} is returned.
	 * @param localizable Localizable message
	 * @param lenient if <code>true</code> and the {@link LocalizationContext} is not localized, default
	 *        {@link Localizable#getMessage()} is returned, otherwise a {@link LocalizationException} is thrown
	 * @return Localized message
	 * @throws LocalizationException If not <code>lenient</code> and context is not localized
	 */
	String getMessage(Localizable localizable, boolean lenient);

	/**
	 * Sets the default {@link Localizable} message to use to localize a boolean value
	 * @param value Boolean value
	 * @param message The localizable message to use to localize given boolean value
	 */
	void setDefaultBooleanLocalization(boolean value, Localizable message);

	/**
	 * Gets the default {@link Localizable} message to use to localize a boolean value
	 * @param value Boolean value to localize
	 * @return Optional localizable message to use to localize given boolean value
	 */
	Optional<Localizable> getDefaultBooleanLocalization(boolean value);

	/**
	 * Set the default {@link TemporalFormat} style to use to format dates.
	 * <p>
	 * This is overriden by any {@link Localization#getDefaultDateTemporalFormat()} value.
	 * </p>
	 * @param format Defalt date format style
	 */
	void setDefaultDateFormatStyle(TemporalFormat format);

	/**
	 * Set the default {@link TemporalFormat} style to use to format times.
	 * <p>
	 * This is overriden by any {@link Localization#getDefaultTimeTemporalFormat()} value.
	 * </p>
	 * @param format Defalt time format style
	 */
	void setDefaultTimeFormatStyle(TemporalFormat format);

	/**
	 * Format given <code>number</code> according to current Context {@link Locale}.
	 * @param number Number to format (may be null)
	 * @param decimalPositions Decimal positions to use (<code>-1</code> for default, i.e. display all the decimal
	 *        positions of the given Number, if any)
	 * @param features Optional format features
	 * @return Formatted number, or <code>null</code> if given Number was null
	 * @throws LocalizationException If context is not localized
	 */
	String format(Number number, int decimalPositions, NumberFormatFeature... features);

	/**
	 * Format given <code>number</code> according to current Context {@link Locale}.
	 * @param number Number to format (may be null)
	 * @param features Optional format features
	 * @return Formatted number, or <code>null</code> if given Number was null
	 * @throws LocalizationException If context is not localized
	 */
	default String format(Number number, NumberFormatFeature... features) {
		return format(number, -1, features);
	}

	/**
	 * Get a {@link NumberFormat} for numbers formatting and set it up according to context {@link Locale} and
	 * {@link Localization} settings.
	 * @param numberType Number type
	 * @param decimalPositions Decimal positions to use with decimal number types (<code>-1</code> for default, i.e.
	 *        display all the decimal positions, if any)
	 * @param disableGrouping <code>true</code> to not use grouping symbol
	 * @return NumberFormat
	 * @throws LocalizationException If context is not localized
	 */
	NumberFormat getNumberFormat(Class<? extends Number> numberType, int decimalPositions, boolean disableGrouping);

	/**
	 * Get a {@link NumberFormat} for numbers formatting and set it up according to context {@link Locale} and
	 * {@link Localization} settings.
	 * @param numberType Number type
	 * @return NumberFormat
	 * @throws LocalizationException If context is not localized
	 */
	default NumberFormat getNumberFormat(Class<? extends Number> numberType) {
		return getNumberFormat(numberType, -1, false);
	}

	/**
	 * Format given <code>date</code> according to current Context {@link Locale}.
	 * @param date Date to format (may be null)
	 * @param type Date temporal type (Only a date, only a time or a date and time)
	 * @param dateFormat Date format style (null for default)
	 * @param timeFormat Time format style (null for default)
	 * @return Formatted date, or <code>null</code> if given date was null
	 * @throws LocalizationException If context is not localized
	 */
	String format(Date date, TemporalType type, TemporalFormat dateFormat, TemporalFormat timeFormat);

	/**
	 * Format given <code>date</code> according to current Context {@link Locale} using default format styles.
	 * @param date Date to format (may be null)
	 * @param type Date temporal type (Only a date, only a time or a date and time)
	 * @return Formatted date, or <code>null</code> if given date was null
	 * @throws LocalizationException If context is not localized
	 */
	default String format(Date date, TemporalType type) {
		return format(date, type, TemporalFormat.DEFAULT, TemporalFormat.DEFAULT);
	}

	/**
	 * Get a {@link DateFormat} for {@link Date} formatting and set it up according to context {@link Locale} and
	 * {@link Localization} settings.
	 * @param type Temporal type
	 * @param dateFormat Date format style, <code>null</code> for default
	 * @param timeFormat Time format style, <code>null</code> for default
	 * @return DateFormat
	 * @throws LocalizationException If context is not localized
	 */
	DateFormat getDateFormat(TemporalType type, TemporalFormat dateFormat, TemporalFormat timeFormat);

	/**
	 * Get a {@link DateFormat} for {@link Date} formatting and set it up according to context {@link Locale} and
	 * {@link Localization} settings, using default format styles.
	 * @param type Temporal type
	 * @return DateFormat
	 * @throws LocalizationException If context is not localized
	 */
	default DateFormat getDateFormat(TemporalType type) {
		return getDateFormat(type, null, null);
	}

	/**
	 * Format given <code>temporal</code> (for example a {@link LocalDate}, a {@link LocalTime} or a
	 * {@link LocalDateTime}) according to current Context {@link Locale}.
	 * @param temporal Temporal to format (may be null)
	 * @param dateFormat Date format style (null for default)
	 * @param timeFormat Time format style (null for default)
	 * @return Formatted temporal, or <code>null</code> if given temporal was null
	 * @throws LocalizationException If context is not localized
	 */
	String format(Temporal temporal, TemporalFormat dateFormat, TemporalFormat timeFormat);

	/**
	 * Format given <code>temporal</code> (for example a {@link LocalDate}, a {@link LocalTime} or a
	 * {@link LocalDateTime}) according to current Context {@link Locale} using default format styles.
	 * @param temporal Temporal to format (may be null)
	 * @return Formatted temporal, or <code>null</code> if given temporal was null
	 * @throws LocalizationException If context is not localized
	 */
	default String format(Temporal temporal) {
		return format(temporal, TemporalFormat.DEFAULT, TemporalFormat.DEFAULT);
	}

	/**
	 * Format given {@link LocalDate} according to current Context {@link Locale} using given date format style.
	 * @param temporal Date to format (may be null)
	 * @param dateFormat Date format style
	 * @return Formatted temporal, or <code>null</code> if given temporal was null
	 * @throws LocalizationException If context is not localized
	 */
	default String format(LocalDate temporal, TemporalFormat dateFormat) {
		return format(temporal, dateFormat, TemporalFormat.DEFAULT);
	}

	/**
	 * Format given {@link LocalTime} according to current Context {@link Locale} using given time format style.
	 * @param temporal Time to format (may be null)
	 * @param timeFormat Time format style
	 * @return Formatted temporal, or <code>null</code> if given temporal was null
	 * @throws LocalizationException If context is not localized
	 */
	default String format(LocalTime temporal, TemporalFormat timeFormat) {
		return format(temporal, TemporalFormat.DEFAULT, timeFormat);
	}

	/**
	 * Get a {@link DateTimeFormatter} for jakarta.time.* types formatting and set it up according to context
	 * {@link Locale} and {@link Localization} settings.
	 * @param type Temporal type
	 * @param dateFormat Date format style, <code>null</code> for default
	 * @param timeFormat Time format style, <code>null</code> for default
	 * @return DateTimeFormatter
	 * @throws LocalizationException If context is not localized
	 */
	DateTimeFormatter getDateTimeFormatter(TemporalType type, TemporalFormat dateFormat, TemporalFormat timeFormat);

	/**
	 * Get a {@link DateTimeFormatter} for jakarta.time.* types formatting and set it up according to context
	 * {@link Locale} and {@link Localization} settings, using default format styles.
	 * @param type Temporal type
	 * @return DateTimeFormatter
	 * @throws LocalizationException If context is not localized
	 */
	default DateTimeFormatter getDateTimeFormatter(TemporalType type) {
		return getDateTimeFormatter(type, null, null);
	}

	// ------- MessageResolver

	/**
	 * Get this localization context as a {@link MessageResolver}, which provides messages localization by
	 * {@link Locale} using the available {@link MessageProvider}s.
	 * @return this localization context as a {@link MessageResolver}
	 */
	MessageResolver asMessageResolver();

	// ------- Helpers

	/**
	 * Try to translate given <code>localizable</code> using the {@link LocalizationContext} available as
	 * {@link Context} resource through {@link LocalizationContext#getCurrent()}. The LocalizationContext must be
	 * localized.
	 * @param localizable Localizable to translate
	 * @param lenient if <code>true</code> and a {@link LocalizationContext} is not available or it is not localized,
	 *        silently return default {@link Localizable#getMessage()}. If <code>false</code>, a
	 *        {@link LocalizationException} is thrown for that situation
	 * @return The localized message, or default {@link Localizable#getMessage()} if a localized LocalizationContext is
	 *         not available and lenient is <code>true</code>, or {@link Localizable#getMessageCode()} is
	 *         <code>null</code>.
	 */
	static String translate(final Localizable localizable, final boolean lenient) {
		ObjectUtils.argumentNotNull(localizable, "Localizable must be not null");
		Optional<LocalizationContext> lc = LocalizationContext.getCurrent().filter(l -> l.isLocalized());
		if (!lc.isPresent() && !lenient) {
			throw new LocalizationException(
					"A LocalizationContext is not available from context or it is not localized");
		}
		return lc.map(l -> l.getMessage(localizable, lenient))
				.orElse(FormatUtils.resolveMessageArguments(MessageProvider.DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER,
						localizable.getMessage(), localizable.getMessageArguments()));
	}

	/**
	 * Try to translate given <code>localizable</code> using the {@link LocalizationContext} available as
	 * {@link Context} resource through {@link LocalizationContext#getCurrent()}. The LocalizationContext must be
	 * localized.
	 * @param localizable Localizable to translate
	 * @return The localized message, or default {@link Localizable#getMessage()} if
	 *         {@link Localizable#getMessageCode()} is <code>null</code>
	 * @throws LocalizationException If a {@link LocalizationContext} is not available as {@link Context} resource or it
	 *         is not localized
	 */
	static String translate(final Localizable localizable) {
		return translate(localizable, false);
	}

	/**
	 * Try to translate given <code>messageCode</code> using the {@link LocalizationContext} available as
	 * {@link Context} resource through {@link LocalizationContext#getCurrent()}. The LocalizationContext must be
	 * localized.
	 * @param messageCode Message code to translate
	 * @param defaultMessage Default message
	 * @param lenient if <code>true</code> and a {@link LocalizationContext} is not available or it is not localized,
	 *        silently return default <code>defaultMessage</code>. If <code>false</code>, a
	 *        {@link LocalizationException} is thrown for that situation
	 * @return The localized message, or default <code>defaultMessage</code> if a localized LocalizationContext is not
	 *         available and lenient is <code>true</code>, or <code>messageCode</code> is <code>null</code>.
	 */
	static String translate(String messageCode, String defaultMessage, boolean lenient) {
		return translate(Localizable.builder().messageCode(messageCode).message(defaultMessage).build(), lenient);
	}

	/**
	 * Try to translate given <code>messageCode</code> using the {@link LocalizationContext} available as
	 * {@link Context} resource through {@link LocalizationContext#getCurrent()}. The LocalizationContext must be
	 * localized.
	 * @param messageCode Message code to translate
	 * @param defaultMessage Default message
	 * @return The localized message, or default <code>defaultMessage</code> if <code>messageCode</code> is
	 *         <code>null</code>
	 * @throws LocalizationException If a {@link LocalizationContext} is not available as {@link Context} resource or it
	 *         is not localized
	 */
	static String translate(String messageCode, String defaultMessage) {
		return translate(messageCode, defaultMessage, false);
	}

	// Context resource

	/**
	 * Convenience method to obtain the current {@link LocalizationContext} made available as {@link Context} resource,
	 * using default {@link ClassLoader}.
	 * <p>
	 * See {@link Context#resource(String, Class)} for details about context resources availability conditions.
	 * </p>
	 * @return Optional LocalizationContext, empty if not available as context resource
	 */
	static Optional<LocalizationContext> getCurrent() {
		return Context.get().resource(CONTEXT_KEY, LocalizationContext.class);
	}

	/**
	 * Requires the current {@link LocalizationContext}. If not available using {@link #getCurrent()}, an
	 * {@link IllegalStateException} is thrown.
	 * @return Current LocalizationContext
	 * @throws IllegalStateException LocalizationContext is not available as a {@link Context} resource
	 */
	static LocalizationContext require() {
		return getCurrent().orElseThrow(
				() -> new IllegalStateException("LocalizationContext is not available as context resource"));
	}

	/**
	 * Requires the current {@link LocalizationContext}, checking that it is localized. If not available using
	 * {@link #getCurrent()}, an {@link IllegalStateException} is thrown. If not localized, a
	 * {@link LocalizationException} is thrown instead.
	 * @return Current LocalizationContext
	 * @throws IllegalStateException LocalizationContext is not available as a {@link Context} resource
	 * @throws LocalizationException A LocalizationContext is available as context resource but it is not localized
	 * @see #isLocalized()
	 */
	static LocalizationContext requireLocalized() {
		LocalizationContext localizationContext = require();
		if (!localizationContext.isLocalized()) {
			throw new LocalizationException("LocalizationContext is not localized");
		}
		return localizationContext;
	}

	// ------- Listeners

	/**
	 * A listener to listen for {@link LocalizationContext} localization change events.
	 * 
	 * @since 5.2.0
	 */
	@FunctionalInterface
	public interface LocalizationChangeListener extends Serializable {

		/**
		 * Invoked when the {@link LocalizationContext} localization changes.
		 * @param event The localization change event
		 */
		void onLocalizationChange(LocalizationChangeEvent event);

	}

	/**
	 * A localization change event.
	 *
	 * @since 5.2.0
	 */
	public interface LocalizationChangeEvent extends Serializable {

		/**
		 * Get the {@link LocalizationContext} which triggered the localization change event.
		 * @return the {@link LocalizationContext}
		 */
		LocalizationContext getSource();

		/**
		 * Get the new {@link LocalizationContext} {@link Locale}, if available.
		 * @return Optional {@link Locale}
		 */
		default Optional<Locale> getLocale() {
			return getSource().getLocale();
		}

	}

	/**
	 * A listener which can be registered to a {@link LocalizationContext} to be notified when a message translation is
	 * missing from any of the available {@link MessageProvider}s.
	 * 
	 * @since 5.0.4
	 */
	@FunctionalInterface
	public interface MissingMessageLocalizationListener extends Serializable {

		/**
		 * Triggered when a message localization is not provided by any of the {@link MessageProvider}s registered in
		 * the {@link LocalizationContext}.
		 * @param locale The {@link Locale} for which the message localization was requested (never null)
		 * @param messageCode Localization message code (never null)
		 * @param defaultMessage The provided default message, if available (may be null)
		 */
		void messageLocalizationIsMissing(Locale locale, String messageCode, String defaultMessage);

	}

	// ------- Builder

	/**
	 * Builder to create LocalizationContext instances
	 * @return LocalizationContextBuilder
	 */
	static Builder builder() {
		return new DefaultLocalizationContext.DefaultBuilder();
	}

	/**
	 * Builder for {@link LocalizationContext} creation
	 */
	public interface Builder {

		/**
		 * Initialize context with given Locale
		 * @param locale Locale
		 * @return this
		 */
		Builder withInitialLocale(Locale locale);

		/**
		 * Initialize context with given Localization
		 * @param localization Localization
		 * @return this
		 */
		Builder withInitialLocalization(Localization localization);

		/**
		 * Initialize context with current system default Locale
		 * @return this
		 */
		Builder withInitialSystemLocale();

		/**
		 * Sets the default {@link Localizable} message to use to localize a boolean value
		 * @param value Boolean value
		 * @param message The localizable message to use to localize given boolean value
		 * @return this
		 */
		Builder withDefaultBooleanLocalization(boolean value, Localizable message);

		/**
		 * Set the default {@link TemporalFormat} style to use to format dates.
		 * <p>
		 * This is overriden by any {@link Localization#getDefaultDateTemporalFormat()} value.
		 * </p>
		 * @param format Defalt date format style
		 * @return this
		 */
		Builder withDefaultDateTemporalFormat(TemporalFormat format);

		/**
		 * Set the default {@link TemporalFormat} style to use to format times.
		 * <p>
		 * This is overriden by any {@link Localization#getDefaultTimeTemporalFormat()} value.
		 * </p>
		 * @param format Defalt time format style
		 * @return this
		 */
		Builder withDefaultTimeTemporalFormat(TemporalFormat format);

		/**
		 * Disable caching of date/time formatters
		 * @return this
		 */
		Builder disableDateTimeFormatsCache();

		/**
		 * Set localized message arguments placehoder. Default is
		 * {@link MessageProvider#DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER}.
		 * @param placeholder Placeholder for message arguments
		 * @return this
		 */
		Builder messageArgumentsPlaceholder(String placeholder);

		/**
		 * Add a {@link MessageProvider} for messages localization.
		 * @param messageProvider The MessageProvider to add
		 * @return this
		 */
		Builder withMessageProvider(MessageProvider messageProvider);

		/**
		 * Add a MessageProvider for messages localization
		 * @param messageProvider MessageProvider to add
		 * @return this
		 * @deprecated Use {@link #withMessageProvider(MessageProvider)}
		 */
		@Deprecated
		default Builder messageProvider(MessageProvider messageProvider) {
			return withMessageProvider(messageProvider);
		}

		/**
		 * Add a {@link LocalizationChangeListener} to listen for localization changes.
		 * @param listener The listener to add (not null)
		 * @return this
		 */
		Builder withLocalizationChangeListener(LocalizationChangeListener listener);

		/**
		 * Add a {@link MissingMessageLocalizationListener} to be notified when a message localization is not provided
		 * by any of the {@link MessageProvider}s registered in the {@link LocalizationContext}.
		 * @param listener The listener to add (not null)
		 * @return this
		 */
		Builder withMissingMessageLocalizationListener(MissingMessageLocalizationListener listener);

		/**
		 * Build LocalizationContext instance
		 * @return LocalizationContext
		 */
		LocalizationContext build();

	}

}
