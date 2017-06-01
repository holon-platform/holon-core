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

import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.internal.DefaultParameterSet;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyConfiguration.PropertyConfigurationEditor;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Default {@link PropertyConfiguration} implementation.
 *
 * @since 5.0.0
 */
public class DefaultPropertyConfiguration extends DefaultParameterSet implements PropertyConfigurationEditor {

	private static final long serialVersionUID = 3701269276925181438L;

	/**
	 * Temporal type
	 */
	private TemporalType temporalType;

	/**
	 * Construct a new PropertyConfiguration
	 */
	public DefaultPropertyConfiguration() {
		super();
	}

	/**
	 * Construct a new PropertyConfiguration using given <code>parameters</code> as initial value.
	 * @param parameters Initial parameters
	 */
	public DefaultPropertyConfiguration(Map<String, Object> parameters) {
		super(parameters);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyConfiguration#getTemporalType()
	 */
	@Override
	public Optional<TemporalType> getTemporalType() {
		return Optional.ofNullable(temporalType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyConfiguration.PropertyConfigurationEditor#setTemporalType(com.
	 * holonplatform.core.temporal.TemporalType)
	 */
	@Override
	public void setTemporalType(TemporalType temporalType) {
		this.temporalType = temporalType;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultPropertyConfiguration [temporalType=" + temporalType + ", parameters" + super.toString() + "]";
	}

}
