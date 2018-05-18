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
import java.util.function.Consumer;

import com.holonplatform.core.internal.property.DefaultListPathProperty;
import com.holonplatform.core.property.CollectionProperty.ListProperty;

/**
 * A {@link CollectionPathProperty} which uses a {@link List} as concrete collection type.
 * 
 * @param <T> List elements type
 *
 * @since 5.2.0
 */
public interface ListPathProperty<T> extends CollectionPathProperty<T, List<T>>, ListProperty<T> {

	/**
	 * Clone this property.
	 * @param builder A property builder which can be used to perform additional property configuration
	 * @return The cloned property
	 */
	@Override
	ListPathProperty<T> clone(Consumer<PathProperty.Builder<List<T>, PathProperty<List<T>>, ?>> builder);

	/**
	 * Create a new {@link ListPathProperty}.
	 * @param <T> Collection elements type
	 * @param name Property (and path) name (not null)
	 * @param elementType Collection elements type (not null)
	 * @return a new {@link ListPathProperty} instance
	 */
	static <T> ListPathPropertyBuilder<T> create(String name, Class<? extends T> elementType) {
		return new DefaultListPathProperty<>(name, elementType);
	}

	/**
	 * {@link ListPathProperty} builder.
	 *
	 * @param <T> Collection elements type
	 */
	public interface ListPathPropertyBuilder<T>
			extends CollectionPathProperty.Builder<T, List<T>, ListPathProperty<T>, ListPathPropertyBuilder<T>>,
			ListPathProperty<T> {

	}

}
