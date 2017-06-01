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

import com.holonplatform.core.property.Property;

/**
 * Repeatable annotation which can be used on a bean property to specify a configuration key and its value to be setted
 * in the {@link Property} configuration.
 * <p>
 * This annotation is limited to {@link String} type configuration values. Refer to {@link BeanPropertyPostProcessor} to
 * implement more advanced bean property configuration operations.
 * </p>
 * 
 * @since 5.0.0
 *
 * @see Property#getConfiguration()
 * @see BeanIntrospector
 */
@Repeatable(Configs.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface Config {

	/**
	 * Get the property configuration key.
	 * @return Property configuration key
	 */
	String key();

	/**
	 * Get the value to bind to property configuration {@link #key()}.
	 * @return Property configuration value
	 */
	String value();

}
