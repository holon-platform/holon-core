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

import com.holonplatform.core.internal.beans.DefaultTemporalBeanProperty;
import com.holonplatform.core.property.TemporalProperty;

/**
 * A temporal type {@link BeanProperty}, implementing {@link TemporalProperty}.
 *
 * @param <T> Property type
 *
 * @since 5.1.0
 */
public interface TemporalBeanProperty<T> extends BeanProperty<T>, TemporalProperty<T> {

	/**
	 * Get a builder to create a {@link TemporalBeanProperty}.
	 * @param <T> Property type
	 * @param name Property name (not null)
	 * @param type Property type (not null)
	 * @return BeanProperty builder
	 */
	static <T> BeanProperty.Builder<T> builder(String name, Class<T> type) {
		return new DefaultTemporalBeanProperty<>(name, type);
	}

}
