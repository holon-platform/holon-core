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

import java.util.List;

import com.holonplatform.core.internal.property.DefaultListVirtualProperty;

/**
 * A collection type {@link VirtualProperty} using a {@link List} as concrete collection type.
 *
 * @param <T> List elements type
 * 
 * @since 5.2.0
 */
public interface ListVirtualProperty<T> extends VirtualProperty<List<T>>, CollectionProperty<T, List<T>> {

	/**
	 * Create a new {@link ListVirtualProperty}.
	 * @param <T> List elements type
	 * @param elementType List elements type (not null)
	 * @param valueProvider Property value provider (not null)
	 * @return A new {@link ListVirtualProperty} instance
	 */
	static <T> ListVirtualPropertyBuilder<T> create(Class<T> elementType,
			PropertyValueProvider<List<T>> valueProvider) {
		return new DefaultListVirtualProperty<>(elementType).valueProvider(valueProvider);
	}

	/**
	 * {@link ListVirtualPropertyBuilder} builder.
	 *
	 * @param <T> Collection elements type
	 */
	public interface ListVirtualPropertyBuilder<T>
			extends VirtualProperty.Builder<List<T>, ListVirtualProperty<T>, ListVirtualPropertyBuilder<T>>,
			CollectionProperty.Builder<T, List<T>, ListVirtualProperty<T>, ListVirtualPropertyBuilder<T>>,
			ListVirtualProperty<T> {

	}

}
