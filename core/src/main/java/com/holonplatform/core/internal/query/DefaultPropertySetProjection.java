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
package com.holonplatform.core.internal.query;

import com.holonplatform.core.internal.query.QueryProjectionVisitor.VisitableQueryProjection;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.PropertySetProjection;

/**
 * Default {@link PropertySetProjection} implementation.
 *
 * @since 5.0.0
 */
public class DefaultPropertySetProjection implements PropertySetProjection, VisitableQueryProjection<PropertyBox> {

	private final PropertySet<?> propertySet;

	/**
	 * Constructor
	 * @param propertySet Projection property set
	 */
	public DefaultPropertySetProjection(PropertySet<?> propertySet) {
		super();
		ObjectUtils.argumentNotNull(propertySet, "PropertySet must be not null");
		this.propertySet = propertySet;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.PropertySetProjection#getPropertySet()
	 */
	@Override
	public PropertySet<?> getPropertySet() {
		return propertySet;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getPropertySet() == null) {
			throw new InvalidExpressionException("Null property set");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.query.QueryProjectionVisitor.VisitableQueryProjection#accept(com.holonplatform.
	 * core.internal.query.QueryProjectionVisitor, java.lang.Object)
	 */
	@Override
	public <R, C> R accept(QueryProjectionVisitor<R, C> visitor, C context) {
		return visitor.visit(this, context);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultPropertySetProjection [propertySet=" + propertySet + "]";
	}

}
