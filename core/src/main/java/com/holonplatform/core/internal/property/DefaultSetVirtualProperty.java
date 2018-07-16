/*
 * Copyright 2016-2018 Axioma srl.
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

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import com.holonplatform.core.property.SetVirtualProperty;
import com.holonplatform.core.property.SetVirtualProperty.SetVirtualPropertyBuilder;

/**
 * Default {@link SetVirtualProperty} implementation.
 * 
 * @param <T> Collection elements type
 *
 * @since 5.2.0
 */
public class DefaultSetVirtualProperty<T>
		extends AbstractCollectionVirtualProperty<T, Set<T>, SetVirtualProperty<T>, SetVirtualPropertyBuilder<T>>
		implements SetVirtualPropertyBuilder<T> {

	private static final long serialVersionUID = -479915967940250606L;

	/**
	 * Constructor.
	 * @param elementType Collection elements type (not null)
	 */
	@SuppressWarnings("unchecked")
	public DefaultSetVirtualProperty(Class<? extends T> elementType) {
		super((Class<? extends Set<T>>) (Class<?>) Set.class, elementType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractCollectionVirtualProperty#getDefaultInstanceProvider()
	 */
	@Override
	protected Supplier<Set<T>> getDefaultInstanceProvider() {
		return () -> new HashSet<>();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualProperty()
	 */
	@Override
	protected SetVirtualProperty<T> getActualProperty() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualBuilder()
	 */
	@Override
	protected SetVirtualPropertyBuilder<T> getActualBuilder() {
		return this;
	}

}
