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
package com.holonplatform.core.datastore.beans;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.internal.query.QueryUtils;
import com.holonplatform.core.query.QueryResults.QueryNonUniqueResultException;

/**
 * A query which uses a bean class for query composition and projections.
 * 
 * @param <T> Bean type
 *
 * @since 5.1.0
 */
public interface BeanQuery<T> extends BeanQueryBuilder<BeanQuery<T>> {

	/**
	 * Execute query and get a {@link Stream} of bean query results.
	 * @return Query results stream
	 * @throws DataAccessException Error in query execution
	 */
	Stream<T> stream();

	/**
	 * Convenience method to obtain the query results {@link #stream()} as a {@link List}.
	 * @return Query results list
	 * @throws DataAccessException Error in query execution
	 */
	default List<T> list() {
		return stream().collect(Collectors.toList());
	}

	/**
	 * Execute query and get an expected unique result bean.
	 * <p>
	 * If more than one result is returned by the query, a {@link QueryNonUniqueResultException} is thrown.
	 * </p>
	 * @return Optional unique query result (an empty Optional if no results was returned from query execution)
	 * @throws QueryNonUniqueResultException Only one result expected but more than one was found
	 * @throws DataAccessException Error in query execution
	 */
	default Optional<T> findOne() throws QueryNonUniqueResultException {
		return stream().collect(Collectors.collectingAndThen(Collectors.toList(), QueryUtils.uniqueResult()));
	}

	/**
	 * Count all the results of a query.
	 * @return Total results count
	 * @throws DataAccessException Error in query execution
	 */
	long count();

}
