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
package com.holonplatform.core.internal.query;

import java.util.stream.Stream;

import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.Query;
import com.holonplatform.core.query.QueryProjection;

/**
 * {@link Query} implementation that uses a {@link QueryAdapter} to connect concrete query execution environment to
 * generic Query methods.
 * 
 * @param <D> Query definition type
 * 
 * @since 5.0.0
 */
public class QueryAdapterQuery<D extends QueryDefinition> extends AbstractQuery<D> {

	private static final long serialVersionUID = -3458543821787507949L;

	/*
	 * Adapter (immutable)
	 */
	private final QueryAdapter<? super D> queryAdapter;

	/**
	 * Constructor
	 * @param datastore Datastore (not null)
	 * @param queryAdapter Query adapter (not null)
	 * @param queryDefinition Query definition (not null)
	 */
	@SuppressWarnings("unchecked")
	public QueryAdapterQuery(Datastore datastore, QueryAdapter<? super D> queryAdapter, D queryDefinition) {
		super(queryDefinition);
		ObjectUtils.argumentNotNull(datastore, "Datastore must be not null");
		ObjectUtils.argumentNotNull(queryAdapter, "QueryAdapter must be not null");
		this.queryAdapter = queryAdapter;
		// inherit datastore resolvers
		datastore.getExpressionResolvers().forEach(r -> queryDefinition.addExpressionResolver(r));
	}

	/**
	 * Get query adapter
	 * @return Query adapter
	 */
	protected QueryAdapter<? super D> getQueryAdapter() {
		return queryAdapter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.QueryResults#stream(com.holonplatform.core.query.QueryProjection)
	 */
	@Override
	public <R> Stream<R> stream(QueryProjection<R> projection) throws QueryExecutionException {
		try {
			return getQueryAdapter().stream(getQueryDefinition(), projection);
		} catch (QueryExecutionException e) {
			throw e;
		} catch (Exception e) {
			throw new QueryExecutionException(e);
		}
	}

}
