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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.internal.query.QueryUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;

/**
 * Provide operations to get the results of a {@link Query} operation using a {@link QueryProjection}.
 * 
 * @since 5.0.0
 * 
 * @see Query
 */
@SuppressWarnings("rawtypes")
public interface QueryResults extends QueryProjectionOperations<Stream, List, Optional, Long> {

	/**
	 * Execute query and get a {@link Stream} of query results using given <code>projection</code> to map results to
	 * required type.
	 * @param <R> Results type
	 * @param projection Query projection (not null)
	 * @return Query results stream, an empty Stream if none
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	<R> Stream<R> stream(QueryProjection<R> projection);

	/**
	 * Convenience method to obtain the query results {@link #stream(QueryProjection)} as a {@link List}
	 * @param <R> Results type
	 * @param projection Query projection (not null)
	 * @return Query results list, an empty List if none
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default <R> List<R> list(QueryProjection<R> projection) {
		return stream(projection).collect(Collectors.toList());
	}

	/**
	 * Execute query and get an expected unique result using <code>projection</code> to map result to required type.
	 * <p>
	 * If more than one result is returned by the query, a {@link QueryNonUniqueResultException} is thrown.
	 * </p>
	 * @param <R> Result type
	 * @param projection Query projection (not null)
	 * @return Optional unique query result (an empty Optional if no results was returned from query execution)
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default <R> Optional<R> findOne(QueryProjection<R> projection) throws QueryNonUniqueResultException {
		return stream(projection).collect(Collectors.collectingAndThen(Collectors.toList(), QueryUtils.uniqueResult()));
	}

	/**
	 * Count all the results of a query.
	 * @return Total results count, an empty Optional if none
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default Long countAll() {
		return findOne(CountAllProjection.create()).orElse(0L);
	}

	/**
	 * Count all the results of a query.
	 * <p>
	 * This is {@link #countAll()} alternative convenience method to provide a primitive long type result.
	 * </p>
	 * @return Total results count
	 * @throws DataAccessException Error in query execution
	 */
	default long count() {
		final Long count = countAll();
		return (count != null) ? count : 0L;
	}

	/**
	 * Execute query and get a {@link Stream} of query results as {@link PropertyBox} using given
	 * <code>properties</code> as projection.
	 * <p>
	 * Returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param <P> Property type
	 * @param properties Property set to use as projection (not null)
	 * @return Query results {@link PropertyBox} stream
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default <P extends Property> Stream<PropertyBox> stream(Iterable<P> properties) {
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
	 * @param properties Property set to use as projection (not null)
	 * @return Optional unique query result (an empty Optional if no results was returned from query execution)
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default <P extends Property> Optional<PropertyBox> findOne(Iterable<P> properties)
			throws QueryNonUniqueResultException {
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
	 * @param properties Property set to use as projection (not null)
	 * @return Query results {@link PropertyBox} list
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default <P extends Property> List<PropertyBox> list(Iterable<P> properties) {
		return list(PropertySetProjection.of(properties));
	}

	/**
	 * Execute query and get a {@link Stream} of query results as {@link PropertyBox} using given
	 * <code>properties</code> as projection.
	 * <p>
	 * Returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param properties Property set to use as projection (not null)
	 * @return Query results {@link PropertyBox} stream
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default Stream<PropertyBox> stream(Property... properties) {
		return stream(PropertySet.of(properties));
	}

	/**
	 * Execute query and get an expected unique result as {@link PropertyBox} using given <code>properties</code> as
	 * projection.
	 * <p>
	 * Returned {@link PropertyBox} instance will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param properties Property set to use as projection (not null)
	 * @return Optional unique query result (an empty Optional if no results was returned from query execution)
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default Optional<PropertyBox> findOne(Property... properties) throws QueryNonUniqueResultException {
		return findOne(PropertySet.of(properties));
	}

	/**
	 * Execute query and get a {@link List} of query results as {@link PropertyBox} using given <code>properties</code>
	 * as projection.
	 * <p>
	 * Returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param properties Property set to use as projection (not null)
	 * @return Query results {@link PropertyBox} list
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default List<PropertyBox> list(Property... properties) {
		return list(PropertySet.of(properties));
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

}
