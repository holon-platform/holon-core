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
package com.holonplatform.core.internal.property;

import javax.annotation.Priority;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.presentation.StringValuePresenter;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyValuePresenter;

/**
 * Default {@link PropertyValuePresenter}, using default {@link StringValuePresenter#getDefault()} and
 * {@link PropertyConfiguration} as presentation parameters source.
 *
 * @since 5.0.0
 */
@Priority(Integer.MAX_VALUE)
@SuppressWarnings("rawtypes")
public class DefaultPropertyValuePresenter implements PropertyValuePresenter {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValuePresenter#present(com.holonplatform.core.property.Property,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String present(Property property, Object value) {
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		ParameterSet.Builder<?> parameters = ParameterSet.builder().parameters(property.getConfiguration());
		property.getConfiguration().getTemporalType()
				.ifPresent(t -> parameters.parameter(StringValuePresenter.TEMPORAL_TYPE, t));
		return StringValuePresenter.getDefault().present(property.getType(), value, parameters.build());
	}

}
