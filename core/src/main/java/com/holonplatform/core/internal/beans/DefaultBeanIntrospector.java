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
package com.holonplatform.core.internal.beans;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

import javax.annotation.Priority;

import com.holonplatform.core.Path;
import com.holonplatform.core.beans.BeanConfigProperties;
import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanProperty;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.beans.Ignore;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;

/**
 * Default {@link BeanIntrospector} implementation.
 * 
 * <p>
 * Cache is supported and active by default. Use {@link BeanConfigProperties#BEAN_INTROSPECTOR_CACHE_ENABLED} to disable
 * cache in the case of memory consumption issues.
 * </p>
 * 
 * @since 5.0.0
 */
public class DefaultBeanIntrospector implements BeanIntrospector {

	/**
	 * Default introspectors map by ClassLoader
	 */
	private static final Map<ClassLoader, BeanIntrospector> DEFAULT_INSTANCES = new WeakHashMap<>();

	/**
	 * Return the default {@link BeanIntrospector} using given <code>classLoader</code>.
	 * <p>
	 * The default introspector is inited loading {@link BeanIntrospector}s using fully qualified name of its
	 * implementation class name to a <code>com.holonplatform.core.beans.BeanIntrospector</code> file in the
	 * <code>META-INF/services</code> directory.
	 * </p>
	 * @param classLoader ClassLoader to use, or <code>null</code> for the default ClassLoader
	 * @return Default BeanIntrospector
	 */
	public static BeanIntrospector getDefault(ClassLoader classLoader) {
		return ensureInited((classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Ensure the default PropertyValuePresenterRegistry instance is inited.
	 * @param classLoader ClassLoader to use
	 * @return Default PropertyValuePresenterRegistry
	 */
	private static synchronized BeanIntrospector ensureInited(final ClassLoader classLoader) {
		if (!DEFAULT_INSTANCES.containsKey(classLoader)) {
			DEFAULT_INSTANCES.put(classLoader, new DefaultBeanIntrospector(classLoader));
		}
		return DEFAULT_INSTANCES.get(classLoader);
	}

	/**
	 * Logger
	 */
	private static final Logger LOGGER = BeanLogger.create();

	private static final Comparator<BeanPropertyPostProcessor> PRIORITY_COMPARATOR = Comparator
			.comparingInt(p -> p.getClass().isAnnotationPresent(Priority.class)
					? p.getClass().getAnnotation(Priority.class).value() : BeanPropertyPostProcessor.DEFAULT_PRIORITY);

	private static final Comparator<BeanProperty<?>> SEQUENCE_COMPARATOR = new Comparator<BeanProperty<?>>() {

		@Override
		public int compare(BeanProperty<?> o1, BeanProperty<?> o2) {
			if (o1 != null && o2 != null) {
				return o1.getSequence().orElse(Integer.MAX_VALUE).compareTo(o2.getSequence().orElse(Integer.MAX_VALUE));
			}
			return 0;
		}
	};

	/**
	 * Default property names to exclude during bean introspection
	 */
	static final Collection<String> EXCLUDE_DEFAULT_BEAN_PROPERTY_NAMES;

	static {
		EXCLUDE_DEFAULT_BEAN_PROPERTY_NAMES = new ArrayList<>(3);
		EXCLUDE_DEFAULT_BEAN_PROPERTY_NAMES.add("class");
		EXCLUDE_DEFAULT_BEAN_PROPERTY_NAMES.add("classLoader");
		EXCLUDE_DEFAULT_BEAN_PROPERTY_NAMES.add("protectionDomain");
	}

	/**
	 * Cache enabled
	 */
	private static boolean CACHE_ENABLED = true;

	static {
		try {
			CACHE_ENABLED = BeanConfigProperties.builder().withDefaultPropertySources().build()
					.isBeanIntrospectorCacheEnabled();
			LOGGER.debug(() -> "Cache enabled: " + CACHE_ENABLED);
		} catch (Exception e) {
			CACHE_ENABLED = true;
			LOGGER.warn("Failed to read bean introspection cache enabled configuration property", e);
		}
	}

	/**
	 * Post processors
	 */
	private final List<BeanPropertyPostProcessor> propertyPostProcessors = new LinkedList<>();

	/**
	 * Constructor
	 * @param classLoader ClassLoader to use
	 */
	public DefaultBeanIntrospector(ClassLoader classLoader) {
		super();
		ObjectUtils.argumentNotNull(classLoader, "ClassLoader must be not null");
		init(classLoader);
	}

	/**
	 * Load {@link BeanPropertyPostProcessor}s from <code>META-INF/services</code> extensions.
	 * @param classLoader ClassLoader to use
	 * @return {@link BeanPropertyPostProcessor}s list, empty if none found
	 */
	private void init(final ClassLoader classLoader) {
		LOGGER.debug(() -> "Load BeanPropertyPostProcessors for classloader [" + classLoader
				+ "] using ServiceLoader with service name: " + BeanPropertyPostProcessor.class.getName());
		Iterable<BeanPropertyPostProcessor> postProcessors = AccessController
				.doPrivileged(new PrivilegedAction<Iterable<BeanPropertyPostProcessor>>() {
					@Override
					public Iterable<BeanPropertyPostProcessor> run() {
						return ServiceLoader.load(BeanPropertyPostProcessor.class, classLoader);
					}
				});
		postProcessors.forEach(pr -> {
			propertyPostProcessors.add(pr);
			LOGGER.debug(() -> "Loaded and registered BeanPropertyPostProcessor [" + pr + "] for classloader ["
					+ classLoader + "]");
		});
		// sort by priority
		Collections.sort(propertyPostProcessors, PRIORITY_COMPARATOR);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanIntrospector#addBeanPropertyPostProcessor(com.holonplatform.core.beans.
	 * BeanPropertyPostProcessor)
	 */
	@Override
	public void addBeanPropertyPostProcessor(BeanPropertyPostProcessor beanPropertyPostProcessor) {
		ObjectUtils.argumentNotNull(beanPropertyPostProcessor, "BeanPropertyPostProcessor must be not null");
		propertyPostProcessors.add(beanPropertyPostProcessor);
		LOGGER.debug(() -> "Registered BeanPropertyPostProcessor [" + beanPropertyPostProcessor + "]");
		// sort by priority
		Collections.sort(propertyPostProcessors, PRIORITY_COMPARATOR);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanIntrospector#removeBeanPropertyPostProcessor(com.holonplatform.core.beans.
	 * BeanPropertyPostProcessor)
	 */
	@Override
	public void removeBeanPropertyPostProcessor(BeanPropertyPostProcessor beanPropertyPostProcessor) {
		ObjectUtils.argumentNotNull(beanPropertyPostProcessor, "BeanPropertyPostProcessor must be not null");
		propertyPostProcessors.remove(beanPropertyPostProcessor);
		LOGGER.debug(() -> "Unregistered BeanPropertyPostProcessor [" + beanPropertyPostProcessor + "]");
	}

	/**
	 * Post-process given {@link BeanProperty} using registered {@link BeanPropertyPostProcessor}s.
	 * @param property Property to process
	 * @param beanOrNestedClass Bean or nested class instance
	 * @return Processed property
	 */
	private BeanProperty.Builder<?> postProcessBeanProperty(BeanProperty.Builder<?> property,
			Class<?> beanOrNestedClass) {
		BeanProperty.Builder<?> processed = property;
		for (BeanPropertyPostProcessor propertyPostProcessor : propertyPostProcessors) {
			LOGGER.debug(() -> "Invoke BeanPropertyPostProcessor [" + propertyPostProcessor + "] on property ["
					+ property + "]");
			processed = propertyPostProcessor.processBeanProperty(processed, beanOrNestedClass);
			if (processed == null) {
				throw new BeanIntrospectionException(
						"BeanPropertyPostProcessor [" + propertyPostProcessor + "] returned a null property");
			}
		}
		return processed;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanIntrospector#clearCache()
	 */
	@Override
	public boolean clearCache() {
		LOGGER.debug(() -> "Clear cache");
		synchronized (cache) {
			cache.clear();
		}
		return true;
	}

	/**
	 * Get the current cache size
	 * @return Cache size
	 */
	public int getCacheSize() {
		return cache.size();
	}

	/**
	 * Cache: bean class <-> BeanPropertyContext
	 */
	@SuppressWarnings("rawtypes")
	private final Map<Class<?>, BeanPropertySet> cache = new WeakHashMap<>(16, 0.9f);

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanIntrospector#getPropertySet(java.lang.Class, com.holonplatform.core.Path)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> BeanPropertySet<T> getPropertySet(Class<? extends T> beanClass, Path<?> parentPath) {
		ObjectUtils.argumentNotNull(beanClass, "Bean class must be not null");
		LOGGER.debug(() -> "Get BeanPropertySet for bean class [" + beanClass + "]");
		synchronized (cache) {
			if (CACHE_ENABLED && cache.containsKey(beanClass)) {
				return cache.get(beanClass);
			}
			BeanPropertySet beanPropertySet = buildBeanPropertySet(beanClass, parentPath);
			if (CACHE_ENABLED) {
				BeanPropertySet existing = cache.putIfAbsent(beanClass, beanPropertySet);
				return (existing != null ? existing : beanPropertySet);
			}
			return beanPropertySet;
		}
	}

	/**
	 * Build a new {@link BeanPropertySet} for given bean class
	 * @param beanClass Bean class for which to build the BeanPropertySet
	 * @param parentPath Optional parent path to set as bean root properties parent path
	 * @return BeanPropertySet instance
	 * @throws BeanIntrospectionException Error during bean introspection
	 */
	private <T> BeanPropertySet<T> buildBeanPropertySet(Class<? extends T> beanClass, Path<?> parentPath)
			throws BeanIntrospectionException {
		LOGGER.debug(() -> "Build BeanPropertySet for bean class [" + beanClass + "]");
		List<BeanProperty<?>> properties = resolveBeanProperties(beanClass, parentPath, null);
		// sort
		properties.sort(SEQUENCE_COMPARATOR);
		return new DefaultBeanPropertySet<>(beanClass, properties);
	}

	/**
	 * Resolve bean properties inspecting given bean class and invoking
	 * {@link #buildBeanProperty(Class, BeanProperty, PropertyDescriptor)} for each valid detected property.
	 * @param beanClass Bean property to inspect
	 * @param parentPath Optional parent path
	 * @param parent Parent bean property if bean class is a nested bean class
	 * @return List of {@link BeanProperty}s
	 * @throws BeanIntrospectionException Error introspecting bean class
	 */
	private List<BeanProperty<?>> resolveBeanProperties(Class<?> beanClass, Path<?> parentPath, BeanProperty<?> parent)
			throws BeanIntrospectionException {
		final List<BeanProperty<?>> properties = new LinkedList<>();
		if (isIntrospectable(beanClass)) {
			// get bean info
			try {
				PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
				if (propertyDescriptors != null) {
					for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
						if (!EXCLUDE_DEFAULT_BEAN_PROPERTY_NAMES.contains(propertyDescriptor.getName())) {
							buildBeanProperty(beanClass, parentPath, parent, propertyDescriptor).ifPresent(p -> {
								properties.add(p);
								LOGGER.debug(() -> "Bean class [" + beanClass + "] - added bean property [" + p + "]");
								// check nested
								properties.addAll(resolveBeanProperties(p.getType(), null, p));
							});
						}
					}
				}
			} catch (IntrospectionException e) {
				throw new BeanIntrospectionException("Failed to introspect bean class [" + beanClass.getName() + "]",
						e);
			}
		}
		return properties;
	}

	/**
	 * Build a {@link BeanProperty} instance using given {@link PropertyDescriptor}.
	 * @param beanClass Bean class
	 * @param parentPath Optional parent path
	 * @param parent Parent bean property if bean class is a nested bean class
	 * @param propertyDescriptor Bean property descriptor
	 * @return BeanProperty instance
	 * @throws BeanIntrospectionException Error introspecting bean class
	 */
	private Optional<BeanProperty<?>> buildBeanProperty(Class<?> beanClass, Path<?> parentPath, BeanProperty<?> parent,
			PropertyDescriptor propertyDescriptor) throws BeanIntrospectionException {
		// annotations
		Annotation[] annotations = null;
		Field propertyField = findDeclaredField(beanClass, propertyDescriptor.getName());
		if (propertyField != null) {
			// check ignore
			if (propertyField.isAnnotationPresent(Ignore.class)) {
				LOGGER.debug(() -> "Bean class [" + beanClass + "] - Property " + propertyDescriptor.getName()
						+ " ignored according to IgnoreProperty annotation ");
				return Optional.empty();
			}

			annotations = propertyField.getAnnotations();
		}

		BeanProperty.Builder<?> property = BeanProperty
				.builder(propertyDescriptor.getName(), propertyDescriptor.getPropertyType()).parent(parent)
				.readMethod(propertyDescriptor.getReadMethod()).writeMethod(propertyDescriptor.getWriteMethod())
				.field(propertyField).annotations(annotations);

		if (parent == null && parentPath != null) {
			property.parent(parentPath);
		}

		// post processors
		property = postProcessBeanProperty(property, beanClass);

		return Optional.of(property);

	}

	/**
	 * Find a declared field on given class
	 * @param cls Class to introspect
	 * @param fieldName Field name to find
	 * @return Matching {@link Field} instance, or <code>null</code> if not found
	 */
	private static Field findDeclaredField(Class<?> cls, String fieldName) {
		if (cls != null && fieldName != null) {
			Field fld = null;
			try {
				fld = cls.getDeclaredField(fieldName);
			} catch (@SuppressWarnings("unused") NoSuchFieldException e) {
				// ignore
			} catch (Exception e) {
				// ignore
				LOGGER.debug(() -> "Field " + fieldName + " on class: " + cls
						+ " will be ignored because of access exception ", e);
			}

			if (fld != null) {
				return fld;
			}

			// try in superclasses
			if (cls.getSuperclass() != null) {
				return findDeclaredField(cls.getSuperclass(), fieldName);
			}
		}
		return null;
	}

	/*
	 * Check a a property type should be considered a potentially nested bean property container.
	 */
	private static boolean isIntrospectable(Class<?> propertyClass) {
		return propertyClass != Object.class && !propertyClass.isArray() && !propertyClass.isPrimitive()
				&& !propertyClass.isEnum() && !TypeUtils.isString(propertyClass) && !TypeUtils.isNumber(propertyClass)
				&& !TypeUtils.isTemporalOrCalendar(propertyClass) && !TypeUtils.isBoolean(propertyClass)
				&& !Collection.class.isAssignableFrom(propertyClass);
	}

}
