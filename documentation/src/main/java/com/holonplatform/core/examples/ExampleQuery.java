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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.relational.Join.JoinType;
import com.holonplatform.core.datastore.relational.RelationalTarget;
import com.holonplatform.core.datastore.relational.SubQuery;
import com.holonplatform.core.property.NumericProperty;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.property.TemporalProperty;
import com.holonplatform.core.query.BeanProjection;
import com.holonplatform.core.query.ConstantExpressionProjection;
import com.holonplatform.core.query.Query;
import com.holonplatform.core.query.QueryAggregation;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QueryFilter.QueryFilterResolver;
import com.holonplatform.core.query.QueryFunction;
import com.holonplatform.core.query.QueryFunction.Avg;
import com.holonplatform.core.query.QueryFunction.Count;
import com.holonplatform.core.query.QueryFunction.Max;
import com.holonplatform.core.query.QueryFunction.Min;
import com.holonplatform.core.query.QueryFunction.Sum;
import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.query.QuerySort.QuerySortResolver;
import com.holonplatform.core.query.QuerySort.SortDirection;
import com.holonplatform.core.query.StringFunction.Lower;
import com.holonplatform.core.query.StringFunction.Upper;
import com.holonplatform.core.query.TemporalFunction.CurrentDate;
import com.holonplatform.core.query.TemporalFunction.CurrentLocalDate;
import com.holonplatform.core.query.TemporalFunction.CurrentLocalDateTime;
import com.holonplatform.core.query.TemporalFunction.CurrentTimestamp;
import com.holonplatform.core.query.TemporalFunction.Day;
import com.holonplatform.core.query.TemporalFunction.Hour;
import com.holonplatform.core.query.TemporalFunction.Month;
import com.holonplatform.core.query.TemporalFunction.Year;

@SuppressWarnings("unused")
public class ExampleQuery {

	public void filter1() {
		// tag::filter1[]
		final PathProperty<Integer> PROPERTY1 = PathProperty.create("test1", Integer.class);
		final PathProperty<Integer> PROPERTY2 = PathProperty.create("test2", Integer.class);
		final StringProperty STRING_PROPERTY = StringProperty.create("test3");

		// <1>
		QueryFilter restriction = QueryFilter.isNotNull(PROPERTY1); // is not null
		restriction = QueryFilter.isNull(PROPERTY1); // is null
		restriction = QueryFilter.eq(PROPERTY1, 7); // equal to a value
		restriction = QueryFilter.eq(PROPERTY1, PROPERTY2); // equal to another property expression
		restriction = QueryFilter.neq(PROPERTY1, 7); // not equal
		restriction = QueryFilter.lt(PROPERTY1, 7); // less than
		restriction = QueryFilter.loe(PROPERTY1, 7); // less than or equal
		restriction = QueryFilter.gt(PROPERTY1, 7); // greater than
		restriction = QueryFilter.goe(PROPERTY1, 7); // greater than or equal
		restriction = QueryFilter.between(PROPERTY1, 1, 7); // between
		restriction = QueryFilter.in(PROPERTY1, 1, 2, 3); // in
		restriction = QueryFilter.nin(PROPERTY1, 1, 2, 3); // not in

		// <2>
		restriction = QueryFilter.startsWith(STRING_PROPERTY, "V", false); // starts with 'v'
		restriction = QueryFilter.startsWith(STRING_PROPERTY, "v", true); // starts with 'v', ignoring case
		restriction = QueryFilter.endsWith(STRING_PROPERTY, "v", false); // ends with 'v'
		restriction = QueryFilter.contains(STRING_PROPERTY, "v", false); // contains 'v'
		QueryFilter restriction2 = QueryFilter.contains(STRING_PROPERTY, "v", true); // contains 'v', ignoring case

		// negation // <3>
		QueryFilter negation = restriction.not();
		negation = QueryFilter.not(restriction);

		// conjuction // <4>
		QueryFilter conjuction = restriction.and(restriction2);
		conjuction = QueryFilter.allOf(restriction, restriction2).orElse(null);

		// disjunction // <5>
		QueryFilter disjunction = restriction.or(restriction2);
		disjunction = QueryFilter.anyOf(restriction, restriction2).orElse(null);
		// end::filter1[]
	}

