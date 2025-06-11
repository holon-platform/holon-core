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

import java.util.Comparator;
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
import com.holonplatform.core.property.PropertyValuePresenter;
import com.holonplatform.core.property.PropertyValuePresenterRegistry;

/**
 * Default {@link PropertyValuePresenterRegistry} implementation.
 *
 * @since 5.0.0
 */
public class DefaultPropertyValuePresenterRegistry implements PropertyValuePresenterRegistry {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = PropertyLogger.create();

	/**
	 * Default registry map by ClassLoader
	 */
	private static final Map<ClassLoader, PropertyValuePresenterRegistry> DEFAULT_INSTANCES = new WeakHashMap<>();

	/**
	 * Return the default {@link PropertyValuePresenterRegistry} using given <code>classLoader</code>.
	 * <p>
	 * The default registry is inited loading {@link PropertyValuePresenter}s using fully qualified name of its
	 * implementation class name to a <code>com.holonplatform.core.property.PropertyValuePresenter</code> file in the
	 * <code>META-INF/services</code> directory.
	 * </p>
	 * @param classLoader ClassLoader to use, or <code>null</code> for the default ClassLoader
	 * @return Default PropertyValuePresenterRegistry
	 */
	public static PropertyValuePresenterRegistry getDefault(ClassLoader classLoader) {
		return ensureInited((classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Ensure the default PropertyValuePresenterRegistry instance is inited.
	 * @param classLoader ClassLoader to use
	 * @return Default PropertyValuePresenterRegistry
	 */
	private static synchronized PropertyValuePresenterRegistry ensureInited(final ClassLoader classLoader) {
		if (!DEFAULT_INSTANCES.containsKey(classLoader)) {
			DEFAULT_INSTANCES.put(classLoader, new DefaultPropertyValuePresenterRegistry(true, classLoader));
		}
		return DEFAULT_INSTANCES.get(classLoader);
	}

	@SuppressWarnings("rawtypes")
	private static final Comparator<PropertyValuePresenter> PRIORITY_COMPARATOR = Comparator.comparingInt(
			p -> p.getClass().isAnnotationPresent(Priority.class) ? p.getClass().getAnnotation(Priority.class).value()
					: PropertyValuePresenter.DEFAULT_PRIORITY);

	/**
	 * Presenters
	 */
	@SuppressWarnings("rawtypes")
	protected final ConcurrentMap<Predicate, PropertyValuePresenter> presenters = new ConcurrentHashMap<>(8, 0.9f, 1);

	/**
	 * Construct a new PropertyValuePresenterRegistry
	 * @param loadDefaults <code>true</code> to load default {@link PropertyValuePresenter}s from
	 *        <code>META-INF/services</code> using <code>com.holonplatform.core.property.PropertyValuePresenter</code>
	 *        files using default ClassLoader. Every default presenter will be registered using an always
	 *        <code>true</code> condition.
	 */
	public DefaultPropertyValuePresenterRegistry(boolean loadDefaults) {
		this(loadDefaults, ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Construct a new PropertyValuePresenterRegistry
	 * @param loadDefaults <code>true</code> to load default {@link PropertyValuePresenter}s from
	 *        <code>META-INF/services</code> using <code>com.holonplatform.core.property.PropertyValuePresenter</code>
	 *        files. Every default presenter will be registered using an always <code>true</code> condition.
	 * @param classLoader ClassLoader to use to load default presenters, if <code>loadDefaults</code> is true
	 */
	@SuppressWarnings("unchecked")
	public DefaultPropertyValuePresenterRegistry(boolean loadDefaults, ClassLoader classLoader) {
		super();
		if (loadDefaults) {
			DefaultPropertyPresenters.getDefaultPresenters(classLoader).forEach(pr -> register(p -> true, pr));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValuePresenterRegistry#register(java.util.function.Predicate,
	 * com.holonplatform.core.property.PropertyValuePresenter)
	 */
	@Override
	public <T> void register(Predicate<Property<? extends T>> condition, PropertyValuePresenter<? super T> presenter) {
		ObjectUtils.argumentNotNull(condition, "Condition Predicate must be not null");
		ObjectUtils.argumentNotNull(presenter, "PropertyValuePresenter must be not null");

		PropertyValuePresenter<?> rp = presenters.putIfAbsent(condition, presenter);

		if (rp == null) {
			LOGGER.debug(() -> "DefaultPropertyValuePresenterRegistry: registered presenter [" + presenter
					+ "] bound to condition [" + condition + "]");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValuePresenterRegistry#getPresenter(com.holonplatform.core.property.
	 * Property)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> Optional<PropertyValuePresenter<T>> getPresenter(Property<T> property) {
		ObjectUtils.argumentNotNull(property, "Property must be not null");

		LOGGER.debug(() -> "Get PropertyValuePresenter for property [" + property + "]");

		final LinkedList<PropertyValuePresenter> candidates = new LinkedList<>();

		for (Entry<Predicate, PropertyValuePresenter> entry : presenters.entrySet()) {
			if (entry.getKey().test(property)) {
				candidates.add(entry.getValue());
			}
		}

		if (!candidates.isEmpty()) {
			if (candidates.size() > 1) {
				// sort by priority
				candidates.sort(PRIORITY_COMPARATOR);

				LOGGER.debug(() -> "Get PropertyValuePresenter for property [" + property
						+ "] - return first of candidates: [" + candidates + "]");
			}
			return Optional.of(candidates.getFirst());
		}

		LOGGER.debug(() -> "No PropertyValuePresenter available for property [" + property + "]");

		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultPropertyValuePresenterRegistry [presenters=" + presenters + "]";
	}

}
