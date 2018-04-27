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
import java.util.stream.Stream;

import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.QueryResults.QueryNonUniqueResultException;

/**
 * Base interface to provide query results using a {@link QueryProjection}.
 * 
 * @param <SR> Actual Stream result type
 * @param <LR> Actual List result type
 * @param <OR> Actual optional single result type
 * @param <CR> Actual count result type
 *
 * @since 5.2.0
 */
@SuppressWarnings("rawtypes")
public interface QueryProjectionOperations<SR, LR, OR, CR> {

	/**
	 * Execute the query and get a {@link Stream} of query results using given <code>projection</code> to map results to
	 * the required type.
	 * @param <R> Query projection (result) type
	 * @param projection Query projection (not null)
	 * @return Query results stream
	 * @throws DataAccessException Error in query execution
	 */
	<R> SR stream(QueryProjection<R> projection);

	/**
	 * Convenience method to obtain the query results {@link #stream(QueryProjection)} as a {@link List}
	 * @param <R> Query projection (result) type
	 * @param projection Query projection (not null)
	 * @return Query results list, an empty List if none
	 * @throws DataAccessException Error in query execution
	 */
	<R> LR list(QueryProjection<R> projection);

	/**
	 * Execute the query and get an expected unique result using given <code>projection</code> to map the result to the
	 * required type.
	 * <p>
	 * If more than one result is returned by the query, a {@link QueryNonUniqueResultException} is thrown.
	 * </p>
	 * @param <R> Result type
	 * @param projection Query projection (not null)
	 * @return Optional unique query result (an empty Optional if no results was returned from query execution)
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 * @throws DataAccessException Error in query execution
	 */
	<R> OR findOne(QueryProjection<R> projection) throws QueryNonUniqueResultException;

	/**
	 * Count all the results of a query.
	 * @return Total results count
	 * @throws DataAccessException Error in query execution
	 */
	CR countAll();

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
	default <P extends Property> SR stream(Iterable<P> properties) {
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
	default <P extends Property> OR findOne(Iterable<P> properties) throws QueryNonUniqueResultException {
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
	default <P extends Property> LR list(Iterable<P> properties) {
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
	default SR stream(Property... properties) {
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
	default OR findOne(Property... properties) throws QueryNonUniqueResultException {
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
	default LR list(Property... properties) {
		return list(PropertySet.of(properties));
	}

}