	public void filter2() {
		// tag::filter2[]
		final PathProperty<Integer> PROPERTY1 = PathProperty.create("test1", Integer.class);
		final PathProperty<Integer> PROPERTY2 = PathProperty.create("test2", Integer.class);
		final StringProperty STRING_PROPERTY = StringProperty.create("test3");

		// <1>
		QueryFilter restriction = PROPERTY1.isNotNull(); // is not null
		restriction = PROPERTY1.isNull(); // is null
		restriction = PROPERTY1.eq(7); // equal to a value
		restriction = PROPERTY1.eq(PROPERTY2); // equal to another property
		restriction = PROPERTY1.neq(7); // not equal
		restriction = PROPERTY1.lt(7); // less than
		restriction = PROPERTY1.loe(7); // less than or equal
		restriction = PROPERTY1.gt(7); // greater than
		restriction = PROPERTY1.goe(7); // greater than or equal
		restriction = PROPERTY1.between(1, 7); // between
		restriction = PROPERTY1.in(1, 2, 3); // in
		restriction = PROPERTY1.nin(1, 2, 3); // not in

		// <2>
		restriction = STRING_PROPERTY.startsWith("v"); // starts with
		restriction = STRING_PROPERTY.startsWithIgnoreCase("v"); // starts with ignoring case
		restriction = STRING_PROPERTY.endsWith("v"); // ends with
		restriction = STRING_PROPERTY.endsWithIgnoreCase("v"); // ends with ignoring case
		restriction = STRING_PROPERTY.contains("v"); // contains
		QueryFilter restriction2 = STRING_PROPERTY.containsIgnoreCase("v"); // contains ignoring case

		// <3>
		QueryFilter negation = PROPERTY1.eq(7).not(); // negation
		QueryFilter conjuction = PROPERTY1.isNotNull().and(PROPERTY2.eq(3)); // conjuction
		QueryFilter disjunction = PROPERTY1.isNull().or(PROPERTY2.eq(3)); // disjunction
		// end::filter2[]
	}

	public void sort1() {
		// tag::sort1[]
		final PathProperty<String> PROPERTY = PathProperty.create("test", String.class);
		final PathProperty<String> ANOTHER_PROPERTY = PathProperty.create("another", String.class);

		// <1>
		QuerySort sort = QuerySort.of(PROPERTY, SortDirection.ASCENDING); // sort ASCENDING on given property path
		sort = QuerySort.of(PROPERTY, true); // sort ASCENDING on given property path
		sort = QuerySort.asc(PROPERTY); // sort ASCENDING on given property path
		QuerySort sort2 = QuerySort.desc(ANOTHER_PROPERTY); // sort DESCENDING on given property path

		// <2>
		QuerySort.of(sort, sort2); // sort using 'sort' and 'sort2' declarations, in the given order
		// end::sort1[]
	}

	public void sort2() {
		// tag::sort2[]
		final PathProperty<String> PROPERTY = PathProperty.create("test", String.class);
		final PathProperty<String> ANOTHER_PROPERTY = PathProperty.create("another", String.class);

		// <1>
		QuerySort sortAsc = PROPERTY.asc(); // sort ASCENDING on given property
		QuerySort sortDesc = PROPERTY.desc(); // sort DESCENDING on given property

		// <2>
		PROPERTY.asc().and(ANOTHER_PROPERTY.desc()); // sort ASCENDING on PROPERTY, than sort DESCENDING on
														// ANOTHER_PROPERTY
		// end::sort2[]
	}

	public void aggregationFunctions() {
		// tag::aggfun[]
		final NumericProperty<Integer> PROPERTY = NumericProperty.integerType("test");

		// <1>
		Count count = QueryFunction.count(PROPERTY);
		Min<Integer> min = QueryFunction.min(PROPERTY);
		Max<Integer> max = QueryFunction.max(PROPERTY);
		Avg avg = QueryFunction.avg(PROPERTY);
		Sum<Integer> sum = QueryFunction.sum(PROPERTY);

		// <2>
		count = Count.create(PROPERTY);
		min = Min.create(PROPERTY);
		max = Max.create(PROPERTY);
		avg = Avg.create(PROPERTY);
		sum = Sum.create(PROPERTY);

		// <3>
		count = PROPERTY.count();
		min = PROPERTY.min();
		max = PROPERTY.max();
		avg = PROPERTY.avg();
		sum = PROPERTY.sum();
		// end::aggfun[]
	}

	public void stringFunctions() {
		// tag::strfun[]
		final StringProperty PROPERTY = StringProperty.create("test");

		// <1>
		Lower lower = QueryFunction.lower(PROPERTY);
		Upper upper = QueryFunction.upper(PROPERTY);

		// <2>
		lower = Lower.create(PROPERTY);
		upper = Upper.create(PROPERTY);

		// <3>
		lower = PROPERTY.lower();
		upper = PROPERTY.upper();
		// end::strfun[]
	}

	public void currentTimeFunctions() {
		// tag::cdtfun[]
		// <1>
		CurrentDate currentDate = QueryFunction.currentDate();
		CurrentLocalDate currentLocalDate = QueryFunction.currentLocalDate();
		CurrentTimestamp currentTimestamp = QueryFunction.currentTimestamp();
		CurrentLocalDateTime currentLocalDateTime = QueryFunction.currentLocalDateTime();

		// <2>
		currentDate = CurrentDate.create();
		currentLocalDate = CurrentLocalDate.create();
		currentTimestamp = CurrentTimestamp.create();
		currentLocalDateTime = CurrentLocalDateTime.create();
		// end::cdtfun[]
	}

