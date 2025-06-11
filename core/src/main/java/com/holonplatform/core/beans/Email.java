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
package com.holonplatform.core.beans;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Additional validation constraint to check the annotated element is a valid e-mail address using RFC822 format rules.
 * <p>
 * Supported data types: {@link CharSequence}.
 * </p>
 *
 * @since 5.0.0
 * 
 * @deprecated Use the {@link jakarta.validation.constraints.Email} constraint of the Bean Validation API 2.x instead
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Deprecated
public @interface Email {

	/**
	 * Validation error message
	 * @return Validation error message
	 */
	String message() default "{jakarta.validation.constraints.Email.message}";

}
