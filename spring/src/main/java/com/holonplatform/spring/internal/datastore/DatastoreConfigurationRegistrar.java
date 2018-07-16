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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.holonplatform.core.datastore.Datastore;

/**
 * Registers a {@link DatastoreConfigurationBeanPostProcessor} to configure {@link Datastore} type beans using the
 * Spring context.
 *
 * @since 5.2.0
 */
public class DatastoreConfigurationRegistrar implements ImportBeanDefinitionRegistrar {

	private static final String DATASTORE_CONFIGURATION_POST_PROCESSOR_NAME = DatastoreConfigurationBeanPostProcessor.class
			.getName();

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.context.annotation.ImportBeanDefinitionRegistrar#registerBeanDefinitions(org.springframework.
	 * core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		// register post processor
		if (!registry.containsBeanDefinition(DATASTORE_CONFIGURATION_POST_PROCESSOR_NAME)) {
			BeanDefinitionBuilder postProcessorBuilder = BeanDefinitionBuilder
					.genericBeanDefinition(DatastoreConfigurationBeanPostProcessor.class)
					.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(DATASTORE_CONFIGURATION_POST_PROCESSOR_NAME,
					postProcessorBuilder.getBeanDefinition());
		}
	}

}
