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
package com.holonplatform.core;

import java.util.Optional;

/**
 * Declares the support for a {@link ParameterSet} type configuration property container.
 *
 * @param <C> Actual configuration {@link ParameterSet} type
 *
 * @since 5.1.0
 */
public interface HasConfiguration<C extends ParameterSet> {

	/**
	 * Get the configuration property set.
	 * @return The configuration property set (must never be null)
	 */
	C getConfiguration();

	/**
	 * Checks if given object supports a configuration parameter set.
	 * @param object The object to check
	 * @return If given object supports a configuration parameter set, the object itself is returned as a
	 *         {@link HasConfiguration} instance. Otherwise, an empty Optional is returned.
	 */
	static Optional<HasConfiguration<?>> hasConfiguration(Object object) {
		return (object != null && HasConfiguration.class.isAssignableFrom(object.getClass()))
				? Optional.of((HasConfiguration<?>) object)
				: Optional.empty();
	}

}
