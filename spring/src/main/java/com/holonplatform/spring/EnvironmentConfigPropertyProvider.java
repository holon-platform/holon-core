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
package com.holonplatform.spring;

import org.springframework.core.env.Environment;

import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.spring.internal.DefaultEnvironmentConfigPropertyProvider;

/**
 * A {@link ConfigPropertyProvider} using Spring context {@link Environment} as configuration property source.
 * 
 * @since 5.0.0
 */
public interface EnvironmentConfigPropertyProvider extends ConfigPropertyProvider {

	/**
	 * Gets the Spring Environment.
	 * @return the Spring Environment
	 */
	Environment getEnvironment();

	/**
	 * Create an {@link EnvironmentConfigPropertyProvider} using given Spring <code>environment</code>.
	 * @param environment Spring {@link Environment} (not null)
	 * @return A new EnvironmentConfigPropertyProvider
	 */
	static EnvironmentConfigPropertyProvider create(Environment environment) {
		return new DefaultEnvironmentConfigPropertyProvider(environment);
	}

}
