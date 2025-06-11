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

import jakarta.annotation.Priority;

import com.holonplatform.core.DataMappable;
import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanPropertySetPostProcessor;
import com.holonplatform.core.beans.DataPath;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.AnnotationUtils;

/**
 * A {@link BeanPropertySetPostProcessor} to inspect default {@link DataPath} annotation and setup the bean property set
 * {@link DataMappable#PATH} configuration parameter.
 * <p>
 * This post processor is automatically registered in default {@link BeanIntrospector} instances.
 * </p>
 *
 * @since 5.1.0
 */
@Priority(100)
public class BeanPropertySetPathPostProcessor implements BeanPropertySetPostProcessor {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = BeanLogger.create();

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.beans.BeanPropertySetPostProcessor#processBeanPropertySet(com.holonplatform.core.beans.
	 * BeanPropertySet.Builder, java.lang.Class)
	 */
	@Override
	public void processBeanPropertySet(com.holonplatform.core.beans.BeanPropertySet.Builder<?, ?> propertySet,
			Class<?> beanClass) {
		if (beanClass.isAnnotationPresent(DataPath.class)) {
			final String path = AnnotationUtils.getStringValue(beanClass.getAnnotation(DataPath.class).value());
			if (path != null) {
				propertySet.configuration(DataMappable.PATH, path);

				LOGGER.debug(() -> "BeanPropertySetPathPostProcessor: setted bean [" + beanClass
						+ "] property set data path to [" + path + "]");
			}
		}
	}

}
