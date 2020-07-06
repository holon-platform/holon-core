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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.internal.DefaultParameterSet;
import com.holonplatform.core.internal.MutableParameterSet;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertySet;

/**
 * Default {@link PropertySet} implementation using an {@link ArrayList}
 * instance.
 * 
 * @param <P> Concrete type of the properties contained in the set
 * 
 * @since 5.0.0
 */
@SuppressWarnings("rawtypes")
public class DefaultPropertySet<P extends Property> extends ArrayList<P> implements PropertySet<P> {

	private static final long serialVersionUID = 288703271476761715L;

	/**
	 * Identifiers
	 */
	private Set<P> identifiers;

	/**
	 * Configuration
	 */
	private MutableParameterSet configuration;

	/**
	 * Default empty constructor
	 */
	public DefaultPropertySet() {
		super();
	}

	/**
	 * Constructs a property set containing the elements of the specified
	 * collection, in the order they are returned by the collection's iterator.
	 * @param <C>        Actual property type
	 * @param properties Property collection whose elements are to be placed into
	 *                   this set
	 */
	protected <C extends P> DefaultPropertySet(Collection<C> properties) {
		super(properties);
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
	 * 
	 * @see
	 * com.holonplatform.core.property.PropertySet#contains(com.holonplatform.core.
	 * property.Property)
	 */
	@Override
	public boolean contains(Property property) {
		return (property == null) ? false : super.contains(property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#stream()
	 */
	@Override
	public Stream<P> stream() {
		return super.stream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.holonplatform.core.property.PropertySet#getIdentifiers()
	 */
	@Override
	public Set<P> getIdentifiers() {
		return (identifiers == null) ? Collections.emptySet() : Collections.unmodifiableSet(identifiers);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.holonplatform.core.property.PropertySet#getConfiguration()
	 */
	@Override
	public ParameterSet getConfiguration() {
		return (configuration != null) ? configuration : ParameterSet.empty();
	}

	/**
	 * Add a parameter to the property set configuration.
	 * @param name  Parameter name (not null)
	 * @param value Parameter value
	 */
	protected void addConfigurationParameter(String name, Object value) {
		ObjectUtils.argumentNotNull(name, "Configuration parameter name must be not null");
		if (configuration == null) {
			configuration = new DefaultParameterSet();
		}
		configuration.addParameter(name, value);
	}

	/**
	 * Add given property to property set identifiers.
	 * @param property The property to declare as property set identifier (not null)
	 */
	protected void addIdentifier(P property) {
		ObjectUtils.argumentNotNull(property, "Identifier property must be not null");
		if (identifiers == null) {
			identifiers = new LinkedHashSet<>(4);
		}
		identifiers.add(property);
	}

	/**
	 * Set given properties as property set identifiers.
	 * @param <PT>       Actual property type
	 * @param properties Identifier properties (not null)
	 */
	protected <PT extends P> void setIdentifers(Iterable<PT> properties) {
		ObjectUtils.argumentNotNull(properties, "Identifier properties must be not null");
		identifiers = new LinkedHashSet<>(4);
		properties.forEach(p -> identifiers.add(p));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PropertySet [");
		sb.append(super.toString());
		if (identifiers != null && !identifiers.isEmpty()) {
			sb.append(" / Identifiers: {");
			sb.append(identifiers.toString());
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
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
		 * 
		 * @see
		 * com.holonplatform.core.property.PropertySet.Builder#add(com.holonplatform.
		 * core.property.Property)
		 */
		@Override
		public <PT extends P> Builder<P> add(PT property) {
			ObjectUtils.argumentNotNull(property, "Property must be not null");
			if (!this.instance.contains(property)) {
				this.instance.add(property);
			}
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.property.PropertySet.Builder#add(java.lang.Iterable)
		 */
		@Override
		public <PT extends P> Builder<P> add(Iterable<PT> properties) {
			ObjectUtils.argumentNotNull(properties, "Properties must be not null");
			properties.forEach(p -> {
				if (!this.instance.contains(p)) {
					this.instance.add(p);
				}
			});
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.property.PropertySet.Builder#remove(com.holonplatform.
		 * core.property.Property)
		 */
		@Override
		public <PT extends P> Builder<P> remove(PT property) {
			ObjectUtils.argumentNotNull(property, "Property must be not null");
			this.instance.remove(property);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.holonplatform.core.property.PropertySet.Builder#remove(java.lang.
		 * Iterable)
		 */
		@Override
		public <PT extends P> Builder<P> remove(Iterable<PT> properties) {
			ObjectUtils.argumentNotNull(properties, "Properties must be not null");
			properties.forEach(this.instance::remove);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.holonplatform.core.property.PropertySet.Builder#withIdentifier(com.
		 * holonplatform.core.property.Property)
		 */
		@Override
		public <PT extends P> Builder<P> withIdentifier(PT property) {
			this.instance.addIdentifier(property);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.property.PropertySet.Builder#identifiers(java.lang.
		 * Iterable)
		 */
		@Override
		public <PT extends P> Builder<P> identifiers(Iterable<PT> properties) {
			this.instance.setIdentifers(properties);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.property.PropertySet.Builder#withConfiguration(java.
		 * lang.String, java.lang.Object)
		 */
		@Override
		public Builder<P> withConfiguration(String name, Object value) {
			this.instance.addConfigurationParameter(name, value);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.holonplatform.core.internal.datastore.PropertySetBuilder#build()
		 */
		@Override
		public PropertySet<P> build() {
			instance.trimToSize();
			return instance;
		}

	}

}
