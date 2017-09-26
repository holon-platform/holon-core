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
package com.holonplatform.core.internal.property;

import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.property.PropertyValueProvider;
import com.holonplatform.core.property.VirtualProperty;
import com.holonplatform.core.property.VirtualProperty.Builder;

/**
 * Default {@link VirtualProperty} implementation with {@link Localizable} support.
 * 
 * @param <T> Property value type
 * 
 * @since 5.0.0
 */
public class DefaultVirtualProperty<T> extends AbstractProperty<T, DefaultVirtualProperty<T>>
		implements Builder<T, DefaultVirtualProperty<T>> {

	private static final long serialVersionUID = -7091967623813118367L;

	/*
	 * Optional value provider
	 */
	private PropertyValueProvider<T> valueProvider;

	/**
	 * Constructor
	 * @param type Property value type
	 */
	public DefaultVirtualProperty(Class<? extends T> type) {
		super(type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.ValueProviderProperty#getValueProvider()
	 */
	@Override
	public PropertyValueProvider<T> getValueProvider() {
		return valueProvider;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.VirtualPropertyBuilder#valueProvider(com.holonplatform.core.property.
	 * PropertyValueProvider)
	 */
	@Override
	public DefaultVirtualProperty<T> valueProvider(PropertyValueProvider<T> valueProvider) {
		this.valueProvider = valueProvider;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VirtualProperty [type=" + ((getType() != null) ? getType().getName() : "null") + "]";
	}

}
