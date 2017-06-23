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
package com.holonplatform.core.i18n;

import java.util.Locale;
import java.util.Optional;

import com.holonplatform.core.i18n.Localizable.LocalizationException;
import com.holonplatform.core.internal.i18n.PropertiesMessageProvider;

/**
 * Main interface to be used with a {@link LocalizationContext} to localize messages according to a {@link Locale}.
 * 
 * @since 5.0.0
 * 
 * @see LocalizationContext
 */
@FunctionalInterface
public interface MessageProvider {

	/**
	 * Default argument placeholder for message localization
	 */
	public static final String DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER = "&";

	/**
	 * Get a localized message in given <code>locale</code> for specified message <code>code</code>
	 * @param locale Locale
	 * @param code Message code
	 * @return Optional localized message
	 * @throws LocalizationException Error retrieving message
	 */
	Optional<String> getMessage(Locale locale, String code) throws LocalizationException;

	// Builder for Properties message source

	/**
	 * Builder to create a {@link MessageProvider} which uses properties files as messages source.
	 * <p>
	 * Properties files are resolved using configured basenames as prefix, and {@link Locale} language, country and
	 * variant separated by an underscore as file name. Files must have <code>.properties</code> extension.
	 * </p>
	 * <p>
	 * The basenames follow {@link java.util.ResourceBundle} conventions: essentially, a fully-qualified classpath
	 * location. If it doesn't contain a package qualifier, it will be resolved from the classpath root. Note that the
	 * JDK's standard ResourceBundle treats dots as package separators: This means that "test.messages" is equivalent to
	 * "test/messages".
	 * </p>
	 * @param basenames Basenames for resource locations
	 * @return PropertiesMessageProviderBuilder
	 */
	static PropertiesMessageProviderBuilder fromProperties(String... basenames) {
		return new PropertiesMessageProvider.Builder(basenames);
	}

	// Builders

	/**
	 * Builder to create {@link PropertiesMessageProvider} instances
	 */
	public interface PropertiesMessageProviderBuilder {

		/**
		 * Add a basename for resource locations.
		 * <p>
		 * Properties files are resolved using configured basenames as prefix, and {@link Locale} language, country and
		 * variant separated by an underscore as file name. Files must have <code>.properties</code> extension.
		 * </p>
		 * <p>
		 * The basenames follow {@link java.util.ResourceBundle} conventions: essentially, a fully-qualified classpath
		 * location. If it doesn't contain a package qualifier, it will be resolved from the classpath root. Note that
		 * the JDK's standard ResourceBundle treats dots as package separators: This means that "test.messages" is
		 * equivalent to "test/messages".
		 * </p>
		 * @param basename Basename to add
		 * @return this
		 */
		PropertiesMessageProviderBuilder basename(String basename);

		/**
		 * Set the charset to use for parsing properties files.
		 * <p>
		 * Default is none, using the {@code java.util.Properties} default encoding: ISO-8859-1.
		 * </p>
		 * @param encoding Properties files encoding chaset
		 * @return this
		 */
		PropertiesMessageProviderBuilder encoding(String encoding);

		/**
		 * Set the ClassLoader to use to load properties files
		 * @param classLoader ClassLoader to use to load properties files
		 * @return this
		 */
		PropertiesMessageProviderBuilder classLoader(ClassLoader classLoader);

		/**
		 * Build {@link MessageProvider}
		 * @return MessageProvider
		 */
		MessageProvider build();

	}

}
