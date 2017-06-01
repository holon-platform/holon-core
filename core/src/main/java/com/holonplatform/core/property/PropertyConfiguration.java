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

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.internal.property.DefaultPropertyConfiguration;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.temporal.TemporalType;

/**
 * {@link Property} configuration parameters.
 * <p>
 * Extends {@link ParameterSet} to inherit parameter handling operations.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see Property
 */
public interface PropertyConfiguration extends ParameterSet {

	/**
	 * Get the optional {@link TemporalType} specification for {@link Date} or {@link Calendar} type properties, which
	 * can be used to perform consistent operations on property value, such as presentation, rendering or persistence
	 * data manipulation.
	 * @return Optional property {@link TemporalType}
	 */
	Optional<TemporalType> getTemporalType();

	/**
	 * Create a new editable {@link PropertyConfiguration} instance using default implementation class.
	 * @return PropertyConfigurationEditor instance
	 */
	static PropertyConfigurationEditor create() {
		return new DefaultPropertyConfiguration();
	}

	/**
	 * Create a new editable {@link PropertyConfiguration} instance using default implementation class, cloning the
	 * configuration parameters from given <code>fromConfiguration</code> configuration.
	 * @param fromConfiguration Property configuration to clone (not null)
	 * @return PropertyConfigurationEditor instance with parameters cloned from given configuration
	 */
	static PropertyConfigurationEditor clone(PropertyConfiguration fromConfiguration) {
		ObjectUtils.argumentNotNull(fromConfiguration, "Configuration to clone must be not null");
		DefaultPropertyConfiguration configuration = new DefaultPropertyConfiguration();
		fromConfiguration.getTemporalType().ifPresent(t -> configuration.setTemporalType(t));
		fromConfiguration.forEachParameter((n, v) -> configuration.addParameter(n, v));
		return configuration;
	}

	// Editor

	/**
	 * {@link PropertyConfiguration} editor interface to provide configuration parameters management.
	 */
	public interface PropertyConfigurationEditor extends PropertyConfiguration {

		/**
		 * Add a configuration parameter. If parameter with <code>name</code> already exists, its value will be replaced
		 * by the new <code>value</code>.
		 * @param name Parameter name to add (not null)
		 * @param value Parameter value
		 */
		void addParameter(String name, Object value);

		/**
		 * Remove the parameter named <code>name</code>, if exists.
		 * @param name Parameter name to remove (not null)
		 */
		void removeParameter(String name);

		/**
		 * Set the optional {@link TemporalType} specification for {@link Date} or {@link Calendar} type properties.
		 * @param temporalType the {@link TemporalType} to set
		 */
		void setTemporalType(TemporalType temporalType);

	}

}
