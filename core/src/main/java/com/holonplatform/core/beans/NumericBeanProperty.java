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

import com.holonplatform.core.internal.beans.DefaultNumericBeanProperty;
import com.holonplatform.core.property.NumericProperty;

/**
 * A numeric type {@link BeanProperty}, implementing {@link NumericProperty}.
 *
 * @param <N> Number type
 *
 * @since 5.1.0
 */
public interface NumericBeanProperty<N extends Number> extends BeanProperty<N>, NumericProperty<N> {

	/**
	 * Get a builder to create a {@link NumericBeanProperty}.
	 * @param <N> Number type
	 * @param name Property name (not null)
	 * @param type Property type (not null)
	 * @return BeanProperty builder
	 */
	static <N extends Number> BeanProperty.Builder<N> builder(String name, Class<N> type) {
		return new DefaultNumericBeanProperty<>(name, type);
	}

}
