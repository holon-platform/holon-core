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

import java.io.Serializable;

import com.holonplatform.core.internal.config.DefaultConfigProperty;

/**
 * Configuration property reference, identified by a {@link String} key and supporting a typed value.
 * 
 * @param <T> Property value type
 * 
 * @since 5.0.0
 */
public interface ConfigProperty<T> extends Serializable {

	/**
	 * Property key (name)
	 * @return Property key
	 */
	String getKey();

	/**
	 * Property value type
	 * @return Value type
	 */
	Class<T> getType();

	/**
	 * Create a {@link ConfigProperty}
	 * @param <T> Property value type
	 * @param key Property key (not null)
	 * @param type Property type (not null)
	 * @return ConfigProperty instance
	 */
	static <T> ConfigProperty<T> create(String key, Class<T> type) {
		return new DefaultConfigProperty<>(key, type);
	}

}
