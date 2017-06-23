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

import java.io.Serializable;
import java.util.Locale;
import java.util.Optional;

import com.holonplatform.core.internal.i18n.DefaultLocalization;

/**
 * Localization informations to be used with {@link LocalizationContext} to provide {@link Locale} and optional settings
 * for numbers and dates format.
 * 
 * <p>
 * This interface supports hierarchical localizations through {@link #getParent()}, allowing fallback behaviours when
 * some localization data is not available for given Locale.
 * </p>
 * 
 * @since 5.0.0
 */
public interface Localization extends Serializable {

	/**
	 * Locale to which this localization informations refer
	 * @return Locale (must be not null)
	 */
	Locale getLocale();

	/**
	 * Optional parent Localization for fallback behaviours
	 * @return Parent Localization
	 */
	Optional<Localization> getParent();

	/**
	 * Default decimal position for decimal numbers format
	 * @return Default decimal positions, or an empty Optional to use {@link Locale}'s default
	 */
	Optional<Integer> getDefaultDecimalPositions();

	/**
	 * Default dates format style to use when {@link TemporalFormat#DEFAULT} is specified.
	 * @return Default dates format style
	 */
	Optional<TemporalFormat> getDefaultDateTemporalFormat();

	/**
	 * Default times format style to use when {@link TemporalFormat#DEFAULT} is specified.
	 * @return Default times format style
	 */
	Optional<TemporalFormat> getDefaultTimeTemporalFormat();

	/**
	 * Builder to create {@link Localization} instances
	 * @param locale Locale bound to Localization (not null)
	 * @return LocalizationBuilder
	 */
	static Builder builder(Locale locale) {
		return new DefaultLocalization.DefaultBuilder(locale);
	}

	// Builder

	/**
	 * Builder to create {@link Localization} instances
	 */
	public interface Builder {

		/**
		 * Set parent Localization
		 * @param parent Parent Localization
		 * @return this
		 */
		Builder parent(Localization parent);

		/**
		 * Set default decimal positions for number formats
		 * @param defaultDecimalPositions Default decimal positions
		 * @return this
		 */
		Builder defaultDecimalPositions(int defaultDecimalPositions);

		/**
		 * Set {@link TemporalFormat} to use with dates when {@link TemporalFormat#DEFAULT} is specified
		 * @param defaultDateTemporalFormat Default dates TemporalFormat
		 * @return this
		 */
		Builder defaultDateTemporalFormat(TemporalFormat defaultDateTemporalFormat);

		/**
		 * Set {@link TemporalFormat} to use with times when {@link TemporalFormat#DEFAULT} is specified
		 * @param defaultTimeTemporalFormat Default times TemporalFormat
		 * @return this
		 */
		Builder defaultTimeTemporalFormat(TemporalFormat defaultTimeTemporalFormat);

		/**
		 * Build {@link Localization} instance
		 * @return Localization instance
		 */
		Localization build();

	}

}
