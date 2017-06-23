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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Calendar;
import java.util.Date;

import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Annotation which can be used on a bean property to specify the {@link TemporalType} of a {@link Property} of
 * {@link Date} or {@link Calendar} type.
 * <p>
 * The temporal type is setted in property configuration and can be accessed through the
 * {@link PropertyConfiguration#getTemporalType()} method, which can be obtained using
 * {@link Property#getConfiguration()}.
 * </p>
 *
 * @since 5.0.0
 * 
 * @see BeanIntrospector
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Temporal {

	/**
	 * Get the property {@link TemporalType}.
	 * @return the property {@link TemporalType}
	 */
	TemporalType value();

}
