/*
 * Copyright 2016-2018 Axioma srl.
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

import org.springframework.context.annotation.Import;

import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.spring.internal.datastore.DatastoreConfigurationRegistrar;

/**
 * Enables a bean post processor to automatically configure {@link Datastore} bean types using the Spring context. The
 * configuration deals with:
 * <ul>
 * <li>Registration of {@link DatastoreResolver} annotated beans.</li>
 * <li>Registration of {@link DatastoreCommodityFactory} annotated beans.</li>
 * <li>Registration of {@link DatastorePostProcessor} type beans.</li>
 * </ul>
 *
 * @since 5.2.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DatastoreConfigurationRegistrar.class)
public @interface EnableDatastoreConfiguration {

}
