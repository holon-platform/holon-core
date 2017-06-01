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
package com.holonplatform.core.internal.beans;

import javax.annotation.Priority;

import com.holonplatform.core.Validator;
import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanIntrospector.BeanIntrospectionException;
import com.holonplatform.core.beans.BeanProperty.Builder;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.internal.Logger;

/**
 * A {@link BeanPropertyPostProcessor} to inspect default {@link Validator} annotation and setup the bean property
 * validators.
 * <p>
 * This post processor is automatically registered in default {@link BeanIntrospector} instances.
 * </p>
 *
 * @since 5.0.0
 */
@Priority(200)
public class BeanPropertyValidationPostProcessor implements BeanPropertyPostProcessor {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = BeanLogger.create();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertyPostProcessor#processBeanProperty(com.holonplatform.core.beans.
	 * BeanProperty.Builder, java.lang.Class)
	 */
	@Override
	public Builder<?> processBeanProperty(Builder<?> property, Class<?> beanOrNestedClass) {
		if (property.getAnnotation(com.holonplatform.core.beans.Validator.class).isPresent()) {
			addValidators(property, property.getAnnotation(com.holonplatform.core.beans.Validator.class).get());
		} else {
			if (property.getAnnotation(com.holonplatform.core.beans.Validators.class).isPresent()) {
				addValidators(property,
						property.getAnnotation(com.holonplatform.core.beans.Validators.class).get().value());
			}
		}
		return property;
	}

	@SuppressWarnings("unchecked")
	private static void addValidators(Builder<?> property, com.holonplatform.core.beans.Validator... annotations)
			throws BeanIntrospectionException {
		if (annotations != null) {
			for (com.holonplatform.core.beans.Validator annotation : annotations) {
				try {

					property.validator(annotation.value().newInstance());

					LOGGER.debug(() -> "BeanPropertyValidationPostProcessor: added validator to property [" + property
							+ "]: [" + annotation.value().getName() + "]");
				} catch (Exception e) {
					throw new BeanIntrospectionException(
							"Failed to instantiate Validator class [" + annotation.value() + "]", e);
				}
			}
		}
	}

}
