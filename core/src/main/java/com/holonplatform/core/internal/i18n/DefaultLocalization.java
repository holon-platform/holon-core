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

import java.util.Locale;
import java.util.Optional;

import com.holonplatform.core.i18n.Localization;
import com.holonplatform.core.i18n.TemporalFormat;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link Localization} implementation
 *
 * @since 5.0.0
 */
public class DefaultLocalization implements Localization {

	private static final long serialVersionUID = -1127664569575856316L;

	/*
	 * Locale
	 */
	private final Locale locale;

	/*
	 * Parent Localization for fallback
	 */
	private Localization parent;
	/*
	 * Decimal positions
	 */
	private Integer defaultDecimalPositions;
	/*
	 * Default date format style
	 */
	private TemporalFormat defaultDateTemporalFormat;
	/*
	 * Default time format style
	 */
	private TemporalFormat defaultTimeTemporalFormat;

	/**
	 * Constructor
	 * @param locale Locale
	 */
	public DefaultLocalization(Locale locale) {
		super();

		ObjectUtils.argumentNotNull(locale, "Locale must be not null");

		this.locale = locale;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localization#getLocale()
	 */
	@Override
	public Locale getLocale() {
		return locale;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localization#getParent()
	 */
	@Override
	public Optional<Localization> getParent() {
		return Optional.ofNullable(parent);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localization#getDefaultDecimalPositions()
	 */
	@Override
	public Optional<Integer> getDefaultDecimalPositions() {
		return Optional.ofNullable(defaultDecimalPositions);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localization#getDefaultDateTemporalFormat()
	 */
	@Override
	public Optional<TemporalFormat> getDefaultDateTemporalFormat() {
		return Optional.ofNullable(defaultDateTemporalFormat);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localization#getDefaultTimeTemporalFormat()
	 */
	@Override
	public Optional<TemporalFormat> getDefaultTimeTemporalFormat() {
		return Optional.ofNullable(defaultTimeTemporalFormat);
	}

	/**
	 * Set parent Localization
	 * @param parent the parent to set
	 */
	public void setParent(Localization parent) {
		this.parent = parent;
	}

	/**
	 * Set default decimal positions for numbers format
	 * @param defaultDecimalPositions default decimal positions
	 */
	public void setDefaultDecimalPositions(int defaultDecimalPositions) {
		this.defaultDecimalPositions = defaultDecimalPositions;
	}

	/**
	 * Set {@link TemporalFormat} to use with dates when {@link TemporalFormat#DEFAULT} is specified
	 * @param defaultDateTemporalFormat Default dates TemporalFormat
	 */
	public void setDefaultDateTemporalFormat(TemporalFormat defaultDateTemporalFormat) {
		this.defaultDateTemporalFormat = defaultDateTemporalFormat;
	}

	/**
	 * Set {@link TemporalFormat} to use with times when {@link TemporalFormat#DEFAULT} is specified
	 * @param defaultTimeTemporalFormat Default times TemporalFormat
	 */
	public void setDefaultTimeTemporalFormat(TemporalFormat defaultTimeTemporalFormat) {
		this.defaultTimeTemporalFormat = defaultTimeTemporalFormat;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultLocalization [locale=" + locale + ", parent=" + parent + ", defaultDecimalPositions="
				+ defaultDecimalPositions + ", defaultDateTemporalFormat=" + defaultDateTemporalFormat
				+ ", defaultTimeTemporalFormat=" + defaultTimeTemporalFormat + "]";
	}

	/**
	 * Default {@link Builder} implementation
	 */
	public static class DefaultBuilder implements Builder {

		private final DefaultLocalization localization;

		/**
		 * Constructor
		 * @param locale Locale
		 */
		public DefaultBuilder(Locale locale) {
			super();
			this.localization = new DefaultLocalization(locale);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.i18n.Builder#parent(com.holonplatform.core.i18n.Localization)
		 */
		@Override
		public Builder parent(Localization parent) {
			localization.setParent(parent);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.i18n.Builder#groupingSeparator(char)
		 */
		@Override
		public Builder defaultDecimalPositions(int defaultDecimalPositions) {
			localization.setDefaultDecimalPositions(defaultDecimalPositions);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Localization.Builder#defaultDateTemporalFormat(com.holonplatform.core.i18n.
		 * TemporalFormat)
		 */
		@Override
		public Builder defaultDateTemporalFormat(TemporalFormat defaultDateTemporalFormat) {
			localization.setDefaultDateTemporalFormat(defaultDateTemporalFormat);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Localization.Builder#defaultTimeTemporalFormat(com.holonplatform.core.i18n.
		 * TemporalFormat)
		 */
		@Override
		public Builder defaultTimeTemporalFormat(TemporalFormat defaultTimeTemporalFormat) {
			localization.setDefaultTimeTemporalFormat(defaultTimeTemporalFormat);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.i18n.Builder#build()
		 */
		@Override
		public Localization build() {
			return localization;
		}

	}

}
