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
package com.holonplatform.core.i18n;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to declare the caption (display name or short description) for an element, with internationalization
 * support using {@link #messageCode()}.
 * 
 * @since 5.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
@Documented
public @interface Caption {

	final static String NO_VALUE = "";

	/**
	 * Get the caption text.
	 * @return Caption text
	 */
	String value();

	/**
	 * Get the optional caption localization message code.
	 * @return Caption localization message code, or {@link #NO_VALUE} if no localization code is provided
	 */
	String messageCode() default NO_VALUE;

}
