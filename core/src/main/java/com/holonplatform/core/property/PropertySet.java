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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import com.holonplatform.core.Context;
import com.holonplatform.core.HasConfiguration;
import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.internal.property.DefaultPropertySet;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * This interface represent an {@link Iterable} and immutable set of
 * {@link Property}s.
 * 
 * @param <P> Concrete type of the properties contained in the set
 * 
 * @since 5.0.0
 */
@SuppressWarnings("rawtypes")
public interface PropertySet<P extends Property> extends Iterable<P>, HasConfiguration<ParameterSet> {

	/**
	 * Default {@link Context} resource reference
	 */
	public static final String CONTEXT_KEY = PropertySet.class.getName();

	/**
	 * The {@link PropertyConfiguration} attribute to use to declare the PropertySet
	 * for a PropertyBox type {@link Property}.
	 */
	public static final ConfigProperty<PropertySet> PROPERTY_CONFIGURATION_ATTRIBUTE = ConfigProperty
			.create(CONTEXT_KEY, PropertySet.class);

	/**
	 * Returns the number of properties in this set. If this set contains more than
	 * <tt>Integer.MAX_VALUE</tt> elements, returns <tt>Integer.MAX_VALUE</tt>.
	 * @return the number of properties in this set
	 */
	int size();

	/**
	 * Return <code>true</code> if this set contains the given
	 * <code>property</code>. If the given <code>property</code> is
	 * <code>null</code>, always returns <code>false</code>.
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
	 * Get the optional <em>identifier</em> properties which represent the unique
	 * identifier of this property set.
	 * <p>
	 * If one or more identifier property is declared, the values of such properties
	 * uniquely specify the instance of an object bound to this property set. The
	 * identifier properties of the set can be used, for example, as a dicriminator
	 * for object equality.
	 * </p>
	 * @return A {@link Set} of identifier properties, or an empty {@link Set} if no
	 *         identifier is provided
	 * @since 5.1.0
	 */
	Set<P> getIdentifiers();

	/**
	 * Get a {@link Stream} of the properties which represent the unique identifier
	 * of this property set.
	 * @return The identifier properties stream, empty if no identifier is provided
	 *         by this property set
	 * @since 5.1.0
	 */
	default Stream<P> identifiers() {
		return getIdentifiers().stream();
	}

	/**
	 * Get the first property which acts as property set identifier, if available.
	 * @return The first <em>identifier</em> property, or an empty Optional if no
	 *         identifier is provided
	 * @since 5.1.0
	 */
	default Optional<P> getFirstIdentifier() {
		return getIdentifiers().stream().findFirst();
	}

	/**
	 * Get the property set configuration, which can be used for extensions and
	 * application-specific purposes.
	 * <p>
	 * This configuration is considered as immutable. The configuration parameters
	 * has to setted at {@link PropertySet} build time, using the appropriate
	 * {@link Builder} methods: {@link Builder#configuration(String, Object)} and
	 * {@link Builder#configuration(ConfigProperty, Object)}.
	 * </p>
	 * @return The property set configuration {@link ParameterSet} (never null)
	 */
	@Override
	ParameterSet getConfiguration();

	/**
	 * Convert this PropertySet into a {@link List} of properties.
	 * @return List of set properties
	 */
	default List<P> asList() {
		return Collections.unmodifiableList(ConversionUtils.iterableAsList(this));
	}

	/**
	 * Execute given {@link Callable} <code>operation</code> on behalf of this
	 * PropertySet, i.e. binding this PropertySet as {@link Context} resource to
	 * current Thread, and removing the binding after operation execution.
	 * @param <V>       Operation result type
	 * @param operation Operation to execute
	 * @return Operation result
	 */
	default <V> V execute(final Callable<V> operation) {
		return Context.get().executeThreadBound(CONTEXT_KEY, this, operation);
	}

	// Builders

	/**
	 * Obtain a builder to create and populate a generic {@link PropertySet}.
	 * @return A new {@link PropertySet} builder
	 */
	static Builder<Property<?>> builder() {
		return new DefaultPropertySet.DefaultBuilder<>();
	}

	/**
	 * Obtain a builder to create and populate a {@link PropertySet} which supports
	 * given {@link Property} type.
	 * @param <P>          Property type
	 * @param propertyType The property type managed by the {@link PropertySet} to
	 *                     build (not null)
	 * @return A new {@link PropertySet} builder
	 * @since 5.1.0
	 */
	static <P extends Property> Builder<P> builder(Class<? extends P> propertyType) {
		ObjectUtils.argumentNotNull(propertyType, "Property type must be not null");
		return new DefaultPropertySet.DefaultBuilder<>();
	}

