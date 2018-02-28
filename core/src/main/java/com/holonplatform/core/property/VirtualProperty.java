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
package com.holonplatform.core.property;

import com.holonplatform.core.internal.property.DefaultVirtualProperty;

/**
 * A virtual {@link Property} which relies on a {@link PropertyValueProvider} to provide its value, and it is not
 * directly bound to a data model attribute.
 * <p>
 * By default, a {@link VirtualProperty} is always read-only.
 * </p>
 * <p>
 * The natural lifecycle of a {@link VirtualProperty} is within a {@link PropertyBox} instance. The {@link PropertyBox}
 * instance will be provided to the {@link PropertyValueProvider} when the value of a {@link VirtualProperty} is
 * requested through the {@link PropertyBox#getValue(Property)} method. This way, the values of other properties
 * contained in the {@link PropertyBox} can be used by the virtual property to calculate the value to provide.
 * </p>
 * <p>
 * The symbolic property name (made avaialable by {@link Property#getName()}) can be setted using the
 * {@link Builder#name(String)} method. If a specific name is not provided, the name of a virtual property will be a
 * default name composed by the class name and the instance hash code.
 * </p>
 * <p>
 * The {@link #create(Class)} and {@link #create(Class, PropertyValueProvider)} builder methods can be used to create
 * and configure a new {@link PathProperty} instance.
 * </p>
 * 
 * @param <T> Property type
 * 
 * @since 5.0.0
 * 
 * @see PropertyValueProvider
 */
public interface VirtualProperty<T> extends Property<T> {

	/**
	 * Get property value provider.
	 * @return The property value provider (not null)
	 */
	PropertyValueProvider<T> getValueProvider();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#isReadOnly()
	 */
	@Override
	default boolean isReadOnly() {
		return true;
	}

	/**
	 * Create a new VirtualProperty with given <code>type</code>
	 * @param <T> Property value type
	 * @param type Property value type
	 * @return A {@link Builder} to setup property attributes
	 */
	static <T> Builder<T, ?> create(Class<? extends T> type) {
		return new DefaultVirtualProperty<>(type);
	}

	/**
	 * Create a new VirtualProperty with given <code>type</code> using given {@link PropertyValueProvider} as property
	 * value provider.
	 * @param <T> Property value type
	 * @param type Property value type
	 * @param valueProvider Property value provider
	 * @return A {@link Builder} to setup property attributes
	 */
	static <T> Builder<T, ?> create(Class<T> type, PropertyValueProvider<T> valueProvider) {
		return new DefaultVirtualProperty<>(type).valueProvider(valueProvider);
	}

	// Builder

	/**
	 * Base interface for {@link VirtualProperty} building.
	 * @param <T> Property value type
	 * @param <B> Concrete builder type
	 */
	public interface Builder<T, B extends Builder<T, B>> extends Property.Builder<T, B>, VirtualProperty<T> {

		/**
		 * Set the property name.
		 * @param name The property name (not null)
		 * @return this
		 */
		B name(String name);

		/**
		 * Set property value provider.
		 * @param valueProvider The {@link PropertyValueProvider} to set (not null)
		 * @return this
		 */
		B valueProvider(PropertyValueProvider<T> valueProvider);

	}

}
