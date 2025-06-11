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

import java.time.LocalDate;
import java.util.Optional;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.beans.Config;
import com.holonplatform.core.beans.Converter;
import com.holonplatform.core.beans.Converter.BUILTIN;
import com.holonplatform.core.beans.DataPath;
import com.holonplatform.core.beans.Ignore;
import com.holonplatform.core.beans.Sequence;
import com.holonplatform.core.beans.ValidationMessage;
import com.holonplatform.core.beans.Validator;
import com.holonplatform.core.i18n.Caption;
import com.holonplatform.core.property.BooleanProperty;
import com.holonplatform.core.property.NumericProperty;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertyValueConverter;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.property.TemporalProperty;

@SuppressWarnings("unused")
public class ExampleBeans {

	// tag::set[]
	class MyNestedBean {

		private String nestedName;

		public String getNestedName() {
			return nestedName;
		}

		public void setNestedName(String nestedName) {
			this.nestedName = nestedName;
		}

	}

	class MyBean {

		private Long id;
		private boolean valid;
		private MyNestedBean nested;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

		public MyNestedBean getNested() {
			return nested;
		}

		public void setNested(MyNestedBean nested) {
			this.nested = nested;
		}

	}

	public static final BeanPropertySet<MyBean> PROPERTIES = BeanPropertySet.create(MyBean.class); // <1>

	public void propertySet() {
		Optional<PathProperty<Long>> idProperty = PROPERTIES.<Long>getProperty("id"); // <2>
		PathProperty<Long> id = PROPERTIES.property("id", Long.class); // <3>

		PathProperty<String> nestedName = PROPERTIES.property("nested.nestedName"); // <4>

		// read
		MyBean instance = new MyBean();
		instance.setId(1L);

		Long value = PROPERTIES.read("id", instance); // <5>
		PropertyBox box = PROPERTIES.read(instance); // <6>
		value = box.getValue(PROPERTIES.property("id")); // <7>

		// write
		instance = new MyBean();
		PROPERTIES.write("nested.nestedName", "test", instance); // <8>

		MyBean written = PROPERTIES.write(PropertyBox.builder(PROPERTIES).set(PROPERTIES.property("id"), 1L).build(),
				new MyBean()); // <9>
	}
	// end::set[]

	public void set2() {
		// tag::set2[]
		final BeanPropertySet<MyBean> PROPERTIES = BeanPropertySet.create(MyBean.class);

		StringProperty stringProperty = PROPERTIES.propertyString("aStringTypeBeanPropertyName"); // <1>
		NumericProperty<Integer> numericProperty = PROPERTIES.propertyNumeric("aIntegerTypeBeanPropertyName"); // <2>
		TemporalProperty<LocalDate> temporalProperty = PROPERTIES.propertyTemporal("aLocalDateTypeBeanPropertyName"); // <3>
		BooleanProperty booleanProperty = PROPERTIES.propertyBoolean("aBooleanTypeBeanPropertyName"); // <4>
		// end::set2[]
	}

	public void introspect() {
		// tag::introspector[]
		BeanIntrospector introspector = BeanIntrospector.get(); // <1>
		BeanPropertySet<MyBean> properties = introspector.getPropertySet(MyBean.class); // <2>
		// end::introspector[]
	}

	public void introspect2() {
		// tag::introspector2[]
		MyBean instance = new MyBean();
		instance.setId(7L);

		PropertyBox value = BeanIntrospector.get().read(instance); // <1>

		final NumericProperty<Long> ID = NumericProperty.longType("id");

		BeanIntrospector.get().write(PropertyBox.builder(ID).set(ID, 8L).build(), instance); // <2>
		// end::introspector2[]
	}

	public void postProcessor() {
		// tag::postprocessor[]
		BeanIntrospector.get()
				.addBeanPropertyPostProcessor((property, cls) -> property.withConfiguration("test", "testValue")); // <1>
		// end::postprocessor[]
	}

