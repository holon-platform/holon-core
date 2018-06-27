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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.query.function.AvgFunction;
import com.holonplatform.core.internal.query.function.CountFunction;
import com.holonplatform.core.internal.query.function.MaxFunction;
import com.holonplatform.core.internal.query.function.MinFunction;
import com.holonplatform.core.internal.query.function.SumFunction;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.query.StringFunction.Lower;
import com.holonplatform.core.query.StringFunction.Upper;
import com.holonplatform.core.query.TemporalFunction.CurrentDate;
import com.holonplatform.core.query.TemporalFunction.CurrentLocalDate;
import com.holonplatform.core.query.TemporalFunction.CurrentLocalDateTime;
import com.holonplatform.core.query.TemporalFunction.CurrentTimestamp;
import com.holonplatform.core.query.TemporalFunction.Day;
import com.holonplatform.core.query.TemporalFunction.Hour;
import com.holonplatform.core.query.TemporalFunction.Month;
import com.holonplatform.core.query.TemporalFunction.Year;

/**
 * Represents a <em>function</em> expression. A function can be used as {@link QueryProjection} too.
 * 
 * @param <T> Function result type
 * @param <A> Function arguments type
 * 
 * @since 5.0.0
 */
public interface QueryFunction<T, A> extends QueryExpression<T>, QueryProjection<T> {

	/**
	 * If the function supports expression arguments, returns the arguments list.
	 * @return Function arguments, an empty list if none
	 */
	List<TypedExpression<? extends A>> getExpressionArguments();

	/**
	 * A {@link QueryFunction} which do not support any argument.
	 *
	 * @param <T> Function result type
	 */
	public interface NoArgQueryFunction<T> extends QueryFunction<T, Void> {

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.query.QueryFunction#getExpressionArguments()
		 */
		@Override
		default List<TypedExpression<? extends Void>> getExpressionArguments() {
			return Collections.emptyList();
		}

	}

	/**
	 * A {@link QueryFunction} which is also a {@link Property}, allowing the function to be used within a property set
	 * query projection and the function value to be collected in a {@link PropertyBox}.
	 * 
	 * @param <T> Function result type
	 * @param <A> Function arguments type
	 * 
	 * @since 5.1.0
	 */
	public interface PropertyQueryFunction<T, A> extends QueryFunction<T, A>, Property<T> {

	}

	// Aggregate function builders

	/**
	 * Creates an aggregate function to count query results.
	 * @param argument Function argument (not null)
	 * @return A new {@link Count} function
	 */
	public static Count count(TypedExpression<?> argument) {
		return Count.create(argument);
	}

	/**
	 * Creates an aggregate function to calculate the average value of a numeric query result.
	 * @param argument Function argument (not null)
	 * @return A new {@link Avg} function
	 */
	public static Avg avg(TypedExpression<? extends Number> argument) {
		return Avg.create(argument);
	}

	/**
	 * Creates an aggregate function to get the smallest value of a query result.
	 * @param <T> Result type
	 * @param argument Function argument (not null)
	 * @return A new {@link Min} function
	 */
	public static <T> Min<T> min(TypedExpression<T> argument) {
		return Min.create(argument);
	}

	/**
	 * Creates an aggregate function to get the largest value of a query result.
	 * @param <T> Result type
	 * @param argument Function argument (not null)
	 * @return A new {@link Max} function
	 */
	public static <T> Max<T> max(TypedExpression<T> argument) {
		return Max.create(argument);
	}

	/**
	 * Creates an aggregate function to sum numeric query results.
	 * @param <T> Result type
	 * @param argument Function argument (not null)
	 * @return A new {@link Sum} function
	 */
	public static <T extends Number> Sum<T> sum(TypedExpression<T> argument) {
		return Sum.create(argument);
	}

	// String function builders

	/**
	 * Creates a function to convert a String data type into lowercase.
	 * @param argument Function argument (not null)
	 * @return A new {@link Lower} function
	 */
	public static Lower lower(TypedExpression<String> argument) {
		return Lower.create(argument);
	}

	/**
	 * Creates a function to convert a String data type into uppercase.
	 * @param argument Function argument (not null)
	 * @return A new {@link Lower} function
	 */
	public static Upper upper(TypedExpression<String> argument) {
		return Upper.create(argument);
	}

	// Current date/time expression builders

	/**
	 * Create a {@link CurrentDate} function to obtain the current date as a {@link Date}.
	 * @return A function expression to obtain the current date as a {@link Date}.
	 */
	public static CurrentDate currentDate() {
		return CurrentDate.create();
	}

	/**
	 * Create a {@link CurrentLocalDate} function to obtain the current date as a {@link LocalDate}.
	 * @return A function expression to obtain the current date as a {@link LocalDate}.
	 */
	public static CurrentLocalDate currentLocalDate() {
		return CurrentLocalDate.create();
	}

