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

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;

/**
 * A {@link ConfigPropertyProvider} using a {@link Map} as property source.
 * 
 * @since 5.0.0
 */
public class MapConfigPropertyProvider implements ConfigPropertyProvider {

	/**
	 * Properties
	 */
	private final Map<String, Object> properties;

	/**
	 * Constructor
	 * @param properties Property source
	 */
	public MapConfigPropertyProvider(Map<String, Object> properties) {
		super();
		this.properties = (properties != null) ? properties : Collections.emptyMap();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigPropertyProvider#containsProperty(java.lang.String)
	 */
	@Override
	public boolean containsProperty(String key) {
		return properties.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigPropertyProvider#getProperty(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(String key, Class<T> targetType) throws IllegalArgumentException {
		ObjectUtils.argumentNotNull(key, "Property name must be not null");
		ObjectUtils.argumentNotNull(targetType, "Property type must be not null");

		Object value = properties.get(key);
		if (value != null) {
			if (TypeUtils.isAssignable(value.getClass(), targetType)) {
				return (T) value;
			} else if (TypeUtils.isString(value.getClass())) {
				return ConversionUtils.convertStringValue(value.toString(), targetType);
			} else {
				throw new IllegalArgumentException(
						"Property " + key + " type is not consistent " + "with required type: " + targetType.getName()
								+ " (got type: " + value.getClass().getName() + ")");
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigPropertyProvider#getPropertyNames()
	 */
	@Override
	public Stream<String> getPropertyNames() throws UnsupportedOperationException {
		return properties.keySet().stream();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MapConfigPropertyProvider [properties=" + properties + "]";
	}

}
