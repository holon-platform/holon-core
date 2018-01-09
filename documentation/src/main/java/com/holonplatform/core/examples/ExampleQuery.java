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
package com.holonplatform.core.examples;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.relational.Join.JoinType;
import com.holonplatform.core.datastore.relational.RelationalTarget;
import com.holonplatform.core.datastore.relational.SubQuery;
import com.holonplatform.core.property.NumericProperty;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.query.BeanProjection;
import com.holonplatform.core.query.QueryAggregation;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QueryFilter.QueryFilterResolver;
import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.query.QuerySort.QuerySortResolver;
import com.holonplatform.core.query.QuerySort.SortDirection;

@SuppressWarnings({ "unused", "serial" })
public class ExampleQuery {

	public void filter1() {
		// tag::filter1[]
		final PathProperty<String> PROPERTY = PathProperty.create("test", String.class);
		final PathProperty<String> ANOTHER_PROPERTY = PathProperty.create("another", String.class);

		QueryFilter restriction = QueryFilter.isNotNull(PROPERTY); // is not null
		restriction = QueryFilter.isNull(PROPERTY); // is null
		restriction = QueryFilter.eq(PROPERTY, "value"); // equal to a value
		restriction = QueryFilter.eq(PROPERTY, ANOTHER_PROPERTY); // equal to a property
		restriction = QueryFilter.neq(PROPERTY, "value"); // not equal
		restriction = QueryFilter.lt(PROPERTY, "value"); // less than
		restriction = QueryFilter.loe(PROPERTY, "value"); // less than or equal
		restriction = QueryFilter.gt(PROPERTY, "value"); // greater than
		restriction = QueryFilter.goe(PROPERTY, "value"); // greater than or equal
		restriction = QueryFilter.between(PROPERTY, "value1", "value2"); // between
		restriction = QueryFilter.in(PROPERTY, "value1", "value2", "value3"); // in
		restriction = QueryFilter.nin(PROPERTY, "value1", "value2", "value3"); // not in
		restriction = QueryFilter.startsWith(PROPERTY, "V", false); // starts with 'v'
		restriction = QueryFilter.startsWith(PROPERTY, "v", true); // starts with 'v', ignoring case
		restriction = QueryFilter.endsWith(PROPERTY, "v", false); // ends with 'v'
		restriction = QueryFilter.contains(PROPERTY, "v", false); // contains 'v'
		QueryFilter restriction2 = QueryFilter.contains(PROPERTY, "v", true); // contains 'v', ignoring case

		// negation
		QueryFilter negation = QueryFilter.not(restriction);
		negation = restriction.not();

		// conjuction
		QueryFilter conjuction = restriction.and(restriction2);
		conjuction = QueryFilter.allOf(restriction, restriction2).orElse(null);

		// disjunction
		QueryFilter disjunction = restriction.or(restriction2);
		disjunction = QueryFilter.anyOf(restriction, restriction2).orElse(null);
		// end::filter1[]
	}

	public void filter2() {
		// tag::filter2[]
		final StringProperty PROPERTY = StringProperty.create("test");
		final PathProperty<String> ANOTHER_PROPERTY = PathProperty.create("another", String.class);

		QueryFilter restriction = PROPERTY.isNotNull(); // is not null
		restriction = PROPERTY.isNull(); // is null
		restriction = PROPERTY.eq("value"); // equal to a value
		restriction = PROPERTY.eq(ANOTHER_PROPERTY); // equal to a property
		restriction = PROPERTY.neq("value"); // not equal
		restriction = PROPERTY.lt("value"); // less than
		restriction = PROPERTY.loe("value"); // less than or equal
		restriction = PROPERTY.gt("value"); // greater than
		restriction = PROPERTY.goe("value"); // greater than or equal
		restriction = PROPERTY.between("value1", "value2"); // between
		restriction = PROPERTY.in("value1", "value2", "value3"); // in
		restriction = PROPERTY.nin("value1", "value2", "value3"); // not in
		restriction = PROPERTY.startsWith("v"); // starts with
		restriction = PROPERTY.startsWithIgnoreCase("v"); // starts with ignoring case
		restriction = PROPERTY.endsWith("v"); // ends with
		restriction = PROPERTY.endsWithIgnoreCase("v"); // ends with ignoring case
		restriction = PROPERTY.contains("v"); // contains
		QueryFilter restriction2 = PROPERTY.containsIgnoreCase("v"); // contains ignoring case

		// negation
		QueryFilter negation = PROPERTY.eq("value").not();

		// conjuction
		QueryFilter conjuction = PROPERTY.isNotNull().and(PROPERTY.eq("value"));

		// disjunction
		QueryFilter disjunction = PROPERTY.isNull().or(PROPERTY.eq("value"));
		// end::filter2[]
	}

	// tag::custom[]
	class MyFilter implements QueryFilter { // <1>

		final StringProperty property;
		final String value;

		public MyFilter(StringProperty property, String value) {
			this.property = property;
			this.value = value;
		}

		@Override
		public void validate() throws InvalidExpressionException {
			if (value == null)
				throw new InvalidExpressionException("Value must be not null");
		}

	}

