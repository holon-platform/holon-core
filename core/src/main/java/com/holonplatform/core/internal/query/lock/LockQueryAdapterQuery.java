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
package com.holonplatform.core.internal.query.lock;

import java.util.concurrent.locks.Lock;
import java.util.stream.Stream;

import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.internal.query.AbstractQueryBuilder;
import com.holonplatform.core.internal.query.QueryDefinition;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryOperation;
import com.holonplatform.core.query.QueryProjection;
import com.holonplatform.core.query.lock.LockMode;
import com.holonplatform.core.query.lock.LockQuery;
import com.holonplatform.core.query.lock.LockQueryAdapter;

/**
 * {@link LockQuery} implemenation which uses a {@link LockQueryAdapter} to perform the actual operations.
 *
 * @since 5.2.0
 */
public class LockQueryAdapterQuery<D extends QueryDefinition> extends AbstractQueryBuilder<LockQuery, D>
		implements LockQuery {

	private static final long serialVersionUID = -4949926456079781824L;

	/**
	 * Lock mode qury configuration parameter
	 */
	public static final ConfigProperty<LockMode> LOCK_MODE = ConfigProperty.create(Lock.class.getName() + "$LOCK_MODE",
			LockMode.class);

	/**
	 * Lock timeout qury configuration parameter
	 */
	public static final ConfigProperty<Long> LOCK_TIMEOUT = ConfigProperty
			.create(Lock.class.getName() + "$LOCK_TIMEOUT", Long.class);

	private final LockQueryAdapter<? super D> queryAdapter;

	/**
	 * Constructor.
	 * @param queryAdapter Query adapter (not null)
	 * @param queryDefinition Query definition (not null)
	 */
	public LockQueryAdapterQuery(LockQueryAdapter<? super D> queryAdapter, D queryDefinition) {
		super(queryDefinition);
		ObjectUtils.argumentNotNull(queryAdapter, "QueryAdapter must be not null");
		this.queryAdapter = queryAdapter;
	}

	/**
	 * Get the query adapter.
	 * @return the query adapter
	 */
	protected LockQueryAdapter<? super D> getQueryAdapter() {
		return queryAdapter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryResults#stream(com.holonplatform.core.query.QueryProjection)
	 */
	@Override
	public <R> Stream<R> stream(QueryProjection<R> projection) {
		try {
			return getQueryAdapter().stream(QueryOperation.create(getQueryDefinition(), projection));
		} catch (DataAccessException e) {
			throw e;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.lock.LockSupport#tryLock(com.holonplatform.core.query.lock.Lock.LockMode, long)
	 */
	@Override
	public boolean tryLock(LockMode lockMode, long timeout) {
		lock(lockMode, timeout);
		try {
			return getQueryAdapter().tryLock(getQueryConfiguration());
		} catch (DataAccessException e) {
			throw e;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.lock.LockSupport#lock(com.holonplatform.core.query.lock.Lock.LockMode, long)
	 */
	@Override
	public LockQuery lock(LockMode lockMode, long timeout) {
		ObjectUtils.argumentNotNull(lockMode, "Lock mode must be not null");
		LockQuery builder = parameter(LOCK_MODE, lockMode);
		if (timeout > -1) {
			builder = builder.parameter(LOCK_TIMEOUT, timeout);
		}
		return builder;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.AbstractQueryBuilder#getActualBuilder()
	 */
	@Override
	protected LockQuery getActualBuilder() {
		return this;
	}

}
