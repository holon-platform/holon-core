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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.query.function.CurrentDateFunction;
import com.holonplatform.core.internal.query.function.CurrentLocalDateFunction;
import com.holonplatform.core.internal.query.function.CurrentLocalDateTimeFunction;
import com.holonplatform.core.internal.query.function.CurrentTimestampFunction;
import com.holonplatform.core.internal.query.function.DayFunction;
import com.holonplatform.core.internal.query.function.HourFunction;
import com.holonplatform.core.internal.query.function.MonthFunction;
import com.holonplatform.core.internal.query.function.YearFunction;

/**
 * Represents a temporal-related {@link QueryFunction}.
 * 
 * @param <T> Function result type
 * 
 * @since 5.1.0
 */
public interface TemporalFunction<T> extends QueryFunction<T, Object> {

	/**
	 * A function to obtain the current date as a {@link Date}.
	 */
	public interface CurrentDate extends TemporalFunction<Date>, TemporalQueryExpression<Date> {

		/**
		 * Create a new {@link CurrentDate} function instance.
		 * @return New {@link CurrentDate} function instance
		 */
		static CurrentDate create() {
			return new CurrentDateFunction();
		}

	}

	/**
	 * A function to obtain the current timestamp as a {@link Date}.
	 */
	public interface CurrentTimestamp extends TemporalFunction<Date>, TemporalQueryExpression<Date> {

		/**
		 * Create a new {@link CurrentTimestamp} function instance.
		 * @return New {@link CurrentTimestamp} function instance
		 */
		static CurrentTimestamp create() {
			return new CurrentTimestampFunction();
		}

	}

	/**
	 * A function to obtain the current date as a {@link LocalDate}.
	 */
	public interface CurrentLocalDate extends TemporalFunction<LocalDate>, TemporalQueryExpression<LocalDate> {

		/**
		 * Create a new {@link CurrentLocalDate} function instance.
		 * @return New {@link CurrentLocalDate} function instance
		 */
		static CurrentLocalDate create() {
			return new CurrentLocalDateFunction();
		}

	}

	/**
	 * A function to obtain the current date and time as a {@link LocalDateTime}.
	 */
	public interface CurrentLocalDateTime
			extends TemporalFunction<LocalDateTime>, TemporalQueryExpression<LocalDateTime> {

		/**
		 * Create a new {@link CurrentLocalDateTime} function instance.
		 * @return New {@link CurrentLocalDateTime} function instance
		 */
		static CurrentLocalDateTime create() {
			return new CurrentLocalDateTimeFunction();
		}

	}

	/**
	 * A function to extract the <em>year</em> part of a temporal data type.
	 * <p>
	 * The result type is always an {@link Integer}.
	 * </p>
	 */
	public interface Year
			extends TemporalFunction<Integer>, PropertyQueryFunction<Integer, Object>, NumericQueryExpression<Integer> {

		/**
		 * Create a new {@link Year} function instance.
		 * @param argument Function argument (not null)
		 * @return New {@link Year} function instance
		 */
		static Year create(TypedExpression<?> argument) {
			return new YearFunction(argument);
		}

	}

	/**
	 * A function to extract the <em>month</em> part of a temporal data type.
	 * <p>
	 * The result type is always an {@link Integer} and the month range index is between 1 and 12.
	 * </p>
	 */
	public interface Month
			extends TemporalFunction<Integer>, PropertyQueryFunction<Integer, Object>, NumericQueryExpression<Integer> {

		/**
		 * Create a new {@link Month} function instance.
		 * @param argument Function argument (not null)
		 * @return New {@link Month} function instance
		 */
		static Month create(TypedExpression<?> argument) {
			return new MonthFunction(argument);
		}

	}

	/**
	 * A function to extract the <em>day</em> part of a temporal data type. The day is intended as the day of month.
	 * <p>
	 * The result type is always an {@link Integer} and the day range index is between 1 and 31.
	 * </p>
	 */
	public interface Day
			extends TemporalFunction<Integer>, PropertyQueryFunction<Integer, Object>, NumericQueryExpression<Integer> {

		/**
		 * Create a new {@link Day} function instance.
		 * @param argument Function argument (not null)
		 * @return New {@link Day} function instance
		 */
		static Day create(TypedExpression<?> argument) {
			return new DayFunction(argument);
		}

	}

	/**
	 * A function to extract the <em>hour</em> part of a temporal data type. The 24-hour clock is used.
	 * <p>
	 * The result type is always an {@link Integer} and the hour range index is between 0 and 23.
	 * </p>
	 */
	public interface Hour
			extends TemporalFunction<Integer>, PropertyQueryFunction<Integer, Object>, NumericQueryExpression<Integer> {

		/**
		 * Create a new {@link Hour} function instance.
		 * @param argument Function argument (not null)
		 * @return New {@link Hour} function instance
		 */
		static Hour create(TypedExpression<?> argument) {
			return new HourFunction(argument);
		}

	}

}
