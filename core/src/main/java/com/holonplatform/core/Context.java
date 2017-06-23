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
import java.util.concurrent.Callable;

import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.ContextManager;
import com.holonplatform.core.internal.DefaultContext;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Main interface to access global platform context resources.
 * 
 * <p>
 * A context resource is an object instance identified by a {@link String} key.
 * </p>
 * <p>
 * Resources are organized in {@link ContextScope}s, each of them characterized by a resource data source and a resource
 * retrieval strategy. A scope can be manageable, i.e. supporting resource addition and removal, or not.
 * </p>
 * <p>
 * Context scope can be registered either by using standard Java extension service loading using {@link ContextScope}
 * class name files under <code>META-INF/services</code>, or by direct registration using
 * {@link ContextManager#registerScope(ClassLoader, ContextScope)}.
 * </p>
 * <p>
 * When asking for a context resource using {@link #resource(String, Class)} and similar methods, all registered context
 * scopes are invoked, in the order defined by {@link ContextScope#getOrder()} precedence value. The first not null
 * resource instance obtained from a scope is the one returned to caller, if any.
 * </p>
 * <p>
 * Every {@link Context} resource set is bound to a {@link ClassLoader}. If not specified, the default classloader is
 * obtained from {@link ClassUtils#getDefaultClassLoader()}.
 * </p>
 * 
 * Two default context scope are registered by default:
 * <ul>
 * <li>A <em>ClassLoader</em> bound scope named {@link #CLASSLOADER_SCOPE_NAME}, registering and providing resources as
 * <em>singleton</em> instances for the reference ClassLoader. This scope is registered with a low precedence
 * order;</li>
 * <li>A thread-bound scope named {@link #THREAD_SCOPE_NAME}, using current Thread local data set to register and
 * provide context resources. This scope is registered with a high precedence order.</li>
 * </ul>
 * 
 * @since 5.0.0
 */
public interface Context {

	/**
	 * Default thread-bound scope name
	 */
	public static final String THREAD_SCOPE_NAME = "thread";

	/**
	 * Default classloader-bound scope name
	 */
	public static final String CLASSLOADER_SCOPE_NAME = "classloader";

	/**
	 * Lookup a context resource of <code>resourceType</code> class using given <code>resourceKey</code>.
	 * <p>
	 * All registered context scopes are invoked, in the order defined by {@link ContextScope#getOrder()} precedence
	 * value. The first not <code>null</code> resource instance obtained from a scope is the one returned to caller, if
	 * any.
	 * </p>
	 * @param resourceKey Resource key (not null)
	 * @param resourceType Required resource type (not null)
	 * @param classLoader ClassLoader to use. If <code>null</code>, the default ClassLoader is used.
	 * @param <T> Resource type
	 * @return Optional resource instance, empty if not available from context
	 * @throws TypeMismatchException If a context resource with given key was found but it is not of required type
	 */
	<T> Optional<T> resource(String resourceKey, Class<T> resourceType, ClassLoader classLoader);

	/**
	 * Lookup a context resource of <code>resourceType</code> class using given <code>resourceKey</code> and the default
	 * {@link ClassLoader}.
	 * <p>
	 * All registered context scopes are invoked, in the order defined by {@link ContextScope#getOrder()} precedence
	 * value. The first not <code>null</code> resource instance obtained from a scope is the one returned to caller, if
	 * any.
	 * </p>
	 * @param resourceKey Resource key (not null)
	 * @param resourceType Required resource type (not null)
	 * @param <T> Resource type
	 * @return Optional resource instance, empty if not available from context
	 * @throws TypeMismatchException If a context resource with given key was found but it is not of required type
	 */
	default <T> Optional<T> resource(String resourceKey, Class<T> resourceType) {
		return resource(resourceKey, resourceType, null);
	}

	/**
	 * Shortcut method to obtain a {@link Context} resource using the <code>resourceType</code> class name as resource
	 * key.
	 * @param resourceType Resource type (not null)
	 * @param <T> Resource type
	 * @return Optional resource instance, empty if not available from context
	 */
	default <T> Optional<T> resource(Class<T> resourceType) {
		ObjectUtils.argumentNotNull(resourceType, "Context resource type must be not null");
		return resource(resourceType.getName(), resourceType, null);
	}

	/**
	 * Gets a registered {@link ContextScope} with the given <code>name</code> and bound to given
	 * <code>classLoader</code>.
	 * @param name Scope name (not null)
	 * @param classLoader ClassLoader, if <code>null</code>, the default ClassLoader is used
	 * @return The registered {@link ContextScope} with given name, or an empty Optional if a scope with such name is
	 *         not registered in context
	 */
	Optional<ContextScope> scope(String name, ClassLoader classLoader);

	/**
	 * Gets a registered {@link ContextScope} with the given <code>name</code> and bound to default {@link ClassLoader}.
	 * @param name Scope name (not null)
	 * @return The registered {@link ContextScope} with given name, or an empty Optional if a scope with such name is
	 *         not registered in context
	 */
	default Optional<ContextScope> scope(String name) {
		return scope(name, null);
	}

	// Thread scope helper and shortcut methods

	/**
	 * Shortcut method to obtain the thread-bound context scope, i.e. the default {@link ContextScope} named
	 * {@link #THREAD_SCOPE_NAME}.
	 * @param classLoader ClassLoader, if <code>null</code>, the default ClassLoader is used
	 * @return The thread-bound context scope, or an empty Optional if scope is not registered
	 */
	default Optional<ContextScope> threadScope(ClassLoader classLoader) {
		return scope(THREAD_SCOPE_NAME, classLoader);
	}

	/**
	 * Shortcut method to obtain the thread-bound context scope, i.e. the default {@link ContextScope} named
	 * {@link #THREAD_SCOPE_NAME}, using the default {@link ClassLoader}.
	 * @return The thread-bound context scope, or an empty Optional if scope is not registered
	 */
	default Optional<ContextScope> threadScope() {
		return threadScope(null);
	}

	/**
	 * Execute given {@link Runnable} <code>operation</code>, binding given {@link Context} resource key and value to
	 * current Thread, and removing the binding after operation execution.
	 * <p>
	 * The {@link #THREAD_SCOPE_NAME} context scope is used.
	 * </p>
	 * @param resourceKey Context resource key (not null)
	 * @param resource Context resource value to bind to current Thread (not null)
	 * @param operation Operation to execute (not null)
	 * @throws RuntimeException Exception during operation execution
	 */
	default void executeThreadBound(final String resourceKey, final Object resource, final Runnable operation)
			throws RuntimeException {
		ObjectUtils.argumentNotNull(resourceKey, "Resource key must be not null");
		ObjectUtils.argumentNotNull(resource, "Resource value must be not null");
		ObjectUtils.argumentNotNull(operation, "Runnable operation must be not null");
		try {
			threadScope().map((s) -> s.put(resourceKey, resource));
			operation.run();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			threadScope().map((s) -> s.remove(resourceKey));
		}
	}

	/**
	 * Execute given {@link Callable} <code>operation</code>, binding given {@link Context} resource key and value to
	 * current Thread, and removing the binding after operation execution.
	 * <p>
	 * The {@link #THREAD_SCOPE_NAME} context scope is used.
	 * </p>
	 * @param resourceKey Context resource key (not null)
	 * @param resource Context resource value to bind to current Thread (not null)
	 * @param operation Operation to execute
	 * @param <V> Operation result type
	 * @return Operation result
	 * @throws RuntimeException Exception during operation execution
	 */
	default <V> V executeThreadBound(final String resourceKey, final Object resource, final Callable<V> operation)
			throws RuntimeException {
		ObjectUtils.argumentNotNull(resourceKey, "Resource key must be not null");
		ObjectUtils.argumentNotNull(resource, "Resource value must be not null");
		ObjectUtils.argumentNotNull(operation, "Runnable operation must be not null");
		try {
			threadScope().map((s) -> s.put(resourceKey, resource));
			return operation.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			threadScope().map((s) -> s.remove(resourceKey));
		}
	}

	/**
	 * Obtain the default {@link Context} implementation instance.
	 * @return Default {@link Context}
	 */
	static Context get() {
		return DefaultContext.INSTANCE;
	}

}