	public void temporalFunctions() {
		// tag::tmpfun[]
		final TemporalProperty<LocalDateTime> PROPERTY = TemporalProperty.localDateTime("test");

		// <1>
		Year year = QueryFunction.year(PROPERTY);
		Month month = QueryFunction.month(PROPERTY);
		Day day = QueryFunction.day(PROPERTY);
		Hour hour = QueryFunction.hour(PROPERTY);

		// <2>
		year = Year.create(PROPERTY);
		month = Month.create(PROPERTY);
		day = Day.create(PROPERTY);
		hour = Hour.create(PROPERTY);

		// <3>
		year = PROPERTY.year();
		month = PROPERTY.month();
		day = PROPERTY.day();
		hour = PROPERTY.hour();
		// end::tmpfun[]
	}

	public void aggregation() {
		// tag::aggregation[]
		final PathProperty<Integer> PROPERTY = PathProperty.create("test", Integer.class);
		final PathProperty<String> ANOTHER_PROPERTY = PathProperty.create("another", String.class);

		QueryAggregation aggregation = QueryAggregation.builder() // <1>
				.path(PROPERTY) // <2>
				.path(ANOTHER_PROPERTY) // <3>
				.filter(PROPERTY.isNotNull()) // <4>
				.build();
		// end::aggregation[]
	}

	public void queryDefinition() {
		// tag::querydefinition[]
		final PathProperty<Integer> PROPERTY = PathProperty.create("test", Integer.class);

		Datastore datastore = getDatastore(); // build or obtain a Datastore

		Query query = datastore.query() // <1>
				.target(DataTarget.named("testTarget")) // <2>
				.filter(PROPERTY.gt(10)) // <3>
				.sort(PROPERTY.asc()) // <4>
				.aggregate(PROPERTY) // <5>
				.limit(100) // <6>
				.offset(200); // <7>

		query = datastore.query(DataTarget.named("testTarget")) // <8>
				.aggregate(QueryAggregation.builder().path(PROPERTY).filter(PROPERTY.gt(10)).build()) // <9>
				.restrict(100, 200); // <10>
		// end::querydefinition[]
	}

	public void queryResults() {
		// tag::queryresults[]
		final NumericProperty<Integer> PROPERTY1 = NumericProperty.integerType("test1");
		final StringProperty PROPERTY2 = StringProperty.create("test2");

		final PropertySet<?> PROPERTIES = PropertySet.of(PROPERTY1, PROPERTY2);

		final DataTarget<?> TARGET = DataTarget.named("testTarget");

		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		long count = datastore.query().target(TARGET).count(); // <1>

		Stream<Integer> values = datastore.query(TARGET).stream(PROPERTY1); // <2>
		Optional<Integer> value = datastore.query(TARGET).findOne(PROPERTY1); // <3>

		Stream<PropertyBox> results = datastore.query(TARGET).stream(PROPERTY1, PROPERTY2); // <4>
		results = datastore.query(TARGET).stream(PROPERTIES); // <5>
		List<PropertyBox> list = datastore.query(TARGET).list(PROPERTY1, PROPERTY2); // <6>

		Optional<PropertyBox> result = datastore.query(TARGET).findOne(PROPERTY1, PROPERTY2); // <7>
		// end::queryresults[]
	}

	public void projection2() {
		// tag::projection2[]
		final NumericProperty<Integer> PROPERTY1 = NumericProperty.integerType("test");
		final StringProperty PROPERTY2 = StringProperty.create("test2");

		final DataTarget<?> TARGET = DataTarget.named("testTarget");

		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		Optional<Integer> sum = datastore.query(TARGET).findOne(PROPERTY1.sum()); // <1>

		Stream<String> results = datastore.query(TARGET).stream(PROPERTY2.upper()); // <2>
		// end::projection2[]
	}

	public void projection3() {
		// tag::projection3[]
		final DataTarget<?> TARGET = DataTarget.named("testTarget");

		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		Optional<String> result = datastore.query(TARGET).findOne(ConstantExpressionProjection.create("TEST")); // <1>
		// end::projection3[]
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
		final DataTarget<?> TARGET = DataTarget.named("testTarget");

		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		Stream<MyBean> results = datastore.query(TARGET).stream(BeanProjection.of(MyBean.class)); // <1>
		Optional<MyBean> result = datastore.query(TARGET).findOne(BeanProjection.of(MyBean.class)); // <2>

		final BeanPropertySet<MyBean> PROPERTIES = BeanPropertySet.create(MyBean.class);

		results = datastore.query(TARGET).stream(BeanProjection.of(MyBean.class, PROPERTIES.property("code"))); // <3>
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

		SubQuery<Integer> subQuery = SubQuery.create().target(TARGET2).filter(PROPERTY1.goe(1)).select(PROPERTY1); // <1>

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
				.filter(SubQuery.create().target(TARGET2).filter(PROPERTY2.eq(PROPERTY1)).exists()).stream(PROPERTY2); // <1>

		results = datastore.query().target(TARGET1)
				.filter(SubQuery.create().target(TARGET2).filter(PROPERTY2.eq(PROPERTY1)).notExists())
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
