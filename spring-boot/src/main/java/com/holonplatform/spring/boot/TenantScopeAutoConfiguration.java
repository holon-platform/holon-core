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
package com.holonplatform.spring.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.EnableTenantScope;
import com.holonplatform.spring.internal.tenant.TenantScope;

/**
 * Spring boot auto-configuration class to setup the Spring <em>tenant</em> scope when a {@link TenantResolver} bean is
 * available.
 * 
 * @see EnableTenantScope
 * 
 * @since 5.0.0
 */
@Configuration
@ConditionalOnClass(TenantScope.class)
@ConditionalOnBean(TenantResolver.class)
@EnableTenantScope
public class TenantScopeAutoConfiguration {

}
