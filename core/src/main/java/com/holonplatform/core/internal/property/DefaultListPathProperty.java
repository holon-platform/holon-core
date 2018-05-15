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
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.holonplatform.core.property.ListPathProperty;
import com.holonplatform.core.property.ListPathProperty.ListPathPropertyBuilder;
import com.holonplatform.core.property.PathProperty;

/**
 * Default {@link ListPathProperty} implementation.
 * 
 * @param <T> Collection elements type
 *
 * @since 5.2.0
 */
public class DefaultListPathProperty<T>
		extends AbstractCollectionPathProperty<T, List<T>, ListPathProperty<T>, ListPathPropertyBuilder<T>>
		implements ListPathPropertyBuilder<T> {

	private static final long serialVersionUID = -5102949418469320622L;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 * @param elementType Collection elements type (not null)
	 */
	@SuppressWarnings("unchecked")
	public DefaultListPathProperty(String name, Class<? extends T> elementType) {
		super(name, (Class<? extends List<T>>) (Class<?>) List.class, elementType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractCollectionProperty#getDefaultInstanceProvider()
	 */
	@Override
	protected Supplier<List<T>> getDefaultInstanceProvider() {
		return () -> new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualProperty()
	 */
	@Override
	protected ListPathProperty<T> getActualProperty() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualBuilder()
	 */
	@Override
	protected ListPathPropertyBuilder<T> getActualBuilder() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.ListPathProperty#clone(java.util.function.Consumer)
	 */
	@Override
	public ListPathProperty<T> clone(Consumer<PathProperty.Builder<List<T>, PathProperty<List<T>>, ?>> builder) {
		return clonePathProperty(new DefaultListPathProperty<>(getName(), getElementType()), builder);
	}

}
