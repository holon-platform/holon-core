/*
 * Copyright 2016-2018 Axioma srl.
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
package com.holonplatform.spring.internal.datastore;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.holonplatform.core.datastore.ConfigurableDatastore;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * A {@link BeanPostProcessor} for {@link ConfigurableDatastore} type beans to configure the Datastore instance using
 * the Spring context.
 *
 * @since 5.2.0
 * 
 * @see DatastoreInitializer
 */
public class DatastoreConfigurationBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = SpringLogger.create();

	/**
	 * Bean factory
	 */
	private BeanFactory beanFactory;

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (ConfigurableDatastore.class.isAssignableFrom(bean.getClass())) {
			String message = DatastoreInitializer.configureDatastore((ConfigurableDatastore) bean, beanName,
					beanFactory);
			LOGGER.info("Datastore bean [" + beanName + "] configured - " + message);
		}
		return bean;
	}

}
