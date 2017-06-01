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
package com.holonplatform.core.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.holonplatform.core.Context;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;

/**
 * Helper class to manage {@link Context} resources by key.
 * 
 * @since 5.0.0
 */
public final class ContextResourceMap implements Serializable {

	private static final long serialVersionUID = -4229211775339352864L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = CoreLogger.create();

	private final String scopeName;

	private final Map<String, Object> resources;

	/**
	 * Construct a ContextResourceMap with default initial capacity
	 * @param scopeName Scope name
	 * @param threadSafe <code>true</code> to build a thread-safe resource map
	 */
	public ContextResourceMap(String scopeName, boolean threadSafe) {
		this(scopeName, threadSafe, 8);
	}

	/**
	 * Construct a ContextResourceMap
	 * @param scopeName Scope name
	 * @param threadSafe <code>true</code> to build a thread-safe resource map
	 * @param initialCapacity Initial capacity
	 */
	public ContextResourceMap(String scopeName, boolean threadSafe, int initialCapacity) {
		super();
		this.scopeName = scopeName;
		this.resources = threadSafe ? new ConcurrentHashMap<>(initialCapacity, 0.9f, 1)
				: new HashMap<>(initialCapacity);
	}

	/**
	 * Get a value of given <code>type</code> identified by given <code>key</code>.
	 * @param key Resource key
	 * @param type Resource type
	 * @return Resource value, or <code>null</code> if not found
	 * @param <T> Resource type
	 * @throws TypeMismatchException Expected and actual resource type mismatch
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> type) throws TypeMismatchException {
		ObjectUtils.argumentNotNull(key, "Resource key must be not null");
		ObjectUtils.argumentNotNull(type, "Resource type must be not null");

		LOGGER.debug(() -> "<" + scopeName + "> get resource with key [" + key + "]");

		Object resource = resources.get(key);
		if (resource != null) {
			// check type
			if (!TypeUtils.isAssignable(resource.getClass(), type)) {
				throw new TypeMismatchException(
						"<" + scopeName + "> Actual resource type [" + resource.getClass().getName()
								+ "] and required resource type [" + type.getName() + "] mismatch");
			}

			LOGGER.debug(() -> "<" + scopeName + "> Retrieved resource value of type [" + resource.getClass().getName()
					+ "] for key [" + key + "]");

			return (T) resource;
		}
		return null;
	}

	/**
	 * Bind the given resource key with the specified value
	 * @param key Resource key (not null)
	 * @param value Resource instance. If <code>null</code>, the mapping will be removed
	 * @param <T> Resource type
	 * @return Previous bound instance, if any
	 */
	@SuppressWarnings("unchecked")
	public <T> T put(String key, T value) {
		ObjectUtils.argumentNotNull(key, "Resource key must be not null");

		Object previous = null;
		if (value != null) {
			previous = resources.put(key, value);

			LOGGER.debug(() -> "<" + scopeName + "> Bound resource of type [" + value.getClass().getName()
					+ "] to key [" + key + "]");

		} else {
			previous = resources.remove(key);
		}
		try {
			return (T) previous;
		} catch (@SuppressWarnings("unused") Exception e) {
			// ignore type mismatch
			return null;
		}
	}

	/**
	 * Bind the given resource key with the specified value, if not already bound
	 * @param key Resource key (not null)
	 * @param value Resource instance
	 * @param <T> Resource type
	 * @return Previous value, or <code>null</code> if no value was bound and the new instance it's been mapped to the
	 *         key
	 */
	@SuppressWarnings("unchecked")
	public <T> T putIfAbsent(String key, T value) {
		ObjectUtils.argumentNotNull(key, "Resource key must be not null");
		if (value != null) {
			T previous = (T) resources.putIfAbsent(key, value);

			if (previous == null) {
				LOGGER.debug(() -> "<" + scopeName + "> Bound resource of type [" + value.getClass().getName()
						+ "] to key [" + key + "]");
			}

			return previous;
		}
		return null;
	}

	/**
	 * Removes the resource instance bound to given key
	 * @param key Resource key (not null)
	 * @return <code>true</code> if found and removed
	 */
	public boolean remove(String key) {
		ObjectUtils.argumentNotNull(key, "Resource key must be not null");
		Object removed = resources.remove(key);

		if (removed != null) {
			LOGGER.debug(() -> "<" + scopeName + "> Removed resource with key [" + key + "]");
		}

		return removed != null;
	}

	/**
	 * Clears all resource bindings
	 */
	public void clear() {
		resources.clear();

		LOGGER.debug(() -> "Context scope <" + scopeName + "> cleared");
	}

}
