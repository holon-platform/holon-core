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
package com.holonplatform.core.datastore;

import java.util.Optional;

import com.holonplatform.core.config.ConfigProperty;

/**
 * Interface to declare support for data mapping, i.e. to provide the String value of the data model name to which an
 * object is bound.
 *
 * @since 5.1.0
 */
public interface DataMappable {

	/**
	 * Default prefix for data mapping configuration properties.
	 */
	static final String DEFAULT_CONFIG_PROPERTY_PREFIX = DataMappable.class.getPackage().getName() + ".mappings";

	/**
	 * Configuration property to declare the {@link String} type <em>path</em> name to be used to map a data related
	 * object to the actual persistence data model element name.
	 */
	static final ConfigProperty<String> PATH = ConfigProperty.create(DEFAULT_CONFIG_PROPERTY_PREFIX + ".path",
			String.class);

	/**
	 * Get the data model path name to which this object is bound, if available.
	 * @return Optional data model path name
	 */
	Optional<String> getDataPath();

	/**
	 * Checks if given object is {@link DataMappable}.
	 * @param object The object to check
	 * @return If given object is {@link DataMappable}, the object itself is returned as a {@link DataMappable}
	 *         instance. Otherwise, an empty Optional is returned.
	 */
	static Optional<DataMappable> isDataMappable(Object object) {
		return (object != null && DataMappable.class.isAssignableFrom(object.getClass()))
				? Optional.of((DataMappable) object)
				: Optional.empty();
	}

}
