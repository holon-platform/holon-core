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
package com.holonplatform.core;

import java.util.function.Supplier;

import com.holonplatform.core.internal.DefaultInitializer;

/**
 * Interface for lazy initialization of a generic value.
 *
 * @param <T> Type of the value to lazy initialize
 *
 * @since 5.0.0
 */
@FunctionalInterface
public interface Initializer<T> {

	/**
	 * Gets the value, initializing it if the value is not already initialized before returning to caller.
	 * @return the (lazy initialized) value
	 */
	T get();

	/**
	 * Create a new {@link Initializer} using given <code>initializer</code> {@link Supplier} for value initialization.
	 * <p>
	 * The returned {@link Initializer} is thread-safe, ensuring that the initializer {@link Supplier} is called exactly
	 * one time following one or several calls of {@link Initializer#get()} by any number of threads.
	 * </p>
	 * @param <T> Type of the value to lazy initialize
	 * @param initializer Supplier to initialize the value. Must never return <code>null</code>.
	 * @return A new {@link Initializer} instance
	 */
	static <T> Initializer<T> using(Supplier<T> initializer) {
		return new DefaultInitializer<>(initializer);
	}

	/**
	 * Lazy initializing method using given <code>initializer</code> to initialize the value if given <code>value</code>
	 * is <code>null</code>.
	 * @param <T> Type of the value to lazy initialize
	 * @param value Value to return if not <code>null</code>
	 * @param initializer Supplier to initialize the value. Must never return <code>null</code>.
	 * @return The given <code>value</code> if not <code>null</code>, or the initialized value using
	 *         {@link Supplier#get()} method of given <code>initializer</code>
	 */
	static <T> T init(T value, Supplier<T> initializer) {
		return (value != null) ? value : initializer.get();
	}

}
