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

import com.holonplatform.core.property.NumericProperty;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.StringProperty;

public interface TestIdentifiablePropertySet {

	public static final NumericProperty<Long> ID = NumericProperty.longType("id");
	public static final StringProperty TEXT = StringProperty.create("text");
	public static final PathProperty<TestEnum> ENM = PathProperty.create("enm", TestEnum.class);

	public static final PropertySet<?> PROPERTIES = PropertySet.builderOf(ID, TEXT, ENM).identifier(ID)
			.withConfiguration("test", "TEST").build();

}
