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
package com.holonplatform.core.property;

import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Priority;

import com.holonplatform.core.Context;
import com.holonplatform.core.internal.property.DefaultPropertyValuePresenterRegistry;

/**
 * A registry to register {@link PropertyValuePresenter}s bound to a condition and provide a suitable
 * {@link PropertyValuePresenter}.
 * <p>
 * The registry supports {@link PropertyValuePresenter}s priority declaration using {@link Priority} annotation on
 * presenter class (where less priority value means higher priority order), to select and return the most suitable
 * presenter where more than one presenter matches the condition to which is bound at registration time.
 * </p>
 * 
 * @since 5.0.0
 */
public interface PropertyValuePresenterRegistry {

	/**
	 * Default {@link Context} resource key
	 */
	static final String CONTEXT_KEY = PropertyValuePresenterRegistry.class.getName();

	/**
	 * Bind a {@link PropertyValuePresenter} to a property {@link Predicate} <code>condition</code>
	 * @param <T> Property base type
	 * @param condition Condition which has to be satisfied to provide the presenter (not null)
	 * @param presenter PropertyPresenter to register (not null)
	 */
	<T> void register(Predicate<Property<? extends T>> condition, PropertyValuePresenter<? super T> presenter);

	/**
	 * Gets the {@link PropertyValuePresenter} to use with given <code>property</code> according to registered
	 * presenters.
	 * @param <T> Property base type
	 * @param property Property to present
	 * @return PropertyValuePresenter, or an empty Optional if no presenter is available for given property
	 */
	<T> Optional<PropertyValuePresenter<T>> getPresenter(Property<T> property);

	// Builder

	/**
	 * Create a default instance of {@link PropertyValuePresenterRegistry}.
	 * @param loadDefaults <code>true</code> to load default {@link PropertyValuePresenter}s from
	 *        <code>META-INF/services</code> using <code>com.holonplatform.core.property.PropertyValuePresenter</code>
	 *        files using default ClassLoader. Every default presenter will be registered using an always
	 *        <code>true</code> condition.
	 * @return PropertyValuePresenterRegistry instance
	 */
	static PropertyValuePresenterRegistry create(boolean loadDefaults) {
		return new DefaultPropertyValuePresenterRegistry(loadDefaults);
	}

	// Accessor

	/**
	 * Gets the current {@link PropertyValuePresenterRegistry} instance.
	 * @return The {@link Context}-bound PropertyValuePresenterRegistry instance, if available using
	 *         {@link #CONTEXT_KEY} as context key, or the default instance for the default ClassLoader obtained through
	 *         {@link #getDefault()}.
	 */
	static PropertyValuePresenterRegistry get() {
		return Context.get().resource(CONTEXT_KEY, PropertyValuePresenterRegistry.class).orElse(getDefault());
	}

	// Defaults

	/**
	 * Return the default {@link PropertyValuePresenterRegistry} using given <code>classLoader</code>.
	 * <p>
	 * The default registry is inited loading {@link PropertyValuePresenter}s using fully qualified name of its
	 * implementation class name to a <code>com.holonplatform.core.property.PropertyValuePresenter</code> file in the
	 * <code>META-INF/services</code> directory.
	 * </p>
	 * @param classLoader ClassLoader to use
	 * @return Default PropertyValuePresenterRegistry
	 */
	static PropertyValuePresenterRegistry getDefault(ClassLoader classLoader) {
		return DefaultPropertyValuePresenterRegistry.getDefault(classLoader);
	}

	/**
	 * Return the default {@link PropertyValuePresenterRegistry} using default {@link ClassLoader}.
	 * <p>
	 * The default registry is inited loading {@link PropertyValuePresenter}s using fully qualified name of its
	 * implementation class name to a <code>com.holonplatform.core.property.PropertyValuePresenter</code> file in the
	 * <code>META-INF/services</code> directory.
	 * </p>
	 * @return Default PropertyValuePresenterRegistry
	 */
	static PropertyValuePresenterRegistry getDefault() {
		return DefaultPropertyValuePresenterRegistry.getDefault(null);
	}

}
