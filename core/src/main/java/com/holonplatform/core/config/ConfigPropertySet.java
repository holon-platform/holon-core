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
package com.holonplatform.core.config;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

import com.holonplatform.core.internal.config.DefaultConfig;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * A set of configuration properties, identified by a specific name, used as property definition prefix.
 * <p>
 * For external property definitions (for example using a <code>.properties</code> file), the actual property name is
 * composed using the property set name returned by {@link #getName()} and the given specific <code>name</code>. For
 * example, if the property set name is <code>test</code> and the property name is <code>prop</code>, the actual
 * property name will be <code>test.prop</code>.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see ConfigProperty
 */
public interface ConfigPropertySet {

	/**
	 * Configuration property set name, used as property definition prefix.
	 * @return Configuration properties set name (not null)
	 */
	String getName();

	/**
	 * Checks whether this property set contains a not <code>null</code> value associated to given
	 * <code>property</code>.
	 * @param <T> Property type
	 * @param property Property to check (not null)
	 * @return <code>true</code> if this property set contains a not <code>null</code> value associated to given
	 *         <code>property</code>
	 */
	<T> boolean hasConfigProperty(ConfigProperty<T> property);

	/**
	 * Get the value associated to given <code>property</code>, if available.
	 * @param <T> Property type
	 * @param property Configuration property to read (not null)
	 * @return Optional config property value
	 */
	<T> Optional<T> getConfigPropertyValue(ConfigProperty<T> property);

	/**
	 * Returns the value associated to given <code>property</code>.
	 * @param <T> Property type
	 * @param property Configuration property to read (not null)
	 * @param defaultValue Default value to return if property was not found or has no value
	 * @return Property value, or <code>defaultValue</code> if not found
	 */
	default <T> T getConfigPropertyValue(ConfigProperty<T> property, T defaultValue) {
		return getConfigPropertyValue(property).orElse(defaultValue);
	}

	/**
	 * Get the value associated to given <code>property</code>, if available. If not available, try to obtain the value
	 * from the provided <code>orElse</code> supplier.
	 * @param <T> Property type
	 * @param property Configuration property to read (not null)
	 * @param orElse Fallback value supplier (not null)
	 * @return Optional config property value
	 */
	default <T> Optional<T> getConfigPropertyValueOrElse(ConfigProperty<T> property, Supplier<Optional<T>> orElse) {
		ObjectUtils.argumentNotNull(property, "Fallback value supplier must be not null");
		final Optional<T> value = getConfigPropertyValue(property);
		return (value.isPresent()) ? value : orElse.get();
	}

	/**
	 * Get a key-value {@link Map} of all the properties at sub levels of this property set, starting from given prefix.
	 * @param prefix Prefix to use (not null)
	 * @return Map of all the properties at sub levels of this property set, starting from given prefix. An empty map if
	 *         one found.
	 */
	Map<String, String> getSubPropertiesUsingPrefix(String prefix);

	// Builder

	/**
	 * Builder to create {@link ConfigPropertySet}s bound to property data sources.
	 * @param <C> Concrete ConfigPropertySet type to build
	 */
	public interface Builder<C extends ConfigPropertySet> {

		/**
		 * Add default configuration property sources using {@link DefaultConfig#defaultConfigPropertyProviders()}
		 * @return this
		 */
		Builder<C> withDefaultPropertySources();

		/**
		 * Add default configuration property sources using {@link DefaultConfig#defaultConfigPropertyProviders()} and
		 * given ClassLoader
		 * @param classLoader ClassLoader to use
		 * @return this
		 */
		Builder<C> withDefaultPropertySources(ClassLoader classLoader);

		/**
		 * Add given {@link ConfigPropertyProvider} property source to ConfigPropertySet
		 * @param provider ConfigPropertyProvider to add
		 * @return this
		 */
		Builder<C> withPropertySource(ConfigPropertyProvider provider);

		/**
		 * Add given {@link Map} property source to ConfigPropertySet
		 * @param propertyMap Property key-value source
		 * @return this
		 */
		Builder<C> withPropertySource(Map<String, Object> propertyMap);

		/**
		 * Add given {@link Properties} property source to ConfigPropertySet
		 * @param properties Properties instance
		 * @return this
		 */
		Builder<C> withPropertySource(Properties properties);

		/**
		 * Add given {@link InputStream} property source to ConfigPropertySet
		 * @param propertiesInputStream Properties input stream
		 * @throws ConfigurationException Error reading property source
		 * @return this
		 */
		Builder<C> withPropertySource(InputStream propertiesInputStream) throws ConfigurationException;

		/**
		 * Add given <code>sourceFileName</code> as property source to ConfigPropertySet, using default ClassLoader
		 * @param sourceFileName Properties file name
		 * @throws ConfigurationException Error reading property source
		 * @return this
		 */
		Builder<C> withPropertySource(String sourceFileName) throws ConfigurationException;

		/**
		 * Add given <code>sourceFileName</code> as property source to ConfigPropertySet
		 * @param sourceFileName Properties file name
		 * @param classLoader ClassLoader to use. If <code>null</code>, the default current classloader is used
		 * @param lenient <code>true</code> to ignore source loading errors and return a provider with an empty set of
		 *        properties
		 * @throws ConfigurationException Error reading property source
		 * @return this
		 */
		Builder<C> withPropertySource(String sourceFileName, ClassLoader classLoader, boolean lenient)
				throws ConfigurationException;

		/**
		 * Add a property source using {@link System} properties to ConfigPropertySet
		 * @return this
		 */
		Builder<C> withSystemPropertySource();

		/**
		 * Add given config property value.
		 * @param <T> Config property type
		 * @param property Config property for which to provide the value (not null)
		 * @param value Config property value
		 * @return this
		 */
		<T> Builder<C> withProperty(ConfigProperty<T> property, T value);

		/**
		 * Build the {@link ConfigPropertySet} instance
		 * @return ConfigPropertySet instance
		 */
		C build();

	}

	/**
	 * Exception thrown for configuration errors.
	 */
	@SuppressWarnings("serial")
	public class ConfigurationException extends RuntimeException {

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public ConfigurationException(String message) {
			super(message);
		}

		/**
		 * Constructor with nested exception
		 * @param cause Nested exception
		 */
		public ConfigurationException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public ConfigurationException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
