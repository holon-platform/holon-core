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
package com.holonplatform.async.datastore.operation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.internal.query.QueryUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.CountAllProjection;
import com.holonplatform.core.query.PropertySetProjection;
import com.holonplatform.core.query.QueryProjection;
import com.holonplatform.core.query.QueryProjectionOperations;
import com.holonplatform.core.query.QueryResults.QueryNonUniqueResultException;

/**
 * Asynchronous {@link QueryProjectionOperations} API, which uses {@link CompletionStage} to provide the query results
 * and handle the results asynchronously.
 * 
 * @since 5.2.0
 * 
 * @see AsyncQuery
 */
@SuppressWarnings("rawtypes")
public interface AsyncQueryResults
		extends QueryProjectionOperations<CompletionStage, CompletionStage, CompletionStage, CompletionStage<Long>> {

	/**
	 * Execute query asynchronously and get a {@link Stream} of query results using given <code>projection</code> to map
	 * the results to the required type.
	 * @param <R> Results type
	 * @param projection Query projection (not null)
	 * @return A {@link CompletionStage} of query results stream, an empty Stream if none
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	<R> CompletionStage<Stream<R>> stream(QueryProjection<R> projection);

	/**
	 * Convenience method to obtain the query results {@link #stream(QueryProjection)} as a {@link List}.
	 * @param <R> Results type
	 * @param projection Query projection (not null)
	 * @return A {@link CompletionStage} of query results list, an empty List if none
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default <R> CompletionStage<List<R>> list(QueryProjection<R> projection) {
		return stream(projection).thenApply(result -> result.collect(Collectors.toList()));
	}

	/**
	 * Execute query asynchronously and get an expected unique result using <code>projection</code> to map result to
	 * required type.
	 * <p>
	 * If more than one result is returned by the query, a {@link QueryNonUniqueResultException} is thrown.
	 * </p>
	 * @param <R> Result type
	 * @param projection Query projection (not null)
	 * @return A {@link CompletionStage} of the Optional unique query result (an empty Optional if no results was
	 *         returned)
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default <R> CompletionStage<Optional<R>> findOne(QueryProjection<R> projection)
			throws QueryNonUniqueResultException {
		return stream(projection).thenApply(
				result -> result.collect(Collectors.collectingAndThen(Collectors.toList(), QueryUtils.uniqueResult())));
	}

	/**
	 * Count all the results of a query, asynchronously.
	 * @return A {@link CompletionStage} of the total results count
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default CompletionStage<Long> countAll() {
		return findOne(CountAllProjection.create()).thenApply(result -> result.orElse(0L));
	}

	/**
	 * Convenience {@link #countAll()} renamed method.
	 * @return A {@link CompletionStage} of the total results count
	 */
	default CompletionStage<Long> count() {
		return countAll();
	}

	/**
	 * Execute the query asynchronously and get a {@link Stream} of query results as {@link PropertyBox} instances,
	 * using given <code>properties</code> as projection.
	 * <p>
	 * The returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param <P> Property type
	 * @param properties Property set to use as projection (not null)
	 * @return A {@link CompletionStage} of the query results {@link Stream} (an empty Stream if none)
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default <P extends Property> CompletionStage<Stream<PropertyBox>> stream(Iterable<P> properties) {
		return stream(PropertySetProjection.of(properties));
	}

	/**
	 * Execute the query asynchronously and get an expected unique result as a {@link PropertyBox} instance, using given
	 * <code>properties</code> as projection.
	 * <p>
	 * The returned {@link PropertyBox} instance will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param <P> Property type
	 * @param properties Property set to use as projection (not null)
	 * @return A {@link CompletionStage} of the Optional unique query result (an empty Optional if no results was
	 *         returned)
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default <P extends Property> CompletionStage<Optional<PropertyBox>> findOne(Iterable<P> properties)
			throws QueryNonUniqueResultException {
		return findOne(PropertySetProjection.of(properties));
	}

	/**
	 * Execute the query asynchronously and get a {@link List} of query results as {@link PropertyBox} instances, using
	 * given <code>properties</code> as projection.
	 * <p>
	 * The returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param <P> Property type
	 * @param properties Property set to use as projection (not null)
	 * @return A {@link CompletionStage} of the query results {@link List} (an empty List if none)
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default <P extends Property> CompletionStage<List<PropertyBox>> list(Iterable<P> properties) {
		return list(PropertySetProjection.of(properties));
	}

	/**
	 * Execute the query asynchronously and get a {@link Stream} of query results as {@link PropertyBox} instances,
	 * using given <code>properties</code> as projection.
	 * <p>
	 * The returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param properties Property set to use as projection (not null)
	 * @return A {@link CompletionStage} of the query results {@link Stream} (an empty Stream if none)
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default CompletionStage<Stream<PropertyBox>> stream(Property... properties) {
		return stream(PropertySet.of(properties));
	}

	/**
	 * Execute the query asynchronously and get an expected unique result as {@link PropertyBox} instance, using given
	 * <code>properties</code> as projection.
	 * <p>
	 * The returned {@link PropertyBox} instance will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param properties Property set to use as projection (not null)
	 * @return A {@link CompletionStage} of the Optional unique query result (an empty Optional if no results was
	 *         returned)
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default CompletionStage<Optional<PropertyBox>> findOne(Property... properties)
			throws QueryNonUniqueResultException {
		return findOne(PropertySet.of(properties));
	}

	/**
	 * Execute the query asynchronously and get a {@link List} of query results as {@link PropertyBox} instances, using
	 * given <code>properties</code> as projection.
	 * <p>
	 * The returned {@link PropertyBox} instances will contain the values of the properties specified in given
	 * <code>properties</code> set.
	 * </p>
	 * @param properties Property set to use as projection (not null)
	 * @return A {@link CompletionStage} of the query results {@link List} (an empty List if none)
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	default CompletionStage<List<PropertyBox>> list(Property... properties) {
		return list(PropertySet.of(properties));
	}

}
