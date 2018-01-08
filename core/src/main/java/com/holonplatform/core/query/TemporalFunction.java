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

import com.holonplatform.core.Expression;
import com.holonplatform.core.Path;
import com.holonplatform.core.internal.query.function.CurrentDateFunction;
import com.holonplatform.core.internal.query.function.CurrentLocalDateFunction;
import com.holonplatform.core.internal.query.function.CurrentLocalDateTimeFunction;
import com.holonplatform.core.internal.query.function.CurrentTimestampFunction;
import com.holonplatform.core.internal.query.function.DayFunction;
import com.holonplatform.core.internal.query.function.HourFunction;
import com.holonplatform.core.internal.query.function.MinuteFunction;
import com.holonplatform.core.internal.query.function.MonthFunction;
import com.holonplatform.core.internal.query.function.SecondFunction;
import com.holonplatform.core.internal.query.function.YearFunction;
import com.holonplatform.core.query.FunctionExpression.PathFunctionExpression;
import com.holonplatform.core.query.FunctionExpression.PathFunctionExpressionProperty;

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

		/**
		 * Create a {@link CurrentDate} function expression, which can be used for example as query projection or in
		 * query filters.
		 * @return A {@link CurrentDate} function expression
		 */
		static FunctionExpression<Date> expression() {
			return FunctionExpression.create(create());
		}

	}

	/**
	 * A function to obtain the current timestamp as a {@link Date}.
	 */
	public interface CurrentTimestamp extends TemporalFunction<Date> {

		/**
		 * Create a new {@link CurrentTimestamp} function instance.
		 * @return New {@link CurrentTimestamp} function instance
		 */
		static CurrentTimestamp create() {
			return new CurrentTimestampFunction();
		}

		/**
		 * Create a {@link CurrentTimestamp} function expression, which can be used for example as query projection or
		 * in query filters.
		 * @return A {@link CurrentTimestamp} function expression
		 */
		static FunctionExpression<Date> expression() {
			return FunctionExpression.create(create());
		}

	}

	/**
	 * A function to obtain the current date as a {@link LocalDate}.
	 */
	public interface CurrentLocalDate extends TemporalFunction<LocalDate> {

		/**
		 * Create a new {@link CurrentLocalDate} function instance.
		 * @return New {@link CurrentLocalDate} function instance
		 */
		static CurrentLocalDate create() {
			return new CurrentLocalDateFunction();
		}

		/**
		 * Create a {@link CurrentLocalDate} function expression, which can be used for example as query projection or
		 * in query filters.
		 * @return A {@link CurrentLocalDate} function expression
		 */
		static FunctionExpression<LocalDate> expression() {
			return FunctionExpression.create(create());
		}

	}

	/**
	 * A function to obtain the current date and time as a {@link LocalDateTime}.
	 */
	public interface CurrentLocalDateTime extends TemporalFunction<LocalDateTime> {

		/**
		 * Create a new {@link CurrentLocalDateTime} function instance.
		 * @return New {@link CurrentLocalDateTime} function instance
		 */
		static CurrentLocalDateTime create() {
			return new CurrentLocalDateTimeFunction();
		}

		/**
		 * Create a {@link CurrentLocalDateTime} function expression, which can be used for example as query projection or
		 * in query filters.
		 * @return A {@link CurrentLocalDateTime} function expression
		 */
		static FunctionExpression<LocalDateTime> expression() {
			return FunctionExpression.create(create());
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

		/**
		 * Create a {@link Year} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Year} function expression on given path
		 */
		static <T> PathFunctionExpression<T, Integer> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(), path);
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

		/**
		 * Create a {@link Month} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Month} function expression on given path
		 */
		static <T> PathFunctionExpression<T, Integer> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(), path);
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

		/**
		 * Create a {@link Day} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Day} function expression on given path
		 */
		static <T> PathFunctionExpression<T, Integer> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(), path);
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

		/**
		 * Create a {@link Hour} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Hour} function expression on given path
		 */
		static <T> PathFunctionExpression<T, Integer> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(), path);
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

		/**
		 * Create a {@link Minute} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Minute} function expression on given path
		 */
		static <T> PathFunctionExpression<T, Integer> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(), path);
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

		/**
		 * Create a {@link Second} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Second} function expression on given path
		 */
		static <T> PathFunctionExpression<T, Integer> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(), path);
		}

	}

}
