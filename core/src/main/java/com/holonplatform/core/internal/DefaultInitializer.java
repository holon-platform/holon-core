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
package com.holonplatform.core.internal;

import java.util.Objects;
import java.util.function.Supplier;

import com.holonplatform.core.Initializer;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link Initializer} implementation.
 *
 * @param <T> Type of the value to lazy initialize
 *
 * @since 5.0.0
 */
public class DefaultInitializer<T> implements Initializer<T> {

	/**
	 * Initializer
	 */
	private final Supplier<T> initializer;

	/**
	 * Value
	 */
	private volatile T value;

	/**
	 * Constructor
	 * @param initializer Value initializer (not null)
	 */
	public DefaultInitializer(Supplier<T> initializer) {
		super();
		ObjectUtils.argumentNotNull(initializer, "Initializer must be not null");
		this.initializer = initializer;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Initializer#get()
	 */
	@Override
	public T get() {
		final T result = value;
		return result != null ? result : getOrInitialize();
	}

	/**
	 * Get the value if already initialized (not <code>null</code>), or initialize it using the initializer before
	 * return the value.
	 * @return Current value
	 */
	private synchronized T getOrInitialize() {
		if (value != null) {
			return value;
		}
		value = Objects.requireNonNull(initializer.get(),
				"Supplier for lazy initialization returned a null value [" + initializer + "]");
		return value;
	}

}
