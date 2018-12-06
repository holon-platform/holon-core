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
import java.util.function.Consumer;

import com.holonplatform.core.internal.property.DefaultSetPathProperty;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.CollectionProperty.SetProperty;

/**
 * A {@link CollectionPathProperty} which uses a {@link Set} as concrete collection type.
 * 
 * @param <T> Set elements type
 *
 * @since 5.2.0
 */
public interface SetPathProperty<T> extends CollectionPathProperty<T, Set<T>>, SetProperty<T> {

	/**
	 * Clone this property.
	 * @param builder A property builder which can be used to perform additional property configuration
	 * @return The cloned property
	 */
	@Override
	SetPathProperty<T> clone(Consumer<PathProperty.Builder<Set<T>, PathProperty<Set<T>>, ?>> builder);

	/**
	 * Create a new {@link SetPathProperty}.
	 * @param <T> Collection elements type
	 * @param name Property (and path) name (not null)
	 * @param elementType Collection elements type (not null)
	 * @return a new {@link SetPathProperty} instance
	 */
	static <T> SetPathPropertyBuilder<T> create(String name, Class<? extends T> elementType) {
		return new DefaultSetPathProperty<>(name, elementType);
	}

	/**
	 * Create a new {@link PropertyBox} type {@link SetPathProperty}, using given <code>properties</code> as property
	 * set.
	 * @param <P> Property type
	 * @param name Property (and path) name (not null)
	 * @param properties Property set (not null)
	 * @return a new {@link SetPathProperty} instance
	 */
	@SuppressWarnings("rawtypes")
	static <P extends Property> SetPathPropertyBuilder<PropertyBox> propertyBox(String name, Iterable<P> properties) {
		ObjectUtils.argumentNotNull(properties, "Properties must be not null");
		return create(name, PropertyBox.class).withConfiguration(PropertySet.PROPERTY_CONFIGURATION_ATTRIBUTE,
				(properties instanceof PropertySet) ? (PropertySet<?>) properties : PropertySet.of(properties));
	}

	/**
	 * {@link SetPathProperty} builder.
	 *
	 * @param <T> Collection elements type
	 */
	public interface SetPathPropertyBuilder<T>
			extends CollectionPathProperty.Builder<T, Set<T>, SetPathProperty<T>, SetPathPropertyBuilder<T>>,
			SetPathProperty<T> {

	}

}
