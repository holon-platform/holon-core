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
package com.holonplatform.core.query;

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.query.DefaultFunctionExpression;
import com.holonplatform.core.internal.query.DefaultFunctionExpressionProperty;
import com.holonplatform.core.internal.query.DefaultPathFunctionExpressionProperty;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.VirtualProperty;
import com.holonplatform.core.query.QueryFunction.Avg;
import com.holonplatform.core.query.QueryFunction.Count;
import com.holonplatform.core.query.QueryFunction.Max;
import com.holonplatform.core.query.QueryFunction.Min;
import com.holonplatform.core.query.QueryFunction.Sum;

/**
 * A {@link QueryExpression} wich represents the result of a {@link QueryFunction} a can be used as
 * {@link QueryProjection}.
 * 
 * @param <T> Function result, expression and projection type
 *
 * @since 5.0.0
 */
public interface FunctionExpression<T> extends QueryExpression<T>, QueryProjection<T> {

	/**
	 * Get the {@link QueryFunction} associated with this expression.
	 * @return Expression function
	 */
	QueryFunction<T> getFunction();

	/**
	 * Create a new {@link FunctionExpression} using given <code>function</code>.
	 * @param <T> Function type
	 * @param function Expression function (not null)
	 * @return A new {@link FunctionExpression} associated with given query function
	 */
	static <T> FunctionExpression<T> create(QueryFunction<T> function) {
		return new DefaultFunctionExpression<>(function);
	}

	/**
	 * A {@link FunctionExpression} which acts on a {@link Path}.
	 * @param <P> Path type
	 * @param <T> Function result, expression and projection type
	 */
	public interface PathFunctionExpression<P, T> extends FunctionExpression<T> {

		/**
		 * Get the {@link Path} subject of the function.
		 * @return The function path
		 */
		Path<P> getPath();

	}

	// Path aggregation functions

	/**
	 * Create a function expression which represents the {@link Count} aggregation function on given <code>path</code>.
	 * <p>
	 * With the <em>count</em> aggregation function, the query expression and projection type is of {@link Long} type.
	 * </p>
	 * @param <T> Path type
	 * @param path Path to which to apply the function
	 * @return A new {@link PathFunctionExpression}
	 */
	static <T> PathFunctionExpression<T, Long> count(Path<T> path) {
		return PathFunctionExpressionProperty.create(Count.create(), path);
	}

	/**
	 * Create a function expression which represents the {@link Avg} aggregation function on given <code>path</code>.
	 * <p>
	 * With the <em>avg</em> aggregation function, the query expression and projection type is of {@link Double} type.
	 * </p>
	 * @param <T> Path type
	 * @param path Path to which to apply the function
	 * @return A new {@link PathFunctionExpression}
	 */
	static <T> PathFunctionExpression<T, Double> avg(Path<T> path) {
		return PathFunctionExpressionProperty.create(Avg.create(), path);
	}

	/**
	 * Create a function expression which represents the {@link Min} aggregation function on given <code>path</code>.
	 * @param <T> Path type
	 * @param path Path to which to apply the function
	 * @return A new {@link PathFunctionExpression}
	 */
	static <T> PathFunctionExpression<T, T> min(Path<T> path) {
		return PathFunctionExpressionProperty.create(Min.create(path.getType()), path);
	}

	/**
	 * Create a function expression which represents the {@link Max} aggregation function on given <code>path</code>.
	 * @param <T> Path type
	 * @param path Path to which to apply the function
	 * @return A new {@link PathFunctionExpression}
	 */
	static <T> PathFunctionExpression<T, T> max(Path<T> path) {
		return PathFunctionExpressionProperty.create(Max.create(path.getType()), path);
	}

	/**
	 * Create a function expression which represents the {@link Max} aggregation function on given <code>path</code>.
	 * @param <T> Path type
	 * @param path Path to which to apply the function
	 * @return A new {@link PathFunctionExpression}
	 */
	static <T> PathFunctionExpression<T, T> sum(Path<T> path) {
		return PathFunctionExpressionProperty.create(Sum.create(path.getType()), path);
	}

	// Property support

	/**
	 * A {@link FunctionExpression} implemented as a {@link Property}.
	 * @param <T> Property and function type
	 */
	public interface FunctionExpressionProperty<T> extends FunctionExpression<T>, Property<T> {

		/**
		 * Create a {@link FunctionExpressionProperty} using given <code>function</code>.
		 * @param <T> Property and function type
		 * @param function Function associated to the property (not null)
		 * @return A new {@link FunctionExpressionProperty}
		 */
		static <T> FunctionExpressionProperty<T> create(QueryFunction<T> function) {
			ObjectUtils.argumentNotNull(function, "Function must be not null");
			return new DefaultFunctionExpressionProperty<>(function);
		}

	}

	/**
	 * A {@link PathFunctionExpression} implemented as a {@link VirtualProperty}.
	 * @param <P> Path type
	 * @param <T> Property and function type
	 */
	public interface PathFunctionExpressionProperty<P, T>
			extends FunctionExpressionProperty<T>, PathFunctionExpression<P, T> {

		/**
		 * Create a new {@link PathFunctionExpressionProperty} on given <code>path</code> using given
		 * <code>function</code>.
		 * @param <P> Path type
		 * @param <T> Property and function type
		 * @param function Function associated to the property (not null)
		 * @param path Path subject of the function (not null)
		 * @return A new {@link PathFunctionExpressionProperty}
		 */
		static <P, T> PathFunctionExpressionProperty<P, T> create(QueryFunction<T> function, Path<P> path) {
			ObjectUtils.argumentNotNull(function, "Function must be not null");
			ObjectUtils.argumentNotNull(path, "Path must be not null");
			return new DefaultPathFunctionExpressionProperty<>(function, path);
		}

		/**
		 * Create a new {@link PathFunctionExpressionProperty} on given <code>property</code> using given
		 * <code>function</code>.
		 * @param <P> Path type
		 * @param <T> Property and function type
		 * @param function Function associated to the property (not null)
		 * @param property Property subject of the function (not null)
		 * @return A new {@link PathFunctionExpressionProperty}
		 */
		static <P, T> PathFunctionExpressionProperty<P, T> create(QueryFunction<T> function, PathProperty<P> property) {
			ObjectUtils.argumentNotNull(function, "Function must be not null");
			ObjectUtils.argumentNotNull(property, "Property must be not null");
			return new DefaultPathFunctionExpressionProperty<>(function, property);
		}

	}

}