	public void postProcessor2() {
		// tag::postprocessor2[]
		BeanIntrospector.get()
				.addBeanPropertySetPostProcessor((propertySet, cls) -> propertySet.configuration("test", "testValue")); // <1>
		// end::postprocessor2[]
	}

	@SuppressWarnings("hiding")
	static
	// tag::ignore[]
	class Bean1 {

		public static final BeanPropertySet<Bean1> PROPERTIES = BeanPropertySet.create(Bean1.class); // <2>

		private Long id;

		@Ignore // <1>
		private String name;

		// getters and setters omitted

	}
	// end::ignore[]

	@SuppressWarnings("hiding")
	static
	// tag::caption[]
	class Bean2 {

		public static final BeanPropertySet<Bean2> PROPERTIES = BeanPropertySet.create(Bean2.class);

		@Caption("Code") // <1>
		private Long id;

		@Caption(value = "Name", messageCode = "name.localization.code") // <2>
		private String name;

		// getters and setters omitted

	}
	// end::caption[]

	@SuppressWarnings("hiding")
	static
	// tag::sequence[]
	class Bean3 {

		public static final BeanPropertySet<Bean3> PROPERTIES = BeanPropertySet.create(Bean3.class);

		@Sequence(10) // <1>
		private Long id;

		@Sequence(20) // <2>
		private String name;

		// getters and setters omitted

	}
	// end::sequence[]

	@SuppressWarnings("hiding")
	static
	// tag::config[]
	class Bean4 {

		public static final BeanPropertySet<Bean4> PROPERTIES = BeanPropertySet.create(Bean4.class);

		private Long id;

		@Config(key = "test1", value = "myValue1") // <1>
		@Config(key = "test2", value = "myValue2")
		private String name;

		// getters and setters omitted

	}
	// end::config[]

	@SuppressWarnings("serial")
	private static final class MyConverter implements PropertyValueConverter<String, Long> {

		@Override
		public String fromModel(Long value, Property<String> property) throws PropertyConversionException {
			return null;
		}

		@Override
		public Long toModel(String value, Property<String> property) throws PropertyConversionException {
			return null;
		}

		@Override
		public Class<String> getPropertyType() {
			return null;
		}

		@Override
		public Class<Long> getModelType() {
			return null;
		}

	}

	@SuppressWarnings("hiding")
	static
	// tag::converter[]
	class Bean5 {

		public static final BeanPropertySet<Bean5> PROPERTIES = BeanPropertySet.create(Bean5.class);

		@Converter(MyConverter.class) // <1>
		private Long id;

		@Converter(builtin = BUILTIN.NUMERIC_BOOLEAN) // <2>
		private Boolean value;

		// getters and setters omitted

	}
	// end::converter[]

	@SuppressWarnings("serial")
	private static final class MyFirstValidator implements com.holonplatform.core.Validator<String> {

		@Override
		public void validate(String value) throws ValidationException {
		}

	}

	@SuppressWarnings("serial")
	private static final class MySecondValidator implements com.holonplatform.core.Validator<String> {

		@Override
		public void validate(String value) throws ValidationException {
		}

	}

	@SuppressWarnings("hiding")
	static
	// tag::validation[]
	class Bean6 {

		public static final BeanPropertySet<Bean6> PROPERTIES = BeanPropertySet.create(Bean6.class);

		@Min(1) // <1>
		@Max(value = 100, message = "{my.localizable.message}") // <2>
		private Long id;

		@NotBlank // <3>
		@ValidationMessage(message = "Name must be not blank", messageCode = "my.message.localization.code")
		private String name;

		@Validator(MyFirstValidator.class) // <4>
		@Validator(MySecondValidator.class)
		private String value;

		// getters and setters omitted

	}
	// end::validation[]

	@SuppressWarnings("hiding")
	static
	// tag::datapath[]
	@DataPath("myPath") // <2>
	class Bean7 {

		public static final BeanPropertySet<Bean7> PROPERTIES = BeanPropertySet.create(Bean7.class);

		@DataPath("code") // <1>
		private Long id;

		private String name;

		// getters and setters omitted

	}
	// end::datapath[]

}