	/**
	 * Create a {@link CurrentTimestamp} function to obtain the current timestamp as a {@link Date}.
	 * @return A function expression to obtain the current timestamp
	 */
	public static CurrentTimestamp currentTimestamp() {
		return CurrentTimestamp.create();
	}

	/**
	 * Create a {@link CurrentLocalDateTime} function to obtain the current date and time as a {@link LocalDateTime}.
	 * @return A function expression to obtain the current date and time as a {@link LocalDateTime}.
	 */
	public static CurrentLocalDateTime currentLocalDateTime() {
		return CurrentLocalDateTime.create();
	}

	// Temporal function builders

	/**
	 * Create a function to extract the <em>year</em> part of a temporal data type.
	 * @param argument Function argument (not null)
	 * @return A function to extract the <em>year</em> part of a temporal data type.
	 */
	public static Year year(TypedExpression<?> argument) {
		return Year.create(argument);
	}

	/**
	 * Create a function to extract the <em>month</em> part of a temporal data type.
	 * <p>
	 * The month range index is between 1 and 12.
	 * </p>
	 * @param argument Function argument (not null)
	 * @return A function to extract the <em>month</em> part of a temporal data type.
	 */
	public static Month month(TypedExpression<?> argument) {
		return Month.create(argument);
	}

	/**
	 * Create a function to extract the <em>day</em> part of a temporal data type.
	 * <p>
	 * The day is intended as the day of month and the day range index is between 1 and 31.
	 * </p>
	 * @param argument Function argument (not null)
	 * @return A function to extract the <em>day</em> part of a temporal data type.
	 */
	public static Day day(TypedExpression<?> argument) {
		return Day.create(argument);
	}

	/**
	 * Create a function to extract the <em>hour</em> part of a temporal data type.
	 * <p>
	 * The 24-hour clock is used and the hour range index is between 0 and 23.
	 * </p>
	 * @param argument Function argument (not null)
	 * @return A function to extract the <em>hour</em> part of a temporal data type.
	 */
	public static Hour hour(TypedExpression<?> argument) {
		return Hour.create(argument);
	}

	// -------

	/**
	 * A function which represents the <em>count</em> of a query result values.
	 * <p>
	 * The result type is always a {@link Long}.
	 * </p>
	 */
	public interface Count extends PropertyQueryFunction<Long, Object>, NumericQueryExpression<Long> {

		/**
		 * Create a new {@link Count} function instance.
		 * @param argument Function argument (not null)
		 * @return New {@link Count} function instance
		 */
		static Count create(TypedExpression<?> argument) {
			return new CountFunction(argument);
		}

	}

	/**
	 * A function which represents the <em>smallest value</em> of a query result.
	 * @param <T> Result type
	 */
	public interface Min<T> extends PropertyQueryFunction<T, T> {

		/**
		 * Create a new {@link Min} function instance.
		 * @param <T> Result type
		 * @param argument Function argument (not null)
		 * @return New {@link Min} function instance
		 */
		static <T> Min<T> create(TypedExpression<T> argument) {
			return new MinFunction<>(argument);
		}

	}

	/**
	 * A function which represents the <em>largest value</em> of a query result.
	 * @param <T> Result type
	 */
	public interface Max<T> extends PropertyQueryFunction<T, T> {

		/**
		 * Create a new {@link Max} function instance.
		 * @param <T> Result type
		 * @param argument Function argument (not null)
		 * @return New {@link Max} function instance
		 */
		static <T> Max<T> create(TypedExpression<T> argument) {
			return new MaxFunction<>(argument);
		}

	}

	/**
	 * A function which represents the <em>average value</em> of a query result.
	 * <p>
	 * The result type is always a {@link Double}.
	 * </p>
	 */
	public interface Avg extends PropertyQueryFunction<Double, Number>, NumericQueryExpression<Double> {

		/**
		 * Create a new {@link Avg} function instance.
		 * @param argument Function argument (not null)
		 * @return New {@link Avg} function instance
		 */
		static Avg create(TypedExpression<? extends Number> argument) {
			return new AvgFunction(argument);
		}

	}

	/**
	 * A function which represents the <em>sum</em> of a numeric query result values.
	 * @param <T> Result type
	 */
	public interface Sum<T extends Number> extends PropertyQueryFunction<T, T>, NumericQueryExpression<T> {

		/**
		 * Create a new {@link Sum} function instance.
		 * @param <T> Result type
		 * @param argument Function argument (not null)
		 * @return New {@link Sum} function instance
		 */
		static <T extends Number> Sum<T> create(TypedExpression<T> argument) {
			return new SumFunction<>(argument);
		}

	}

}
