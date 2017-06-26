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
package com.holonplatform.core.property;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which can be used to declare the reference to a {@link PropertySet} instance.
 * <p>
 * The {@link #value()} class can be either:
 * <ul>
 * <li>A {@link PropertySet} type class: in this case, a new instance of such class will be used as property set;</li>
 * <li>A class which contains the {@link PropertySet} instance as a <code>public static</code> field.</li>
 * </ul>
 * If the {@link #field()} attribute is specified, it will be used to locate the {@link PropertySet} type field in the
 * {@link #value()} class. Otherwise, a single <code>public static</code> {@link PropertySet} type field is expected for
 * the {@link #value()} class.
 *
 * @since 5.0.0
 */
@Target({ ElementType.PARAMETER, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertySetRef {

	/**
	 * The {@link PropertySet} type class or the class in which the {@link PropertySet} is declared as a
	 * <code>public static</code> field.
	 * @return PropertySet class
	 */
	Class<?> value();

	/**
	 * The optional {@link PropertySet} field name in the class declared through {@link #value()}.
	 * @return PropertySet field name
	 */
	String field() default "";

}
