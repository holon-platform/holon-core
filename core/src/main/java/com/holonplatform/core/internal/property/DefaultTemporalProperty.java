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
package com.holonplatform.core.internal.property;

import java.util.function.Consumer;

import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.TemporalProperty;
import com.holonplatform.core.property.TemporalProperty.TemporalPropertyBuilder;

/**
 * Default {@link TemporalProperty} implementation.
 * 
 * @param <T> Property type
 *
 * @since 5.1.0
 */
public class DefaultTemporalProperty<T> extends AbstractPathProperty<T, TemporalProperty<T>, TemporalPropertyBuilder<T>>
		implements TemporalPropertyBuilder<T> {

	private static final long serialVersionUID = 8579232846776577003L;

	/**
	 * Constructor
	 * @param name Property name (not null)
	 * @param type Property type (not null)
	 */
	public DefaultTemporalProperty(String name, Class<? extends T> type) {
		super(name, type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualProperty()
	 */
	@Override
	protected TemporalProperty<T> getActualProperty() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualBuilder()
	 */
	@Override
	protected TemporalPropertyBuilder<T> getActualBuilder() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.TemporalProperty#clone(java.util.function.Consumer)
	 */
	@Override
	public TemporalProperty<T> clone(Consumer<PathProperty.Builder<T, PathProperty<T>, ?>> builder) {
		return clonePathProperty(new DefaultTemporalProperty<>(getName(), getType()), builder);
	}

}
