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
package com.holonplatform.spring.internal.tenant;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextException;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.EnableTenantScope;
import com.holonplatform.spring.TenantScopeManager;
import com.holonplatform.spring.internal.BeanRegistryUtils;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * A {@link BeanFactoryPostProcessor} to register the {@link TenantScope}.
 * 
 * @since 5.1.0
 */
public class TenantScopePostProcessor implements BeanFactoryPostProcessor {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = SpringLogger.create();

	/**
	 * {@link TenantResolver} bean name to use to obtain the current tenant id.
	 */
	private final String tenantResolver;

	/**
	 * Whether to enable
	 */
	private final boolean enableTenantScopeManager;

	/**
	 * The tenant scope
	 */
	private TenantScope tenantScope;

	public TenantScopePostProcessor(String tenantResolver, boolean enableTenantScopeManager) {
		super();
		this.tenantResolver = tenantResolver;
		this.enableTenantScopeManager = enableTenantScopeManager;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.
	 * beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		String tenantResolverBeanName = tenantResolver;

		if (tenantResolverBeanName == null || tenantResolverBeanName.trim().length() == 0) {
			// try to detect a unique TenantResolver bean type
			List<String> names = BeanRegistryUtils.getBeanNames(beanFactory, TenantResolver.class);
			if (names.isEmpty()) {
				throw new ApplicationContextException("The tenant scope cannot be registered: no ["
						+ TenantResolver.class.getName() + "] type bean found");
			}
			if (names.size() > 1) {
				throw new ApplicationContextException("The tenant scope cannot be registered: more than one ["
						+ TenantResolver.class.getName() + "] type bean definition found " + names.toString()
						+ " - Please specify the TenantResolver bean name to use through the @EnableTenantScope annotation "
						+ "or the [" + EnableTenantScope.TENANT_RESOLVER_PROPERTY_NAME + "] property name");
			}
			tenantResolverBeanName = names.get(0);
		}

		if (tenantResolverBeanName == null) {
			throw new ApplicationContextException(
					"The tenant scope cannot be registered: missing TenantResolver bean definition name");
		}

		tenantScope = new TenantScope(tenantResolverBeanName, beanFactory);

		beanFactory.registerScope(TenantScope.SCOPE_NAME, tenantScope);

		LOGGER.info("Registered scope [" + TenantScope.SCOPE_NAME + "] using TenantResolver bean name ["
				+ tenantResolverBeanName + "]");

		// Tenant scope manager registration
		if (enableTenantScopeManager) {
			beanFactory.registerSingleton(TenantScopeManager.class.getName(),
					new DefaultTenantScopeManager(tenantScope));
		}
	}

	/**
	 * Unregister the tenant scope, invoking the scoped beans destruction callbacks.
	 */
	public void unregister() {
		if (tenantScope != null) {
			tenantScope.destroy();
			tenantScope = null;
			LOGGER.info("Tenant scope successfully destroyed");
		}
	}

}
