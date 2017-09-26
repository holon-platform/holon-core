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

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.DataTarget.DataTargetResolver;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.DatastoreConfigProperties;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;

@SuppressWarnings("unused")
public class ExampleDatastore {

	public void datatarget() {
		// tag::datatarget[]
		DataTarget<String> target = DataTarget.named("test");
		// end::datatarget[]
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void resolver() {
		// tag::resolver[]
		ExpressionResolver resolver = DataTargetResolver.create(DataTarget.class,
				(target, context) -> "test".equals(target.getName())
						? Optional.of(DataTarget.named("wellKnownTargetName")) : Optional.empty()); // <1>

		Datastore datastore = getDatastore(); // build or obtain a concrete Datastore implementation
		datastore.addExpressionResolver(resolver); // <2>
		// end::resolver[]
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

	@SuppressWarnings("static-method")
	private Datastore getDatastore() {
		return null;
	}

}
