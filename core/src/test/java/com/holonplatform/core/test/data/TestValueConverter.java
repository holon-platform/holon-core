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
package com.holonplatform.core.test.data;

import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

@SuppressWarnings("serial")
public class TestValueConverter implements PropertyValueConverter<TestEnum2, String> {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#fromModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public TestEnum2 fromModel(String value, Property<TestEnum2> property)
			throws com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException {
		return TestEnum2.parse(value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#toModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public String toModel(TestEnum2 value, Property<TestEnum2> property)
			throws com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException {
		return (value != null) ? value.getStringValue() : null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getPropertyType()
	 */
	@Override
	public Class<TestEnum2> getPropertyType() {
		return TestEnum2.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getModelType()
	 */
	@Override
	public Class<String> getModelType() {
		return String.class;
	}

}
