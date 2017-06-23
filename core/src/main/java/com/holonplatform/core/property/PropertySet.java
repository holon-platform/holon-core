/*
 * Copyright 2000-2016 Holon TDCN.
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
	 * Build a new {@link PropertySet} joining the properties of this property set with given <code>properties</code>.
	 * <p>
	 * If the underlying set implementation supports property ordering, the given properties will be added after the
	 * current set properties.
	 * </p>
	 * @param properties Properties to add (not null)
	 * @return A new {@link PropertySet} containing all the properties of this set and all given <code>properties</code>
	 */
	@SuppressWarnings("unchecked")
	default PropertySet<P> join(P... properties) {
		return join(false, properties);
	}

	/**
	 * Build a new {@link PropertySet} joining the properties of this property set with given <code>properties</code>.
	 * @param before <code>true</code> to add the given properties before the current set properties, <code>false</code>
	 *        to add them after the current set properties
	 * @param properties Properties to add (not null)
	 * @return A new {@link PropertySet} containing all the properties of this set and all given <code>properties</code>
	 */
	@SuppressWarnings("unchecked")
	default PropertySet<P> join(boolean before, P... properties) {
		ObjectUtils.argumentNotNull(properties, "Properties to join must be not null");
		Builder<P> builder = builder();
		if (before) {
			builder.add(properties);
		}
		forEach(p -> builder.add(p));
		if (!before) {
			builder.add(properties);
		}
		return builder.build();
	}

	/**
	 * Build a new {@link PropertySet} containing all the properties of this property set but the given
	 * <code>properties</code>.
	 * @param properties Properties to exclude (not null)
	 * @return A new {@link PropertySet} with the properties of this set excluding given <code>properties</code>
	 */
	@SuppressWarnings("unchecked")
	default PropertySet<P> exclude(P... properties) {
		ObjectUtils.argumentNotNull(properties, "Properties to exclude must be not null");
		Builder<P> builder = builder();
		forEach(p -> {
			if (!ObjectUtils.contains(properties, p))
				builder.add(p);
		});
		return builder.build();
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
		 * Add all the properties in the given array to the set.
		 * @param <PT> Actual property type
		 * @param properties The properties to add (not null)
		 * @return this
		 */
		<PT extends P> Builder<P> add(PT[] properties);

		/**
		 * Add all the properties provided by given {@link Iterable} to the set.
		 * @param <PT> Actual property type
		 * @param properties Properties {@link Iterable} to add (not null)
		 * @return this
		 */
		<PT extends P> Builder<P> add(Iterable<PT> properties);

		/**
		 * Build {@link PropertySet} instance
		 * @return PropertySet instance
		 */
		PropertySet<P> build();

	}

}
