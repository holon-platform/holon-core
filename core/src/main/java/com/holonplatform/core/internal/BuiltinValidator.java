/*
 * Copyright 2000-2017 Holon TDCN.
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
package com.holonplatform.core.internal;

import java.util.Optional;

import com.holonplatform.core.Validator;

/**
 * A platform builtin {@link Validator}.
 *
 * @param <T> Validation target value type
 *
 * @since 5.0.0
 */
public interface BuiltinValidator<T> extends Validator<T> {

	/**
	 * Get the validator descriptor, if available.
	 * @return Optional validator descriptor
	 */
	Optional<ValidatorDescriptor> getDescriptor();

}
