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

import com.holonplatform.core.Expression;
import com.holonplatform.core.internal.query.AvgFunction;
import com.holonplatform.core.internal.query.CountFunction;
import com.holonplatform.core.internal.query.MaxFunction;
import com.holonplatform.core.internal.query.MinFunction;
import com.holonplatform.core.internal.query.SumFunction;

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

	}

}