	class MyFilterResolver implements QueryFilterResolver<MyFilter> { // <2>

		@Override
		public Class<? extends MyFilter> getExpressionType() {
			return MyFilter.class;
		}

		@Override
		public Optional<QueryFilter> resolve(MyFilter expression, ResolutionContext context)
				throws InvalidExpressionException {
			return Optional
					.of(expression.property.isNotNull().and(expression.property.contains(expression.value, true))); // <3>
		}

	}

	final static StringProperty PROPERTY = StringProperty.create("testProperty");

	public void customFilter() {
		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation
		datastore.addExpressionResolver(new MyFilterResolver()); // <4>

		Stream<String> results = datastore.query().target(DataTarget.named("test"))
				.filter(PROPERTY.isNotNull().and(new MyFilter(PROPERTY, "testValue"))).stream(PROPERTY); // <5>
	}
	// end::custom[]

	public void sort1() {
		// tag::sort1[]
		final PathProperty<String> PROPERTY = PathProperty.create("test", String.class);
		final PathProperty<String> ANOTHER_PROPERTY = PathProperty.create("another", String.class);

		QuerySort sort = QuerySort.asc(PROPERTY); // sort ASCENDING on given property
		sort = QuerySort.desc(PROPERTY); // sort DESCENDING on given property

		QuerySort sort2 = QuerySort.of(ANOTHER_PROPERTY, SortDirection.ASCENDING); // sort ASCENDING on given property
		sort2 = QuerySort.of(ANOTHER_PROPERTY, true); // sort ASCENDING on given property

		QuerySort.of(sort, sort2); // sort using 'sort' and 'sort2' declarations, in the given order
		// end::sort1[]
	}

	public void sort2() {
		// tag::sort2[]
		final PathProperty<String> PROPERTY = PathProperty.create("test", String.class);
		final PathProperty<String> ANOTHER_PROPERTY = PathProperty.create("another", String.class);

		QuerySort sort = PROPERTY.asc(); // sort ASCENDING on given property
		sort = PROPERTY.desc(); // sort DESCENDING on given property

		PROPERTY.asc().and(ANOTHER_PROPERTY.desc()); // sort ASCENDING on PROPERTY, than sort DESCENDING on
														// ANOTHER_PROPERTY
		// end::sort2[]
	}

	// tag::customsort[]
	class MySort implements QuerySort { // <1>

		@Override
		public void validate() throws InvalidExpressionException {
		}

	}

	class MySortResolver implements QuerySortResolver<MySort> { // <2>

		final PathProperty<String> P1 = PathProperty.create("testProperty1", String.class);
		final PathProperty<Integer> P2 = PathProperty.create("testProperty2", Integer.class);

		@Override
		public Class<? extends MySort> getExpressionType() {
			return MySort.class;
		}

		@Override
		public Optional<QuerySort> resolve(MySort expression, ResolutionContext context)
				throws InvalidExpressionException {
			return Optional.of(P1.asc().and(P2.desc())); // <3>
		}

	}

	public void customSort() {
		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation
		datastore.addExpressionResolver(new MySortResolver()); // <4>

		Stream<String> results = datastore.query().target(DataTarget.named("test")).sort(new MySort()).stream(PROPERTY); // <5>
	}
	// end::customsort[]

	public void aggregationFunctions() {
		// tag::aggfun[]
		final PathProperty<Integer> PROPERTY = PathProperty.create("test1", Integer.class);
		final NumericProperty<Integer> PROPERTY2 = NumericProperty.integerType("test2");

		PROPERTY.count();
		PROPERTY.min();
		PROPERTY.max();

		PROPERTY2.avg();
		PROPERTY2.sum();
		// end::aggfun[]
	}

	public void aggregation() {
		// tag::aggregation[]
		final PathProperty<Integer> PROPERTY = PathProperty.create("test", Integer.class);
		final PathProperty<String> ANOTHER_PROPERTY = PathProperty.create("another", String.class);

		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		Stream<PropertyBox> results = datastore.query().target(DataTarget.named("testTarget")).aggregate(PROPERTY)
				.stream(PROPERTY, ANOTHER_PROPERTY.max()); // <1>

		results = datastore.query().target(DataTarget.named("testTarget"))
				.aggregate(QueryAggregation.builder().path(PROPERTY).filter(PROPERTY.isNotNull()).build())
				.stream(PROPERTY, ANOTHER_PROPERTY.max()); // <2>
		// end::aggregation[]
	}

	public void pagination() {
		// tag::pagination[]
		final PathProperty<Integer> PROPERTY = PathProperty.create("test", Integer.class);

		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		Stream<Integer> values = datastore.query().target(DataTarget.named("testTarget")).limit(100).offset(0)
				.stream(PROPERTY); // <1>
		values = datastore.query().target(DataTarget.named("testTarget")).limit(100).offset(100).stream(PROPERTY); // <2>
		// end::pagination[]
	}

