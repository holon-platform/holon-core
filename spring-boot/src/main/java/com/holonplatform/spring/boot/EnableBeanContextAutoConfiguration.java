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
package com.holonplatform.spring.boot;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import com.holonplatform.core.Context;
import com.holonplatform.core.ContextScope;
import com.holonplatform.spring.EnableBeanContext;

/**
 * Spring boot auto-configuration class to setup a {@link Context} scope using Spring application
 * context (or in exact terms, using the application context {@link BeanFactory}) to provide context
 * resource instances as Spring beans.
 * @see EnableBeanContext
 * @since 5.0.0
 */
@AutoConfiguration
@ConditionalOnClass(ContextScope.class)
@EnableBeanContext
public class EnableBeanContextAutoConfiguration {

}
