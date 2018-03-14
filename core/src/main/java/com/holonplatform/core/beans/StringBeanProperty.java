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
package com.holonplatform.core.beans;

import com.holonplatform.core.internal.beans.DefaultStringBeanProperty;
import com.holonplatform.core.property.StringProperty;

/**
 * A String type {@link BeanProperty}, implementing {@link StringProperty}.
 *
 * @since 5.1.0
 */
public interface StringBeanProperty extends BeanProperty<String>, StringProperty {

	/**
	 * Get a builder to create a {@link StringBeanProperty}.
	 * @param name Property name (not null)
	 * @return BeanProperty builder
	 */
	static BeanProperty.Builder<String> builder(String name) {
		return new DefaultStringBeanProperty(name);
	}

}
