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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.holonplatform.core.property.ListVirtualProperty;
import com.holonplatform.core.property.ListVirtualProperty.ListVirtualPropertyBuilder;

/**
 * Default {@link ListVirtualProperty} implementation.
 * 
 * @param <T> Collection elements type
 *
 * @since 5.2.0
 */
public class DefaultListVirtualProperty<T>
		extends AbstractCollectionVirtualProperty<T, List<T>, ListVirtualProperty<T>, ListVirtualPropertyBuilder<T>>
		implements ListVirtualPropertyBuilder<T> {

	private static final long serialVersionUID = 15706655929046896L;

	/**
	 * Constructor.
	 * @param elementType Collection elements type (not null)
	 */
	@SuppressWarnings("unchecked")
	public DefaultListVirtualProperty(Class<? extends T> elementType) {
		super((Class<? extends List<T>>) (Class<?>) List.class, elementType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualProperty()
	 */
	@Override
	protected ListVirtualProperty<T> getActualProperty() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualBuilder()
	 */
	@Override
	protected ListVirtualPropertyBuilder<T> getActualBuilder() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractCollectionVirtualProperty#getDefaultInstanceProvider()
	 */
	@Override
	protected Supplier<List<T>> getDefaultInstanceProvider() {
		return () -> new ArrayList<>();
	}

}
