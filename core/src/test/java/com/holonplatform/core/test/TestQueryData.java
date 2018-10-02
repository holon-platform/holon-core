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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.CollectionConstantExpression;
import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.internal.query.QueryFilterVisitor.VisitableQueryFilter;
import com.holonplatform.core.internal.query.QuerySortVisitor;
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
import com.holonplatform.core.internal.query.filter.OperationQueryFilter;
import com.holonplatform.core.internal.query.filter.OperationQueryFilter.FilterOperator;
import com.holonplatform.core.internal.query.filter.OrFilter;
import com.holonplatform.core.internal.query.filter.StringMatchFilter;
import com.holonplatform.core.internal.query.filter.StringMatchFilter.MatchMode;
import com.holonplatform.core.internal.query.sort.MultiSort;
import com.holonplatform.core.internal.query.sort.Sort;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.ConstantExpression;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.query.QuerySort.CompositeQuerySort;
import com.holonplatform.core.query.QuerySort.PathQuerySort;
import com.holonplatform.core.query.QuerySort.SortDirection;
import com.holonplatform.core.test.data.TestBoxBean;
import com.holonplatform.core.test.data.TestBoxBeanPk;
import com.holonplatform.core.test.data.TestFilterVisitor;
import com.holonplatform.core.test.data.TestPropertySet;
import com.holonplatform.test.TestUtils;

public class TestQueryData {

