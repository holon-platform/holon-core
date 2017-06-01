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
package com.holonplatform.core;

import java.util.Optional;
import java.util.ServiceLoader;

import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.ContextManager;

/**
 * A {@link Context} scope to hold, manage and access context resources. Every resource is identified by a
 * {@link String} key.
 * <p>
 * Each scope is identified by a name which must be unique among all registered context scopes and may implement a
 * specific strategy to store and provide resources.
 * </p>
 * <p>
 * Every concrete scope must provide the {@link #get(String, Class)} operation. Resource management operations
 * (put/remove) are optional and may not be available, throwing an {@link UnsupportedOperationException}.
 * </p>
 * <p>
 * A ContextScope must be registered in {@link ContextManager} to be accessibile from {@link Context} façade and join
 * generic context resource retrieval chain. Scope registration can be performed using default Java extension through
 * {@link ServiceLoader} or directly using {@link ContextManager#registerScope(ClassLoader, ContextScope)}.
 * </p>
 * <p>
 * The {@link #getOrder()} method can be used to assign a order to the scope within the scopes chain for context
 * resource resolution. Lower order values mean highest precedence.
 * </p>
 * 
 * @since 5.0.0
 */
public interface ContextScope {

	/**
	 * Gets the scope name. Scope name must be unique among all registered context scopes.
	 * @return the scope name
	 */
	String getName();

	/**
	 * Gets the scope order. Lower order values mean highest precedence.
	 * @return the scope order
	 */
	int getOrder();

	/**
	 * Try to obtain the resource identified by given <code>resourceKey</code> and with expected
	 * <code>resourceType</code>.
	 * @param resourceKey Resource key (not null)
	 * @param resourceType Expected resource type (not null)
	 * @param <T> Resource type
	 * @return Optional resource instance, an empty Optional if resource is not available from scope according to scope
	 *         resource provisioning strategy
	 * @throws TypeMismatchException If a resource is available but the expected type is not compatible with the
	 *         resource type
	 */
	<T> Optional<T> get(String resourceKey, Class<T> resourceType) throws TypeMismatchException;

	/**
	 * Stores a resource reference identified by given <code>resourceKey</code>.
	 * @param resourceKey Resource key (not null)
	 * @param value Resource instance. If <code>null</code>, if a previous resource was bound to given resource key, it
	 *        will be removed from scope.
	 * @param <T> Resource instance type
	 * @return Optional previous resource instance bound to given key
	 * @throws UnsupportedOperationException If the concrete scope implementation does not support this operation
	 */
	<T> Optional<T> put(String resourceKey, T value) throws UnsupportedOperationException;

	/**
	 * Stores a resource reference identified by given <code>resourceKey</code>, only if there is not a resource
	 * instance already bound to given key.
	 * @param resourceKey Resource key (not null)
	 * @param value Resource instance, if <code>null</code>, this method should do nothing.
	 * @param <T> Resource instance type
	 * @return Optional previous resource instance bound to given key. If there was no resource already bound to the
	 *         key, an empty Optional is returned, meaning the given <code>value</code> has been associated to the
	 *         resource key.
	 * @throws UnsupportedOperationException If the concrete scope implementation does not support this operation
	 */
	<T> Optional<T> putIfAbsent(String resourceKey, T value) throws UnsupportedOperationException;

	/**
	 * Removes the resource reference bound to the given <code>resourceKey</code> from scope, if any.
	 * @param resourceKey Resource key (not null)
	 * @return <code>true</code> if a resource instance was bound to the given key and it's been removed,
	 *         <code>false</code> otherwise.
	 * @throws UnsupportedOperationException If the concrete scope implementation does not support this operation
	 */
	boolean remove(String resourceKey) throws UnsupportedOperationException;

}
