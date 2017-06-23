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

import java.text.DateFormat;
import java.time.format.FormatStyle;

/**
 * Enumeration of the format style of a date, time or date-time formatter.
 *
 * @since 5.0.0
 * 
 * @see LocalizationContext
 */
public enum TemporalFormat {

	/**
	 * Default. Use the default display style according to current context.
	 */
	DEFAULT(FormatStyle.SHORT, FormatStyle.SHORT, DateFormat.SHORT, DateFormat.SHORT),

	/**
	 * Short style, typically numeric.
	 */
	SHORT(FormatStyle.SHORT, FormatStyle.SHORT, DateFormat.SHORT, DateFormat.SHORT),

	/**
	 * Medium style, with some detail.
	 */
	MEDIUM(FormatStyle.MEDIUM, FormatStyle.MEDIUM, DateFormat.MEDIUM, DateFormat.MEDIUM),

	/**
	 * Long style, with lots of detail.
	 */
	LONG(FormatStyle.LONG, FormatStyle.MEDIUM, DateFormat.LONG, DateFormat.LONG),

	/**
	 * Full style, with the most detail.
	 */
	FULL(FormatStyle.FULL, FormatStyle.MEDIUM, DateFormat.FULL, DateFormat.FULL);

	// --------------------------------------------------------------------------

	private final FormatStyle dateFormatStyle;
	private final FormatStyle timeFormatStyle;
	private final int dateStyle;
	private final int timeStyle;

	/**
	 * Constructor
	 * @param dateFormatStyle Date {@link FormatStyle}
	 * @param timeFormatStyle Time {@link FormatStyle}
	 * @param dateStyle Date {@link DateFormat} style
	 * @param timeStyle Time {@link DateFormat} style
	 */
	private TemporalFormat(FormatStyle dateFormatStyle, FormatStyle timeFormatStyle, int dateStyle, int timeStyle) {
		this.dateFormatStyle = dateFormatStyle;
		this.timeFormatStyle = timeFormatStyle;
		this.dateStyle = dateStyle;
		this.timeStyle = timeStyle;
	}

	/**
	 * Date {@link FormatStyle}
	 * @return Date {@link FormatStyle}
	 */
	public FormatStyle getDateFormatStyle() {
		return dateFormatStyle;
	}

	/**
	 * Time {@link FormatStyle}
	 * @return Time {@link FormatStyle}
	 */
	public FormatStyle getTimeFormatStyle() {
		return timeFormatStyle;
	}

	/**
	 * Date {@link DateFormat} style
	 * @return Date {@link DateFormat} style
	 */
	public int getDateStyle() {
		return dateStyle;
	}

	/**
	 * Time {@link DateFormat} style
	 * @return Time {@link DateFormat} style
	 */
	public int getTimeStyle() {
		return timeStyle;
	}

}
