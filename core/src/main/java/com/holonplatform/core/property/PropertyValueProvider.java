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
package com.holonplatform.core.property;

import java.io.Serializable;

/**
 * Interface to represent a class which act as provider for {@link Property} value.
 * <p>
 * {@link VirtualProperty}s declare to provide their value using a {@link PropertyValueProvider}. Property values
 * containers, such as {@link PropertyBox}, must call the property value provider to obtain the property value when
 * requested.
 * </p>
 * 
 * @param <T> Property value type
 * 
 * @since 5.0.0
 * 
 * @see VirtualProperty
 */
@FunctionalInterface
public interface PropertyValueProvider<T> extends Serializable {

	/**
	 * Gets the property value.
	 * @param propertyBox The {@link PropertyBox} which contains the property
	 * @return Property value
	 */
	T getPropertyValue(PropertyBox propertyBox);

}
