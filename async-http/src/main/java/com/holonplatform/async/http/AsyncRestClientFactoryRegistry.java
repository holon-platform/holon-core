/*
 * Copyright 2000-2017 Holon TDCN.
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
package com.holonplatform.async.http;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

import javax.annotation.Priority;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.http.exceptions.RestClientCreationException;
import com.holonplatform.http.internal.HttpLogger;

/**
 * {@link AsyncRestClientFactory} registry.
 *
 * @since 5.2.0
 */
public enum AsyncRestClientFactoryRegistry {

	INSTANCE;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = HttpLogger.create();

	/**
	 * {@link Priority} based comparator.
	 */
	private static final Comparator<AsyncRestClientFactory> PRIORITY_COMPARATOR = Comparator.comparingInt(
			p -> p.getClass().isAnnotationPresent(Priority.class) ? p.getClass().getAnnotation(Priority.class).value()
					: AsyncRestClientFactory.DEFAULT_PRIORITY);

	/**
	 * The {@link AsyncRestClientFactory}s by class name organized by the {@link ClassLoader} was used to load them.
	 */
	private final WeakHashMap<ClassLoader, List<AsyncRestClientFactory>> factories;

	private AsyncRestClientFactoryRegistry() {
		factories = new WeakHashMap<>();
	}

	/**
	 * Get a {@link AsyncRestClient} instance for given <code>fullyQualifiedClassName</code> and {@link ClassLoader}
	 * using a suitable {@link AsyncRestClientFactory}.
	 * @param fullyQualifiedClassName {@link AsyncRestClient} class name to obtain, or <code>null</code> for the default
	 *        one
	 * @param classLoader ClassLoader for which to obtain the {@link AsyncRestClient}
	 * @return A new {@link AsyncRestClient} instance for given <code>fullyQualifiedClassName</code> (or the default one
	 *         if <code>null</code>) and {@link ClassLoader}
	 * @throws RestClientCreationException If no {@link AsyncRestClientFactory} available, or none of the available
	 *         factories returned a not <code>null</code> instance or a creation error occurred
	 */
	public AsyncRestClient createRestClient(String fullyQualifiedClassName, ClassLoader classLoader) {
		ClassLoader cl = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
		List<AsyncRestClientFactory> restClientFactories = getRestClientFactories(fullyQualifiedClassName, cl);
		if (restClientFactories.isEmpty()) {
			throw new RestClientCreationException("No AsyncRestClientFactory available for ClassLoader [" + cl + "]"
					+ ((fullyQualifiedClassName != null)
							? " and AsyncRestClient implementation class name [" + fullyQualifiedClassName + "]"
							: ""));
		}
		AsyncRestClient restClient = null;
		for (AsyncRestClientFactory factory : restClientFactories) {
			restClient = factory.create(cl);
			if (restClient != null) {
				break;
			}
		}
		if (restClient == null) {
			throw new RestClientCreationException("No AsyncRestClient available for ClassLoader [" + cl + "]"
					+ ((fullyQualifiedClassName != null)
							? " and AsyncRestClient implementation class name [" + fullyQualifiedClassName + "]"
							: ""));
		}
		return restClient;
	}

	/**
	 * Get the {@link AsyncRestClientFactory} for given <code>fullyQualifiedClassName</code> {@link AsyncRestClient}
	 * instance and {@link ClassLoader}.
	 * @param fullyQualifiedClassName {@link AsyncRestClientFactory} class name to obtain, or <code>null</code> for the
	 *        default one
	 * @param classLoader ClassLoader for which to obtain the factory
	 * @return {@link AsyncRestClientFactory} for given <code>fullyQualifiedClassName</code> (or the default one if
	 *         <code>null</code>) and {@link ClassLoader}
	 * @throws RestClientCreationException If no {@link AsyncRestClientFactory} available or a creation error occurred
	 */
	public List<AsyncRestClientFactory> getRestClientFactories(String fullyQualifiedClassName,
			ClassLoader classLoader) {
		ClassLoader serviceClassLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
		List<AsyncRestClientFactory> restClientFactories = getRestClientFactories(serviceClassLoader);
		if (fullyQualifiedClassName != null) {
			List<AsyncRestClientFactory> byName = new LinkedList<>();
			for (AsyncRestClientFactory factory : restClientFactories) {
				Class<?> cls = factory.getRestClientImplementationClass();
				if (cls != null && cls.getName().equals(fullyQualifiedClassName)) {
					byName.add(factory);
				}
			}
			return byName;
		}
		return restClientFactories;
	}

	/**
	 * Obtain the {@link AsyncRestClientFactory}s that are available via the specified {@link ClassLoader}.
	 * @param classLoader the {@link ClassLoader} of the returned {@link AsyncRestClientFactory}s
	 * @return an list of {@link AsyncRestClientFactory}s loaded by the specified {@link ClassLoader}
	 */
	private synchronized List<AsyncRestClientFactory> getRestClientFactories(ClassLoader classLoader) {

		final ClassLoader serviceClassLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
		List<AsyncRestClientFactory> restClientFactories = factories.get(serviceClassLoader);

		if (restClientFactories == null) {
			restClientFactories = AccessController.doPrivileged(new PrivilegedAction<List<AsyncRestClientFactory>>() {
				@Override
				public List<AsyncRestClientFactory> run() {
					LinkedList<AsyncRestClientFactory> result = new LinkedList<>();
					ServiceLoader<AsyncRestClientFactory> serviceLoader = ServiceLoader
							.load(AsyncRestClientFactory.class, serviceClassLoader);
					for (AsyncRestClientFactory factory : serviceLoader) {
						result.add(factory);
						LOGGER.debug(() -> "Loaded and registered AsyncRestClientFactory ["
								+ factory.getClass().getName() + "]");
					}
					// sort
					Collections.sort(result, PRIORITY_COMPARATOR);
					return result;
				}
			});
			factories.put(serviceClassLoader, restClientFactories);
		}
		return restClientFactories;
	}

}
