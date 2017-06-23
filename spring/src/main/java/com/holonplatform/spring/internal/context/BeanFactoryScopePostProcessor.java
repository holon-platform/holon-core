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
package com.holonplatform.spring.internal.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.holonplatform.core.Context;
import com.holonplatform.core.internal.ContextManager;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * {@link BeanFactoryPostProcessor} to enable a {@link BeanFactoryScope} and register it as {@link Context} scope, bound
 * to the BeanFactory beans ClassLoader.
 * 
 * @since 5.0.0
 */
public class BeanFactoryScopePostProcessor implements BeanFactoryPostProcessor {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = SpringLogger.create();

	/**
	 * BeanFactory beans ClassLoader
	 */
	private transient volatile ClassLoader beanFactoryClassLoader;

	/**
	 * Spring bean lookup by type ignoring name
	 */
	private boolean lookupByType = true;

	/**
	 * Gets whether to lookup context resource bean candidates by type, ignoring bean name, when default lookup by name
	 * and type fails.
	 * @return the lookupByType <code>true</code> if enabled
	 */
	public boolean isLookupByType() {
		return lookupByType;
	}

	/**
	 * Sets whether to lookup context resource bean candidates by type, ignoring bean name, when default lookup by name
	 * and type fails.
	 * @param lookupByType <code>true</code> to enable
	 */
	public void setLookupByType(boolean lookupByType) {
		this.lookupByType = lookupByType;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.
	 * beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		beanFactoryClassLoader = beanFactory.getBeanClassLoader();
		// register scope
		ContextManager.registerScope(beanFactoryClassLoader, new BeanFactoryScope(beanFactory, lookupByType));
		// log
		LOGGER.info("Spring BeanFactory context scope registered for ClassLoader " + beanFactoryClassLoader);
	}

	/**
	 * Unregister {@link BeanFactoryScope} from {@link Context}.
	 */
	public void unregister() {
		if (beanFactoryClassLoader != null) {
			ContextManager.unregisterScope(beanFactoryClassLoader, BeanFactoryScope.NAME);
			LOGGER.info("Spring BeanFactory context scope unregistered [ClassLoader: " + beanFactoryClassLoader + "]");
		}
	}

}
