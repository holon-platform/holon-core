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
package com.holonplatform.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.internal.query.DefaultCollectionExpression;
import com.holonplatform.core.internal.query.DefaultConstantExpression;
import com.holonplatform.core.internal.query.DefaultQueryDefinition;
import com.holonplatform.core.internal.query.QueryDefinition;
import com.holonplatform.core.query.Query;
import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.test.data.DummyQuery;
import com.holonplatform.core.test.data.TestPropertySet;

public class TestQuery {

	@Test
	public void testExpressions() throws InvalidExpressionException {

		DefaultConstantExpression<String> ce = new DefaultConstantExpression<>("test");

		assertEquals(String.class, ce.getType());
		assertEquals("test", ce.getValue());

		DefaultConstantExpression<String> ce2 = new DefaultConstantExpression<>("test");
		DefaultConstantExpression<String> ce3 = new DefaultConstantExpression<>("test3");

		assertTrue(ce.equals(ce));
		assertTrue(ce.equals(ce2));
		assertTrue(ce.hashCode() == ce2.hashCode());
		assertFalse(ce.equals(ce3));
		assertFalse(ce.equals(null));
		assertFalse(ce.equals("x"));

		DefaultCollectionExpression<String> le = new DefaultCollectionExpression<>("test", "test2");

		assertNotNull(le.getValue());
		assertEquals(Collection.class, le.getType());
		assertEquals(2, le.size());

		List<String> ls = new ArrayList<>();
		ls.add("test");
		ls.add("test2");

		le = new DefaultCollectionExpression<>(ls);

		assertEquals(Collection.class, le.getType());
		assertEquals(2, le.size());

	}

	@Test
	public void testQueryDefinition() {

		DefaultQueryDefinition qd = new DefaultQueryDefinition();
		qd.setTarget(DataTarget.named("target"));

		assertTrue(qd.getTarget().isPresent());
		assertEquals("target", qd.getTarget().get().getName());

		qd.setLimit(Integer.valueOf(10));
		qd.setOffset(Integer.valueOf(20));

		assertTrue(qd.getLimit().isPresent());
		assertTrue(qd.getLimit().isPresent());
		assertEquals(new Integer(10), qd.getLimit().get());
		assertEquals(new Integer(20), qd.getOffset().get());

		qd.addFilter(TestPropertySet.NAME.isNotNull());
		assertTrue(qd.getFilter().isPresent());

		QuerySort qs = TestPropertySet.NAME.asc();
		qd.addSort(qs);
		assertEquals(qs, qd.getSort().get());

	}

	@Test
	public void testQuery() {

		QueryDefinition qd = new DefaultQueryDefinition();

		Query q = new DummyQuery(qd);

		q.target(DataTarget.named("target"));
		assertEquals("target", qd.getTarget().get().getName());

		q.parameter("tp", Integer.valueOf(1));
		assertTrue(qd.hasParameters());
		assertTrue(qd.hasParameter("tp"));
		assertTrue(qd.hasNotNullParameter("tp"));
		assertNotNull(qd.getParameter("tp"));
		assertEquals(new Integer(1), qd.getParameter("tp", int.class).orElse(null));
		assertEquals(new Integer(1), qd.getParameter("tp", int.class, Integer.valueOf(2)));

		q.limit(10);
		assertEquals(new Integer(10), qd.getLimit().get());
		q.offset(20);
		assertEquals(new Integer(20), qd.getOffset().get());

		q.restrict(20, 100);
		assertEquals(new Integer(20), qd.getLimit().get());
		assertEquals(new Integer(100), qd.getOffset().get());

		q.filter(TestPropertySet.NAME.isNotNull());
		assertTrue(qd.getFilter().isPresent());

		q.filter(TestPropertySet.NAME.isNotNull());

		QuerySort qs = TestPropertySet.NAME.asc();
		q.sort(qs);
		assertEquals(qs, qd.getSort().get());

		q.count();

		q.stream(TestPropertySet.PROPERTIES);
		q.findOne(TestPropertySet.PROPERTIES);
		q.list(TestPropertySet.PROPERTIES);

		q.stream(TestPropertySet.NAME);
		q.findOne(TestPropertySet.NAME);
		q.list(TestPropertySet.NAME);

		q.stream(TestPropertySet.NAME, TestPropertySet.SEQUENCE);
		q.findOne(TestPropertySet.NAME, TestPropertySet.SEQUENCE);
		q.list(TestPropertySet.NAME, TestPropertySet.SEQUENCE);

	}

}
