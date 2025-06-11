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
package com.holonplatform.http.internal.rest;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

import jakarta.annotation.Priority;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.http.exceptions.RestClientCreationException;
import com.holonplatform.http.internal.HttpLogger;
import com.holonplatform.http.rest.RestClient;
import com.holonplatform.http.rest.RestClientFactory;

/**
 * {@link RestClientFactory} registry.
 *
 * @since 5.0.0
 */
public enum RestClientFactoryRegistry {

	INSTANCE;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = HttpLogger.create();

	/**
	 * {@link Priority} based comparator.
	 */
	private static final Comparator<RestClientFactory> PRIORITY_COMPARATOR = Comparator.comparingInt(
			p -> p.getClass().isAnnotationPresent(Priority.class) ? p.getClass().getAnnotation(Priority.class).value()
					: RestClientFactory.DEFAULT_PRIORITY);

	/**
	 * The {@link RestClientFactory}s by class name organized by the {@link ClassLoader} was used to load them.
	 */
	private final WeakHashMap<ClassLoader, List<RestClientFactory>> factories;

	private RestClientFactoryRegistry() {
		factories = new WeakHashMap<>();
	}

	/**
	 * Get a {@link RestClient} instance for given <code>fullyQualifiedClassName</code> and {@link ClassLoader} using a
	 * suitable {@link RestClientFactory}.
	 * @param fullyQualifiedClassName {@link RestClient} class name to obtain, or <code>null</code> for the default one
	 * @param classLoader ClassLoader for which to obtain the {@link RestClient}
	 * @return A new {@link RestClient} instance for given <code>fullyQualifiedClassName</code> (or the default one if
	 *         <code>null</code>) and {@link ClassLoader}
	 * @throws RestClientCreationException If no {@link RestClientFactory} available, or none of the available factories
	 *         returned a not <code>null</code> instance or a creation error occurred
	 */
	public RestClient createRestClient(String fullyQualifiedClassName, ClassLoader classLoader) {
		ClassLoader cl = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
		List<RestClientFactory> restClientFactories = getRestClientFactories(fullyQualifiedClassName, cl);
		if (restClientFactories.isEmpty()) {
			throw new RestClientCreationException("No RestClientFactory available for ClassLoader [" + cl + "]"
					+ ((fullyQualifiedClassName != null)
							? " and RestClient implementation class name [" + fullyQualifiedClassName + "]"
							: ""));
		}
		RestClient restClient = null;
		for (RestClientFactory factory : restClientFactories) {
			restClient = factory.create(cl);
			if (restClient != null) {
				break;
			}
		}
		if (restClient == null) {
			throw new RestClientCreationException("No RestClient available for ClassLoader [" + cl + "]"
					+ ((fullyQualifiedClassName != null)
							? " and RestClient implementation class name [" + fullyQualifiedClassName + "]"
							: ""));
		}
		return restClient;
	}

	/**
	 * Get the {@link RestClientFactory} for given <code>fullyQualifiedClassName</code> {@link RestClient} instance and
	 * {@link ClassLoader}.
	 * @param fullyQualifiedClassName {@link RestClientFactory} class name to obtain, or <code>null</code> for the
	 *        default one
	 * @param classLoader ClassLoader for which to obtain the factory
	 * @return {@link RestClientFactory} for given <code>fullyQualifiedClassName</code> (or the default one if
	 *         <code>null</code>) and {@link ClassLoader}
	 * @throws RestClientCreationException If no {@link RestClientFactory} available or a creation error occurred
	 */
	public List<RestClientFactory> getRestClientFactories(String fullyQualifiedClassName, ClassLoader classLoader) {
		ClassLoader serviceClassLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
		List<RestClientFactory> restClientFactories = getRestClientFactories(serviceClassLoader);
		if (fullyQualifiedClassName != null) {
			List<RestClientFactory> byName = new LinkedList<>();
			for (RestClientFactory factory : restClientFactories) {
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
	 * Obtain the {@link RestClientFactory}s that are available via the specified {@link ClassLoader}.
	 * @param classLoader the {@link ClassLoader} of the returned {@link RestClientFactory}s
	 * @return an list of {@link RestClientFactory}s loaded by the specified {@link ClassLoader}
	 */
	private synchronized List<RestClientFactory> getRestClientFactories(ClassLoader classLoader) {

		final ClassLoader serviceClassLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
		List<RestClientFactory> restClientFactories = factories.get(serviceClassLoader);

		if (restClientFactories == null) {
			restClientFactories = AccessController.doPrivileged(new PrivilegedAction<List<RestClientFactory>>() {
				@Override
				public List<RestClientFactory> run() {
					LinkedList<RestClientFactory> result = new LinkedList<>();
					ServiceLoader<RestClientFactory> serviceLoader = ServiceLoader.load(RestClientFactory.class,
							serviceClassLoader);
					for (RestClientFactory factory : serviceLoader) {
						result.add(factory);
						LOGGER.debug(
								() -> "Loaded and registered RestClientFactory [" + factory.getClass().getName() + "]");
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
