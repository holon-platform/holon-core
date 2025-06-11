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

import jakarta.annotation.Priority;

import com.holonplatform.core.Path;
import com.holonplatform.core.Path.FinalPath;
import com.holonplatform.core.Path.FinalPath.FinalPathBuilder;
import com.holonplatform.core.beans.BeanConfigProperties;
import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanProperty;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.beans.BeanPropertySetPostProcessor;
import com.holonplatform.core.beans.BooleanBeanProperty;
import com.holonplatform.core.beans.Ignore;
import com.holonplatform.core.beans.IgnoreMode;
import com.holonplatform.core.beans.NumericBeanProperty;
import com.holonplatform.core.beans.StringBeanProperty;
import com.holonplatform.core.beans.TemporalBeanProperty;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;
import com.holonplatform.core.property.PropertyBox;

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

	private static final Comparator<BeanPropertySetPostProcessor> PROPERTY_SET_POST_PROCESSOR_PRIORITY_COMPARATOR = Comparator
			.comparingInt(p -> p.getClass().isAnnotationPresent(Priority.class)
					? p.getClass().getAnnotation(Priority.class).value()
					: BeanPropertySetPostProcessor.DEFAULT_PRIORITY);

	private static final Comparator<BeanPropertyPostProcessor> PROPERTY_POST_PROCESSOR_PRIORITY_COMPARATOR = Comparator
			.comparingInt(p -> p.getClass().isAnnotationPresent(Priority.class)
					? p.getClass().getAnnotation(Priority.class).value()
					: BeanPropertyPostProcessor.DEFAULT_PRIORITY);

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
	 * Property set post processors
	 */
	private final List<BeanPropertySetPostProcessor> propertySetPostProcessors = new LinkedList<>();

	/**
	 * Property post processors
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
	 * Load {@link BeanPropertySetPostProcessor}s and {@link BeanPropertyPostProcessor}s from
	 * <code>META-INF/services</code> extensions.
	 * @param classLoader ClassLoader to use
	 */
	private void init(final ClassLoader classLoader) {
		// property set post processors
		LOGGER.debug(() -> "Load BeanPropertySetPostProcessor for classloader [" + classLoader
				+ "] using ServiceLoader with service name: " + BeanPropertySetPostProcessor.class.getName());
		Iterable<BeanPropertySetPostProcessor> propertySetPostProcessors = AccessController
				.doPrivileged(new PrivilegedAction<Iterable<BeanPropertySetPostProcessor>>() {
					@Override
					public Iterable<BeanPropertySetPostProcessor> run() {
						return ServiceLoader.load(BeanPropertySetPostProcessor.class, classLoader);
					}
				});
		propertySetPostProcessors.forEach(pr -> {
			this.propertySetPostProcessors.add(pr);
			LOGGER.debug(() -> "Loaded and registered BeanPropertySetPostProcessor [" + pr + "] for classloader ["
					+ classLoader + "]");
		});
		// sort by priority
		Collections.sort(this.propertySetPostProcessors, PROPERTY_SET_POST_PROCESSOR_PRIORITY_COMPARATOR);
		// property post processors
		LOGGER.debug(() -> "Load BeanPropertyPostProcessors for classloader [" + classLoader
				+ "] using ServiceLoader with service name: " + BeanPropertyPostProcessor.class.getName());
		Iterable<BeanPropertyPostProcessor> propertyPostProcessors = AccessController
				.doPrivileged(new PrivilegedAction<Iterable<BeanPropertyPostProcessor>>() {
					@Override
					public Iterable<BeanPropertyPostProcessor> run() {
						return ServiceLoader.load(BeanPropertyPostProcessor.class, classLoader);
					}
				});
		propertyPostProcessors.forEach(pr -> {
			this.propertyPostProcessors.add(pr);
			LOGGER.debug(() -> "Loaded and registered BeanPropertyPostProcessor [" + pr + "] for classloader ["
					+ classLoader + "]");
		});
		// sort by priority
		Collections.sort(this.propertyPostProcessors, PROPERTY_POST_PROCESSOR_PRIORITY_COMPARATOR);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanIntrospector#addBeanPropertySetPostProcessor(com.holonplatform.core.beans.
	 * BeanPropertySetPostProcessor)
	 */
	@Override
	public void addBeanPropertySetPostProcessor(BeanPropertySetPostProcessor beanPropertySetPostProcessor) {
		ObjectUtils.argumentNotNull(propertySetPostProcessors, "BeanPropertySetPostProcessor must be not null");
		propertySetPostProcessors.add(beanPropertySetPostProcessor);
		LOGGER.debug(() -> "Registered BeanPropertySetPostProcessor [" + beanPropertySetPostProcessor + "]");
		// sort by priority
		Collections.sort(propertySetPostProcessors, PROPERTY_SET_POST_PROCESSOR_PRIORITY_COMPARATOR);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.beans.BeanIntrospector#removeBeanPropertySetPostProcessor(com.holonplatform.core.beans.
	 * BeanPropertySetPostProcessor)
	 */
	@Override
	public void removeBeanPropertySetPostProcessor(BeanPropertySetPostProcessor beanPropertySetPostProcessor) {
		ObjectUtils.argumentNotNull(beanPropertySetPostProcessor, "BeanPropertySetPostProcessor must be not null");
		propertySetPostProcessors.remove(beanPropertySetPostProcessor);
		LOGGER.debug(() -> "Unregistered BeanPropertySetPostProcessor [" + beanPropertySetPostProcessor + "]");
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
		Collections.sort(propertyPostProcessors, PROPERTY_POST_PROCESSOR_PRIORITY_COMPARATOR);
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
	 * Post-process given {@link BeanPropertySet} using registered {@link BeanPropertySetPostProcessor}s.
	 * @param <T> Bean type
	 * @param propertySet Property set to process
	 * @param beanClass Bean class
	 * @return Processed property set
	 */
	private <T> void postProcessBeanPropertySet(BeanPropertySet.Builder<T, DefaultBeanPropertySet<T>> propertySet,
			Class<?> beanClass) {
		BeanPropertySet.Builder<?, ?> processed = propertySet;
		for (BeanPropertySetPostProcessor propertySetPostProcessor : propertySetPostProcessors) {
			LOGGER.debug(() -> "Invoke BeanPropertySetPostProcessor [" + propertySetPostProcessor
					+ "] on property set of bean class [" + beanClass + "]");
			propertySetPostProcessor.processBeanPropertySet(processed, beanClass);

		}
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
	 * @see com.holonplatform.core.beans.BeanIntrospector#read(com.holonplatform.core.property.PropertyBox,
	 * java.lang.Object, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> PropertyBox read(PropertyBox propertyBox, T instance, boolean ignoreMissing) {
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");
		return getPropertySet((Class<T>) instance.getClass()).read(propertyBox, instance, ignoreMissing);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanIntrospector#read(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> PropertyBox read(T instance) {
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");
		BeanPropertySet<T> propertySet = getPropertySet((Class<T>) instance.getClass());
		return propertySet.read(PropertyBox.builder(propertySet).invalidAllowed(true).build(), instance);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanIntrospector#write(com.holonplatform.core.property.PropertyBox,
	 * java.lang.Object, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T write(PropertyBox propertyBox, T instance, boolean ignoreMissing) {
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");
		return getPropertySet((Class<T>) instance.getClass()).write(propertyBox, instance, ignoreMissing);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanIntrospector#getPropertySet(java.lang.Class, com.holonplatform.core.Path)
	 */
	@Override
	public <T> BeanPropertySet<T> getPropertySet(Class<? extends T> beanClass, Path<?> parentPath) {
		return getPropertySet(beanClass);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanIntrospector#getPropertySet(java.lang.Class)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> BeanPropertySet<T> getPropertySet(Class<? extends T> beanClass) {
		ObjectUtils.argumentNotNull(beanClass, "Bean class must be not null");
		LOGGER.debug(() -> "Get BeanPropertySet for bean class [" + beanClass + "]");
		synchronized (cache) {
			if (CACHE_ENABLED && cache.containsKey(beanClass)) {
				return cache.get(beanClass);
			}

			// get bean path
			final FinalPathBuilder<T> rootBeanPath = FinalPath.of(beanClass.getName(), beanClass);

			final BeanPropertySet beanPropertySet = buildBeanPropertySet(beanClass, rootBeanPath);

			// check data path
			((BeanPropertySet<?>) beanPropertySet).getDataPath().ifPresent(dp -> rootBeanPath.dataPath(dp));

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
		// resolve bean properties
		List<BeanProperty<?>> properties = resolveBeanProperties(beanClass, parentPath, null);
		// sort
		properties.sort(SEQUENCE_COMPARATOR);
		// property set
		BeanPropertySet.Builder<T, DefaultBeanPropertySet<T>> builder = new DefaultBeanPropertySet.DefaultBuilder<>(
				beanClass, properties);
		// post processing
		postProcessBeanPropertySet(builder, beanClass);
		// build property set
		final DefaultBeanPropertySet<T> beanPropertySet = builder.build();
		// check identifiers
		properties.stream().filter(p -> p.isIdentifier()).forEach(p -> beanPropertySet.addIdentifier(p));
		// return bean property set
		return beanPropertySet;
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
								if (p.addToPropertySet) {
									properties.add(p.property);
									LOGGER.debug(
											() -> "Bean class [" + beanClass + "] - added bean property [" + p + "]");
								}
								// check nested
								properties.addAll(resolveBeanProperties(p.property.getType(), null, p.property));
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
	private Optional<ResolvedBeanProperty<?>> buildBeanProperty(Class<?> beanClass, Path<?> parentPath,
			BeanProperty<?> parent, PropertyDescriptor propertyDescriptor) throws BeanIntrospectionException {

		boolean addToPropertySet = true;

		// annotations
		Annotation[] annotations = null;
		Field propertyField = findDeclaredField(beanClass, propertyDescriptor.getName());
		if (propertyField != null) {
			// check ignore
			if (propertyField.isAnnotationPresent(Ignore.class)) {
				final Ignore ignore = propertyField.getAnnotation(Ignore.class);

				LOGGER.debug(() -> "Bean class [" + beanClass + "] - Property " + propertyDescriptor.getName()
						+ " ignored according to IgnoreProperty annotation - Include nested: "
						+ ignore.includeNested());

				addToPropertySet = false;

				if (ignore.includeNested()) {
					return Optional.empty();
				}
			}

			annotations = propertyField.getAnnotations();
		}

		BeanProperty.Builder<?> property = getPropertyBuilder(propertyDescriptor,
				(annotations != null) ? annotations : new Annotation[0]).parent(parent)
						.readMethod(propertyDescriptor.getReadMethod()).writeMethod(propertyDescriptor.getWriteMethod())
						.field(propertyField).annotations(annotations);

		if (parent == null && parentPath != null) {
			property.parent(parentPath);
		}

		// post processors
		if (addToPropertySet) {
			property = postProcessBeanProperty(property, beanClass);
		}

		// check ignore mode
		IgnoreMode ignoreMode = property.getIgnoreMode().orElse(IgnoreMode.DO_NOT_IGNORE);

		if (ignoreMode != IgnoreMode.DO_NOT_IGNORE) {
			if (ignoreMode == IgnoreMode.IGNORE_INCLUDE_NESTED) {
				return Optional.empty();
			} else {
				addToPropertySet = false;
			}
		}

		return Optional.of(new ResolvedBeanProperty<>(property, addToPropertySet));

	}

	/**
	 * Get a suitable {@link BeanProperty} builder according to property type.
	 * @param propertyDescriptor Property descriptor
	 * @param annotations The annotations bound to the bean property
	 * @return {@link BeanProperty} builder
	 */
	@SuppressWarnings("unchecked")
	private static BeanProperty.Builder<?> getPropertyBuilder(PropertyDescriptor propertyDescriptor,
			Annotation[] annotations) {
		// check type
		if (TypeUtils.isString(propertyDescriptor.getPropertyType())) {
			return StringBeanProperty.builder(propertyDescriptor.getName());
		}
		if (TypeUtils.isBoolean(propertyDescriptor.getPropertyType())) {
			return BooleanBeanProperty.builder(propertyDescriptor.getName(),
					propertyDescriptor.getPropertyType().isPrimitive());
		}
		if (TypeUtils.isNumber(propertyDescriptor.getPropertyType())) {
			return NumericBeanProperty.builder(propertyDescriptor.getName(),
					(Class<? extends Number>) propertyDescriptor.getPropertyType());
		}
		if (TypeUtils.isDate(propertyDescriptor.getPropertyType())
				|| TypeUtils.isTemporal(propertyDescriptor.getPropertyType())) {
			return TemporalBeanProperty.builder(propertyDescriptor.getName(), propertyDescriptor.getPropertyType());
		}
		// default
		return BeanProperty.builder(propertyDescriptor.getName(), propertyDescriptor.getPropertyType());
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

	/*
	 * Support class
	 */
	private static final class ResolvedBeanProperty<T> {

		final BeanProperty<T> property;
		final boolean addToPropertySet;

		ResolvedBeanProperty(BeanProperty<T> property, boolean addToPropertySet) {
			super();
			this.property = property;
			this.addToPropertySet = addToPropertySet;
		}

	}

}