	public void projection() {
		// tag::projection[]
		final NumericProperty<Integer> PROPERTY = NumericProperty.integerType("test");
		final PathProperty<String> ANOTHER_PROPERTY = PathProperty.create("another", String.class);

		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		Stream<Integer> values = datastore.query().target(DataTarget.named("testTarget")).stream(PROPERTY); // <1>
		Optional<Integer> value = datastore.query().target(DataTarget.named("testTarget")).findOne(PROPERTY); // <2>
		Stream<PropertyBox> boxes = datastore.query().target(DataTarget.named("testTarget")).stream(PROPERTY,
				ANOTHER_PROPERTY); // <3>
		List<PropertyBox> list = datastore.query().target(DataTarget.named("testTarget")).list(PROPERTY,
				ANOTHER_PROPERTY); // <4>
		Optional<PropertyBox> box = datastore.query().target(DataTarget.named("testTarget")).findOne(PROPERTY,
				ANOTHER_PROPERTY); // <5>
		Optional<Integer> sum = datastore.query().target(DataTarget.named("testTarget")).findOne(PROPERTY.sum()); // <6>
		// end::projection[]
	}

	// tag::beanprojection[]
	class MyBean {

		private Integer code;
		private String text;

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

	public void beanProjection() {
		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		Stream<MyBean> results = datastore.query().target(DataTarget.named("testTarget"))
				.stream(BeanProjection.of(MyBean.class)); // <1>
		Optional<MyBean> result = datastore.query().target(DataTarget.named("testTarget"))
				.findOne(BeanProjection.of(MyBean.class)); // <2>

		final PathProperty<Integer> CODE = PathProperty.create("code", Integer.class);
		final PathProperty<String> TEXT = PathProperty.create("text", String.class);

		results = datastore.query().target(DataTarget.named("testTarget"))
				.stream(BeanProjection.of(MyBean.class, CODE, TEXT)); // <3>
	}
	// end::beanprojection[]

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void subquery1() {
		// tag::subquery1[]
		Datastore datastore = getDatastore(); // this is supposed to be a relational Datastore implementation

		final DataTarget TARGET1 = DataTarget.named("testTarget1");
		final PathProperty<Integer> PROPERTY1 = TARGET1.property("test", Integer.class);

		final DataTarget TARGET2 = DataTarget.named("testTarget2");
		final PathProperty<Integer> PROPERTY2 = TARGET2.property("test", Integer.class);

		SubQuery<Integer> subQuery = SubQuery.create(datastore).target(TARGET2).filter(PROPERTY1.goe(1))
				.select(PROPERTY1); // <1>

		Stream<Integer> results = datastore.query().target(TARGET1).filter(PROPERTY2.in(subQuery)).stream(PROPERTY2); // <2>
		// end::subquery1[]
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void subquery2() {
		// tag::subquery2[]
		Datastore datastore = getDatastore(); // this is supposed to be a relational Datastore implementation

		final DataTarget TARGET1 = DataTarget.named("testTarget1");
		final PathProperty<Integer> PROPERTY1 = TARGET1.property("test", Integer.class);

		final DataTarget TARGET2 = DataTarget.named("testTarget2");
		final PathProperty<Integer> PROPERTY2 = TARGET2.property("test", Integer.class);

		Stream<Integer> results = datastore.query().target(TARGET1)
				.filter(SubQuery.create(datastore).target(TARGET2).filter(PROPERTY2.eq(PROPERTY1)).exists())
				.stream(PROPERTY2); // <1>

		results = datastore.query().target(TARGET1)
				.filter(SubQuery.create(datastore).target(TARGET2).filter(PROPERTY2.eq(PROPERTY1)).notExists())
				.stream(PROPERTY2); // <2>
		// end::subquery2[]
	}

	public void alias() {
		// tag::alias[]
		final DataTarget<String> TARGET = DataTarget.named("testTarget");

		RelationalTarget<String> RT = RelationalTarget.of(TARGET); // <1>
		RelationalTarget<String> RT2 = RT.alias("aliasName"); // <2>
		// end::alias[]
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void joins() {
		// tag::joins[]
		final DataTarget TARGET1 = DataTarget.named("testTarget1");
		final PathProperty<Integer> PROPERTY1 = TARGET1.property("test", Integer.class);

		final DataTarget TARGET2 = DataTarget.named("testTarget2");
		final PathProperty<Integer> PROPERTY2 = TARGET2.property("test", Integer.class);

		RelationalTarget<String> RT = RelationalTarget.of(TARGET1) // <1>
				.join(TARGET2, JoinType.INNER).on(PROPERTY2.eq(PROPERTY1)).add(); // <2>

		RT = RelationalTarget.of(TARGET1).innerJoin(TARGET2).on(PROPERTY2.eq(PROPERTY1)).add(); // <3>
		RT = RelationalTarget.of(TARGET1).leftJoin(TARGET2).on(PROPERTY2.eq(PROPERTY1)).add(); // <4>
		RT = RelationalTarget.of(TARGET1).rightJoin(TARGET2).on(PROPERTY2.eq(PROPERTY1)).add(); // <5>

		Stream<Integer> results = getDatastore().query().target(RT).stream(PROPERTY1); // <6>
		// end::joins[]
	}

	@SuppressWarnings("static-method")
	private Datastore getDatastore() {
		return null;
	}

}
