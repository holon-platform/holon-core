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
package com.holonplatform.core.objects;

/**
 * Handler to check objects equality.
 * 
 * @param <T> Object type
 *
 * @since 5.1.0
 */
public interface EqualsHandler<T> {

	/**
	 * Gets whether given <code>object</code> is <em>equal to</em> to another object.
	 * <p>
	 * The equals logic should follow the same semantic of {@link Object#equals(Object)}.
	 * </p>
	 * @param object The object to compare (may be null)
	 * @param other The object with which the first object must be compared (may be null)
	 * @return <code>true</code> if the two object must be considered equal, <code>false</code> otherwise
	 */
	boolean equals(T object, Object other);

}
