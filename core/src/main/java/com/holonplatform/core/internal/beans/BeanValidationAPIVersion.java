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
package com.holonplatform.core.internal.beans;

import com.holonplatform.core.internal.utils.ClassUtils;

/**
 * Enumeration of bean validation API versions.
 *
 * @since 5.2.0
 */
public enum BeanValidationAPIVersion {

	/**
	 * Unknown validation API version.
	 */
	UNKNOWN,

	/**
	 * Version 1.x
	 */
	V_1x,

	/**
	 * Version 2.0.x
	 */
	V_20x;

	/**
	 * Get whether the bean validation API version is 2.0.x or higher.
	 * @return <code>true</code> if the bean validation API version is 2.0.x or higher
	 */
	public boolean is20xOrHigher() {
		return this == BeanValidationAPIVersion.V_20x;
	}

	/**
	 * Detect the bean validation API version for given ClassLoader.
	 * @param classLoader The ClassLoader to use
	 * @return Bean validation API version, or {@link BeanValidationAPIVersion#UNKNOWN} if not detected
	 */
	public static BeanValidationAPIVersion getVersion(ClassLoader classLoader) {
		if (classLoader != null) {
			if (ClassUtils.isPresent("jakarta.validation.Validation", classLoader)) {
				if (ClassUtils.isPresent("jakarta.validation.ClockProvider", classLoader)) {
					return BeanValidationAPIVersion.V_20x;
				}
				return BeanValidationAPIVersion.V_1x;
			}
		}
		return BeanValidationAPIVersion.UNKNOWN;
	}

}
