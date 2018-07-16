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
package com.holonplatform.core.property;

import java.util.Set;

import com.holonplatform.core.internal.property.DefaultSetVirtualProperty;

/**
 * A collection type {@link VirtualProperty} using a {@link Set} as concrete collection type.
 *
 * @param <T> Set elements type
 * 
 * @since 5.2.0
 */
public interface SetVirtualProperty<T> extends VirtualProperty<Set<T>>, CollectionProperty<T, Set<T>> {

	/**
	 * Create a new {@link SetVirtualProperty}.
	 * @param <T> Set elements type
	 * @param elementType Set elements type (not null)
	 * @param valueProvider Property value provider (not null)
	 * @return A new {@link SetVirtualProperty} instance
	 */
	static <T> SetVirtualPropertyBuilder<T> create(Class<T> elementType, PropertyValueProvider<Set<T>> valueProvider) {
		return new DefaultSetVirtualProperty<>(elementType).valueProvider(valueProvider);
	}

	/**
	 * {@link SetVirtualProperty} builder.
	 *
	 * @param <T> Collection elements type
	 */
	public interface SetVirtualPropertyBuilder<T>
			extends VirtualProperty.Builder<Set<T>, SetVirtualProperty<T>, SetVirtualPropertyBuilder<T>>,
			CollectionProperty.Builder<T, Set<T>, SetVirtualProperty<T>, SetVirtualPropertyBuilder<T>>,
			SetVirtualProperty<T> {

	}

}
