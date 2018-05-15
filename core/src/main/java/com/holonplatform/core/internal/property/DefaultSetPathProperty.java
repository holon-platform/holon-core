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
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.SetPathProperty;
import com.holonplatform.core.property.SetPathProperty.SetPathPropertyBuilder;

/**
 * Default {@link SetPathProperty} implementation.
 * 
 * @param <T> Collection elements type
 *
 * @since 5.2.0
 */
public class DefaultSetPathProperty<T>
		extends AbstractCollectionPathProperty<T, Set<T>, SetPathProperty<T>, SetPathPropertyBuilder<T>>
		implements SetPathPropertyBuilder<T> {

	private static final long serialVersionUID = -2839473817790424464L;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 * @param elementType Collection elements type (not null)
	 */
	@SuppressWarnings("unchecked")
	public DefaultSetPathProperty(String name, Class<? extends T> elementType) {
		super(name, (Class<? extends Set<T>>) (Class<?>) Set.class, elementType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractCollectionPathProperty#getDefaultInstanceProvider()
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
	protected SetPathProperty<T> getActualProperty() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualBuilder()
	 */
	@Override
	protected SetPathPropertyBuilder<T> getActualBuilder() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.SetPathProperty#clone(java.util.function.Consumer)
	 */
	@Override
	public SetPathProperty<T> clone(Consumer<PathProperty.Builder<Set<T>, PathProperty<Set<T>>, ?>> builder) {
		return clonePathProperty(new DefaultSetPathProperty<>(getName(), getElementType()), builder);
	}

}