	/**
	 * Obtain a builder to create and populate a {@link PropertySet}, and add given
	 * <code>properties</code> to the property set to build.
	 * @param <P>        Property type
	 * @param properties Properties to initially add to the property set (not null)
	 * @return A new {@link PropertySet} builder
	 * @since 5.1.0
	 */
	@SafeVarargs
	static <P extends Property> Builder<Property<?>> builderOf(P... properties) {
		ObjectUtils.argumentNotNull(properties, "Properties must be not null");
		Builder<Property<?>> builder = builder();
		for (P property : properties) {
			builder.add(property);
		}
		return builder;
	}

	/**
	 * Get a builder to create a new {@link PropertySet} from given
	 * <code>propertySet</code>, cloning configuration, identifiers and properties
	 * of the provided <code>propertySet</code>.
	 * @param <P>         Property type
	 * @param propertySet The property set to clone for the new builder (not null)
	 * @return A new {@link PropertySet} builder
	 * @since 5.3.0
	 */
	static <P extends Property> Builder<Property<?>> builderOf(PropertySet<P> propertySet) {
		ObjectUtils.argumentNotNull(propertySet, "PropertySet must be not null");
		Builder<Property<?>> builder = builder();
		// identifiers
		propertySet.identifiers().forEach(i -> builder.withIdentifier(i));
		// configuration
		propertySet.getConfiguration().forEachParameter((n, v) -> builder.withConfiguration(n, v));
		// properties
		propertySet.forEach(p -> builder.add(p));
		return builder;
	}

	/**
	 * Create a new PropertySet containing given <code>properties</code>.
	 * @param <P>        Type of the property managed by the property set
	 * @param properties Properties of the set (not null)
	 * @return PropertySet instance
	 */
	@SafeVarargs
	static <P extends Property> PropertySet<P> of(P... properties) {
		ObjectUtils.argumentNotNull(properties, "Properties must be not null");
		return of(Arrays.asList(properties));
	}

	/**
	 * Create a new PropertySet containing all given <code>properties</code>
	 * {@link Iterable} elements.
	 * @param <P>        Type of the property managed by the property set
	 * @param properties Properties of the set (not null)
	 * @return PropertySet instance
	 */
	static <P extends Property> PropertySet<P> of(Iterable<P> properties) {
		ObjectUtils.argumentNotNull(properties, "Properties must be not null");
		final Builder<P> builder = new DefaultPropertySet.DefaultBuilder<>();
		for (P property : properties) {
			if (property != null) {
				builder.add(property);
			}
		}
		return builder.build();
	}

