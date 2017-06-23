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
package com.holonplatform.core.internal.config;

import java.util.stream.Stream;

import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * {@link ConfigPropertyProvider} which supports a fixed prefix for property keys
 *
 * @since 5.0.0
 */
public class PrefixedConfigPropertyProvider implements ConfigPropertyProvider {

	/**
	 * Concrete provider
	 */
	private final ConfigPropertyProvider wrappedProvider;

	/**
	 * Property keys prefix
	 */
	private final String prefix;

	/**
	 * Constructor
	 * @param wrappedProvider Concrete provider
	 * @param prefix Property keys prefix
	 */
	public PrefixedConfigPropertyProvider(ConfigPropertyProvider wrappedProvider, String prefix) {
		super();

		ObjectUtils.argumentNotNull(wrappedProvider, "Wrapped provider must be not null");
		ObjectUtils.argumentNotNull(prefix, "Prefix must be not null");

		this.wrappedProvider = wrappedProvider;
		this.prefix = prefix;
	}

	/**
	 * Property keys prefix
	 * @return Property keys prefix
	 */
	protected String getPrefix() {
		return prefix;
	}

	/**
	 * Compose property key using {@link #getPrefix()}
	 * @param key Property key
	 * @return A String composed by the prefix and the property key
	 */
	protected String composeKey(String key) {
		return (key != null) ? (getPrefix() + key) : null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigPropertyProvider#containsProperty(java.lang.String)
	 */
	@Override
	public boolean containsProperty(String key) {
		return wrappedProvider.containsProperty(composeKey(key));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigPropertyProvider#getProperty(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T getProperty(String key, Class<T> targetType) throws IllegalArgumentException {
		return wrappedProvider.getProperty(composeKey(key), targetType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigPropertyProvider#getPropertyNames()
	 */
	@Override
	public Stream<String> getPropertyNames() throws UnsupportedOperationException {
		return wrappedProvider.getPropertyNames().filter((n) -> n.startsWith(getPrefix()))
				.map((n) -> n.substring(getPrefix().length()));
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PrefixedConfigPropertyProvider [wrappedProvider=" + wrappedProvider + ", prefix=" + prefix + "]";
	}

}
