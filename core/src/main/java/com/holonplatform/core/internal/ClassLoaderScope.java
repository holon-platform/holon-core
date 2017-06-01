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

import java.util.Optional;

import com.holonplatform.core.Context;
import com.holonplatform.core.ContextScope;
import com.holonplatform.core.exceptions.TypeMismatchException;

/**
 * A {@link ContextScope} for {@link ClassLoader}-scoped <em>singleton</em> context resources.
 * 
 * @since 5.0.0
 */
public class ClassLoaderScope implements ContextScope {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = CoreLogger.create();

	/**
	 * Default scope order
	 */
	public static final int DEFAULT_ORDER = Integer.MAX_VALUE - 1000;

	/**
	 * Resources
	 */
	private final ContextResourceMap resources;

	public ClassLoaderScope() {
		super();
		this.resources = new ContextResourceMap(Context.CLASSLOADER_SCOPE_NAME, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#getName()
	 */
	@Override
	public String getName() {
		return Context.CLASSLOADER_SCOPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#getOrder()
	 */
	@Override
	public int getOrder() {
		return DEFAULT_ORDER;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> Optional<T> get(String resourceKey, Class<T> resourceType) throws TypeMismatchException {
		LOGGER.debug(() -> "Get resource with key [" + resourceKey + "] and type [" + resourceType + "]");
		final Optional<T> resource = Optional.ofNullable(resources.get(resourceKey, resourceType));
		LOGGER.debug(() -> "Get resource with key [" + resourceKey + "] and type [" + resourceType + "]: "
				+ (resource.isPresent() ? "found [" + resource.get() + "]" : "not found"));
		return resource;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> Optional<T> put(String resourceKey, T value) throws UnsupportedOperationException {
		LOGGER.debug(() -> "Put resource with key [" + resourceKey + "]: [" + value + "]");
		return Optional.ofNullable(resources.put(resourceKey, value));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#putIfAbsent(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> Optional<T> putIfAbsent(String resourceKey, T value) throws UnsupportedOperationException {
		LOGGER.debug(() -> "Put resource if absent with key [" + resourceKey + "]: [" + value + "]");
		final T resource = resources.putIfAbsent(resourceKey, value);
		LOGGER.debug(() -> "Resource with key [" + resourceKey + "] and [" + value + "] "
				+ ((resource != null) ? "replaced the resource [" + resource + "]" : " was not present"));
		return Optional.ofNullable(resource);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#remove(java.lang.String)
	 */
	@Override
	public boolean remove(String resourceKey) throws UnsupportedOperationException {
		final boolean removed = resources.remove(resourceKey);
		LOGGER.debug(() -> "Remove resource with key [" + resourceKey + "] - was present:" + removed);
		return removed;
	}

}
