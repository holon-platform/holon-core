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
 * perform additional {@link BeanPropertySet} configuration operations at bean
 * introspection time.
 * <p>
 * BeanPropertySetPostProcessor registration can be performed also using default
 * Java extension through {@link ServiceLoader}, providing a
 * <code>com.holonplatform.core.beans.BeanPropertySetPostProcessor</code> file
 * in <code>META-INF/services</code> containing the BeanPropertySetPostProcessor
 * concrete class names to register.
 * </p>
 *
 * @since 5.1.0
 */
@FunctionalInterface
public interface BeanPropertySetPostProcessor {

	/**
	 * Default {@link BeanPropertySetPostProcessor} priority if not specified using
	 * {@link Priority} annotation.
	 */
	public static final int DEFAULT_PRIORITY = 10000;

	/**
	 * Process given bean <code>propertySet</code>.
	 * 
	 * @param propertySet {@link BeanPropertySet} to process as builder
	 * @param beanClass   The property set bean class
	 * @throws BeanIntrospectionException If an error occurred
	 */
	void processBeanPropertySet(BeanPropertySet.Builder<?, ?> propertySet, Class<?> beanClass);

}
