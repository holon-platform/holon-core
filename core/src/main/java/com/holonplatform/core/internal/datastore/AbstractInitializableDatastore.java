/*
 * Copyright 2016-2018 Axioma srl.
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
package com.holonplatform.core.internal.datastore;

import java.lang.ref.WeakReference;
import java.util.ServiceLoader;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.DatastoreCommodityContext;
import com.holonplatform.core.datastore.DatastoreCommodityFactory;
import com.holonplatform.core.internal.utils.ClassUtils;

/**
 * {@link AbstractDatastore} extension with initialization support.
 *
 * @param <X> Concrete commodity context type
 * 
 * @since 5.2.0
 */
public abstract class AbstractInitializableDatastore<X extends DatastoreCommodityContext> extends AbstractDatastore<X> {

	/**
	 * Initialization ClassLoader
	 */
	private WeakReference<ClassLoader> initializationClassLoader;

	/**
	 * Whether the datastore was initialized
	 */
	private boolean initialized = false;

	/**
	 * Whether the datastore is initializing
	 */
	private boolean initializing = false;

	/**
	 * Constructor
	 * @param commodityFactoryType Base {@link DatastoreCommodityFactory} type to be loaded using standard Java
	 *        {@link ServiceLoader} extensions, or <code>null</code> to disable automatic factory registration.
	 * @param expressionResolverType Base {@link ExpressionResolver} type to be loaded using standard Java
	 *        {@link ServiceLoader} extensions, or <code>null</code> to disable automatic resolvers registration.
	 */
	@SuppressWarnings("rawtypes")
	public AbstractInitializableDatastore(Class<? extends DatastoreCommodityFactory> commodityFactoryType,
			Class<? extends ExpressionResolver> expressionResolverType) {
		super(commodityFactoryType, expressionResolverType);
	}

	/**
	 * Get the initialization ClassLoader.
	 * @return the initialization ClassLoader
	 */
	public ClassLoader getInitializationClassLoader() {
		return (initializationClassLoader != null) ? initializationClassLoader.get() : null;
	}

	/**
	 * Set the initialization ClassLoader.
	 * @param initializationClassLoader the ClassLoader to set
	 */
	public void setInitializationClassLoader(ClassLoader initializationClassLoader) {
		this.initializationClassLoader = new WeakReference<>(initializationClassLoader);
	}

	/**
	 * Get whether the Datastore was initialized.
	 * @return <code>true</code> if the Datastore is initialized, <code>false</code> otherwise
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Checks whether the Datastore is initialized, throwing an {@link IllegalStateException} if not.
	 */
	protected void checkInitialized() {
		if (!initializing) {
			if (!isInitialized()) {
				throw new IllegalStateException("Datastore was not initialized [" + getClass().getName() + "]");
			}
		}
	}

	/**
	 * Initialize the Datastore.
	 */
	public synchronized void initialize() {
		// check initialized
		if (!isInitialized()) {
			// check ClassLoader
			final ClassLoader initClassLoader = getInitializationClassLoader();
			// initialize
			try {
				initializing = true;
				initialized = initialize(
						(initClassLoader != null) ? initClassLoader : ClassUtils.getDefaultClassLoader());
			} finally {
				initializing = false;
			}
		}
	}

	/**
	 * Initialize the Datastore.
	 * @param classLoader The ClassLoader to use to load any required initialization class (not null)
	 * @return <code>true</code> if the initialization succeeded, <code>false</code> otherwise
	 */
	protected abstract boolean initialize(ClassLoader classLoader);

}
