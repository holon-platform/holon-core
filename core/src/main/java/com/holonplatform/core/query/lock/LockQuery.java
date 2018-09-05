/*
 * Copyright 2016-2018 Axioma srl.
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
package com.holonplatform.core.query.lock;

import java.util.stream.Stream;

import com.holonplatform.core.datastore.DatastoreCommodity;
import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.query.QueryBuilder;
import com.holonplatform.core.query.QueryProjection;
import com.holonplatform.core.query.QueryResults;

/**
 * A query with locking support, which is available through the {@link LockSupport} API.
 *
 * @since 5.2.0
 * 
 * @see LockSupport
 */
public interface LockQuery extends QueryBuilder<LockQuery>, QueryResults, DatastoreCommodity, LockSupport<LockQuery> {

	/**
	 * Execute query and get a {@link Stream} of query results using given <code>projection</code> to map results to
	 * required type.
	 * @param <R> Results type
	 * @param projection Query projection (not null)
	 * @return Query results stream, an empty Stream if none
	 * @throws LockAcquisitionException If a lock mode was configured and the lock can't be acquired
	 * @throws DataAccessException Error in query execution
	 */
	@Override
	<R> Stream<R> stream(QueryProjection<R> projection);

}
