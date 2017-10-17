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

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import com.holonplatform.core.Context;
import com.holonplatform.core.internal.property.DefaultPropertySet;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * This interface represent an {@link Iterable} and immutable set of {@link Property}s.
 * 
 * @param <P> Concrete type of the properties contained in the set
 * 
 * @since 5.0.0
 */
@SuppressWarnings("rawtypes")
public interface PropertySet<P extends Property> extends Iterable<P> {

	/**
	 * Default {@link Context} resource reference
	 */
	public static final String CONTEXT_KEY = PropertySet.class.getName();

	/**
	 * Returns the number of properties in this set. If this set contains more than <tt>Integer.MAX_VALUE</tt> elements,
	 * returns <tt>Integer.MAX_VALUE</tt>.
	 * @return the number of properties in this set
	 */
	int size();

	/**
	 * Return <code>true</code> if this set contains the given <code>property</code>. If the given <code>property</code>
	 * is <code>null</code>, always returns <code>false</code>.
	 * @param property Property whose presence in this set is to be tested
	 * @return <code>true</code> if this set contains the specified property
	 */
	boolean contains(Property property);

	/**
	 * Returns a sequential {@code Stream} of the {@link Property}s of this set.
	 * @return Properties stream
	 */
	Stream<P> stream();

	/**
	 * Convert this PropertySet into a {@link List} of properties.
	 * @return List of set properties
	 */
	default List<P> asList() {
		return ConversionUtils.iterableAsList(this);
	}

	/**
	 * Execute given {@link Callable} <code>operation</code> on behalf of this PropertySet, i.e. binding this
	 * PropertySet as {@link Context} resource to current Thread, and removing the binding after operation execution.
	 * @param <V> Operation result type
	 * @param operation Operation to execute
	 * @return Operation result
	 */
	default <V> V execute(final Callable<V> operation) {
		return Context.get().executeThreadBound(CONTEXT_KEY, this, operation);
	}

	// builders

	/**
	 * Builder to create and populate a {@link PropertySet}.
	 * @param <P> Type of the property managed by the property set
	 * @return PropertySetBuilder
	 */
	static <P extends Property> Builder<P> builder() {
		return new DefaultPropertySet.DefaultBuilder<>();
	}

	/**
	 * Create a new PropertySet containing given <code>properties</code>.
	 * @param <P> Type of the property managed by the property set
	 * @param properties Properties of the set
	 * @return PropertySet instance
	 */
	@SafeVarargs
	static <P extends Property> PropertySet<P> of(P... properties) {
		return new DefaultPropertySet<>(properties);
	}

	/**
	 * Create a new PropertySet containing all given <code>properties</code> {@link Iterable} elements.
	 * @param <P> Type of the property managed by the property set
	 * @param properties Properties of the set
	 * @return PropertySet instance
	 */
	static <P extends Property> PropertySet<P> of(Iterable<P> properties) {
		return new DefaultPropertySet<>(properties);
	}

	/**
	 * Create a new {@link PropertySet} joining given <code>propertySet</code> with given additional
	 * <code>properties</code>.
	 * @param <P> Property type
	 * @param propertySet Source property set (not null)
	 * @param properties Additional properties
	 * @return A new {@link PropertySet} instance containing the properties of given <code>propertySet</code> and any
	 *         additional provided property
	 */
	@SafeVarargs
	static <P extends Property> PropertySet<P> of(PropertySet<? extends P> propertySet, P... properties) {
		ObjectUtils.argumentNotNull(propertySet, "Source property set must be not null");
		Builder<P> builder = builder();
		propertySet.forEach(p -> builder.add(p));
		if (properties != null && properties.length > 0) {
			for (P property : properties) {
				if (property != null) {
					builder.add(property);
				}
			}
		}
		return builder.build();
	}

	/**
	 * Join given {@link PropertySet}s and return a new PropertySet containing all the properties of given sets.
	 * @param <P> Actual property type
	 * @param propertySets PropertySet to join (not null and not empty)
	 * @return New PropertySet containing all the properties of given sets
	 */
	@SafeVarargs
	static <P extends Property> PropertySet<P> join(PropertySet<? extends P>... propertySets) {
		if (propertySets == null || propertySets.length == 0) {
			throw new IllegalArgumentException("No PropertySet to join");
		}
		Builder<P> builder = builder();
		for (PropertySet<? extends P> ps : propertySets) {
			ps.forEach(p -> builder.add(p));
		}
		return builder.build();
	}

	// Builder

	/**
	 * Builder to create {@link PropertySet} instances.
	 * @param <P> Concrete type of the properties contained in the set
	 */
	public interface Builder<P extends Property> {

		/**
		 * Add a property to the set.
		 * @param <PT> Actual property type
		 * @param property The property to add (not null)
		 * @return this
		 */
		<PT extends P> Builder<P> add(PT property);

		/**
		 * Add all the properties provided by given {@link Iterable} to the set.
		 * @param <PT> Actual property type
		 * @param properties Properties {@link Iterable} to add (not null)
		 * @return this
		 */
		<PT extends P> Builder<P> add(Iterable<PT> properties);

		/**
		 * Remove a property from the set.
		 * @param <PT> Actual property type
		 * @param property The property to remove (not null)
		 * @return this
		 */
		<PT extends P> Builder<P> remove(PT property);

		/**
		 * Remove all the properties provided by given {@link Iterable} from the set.
		 * @param <PT> Actual property type
		 * @param properties Properties {@link Iterable} to remove (not null)
		 * @return this
		 */
		<PT extends P> Builder<P> remove(Iterable<PT> properties);

		/**
		 * Build {@link PropertySet} instance
		 * @return PropertySet instance
		 */
		PropertySet<P> build();

	}

}
