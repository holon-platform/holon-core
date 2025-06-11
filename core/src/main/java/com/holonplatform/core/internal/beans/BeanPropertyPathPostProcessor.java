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

import java.util.Optional;

import jakarta.annotation.Priority;

import com.holonplatform.core.DataMappable;
import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanProperty;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.beans.DataPath;
import com.holonplatform.core.internal.Logger;

/**
 * A {@link BeanPropertyPostProcessor} to inspect default {@link DataPath} annotation and setup the bean property
 * {@link DataMappable#PATH} configuration parameter.
 * <p>
 * This post processor is automatically registered in default {@link BeanIntrospector} instances.
 * </p>
 *
 * @since 5.1.0
 */
@Priority(120)
public class BeanPropertyPathPostProcessor implements BeanPropertyPostProcessor {

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
	public BeanProperty.Builder<?> processBeanProperty(final BeanProperty.Builder<?> property,
			Class<?> beanOrNestedClass) {
		// check read method
		final Optional<DataPath> methodPath = property.getReadMethod().map(m -> m.getAnnotation(DataPath.class))
				.filter(a -> a.value().trim().length() > 0);
		if (methodPath.isPresent()) {
			property.withConfiguration(DataMappable.PATH, methodPath.get().value());
			LOGGER.debug(() -> "BeanPropertyPathPostProcessor: setted property [" + property + "] data path to ["
					+ methodPath.get().value() + "]");
		} else {
			// check property field
			property.getAnnotation(DataPath.class).filter(a -> a.value().trim().length() > 0).ifPresent(a -> {
				property.withConfiguration(DataMappable.PATH, a.value());
				LOGGER.debug(() -> "BeanPropertyPathPostProcessor: setted property [" + property + "] data path to ["
						+ a.value() + "]");
			});
		}
		return property;
	}

}
