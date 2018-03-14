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

import com.holonplatform.core.DataMappable;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.internal.beans.DefaultBeanDataTarget;

/**
 * A {@link DataTarget} which uses a bean class a path type.
 * <p>
 * The data target path name is the bean class simple name.
 * </p>
 * <p>
 * If the bean class property set provides a {@link DataMappable#PATH} configuration property value (for example
 * detected from the {@link DataPath} annotation at bean introspection time), such data path will be provided as
 * {@link DataMappable#getDataPath()}.
 * </p>
 * 
 * @param <T> Bean type
 *
 * @since 5.1.0
 */
public interface BeanDataTarget<T> extends DataTarget<T> {

	/**
	 * Create a new {@link BeanDataTarget} using given bean class.
	 * @param <T> Bean type
	 * @param beanClass The bean class (not null)
	 * @return A new {@link BeanDataTarget} on given bean class
	 */
	static <T> BeanDataTarget<T> of(Class<? extends T> beanClass) {
		return new DefaultBeanDataTarget<>(beanClass);
	}

}
