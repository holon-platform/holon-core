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

import java.io.Serializable;
import java.util.List;

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.Path;
import com.holonplatform.core.internal.CallbackExpressionResolver;
import com.holonplatform.core.internal.query.sort.MultiSort;
import com.holonplatform.core.internal.query.sort.Sort;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * A {@link Query} expression representing a sort condition.
 * 
 * @since 4.4.0
 */
public interface QuerySort extends Serializable, Expression {

	/**
	 * Sort direction (ascending or descending) definition
	 */
	public enum SortDirection {

		/**
		 * Ascending (A-Z, 1..9) sort order
		 */
		ASCENDING {
			@Override
			public SortDirection getOpposite() {
				return DESCENDING;
			}
		},

		/**
		 * Descending (Z-A, 9..1) sort order
		 */
		DESCENDING {
			@Override
			public SortDirection getOpposite() {
				return ASCENDING;
			}
		};

		/**
		 * Direct opposite sort direction
		 * @return Opposite sort direction
		 */
		public abstract SortDirection getOpposite();

	}

	/**
	 * Combine this sort with given <code>sort</code>
	 * @param sort Sort to combine
	 * @return A QuerySort using declaring this sort and than the given <code>sort</code>
	 */
	default QuerySort and(QuerySort sort) {
		ObjectUtils.argumentNotNull(sort, "QuerySort to join must be not null");
		return new MultiSort(this, sort);
	}

	/**
	 * Create a QuerySort on given <code>path</code> using {@link SortDirection#ASCENDING} sort direction.
	 * @param <T> Path type
	 * @param path Path by which to sort (not null)
	 * @return QuerySort
	 */
	static <T> QuerySort asc(Path<T> path) {
		return new Sort<>(path, SortDirection.ASCENDING);
	}

	/**
	 * Create a QuerySort on given <code>path</code> using {@link SortDirection#DESCENDING} sort direction.
	 * @param <T> Path type
	 * @param path Path by which to sort (not null)
	 * @return QuerySort
	 */
	static <T> QuerySort desc(Path<T> path) {
		return new Sort<>(path, SortDirection.DESCENDING);
	}

	/**
	 * Create a QuerySort on given <code>path</code> using given sort direction
	 * @param <T> Path type
	 * @param path Path by which to sort (not null)
	 * @param direction Sort direction
	 * @return QuerySort
	 */
	static <T> QuerySort of(Path<T> path, SortDirection direction) {
		return new Sort<>(path, direction);
	}

	/**
	 * Create a QuerySort on given <code>path</code> using given sort direction
	 * @param <T> Path type
	 * @param path Path by which to sort (not null)
	 * @param ascending <code>true</code> for ascending or <code>false</code> for descending
	 * @return QuerySort
	 */
	static <T> QuerySort of(Path<T> path, boolean ascending) {
		return new Sort<>(path, ascending ? SortDirection.ASCENDING : SortDirection.DESCENDING);
	}

	/**
	 * Build a multiple QuerySort using given <code>sorts</code>, in the order they are provided.
	 * @param sorts Sorts (not null)
	 * @return A QuerySort representing a multiple sort on given, ordered, QuerySorts
	 */
	static QuerySort of(QuerySort... sorts) {
		ObjectUtils.argumentNotNull(sorts, "Sort must not be null");
		return (sorts.length == 1) ? sorts[0] : new MultiSort(sorts);
	}

	/**
	 * Build a multiple QuerySort using given <code>sorts</code>, in the order they are provided.
	 * @param <S> Actual sort type
	 * @param sorts Sorts
	 * @return A QuerySort representing a multiple sort on given QuerySorts
	 */
	static <S extends QuerySort> QuerySort of(List<S> sorts) {
		ObjectUtils.argumentNotNull(sorts, "Sort must not be null");
		return (sorts.size() == 1) ? sorts.get(0) : new MultiSort(sorts);
	}

	/**
	 * Interface implemented by classes which support {@link QuerySort}s addition.
	 * @param <C> Concrete type
	 */
	public interface QuerySortSupport<C extends QuerySortSupport<C>> {

		/**
		 * Add a sort clause
		 * @param sort Sort clause to add. If <code>null</code>, the sort clause is ignored.
		 * @return the QuerySortSupport which contains the added sort clause (usually the same instance)
		 */
		C sort(QuerySort sort);

	}

	/**
	 * Convenience interface to create an {@link ExpressionResolver} to resolve a custom {@link QuerySort} class into a
	 * standard {@link QuerySort}.
	 * @param <T> QuerySort type to be resolved
	 */
	public interface QuerySortResolver<T extends QuerySort> extends ExpressionResolver<T, QuerySort> {

		@Override
		default Class<? extends QuerySort> getResolvedType() {
			return QuerySort.class;
		}

		/**
		 * Create an {@link ExpressionResolver} to resolve a custom {@link QuerySort} class into a standard
		 * {@link QuerySort} using the given resolver function.
		 * @param <T> QuerySort type to be resolved
		 * @param type QuerySort type to be resolved
		 * @param function Resolver function
		 * @return A new {@link ExpressionResolver}
		 */
		static <T extends QuerySort> ExpressionResolver<T, QuerySort> create(Class<? extends T> type,
				ExpressionResolverFunction<T, QuerySort> function) {
			return new CallbackExpressionResolver<>(type, QuerySort.class, function);
		}

	}

	/**
	 * A {@link QuerySort} bound to a {@link Path} expression.
	 * @param <T> Query path type
	 */
	public interface PathQuerySort<T> extends QuerySort {

		/**
		 * Get the {@link Path} on which the sort is declared.
		 * @return The sort path
		 */
		Path<T> getPath();

		/**
		 * Get the sort direction (ascending or descending).
		 * @return The sort direction
		 */
		SortDirection getDirection();

	}

	/**
	 * A {@link QuerySort} which represents a sort expressions list.
	 */
	public interface CompositeQuerySort extends QuerySort {

		/**
		 * Get the {@link QuerySort}s composition.
		 * @return QuerySorts list
		 */
		List<QuerySort> getComposition();

	}

}
