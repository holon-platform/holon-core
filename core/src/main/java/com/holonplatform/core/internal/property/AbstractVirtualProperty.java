/*
 * Copyright 2016-2018 Axioma srl.
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

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyValueProvider;
import com.holonplatform.core.property.VirtualProperty;

/**
 * Base {@link VirtualProperty} implementation.
 *
 * @param <T> Property type
 * 
 * @since 5.2.0
 */
public abstract class AbstractVirtualProperty<T, P extends VirtualProperty<T>, B extends VirtualProperty.Builder<T, P, B>>
		extends AbstractProperty<T, P, B> implements VirtualProperty.Builder<T, P, B> {

	private static final long serialVersionUID = -5224780091372241935L;

	/**
	 * Property name
	 */
	private String name;

	/**
	 * Optional value provider
	 */
	private PropertyValueProvider<T> valueProvider;

	/**
	 * Constructor.
	 * @param type Property type (not null)
	 */
	public AbstractVirtualProperty(Class<? extends T> type) {
		super(type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.VirtualProperty.Builder#name(java.lang.String)
	 */
	@Override
	public B name(String name) {
		ObjectUtils.argumentNotNull(name, "Property name must be not null");
		this.name = name;
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.VirtualProperty.Builder#valueProvider(com.holonplatform.core.property.
	 * PropertyValueProvider)
	 */
	@Override
	public B valueProvider(PropertyValueProvider<T> valueProvider) {
		ObjectUtils.argumentNotNull(valueProvider, "PropertyValueProvider must be not null");
		this.valueProvider = valueProvider;
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.VirtualProperty#getValueProvider()
	 */
	@Override
	public PropertyValueProvider<T> getValueProvider() {
		return valueProvider;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#getName()
	 */
	@Override
	public String getName() {
		if (name == null) {
			return VirtualProperty.class.getSimpleName() + "|" + hashCode();
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("VirtualProperty [name=");
		sb.append(getName());
		sb.append(", type=");
		sb.append(((getType() != null) ? getType().getName() : "null"));
		sb.append("]");
		return sb.toString();
	}

}
