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

import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link ConfigProperty} implementation.
 * 
 * @since 5.0.0
 */
public class DefaultConfigProperty<T> implements ConfigProperty<T> {

	private static final long serialVersionUID = 2358690479206398336L;

	/*
	 * Immutable property name
	 */
	private final String key;

	/*
	 * Immutable property value type
	 */
	private final Class<T> type;

	/**
	 * Constructor
	 * @param key Property key
	 * @param type Property type
	 */
	public DefaultConfigProperty(String key, Class<T> type) {
		super();

		ObjectUtils.argumentNotNull(key, "Config property key must be not null");
		ObjectUtils.argumentNotNull(type, "Config property type must be not null");

		this.key = key;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigProperty#getKey()
	 */
	@Override
	public String getKey() {
		return key;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigProperty#getType()
	 */
	@Override
	public Class<T> getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultConfigProperty other = (DefaultConfigProperty) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConfigProperty[key=" + key + ", type=" + type.getName() + "]";
	}

}
