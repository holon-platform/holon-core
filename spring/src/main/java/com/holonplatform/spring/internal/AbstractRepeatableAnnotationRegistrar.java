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

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Abstract class to support bean registrations using {@link Repeatable} annotations.
 * 
 * @since 5.0.0
 */
public abstract class AbstractRepeatableAnnotationRegistrar implements ImportBeanDefinitionRegistrar {

	private final Class<? extends Annotation> repeatableAnnotation;
	private final Class<? extends Annotation> repeatableAnnotationContainer;

	/**
	 * Constructor
	 * @param repeatableAnnotation Repeatable configuration annotation class
	 * @param repeatableAnnotationContainer Repeatable configuration annotation container class
	 */
	public AbstractRepeatableAnnotationRegistrar(Class<? extends Annotation> repeatableAnnotation,
			Class<? extends Annotation> repeatableAnnotationContainer) {
		super();

		ObjectUtils.argumentNotNull(repeatableAnnotation, "Repeatable annotation class must be not null");
		ObjectUtils.argumentNotNull(repeatableAnnotationContainer,
				"Repeatable annotation container class must be not null");

		this.repeatableAnnotation = repeatableAnnotation;
		this.repeatableAnnotationContainer = repeatableAnnotationContainer;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.context.annotation.ImportBeanDefinitionRegistrar#registerBeanDefinitions(org.springframework.
	 * core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
		// Single annotation
		if (annotationMetadata.isAnnotated(repeatableAnnotation.getName())) {
			register(annotationMetadata.getAnnotationAttributes(repeatableAnnotation.getName()), registry, false);
		} else if (annotationMetadata.isAnnotated(repeatableAnnotationContainer.getName())) {
			// Multiple annotations
			Map<String, Object> attributes = annotationMetadata
					.getAnnotationAttributes(repeatableAnnotationContainer.getName());
			AnnotationAttributes[] repetitions = (attributes != null) ? (AnnotationAttributes[]) attributes.get("value")
					: null;
			if (repetitions != null) {
				for (AnnotationAttributes repetition : repetitions) {
					register(repetition, registry, true);
				}
			}
		}
	}

	/**
	 * Concrete bean definitions registration. IN case of annotation repetition, this method is called for every
	 * repeated annotation found, providing specific repetition annotation attributes.
	 * @param attributes Annotation attributes
	 * @param registry Bean definitions registry
	 * @param fromRepeatableAnnotationContainer Registration from multiple annotation repetitions
	 */
	protected abstract void register(Map<String, Object> attributes, BeanDefinitionRegistry registry,
			boolean fromRepeatableAnnotationContainer);

}
