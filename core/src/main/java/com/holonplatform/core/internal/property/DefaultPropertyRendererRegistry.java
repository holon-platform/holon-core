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
package com.holonplatform.core.internal.property;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

import jakarta.annotation.Priority;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyRenderer;
import com.holonplatform.core.property.PropertyRendererRegistry;

/**
 * Default {@link PropertyRendererRegistry} implementation.
 *
 * @since 5.0.0
 */
public class DefaultPropertyRendererRegistry implements PropertyRendererRegistry {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = PropertyLogger.create();

	/**
	 * Default registry map by ClassLoader
	 */
	private static final Map<ClassLoader, PropertyRendererRegistry> DEFAULT_INSTANCES = new WeakHashMap<>();

	/**
	 * Return the default {@link PropertyRendererRegistry} using given <code>classLoader</code>.
	 * <p>
	 * The default registry is inited loading {@link PropertyRenderer}s using fully qualified name of its implementation
	 * class name to a <code>com.holonplatform.core.property.PropertyRenderer</code> file in the
	 * <code>META-INF/services</code> directory.
	 * </p>
	 * @param classLoader ClassLoader to use, or <code>null</code> for the default ClassLoader
	 * @return Default PropertyRendererRegistry
	 */
	public static PropertyRendererRegistry getDefault(ClassLoader classLoader) {
		return ensureInited((classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Ensure the default PropertyRendererRegistry instance is inited.
	 * @param classLoader ClassLoader to use
	 * @return Default PropertyRendererRegistry
	 */
	private static synchronized PropertyRendererRegistry ensureInited(final ClassLoader classLoader) {
		if (!DEFAULT_INSTANCES.containsKey(classLoader)) {
			DEFAULT_INSTANCES.put(classLoader, new DefaultPropertyRendererRegistry(true, classLoader));
		}
		return DEFAULT_INSTANCES.get(classLoader);
	}

	@SuppressWarnings("rawtypes")
	private static final Comparator<PropertyRenderer> PRIORITY_COMPARATOR = Comparator.comparingInt(
			p -> p.getClass().isAnnotationPresent(Priority.class) ? p.getClass().getAnnotation(Priority.class).value()
					: PropertyRenderer.DEFAULT_PRIORITY);

	/**
	 * Renderers
	 */
	@SuppressWarnings("rawtypes")
	protected final ConcurrentMap<Class<?>, Map<Predicate, PropertyRenderer>> renderers = new ConcurrentHashMap<>(16,
			0.9f, 1);

	/**
	 * Construct a new PropertyRendererRegistry
	 * @param loadDefaults <code>true</code> to load default {@link PropertyRenderer}s from
	 *        <code>META-INF/services</code> using <code>com.holonplatform.core.property.PropertyRenderer</code> files
	 *        using default ClassLoader. Every default renderer will be registered using an always <code>true</code>
	 *        condition.
	 */
	public DefaultPropertyRendererRegistry(boolean loadDefaults) {
		this(loadDefaults, ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Construct a new PropertyRendererRegistry
	 * @param loadDefaults <code>true</code> to load default {@link PropertyRenderer}s from
	 *        <code>META-INF/services</code> using <code>com.holonplatform.core.property.PropertyRenderer</code> files.
	 *        Every default renderer will be registered using an always <code>true</code> condition.
	 * @param classLoader ClassLoader to use to load default renderer, if <code>loadDefaults</code> is true
	 */
	@SuppressWarnings("unchecked")
	public DefaultPropertyRendererRegistry(boolean loadDefaults, ClassLoader classLoader) {
		super();
		if (loadDefaults) {
			DefaultPropertyRenderers.getDefaultRenderers(classLoader).forEach(pr -> register(p -> true, pr));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyRendererRegistry#register(java.util.function.Predicate,
	 * com.holonplatform.core.property.PropertyRenderer)
	 */
	@Override
	public <R, T> void register(Predicate<Property<? extends T>> condition, PropertyRenderer<R, T> renderer) {
		ObjectUtils.argumentNotNull(condition, "Condition must be not null");
		ObjectUtils.argumentNotNull(renderer, "PropertyRenderer must be not null");
		ObjectUtils.argumentNotNull(renderer.getRenderType(), "PropertyRenderer rendering type must be not null");

		renderers.putIfAbsent(renderer.getRenderType(), new HashMap<>(4));
		renderers.get(renderer.getRenderType()).put(condition, renderer);

		LOGGER.debug(() -> "DefaultPropertyRendererRegistry: registered renderer [" + renderer
				+ "] bound to condition [" + condition + "]");
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyRendererRegistry#getRenderer(java.lang.Class,
	 * com.holonplatform.core.property.Property)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <R, T> Optional<PropertyRenderer<R, T>> getRenderer(Class<R> renderingType, Property<? extends T> property) {
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		ObjectUtils.argumentNotNull(renderingType, "Rendering type must be not null");

		LOGGER.debug(() -> "Get PropertyRenderer for property [" + property + "] and type [" + renderingType + "]");

		final Map<Predicate, PropertyRenderer> renderersForType = renderers.getOrDefault(renderingType,
				Collections.emptyMap());

		final LinkedList<PropertyRenderer> candidates = new LinkedList<>();

		for (Entry<Predicate, PropertyRenderer> entry : renderersForType.entrySet()) {
			if (entry.getKey().test(property)) {
				candidates.add(entry.getValue());
			}
		}

		if (!candidates.isEmpty()) {
			if (candidates.size() > 1) {
				// sort by priority
				candidates.sort(PRIORITY_COMPARATOR);

				LOGGER.debug(() -> "Get PropertyRenderer for property [" + property + "] and type [" + renderingType
						+ "] - return first of candidates: [" + candidates + "]");
			}
			return Optional.of(candidates.getFirst());
		}

		LOGGER.debug(
				() -> "No PropertyRenderer available for property [" + property + "] and type [" + renderingType + "]");

		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultPropertyRendererRegistry [renderers=" + renderers + "]";
	}

}
