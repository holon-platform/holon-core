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
package com.holonplatform.spring.internal.context;

import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import com.holonplatform.spring.EnableBeanContext;

/**
 * Registrar to register a {@link BeanFactoryScopePostProcessor} using {@link EnableBeanContext} annotation.
 * 
 * @since 5.0.0
 */
public class BeanFactoryScopeRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

	private static final String BEAN_FACTORY_SCOPE_POST_PROCESSOR_NAME = BeanFactoryScopePostProcessor.class.getName();

	private Environment environment;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.EnvironmentAware#setEnvironment(org.springframework.core.env.Environment)
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.context.annotation.ImportBeanDefinitionRegistrar#registerBeanDefinitions(org.springframework.
	 * core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {

		Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableBeanContext.class.getName());
		if (attributes == null) {
			return;
		}

		boolean lookupByType = (boolean) attributes.get("lookupByType");

		// check environment
		if (environment.containsProperty(EnableBeanContext.LOOKUP_BY_TYPE_PROPERTY_NAME)) {
			lookupByType = environment.getProperty(EnableBeanContext.LOOKUP_BY_TYPE_PROPERTY_NAME, Boolean.class);
		}

		// register post processor
		if (!registry.containsBeanDefinition(BEAN_FACTORY_SCOPE_POST_PROCESSOR_NAME)) {
			BeanDefinitionBuilder postProcessorBuilder = BeanDefinitionBuilder
					.genericBeanDefinition(BeanFactoryScopePostProcessor.class).setDestroyMethodName("unregister")
					.addPropertyValue("lookupByType", lookupByType).setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(BEAN_FACTORY_SCOPE_POST_PROCESSOR_NAME,
					postProcessorBuilder.getBeanDefinition());
		}
	}

}
