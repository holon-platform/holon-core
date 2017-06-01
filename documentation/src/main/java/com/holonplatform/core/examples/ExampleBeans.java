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

import java.util.Optional;

import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;

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

	public static final BeanPropertySet<MyBean> PROPERTIES = BeanIntrospector.get().getPropertySet(MyBean.class); // <1>

	public void propertySet() {
		Optional<PathProperty<Long>> idProperty = PROPERTIES.<Long>getProperty("id"); // <2>
		PathProperty<Long> id = PROPERTIES.requireProperty("id", Long.class); // <3>

		PathProperty<String> nestedName = PROPERTIES.requireProperty("nested.nestedName"); // <4>

		// read
		MyBean instance = new MyBean();
		instance.setId(1L);

		Long value = PROPERTIES.read("id", instance); // <5>
		PropertyBox box = PROPERTIES.read(instance); // <6>
		value = box.getValue(PROPERTIES.requireProperty("id")); // <7>

		// write
		instance = new MyBean();
		PROPERTIES.write("nested.nestedName", "test", instance); // <8>

		MyBean written = PROPERTIES
				.write(PropertyBox.builder(PROPERTIES).set(PROPERTIES.requireProperty("id"), 1L).build(), new MyBean()); // <9>
	}
	// end::set[]

	public void introspect() {
		// tag::introspector[]
		BeanIntrospector introspector = BeanIntrospector.get(); // <1>
		BeanPropertySet<MyBean> properties = introspector.getPropertySet(MyBean.class); // <2>
		// end::introspector[]
	}
	
	public void postProcessor() {
		// tag::postprocessor[]
		BeanIntrospector.get().addBeanPropertyPostProcessor((property, cls) -> property.configuration("test", "testValue")); // <1>
		// end::postprocessor[]
	}

}
