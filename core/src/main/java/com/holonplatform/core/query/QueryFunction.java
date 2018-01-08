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

import com.holonplatform.core.Expression;
import com.holonplatform.core.Path;
import com.holonplatform.core.internal.query.function.AvgFunction;
import com.holonplatform.core.internal.query.function.CountFunction;
import com.holonplatform.core.internal.query.function.MaxFunction;
import com.holonplatform.core.internal.query.function.MinFunction;
import com.holonplatform.core.internal.query.function.SumFunction;
import com.holonplatform.core.query.FunctionExpression.PathFunctionExpression;
import com.holonplatform.core.query.FunctionExpression.PathFunctionExpressionProperty;
import com.holonplatform.core.query.StringFunction.Lower;
import com.holonplatform.core.query.StringFunction.Upper;
import com.holonplatform.core.query.TemporalFunction.CurrentDate;
import com.holonplatform.core.query.TemporalFunction.CurrentTimestamp;
import com.holonplatform.core.query.TemporalFunction.Day;
import com.holonplatform.core.query.TemporalFunction.Hour;
import com.holonplatform.core.query.TemporalFunction.Minute;
import com.holonplatform.core.query.TemporalFunction.Month;
import com.holonplatform.core.query.TemporalFunction.Second;
import com.holonplatform.core.query.TemporalFunction.Year;

/**
 * Represents a generic <em>function</em> to be used in a {@link Query} definition.
 * 
 * @param <T> Function result type
 * 
 * @since 5.0.0
 */
public interface QueryFunction<T> extends Expression {

	/**
	 * Get the function result type.
	 * @return Function result type
	 */
	Class<? extends T> getResultType();

	// Aggregate function builders

	/**
	 * Creates an aggregate function to count query results.
	 * @return A new {@link Count} function
	 */
	public static Count count() {
		return Count.create();
	}

	/**
	 * Creates an aggregate function to calculate the average value of a query result.
	 * @return A new {@link Avg} function
	 */
	public static Avg avg() {
		return Avg.create();
	}

	/**
	 * Creates an aggregate function to get the smallest value of a query result.
	 * @param <T> Result type
	 * @param resultType Function and query result type
	 * @return A new {@link Min} function
	 */
	public static <T> Min<T> min(Class<? extends T> resultType) {
		return Min.create(resultType);
	}

	/**
	 * Creates an aggregate function to get the largest value of a query result.
	 * @param <T> Result type
	 * @param resultType Function and query result type
	 * @return A new {@link Max} function
	 */
	public static <T> Max<T> max(Class<? extends T> resultType) {
		return Max.create(resultType);
	}

	/**
	 * Creates an aggregate function to sum query results.
	 * @param <T> Result type
	 * @param resultType Function and query result type
	 * @return A new {@link Sum} function
	 */
	public static <T> Sum<T> sum(Class<? extends T> resultType) {
		return Sum.create(resultType);
	}

	// String function builders

	/**
	 * Creates a function to convert a String data type into lowercase.
	 * @return A new {@link Lower} function
	 */
	public static Lower lower() {
		return Lower.create();
	}

	/**
	 * Creates a function to convert a String data type into uppercase.
	 * @return A new {@link Lower} function
	 */
	public static Upper upper() {
		return Upper.create();
	}

	// Current date/time expression builders

	/**
	 * Create a {@link FunctionExpression} to obtain the current date as a {@link Date}.
	 * @return A function expression to obtain the current date as a {@link Date}.
	 */
	public static FunctionExpression<Date> currentDate() {
		return CurrentDate.expression();
	}

	/**
	 * Create a {@link FunctionExpression} to obtain the current timestamp as a {@link Long} number which represents the
	 * milliseconds since January 1, 1970, 00:00:00 GMT (Unix epoch). A negative number is the number of milliseconds
	 * before January 1, 1970, 00:00:00 GMT.
	 * @return A function expression to obtain the current timestamp
	 */
	public static FunctionExpression<Long> currentTimestamp() {
		return CurrentTimestamp.expression();
	}

	// Temporal function builders

	/**
	 * Create a function to extract the <em>year</em> part of a temporal data type.
	 * @return A function to extract the <em>year</em> part of a temporal data type.
	 */
	public static Year year() {
		return Year.create();
	}

	/**
	 * Create a function to extract the <em>month</em> part of a temporal data type.
	 * <p>
	 * The month range index is between 1 and 12.
	 * </p>
	 * @return A function to extract the <em>month</em> part of a temporal data type.
	 */
	public static Month month() {
		return Month.create();
	}

