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

import com.holonplatform.core.internal.property.DefaultPropertyBoxProperty;

/**
 * A {@link PropertyBox} type {@link PathProperty}.
 * <p>
 * The {@link PropertySet} of the {@link PropertyBox} type is provided at construction time and made available through
 * the {@link #getPropertySet()} method.
 * </p>
 *
 * @since 5.1.0
 */
public interface PropertyBoxProperty extends PathProperty<PropertyBox> {

	/**
	 * Get the {@link PropertySet} bound to the {@link PropertyBox} type of this property.
	 * @return The property {@link PropertySet} (should never be null)
	 */
	PropertySet<?> getPropertySet();

	/**
	 * Create a new {@link PropertyBoxProperty} with given <code>name</code> and bound to given <code>properties</code>
	 * property set.
	 * @param <P> Property set property type
	 * @param name Property name (not null)
	 * @param properties Property set (not null)
	 * @return {@link PropertyBoxProperty} builder
	 */
	@SuppressWarnings("rawtypes")
	static <P extends Property> PropertyBoxPropertyBuilder create(String name, Iterable<P> properties) {
		return new DefaultPropertyBoxProperty(name,
				(properties instanceof PropertySet) ? (PropertySet<?>) properties : PropertySet.of(properties));
	}

	/**
	 * Create a new {@link PropertyBoxProperty} with given <code>name</code> and bound to given <code>properties</code>
	 * property set.
	 * @param <P> Property set property type
	 * @param name Property name (not null)
	 * @param properties Property set (not null)
	 * @return {@link PropertyBoxProperty} builder
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static <P extends Property> PropertyBoxPropertyBuilder create(String name, P... properties) {
		return new DefaultPropertyBoxProperty(name, PropertySet.of(properties));
	}

	/**
	 * {@link PropertyBoxProperty} builder.
	 */
	public interface PropertyBoxPropertyBuilder
			extends Builder<PropertyBox, PropertyBoxProperty, PropertyBoxPropertyBuilder>, PropertyBoxProperty {

	}

}
