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
package com.holonplatform.core.test.data;

import static com.holonplatform.core.property.PathProperty.create;

import java.util.Date;

import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.VirtualProperty;

/*
 * Properties of TestBean
 */
public final class TestPropertySet {

	public static final PropertySet<Property<?>> PROPERTIES;

	public static final PathProperty<String> NAME = create("name", String.class).message("Name")
			.messageCode("test.message.name");
	public static final PathProperty<Integer> SEQUENCE = create("sequence", Integer.class);
	public static final PathProperty<Number> GENERIC = create("generic", Number.class);
	public static final PathProperty<Long> NESTED_ID = create("nested.nestedId", long.class);
	public static final PathProperty<Date> NESTED_DATE = create("nested.nestedDate", Date.class);

	public static final Property<String> VIRTUAL = VirtualProperty.create(String.class).valueProvider(b -> "TEST");

	static {
		PROPERTIES = PropertySet.of(NAME, SEQUENCE, GENERIC, NESTED_ID, NESTED_DATE, VIRTUAL);
	}

	private TestPropertySet() {
	}

}
