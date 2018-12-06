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

import java.util.function.Consumer;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertyBoxProperty;
import com.holonplatform.core.property.PropertyBoxProperty.PropertyBoxPropertyBuilder;
import com.holonplatform.core.property.PropertySet;

/**
 * Default {@link PropertyBoxProperty} implementation.
 *
 * @since 5.1.0
 */
public class DefaultPropertyBoxProperty
		extends AbstractPathProperty<PropertyBox, PropertyBoxProperty, PropertyBoxPropertyBuilder>
		implements PropertyBoxPropertyBuilder {

	private static final long serialVersionUID = 905462976330958644L;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 * @param propertySet Property set (not null)
	 */
	public DefaultPropertyBoxProperty(String name, PropertySet<?> propertySet) {
		super(name, PropertyBox.class);
		ObjectUtils.argumentNotNull(propertySet, "PropertySet must be not null");
		withConfiguration(PropertySet.PROPERTY_CONFIGURATION_ATTRIBUTE, propertySet);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyBoxProperty#getPropertySet()
	 */
	@Override
	public PropertySet<?> getPropertySet() {
		return getConfiguration().getParameter(PropertySet.PROPERTY_CONFIGURATION_ATTRIBUTE).orElse(null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualProperty()
	 */
	@Override
	protected PropertyBoxProperty getActualProperty() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualBuilder()
	 */
	@Override
	protected PropertyBoxPropertyBuilder getActualBuilder() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyBoxProperty#clone(java.util.function.Consumer)
	 */
	@Override
	public PropertyBoxProperty clone(
			Consumer<PathProperty.Builder<PropertyBox, PathProperty<PropertyBox>, ?>> builder) {
		return clonePathProperty(new DefaultPropertyBoxProperty(getName(), getPropertySet()), builder);
	}

}
