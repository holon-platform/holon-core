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
package com.holonplatform.core;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.holonplatform.core.internal.DefaultFinalPath;
import com.holonplatform.core.internal.DefaultPath;

/**
 * Represents a generic, typed path for a data structure attribute which can be identified by a {@link String} name.
 * <p>
 * If the path belongs to a hierarchical structure, the parent path can be provided using {@link #getParent()}.
 * </p>
 *
 * @param <T> Type of the element identified by this path
 *
 * @since 5.0.0
 */
public interface Path<T> extends TypedExpression<T>, DataMappable, Serializable {

	/**
	 * Separator character used as separator between path hierarchy elements when composing or parsing a path name.
	 */
	public static final char PATH_HIERARCHY_SEPARATOR = '.';

	/**
	 * Gets the path name
	 * @return Path name (should never be null)
	 */
	String getName();

	/**
	 * Gets the parent path
	 * @return Optional parent path, empty if none
	 */
	Optional<Path<?>> getParent();

	/**
	 * Checks whether this path is a root path, i.e. it has no parent path.
	 * @return <code>true</code> if it is a root path, <code>false</code> otherwise
	 */
	default boolean isRootPath() {
		return !getParent().isPresent();
	}

	/**
	 * Returns a {@link Stream} of path hierarchy, starting form this path and walking through parent paths, if any.
	 * @return Path hierarchy stream
	 */
	default Stream<Path<?>> stream() {
		final Iterator<Path<?>> iterator = new Iterator<Path<?>>() {

			private Path<?> path = Path.this;

			@Override
			public boolean hasNext() {
				return path != null;
			}

			@Override
			public Path<?> next() {
				Path<?> current = path;
				path = current.getParent().orElse(null);
				return current;
			}

		};
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
	}

	/**
	 * Gets the path full name, including any parent path, separated by a dot <code>.</code> character.
	 * @return Path full name
	 */
	default String fullName() {
		return getParent().map(pr -> stream().map(p -> p.getName())
				.collect(LinkedList<String>::new, LinkedList::addFirst, (a, b) -> a.addAll(0, b)).stream()
				.collect(Collectors.joining("."))).orElse(getName());
	}

	/**
	 * Gets the <em>relative</em> path name, separated by a dot <code>.</code> character, i.e. exclude any
	 * {@link FinalPath} path instance from path name composition.
	 * @return Path relative name, en empty String if this Path is a {@link FinalPath} itself.
	 */
	default String relativeName() {
		return getParent()
				.map(pr -> stream().filter(p -> !FinalPath.class.isAssignableFrom(p.getClass())).map(p -> p.getName())
						.collect(LinkedList<String>::new, LinkedList::addFirst, (a, b) -> a.addAll(0, b)).stream()
						.collect(Collectors.joining(".")))
				.orElse(FinalPath.class.isAssignableFrom(this.getClass()) ? "" : getName());
	}

	/**
	 * Create a default {@link Path} implementation with given <code>name</code> and <code>type</code>.
	 * <p>
	 * The returned type is a {@link Path} instance implementing also {@link Builder} to provide instance configuration
	 * methods.
	 * </p>
	 * @param <T> Path type
	 * @param name Path name (not null)
	 * @param type Path type (not null)
	 * @return New PathBuilder path instance
	 */
	static <T> PathBuilder<T> of(String name, Class<? extends T> type) {
		return new DefaultPath<>(name, type);
	}

	/**
	 * Path builder.
	 * @param <T> Path type
	 */
	public interface PathBuilder<T> extends Builder<T, PathBuilder<T>>, Path<T> {

	}

	/**
	 * Base path builder.
	 * @param <T> Path type
	 * @param <B> Concrete builder type
	 */
	public interface Builder<T, B extends Builder<T, B>> extends DataMappable.Builder<B> {

		/**
		 * Sets the parent path
		 * @param parent The parent path to set
		 * @return this
		 * @throws UnsupportedOperationException If this path is a final path, i.e. does not supports parent paths
		 */
		B parent(Path<?> parent);

	}

	/**
	 * Represents a <em>final</em> path, i.e. a path which do not support any parent path.
	 * 
	 * @param <T> Path type
	 */
	public interface FinalPath<T> extends Path<T> {

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.Path#getParent()
		 */
		@Override
		default Optional<Path<?>> getParent() {
			return Optional.empty();
		}

		/**
		 * Create a default {@link FinalPath} implementation with given <code>name</code> and <code>type</code>.
		 * @param <T> Path type
		 * @param name Path name (not null)
		 * @param type Path type (not null)
		 * @return New PathBuilder path instance
		 */
		static <T> FinalPathBuilder<T> of(String name, Class<? extends T> type) {
			return new DefaultFinalPath<>(name, type);
		}

		/**
		 * {@link FinalPath} builder.
		 * @param <T> Path type
		 */
		public interface FinalPathBuilder<T> extends PathBuilder<T>, FinalPath<T> {

		}

	}

}
