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
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.core.config.ConfigPropertySet;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Base {@link ConfigPropertySet} implementation.
 * 
 * @since 5.0.0
 */
public class DefaultConfigPropertySet implements ConfigPropertySet {

	/**
	 * Property set name
	 */
	private final String name;

	/**
	 * Property providers
	 */
	private final List<ConfigPropertyProvider> propertyProviders = new LinkedList<>();

	/**
	 * Alias property set name
	 */
	private final List<String> aliasNames = new LinkedList<>();

	/**
	 * Construct a new property set
	 * @param name Property set name (not null)
	 */
	public DefaultConfigPropertySet(String name) {
		super();
		ObjectUtils.argumentNotNull(name, "Property set name must be not null");
		this.name = name;
	}

	/**
	 * Concrete property providers
	 * @return Property providers
	 */
	protected List<ConfigPropertyProvider> getPropertyProviders() {
		return propertyProviders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.holonplatform.core.config.ConfigPropertySet#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.holonplatform.core.config.ConfigPropertySet#getConfigPropertyName(com.
	 * holonplatform.core.config. ConfigProperty)
	 */
	@Override
	public <T> String getConfigPropertyName(ConfigProperty<T> property) {
		ObjectUtils.argumentNotNull(property, "Configuration property must be not null");
		return getName() + "." + property.getKey();
	}

	/**
	 * Add a {@link ConfigPropertyProvider} for configuration properties resolution.
	 * @param propertyProvider The property provider to add
	 */
	public void addPropertyProvider(ConfigPropertyProvider propertyProvider) {
		ObjectUtils.argumentNotNull(propertyProvider, "Property provider must be not null");
		propertyProviders.add(ConfigPropertyProvider.prefixed(propertyProvider, (getName() + ".")));
		for (String alias : aliasNames) {
			propertyProviders.add(ConfigPropertyProvider.prefixed(propertyProvider, (alias + ".")));
		}
	}

