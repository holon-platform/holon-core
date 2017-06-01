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
package com.holonplatform.spring.internal;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.spring.EnvironmentConfigPropertyProvider;

/**
 * A {@link ConfigPropertyProvider} using Spring context {@link Environment} as configuration property source.
 * 
 * @since 5.0.0
 */
public class DefaultEnvironmentConfigPropertyProvider implements EnvironmentConfigPropertyProvider {

	/**
	 * Spring context Environment
	 */
	private final Environment environment;

	/**
	 * Constructor
	 * @param environment Spring context Environment (not null)
	 */
	public DefaultEnvironmentConfigPropertyProvider(Environment environment) {
		super();
		ObjectUtils.argumentNotNull(environment, "Spring Environment must be not null");
		this.environment = environment;
	}

	/**
	 * Gets the Spring context Environment
	 * @return the Spring context Environment
	 */
	@Override
	public Environment getEnvironment() {
		return environment;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigPropertyProvider#containsProperty(java.lang.String)
	 */
	@Override
	public boolean containsProperty(String key) {
		return getEnvironment().containsProperty(key);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigPropertyProvider#getProperty(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T getProperty(String key, Class<T> targetType) throws IllegalArgumentException {
		return getEnvironment().getProperty(key, targetType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.config.ConfigPropertyProvider#getPropertyNames()
	 */
	@Override
	public Stream<String> getPropertyNames() throws UnsupportedOperationException {
		List<String> names = new LinkedList<>();
		if (ConfigurableEnvironment.class.isAssignableFrom(getEnvironment().getClass())) {
			MutablePropertySources propertySources = ((ConfigurableEnvironment) getEnvironment()).getPropertySources();
			if (propertySources != null) {
				Iterator<PropertySource<?>> i = propertySources.iterator();
				while (i.hasNext()) {
					PropertySource<?> source = i.next();
					if (source instanceof EnumerablePropertySource) {
						String[] propertyNames = ((EnumerablePropertySource<?>) source).getPropertyNames();
						if (propertyNames != null) {
							names.addAll(Arrays.asList(propertyNames));
						}
					}
				}
			}
		}
		return names.stream();
	}

}
