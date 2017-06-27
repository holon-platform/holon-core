/*
 * Copyright 2000-2017 Holon TDCN.
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

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.Logger.Level;
import com.holonplatform.core.internal.utils.AnnotationUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.PropertySetRef;

/**
 * Default {@link PropertySetRefIntrospector} implementation.
 *
 * @since 5.0.0
 */
public enum DefaultPropertySetRefIntrospector implements PropertySetRefIntrospector {

	INSTANCE;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = PropertyLogger.create();

	/**
	 * Class to be used as {@link PropertySet} references cache key.
	 */
	class CacheKey {

		private final WeakReference<Class<?>> clazz;
		private final String fieldName;

		/**
		 * Constructor
		 * @param clazz Reference class
		 * @param fieldName Field name
		 */
		public CacheKey(Class<?> clazz, String fieldName) {
			super();
			this.clazz = new WeakReference<>(clazz);
			this.fieldName = fieldName;
		}

		/**
		 * Get the reference class.
		 * @return the reference class
		 */
		public Class<?> getClazz() {
			return (clazz != null) ? clazz.get() : null;
		}

		/**
		 * Get the field name.
		 * @return the field name
		 */
		public String getFieldName() {
			return fieldName;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((clazz == null || clazz.get() == null) ? 0 : clazz.get().hashCode());
			result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheKey other = (CacheKey) obj;
			if (clazz == null || clazz.get() == null) {
				if (other.clazz != null || other.clazz.get() != null)
					return false;
			} else if (other.clazz == null || other.clazz.get() == null || !clazz.get().equals(other.clazz.get()))
				return false;
			if (fieldName == null) {
				if (other.fieldName != null)
					return false;
			} else if (!fieldName.equals(other.fieldName))
				return false;
			return true;
		}

	}

	/**
	 * Cache
	 */
	private final Map<CacheKey, PropertySet<?>> cache = new WeakHashMap<>(16, 0.9f);

	@Override
	public PropertySet<?> getPropertySet(PropertySetRef annotation) throws PropertySetIntrospectionException {
		ObjectUtils.argumentNotNull(annotation, "PropertySetRef annotation must be not null");

		// reference class
		final Class<?> cls = annotation.value();
		if (cls == null) {
			throw new PropertySetIntrospectionException("[PropertySetRef] missing value");
		}

		// field name
		String fieldName = AnnotationUtils.getStringValue(annotation.field());

		boolean wasNullFieldName = (fieldName == null);

		if (fieldName == null) {

			// check cache
			synchronized (cache) {
				final CacheKey key = new CacheKey(cls, null);
				if (cache.containsKey(key)) {
					return cache.get(key);
				}
			}

			if (PropertySet.class == cls) {
				throw new PropertySetIntrospectionException(
						"Invalid PropertySetRef class value: [" + cls.getName() + "]");
			}

			// If the class itself is a PropertySet, try to instantiate it
			if (PropertySet.class.isAssignableFrom(cls)) {
				try {
					return (PropertySet<?>) cls.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new PropertySetIntrospectionException(
							"[PropertySetRef] Failed to instantiate PropertySet class [" + cls.getName() + "]", e);
				}
			}

			// Look for a public static PropertySet type field
			List<String> candidateFieldNames = new LinkedList<>();
			Field[] flds = cls.getDeclaredFields();
			if (flds != null) {
				for (Field fld : flds) {
					if (Modifier.isStatic(fld.getModifiers()) && Modifier.isPublic(fld.getModifiers())
							&& PropertySet.class.isAssignableFrom(fld.getType())) {
						candidateFieldNames.add(fld.getName());
					}
				}
			}

			if (candidateFieldNames.isEmpty()) {
				throw new PropertySetIntrospectionException(
						"[PropertySetRef] Cannot find any valid public static PropertySet type field in class ["
								+ cls.getName() + "]");
			}

			if (candidateFieldNames.size() > 1) {
				throw new PropertySetIntrospectionException(
						"[PropertySetRef] More than one valid PropertySet type field found in class [" + cls.getName()
								+ "]: please specify the field name to use in PropertySetRef annotation. Detected PropertySet fields: ["
								+ candidateFieldNames + "]");
			}

			fieldName = candidateFieldNames.get(0);

		} else {

			// check cache
			synchronized (cache) {
				final CacheKey key = new CacheKey(cls, fieldName);
				if (cache.containsKey(key)) {
					return cache.get(key);
				}
			}

		}

		if (LOGGER.isEnabled(Level.DEBUG)) {
			final String fn = fieldName;
			LOGGER.debug(() -> "Get PropertySet using PropertySetRef annotation for class [" + cls
					+ "] and field name [" + fn + "]");
		}

		// Read the PropertySet field
		try {
			Object value = FieldUtils.readStaticField(cls, fieldName);

			if (value == null) {
				throw new PropertySetIntrospectionException("[PropertySetRef] The field [" + fieldName + "] in class ["
						+ cls.getName() + "] has null value");
			}

			if (!PropertySet.class.isAssignableFrom(value.getClass())) {
				throw new PropertySetIntrospectionException("[PropertySetRef] The field [" + fieldName + "] in class ["
						+ cls.getName() + "] is not of PropertySet type but [" + value.getClass().getName() + "]");
			}

			final PropertySet<?> propertySet = (PropertySet<?>) value;

			// put in cache and return
			if (wasNullFieldName) {
				cache.putIfAbsent(new CacheKey(cls, null), propertySet);
			}
			PropertySet<?> existing = cache.putIfAbsent(new CacheKey(cls, fieldName), propertySet);
			return (existing != null ? existing : propertySet);

		} catch (IllegalAccessException e) {
			throw new PropertySetIntrospectionException(
					"[PropertySetRef] Failed to read field [" + fieldName + "] from class [" + cls.getName() + "]", e);
		}
	}

}
