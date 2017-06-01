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
package com.holonplatform.auth.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which can be used on classes or methods to require authentication for resource access.
 * <p>
 * Optional allowed authentication scheme names specification is supported.
 * </p>
 * @since 5.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface Authenticate {

	/**
	 * Optional allowed authentication schemes to use to perform principal authentication.
	 * <p>
	 * When more than one scheme is specified, authentication delegate should validate scheme applicability in the order
	 * that they are declared.
	 * </p>
	 * @return Authentication scheme names
	 */
	String[] schemes() default {};

	/**
	 * Optional URI to which to redirect when the authentication fails.
	 * <p>
	 * The semantic and behaviour associated to the redirect URI is specific for every authentication delegate, and may
	 * not be supported at all.
	 * </p>
	 * @return Redirect URI
	 */
	String redirectURI() default "";

}
