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

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.holonplatform.core.Path;
import com.holonplatform.core.beans.BeanProperty;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.property.DefaultPropertySet;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.Property.PropertyNotFoundException;
import com.holonplatform.core.property.Property.PropertyReadException;
import com.holonplatform.core.property.Property.PropertyWriteException;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * Default {@link BeanPropertySet} implementation.
 * 
 * @param <T> Bean type
 *
 * @since 5.0.0
 */
public class DefaultBeanPropertySet<T> extends DefaultPropertySet<PathProperty<?>> implements BeanPropertySet<T> {

	private static final long serialVersionUID = -3545524671920283172L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = BeanLogger.create();

	/**
	 * Bean class to which this property set refers
	 */
	private final WeakReference<Class<? extends T>> beanClass;

	/**
	 * Constructor.
	 * @param <P> Actual property type
	 * @param beanClass Bean class to which this property set refers
	 * @param properties Properties of the set
	 */
	public <P extends PathProperty<?>> DefaultBeanPropertySet(Class<? extends T> beanClass, Collection<P> properties) {
		super(properties);
		this.beanClass = new WeakReference<>(beanClass);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertySet#getBeanClass()
	 */
	@Override
	public Class<? extends T> getBeanClass() {
		return beanClass.get();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertySet#getProperty(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <PT> Optional<PathProperty<PT>> getProperty(String propertyName) {
		ObjectUtils.argumentNotNull(propertyName, "Property name must be not null");
		return stream().filter(p -> propertyName.equals(p.relativeName())).findFirst().map(p -> (PathProperty<PT>) p);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertySet#getProperty(java.lang.String, java.lang.Class)
	 */
	@Override
	public <PT> Optional<PathProperty<PT>> getProperty(String propertyName, Class<PT> type) {
		ObjectUtils.argumentNotNull(propertyName, "Property name must be not null");
		ObjectUtils.argumentNotNull(type, "Property type name must be not null");
		return stream().filter(p -> propertyName.equals(p.relativeName())).findFirst()
				.map(p -> checkPropertyType(p, type));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertySet#read(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <V> V read(String propertyName, T instance) {
		ObjectUtils.argumentNotNull(propertyName, "Property name must be not null");
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");

		final BeanProperty<?> property = (BeanProperty<?>) requireProperty(propertyName);
		final Object value = read(property, instance, null);

		// check type
		if (value != null && !TypeUtils.isAssignable(value.getClass(), property.getType())) {
			throw new TypeMismatchException("Read value type " + value.getClass().getName()
					+ " doesn't match property type " + property.getType().getName());
		}

		return (V) value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertySet#read(com.holonplatform.core.Path, java.lang.Object)
	 */
	@Override
	public <V> V read(Path<V> path, T instance) {
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");

		final BeanProperty<?> property = (BeanProperty<?>) requireProperty(path.fullName());
		return read(property, instance, path.getType());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertySet#write(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void write(String propertyName, final Object value, T instance) {
		ObjectUtils.argumentNotNull(propertyName, "Property name must be not null");
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");

		final BeanProperty<?> property = (BeanProperty<?>) requireProperty(propertyName);
		write(property, null, value, instance);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertySet#write(com.holonplatform.core.Path, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public <P> void write(Path<P> path, P value, T instance) {
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");

		final BeanProperty<?> property = (BeanProperty<?>) requireProperty(path.fullName());
		write(property, path.getType(), value, instance);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertySet#read(com.holonplatform.core.property.PropertyBox,
	 * java.lang.Object, boolean)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PropertyBox read(final PropertyBox propertyBox, final T instance, final boolean ignoreMissing) {
		ObjectUtils.argumentNotNull(propertyBox, "PropertyBox must be not null");
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");

		propertyBox.stream().filter(p -> !p.isReadOnly()).filter(p -> Path.class.isAssignableFrom(p.getClass()))
				.map(p -> (Path<?>) p).forEach(p -> {
					getProperty(p, ignoreMissing).ifPresent(
							bp -> propertyBox.setValue((Property) p, read(bp, instance, (Class<Object>) p.getType())));
				});

		return propertyBox;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertySet#write(com.holonplatform.core.property.PropertyBox,
	 * java.lang.Object, boolean)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public T write(PropertyBox propertyBox, T instance, boolean ignoreMissing) {
		ObjectUtils.argumentNotNull(propertyBox, "PropertyBox must be not null");
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");

		propertyBox.stream().filter(p -> !p.isReadOnly()).filter(p -> Path.class.isAssignableFrom(p.getClass()))
				.map(p -> (Path<?>) p).forEach(p -> {
					getProperty(p, ignoreMissing).ifPresent(bp -> {
						final Property<Object> property = ((Property) p);
						final Object boxValue = propertyBox.getValue(property);
						Object value = boxValue;
						// check conversion
						if (!TypeUtils.isAssignable(bp.getType(), property.getType())) {
							value = property.getConverter()
									.filter(c -> TypeUtils.isAssignable(bp.getType(), c.getModelType()))
									.map(c -> ((PropertyValueConverter) c).toModel(boxValue, property))
									.orElse(boxValue);
						}
						write(bp, p.getType(), value, instance);
					});
				});

		return instance;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <V> V read(BeanProperty<?> property, T instance, Class<V> expectedType) {
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");

		Object value = null;
		Object currentInstance = instance;
		for (BeanProperty<?> p : getPropertyHierarchy(property)) {
			currentInstance = value = readValue(p, currentInstance);
		}

		final Object readValue = value;
		final V propertyValue = (expectedType == null) ? (V) readValue
				: (V) property.getConverter()
						.filter(c -> (readValue == null || (!TypeUtils.isAssignable(readValue.getClass(), expectedType)
								&& TypeUtils.isAssignable(readValue.getClass(), c.getPropertyType()))))
						.map(cv -> ((PropertyValueConverter) cv).toModel(readValue, property)).orElse(readValue);

		LOGGER.debug(() -> "BeanPropertySet: read property [" + property + "] value [" + propertyValue
				+ "] from instance [" + instance + "]");

		return propertyValue;
	}

	/**
	 * Read the <code>property</code> value from given instance (if not null), using BeanProperty configuration read
	 * method or field, if available.
	 * @param property Property to read
	 * @param instance Instance to read from
	 * @return Property value
	 */
	private static Object readValue(BeanProperty<?> property, Object instance) {

		ObjectUtils.argumentNotNull(property, "Property must be not null");

		if (instance == null) {
			return null;
		}

		final Object value;

		if (property.getReadMethod().isPresent()) {
			try {
				value = property.getReadMethod().get().invoke(instance);

				LOGGER.debug(() -> "BeanPropertySet: read property [" + property + "] value [" + value
						+ "] from instance [" + instance + "] using method [" + property.getReadMethod() + "]");

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new PropertyReadException(property, e);
			}
		} else {
			Field field = property.getField()
					.orElseThrow(() -> new PropertyReadException(property,
							"No read method and no accessible field available to read property [" + property
									+ "] on bean class [" + instance.getClass().getName() + "]"));
			try {
				value = FieldUtils.readField(field, instance, true);

				LOGGER.debug(() -> "BeanPropertySet: read property [" + property + "] value [" + value
						+ "] from instance [" + instance + "] using field [" + field + "]");

			} catch (IllegalAccessException e) {
				throw new PropertyReadException(property, e);
			}

		}

		return value;

	}

	/**
	 * Write the <code>value</code> of the given property on given bean instance.
	 * @param property Property to write
	 * @param valueType Value type (may be null)
	 * @param value Property value to write
	 * @param instance Bean instance to write into
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void write(BeanProperty<?> property, Class<?> valueType, Object value, T instance) {
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");

		List<BeanProperty<?>> hiearchy = getPropertyHierarchy(property);

		Object instanceToWrite = instance;
		if (hiearchy.size() > 1) {
			for (int i = 0; i < hiearchy.size() - 1; i++) {
				BeanProperty<?> p = hiearchy.get(i);
				Object read = readValue(p, instanceToWrite);
				if (read == null) {
					try {
						// try to istantiate
						read = writeValue(p, p.getType().newInstance(), instanceToWrite);
					} catch (Exception e) {
						throw new PropertyWriteException(property,
								"Failed to istantiate nested class " + p.getType().getName(), e);
					}
				}
				instanceToWrite = read;
			}
		}

		// actual write
		final Class<?> type = (valueType != null) ? valueType : ((value != null) ? value.getClass() : null);

		writeValue(property,
				property.getConverter().filter(c -> (type != null && TypeUtils.isAssignable(type, c.getModelType())))
						.map(cv -> ((PropertyValueConverter) cv).fromModel(value, property)).orElse(value),
				instanceToWrite);
	}

	/**
	 * Write the <code>property</code> value into given instance using given value, using BeanProperty configuration
	 * write method or field, if available.
	 * @param property Property to write
	 * @param value Value to write
	 * @param instance Instance to write
	 * @return Written value
	 */
	private static Object writeValue(BeanProperty<?> property, Object value, Object instance) {
		ObjectUtils.argumentNotNull(property, "Property must be not null");

		if (property.getWriteMethod().isPresent()) {
			try {
				property.getWriteMethod().get().invoke(instance, new Object[] {
						getValueToWrite(property.getWriteMethod().get().getParameters()[0].getType(), value) });

				LOGGER.debug(() -> "BeanPropertySet: written property [" + property + "] value [" + value
						+ "] from instance [" + instance + "] using method [" + property.getWriteMethod() + "]");

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new PropertyWriteException(property,
						"Cannot write property [" + property + "] value of type ["
								+ ((value != null) ? value.getClass().getName() : "null") + "] on bean instance ["
								+ instance + "]",
						e);
			}
		} else {
			Field field = property.getField()
					.orElseThrow(() -> new PropertyReadException(property,
							"No write method and no accessible field available to write property [" + property
									+ "] on bean class [" + instance.getClass().getName() + "]"));
			try {
				FieldUtils.writeField(field, instance, getValueToWrite(field.getType(), value), true);

				LOGGER.debug(() -> "BeanPropertySet: read property [" + property + "] value [" + value
						+ "] from instance [" + instance + "] using field [" + field + "]");

			} catch (IllegalAccessException e) {
				throw new PropertyWriteException(property, e);
			}
		}

		return value;
	}

	/**
	 * Get the value to write on bean property, converting <code>null</code> values to <code>false</code> or
	 * <code>0</code> for primitive types.
	 * @param valueType Value type
	 * @param value Value to write
	 * @return Actual value
	 */
	private static Object getValueToWrite(Class<?> valueType, Object value) {
		if (value == null) {
			// check primitive types
			if (valueType != null && valueType.isPrimitive()) {
				if (boolean.class == valueType) {
					return false;
				}
				return 0;
			}
		}
		return value;
	}

	/**
	 * Get the property with given <code>propertyPath</code> from bean property set using the properties full name as
	 * matching rule.
	 * @param propertyPath Property path
	 * @param ignoreMissing <code>true</code> to ignore mismatches
	 * @return Optional matching bean property
	 * @throws PropertyNotFoundException If ignoreMissing is false and a matching bean property was not found
	 */
	private Optional<BeanProperty<?>> getProperty(Path<?> propertyPath, boolean ignoreMissing)
			throws PropertyNotFoundException {
		ObjectUtils.argumentNotNull(propertyPath, "Property path must be not null");
		Optional<PathProperty<?>> beanProperty = stream()
				.filter(p -> propertyPath.relativeName().equals(p.relativeName())).findFirst();
		if (!ignoreMissing && !beanProperty.isPresent()) {
			throw new PropertyNotFoundException((propertyPath instanceof Property) ? (Property<?>) propertyPath : null,
					"Property with name [" + propertyPath.relativeName() + "] was not found in bean [" + getBeanClass()
							+ "] property set");
		}
		return beanProperty.map(p -> (BeanProperty<?>) p);
	}

	/**
	 * Get the property hierarchy, starting from root hierarchy property.
	 * @param property Property for which to obtain the hierarchy
	 * @return Property hierarchy
	 */
	private static List<BeanProperty<?>> getPropertyHierarchy(BeanProperty<?> property) {
		List<BeanProperty<?>> hierarchy = new LinkedList<>();
		hierarchy.add(property);
		property.getParentProperty().ifPresent((p) -> {
			hierarchy.addAll(0, getPropertyHierarchy(p));
		});
		return hierarchy;
	}

	/**
	 * Check the given property is of given type
	 * @param property Property to check
	 * @param type Required type
	 * @return Typed property
	 * @throws TypeMismatchException If the given type is not consistent with actual property type
	 */
	@SuppressWarnings("unchecked")
	private static <PT> PathProperty<PT> checkPropertyType(PathProperty<?> property, Class<PT> type) {
		if (!TypeUtils.isAssignable(type, property.getType())) {
			throw new TypeMismatchException("Requested property type " + type.getName()
					+ " doesn't match property type " + property.getType().getName());
		}
		return (PathProperty<PT>) property;
	}

}
