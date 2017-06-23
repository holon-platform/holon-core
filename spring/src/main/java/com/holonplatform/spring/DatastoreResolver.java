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
package com.holonplatform.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.Datastore;

/**
 * Annotation wich can be used on a {@link ExpressionResolver} type class to automatically register this resolver into a
 * {@link Datastore} Spring bean.
 * 
 * <p>
 * The {@link #datastoreBeanName()} can be used to uniquely identify the {@link Datastore} bean into which register the
 * resolver, if more than one Datastore bean is present in Spring context.
 * </p>
 * 
 * <p>
 * The annotation is itself annotated with {@link Component} to register the annotated class as a Spring bean.
 * </p>
 *
 * @since 5.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface DatastoreResolver {

	/**
	 * Get the optional {@link Datastore} bean name into which register the resolver, if more than one Datastore bean is
	 * present in Spring context.
	 * @return Datastore bean name
	 */
	String datastoreBeanName() default "";

}
