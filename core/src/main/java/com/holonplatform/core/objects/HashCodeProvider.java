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

import java.io.Serializable;
import java.util.Optional;

/**
 * Object <code>hashCode</code> provider.
 * 
 * @param <T> Object type
 *
 * @since 5.1.0
 */
@FunctionalInterface
public interface HashCodeProvider<T> extends Serializable {

	/**
	 * Get the hash code of given object.
	 * <p>
	 * The provided hash code should follow the same semantic of {@link Object#hashCode()}.
	 * </p>
	 * @param object The object for which to obtain the hash code (may be null)
	 * @return The object hash code, or an empty Optional to fallback to the default hash code value
	 */
	Optional<Integer> hashCode(T object);

}
