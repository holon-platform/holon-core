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
package com.holonplatform.core.internal.beans;

import com.holonplatform.core.beans.NumericBeanProperty;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyConfiguration.PropertyConfigurationEditor;

/**
 * Default {@link NumericBeanProperty} implementation.
 * 
 * @param <N> Number type
 *
 * @since 5.1.0
 */
public class DefaultNumericBeanProperty<N extends Number> extends AbstractBeanProperty<N>
		implements NumericBeanProperty<N> {

	private static final long serialVersionUID = 378869124643300794L;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 */
	public DefaultNumericBeanProperty(String name, Class<N> type) {
		this(name, type, null);
	}

	/**
	 * Constructor with custom {@link PropertyConfiguration}.
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 * @param configuration Optional property configuration instance
	 */
	public DefaultNumericBeanProperty(String name, Class<? extends N> type, PropertyConfigurationEditor configuration) {
		super(name, type, configuration);
	}

}