	/**
	 * Create a function to extract the <em>day</em> part of a temporal data type.
	 * <p>
	 * The day is intended as the day of month and the day range index is between 1 and 31.
	 * </p>
	 * @return A function to extract the <em>day</em> part of a temporal data type.
	 */
	public static Day day() {
		return Day.create();
	}

	/**
	 * Create a function to extract the <em>hour</em> part of a temporal data type.
	 * <p>
	 * The 24-hour clock is used and the hour range index is between 0 and 23.
	 * </p>
	 * @return A function to extract the <em>hour</em> part of a temporal data type.
	 */
	public static Hour hour() {
		return Hour.create();
	}

	/**
	 * Create a function to extract the <em>minute</em> part of a temporal data type.
	 * <p>
	 * The minute range index is between 0 and 59.
	 * </p>
	 * @return A function to extract the <em>minute</em> part of a temporal data type.
	 */
	public static Minute minute() {
		return Minute.create();
	}

	/**
	 * Create a function to extract the <em>second</em> part of a temporal data type.
	 * <p>
	 * The second range index is between 0 and 59.
	 * </p>
	 * @return A function to extract the <em>second</em> part of a temporal data type.
	 */
	public static Second second() {
		return Second.create();
	}

	// -------

	/**
	 * A function which represents the <em>count</em> of a query result values.
	 * <p>
	 * The result type is always a {@link Long}.
	 * </p>
	 */
	public interface Count extends QueryFunction<Long> {

		/**
		 * Create a new {@link Count} function instance.
		 * @return New {@link Count} function instance
		 */
		static Count create() {
			return new CountFunction();
		}

		/**
		 * Create a {@link Count} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Count} function expression on given path
		 */
		static <T> PathFunctionExpression<T, Long> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(), path);
		}

	}

	/**
	 * A function which represents the <em>average value</em> of a query result.
	 * <p>
	 * The result type is always a {@link Double}.
	 * </p>
	 */
	public interface Avg extends QueryFunction<Double> {

		/**
		 * Create a new {@link Avg} function instance.
		 * @return New {@link Avg} function instance
		 */
		static Avg create() {
			return new AvgFunction();
		}

		/**
		 * Create a {@link Avg} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Avg} function expression on given path
		 */
		static <T> PathFunctionExpression<T, Double> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(), path);
		}

	}

	/**
	 * A function which represents the <em>smallest value</em> of a query result.
	 * @param <T> Result type
	 */
	public interface Min<T> extends QueryFunction<T> {

		/**
		 * Create a new {@link Min} function instance.
		 * @param <T> Result type
		 * @param resultType Function and query result type
		 * @return New {@link Min} function instance
		 */
		static <T> Min<T> create(Class<? extends T> resultType) {
			return new MinFunction<>(resultType);
		}

		/**
		 * Create a {@link Min} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Min} function expression on given path
		 */
		static <T> PathFunctionExpression<T, T> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(path.getType()), path);
		}

	}

	/**
	 * A function which represents the <em>largest value</em> of a query result.
	 * @param <T> Result type
	 */
	public interface Max<T> extends QueryFunction<T> {

		/**
		 * Create a new {@link Max} function instance.
		 * @param <T> Result type
		 * @param resultType Function and query result type
		 * @return New {@link Max} function instance
		 */
		static <T> Max<T> create(Class<? extends T> resultType) {
			return new MaxFunction<>(resultType);
		}

		/**
		 * Create a {@link Max} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Max} function expression on given path
		 */
		static <T> PathFunctionExpression<T, T> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(path.getType()), path);
		}

	}

	/**
	 * A function which represents the <em>sum</em> of a query result values.
	 * @param <T> Result type
	 */
	public interface Sum<T> extends QueryFunction<T> {

		/**
		 * Create a new {@link Sum} function instance.
		 * @param <T> Result type
		 * @param resultType Function and query result type
		 * @return New {@link Sum} function instance
		 */
		static <T> Sum<T> create(Class<? extends T> resultType) {
			return new SumFunction<>(resultType);
		}

		/**
		 * Create a {@link Sum} function {@link Expression} using given <code>path</code> as function argument.
		 * @param <T> Path type
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Sum} function expression on given path
		 */
		static <T> PathFunctionExpression<T, T> of(Path<T> path) {
			return PathFunctionExpressionProperty.create(create(path.getType()), path);
		}

	}

}