	/**
	 * Create a new {@link PropertySet} joining given <code>propertySet</code> with
	 * given additional <code>properties</code>.
	 * <p>
	 * Any identifier property declared by given <code>propertySet</code> will be an
	 * identifier of the new property set too. The original property set
	 * configuration is cloned to the new {@link PropertySet} instance
	 * configuration.
	 * </p>
	 * @param <P>         Property type
	 * @param propertySet Source property set (not null)
	 * @param properties  Additional properties
	 * @return A new {@link PropertySet} instance containing the properties of given
	 *         <code>propertySet</code> and any additional provided property
	 */
	@SuppressWarnings("unchecked")
	@SafeVarargs
	static <P extends Property> PropertySet<P> of(PropertySet<? extends P> propertySet, P... properties) {
		ObjectUtils.argumentNotNull(propertySet, "Source property set must be not null");
		final Builder builder = builder();
		propertySet.forEach(p -> builder.add(p));
		// identifiers
		propertySet.identifiers().forEach(i -> builder.identifier(i));
		// configuration
		if (propertySet.getConfiguration().hasParameters()) {
			propertySet.getConfiguration().forEachParameter((n, v) -> builder.configuration(n, v));
		}
		// additional properties
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
	 * Join given {@link PropertySet}s and return a new PropertySet containing all
	 * the properties of given sets.
	 * @param <P>          Actual property type
	 * @param propertySets PropertySet to join (not null and not empty)
	 * @return New PropertySet containing all the properties of given sets
	 * @deprecated Using this method causes the loss of any property set
	 *             configuration and/or identifier property declaration. Use the
	 *             default PropertySet builder to compose a new PropertySet from
	 *             different property sources.
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	@SafeVarargs
	static <P extends Property> PropertySet<P> join(PropertySet<? extends P>... propertySets) {
		if (propertySets == null || propertySets.length == 0) {
			throw new IllegalArgumentException("No PropertySet to join");
		}
		Builder builder = builder();
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
		 * @param <PT>     Actual property type
		 * @param property The property to add (not null)
		 * @return this
		 */
		<PT extends P> Builder<P> add(PT property);

		/**
		 * Add all the properties provided by given {@link Iterable} to the set.
		 * @param <PT>       Actual property type
		 * @param properties Properties {@link Iterable} to add (not null)
		 * @return this
		 */
		<PT extends P> Builder<P> add(Iterable<PT> properties);

		/**
		 * Remove a property from the set.
		 * @param <PT>     Actual property type
		 * @param property The property to remove (not null)
		 * @return this
		 */
		<PT extends P> Builder<P> remove(PT property);

		/**
		 * Remove all the properties provided by given {@link Iterable} from the set.
		 * @param <PT>       Actual property type
		 * @param properties Properties {@link Iterable} to remove (not null)
		 * @return this
		 */
		<PT extends P> Builder<P> remove(Iterable<PT> properties);

		/**
		 * Add given <code>property</code> to the property set identifiers.
		 * <p>
		 * The property to declare as identifier must be already present in the property
		 * set.
		 * </p>
		 * @param <PT>     Actual property type
		 * @param property The property to declare as property set identifier (not null)
		 * @return this
		 * @throws IllegalStateException If the property to declare as identifier is not
		 *                               part of the property set
		 * @since 5.3.0
		 */
		<PT extends P> Builder<P> withIdentifier(PT property);

		/**
		 * Add given <code>property</code> to the property set identifiers.
		 * <p>
		 * The property to declare as identifier must be already present in the property
		 * set.
		 * </p>
		 * @param <PT>     Actual property type
		 * @param property The property to declare as property set identifier (not null)
		 * @return this
		 * @throws IllegalStateException If the property to declare as identifier is not
		 *                               part of the property set
		 * @since 5.1.0
		 * @deprecated Use {@link #withIdentifier(Property)}
		 */
		@Deprecated
		default <PT extends P> Builder<P> identifier(PT property) {
			return withIdentifier(property);
		}

		/**
		 * Set given <code>properties</code> as property set identifiers. Any previously
		 * declared identifier property will be replaced by given identifier properties.
		 * <p>
		 * The properties to declare as identifiers must be already present in the
		 * property set.
		 * </p>
		 * @param <PT>       Actual property type
		 * @param properties The properties to declare as property set identifiers (not
		 *                   null)
		 * @return this
		 * @throws IllegalStateException If one of the properties to declare as
		 *                               identifier is not part of the property set
		 * @since 5.1.0
		 */
		<PT extends P> Builder<P> identifiers(Iterable<PT> properties);

		/**
		 * Set given <code>properties</code> as property set identifiers. Any previously
		 * declared identifier property will be replaced by given identifier properties.
		 * <p>
		 * The properties to declare as identifiers must be already present in the
		 * property set.
		 * </p>
		 * @param properties The properties to declare as property set identifiers (not
		 *                   null)
		 * @return this
		 * @throws IllegalStateException If one of the properties to declare as
		 *                               identifier is not part of the property set
		 * @since 5.2.2
		 */
		@SuppressWarnings("unchecked")
		default Builder<P> identifiers(P... properties) {
			return identifiers(Arrays.asList(properties));
		}

		/**
		 * Add a {@link PropertySet} configuration parameter
		 * @param name  The parameter name to add (not null)
		 * @param value The configuration parameter value
		 * @return this
		 */
		Builder<P> withConfiguration(String name, Object value);

		/**
		 * Add a {@link PropertySet} configuration parameter
		 * @param name  The parameter name to add (not null)
		 * @param value The configuration parameter value
		 * @return this
		 * @deprecated Use {@link #withConfiguration(String, Object)}
		 */
		@Deprecated
		default Builder<P> configuration(String name, Object value) {
			return withConfiguration(name, value);
		}

		/**
		 * Add a {@link PropertySet} configuration parameter using a
		 * {@link ConfigProperty}, with {@link ConfigProperty#getKey()} as parameter
		 * name.
		 * @param <C>                   Config property type
		 * @param configurationProperty The {@link ConfigProperty} to add (not null)
		 * @param value                 The configuration property value
		 * @return this
		 */
		default <C> Builder<P> withConfiguration(ConfigProperty<C> configurationProperty, C value) {
			ObjectUtils.argumentNotNull(configurationProperty, "Configuration property must be not null");
			return withConfiguration(configurationProperty.getKey(), value);
		}

		/**
		 * Add a {@link PropertySet} configuration parameter using a
		 * {@link ConfigProperty}, with {@link ConfigProperty#getKey()} as parameter
		 * name.
		 * @param <C>                   Config property type
		 * @param configurationProperty The {@link ConfigProperty} to add (not null)
		 * @param value                 The configuration property value
		 * @return this
		 * @deprecated Use {@link #withConfiguration(ConfigProperty, Object)}
		 */
		@Deprecated
		default <C> Builder<P> configuration(ConfigProperty<C> configurationProperty, C value) {
			ObjectUtils.argumentNotNull(configurationProperty, "Configuration property must be not null");
			return withConfiguration(configurationProperty.getKey(), value);
		}

		/**
		 * Build {@link PropertySet} instance
		 * @return PropertySet instance
		 */
		PropertySet<P> build();

	}

}
