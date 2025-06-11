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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import jakarta.annotation.Priority;

import com.holonplatform.core.beans.BeanIntrospector.BeanIntrospectionException;
import com.holonplatform.core.beans.BeanProperty;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.beans.Converter;
import com.holonplatform.core.beans.Converter.BUILTIN;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link BeanPropertyPostProcessor} to setup a {@link PropertyValueConverter} using {@link Converter} annotation.
 *
 * @since 5.0.0
 */
@Priority(150)
public class BeanPropertyConverterPostProcessor implements BeanPropertyPostProcessor {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = BeanLogger.create();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertyPostProcessor#processBeanProperty(com.holonplatform.core.beans.
	 * BeanProperty.Builder, java.lang.Class)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BeanProperty.Builder<?> processBeanProperty(BeanProperty.Builder<?> property, Class<?> beanOrNestedClass) {
		if (property.getAnnotation(Converter.class).isPresent()) {

			BUILTIN builtin = property.getAnnotation(Converter.class).get().builtin();

			Class<? extends PropertyValueConverter> converterClass = property.getAnnotation(Converter.class).get()
					.value();

			if (BUILTIN.NONE == builtin && PropertyValueConverter.class == converterClass) {
				throw new BeanIntrospectionException(
						"No builtin or custom PropertyValueConverter declared using Converter annotation on bean property ["
								+ property + "]");
			}

			if (BUILTIN.NONE != builtin && PropertyValueConverter.class != converterClass) {
				throw new BeanIntrospectionException(
						"Both builtin and custom PropertyValueConverter declared using Converter annotation on bean property ["
								+ property + "]: only one is admitted");
			}

			if (BUILTIN.NONE != builtin) {
				converterClass = builtin.getConverter();
			}

			// inspect constructors
			Constructor<?>[] constructors = converterClass.getDeclaredConstructors();
			if (constructors == null || constructors.length == 0) {
				throw new BeanIntrospectionException("Invalid PropertyValueConverter [" + converterClass.getName()
						+ "] declared using Converter annotation on bean property [" + property
						+ "]: no accessible Constructor found");
			}

			final PropertyValueConverter converter;

			try {
				Constructor<?> constructor = getNoArgumentsConstructor(constructors);
				if (constructor == null) {
					throw new BeanIntrospectionException("Invalid PropertyValueConverter [" + converterClass.getName()
							+ "] declared using Converter annotation on bean property [" + property
							+ "]: no public Constructor with no arguments found");
				}
				converter = (PropertyValueConverter) constructor.newInstance();
			} catch (Exception e) {
				throw new BeanIntrospectionException(
						"Failed to instantiate PropertyValueConverter [" + converterClass.getName()
								+ "] declared using Converter annotation on bean property [" + property + "]",
						e);
			}

			property.converter(converter);

			LOGGER.debug(() -> "BeanPropertyConverterPostProcessor: setted property [" + property + "] converter ["
					+ converter + "]");
		}
		return property;
	}

	private static Constructor<?> getNoArgumentsConstructor(Constructor<?>[] constructors) {
		for (Constructor<?> constructor : constructors) {
			if (Modifier.isPublic(constructor.getModifiers())) {
				if (constructor.getParameterTypes().length == 0) {
					return constructor;
				}
			}
		}
		return null;
	}

}
