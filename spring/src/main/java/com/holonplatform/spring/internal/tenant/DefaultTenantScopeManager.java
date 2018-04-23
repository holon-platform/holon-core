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

import java.lang.ref.WeakReference;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.spring.TenantScopeManager;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * Default {@link TenantScopeManager} implementation.
 *
 * @since 5.1.0
 */
public class DefaultTenantScopeManager implements TenantScopeManager {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = SpringLogger.create();

	/**
	 * Tenant scope reference
	 */
	private final WeakReference<TenantScope> tenantScopeRef;

	/**
	 * Constructor.
	 * @param tenantScope Tenant scope (not null)
	 */
	public DefaultTenantScopeManager(TenantScope tenantScope) {
		super();
		ObjectUtils.argumentNotNull(tenantScope, "TenantScope must be not null");
		this.tenantScopeRef = new WeakReference<>(tenantScope);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.spring.TenantScopeManager#discardTenantBeanStore(java.lang.String)
	 */
	@Override
	public synchronized void discardTenantBeanStore(String tenantId) {
		ObjectUtils.argumentNotNull(tenantId, "Tenant id must be not null");
		final TenantScope tenantScope = tenantScopeRef.get();
		if (tenantScope != null) {
			tenantScope.destroy(tenantId);
			LOGGER.info("Tenant bean store discarded for tenant id [" + tenantId + "]");
		} else {
			LOGGER.debug(() -> "TenantScope reference is no more available - skip discardTenantBeanStore");
		}
	}

}
