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
package com.holonplatform.core.test.data;

import java.util.Collections;
import java.util.stream.Stream;

import com.holonplatform.core.internal.query.AbstractQuery;
import com.holonplatform.core.internal.query.QueryDefinition;
import com.holonplatform.core.query.QueryProjection;

@SuppressWarnings("serial")
public class DummyQuery extends AbstractQuery<QueryDefinition> {

	public DummyQuery(QueryDefinition queryDefinition) {
		super(queryDefinition);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryResults#count()
	 */
	@Override
	public long count() throws QueryExecutionException {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.QueryResults#stream(com.holonplatform.core.query.QueryProjection)
	 */
	@Override
	public <R> Stream<R> stream(QueryProjection<R> projection) throws QueryExecutionException {
		return Collections.<R>emptyList().stream();
	}

}
