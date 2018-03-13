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

import com.holonplatform.core.datastore.DataMappable;
import com.holonplatform.core.property.PathProperty;

/**
 * Annotation which can be used on bean classes to declare the data {@link DataMappable#PATH} mapping, i.e. the actual
 * data element name when it is not the same as the bean class or the bean property name.
 * <p>
 * When used on a bean class type with a {@link BeanIntrospector}, the {@link DataMappable#PATH} configuration property
 * will be setted in the {@link BeanPropertySet} configuration. It will represent the persistence <em>entity</em> name
 * of the actual data model. For example, in a relational data model could represent the table name.
 * </p>
 * <p>
 * When used on a bean property or getter method with a {@link BeanIntrospector}, the {@link DataMappable#PATH}
 * configuration property will be setted in the corresponding {@link PathProperty} configuration. It will represent the
 * persistence <em>property</em> name of the actual data model. For example, in a relational data model could represent
 * a table column name.
 * </p>
 *
 * @since 5.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, })
@Documented
public @interface DataPath {

	/**
	 * Get the data path mapping name.
	 * <p>
	 * When the annotation is used with a {@link BeanIntrospector}, this value is used as the {@link DataMappable#PATH}
	 * configuration property value.
	 * </p>
	 * @return The data path name
	 */
	String value();

}
