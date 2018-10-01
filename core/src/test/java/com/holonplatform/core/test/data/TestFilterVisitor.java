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

import org.junit.jupiter.api.Assertions;

import com.holonplatform.core.internal.query.QueryFilterVisitor;
import com.holonplatform.core.internal.query.filter.AndFilter;
import com.holonplatform.core.internal.query.filter.BetweenFilter;
import com.holonplatform.core.internal.query.filter.EqualFilter;
import com.holonplatform.core.internal.query.filter.GreaterFilter;
import com.holonplatform.core.internal.query.filter.InFilter;
import com.holonplatform.core.internal.query.filter.LessFilter;
import com.holonplatform.core.internal.query.filter.NotEqualFilter;
import com.holonplatform.core.internal.query.filter.NotFilter;
import com.holonplatform.core.internal.query.filter.NotInFilter;
import com.holonplatform.core.internal.query.filter.NotNullFilter;
import com.holonplatform.core.internal.query.filter.NullFilter;
import com.holonplatform.core.internal.query.filter.OrFilter;
import com.holonplatform.core.internal.query.filter.StringMatchFilter;
import com.holonplatform.core.query.QueryFilter;

public class TestFilterVisitor implements QueryFilterVisitor<QueryFilter, Object> {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.NullFilter,
	 * java.lang.Object)
	 */
	@Override
	public QueryFilter visit(NullFilter filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.NotNullFilter,
	 * java.lang.Object)
	 */
	@Override
	public QueryFilter visit(NotNullFilter filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.EqualFilter,
	 * java.lang.Object)
	 */
	@Override
	public <T> QueryFilter visit(EqualFilter<T> filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.NotEqualFilter,
	 * java.lang.Object)
	 */
	@Override
	public <T> QueryFilter visit(NotEqualFilter<T> filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.GreaterFilter,
	 * java.lang.Object)
	 */
	@Override
	public <T> QueryFilter visit(GreaterFilter<T> filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.LessFilter,
	 * java.lang.Object)
	 */
	@Override
	public <T> QueryFilter visit(LessFilter<T> filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.InFilter,
	 * java.lang.Object)
	 */
	@Override
	public <T> QueryFilter visit(InFilter<T> filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.NotInFilter,
	 * java.lang.Object)
	 */
	@Override
	public <T> QueryFilter visit(NotInFilter<T> filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.BetweenFilter,
	 * java.lang.Object)
	 */
	@Override
	public <T> QueryFilter visit(BetweenFilter<T> filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.LikeFilter,
	 * java.lang.Object)
	 */
	@Override
	public QueryFilter visit(StringMatchFilter filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.AndFilter,
	 * java.lang.Object)
	 */
	@Override
	public QueryFilter visit(AndFilter filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.OrFilter,
	 * java.lang.Object)
	 */
	@Override
	public QueryFilter visit(OrFilter filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilterVisitor#visit(com.holonplatform.core.query.filter.NotFilter,
	 * java.lang.Object)
	 */
	@Override
	public QueryFilter visit(NotFilter filter, Object context) {
		Assertions.assertNotNull(filter);
		return filter;
	}

}
