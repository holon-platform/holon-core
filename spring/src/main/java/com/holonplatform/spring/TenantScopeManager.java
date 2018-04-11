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

/**
 * Tenant scope manager API to handle tenant scoped beans lifecycle.
 *
 * @since 5.1.0
 * 
 * @see EnableTenantScope
 */
public interface TenantScopeManager {

	/**
	 * Destroy the bean store bound to given <code>tenant id</code>, i.e. removes all the tenant scoped bean instances
	 * which refer to given tenant id, triggering any associated destruction callback.
	 * @param tenantId The tenant id which identifies the bean store (not null)
	 */
	void discardTenantBeanStore(String tenantId);

}
