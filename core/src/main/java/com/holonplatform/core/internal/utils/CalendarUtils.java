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
package com.holonplatform.core.internal.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for {@link Calendar} and {@link Date} types.
 * 
 * @since 5.0.0
 */
public final class CalendarUtils implements Serializable {

	private static final long serialVersionUID = 1849295907464812821L;

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private CalendarUtils() {
	}

	/**
	 * Returns a new {@link Calendar} with the same date of given <code>calendar</code> but with all time fields setted
	 * to <code>0</code> ({@link Calendar#HOUR_OF_DAY}, {@link Calendar#MINUTE}, {@link Calendar#SECOND},
	 * {@link Calendar#MILLISECOND}).
	 * @param calendar Calendar to floor
	 * @return A new Calendar with all time fields setted to <code>0</code>, or <code>null</code> if given Calendar was
	 *         null
	 */
	public static Calendar floorTime(Calendar calendar) {
		if (calendar != null) {
			Calendar c = (Calendar) calendar.clone();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c;
		}
		return calendar;
	}

	/**
	 * Returns a new {@link Calendar} with the same date of given <code>calendar</code> but with all time fields setted
	 * to their max value ({@link Calendar#HOUR_OF_DAY} to 23, {@link Calendar#MINUTE} to 59, {@link Calendar#SECOND} to
	 * 59, {@link Calendar#MILLISECOND} to 999).
	 * @param calendar Calendar to ceil
	 * @return A new Calendar with all time fields setted to their max value, or <code>null</code> if given Calendar was
	 *         null
	 */
	public static Calendar ceilTime(Calendar calendar) {
		if (calendar != null) {
			Calendar c = (Calendar) calendar.clone();
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);
			return c;
		}
		return calendar;
	}

	/**
	 * Returns a new {@link Date} with the same date of given <code>date</code> but with all time fields setted to
	 * <code>0</code> ({@link Calendar#HOUR_OF_DAY}, {@link Calendar#MINUTE}, {@link Calendar#SECOND},
	 * {@link Calendar#MILLISECOND}).
	 * @param date Date to floor
	 * @return A new Date with all time fields setted to <code>0</code>, or <code>null</code> if given Date was null
	 */
	public static Date floorTime(Date date) {
		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTime();
		}
		return date;
	}

	/**
	 * Returns a new {@link Date} with the same date of given <code>date</code> but with all time fields setted to their
	 * max value ({@link Calendar#HOUR_OF_DAY} to 23, {@link Calendar#MINUTE} to 59, {@link Calendar#SECOND} to 59,
	 * {@link Calendar#MILLISECOND} to 999).
	 * @param date Date to ceil
	 * @return A new Date with all time fields setted to their max value, or <code>null</code> if given Date was null
	 */
	public static Date ceilTime(Date date) {
		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);
			return c.getTime();
		}
		return date;
	}

}
