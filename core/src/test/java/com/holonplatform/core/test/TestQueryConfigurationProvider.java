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
package com.holonplatform.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.internal.query.filter.AndFilter;
import com.holonplatform.core.internal.query.sort.MultiSort;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.query.QueryConfigurationProvider;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QueryFilter.CompositeQueryFilter;
import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.query.QuerySort.CompositeQuerySort;

public class TestQueryConfigurationProvider {

	private static final StringProperty PROPERTY = StringProperty.create("test");

	private static final QueryFilter FILTER = PROPERTY.eq("TEST");
	private static final QueryFilter FILTER2 = PROPERTY.neq("TEST2");

	private static final QuerySort SORT = PROPERTY.desc();
	private static final QuerySort SORT2 = PROPERTY.asc();

	private static final ParameterSet PARAMETERS = ParameterSet.builder().withParameter("testp", "TESTVAL").build();

	private static final ConfigProperty<Integer> CP = ConfigProperty.create("cp", Integer.class);

	@Test
	public void testCreation() {

		QueryConfigurationProvider qcp = QueryConfigurationProvider.create(FILTER, SORT);

		assertEquals(FILTER, qcp.getQueryFilter());
		assertEquals(SORT, qcp.getQuerySort());
		assertNull(qcp.getQueryParameters());

		qcp = QueryConfigurationProvider.create(() -> FILTER, () -> SORT);

		assertEquals(FILTER, qcp.getQueryFilter());
		assertEquals(SORT, qcp.getQuerySort());
		assertNull(qcp.getQueryParameters());

		qcp = QueryConfigurationProvider.create(() -> FILTER, () -> SORT, () -> PARAMETERS);

		assertEquals(FILTER, qcp.getQueryFilter());
		assertEquals(SORT, qcp.getQuerySort());
		assertEquals(PARAMETERS, qcp.getQueryParameters());

		qcp = QueryConfigurationProvider.withFilter(FILTER);
		assertEquals(FILTER, qcp.getQueryFilter());
		assertNull(qcp.getQuerySort());
		assertNull(qcp.getQueryParameters());

		qcp = QueryConfigurationProvider.withSort(SORT);
		assertNull(qcp.getQueryFilter());
		assertEquals(SORT, qcp.getQuerySort());
		assertNull(qcp.getQueryParameters());

		qcp = QueryConfigurationProvider.withFilter(() -> FILTER);
		assertEquals(FILTER, qcp.getQueryFilter());
		assertNull(qcp.getQuerySort());
		assertNull(qcp.getQueryParameters());

		qcp = QueryConfigurationProvider.withSort(() -> SORT);
		assertNull(qcp.getQueryFilter());
		assertEquals(SORT, qcp.getQuerySort());
		assertNull(qcp.getQueryParameters());

	}

	@Test
	public void testBuilder() {

		QueryConfigurationProvider qcp = QueryConfigurationProvider.builder().filter(FILTER).sort(SORT).build();

		assertEquals(FILTER, qcp.getQueryFilter());
		assertEquals(SORT, qcp.getQuerySort());
		assertNull(qcp.getQueryParameters());

		qcp = QueryConfigurationProvider.builder().filter(FILTER).build();

		assertEquals(FILTER, qcp.getQueryFilter());
		assertNull(qcp.getQuerySort());
		assertNull(qcp.getQueryParameters());

		qcp = QueryConfigurationProvider.builder().sort(SORT).build();

		assertNull(qcp.getQueryFilter());
		assertEquals(SORT, qcp.getQuerySort());
		assertNull(qcp.getQueryParameters());

		qcp = QueryConfigurationProvider.builder().filter(FILTER).filter(FILTER2).build();

		QueryFilter filter = qcp.getQueryFilter();
		assertNotNull(filter);
		assertTrue(filter instanceof AndFilter);

		List<QueryFilter> filters = ((CompositeQueryFilter) filter).getComposition();
		assertTrue(filters.contains(FILTER));
		assertTrue(filters.contains(FILTER2));

		qcp = QueryConfigurationProvider.builder().sort(SORT).sort(SORT2).build();

		QuerySort sort = qcp.getQuerySort();
		assertNotNull(sort);
		assertTrue(sort instanceof MultiSort);

		List<QuerySort> sorts = ((CompositeQuerySort) sort).getComposition();
		assertTrue(sorts.contains(SORT));
		assertTrue(sorts.contains(SORT2));

		qcp = QueryConfigurationProvider.builder().parameter("test", "val").parameter(CP, 3).build();

		ParameterSet ps = qcp.getQueryParameters();
		assertNotNull(ps);
		assertEquals("val", ps.getParameter("test", String.class, null));
		assertEquals(Integer.valueOf(3), ps.getParameter(CP, 0));

	}

}
