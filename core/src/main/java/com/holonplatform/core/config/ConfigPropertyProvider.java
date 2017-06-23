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
package com.holonplatform.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import com.holonplatform.core.internal.config.MapConfigPropertyProvider;
import com.holonplatform.core.internal.config.PrefixedConfigPropertyProvider;
import com.holonplatform.core.internal.config.PropertiesConfigProvider;
import com.holonplatform.core.internal.config.SystemConfigPropertyProvider;
import com.holonplatform.core.internal.utils.ClassUtils;

/**
 * Base interface to access config properties.
 * 
 * @since 5.0.0
 */
public interface ConfigPropertyProvider {

	/**
	 * Return whether the given property key is available, i.e. the property key is present and the value for the given
	 * key is not {@code null}.
	 * @param key Property key (not null)
	 * @return <code>true</code> if property exists and has not <code>null</code> value
	 */
	boolean containsProperty(String key);

	/**
	 * Return the property value associated with the given key and try to convert property value to given target type.
	 * @param key the property name to resolve (not null)
	 * @param targetType the expected type of the property value
	 * @param <T> Expected property and result type
	 * @return Property value or {@code null} if the key cannot be resolved
	 * @throws IllegalArgumentException If property value cannot be converted to target type
	 */
	<T> T getProperty(String key, Class<T> targetType) throws IllegalArgumentException;

	/**
	 * Returns a Stream of all available property names
	 * @return Property names stream
	 * @throws UnsupportedOperationException If this operation is not supported by concrete implementation
	 */
	Stream<String> getPropertyNames() throws UnsupportedOperationException;

	// Builders

	/**
	 * Build a {@link ConfigPropertyProvider} using {@link System} properties as property source.
	 * @return System properties ConfigPropertyProvider
	 */
	static ConfigPropertyProvider usingSystemProperties() {
		return new SystemConfigPropertyProvider();
	}

	/**
	 * Build a {@link ConfigPropertyProvider} using given Map as property source.
	 * @param propertyMap Property key-value map
	 * @return ConfigPropertyProvider with given <code>propertyMap</code> as property source.
	 */
	static ConfigPropertyProvider using(Map<String, Object> propertyMap) {
		return new MapConfigPropertyProvider(propertyMap);
	}

	/**
	 * Build a {@link ConfigPropertyProvider} using given {@link Properties} as property source.
	 * @param properties Source Properties
	 * @return ConfigPropertyProvider with given <code>properties</code> as property source.
	 */
	static ConfigPropertyProvider using(Properties properties) {
		return new PropertiesConfigProvider(properties);
	}

	/**
	 * Build a {@link ConfigPropertyProvider} using given {@link InputStream} as {@link Properties} property source.
	 * @param propertySource Source properties input stream
	 * @return ConfigPropertyProvider with given <code>propertySource</code> as property source.
	 * @throws IOException Inout stream read error
	 */
	static ConfigPropertyProvider using(InputStream propertySource) throws IOException {
		Properties source = new Properties();
		source.load(propertySource);
		return using(source);
	}

	/**
	 * Build a {@link ConfigPropertyProvider} using given <code>sourceFileName</code> as property source.
	 * <p>
	 * This method uses {@link ClassLoader#getResourceAsStream(String)} method to load the property stream, using the
	 * same conventions to locate the resource to load.
	 * </p>
	 * @param sourceFileName Property file name
	 * @param classLoader ClassLoader to use. If <code>null</code>, the default current classloader is used
	 * @param lenient <code>true</code> to ignore source loading errors and return a provider with an empty set of
	 *        properties
	 * @return ConfigPropertyProvider with given <code>sourceFileName</code> as property source.
	 * @throws IOException File not found or read error
	 */
	static ConfigPropertyProvider using(String sourceFileName, ClassLoader classLoader, boolean lenient)
			throws IOException {
		return using(ClassUtils.loadProperties(sourceFileName, classLoader, lenient));
	}

	/**
	 * Build a {@link ConfigPropertyProvider} using given <code>sourceFileName</code> as property source.
	 * <p>
	 * This method uses {@link ClassLoader#getResourceAsStream(String)} method to load the property stream, using the
	 * same conventions to locate the resource to load.
	 * </p>
	 * @param sourceFileName Property file name
	 * @param classLoader ClassLoader to use. If <code>null</code>, the default current classloader is used
	 * @return ConfigPropertyProvider with given <code>sourceFileName</code> as property source.
	 * @throws IOException File not found or read error
	 */
	static ConfigPropertyProvider using(String sourceFileName, ClassLoader classLoader) throws IOException {
		return using(ClassUtils.loadProperties(sourceFileName, classLoader, false));
	}

	/**
	 * Create a ConfigPropertyProvider that filters property names from given concrete <code>provider</code>, returning
	 * only property names which starts with given <code>prefix</code>.
	 * @param provider Concrete provider (not null)
	 * @param prefix Prefix to use (not null)
	 * @return Prefixed property provider
	 */
	static ConfigPropertyProvider prefixed(ConfigPropertyProvider provider, String prefix) {
		return new PrefixedConfigPropertyProvider(provider, prefix);
	}

}
