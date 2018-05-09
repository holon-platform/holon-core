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

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.holonplatform.core.Path;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.property.DefaultPathPropertySetAdapter.DefaultPathPropertySetAdapterBuilder;

/**
 * Adapter to use {@link Path} expressions to inspect a {@link PropertySet}.
 * <p>
 * The {@link Property} definitions of the property set can be inspected and obtained:
 * <ul>
 * <li>By {@link Path}, checking if a {@link Property} implements {@link Path} and if its path name corresponds to a
 * given {@link Path} name.</li>
 * <li>By name, using the {@link Property} name (see {@link Property#getName()}) to look for a matching property
 * definition.</li>
 * </ul>
 * <p>
 * A {@link PathConverter} and a {@link PathMatcher} definition can be provided to the builder API to control and
 * customize how a {@link Property} is converted to a {@link Path} and the path name matching strategy to use. See the
 * {@link Builder} API for details.
 * </p>
 * 
 * @since 5.1.0
 */
public interface PathPropertySetAdapter {

	/**
	 * Return <code>true</code> if a property which corresponds to given path is available in the property set.
	 * @param path The path to check (not null)
	 * @return <code>true</code> if the property set contains a {@link Property} which corresponds to the specified
	 *         path, <code>false</code> otherwise
	 */
	boolean contains(Path<?> path);

	/**
	 * Check if a {@link Property} which corresponds to given <code>path</code> is present in the property set.
	 * @param <T> Path type
	 * @param path The path to check (not null)
	 * @return If a {@link Property} which corresponds to given <code>path</code> is present in the property set,
	 *         returns the property. Otherwise an empty Optional is returned.
	 */
	<T> Optional<Property<T>> getProperty(Path<T> path);

	/**
	 * Check if given <code>property</code> is available in property set and if it can be represented as a {@link Path}.
	 * @param <T> Path type
	 * @param property The property to check (not null)
	 * @return If given <code>property</code> is available in property set and if it can be represented as a
	 *         {@link Path}, returns the property as a {@link Path}.Otherwise an empty Optional is returned.
	 */
	<T> Optional<Path<T>> getPath(Property<T> property);

	/**
	 * If the property set declares a set of identifiers, and each identifier property can be represented as a
	 * {@link Path}, returns the set of identifiers represented as paths.
	 * @return A {@link Set} of identifier paths, or an empty {@link Set} if no identifier is provided by the property
	 *         set or at least one of the identifier properties cannot be represented as a path
	 */
	Set<Path<?>> getPathIdentifiers();

	/**
	 * Returns a stream of {@link Path}s, containing the {@link Path} representation of all the properties in the
	 * property set which can be represented as a {@link Path}.
	 * @return Property set paths stream
	 */
	Stream<Path<?>> pathStream();

	// ------- by name

	/**
	 * Checks if a {@link Property} with given <code>name</code> is present in the property set.
	 * @param name Property name (not null)
	 * @return <code>true</code> if a {@link Property} with given <code>name</code> is present, <code>false</code>
	 *         otherwise
	 * @since 5.2.0
	 */
	boolean contains(String name);

	/**
	 * Get the {@link Property} with given <code>name</code>, if available in the property set.
	 * @param <T> Property type
	 * @param name Property name (not null)
	 * @param type Expected property type (not null)
	 * @return The {@link Property} with given <code>name</code>, if available
	 * @throws TypeMismatchException If a property with given name is present but its type is not compatible with the
	 *         expected property type
	 * @since 5.2.0
	 */
	<T> Optional<Property<T>> getProperty(String name, Class<T> type);

	/**
	 * Get the {@link Property} with given <code>name</code>, if available in the property set.
	 * @param name Property name (not null)
	 * @return The {@link Property} with given <code>name</code>, if available
	 * @since 5.2.0
	 */
	Optional<Property<?>> getProperty(String name);

	/**
	 * Returns a stream of the {@link Property} names available in the property set.
	 * @return Property names stream
	 * @since 5.2.0
	 */
	Stream<String> nameStream();

	// ------- Builders

	/**
	 * Create a new {@link PathPropertySetAdapter}.
	 * @param propertySet The property set to use (not null)
	 * @return A new {@link PathPropertySetAdapter}
	 */
	static PathPropertySetAdapter create(PropertySet<?> propertySet) {
		return builder(propertySet).build();
	}

	/**
	 * Create a new {@link PathPropertySetAdapter} builder.
	 * @param propertySet The property set to use (not null)
	 * @return A new {@link PathPropertySetAdapter} builder
	 */
	static PathPropertySetAdapterBuilder builder(PropertySet<?> propertySet) {
		return new DefaultPathPropertySetAdapterBuilder(propertySet);
	}

	/**
	 * Converter to obtain a {@link Path} from a {@link Property}.
	 */
	public interface PathConverter {

		/**
		 * Get the {@link Path} representation of given {@link Property}, if the property can be represented by a path.
		 * @param <T> Property type
		 * @param property The property for which to obtain a path
		 * @return Optional property path representation
		 */
		<T> Optional<Path<T>> convert(Property<T> property);

	}

	/**
	 * Function to implement a {@link Path} matching strategy.
	 */
	@FunctionalInterface
	public interface PathMatcher {

		/**
		 * Checks whether the given paths <em>match</em>, i.e. they are to be considered equal.
		 * @param path First path
		 * @param otherPath Second path
		 * @return <code>true</code> if given paths match
		 */
		boolean match(Path<?> path, Path<?> otherPath);

	}

	/**
	 * Default builder.
	 */
	public interface PathPropertySetAdapterBuilder
			extends Builder<PathPropertySetAdapterBuilder, PathPropertySetAdapter> {

	}

	/**
	 * {@link PathPropertySetAdapter} builder.
	 *
	 * @param <B> Concrete builder type
	 * @param <A> PathPropertySetAdapter type
	 */
	public interface Builder<B extends Builder<B, A>, A extends PathPropertySetAdapter> {

		/**
		 * Set the {@link PathConverter} to use to convert a {@link Property} into a {@link Path} representation.
		 * <p>
		 * By default, the {@link Property} path representation is obtained checking if the {@link Property} extends the
		 * {@link Path} interface and, in this case, using the property definition itself as path representation.
		 * </p>
		 * @param pathConverter The path converter to set (not null)
		 * @return this
		 */
		B pathConverter(PathConverter pathConverter);

		/**
		 * Set the {@link PathMatcher} to use for {@link Path} matching strategy.
		 * <p>
		 * By default, the {@link Path#relativeName()} method is used to obtain the path names and the names are matched
		 * using the default <code>equals</code> strategy.
		 * </p>
		 * @param pathMatcher The path matcher to set (not null)
		 * @return this
		 */
		B pathMatcher(PathMatcher pathMatcher);

		/**
		 * Use a case insensitive matcher to compare path names.
		 * @return this
		 */
		B withCaseInsensitivePathMatcher();

		/**
		 * Build the adapter instance.
		 * @return A new adapter
		 */
		A build();

	}

}