	/**
	 * Add an alias property set name to lookup if a property was not found using
	 * primary property set name
	 * @param alias Alias name (not null)
	 */
	public void addAliasName(String alias) {
		ObjectUtils.argumentNotNull(alias, "Alias must be not null");
		aliasNames.add(alias);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.holonplatform.core.config.ConfigPropertySet#hasConfigProperty(com.
	 * holonplatform.core.config.ConfigProperty)
	 */
	@Override
	public <T> boolean hasConfigProperty(ConfigProperty<T> property) {
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		for (ConfigPropertyProvider propertyProvider : getPropertyProviders()) {
			if (propertyProvider.containsProperty(property.getKey())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.holonplatform.core.config.ConfigPropertySet#getConfigPropertyValue(com.
	 * holonplatform.core.config. ConfigProperty)
	 */
	@Override
	public <T> Optional<T> getConfigPropertyValue(ConfigProperty<T> property) {
		ObjectUtils.argumentNotNull(property, "Configuration property must be not null");

		T value = null;

		for (ConfigPropertyProvider propertyProvider : getPropertyProviders()) {
			value = propertyProvider.getProperty(property.getKey(), property.getType());
			if (value != null) {
				break;
			}
		}

		return Optional.ofNullable(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.holonplatform.core.config.ConfigPropertySet#getSubPropertiesUsingPrefix(
	 * java.lang.String)
	 */
	@Override
	public Map<String, String> getSubPropertiesUsingPrefix(String prefix) {
		ObjectUtils.argumentNotNull(prefix, "Prefix must be not null");

		final String subPrefix = prefix + ".";

		Map<String, String> properties = new HashMap<>();

		for (final ConfigPropertyProvider provider : propertyProviders) {
			try {
				Stream<String> propertyNames = provider.getPropertyNames();
				if (propertyNames != null) {
					propertyNames.filter(n -> (n.length() > subPrefix.length()) && n.startsWith(subPrefix))
							.forEach(n -> properties.put(n.substring(subPrefix.length()),
									provider.getProperty(n, String.class)));
				}
			} catch (@SuppressWarnings("unused") UnsupportedOperationException e) {
				// ignore
			}
		}

		return properties;
	}

	// Builder

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultConfigPropertySet [name=" + name + ", propertyProviders=" + propertyProviders + ", aliasNames="
				+ aliasNames + "]";
	}

	public static class DefaultBuilder<C extends ConfigPropertySet> implements Builder<C> {

		/*
		 * Building instance
		 */
		protected final DefaultConfigPropertySet instance;

		/*
		 * Explicit values
		 */
		protected Map<ConfigProperty<?>, Object> values;

		/**
		 * Constructor
		 * @param instance Instance to build
		 */
		public DefaultBuilder(DefaultConfigPropertySet instance) {
			super();
			this.instance = instance;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.holonplatform.core.config.ConfigPropertySet.Builder#
		 * withDefaultPropertySources()
		 */
		@Override
		public Builder<C> withDefaultPropertySources() {
			DefaultConfig.defaultConfigPropertyProviders().forEach(p -> withPropertySource(p));
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.holonplatform.core.config.ConfigPropertySet.Builder#
		 * withDefaultPropertySources(java.lang.ClassLoader)
		 */
		@Override
		public Builder<C> withDefaultPropertySources(ClassLoader classLoader) {
			DefaultConfig
					.defaultConfigPropertyProviders(
							(classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader())
					.forEach(p -> withPropertySource(p));
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.config.ConfigPropertySet.Builder#withPropertySource(
		 * com.holonplatform.core.config. ConfigPropertyProvider)
		 */
		@Override
		public Builder<C> withPropertySource(ConfigPropertyProvider provider) {
			instance.addPropertyProvider(provider);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.config.ConfigPropertySet.Builder#withPropertySource(
		 * java.util.Map)
		 */
		@Override
		public Builder<C> withPropertySource(Map<String, Object> propertyMap) {
			instance.addPropertyProvider(ConfigPropertyProvider.using(propertyMap));
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.config.ConfigPropertySet.Builder#withPropertySource(
		 * java.util.Properties)
		 */
		@Override
		public Builder<C> withPropertySource(Properties properties) {
			instance.addPropertyProvider(ConfigPropertyProvider.using(properties));
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.config.ConfigPropertySet.Builder#withPropertySource(
		 * java.io.InputStream)
		 */
		@Override
		public Builder<C> withPropertySource(InputStream propertiesInputStream) throws ConfigurationException {
			try {
				instance.addPropertyProvider(ConfigPropertyProvider.using(propertiesInputStream));
			} catch (IOException e) {
				throw new ConfigurationException(e);
			}
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.config.ConfigPropertySet.Builder#withPropertySource(
		 * java.lang.String, java.lang.ClassLoader, boolean)
		 */
		@Override
		public Builder<C> withPropertySource(String sourceFileName, ClassLoader classLoader, boolean lenient)
				throws ConfigurationException {
			try {
				instance.addPropertyProvider(ConfigPropertyProvider.using(sourceFileName, classLoader, lenient));
			} catch (IOException e) {
				throw new ConfigurationException("Failed to read property source file [" + sourceFileName + "]", e);
			}
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.config.ConfigPropertySet.Builder#withPropertySource(
		 * java.lang.String)
		 */
		@Override
		public Builder<C> withPropertySource(String sourceFileName) throws ConfigurationException {
			return withPropertySource(sourceFileName, ClassUtils.getDefaultClassLoader(), false);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.holonplatform.core.config.ConfigPropertySet.Builder#
		 * withSystemPropertySource()
		 */
		@Override
		public Builder<C> withSystemPropertySource() {
			instance.addPropertyProvider(ConfigPropertyProvider.usingSystemProperties());
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.holonplatform.core.config.ConfigPropertySet.Builder#withProperty(com.
		 * holonplatform.core.config. ConfigProperty, java.lang.Object)
		 */
		@Override
		public <T> Builder<C> withProperty(ConfigProperty<T> property, T value) {
			ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
			if (values == null) {
				values = new HashMap<>(8);
			}
			values.put(property, value);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.holonplatform.core.config.ConfigPropertySet.Builder#build()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public C build() {

			// explicit values
			if (values != null && !values.isEmpty()) {
				final Map<String, Object> properties = new HashMap<>(values.size());
				for (Entry<ConfigProperty<?>, Object> value : values.entrySet()) {
					properties.put(instance.getName() + "." + value.getKey().getKey(), value.getValue());
				}
				instance.addPropertyProvider(ConfigPropertyProvider.using(properties));
			}

			return (C) instance;
		}

	}

}
