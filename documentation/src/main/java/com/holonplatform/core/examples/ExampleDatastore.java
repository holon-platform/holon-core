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

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import com.holonplatform.core.DataMappable;
import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.DataTarget.DataTargetResolver;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.DatastoreConfigProperties;
import com.holonplatform.core.datastore.transaction.Transaction;
import com.holonplatform.core.datastore.transaction.TransactionConfiguration;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QueryFilter.QueryFilterResolver;
import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.query.QuerySort.QuerySortResolver;

@SuppressWarnings({ "unused", "serial" })
public class ExampleDatastore {

	private static final class MyType {
	}

	public void datatarget() {
		// tag::datatarget[]
		DataTarget<String> target1 = DataTarget.named("test1"); // <1>
		DataTarget<MyType> target2 = DataTarget.of("test2", MyType.class); // <2>
		// end::datatarget[]
	}

	public void datastore() {
		// tag::datastore[]
		final PathProperty<String> A_PROPERTY = PathProperty.create("propertyPath", String.class);
		final DataTarget<String> TARGET = DataTarget.named("test");

		final Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		PropertyBox data = PropertyBox.builder(A_PROPERTY).set(A_PROPERTY, "aValue").build();

		OperationResult result = datastore.save(TARGET, data); // <1>

		result = datastore.insert(TARGET, data); // <2>
		result = datastore.update(TARGET, data); // <3>

		PropertyBox refreshed = datastore.refresh(TARGET, data); // <4>
		datastore.delete(TARGET, refreshed); // <5>

		// Bulk operations
		result = datastore.bulkInsert(TARGET, PropertySet.of(A_PROPERTY))
				.add(PropertyBox.builder(A_PROPERTY).set(A_PROPERTY, "aValue1").build())
				.add(PropertyBox.builder(A_PROPERTY).set(A_PROPERTY, "aValue2").build())
				.add(PropertyBox.builder(A_PROPERTY).set(A_PROPERTY, "aValue3").build()).execute(); // <6>

		result = datastore.bulkUpdate(TARGET).set(A_PROPERTY, "updated").filter(A_PROPERTY.isNull()).execute(); // <7>

		result = datastore.bulkDelete(TARGET).filter(A_PROPERTY.isNull()).execute(); // <8>
		// end::datastore[]
	}

	public void config() throws IOException {
		// tag::config[]
		DatastoreConfigProperties config = DatastoreConfigProperties.builder().withDefaultPropertySources().build(); // <1>

		config = DatastoreConfigProperties.builder().withSystemPropertySource().build(); // <2>

		Properties props = new Properties();
		props.put("holon.datastore.trace", "true");
		config = DatastoreConfigProperties.builder().withPropertySource(props).build(); // <3>

		config = DatastoreConfigProperties.builder().withPropertySource("datastore.properties").build(); // <4>
		// end::config[]
	}

	public void configuration2() throws IOException {
		// tag::config2[]
		DatastoreConfigProperties config1 = DatastoreConfigProperties.builder("one")
				.withPropertySource("datastore.properties").build();

		DatastoreConfigProperties config2 = DatastoreConfigProperties.builder("two")
				.withPropertySource("datastore.properties").build();
		// end::config2[]
	}

	public void transactional1() throws IOException {
		// tag::transactional1[]
		final PathProperty<String> A_PROPERTY = PathProperty.create("propertyPath", String.class);
		final DataTarget<String> TARGET = DataTarget.named("test");

		final Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		datastore.isTransactional().ifPresent(transactional -> { // <1>
			OperationResult result = transactional.withTransaction(tx -> { // <2>
				OperationResult r = datastore.insert(TARGET,
						PropertyBox.builder(A_PROPERTY).set(A_PROPERTY, "test").build()); // <3>
				tx.commit(); // <4>
				return r;
			});
		});
		// end::transactional1[]
	}

	public void transactional2() throws IOException {
		// tag::transactional2[]
		final PathProperty<String> A_PROPERTY = PathProperty.create("propertyPath", String.class);
		final DataTarget<String> TARGET = DataTarget.named("test");

		final Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation

		datastore.requireTransactional() // <1>
				.withTransaction(tx -> { // <2>
					datastore.insert(TARGET, PropertyBox.builder(A_PROPERTY).set(A_PROPERTY, "test").build()); // <3>
					tx.commit(); // <4>
				});
		// end::transactional2[]
	}

	public void transactional3() throws IOException {
		// tag::transactional3[]
		getDatastore().requireTransactional().withTransaction(tx -> {
			tx.setRollbackOnly(); // <1>
		});
		// end::transactional3[]
	}

	public void transactional4() throws IOException {
		// tag::transactional4[]
		getDatastore().requireTransactional().withTransaction(tx -> {
			// ...
		}, TransactionConfiguration.withAutoCommit()); // <1>
		// end::transactional4[]
	}

	public void transactional5() throws IOException {
		// tag::transactional5[]
		getDatastore().requireTransactional().withTransaction(tx -> {
			// ...
		}, TransactionConfiguration.create(false, false)); // <1>
		// end::transactional5[]
	}

	public void transactional6() throws IOException {
		// tag::transactional6[]
		Transaction tx = getDatastore().requireTransactional().getTransaction(); // <1>
		// Datastore operations execution...
		tx.commit(); // <2>
		// end::transactional6[]
	}

	// tag::resolver[]
	final static PathProperty<String> SOME_PROPERTY = PathProperty.create("test", String.class);

	class MyExpression implements QuerySort { // <1>

		@Override
		public void validate() throws InvalidExpressionException {
		}

	}

	public void resolver() {

		ExpressionResolver<MyExpression, QuerySort> resolver = ExpressionResolver.create(MyExpression.class, // <2>
				QuerySort.class, (expression, context) -> {
					return Optional.of(QuerySort.asc(SOME_PROPERTY));
				});

		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation
		datastore.addExpressionResolver(resolver); // <3>

		datastore.query().target(DataTarget.named("test")).sort(new MyExpression()).stream(SOME_PROPERTY); // <4>
	}
	// end::resolver[]

	@SuppressWarnings("rawtypes")
	public void datatargetResolver() {
		// tag::datatargetresolver[]
		ExpressionResolver resolver = DataTargetResolver.create(DataTarget.class,
				(target, context) -> "test".equals(target.getName())
						? Optional.of(DataTarget.named("wellKnownTargetName"))
						: Optional.empty()); // <1>
		// end::datatargetresolver[]
	}

	@SuppressWarnings("static-method")
	private Datastore getDatastore() {
		return null;
	}

	// tag::customfilter[]
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
	// end::customfilter[]

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

	public void dataMappable1() {
		// tag::datamappable1[]
		PathProperty<Integer> property = PathProperty.create("name", Integer.class) //
				.dataPath("mapping-name"); // <1>

		Optional<String> mapping = property.getDataPath(); // <2>
		// end::datamappable1[]
	}

	public void dataMappable2() {
		final StringProperty P1 = StringProperty.create("p1");
		final StringProperty P2 = StringProperty.create("p2");

		// tag::datamappable2[]
		PropertySet<?> PROPERTIES = PropertySet.builderOf(P1, P2) //
				.configuration(DataMappable.PATH, "mapping-name") // <1>
				.build();

		Optional<String> mapping = PROPERTIES.getConfiguration().getParameter(DataMappable.PATH); // <2>
		// end::datamappable2[]
	}

}
