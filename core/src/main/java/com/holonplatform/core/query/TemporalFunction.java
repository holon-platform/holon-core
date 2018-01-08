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
package com.holonplatform.core.query;

import java.util.Date;

import com.holonplatform.core.internal.query.temporal.CurrentDateFunction;
import com.holonplatform.core.internal.query.temporal.CurrentTimestampFunction;
import com.holonplatform.core.internal.query.temporal.DayFunction;
import com.holonplatform.core.internal.query.temporal.HourFunction;
import com.holonplatform.core.internal.query.temporal.MinuteFunction;
import com.holonplatform.core.internal.query.temporal.MonthFunction;
import com.holonplatform.core.internal.query.temporal.SecondFunction;
import com.holonplatform.core.internal.query.temporal.YearFunction;

/**
 * Represents a temporal-related {@link QueryFunction}.
 * 
 * @param <T> Function result type
 * 
 * @since 5.1.0
 */
public interface TemporalFunction<T> extends QueryFunction<T> {

	/**
	 * A function to obtain the current date as a {@link Date}.
	 */
	public interface CurrentDate extends TemporalFunction<Date> {

		/**
		 * Create a new {@link CurrentDate} function instance.
		 * @return New {@link CurrentDate} function instance
		 */
		static CurrentDate create() {
			return new CurrentDateFunction();
		}

	}

	/**
	 * A function to obtain the current timestamp as a {@link Long} number which represents the milliseconds since
	 * January 1, 1970, 00:00:00 GMT (Unix epoch). A negative number is the number of milliseconds before January 1,
	 * 1970, 00:00:00 GMT.
	 */
	public interface CurrentTimestamp extends TemporalFunction<Long> {

		/**
		 * Create a new {@link CurrentTimestamp} function instance.
		 * @return New {@link CurrentTimestamp} function instance
		 */
		static CurrentTimestamp create() {
			return new CurrentTimestampFunction();
		}

	}

	/**
	 * A function to extract the <em>year</em> part of a temporal data type.
	 * <p>
	 * The result type is always an {@link Integer}.
	 * </p>
	 */
	public interface Year extends TemporalFunction<Integer> {

		/**
		 * Create a new {@link Year} function instance.
		 * @return New {@link Year} function instance
		 */
		static Year create() {
			return new YearFunction();
		}

	}

	/**
	 * A function to extract the <em>month</em> part of a temporal data type.
	 * <p>
	 * The result type is always an {@link Integer} and the month range index is between 1 and 12.
	 * </p>
	 */
	public interface Month extends TemporalFunction<Integer> {

		/**
		 * Create a new {@link Month} function instance.
		 * @return New {@link Month} function instance
		 */
		static Month create() {
			return new MonthFunction();
		}

	}

	/**
	 * A function to extract the <em>day</em> part of a temporal data type. The day is intended as the day of month.
	 * <p>
	 * The result type is always an {@link Integer} and the day range index is between 1 and 31.
	 * </p>
	 */
	public interface Day extends TemporalFunction<Integer> {

		/**
		 * Create a new {@link Day} function instance.
		 * @return New {@link Day} function instance
		 */
		static Day create() {
			return new DayFunction();
		}

	}

	/**
	 * A function to extract the <em>hour</em> part of a temporal data type. The 24-hour clock is used.
	 * <p>
	 * The result type is always an {@link Integer} and the hour range index is between 0 and 23.
	 * </p>
	 */
	public interface Hour extends TemporalFunction<Integer> {

		/**
		 * Create a new {@link Hour} function instance.
		 * @return New {@link Hour} function instance
		 */
		static Hour create() {
			return new HourFunction();
		}

	}

	/**
	 * A function to extract the <em>minute</em> part of a temporal data type.
	 * <p>
	 * The result type is always an {@link Integer} and the minute range index is between 0 and 59.
	 * </p>
	 */
	public interface Minute extends TemporalFunction<Integer> {

		/**
		 * Create a new {@link Minute} function instance.
		 * @return New {@link Minute} function instance
		 */
		static Minute create() {
			return new MinuteFunction();
		}

	}

	/**
	 * A function to extract the <em>second</em> part of a temporal data type.
	 * <p>
	 * The result type is always an {@link Integer} and the second range index is between 0 and 59.
	 * </p>
	 */
	public interface Second extends TemporalFunction<Integer> {

		/**
		 * Create a new {@link Second} function instance.
		 * @return New {@link Second} function instance
		 */
		static Second create() {
			return new SecondFunction();
		}

	}

}
