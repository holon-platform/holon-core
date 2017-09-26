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
package com.holonplatform.core.beans;

import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.config.ConfigPropertySet;
import com.holonplatform.core.internal.config.DefaultConfigPropertySet;

/**
 * A {@link ConfigPropertySet} for bean related configuration.
 *
 * @since 5.0.0
 */
public interface BeanConfigProperties extends ConfigPropertySet {

	/**
	 * Configuration property set name
	 */
	public static final String NAME = "holon.beans";

	/**
	 * Enable or disable caching for bean informations collected by a {@link BeanIntrospector}.
	 */
	public static final ConfigProperty<Boolean> BEAN_INTROSPECTOR_CACHE_ENABLED = ConfigProperty
			.create("introspector-cache-enabled", Boolean.class);

	@Override
	default String getName() {
		return NAME;
	}

	/**
	 * Gets whether the {@link BeanIntrospector} cache is enabled.
	 * @return <code>true</code> if cache is enabled
	 * @see #BEAN_INTROSPECTOR_CACHE_ENABLED
	 */
	default boolean isBeanIntrospectorCacheEnabled() {
		return getConfigPropertyValue(BEAN_INTROSPECTOR_CACHE_ENABLED, Boolean.TRUE);
	}

	/**
	 * Builder to create property set instances bound to a property data source
	 * @return ConfigPropertySet builder
	 */
	static Builder<BeanConfigProperties> builder() {
		return new DefaultConfigPropertySet.DefaultBuilder<>(new BeanConfigPropertiesImpl());
	}

	/**
	 * Default implementation
	 */
	static class BeanConfigPropertiesImpl extends DefaultConfigPropertySet implements BeanConfigProperties {

		public BeanConfigPropertiesImpl() {
			super(NAME);
		}

	}

}
