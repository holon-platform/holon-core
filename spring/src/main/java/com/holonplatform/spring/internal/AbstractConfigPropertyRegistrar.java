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
package com.holonplatform.spring.internal;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;

import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.spring.EnvironmentConfigPropertyProvider;

/**
 * Abstract class for bean registration using {@link Environment} bound {@link ConfigPropertyProvider}s.
 *
 * @since 5.0.0
 */
public abstract class AbstractConfigPropertyRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar {

	/**
	 * Spring context environment
	 */
	private Environment environment;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.EnvironmentAware#setEnvironment(org.springframework.core.env.Environment)
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * Spring context environment
	 * @return the environment
	 */
	protected Environment getEnvironment() {
		return environment;
	}

	/**
	 * Build a {@link ConfigPropertyProvider} using {@link Environment} as property source
	 * @return Environment-bound ConfigPropertyProvider
	 */
	protected ConfigPropertyProvider buildEnvironmentPropertyProvider() {
		return EnvironmentConfigPropertyProvider.create(getEnvironment());
	}

}
