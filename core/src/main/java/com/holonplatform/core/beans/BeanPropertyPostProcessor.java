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

import java.util.ServiceLoader;

import com.holonplatform.core.beans.BeanIntrospector.BeanIntrospectionException;

import jakarta.annotation.Priority;

/**
 * Interface for classes that can be registered in a {@link BeanIntrospector} to
 * process detected {@link BeanProperty}s before returning the bean properties
 * set, for example to setup the property configuration.
 * 
 * <p>
 * BeanPropertyPostProcessor registration can be performed also using default
 * Java extension through {@link ServiceLoader}, providing a
 * <code>com.holonplatform.core.beans.BeanPropertyPostProcessor</code> file in
 * <code>META-INF/services</code> containing the BeanPropertyPostProcessor
 * concrete class names to register.
 * </p>
 *
 * @since 5.0.0
 */
@FunctionalInterface
public interface BeanPropertyPostProcessor {

	/**
	 * Default {@link BeanPropertyPostProcessor} priority if not specified using
	 * {@link Priority} annotation.
	 */
	public static final int DEFAULT_PRIORITY = 10000;

	/**
	 * Process given <code>property</code>.
	 * 
	 * @param property          Property to process
	 * @param beanOrNestedClass Main or nested bean class to which property belongs
	 * @return Processed property
	 * @throws BeanIntrospectionException If an error occurred
	 */
	BeanProperty.Builder<?> processBeanProperty(BeanProperty.Builder<?> property, Class<?> beanOrNestedClass);

}