	@Test
	public void testSorts() throws InvalidExpressionException {

		TestUtils.checkEnum(SortDirection.class);

		SortDirection sd = SortDirection.ASCENDING;
		SortDirection op = sd.getOpposite();
		assertEquals(SortDirection.DESCENDING, op);
		op = op.getOpposite();
		assertEquals(SortDirection.ASCENDING, op);

		Sort<String> ps = new Sort<>(TestPropertySet.NAME);
		assertEquals(TestPropertySet.NAME, ps.getPath());
		assertEquals(SortDirection.ASCENDING, ps.getDirection());

		ps.setDirection(SortDirection.DESCENDING);
		assertEquals(SortDirection.DESCENDING, ps.getDirection());

		ps.validate();

		assertNotNull(ps.toString());

		QuerySort sort = TestPropertySet.NAME.asc();
		TestUtils.assertInstanceOf(sort, PathQuerySort.class);
		assertEquals(TestPropertySet.NAME, ((PathQuerySort<?>) sort).getPath());
		assertEquals(SortDirection.ASCENDING, ((PathQuerySort<?>) sort).getDirection());

		sort = TestPropertySet.NAME.desc();
		TestUtils.assertInstanceOf(sort, PathQuerySort.class);
		assertEquals(TestPropertySet.NAME, ((PathQuerySort<?>) sort).getPath());
		assertEquals(SortDirection.DESCENDING, ((PathQuerySort<?>) sort).getDirection());

		ps.accept(new QuerySortVisitor<Object, Object>() {

			@Override
			public Object visit(PathQuerySort<?> sort, Object context) {
				return null;
			}

			@Override
			public Object visit(CompositeQuerySort sort, Object context) {
				return null;
			}
		}, null);

		// multi sort

		final MultiSort m = new MultiSort((QuerySort[]) null);

		TestUtils.expectedException(InvalidExpressionException.class, () -> {
			m.validate();
		});

		QuerySort ms = QuerySort.of(TestPropertySet.NAME.asc(), TestPropertySet.SEQUENCE.asc());
		TestUtils.assertInstanceOf(ms, CompositeQuerySort.class);
		assertNotNull(((CompositeQuerySort) ms).getComposition());
		assertEquals(2, ((CompositeQuerySort) ms).getComposition().size());
		TestUtils.assertInstanceOf(((CompositeQuerySort) ms).getComposition().get(0), PathQuerySort.class);
		TestUtils.assertInstanceOf(((CompositeQuerySort) ms).getComposition().get(1), PathQuerySort.class);
		assertEquals(TestPropertySet.NAME,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(0)).getPath());
		assertEquals(SortDirection.ASCENDING,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(0)).getDirection());
		assertEquals(TestPropertySet.SEQUENCE,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(1)).getPath());
		assertEquals(SortDirection.ASCENDING,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(1)).getDirection());

		ms = TestPropertySet.NAME.desc().and(TestPropertySet.SEQUENCE.desc());
		TestUtils.assertInstanceOf(ms, CompositeQuerySort.class);
		assertNotNull(((CompositeQuerySort) ms).getComposition());
		assertEquals(2, ((CompositeQuerySort) ms).getComposition().size());
		TestUtils.assertInstanceOf(((CompositeQuerySort) ms).getComposition().get(0), PathQuerySort.class);
		TestUtils.assertInstanceOf(((CompositeQuerySort) ms).getComposition().get(1), PathQuerySort.class);
		assertEquals(TestPropertySet.NAME,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(0)).getPath());
		assertEquals(SortDirection.DESCENDING,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(0)).getDirection());
		assertEquals(TestPropertySet.SEQUENCE,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(1)).getPath());
		assertEquals(SortDirection.DESCENDING,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(1)).getDirection());

		ms.validate();

		ms = QuerySort.of(TestPropertySet.NAME.asc(), TestPropertySet.SEQUENCE.desc());
		TestUtils.assertInstanceOf(ms, CompositeQuerySort.class);
		assertNotNull(((CompositeQuerySort) ms).getComposition());
		assertEquals(2, ((CompositeQuerySort) ms).getComposition().size());
		TestUtils.assertInstanceOf(((CompositeQuerySort) ms).getComposition().get(0), PathQuerySort.class);
		TestUtils.assertInstanceOf(((CompositeQuerySort) ms).getComposition().get(1), PathQuerySort.class);
		assertEquals(TestPropertySet.NAME,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(0)).getPath());
		assertEquals(SortDirection.ASCENDING,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(0)).getDirection());
		assertEquals(TestPropertySet.SEQUENCE,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(1)).getPath());
		assertEquals(SortDirection.DESCENDING,
				((PathQuerySort<?>) ((CompositeQuerySort) ms).getComposition().get(1)).getDirection());

		MultiSort m1 = new MultiSort(TestPropertySet.NAME.asc(), TestPropertySet.SEQUENCE.desc());
		assertNotNull(m1.toString());

		m1.accept(new QuerySortVisitor<Object, Object>() {

			@Override
			public Object visit(PathQuerySort<?> sort, Object context) {
				return null;
			}

			@Override
			public Object visit(CompositeQuerySort sort, Object context) {
				return null;
			}
		}, null);

		// query property expression

		QuerySort qs = TestPropertySet.NAME.asc();
		TestUtils.assertInstanceOf(qs, PathQuerySort.class);
		assertEquals(TestPropertySet.NAME, ((PathQuerySort<?>) qs).getPath());
		assertEquals(SortDirection.ASCENDING, ((PathQuerySort<?>) qs).getDirection());

		qs = TestPropertySet.SEQUENCE.desc();
		TestUtils.assertInstanceOf(qs, PathQuerySort.class);
		assertEquals(TestPropertySet.SEQUENCE, ((PathQuerySort<?>) qs).getPath());
		assertEquals(SortDirection.DESCENDING, ((PathQuerySort<?>) qs).getDirection());

		// builders

		QuerySort bs = QuerySort.of(TestPropertySet.NAME.asc());

		bs = QuerySort.of(TestPropertySet.NAME.asc(), TestPropertySet.SEQUENCE.desc());
		TestUtils.assertInstanceOf(bs, MultiSort.class);

		bs = QuerySort.of(Collections.singletonList(TestPropertySet.NAME.asc()));

		List<QuerySort> sts = new LinkedList<>();
		sts.add(TestPropertySet.NAME.asc());
		sts.add(TestPropertySet.SEQUENCE.desc());
		bs = QuerySort.of(sts);
		TestUtils.assertInstanceOf(bs, MultiSort.class);

	}

	@Test
	public void testFilters() throws InvalidExpressionException {

		TestUtils.checkEnum(FilterOperator.class);

		FilterOperator op = FilterOperator.EQUAL;
		String sid = op.getSerializedId();
		FilterOperator op2 = FilterOperator.deserialize(sid);
		assertEquals(op, op2);
		String sym = op.getSymbol();
		op2 = FilterOperator.fromSymbol(sym);
		assertEquals(op, op2);

		assertNull(FilterOperator.deserialize(null));
		assertNull(FilterOperator.fromSymbol(null));
		assertNull(FilterOperator.deserialize("xxx"));
		assertNull(FilterOperator.fromSymbol("xxx"));

		TestFilterVisitor visitor = new TestFilterVisitor();

		PathProperty<String> p = TestPropertySet.NAME;
		PathProperty<String> p2 = PathProperty.create("testJoin", String.class);

		OperationQueryFilter<?> uf = new NullFilter(p);
		assertEquals(p, uf.getLeftOperand());
		assertEquals(FilterOperator.NULL, uf.getOperator());
		assertNotNull(uf.toString());
		uf.validate();
		((VisitableQueryFilter) uf).accept(visitor, null);

		uf = new NotNullFilter(p);
		assertEquals(p, uf.getLeftOperand());
		assertEquals(FilterOperator.NOT_NULL, uf.getOperator());
		assertNotNull(uf.toString());
		uf.validate();
		((VisitableQueryFilter) uf).accept(visitor, null);

		NullFilter nf1 = new NullFilter(TestPropertySet.NAME);
		assertNotNull(nf1.toString());

		OperationQueryFilter<String> f = new EqualFilter<>(p, ConstantExpression.create("test"));
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.EQUAL, f.getOperator());
		assertNotNull(f.toString());
		f.validate();
		assertNotNull(f.toString());
		((VisitableQueryFilter) f).accept(visitor, null);

		assertFalse(nf1.equals(f));

		f = new NotEqualFilter<>(p, ConstantExpression.create("test"));
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.NOT_EQUAL, f.getOperator());
		assertNotNull(f.toString());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		f = new GreaterFilter<>(p, ConstantExpression.create("test"), false);
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.GREATER_THAN, f.getOperator());
		assertNotNull(f.toString());
		assertFalse(((GreaterFilter<?>) f).isIncludeEquals());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		f = new GreaterFilter<>(p, ConstantExpression.create("test"), true);
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.GREATER_OR_EQUAL, f.getOperator());
		assertNotNull(f.toString());
		assertTrue(((GreaterFilter<?>) f).isIncludeEquals());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		f = new LessFilter<>(p, ConstantExpression.create("test"), false);
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.LESS_THAN, f.getOperator());
		assertNotNull(f.toString());
		assertFalse(((LessFilter<?>) f).isIncludeEquals());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		f = new LessFilter<>(p, ConstantExpression.create("test"), true);
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.LESS_OR_EQUAL, f.getOperator());
		assertNotNull(f.toString());
		assertTrue(((LessFilter<?>) f).isIncludeEquals());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		f = new StringMatchFilter(p, "test", MatchMode.CONTAINS, true);
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.MATCH, f.getOperator());
		assertNotNull(f.toString());
		assertTrue(((StringMatchFilter) f).isIgnoreCase());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		f = new StringMatchFilter(p, "test", MatchMode.CONTAINS, false);
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.MATCH, f.getOperator());
		assertNotNull(f.toString());
		assertFalse(((StringMatchFilter) f).isIgnoreCase());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		OperationQueryFilter<String> ef = new EqualFilter<>(p, p2);
		assertEquals(p2, ef.getRightOperand().get());
		ef = new NotEqualFilter<>(p, p2);
		assertEquals(p2, ef.getRightOperand().get());
		ef = new GreaterFilter<>(p, p2, false);
		assertEquals(p2, ef.getRightOperand().get());
		ef = new GreaterFilter<>(p, p2, true);
		assertEquals(p2, ef.getRightOperand().get());
		ef = new LessFilter<>(p, p2, false);
		assertEquals(p2, ef.getRightOperand().get());
		ef = new LessFilter<>(p, p2, true);
		assertEquals(p2, ef.getRightOperand().get());

		Collection<String> vs = new ArrayList<>();
		vs.add("v1");

		f = new InFilter<>(p, CollectionConstantExpression.create("test", "test2"));
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.IN, f.getOperator());
		assertNotNull(f.toString());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		f = new InFilter<>(p, ConstantExpression.create(vs));
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.IN, f.getOperator());
		assertNotNull(f.toString());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		f = new NotInFilter<>(p, CollectionConstantExpression.create("test", "test2"));
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.NOT_IN, f.getOperator());
		assertNotNull(f.toString());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		f = new NotInFilter<>(p, ConstantExpression.create(vs));
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.NOT_IN, f.getOperator());
		assertNotNull(f.toString());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		f = new BetweenFilter<>(p, "test", "test2");
		assertEquals(p, f.getLeftOperand());
		assertEquals(FilterOperator.BETWEEN, f.getOperator());
		assertNotNull(f.toString());
		assertEquals("test", ((BetweenFilter<?>) f).getFromValue());
		assertEquals("test2", ((BetweenFilter<?>) f).getToValue());
		f.validate();
		((VisitableQueryFilter) f).accept(visitor, null);

		TestUtils.expectedException(InvalidExpressionException.class, () -> {
			EqualFilter<?> fz = new EqualFilter<>(TestPropertySet.NAME, null);
			fz.validate();
		});

		TestUtils.expectedException(InvalidExpressionException.class, () -> {
			BetweenFilter<String> fb = new BetweenFilter<>(TestPropertySet.NAME, null, "test2");
			fb.getFromValue();
			fb.validate();
		});
		TestUtils.expectedException(InvalidExpressionException.class, () -> {
			BetweenFilter<String> fs = new BetweenFilter<>(TestPropertySet.NAME, "test", null);
			fs.getToValue();
			fs.validate();
		});

		QueryFilter filter = TestPropertySet.NAME.eq("testValue");
		TestUtils.assertInstanceOf(filter, OperationQueryFilter.class);
		assertEquals(TestPropertySet.NAME, ((OperationQueryFilter<?>) filter).getLeftOperand());

		filter = TestPropertySet.NAME.neq("testValue");
		TestUtils.assertInstanceOf(filter, OperationQueryFilter.class);
		assertEquals(TestPropertySet.NAME, ((OperationQueryFilter<?>) filter).getLeftOperand());
		assertEquals(FilterOperator.NOT_EQUAL, ((OperationQueryFilter<?>) filter).getOperator());

		final NotFilter nf = new NotFilter(filter);
		assertNotNull(nf.toString());
		nf.validate();
		nf.accept(visitor, null);

		TestUtils.expectedException(UnsupportedOperationException.class,
				() -> nf.addFilter(new NullFilter(TestPropertySet.NAME)));

		TestUtils.expectedException(InvalidExpressionException.class, () -> {
			NotFilter ntf = new NotFilter(null);
			ntf.validate();
		});

		AndFilter af = new AndFilter(TestPropertySet.NAME.eq("test"), TestPropertySet.SEQUENCE.gt(1));
		assertEquals(2, af.getComposition().size());
		af.validate();
		assertNotNull(af.toString());

		OrFilter of = new OrFilter();

		of = new OrFilter(TestPropertySet.NAME.eq("test"), TestPropertySet.SEQUENCE.gt(1));
		assertEquals(2, of.getComposition().size());
		of.validate();
		assertNotNull(of.toString());

		List<QueryFilter> fs = new ArrayList<>();
		fs.add(TestPropertySet.NAME.eq("test"));
		fs.add(TestPropertySet.SEQUENCE.gt(1));

		AndFilter af2 = new AndFilter(fs);
		assertEquals(2, af2.getComposition().size());
		af2.validate();

		OrFilter of2 = new OrFilter(fs);
		assertEquals(2, of2.getComposition().size());
		of2.validate();

		fs = new ArrayList<>();
		fs.add(TestPropertySet.NAME.eq("test2"));
		fs.add(TestPropertySet.SEQUENCE.gt(4));

		AndFilter af3 = new AndFilter(fs);
		OrFilter of3 = new OrFilter(fs);

		assertFalse(af.equals(af3));
		assertFalse(of.equals(of3));

		af3.accept(visitor, null);
		of3.accept(visitor, null);

		TestUtils.expectedException(InvalidExpressionException.class, () -> {
			AndFilter anf = new AndFilter(new ArrayList<QueryFilter>());
			anf.validate();
		});
		TestUtils.expectedException(InvalidExpressionException.class, () -> {
			OrFilter orf = new OrFilter(new ArrayList<QueryFilter>());
			orf.validate();
		});

		AndFilter af5 = new AndFilter();
		af5.addFilter(TestPropertySet.NAME.eq("test5"));
		assertEquals(1, af5.getComposition().size());

		af5 = new AndFilter();
		af5.setComposition(fs);
		assertEquals(2, af5.getComposition().size());

		// builders

		QueryFilter flt1 = TestPropertySet.NAME.eq("t");
		QueryFilter flt2 = TestPropertySet.SEQUENCE.gt(1);

		QueryFilter qf = flt1.not();
		assertNotNull(qf);
		TestUtils.assertInstanceOf(qf, NotFilter.class);
		assertEquals(flt1, ((NotFilter) qf).getComposition().get(0));

		qf = flt1.or(flt2);
		assertNotNull(qf);
		TestUtils.assertInstanceOf(qf, OrFilter.class);
		assertEquals(2, ((OrFilter) qf).getComposition().size());

		qf = QueryFilter.anyOf(flt1, flt2).orElse(null);
		assertNotNull(qf);
		TestUtils.assertInstanceOf(qf, OrFilter.class);
		assertEquals(2, ((OrFilter) qf).getComposition().size());

		List<QueryFilter> li = new LinkedList<>();
		li.add(flt1);
		li.add(flt2);
		qf = QueryFilter.anyOf(li).orElse(null);
		assertNotNull(qf);
		TestUtils.assertInstanceOf(qf, OrFilter.class);
		assertEquals(2, ((OrFilter) qf).getComposition().size());

		qf = QueryFilter.allOf(flt1, flt2).orElse(null);
		assertNotNull(qf);
		TestUtils.assertInstanceOf(qf, AndFilter.class);
		assertEquals(2, ((AndFilter) qf).getComposition().size());

		qf = QueryFilter.allOf(li).orElse(null);
		assertNotNull(qf);
		TestUtils.assertInstanceOf(qf, AndFilter.class);
		assertEquals(2, ((AndFilter) qf).getComposition().size());

		qf = QueryFilter.allOf(flt1, null, flt2).orElse(null);
		assertNotNull(qf);
		TestUtils.assertInstanceOf(qf, AndFilter.class);
		assertEquals(2, ((AndFilter) qf).getComposition().size());

		qf = QueryFilter.anyOf(flt1, null, flt2, null).orElse(null);
		assertNotNull(qf);
		TestUtils.assertInstanceOf(qf, OrFilter.class);
		assertEquals(2, ((OrFilter) qf).getComposition().size());
	}

	@Test
	public void testDataTarget() {

		TestBoxBeanPk pk = new TestBoxBeanPk();
		pk.setCode(1L);
		TestBoxBean bean = new TestBoxBean();
		bean.setPk(pk);
		bean.setStr("test");

		final DataTarget<String> TARGET = DataTarget.named("testTarget");

		PathProperty<Long> CODE = TARGET.property("pk.code", Long.class);
		PathProperty<String> STR = TARGET.property("str", String.class);

		PropertySet<?> SET = PropertySet.of(CODE, STR);

		BeanPropertySet<TestBoxBean> beanProperties = BeanIntrospector.get().getPropertySet(TestBoxBean.class);

		PropertyBox box = beanProperties.read(PropertyBox.create(SET), bean);

		assertNotNull(box);
		assertEquals(Long.valueOf(1), box.getValue(CODE));
		assertEquals("test", box.getValue(STR));

		TestBoxBean written = beanProperties.write(box, new TestBoxBean());
		assertNotNull(written);
		assertEquals(1L, written.getPk().getCode());
		assertEquals("test", written.getStr());

		// with parent pk

		PathProperty<TestBoxBeanPk> PK = TARGET.property("pk", TestBoxBeanPk.class);
		CODE = TARGET.property("code", Long.class).parent(PK);
		STR = TARGET.property("str", String.class);

		SET = PropertySet.of(PK, CODE, STR);

		box = BeanIntrospector.get().read(PropertyBox.create(SET), bean);

		assertNotNull(box);
		assertEquals(Long.valueOf(1), box.getValue(CODE));
		assertEquals("test", box.getValue(STR));
		assertEquals(pk, box.getValue(PK));

		written = BeanIntrospector.get().write(box, new TestBoxBean());
		assertNotNull(written);
		assertEquals(1L, written.getPk().getCode());
		assertEquals("test", written.getStr());

	}

}
