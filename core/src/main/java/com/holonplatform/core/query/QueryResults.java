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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.holonplatform.core.internal.query.QueryUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;

/**
 * Provide operations to get results from a {@link Query} execution.
 * <p>
 * Query results are obtained through a projection using {@link QueryProjection}, allowing results type specification.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see Query
 */
@SuppressWarnings("rawtypes")
public interface QueryResults {

	/**
	 * Execute query and get a {@link Stream} of query results using given <code>projection</code> to map results to
	 * required type.
	 * @param <R> Results type
	 * @param projection Query projection
	 * @return Query results stream
	 * @throws QueryExecutionException Error in query execution
	 */
	<R> Stream<R> stream(QueryProjection<R> projection) throws QueryExecutionException;

	/**
	 * Convenience method to obtain query results {@link #stream(QueryProjection)} as a {@link List}
	 * @param <R> Results type
	 * @param projection Query projection
	 * @return Query results list
	 * @throws QueryExecutionException Error in query execution
	 */
	default <R> List<R> list(QueryProjection<R> projection) throws QueryExecutionException {
		return stream(projection).collect(Collectors.toList());
	}

	/**
	 * Execute query and get an expected unique result using <code>projection</code> to map result to required type.
	 * <p>
	 * If more than one result is returned by the query, a {@link QueryNonUniqueResultException} is thrown.
	 * </p>
	 * @param <R> Result type
	 * @param projection Query projection
	 * @return Optional unique query result (an empty Optional if no results was returned from query execution)
	 * @throws QueryExecutionException Error in query execution
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 */
	default <R> Optional<R> findOne(QueryProjection<R> projection)
			throws QueryExecutionException, QueryNonUniqueResultException {
		return stream(projection).collect(Collectors.collectingAndThen(Collectors.toList(), QueryUtils.uniqueResult()));
	}

	/**
	 * Count all the results of a query.
	 * @return Total results count
	 * @throws QueryExecutionException Error in query execution
	 */
	default long count() throws QueryExecutionException {
		return findOne(CountAllProjection.create()).orElse(0L);
	}

	/**
	 * Execute query and get a {@link Stream} of query results as {@link PropertyBox} using given
	 * <code>properties</code> as projection.
	 * <p>
	 * Returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param <P> Property type
	 * @param properties Property set to fetch
	 * @return Query results {@link PropertyBox} stream
	 * @throws QueryExecutionException Error in query execution
	 */
	default <P extends Property> Stream<PropertyBox> stream(Iterable<P> properties) throws QueryExecutionException {
		return stream(PropertySetProjection.of(properties));
	}

	/**
	 * Execute query and get an expected unique result as {@link PropertyBox} using given <code>properties</code> as
	 * projection.
	 * <p>
	 * Returned {@link PropertyBox} instance will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param <P> Property type
	 * @param properties Property set to fetch
	 * @return Optional unique query result (an empty Optional if no results was returned from query execution)
	 * @throws QueryExecutionException Error in query execution
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 */
	default <P extends Property> Optional<PropertyBox> findOne(Iterable<P> properties)
			throws QueryExecutionException, QueryNonUniqueResultException {
		return findOne(PropertySetProjection.of(properties));
	}

	/**
	 * Execute query and get a {@link List} of query results as {@link PropertyBox} using given <code>properties</code>
	 * as projection.
	 * <p>
	 * Returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param <P> Property type
	 * @param properties Property set to fetch
	 * @return Query results {@link PropertyBox} list
	 * @throws QueryExecutionException Error in query execution
	 */
	default <P extends Property> List<PropertyBox> list(Iterable<P> properties) throws QueryExecutionException {
		return list(PropertySetProjection.of(properties));
	}

	/**
	 * Execute query and get a {@link Stream} of query results as {@link PropertyBox} using given
	 * <code>properties</code> as projection.
	 * <p>
	 * Returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param properties Property set to fetch
	 * @return Query results {@link PropertyBox} stream
	 * @throws QueryExecutionException Error in query execution
	 */
	default Stream<PropertyBox> stream(Property... properties) throws QueryExecutionException {
		return stream(PropertySet.of(properties));
	}

	/**
	 * Execute query and get an expected unique result as {@link PropertyBox} using given <code>properties</code> as
	 * projection.
	 * <p>
	 * Returned {@link PropertyBox} instance will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param properties Property set to fetch
	 * @return Optional unique query result (an empty Optional if no results was returned from query execution)
	 * @throws QueryExecutionException Error in query execution
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 */
	default Optional<PropertyBox> findOne(Property... properties)
			throws QueryExecutionException, QueryNonUniqueResultException {
		return findOne(PropertySet.of(properties));
	}

	/**
	 * Execute query and get a {@link List} of query results as {@link PropertyBox} using given <code>properties</code>
	 * as projection.
	 * <p>
	 * Returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param properties Property set to fetch
	 * @return Query results {@link PropertyBox} list
	 * @throws QueryExecutionException Error in query execution
	 */
	default List<PropertyBox> list(Property... properties) throws QueryExecutionException {
		return list(PropertySet.of(properties));
	}

	/**
	 * Exception thrown for {@link Query} execution errors.
	 */
	@SuppressWarnings("serial")
	public class QueryExecutionException extends RuntimeException {

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public QueryExecutionException(String message) {
			super(message);
		}

		/**
		 * Constructor with nested exception
		 * @param cause Nested exception
		 */
		public QueryExecutionException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public QueryExecutionException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	/**
	 * Exception thrown by when only one query result was expected but more than one found.
	 */
	@SuppressWarnings("serial")
	public class QueryNonUniqueResultException extends RuntimeException {

		/**
		 * Constructor
		 */
		public QueryNonUniqueResultException() {
			super();
		}

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public QueryNonUniqueResultException(String message) {
			super(message);
		}

	}

	/**
	 * Exception thrown for {@link Query} results conversion or parsing errors.
	 */
	@SuppressWarnings("serial")
	public class QueryResultConversionException extends RuntimeException {

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public QueryResultConversionException(String message) {
			super(message);
		}

		/**
		 * Constructor with nested exception
		 * @param cause Nested exception
		 */
		public QueryResultConversionException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public QueryResultConversionException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
