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

import com.holonplatform.core.internal.beans.DefaultBooleanBeanProperty;
import com.holonplatform.core.property.BooleanProperty;

/**
 * A Boolean type {@link BeanProperty}, implementing {@link BooleanProperty}.
 *
 * @since 5.1.0
 */
public interface BooleanBeanProperty extends BeanProperty<Boolean>, BooleanProperty {

	/**
	 * Get a builder to create a {@link BooleanBeanProperty}.
	 * @param name Property name (not null)
	 * @param primitive Whether to use the primitive <code>boolean</code> type
	 * @return BeanProperty builder
	 */
	static BeanProperty.Builder<Boolean> builder(String name, boolean primitive) {
		return new DefaultBooleanBeanProperty(name, primitive);
	}

}
