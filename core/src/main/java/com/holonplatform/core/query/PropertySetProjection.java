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
package com.holonplatform.core.query;

import com.holonplatform.core.internal.query.DefaultPropertySetProjection;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;

/**
 * A {@link QueryProjection} which uses a {@link PropertySet} to define the projection selection and returns
 * {@link PropertyBox} type results.
 * 
 * @since 5.0.0
 */
public interface PropertySetProjection extends QueryProjection<PropertyBox> {

	/**
	 * Get the projection property set
	 * @return Projection property set
	 */
	PropertySet<?> getPropertySet();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryProjection#getType()
	 */
	@Override
	default Class<? extends PropertyBox> getType() {
		return PropertyBox.class;
	}

	/**
	 * Create a PropertySetProjection using given <code>propertySet</code>
	 * @param propertySet Projection property set
	 * @return PropertySetProjection on given PropertySet
	 */
	static PropertySetProjection of(PropertySet<?> propertySet) {
		return new DefaultPropertySetProjection(propertySet);
	}

	/**
	 * Create a PropertySetProjection using given <code>properties</code>
	 * @param <P> Actual property type
	 * @param properties Projection property set
	 * @return PropertySetProjection on given properties
	 */
	@SuppressWarnings("rawtypes")
	static <P extends Property> PropertySetProjection of(Iterable<P> properties) {
		return new DefaultPropertySetProjection(
				(properties instanceof PropertySet) ? (PropertySet) properties : PropertySet.of(properties));
	}

}
