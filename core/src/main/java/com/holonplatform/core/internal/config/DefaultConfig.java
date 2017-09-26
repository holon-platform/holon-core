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
package com.holonplatform.core.internal.config;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.core.internal.CoreLogger;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Core class to provide default configuration propertiey resolvers.
 * 
 * @since 5.0.0
 */
public final class DefaultConfig implements Serializable {

	private static final long serialVersionUID = 8007897561180272608L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = CoreLogger.create();

	/**
	 * Default properties file name that can be used to setup framework configuration properties
	 */
	public static final String DEFAULT_CONFIG_PROPERTIES_FILE_NAME = "holon.properties";

	/**
	 * Registry of {@link ConfigPropertyProvider}s for {@link #DEFAULT_CONFIG_PROPERTIES_FILE_NAME}
	 */
	private static final WeakHashMap<ClassLoader, List<ConfigPropertyProvider>> configPropertyProviders = new WeakHashMap<>(
			2);

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private DefaultConfig() {
	}

	/**
	 * Get default Holon configuration properties provider using default {@link ClassUtils#getDefaultClassLoader()}
	 * ClassLoader.
	 * @return Holon configuration properties providers, or an empty list if none available
	 */
	public static List<ConfigPropertyProvider> defaultConfigPropertyProviders() {
		return defaultConfigPropertyProviders(ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Get default Holon configuration properties providers
	 * @param classLoader ClassLoader to use (not null)
	 * @return Holon configuration properties providers, or an empty list if none available
	 */
	public static List<ConfigPropertyProvider> defaultConfigPropertyProviders(ClassLoader classLoader) {
		ObjectUtils.argumentNotNull(classLoader, "ClassLoader must be not null");
		return configPropertyProviders.getOrDefault(classLoader, Collections.emptyList());
	}

	/**
	 * Register a {@link ConfigPropertyProvider} for given <code>classLoader</code> to obtain configuration properties.
	 * @param classLoader ClassLoader to which provider belongs. If <code>null</code>, the current Thread or default
	 *        ClassLoader will be used
	 * @param configPropertyProvider Configuration properties provider
	 */
	private synchronized static void registerConfigPropertyProvider(ClassLoader classLoader,
			ConfigPropertyProvider configPropertyProvider) {
		ObjectUtils.argumentNotNull(configPropertyProvider, "ConfigPropertyProvider must be not null");
		ClassLoader cl = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
		List<ConfigPropertyProvider> providers = configPropertyProviders.get(cl);
		if (providers == null) {
			providers = new LinkedList<>();
			configPropertyProviders.put(cl, providers);
		}
		providers.add(configPropertyProvider);

		LOGGER.debug(() -> "Registered default ConfigPropertyProvider [" + configPropertyProvider + "]");
	}

	/**
	 * Default ConfigPropertyProviders initialization
	 */
	static {
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		if (classLoader != null) {
			// System properties
			registerConfigPropertyProvider(classLoader, ConfigPropertyProvider.usingSystemProperties());
			// Default configuration properties file
			try {
				registerConfigPropertyProvider(classLoader,
						ConfigPropertyProvider.using(DEFAULT_CONFIG_PROPERTIES_FILE_NAME, classLoader, true));
			} catch (@SuppressWarnings("unused") IOException e) {
				// ignore: is lenient
			}
		}
	}

}
