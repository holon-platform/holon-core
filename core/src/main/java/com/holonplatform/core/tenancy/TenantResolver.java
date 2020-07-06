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
package com.holonplatform.core.tenancy;

import java.util.Optional;
import java.util.concurrent.Callable;

import com.holonplatform.core.Context;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Resolver to obtain the current tenant id in a multi-tenant environment.
 * 
 * @since 5.0.0
 */
@FunctionalInterface
public interface TenantResolver {

	/**
	 * Default {@link Context} resource reference
	 */
	public static final String CONTEXT_KEY = TenantResolver.class.getName();

	/**
	 * Gets the current tenant id, if available
	 * @return Optional tenant id, an empty Optional if current tenant is not
	 *         available
	 */
	Optional<String> getTenantId();

	/**
	 * Convenience method to obtain the current {@link TenantResolver} made
	 * available as {@link Context} resource, using default {@link ClassLoader}.
	 * <p>
	 * See {@link Context#resource(String, Class)} for details about context
	 * resources availability conditions.
	 * </p>
	 * @return Optional TenantResolver, empty if not available as context resource
	 */
	static Optional<TenantResolver> getCurrent() {
		return Context.get().resource(CONTEXT_KEY, TenantResolver.class);
	}

	/**
	 * Build a static {@link TenantResolver}, returning always the given
	 * <code>tenantId</code> as current tenant id.
	 * @param tenantId Static tenant id to be returned by the resolver
	 * @return Static TenantResolver
	 */
	static TenantResolver staticTenantResolver(final String tenantId) {
		return () -> Optional.ofNullable(tenantId);
	}

	/**
	 * Execute given {@link Runnable} <code>operation</code> on behalf of given
	 * <code>tenantId</code>, binding to current thread a static
	 * {@link TenantResolver} context resource with default
	 * {@link TenantResolver#CONTEXT_KEY} key providing the specified tenant id.
	 * @param tenantId  Tenant id
	 * @param operation Operation to execute (not null)
	 * @throws RuntimeException Exception during operation execution
	 */
	static void execute(final String tenantId, final Runnable operation) throws RuntimeException {
		ObjectUtils.argumentNotNull(operation, "Runnable operation must be not null");
		try {
			Context.get().threadScope()
					.ifPresent(s -> s.put(TenantResolver.CONTEXT_KEY, TenantResolver.staticTenantResolver(tenantId)));
			operation.run();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Context.get().threadScope().ifPresent(s -> s.remove(TenantResolver.CONTEXT_KEY));
		}
	}

	/**
	 * Execute given {@link Callable} <code>operation</code> on behalf of given
	 * <code>tenantId</code>, binding to current thread a static
	 * {@link TenantResolver} context resource with default
	 * {@link TenantResolver#CONTEXT_KEY} key providing the specified tenant id.
	 * @param <V>       Operation result type
	 * @param tenantId  Tenant id
	 * @param operation Operation to execute
	 * @return Operation result
	 * @throws RuntimeException Exception during operation execution
	 */
	static <V> V execute(final String tenantId, final Callable<V> operation) throws RuntimeException {
		ObjectUtils.argumentNotNull(operation, "Runnable operation must be not null");
		try {
			Context.get().threadScope()
					.ifPresent(s -> s.put(TenantResolver.CONTEXT_KEY, TenantResolver.staticTenantResolver(tenantId)));
			return operation.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Context.get().threadScope().ifPresent(s -> s.remove(TenantResolver.CONTEXT_KEY));
		}
	}

}
