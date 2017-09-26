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
package com.holonplatform.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.holonplatform.core.Context;
import com.holonplatform.spring.internal.context.BeanFactoryScopeRegistrar;

/**
 * Setup a {@link Context} scope using Spring application context (or in exact terms, using the application context
 * {@link BeanFactory}) to provide context resource instances as Spring beans.
 * <p>
 * When a context resource is requested (using {@link Context#resource(String, Class)}), the scope strategy to provide a
 * matching Spring bean is defined as follow:
 * <ul>
 * <li>If a Spring bean with a name equal to the context resource key and with the same required type is found, this is
 * returned;</li>
 * <li>Otherwise, if {@link #lookupByType()} is <code>true</code> and a Spring bean of the required type, ignoring the
 * name, is present and only one candidate is available, this instance is returned.</li>
 * </ul>
 * <p>
 * The Spring BeanFactory behaviour is preserved during beans lookup, so, for example, Spring beans scopes strategy is
 * applied, just like bean retrieval using {@link ApplicationContext#getBean(String, Class)} and similar methods.
 * </p>
 * 
 * @since 5.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(BeanFactoryScopeRegistrar.class)
public @interface EnableBeanContext {

	/**
	 * Property which can be used to configure the {@link #lookupByType()} behaviour using a Spring {@link Environment}
	 * configuration property. If setted, overrides the {@link #lookupByType()} annotation attribute value.
	 */
	public static final String LOOKUP_BY_TYPE_PROPERTY_NAME = "holon.context.bean-lookup-by-type";

	/**
	 * Whether to lookup context resource bean candidates by type, ignoring bean name, when default lookup by name and
	 * type fails. Only consistent type beans with a single instance retuned by BeanFactory are taken into account.
	 * @return <code>true</code> to lookup context resource bean candidates by type ignoring bean name and context
	 *         resource key match
	 */
	boolean lookupByType() default true;

}
