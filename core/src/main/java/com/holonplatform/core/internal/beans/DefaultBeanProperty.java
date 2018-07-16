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

import java.util.function.Consumer;

import com.holonplatform.core.beans.BeanProperty;
import com.holonplatform.core.property.PathProperty;

/**
 * Default {@link BeanProperty} implementation.
 * 
 * @param <T> Property type
 * 
 * @since 5.0.0
 */
public class DefaultBeanProperty<T> extends AbstractBeanProperty<T> {

	private static final long serialVersionUID = -136356340742425752L;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 */
	public DefaultBeanProperty(String name, Class<? extends T> type) {
		super(name, type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.CloneableProperty#clone(java.util.function.Consumer)
	 */
	@Override
	public PathProperty<T> clone(Consumer<PathProperty.Builder<T, PathProperty<T>, ?>> builder) {
		return clonePathProperty(new DefaultBeanProperty<>(getName(), getType()), builder);
	}

}
