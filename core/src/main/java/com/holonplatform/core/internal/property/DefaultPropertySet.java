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
package com.holonplatform.core.internal.property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertySet;

/**
 * Default {@link PropertySet} implementation using an {@link ArrayList} instance.
 * 
 * @param <P> Concrete type of the properties contained in the set
 * 
 * @since 5.0.0
 */
@SuppressWarnings("rawtypes")
public class DefaultPropertySet<P extends Property> extends ArrayList<P> implements PropertySet<P> {

	private static final long serialVersionUID = 288703271476761715L;

	/**
	 * Default empty constructor
	 */
	public DefaultPropertySet() {
		super();
	}

	/**
	 * Constructs a property set containing the elements of the specified collection, in the order they are returned by
	 * the collection's iterator.
	 * @param <C> Actual property type
	 * @param properties Property collection whose elements are to be placed into this set
	 */
	public <C extends P> DefaultPropertySet(Collection<C> properties) {
		super(properties);
	}

	/**
	 * Constructs a property set containing the elements of the specified array, in the given order.
	 * @param <C> Actual property type
	 * @param properties Property list
	 */
	@SafeVarargs
	public <C extends P> DefaultPropertySet(final C... properties) {
		super();
		if (properties != null) {
			for (C property : properties) {
				add(property);
			}
		}
	}

	/**
	 * Constructs a property set containing the elements of the specified Iterable, in the given order.
	 * @param <C> Actual property type
	 * @param properties Property set iterator
	 */
	public <C extends P> DefaultPropertySet(Iterable<C> properties) {
		super();
		if (properties != null) {
			if (properties instanceof Collection) {
				addAll((Collection<C>) properties);
			} else {
				for (C property : properties) {
					add(property);
				}
			}
		}
	}

	/**
	 * Constructs a property set with the specified initial capacity.
	 * @param initialCapacity the initial capacity of the property set
	 */
	public DefaultPropertySet(int initialCapacity) {
		super(initialCapacity);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertySet#contains(com.holonplatform.core.property.Property)
	 */
	@Override
	public boolean contains(Property property) {
		return (property == null) ? false : super.contains(property);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#stream()
	 */
	@Override
	public Stream<P> stream() {
		return super.stream();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultPropertySet [" + super.toString() + "]";
	}

	// Builder

	/**
	 * Default {@link Builder} implementation.
	 * @param <P> Property type
	 */
	public static class DefaultBuilder<P extends Property> implements Builder<P> {

		/**
		 * Instance to build
		 */
		private final DefaultPropertySet<P> instance;

		/**
		 * Constructor
		 */
		public DefaultBuilder() {
			super();
			this.instance = new DefaultPropertySet<>();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.property.PropertySet.Builder#add(com.holonplatform.core.property.Property)
		 */
		@Override
		public <PT extends P> Builder<P> add(PT property) {
			ObjectUtils.argumentNotNull(property, "Property must be not null");
			this.instance.add(property);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.property.PropertySet.Builder#add(java.lang.Iterable)
		 */
		@Override
		public <PT extends P> Builder<P> add(Iterable<PT> properties) {
			ObjectUtils.argumentNotNull(properties, "Properties must be not null");
			properties.forEach(p -> this.instance.add(p));
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.property.PropertySet.Builder#remove(com.holonplatform.core.property.Property)
		 */
		@Override
		public <PT extends P> Builder<P> remove(PT property) {
			ObjectUtils.argumentNotNull(property, "Property must be not null");
			this.instance.remove(property);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.property.PropertySet.Builder#remove(java.lang.Iterable)
		 */
		@Override
		public <PT extends P> Builder<P> remove(Iterable<PT> properties) {
			ObjectUtils.argumentNotNull(properties, "Properties must be not null");
			properties.forEach(p -> this.instance.remove(p));
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.datastore.PropertySetBuilder#build()
		 */
		@Override
		public PropertySet<P> build() {
			return instance;
		}

	}

}
