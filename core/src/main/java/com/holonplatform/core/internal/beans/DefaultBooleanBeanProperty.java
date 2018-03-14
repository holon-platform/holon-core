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

import com.holonplatform.core.beans.BooleanBeanProperty;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyConfiguration.PropertyConfigurationEditor;

/**
 * Default {@link BooleanBeanProperty} implementation.
 *
 * @since 5.1.0
 */
public class DefaultBooleanBeanProperty extends AbstractBeanProperty<Boolean> implements BooleanBeanProperty {

	private static final long serialVersionUID = -8900420385684447894L;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 * @param primitive Whether to use the primitive <code>boolean</code> type
	 */
	public DefaultBooleanBeanProperty(String name, boolean primitive) {
		this(name, primitive, null);
	}

	/**
	 * Constructor with custom {@link PropertyConfiguration}.
	 * @param name Property name (not null)
	 * @param primitive Whether to use the primitive <code>boolean</code> type
	 * @param configuration Optional property configuration instance
	 */
	public DefaultBooleanBeanProperty(String name, boolean primitive, PropertyConfigurationEditor configuration) {
		super(name, primitive ? boolean.class : Boolean.class, configuration);
	}

}
