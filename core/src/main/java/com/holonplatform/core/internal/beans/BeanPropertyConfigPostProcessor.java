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
package com.holonplatform.core.internal.beans;

import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanProperty;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.beans.Config;
import com.holonplatform.core.beans.Configs;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.AnnotationUtils;

import jakarta.annotation.Priority;

/**
 * A {@link BeanPropertyPostProcessor} to inspect default {@link Config}
 * annotation and setup property configuration accordingly.
 * <p>
 * This post processor is automatically registered in default
 * {@link BeanIntrospector} instances.
 * </p>
 *
 * @since 5.0.0
 */
@Priority(250)
public class BeanPropertyConfigPostProcessor implements BeanPropertyPostProcessor {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = BeanLogger.create();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.holonplatform.core.beans.BeanPropertyPostProcessor#processBeanProperty(
	 * com.holonplatform.core.beans. BeanProperty.Builder, java.lang.Class)
	 */
	@Override
	public BeanProperty.Builder<?> processBeanProperty(BeanProperty.Builder<?> property, Class<?> beanOrNestedClass) {
		if (property.getAnnotation(Config.class).isPresent()) {
			setupPropertyConfiguration(property, property.getAnnotation(Config.class).get());
		} else {
			if (property.getAnnotation(Configs.class).isPresent()) {
				setupPropertyConfiguration(property, property.getAnnotation(Configs.class).get().value());
			}
		}
		return property;
	}

	/**
	 * Setup property configuration according to given {@link Config} annotations.
	 * 
	 * @param property    Property to setup
	 * @param annotations {@link Config} annotations
	 */
	private static void setupPropertyConfiguration(BeanProperty.Builder<?> property, Config... annotations) {
		if (annotations != null) {
			for (Config annotation : annotations) {
				String key = AnnotationUtils.getStringValue(annotation.key());
				if (key != null) {
					property.withConfiguration(key, annotation.value());
					LOGGER.debug(() -> "BeanPropertyConfigPostProcessor: added configuration value to property ["
							+ property + "]: [" + key + ":" + annotation.value() + "]");
				}
			}
		}
	}

}
