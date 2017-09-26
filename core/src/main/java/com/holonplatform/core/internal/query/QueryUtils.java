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
package com.holonplatform.core.internal.query;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.query.ConstantExpression;
import com.holonplatform.core.query.PropertyConstantExpression;
import com.holonplatform.core.query.Query;
import com.holonplatform.core.query.QueryExpression;
import com.holonplatform.core.query.QueryResults.QueryNonUniqueResultException;
import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.query.QuerySort.CompositeQuerySort;

/**
 * Utility class for {@link Query} management.
 * 
 * @since 5.0.0
 */
public final class QueryUtils implements Serializable {

	private static final long serialVersionUID = -695647607464914028L;

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private QueryUtils() {
	}

	/**
	 * Build a constant {@link QueryExpression} for the given <code>value</code> associated to given
	 * <code>expression</code>.
	 * @param <T> Value type
	 * @param expression Reference expression
	 * @param value Constant value
	 * @return Constant {@link QueryExpression}
	 */
	@SuppressWarnings("unchecked")
	public static <T> QueryExpression<T> asConstantExpression(QueryExpression<T> expression, T value) {
		return (expression instanceof Property) ? PropertyConstantExpression.create((Property<T>) expression, value)
				: ConstantExpression.create(value);
	}

	/**
	 * Build a constant {@link QueryExpression} for the given <code>values</code> associated to given
	 * <code>expression</code>.
	 * @param <T> Value type
	 * @param expression Reference expression
	 * @param values Constant values
	 * @return Constant {@link QueryExpression}
	 */
	@SuppressWarnings("unchecked")
	public static <T> QueryExpression<T> asConstantExpression(QueryExpression<T> expression,
			Collection<? extends T> values) {
		return (expression instanceof Property) ? PropertyConstantExpression.create((Property<T>) expression, values)
				: ConstantExpression.create(values);
	}

	/**
	 * Build a constant {@link QueryExpression} for the given <code>values</code> associated to given
	 * <code>expression</code>.
	 * @param <T> Value type
	 * @param expression Reference expression
	 * @param values Constant values
	 * @return Constant {@link QueryExpression}
	 */
	@SuppressWarnings("unchecked")
	public static <T> QueryExpression<T> asConstantExpression(QueryExpression<T> expression, T... values) {
		return (expression instanceof Property) ? PropertyConstantExpression.create((Property<T>) expression, values)
				: ConstantExpression.create(values);
	}

	/**
	 * Get the model value from given constant <code>expression</code>.
	 * @param <T> Expression type
	 * @param expression Expression (not null)
	 * @return Model value
	 * @throws InvalidExpressionException If the given expression is not a {@link ConstantExpression} or a
	 *         {@link PropertyConstantExpression}
	 */
	public static <T> Object getConstantExpressionValue(QueryExpression<T> expression) {
		ObjectUtils.argumentNotNull(expression, "QueryExpression must be not null");
		if (ConstantExpression.class.isAssignableFrom(expression.getClass())) {
			return ((ConstantExpression<?, ?>) expression).getValue();
		}
		if (PropertyConstantExpression.class.isAssignableFrom(expression.getClass())) {
			return ((PropertyConstantExpression<?, ?>) expression).getModelValue();
		}
		throw new InvalidExpressionException("Expression [" + expression + "] is not a constant expression");
	}

	/**
	 * "flatten" QuerySorts declaration into an ordered List of simple QuerySort, removing any nested
	 * {@link CompositeQuerySort} transforming them in a List of sorts
	 * @param sort QuerySort to flatten
	 * @return Flattened QuerySorts
	 */
	public static List<QuerySort> flattenQuerySort(QuerySort sort) {
		return flattenQuerySorts(Collections.singletonList(sort));
	}

	/**
	 * "flatten" QuerySorts declaration into an ordered List of simple QuerySort
	 * @param sorts QuerySorts
	 * @return Flattened QuerySort
	 */
	private static List<QuerySort> flattenQuerySorts(List<QuerySort> sorts) {
		List<QuerySort> flatten = new LinkedList<>();
		if (sorts != null) {
			for (QuerySort sort : sorts) {
				if (sort instanceof CompositeQuerySort) {
					flatten.addAll(flattenQuerySorts(((CompositeQuerySort) sort).getComposition()));
				} else {
					flatten.add(sort);
				}
			}
		}
		return flatten;
	}

	/**
	 * Turn a query results {@link Iterable} into a {@link Stream}.
	 * @param <T> Results type
	 * @param queryResultsIterable Iterable to convert (not null)
	 * @param closeHandler Optional close handler to perform closing operations when Stream is closed
	 * @return Stream from Iterable
	 */
	public static <T> Stream<T> asResultsStream(Iterable<T> queryResultsIterable, Runnable closeHandler) {
		ObjectUtils.argumentNotNull(queryResultsIterable, "Iterable must be not null");
		Stream<T> stream = StreamSupport.stream(queryResultsIterable.spliterator(), false);
		if (closeHandler != null) {
			stream.onClose(closeHandler);
		}
		return stream;
	}

	/**
	 * Turn a query results {@link Iterator} into a {@link Stream}.
	 * @param <T> Results type
	 * @param queryResultsIterator Iterator to convert (not null)
	 * @param closeHandler Optional close handler to perform closing operations when Stream is closed
	 * @return Stream from Iterator
	 */
	public static <T> Stream<T> asResultsStream(Iterator<T> queryResultsIterator, Runnable closeHandler) {
		ObjectUtils.argumentNotNull(queryResultsIterator, "Iterator must be not null");
		return asResultsStream(() -> queryResultsIterator, closeHandler);
	}

	/**
	 * Turn a query results {@link List} into a {@link Stream}.
	 * @param <T> Results type
	 * @param queryResultsList List to convert
	 * @param closeHandler Optional close handler to perform closing operations when Stream is closed
	 * @return Stream from List
	 */
	public static <T> Stream<T> asResultsStream(List<T> queryResultsList, Runnable closeHandler) {
		List<T> list = (queryResultsList != null) ? queryResultsList : Collections.emptyList();
		Stream<T> stream = list.stream();
		if (closeHandler != null) {
			stream.onClose(closeHandler);
		}
		return stream;
	}

	/**
	 * Get an expected unique result from a List of results.
	 * @param <T> Results type
	 * @return Optional unique results (empty if List was empty)
	 * @throws QueryNonUniqueResultException More than one result is present in results list
	 */
	public static <T> Function<List<T>, Optional<T>> uniqueResult() throws QueryNonUniqueResultException {
		return list -> {
			if (list.size() > 1) {
				throw new QueryNonUniqueResultException("Expected an unique result, but had " + list.size());
			}
			return (list.isEmpty()) ? Optional.empty() : Optional.ofNullable(list.get(0));
		};
	}

}
