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
package com.holonplatform.core.examples;

import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import com.holonplatform.core.Path;
import com.holonplatform.core.Validator;
import com.holonplatform.core.presentation.StringValuePresenter;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyRenderer;
import com.holonplatform.core.property.PropertyRendererRegistry;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.PropertyValueConverter;
import com.holonplatform.core.property.PropertyValuePresenter;
import com.holonplatform.core.property.PropertyValuePresenterRegistry;
import com.holonplatform.core.property.VirtualProperty;
import com.holonplatform.core.temporal.TemporalType;

@SuppressWarnings("unused")
public class ExampleProperty {

	public void path() {
		// tag::path[]
		Path<String> stringPath = Path.of("pathName", String.class); // <1>

		String name = stringPath.getName(); // <2>
		boolean root = stringPath.isRootPath(); // <3>

		Path<String> hierarchicalPath = Path.of("subName", String.class).parent(stringPath); // <4>
		String fullName = hierarchicalPath.fullName(); // <5>
		// end::path[]
	}

	public void config() {
		// tag::config[]
		PathProperty<Date> dateProperty = PathProperty.create("oldDateTypeProperty", Date.class)
				.temporalType(TemporalType.DATE_TIME) // <1>
				.configuration("myAttribute", "myValue"); // <2>

		PropertyConfiguration cfg = dateProperty.getConfiguration(); // <3>
		Optional<String> value = cfg.getParameter("myAttribute", String.class); // <4>
		// end::config[]
	}

	@SuppressWarnings("serial")
	public void converter() {
		// tag::converter[]
		PropertyValueConverter<Integer, String> converter = new PropertyValueConverter<Integer, String>() {

			@Override
			public Integer fromModel(String value, Property<Integer> property) throws PropertyConversionException {
				return (value != null) ? Integer.parseInt(value) : null; // <1>
			}

			@Override
			public String toModel(Integer value, Property<Integer> property) throws PropertyConversionException {
				return (value != null) ? String.valueOf(value) : null; // <2>
			}

			@Override
			public Class<Integer> getPropertyType() {
				return Integer.class;
			}

			@Override
			public Class<String> getModelType() {
				return String.class;
			}

		};
		// end::converter[]
	}

	public void builtinConverters() {
		// tag::bultincnv[]
		PropertyValueConverter.numericBoolean(Integer.class); // <1>
		PropertyValueConverter.localDate(); // <2>
		PropertyValueConverter.localDateTime(); // <3>
		PropertyValueConverter.enumByOrdinal(); // <4>
		PropertyValueConverter.enumByName(); // <5>
		// end::bultincnv[]
	}

	// tag::pathproperty[]
	public final static PathProperty<Long> ID = PathProperty.create("id", Long.class) // <1>
			.configuration("test", 1) // <2>
			.validator(Validator.notNull()) // <3>
			.message("Identifier") // <4>
			.messageCode("property.id"); // <5>

	public final static PathProperty<Boolean> VALID = PathProperty.create("valid", Boolean.class) // <6>
			.converter(PropertyValueConverter.numericBoolean(Integer.class)); // <7>
	// end::pathproperty[]

	// tag::vrtproperty[]
	public final static VirtualProperty<Integer> ALWAYS_ONE = VirtualProperty.create(Integer.class, propertyBox -> 1); // <1>

	public final static PathProperty<String> NAME = PathProperty.create("name", String.class); // <2>
	public final static PathProperty<String> SURNAME = PathProperty.create("surname", String.class); // <3>
	public final static VirtualProperty<String> FULL_NAME = VirtualProperty.create(String.class,
			propertyBox -> propertyBox.getValue(NAME) + " " + propertyBox.getValue(SURNAME)); // <4>
	// end::vrtproperty[]

	@SuppressWarnings("rawtypes")
	public void propertySet() {
		// tag::propertyset[]
		final PathProperty<String> NAME = PathProperty.create("name", String.class);
		final PathProperty<String> SURNAME = PathProperty.create("surname", String.class);
		final PathProperty<Long> ID = PathProperty.create("id", Long.class);

		PropertySet<Property> set = PropertySet.of(NAME, SURNAME); // <1>
		set = PropertySet.builder().add(NAME).add(SURNAME).build(); // <2>

		boolean contains = set.contains(NAME); // <3>
		set.forEach(p -> p.toString()); // <4>
		String captions = set.stream().map(p -> p.getMessage()).collect(Collectors.joining()); // <5>

		PropertySet<Property> newSet = set.join(ID); // <6>
		int size = newSet.size(); // <7>

		newSet = set.exclude(SURNAME); // <8>
		// end::propertyset[]
	}

	@SuppressWarnings("unchecked")
	public void propertyBox() {
		// tag::propertybox[]
		final PathProperty<Long> ID = PathProperty.create("id", Long.class).validator(Validator.notNull());
		final PathProperty<String> NAME = PathProperty.create("name", String.class).validator(Validator.notBlank());

		final PropertySet<?> PROPERTIES = PropertySet.of(ID, NAME);

		PropertyBox propertyBox = PropertyBox.create(ID, NAME); // <1>
		propertyBox = PropertyBox.create(PROPERTIES); // <2>

		propertyBox.setValue(ID, 1L); // <3>
		propertyBox.setValue(NAME, "testName"); // <4>

		propertyBox = PropertyBox.builder(PROPERTIES).invalidAllowed(false).set(ID, 1L).set(NAME, "testName").build(); // <5>

		propertyBox.validate(); // <6>

		Long id = propertyBox.getValue(ID); // <7>
		String name = propertyBox.getValueIfPresent(NAME).orElse("default"); // <8>

		boolean containsNotNullId = propertyBox.containsValue(ID); // <9>

		PropertyBox ids = propertyBox.cloneBox(ID); // <10>
		// end::propertybox[]
	}

	public void presenter() {
		// tag::presenter[]
		final PathProperty<Long> ID = PathProperty.create("id", Long.class);

		String stringValue = ID.present(1L); // <1>

		stringValue = PropertyValuePresenterRegistry.get().getPresenter(ID)
				.orElseThrow(() -> new IllegalStateException("No presenter available for given property"))
				.present(ID, 1L); // <2>
		// end::presenter[]
	}

	public void presenterRegistration() {
		// tag::presenterreg[]
		PropertyValuePresenter<LocalTime> myPresenter = (p, v) -> v.getHour() + "." + v.getMinute(); // <1>

		PropertyValuePresenterRegistry.get().register(p -> LocalTime.class.isAssignableFrom(p.getType()), myPresenter); // <2>
		// end::presenterreg[]
	}

	// tag::renderer[]
	class MyRenderingType { // <1>

		private final Class<?> propertyType;

		public MyRenderingType(Class<?> propertyType) {
			this.propertyType = propertyType;
		}

	}

	public void render() {
		PropertyRenderer<MyRenderingType, Object> myRenderer = PropertyRenderer.create(MyRenderingType.class,
				p -> new MyRenderingType(p.getType())); // <2>

		PropertyRendererRegistry.get().register(p -> true, myRenderer); // <3>

		final PathProperty<Long> ID = PathProperty.create("id", Long.class);

		MyRenderingType rendered = ID.render(MyRenderingType.class); // <4>
	}
	// end::renderer[]

}
