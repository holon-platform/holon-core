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

import com.holonplatform.core.beans.BeanProperty;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.beans.Temporal;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.temporal.TemporalType;

/**
 * A {@link BeanPropertyPostProcessor} to setup a the property {@link TemporalType} using the {@link Temporal}
 * annotation.
 *
 * @since 5.0.0
 */
@Priority(170)
public class BeanPropertyTemporalPostProcessor implements BeanPropertyPostProcessor {

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
	public BeanProperty.Builder<?> processBeanProperty(BeanProperty.Builder<?> property, Class<?> beanOrNestedClass) {
		property.getAnnotation(Temporal.class).ifPresent(a -> {
			property.temporalType(a.value());
			LOGGER.debug(() -> "BeanPropertyTemporalPostProcessor: setted property [" + property
					+ "] temporalType to: [" + a.value() + "]");
		});
		return property;
	}

}
