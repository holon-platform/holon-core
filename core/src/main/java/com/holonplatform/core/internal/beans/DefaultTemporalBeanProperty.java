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

import com.holonplatform.core.beans.TemporalBeanProperty;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.TemporalProperty;

/**
 * Default {@link TemporalBeanProperty} implementation.
 * 
 * @param <T> Temporal type
 *
 * @since 5.1.0
 */
public class DefaultTemporalBeanProperty<T> extends AbstractBeanProperty<T> implements TemporalBeanProperty<T> {

	private static final long serialVersionUID = 3086072770115020722L;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 */
	public DefaultTemporalBeanProperty(String name, Class<? extends T> type) {
		super(name, type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.TemporalProperty#clone(java.util.function.Consumer)
	 */
	@Override
	public TemporalProperty<T> clone(Consumer<PathProperty.Builder<T, PathProperty<T>, ?>> builder) {
		return clonePathProperty(new DefaultTemporalBeanProperty<>(getName(), getType()), builder);
	}

}
