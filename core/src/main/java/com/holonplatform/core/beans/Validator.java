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
package com.holonplatform.core.beans;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Repeatable annotation which can be used on a bean property to specify a {@link com.holonplatform.core.Validator} to
 * add to the property generated using bean property introspection.
 *
 * @since 5.0.0
 */
@Repeatable(Validators.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface Validator {

	/**
	 * Get the validator class from which to obtain the {@link com.holonplatform.core.Validator} instance to add to the
	 * property.
	 * <p>
	 * The specified class must provide a <code>public</code> constructor with no arguments.
	 * </p>
	 * @return Validator class
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends com.holonplatform.core.Validator> value();

}
