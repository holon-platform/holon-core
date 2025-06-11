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
package com.holonplatform.core.property;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import jakarta.annotation.Priority;

import com.holonplatform.core.Context;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.internal.property.DefaultPropertyRendererRegistry;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * A registry to register {@link PropertyRenderer}s bound to a condition and provide a suitable {@link PropertyRenderer}
 * for a given {@link Property} and rendering type.
 * <p>
 * The registry supports {@link PropertyRenderer}s priority declaration using {@link Priority} annotation on renderer
 * class (where less priority value means higher priority order), to select and return the most suitable renderer where
 * more than one renderer matches the condition to which is bound at registration time.
 * </p>
 * 
 * @since 5.0.0
 */
public interface PropertyRendererRegistry {

	/**
	 * Default {@link Context} resource key
	 */
	static final String CONTEXT_KEY = PropertyRendererRegistry.class.getName();

	/**
	 * Bind a {@link PropertyRenderer} to a property {@link Predicate} <code>condition</code>.
	 * @param <R> Rendering type
	 * @param <T> Property base type
	 * @param condition The condition which has to be satisfied to provide the renderer (not null)
	 * @param renderer The PropertyRenderer to register (not null)
	 */
	<R, T> void register(Predicate<Property<? extends T>> condition, PropertyRenderer<R, T> renderer);

	/**
	 * Bind a {@link PropertyRenderer} to the given property. The renderer will be provided when the property to render
	 * is the same as the given property.
	 * @param <R> Rendering type
	 * @param <T> Property base type
	 * @param property The property to render (not null)
	 * @param renderer The PropertyRenderer to register (not null)
	 */
	default <R, T> void forProperty(Property<? extends T> property, PropertyRenderer<R, T> renderer) {
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		register(p -> property.equals(p), renderer);
	}

	/**
	 * Bind a {@link PropertyRenderer} to the given property configuration value. The renderer will be provided when the
	 * property has the given <code>configurationProperty</code> and its value equals to given <code>value</code>.
	 * @param <R> Rendering type
	 * @param <T> Property base type
	 * @param <C> Configuration property type
	 * @param configurationProperty The configuration property to check (not null)
	 * @param value The configuration property value to check (may be null)
	 * @param renderer The PropertyRenderer to register (not null)
	 */
	default <R, T, C> void forPropertyConfiguration(ConfigProperty<C> configurationProperty, C value,
			PropertyRenderer<R, T> renderer) {
		ObjectUtils.argumentNotNull(configurationProperty, "Configuration property must be not null");
		register(p -> p.getConfiguration().getParameter(configurationProperty).map(v -> Objects.equals(v, value))
				.orElse(Boolean.FALSE), renderer);
	}

	/**
	 * Gets the {@link PropertyRenderer} to use with given <code>property</code> according to registered renderers for
	 * given <code>renderingType</code>.
	 * @param <R> Rendering type
	 * @param <T> Property base type
	 * @param renderingType Required rendering type (not null)
	 * @param property Property to render (not null)
	 * @return The {@link PropertyRenderer} to render given property as given rendering type, or an empty Optional if a
	 *         suitable renderer is not available
	 */
	<R, T> Optional<PropertyRenderer<R, T>> getRenderer(Class<R> renderingType, Property<? extends T> property);

	// Builder

	/**
	 * Create a default instance of {@link PropertyRendererRegistry}.
	 * @param loadDefaults <code>true</code> to load default {@link PropertyRenderer}s from
	 *        <code>META-INF/services</code> using <code>com.holonplatform.core.property.PropertyRenderer</code> files
	 *        using default ClassLoader. Every default renderer will be registered using an always <code>true</code>
	 *        condition.
	 * @return PropertyRendererRegistry instance
	 */
	static PropertyRendererRegistry create(boolean loadDefaults) {
		return new DefaultPropertyRendererRegistry(loadDefaults);
	}

	// Accessor

	/**
	 * Gets the current {@link PropertyRendererRegistry} instance.
	 * @return The {@link Context}-bound PropertyRendererRegistry instance, if available using {@link #CONTEXT_KEY} as
	 *         context key, or the default instance for the default ClassLoader obtained through {@link #getDefault()}.
	 */
	static PropertyRendererRegistry get() {
		return Context.get().resource(CONTEXT_KEY, PropertyRendererRegistry.class).orElse(getDefault());
	}

	// Defaults

	/**
	 * Return the default {@link PropertyRendererRegistry} using given <code>classLoader</code>.
	 * <p>
	 * The default registry is inited loading {@link PropertyRenderer}s using fully qualified name of its implementation
	 * class name to a <code>com.holonplatform.core.property.PropertyRenderer</code> file in the
	 * <code>META-INF/services</code> directory.
	 * </p>
	 * @param classLoader ClassLoader to use
	 * @return Default PropertyRendererRegistry
	 */
	static PropertyRendererRegistry getDefault(ClassLoader classLoader) {
		return DefaultPropertyRendererRegistry.getDefault(classLoader);
	}

	/**
	 * Return the default {@link PropertyRendererRegistry} using default {@link ClassLoader}.
	 * <p>
	 * The default registry is inited loading {@link PropertyRenderer}s using fully qualified name of its implementation
	 * class name to a <code>com.holonplatform.core.property.PropertyRenderer</code> file in the
	 * <code>META-INF/services</code> directory.
	 * </p>
	 * @return Default PropertyRendererRegistry
	 */
	static PropertyRendererRegistry getDefault() {
		return DefaultPropertyRendererRegistry.getDefault(null);
	}

	/**
	 * Exception thrown a suitable {@link PropertyRenderer} is not available for a given {@link Property} and rendering
	 * type
	 */
	@SuppressWarnings("serial")
	public static class NoSuitableRendererAvailableException extends RuntimeException {

		/**
		 * Default constructor
		 */
		public NoSuitableRendererAvailableException() {
			super();
		}

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public NoSuitableRendererAvailableException(String message) {
			super(message);
		}

	}

}
