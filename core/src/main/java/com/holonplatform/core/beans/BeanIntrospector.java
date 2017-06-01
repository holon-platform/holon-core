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
package com.holonplatform.core.beans;

import com.holonplatform.core.Context;
import com.holonplatform.core.Path;
import com.holonplatform.core.Validator.ValidationException;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.beans.DefaultBeanIntrospector;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertyValueConverter;
import com.holonplatform.core.property.Property.PropertyAccessException;
import com.holonplatform.core.property.Property.PropertyNotFoundException;

/**
 * Provides functionalities to introspect Java Beans and obtain informations about bean properties, methods and
 * configuration.
 * 
 * @since 5.0.0
 */
public interface BeanIntrospector {

	/**
	 * Default {@link Context} resource key
	 */
	static final String CONTEXT_KEY = BeanIntrospector.class.getName();

	/**
	 * Introspect given Java Bean <code>beanClass</code> and provides a {@link BeanPropertySet} to learn about bean
	 * properties and configuration.
	 * @param beanClass Bean class to introspect (not null)
	 * @return A {@link BeanPropertySet} describing the target bean properties and configuration
	 * @param <T> Bean class type
	 * @throws BeanIntrospectionException If an exception occurs during introspection
	 */
	default <T> BeanPropertySet<T> getPropertySet(Class<? extends T> beanClass) {
		return getPropertySet(beanClass, null);
	}

	/**
	 * Introspect given Java Bean <code>beanClass</code> and provides a {@link BeanPropertySet} to learn about bean
	 * properties and configuration. If a <code>parentPath</code> is specified, it will be setted as bean root
	 * properties parent path.
	 * @param beanClass Bean class to introspect (not null)
	 * @param parentPath Optional parent path to set as bean root properties parent path
	 * @return A {@link BeanPropertySet} describing the target bean properties and configuration
	 * @param <T> Bean class type
	 * @throws BeanIntrospectionException If an exception occurs during introspection
	 */
	<T> BeanPropertySet<T> getPropertySet(Class<? extends T> beanClass, Path<?> parentPath);

	/**
	 * Read the property values from given bean instance into the given {@link PropertyBox}, using given
	 * <code>propertyBox</code> property set.
	 * <p>
	 * The matching between the PropertyBox properties and the bean properties is performed by property name, so only
	 * the PropertyBox properties which implements {@link Path} will be taken into account, using {@link Path#getName()}
	 * as property name.
	 * </p>
	 * <p>
	 * Any {@link PropertyValueConverter} will be applied to read values from bean data model.
	 * </p>
	 * @param <T> Bean type
	 * @param ignoreMissing <code>true</code> to ignore properties of the PropertyBox property set which are not present
	 *        as bean property. If <code>false</code>, when a property of the PropertyBox property set does not match
	 *        with any of the bean properties, a {@link PropertyNotFoundException} is thrown.
	 * @param propertyBox PropertyBox into which to write the property values (not null)
	 * @param instance Bean instance from which read the property values (not null)
	 * @return The updated PropertyBox
	 * @throws PropertyNotFoundException If <code>ignoreMissing</code> is <code>false</code> and a property of the
	 *         PropertyBox property set does not match with any of the bean properties
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If the bean property type and the PropertyBox property type mismatch for a property
	 * @throws ValidationException If not {@link PropertyBox#isInvalidAllowed()} for given property box and one of the
	 *         property values validation failed
	 */
	@SuppressWarnings("unchecked")
	default <T> PropertyBox read(PropertyBox propertyBox, T instance, boolean ignoreMissing) {
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");
		return getPropertySet((Class<T>) instance.getClass()).read(propertyBox, instance);
	}

	/**
	 * Read the property values from given bean instance into the given {@link PropertyBox}, using given
	 * <code>propertyBox</code> property set.
	 * <p>
	 * The matching between the PropertyBox properties and the bean properties is performed by property name, so only
	 * the PropertyBox properties which implements {@link Path} will be taken into account, using {@link Path#getName()}
	 * as property name.
	 * </p>
	 * <p>
	 * Any {@link PropertyValueConverter} will be applied to read values from bean data model.
	 * </p>
	 * @param <T> Bean type
	 * @param propertyBox PropertyBox into which to write the property values (not null)
	 * @param instance Bean instance from which read the property values (not null)
	 * @return The updated PropertyBox
	 * @throws PropertyNotFoundException If a property of the PropertyBox property set does not match with any of the
	 *         bean properties
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If the bean property type and the PropertyBox property type mismatch for a property
	 * @throws ValidationException If not {@link PropertyBox#isInvalidAllowed()} for given property box and one of the
	 *         property values validation failed
	 */
	default <T> PropertyBox read(PropertyBox propertyBox, T instance) {
		return read(propertyBox, instance, false);
	}

	/**
	 * Write the property values contained into given {@link PropertyBox} into given bean instance.
	 * <p>
	 * The matching between the PropertyBox properties and the bean properties is performed by property name, so only
	 * the PropertyBox properties which implements {@link Path} will be taken into account, using {@link Path#getName()}
	 * as property name.
	 * </p>
	 * <p>
	 * Any {@link PropertyValueConverter} will be applied to write values to bean data model.
	 * </p>
	 * @param <T> Bean type
	 * @param ignoreMissing <code>true</code> to ignore properties of the PropertyBox property set which are not present
	 *        as bean property. If <code>false</code>, when a property of the PropertyBox property set does not match
	 *        with any of the bean properties, a {@link PropertyNotFoundException} is thrown.
	 * @param propertyBox PropertyBox from which read the property values (not null)
	 * @param instance Bean instance to which to write the property values (not null)
	 * @return The updated bean instance
	 * @throws PropertyNotFoundException If <code>ignoreMissing</code> is <code>false</code> and a property of the
	 *         PropertyBox property set does not match with any of the bean properties
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If the bean property type and the PropertyBox property type mismatch for a property
	 */
	@SuppressWarnings("unchecked")
	default <T> T write(PropertyBox propertyBox, T instance, boolean ignoreMissing) {
		ObjectUtils.argumentNotNull(instance, "Bean instance must be not null");
		return getPropertySet((Class<T>) instance.getClass()).write(propertyBox, instance);
	}

	/**
	 * Write the property values contained into given {@link PropertyBox} into given bean instance.
	 * <p>
	 * The matching between the PropertyBox properties and the bean properties is performed by property name, so only
	 * the PropertyBox properties which implements {@link Path} will be taken into account, using {@link Path#getName()}
	 * as property name.
	 * </p>
	 * <p>
	 * Any {@link PropertyValueConverter} will be applied to write values to bean data model.
	 * </p>
	 * @param <T> Bean type
	 * @param propertyBox PropertyBox from which read the property values (not null)
	 * @param instance Bean instance to which to write the property values (not null)
	 * @return The updated bean instance
	 * @throws PropertyNotFoundException If a property of the PropertyBox property set does not match with any of the
	 *         bean properties
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If the bean property type and the PropertyBox property type mismatch for a property
	 */
	default <T> T write(PropertyBox propertyBox, T instance) {
		return write(propertyBox, instance, false);
	}

	/**
	 * Adds a {@link BeanPropertyPostProcessor} to handle {@link BeanProperty} configuration during introspection.
	 * @param beanPropertyPostProcessor BeanPropertyPostProcessor to add (not null)
	 */
	void addBeanPropertyPostProcessor(BeanPropertyPostProcessor beanPropertyPostProcessor);

	/**
	 * Removes a {@link BeanPropertyPostProcessor}.
	 * @param beanPropertyPostProcessor BeanPropertyPostProcessor to remove (not null)
	 */
	void removeBeanPropertyPostProcessor(BeanPropertyPostProcessor beanPropertyPostProcessor);

	/**
	 * If caching is enabled and supported, clear current introspection cache.
	 * @return <code>true</code> if caching is supported and was cleared
	 */
	boolean clearCache();

	// Accessors

	/**
	 * Gets the current {@link BeanIntrospector} instance.
	 * @return The {@link Context}-bound BeanIntrospector instance, if available using {@link #CONTEXT_KEY} as context
	 *         key, or the default instance for the default ClassLoader obtained through {@link #getDefault()}.
	 */
	static BeanIntrospector get() {
		return Context.get().resource(CONTEXT_KEY, BeanIntrospector.class).orElse(getDefault());
	}

	// Defaults

	/**
	 * Return the default {@link BeanIntrospector} using given <code>classLoader</code>.
	 * <p>
	 * The default introspector is inited loading {@link BeanPropertyPostProcessor}s using fully qualified name of its
	 * implementation class name to a <code>com.holonplatform.core.beans.BeanPropertyPostProcessor</code> file in the
	 * <code>META-INF/services</code> directory.
	 * </p>
	 * @param classLoader ClassLoader to use
	 * @return Default BeanIntrospector
	 */
	static BeanIntrospector getDefault(ClassLoader classLoader) {
		return DefaultBeanIntrospector.getDefault(classLoader);
	}

	/**
	 * Return the default {@link BeanIntrospector} using default {@link ClassLoader}.
	 * <p>
	 * The default introspector is inited loading {@link BeanPropertyPostProcessor}s using fully qualified name of its
	 * implementation class name to a <code>com.holonplatform.core.beans.BeanPropertyPostProcessor</code> file in the
	 * <code>META-INF/services</code> directory.
	 * </p>
	 * @return Default BeanIntrospector
	 */
	static BeanIntrospector getDefault() {
		return DefaultBeanIntrospector.getDefault(null);
	}

	/**
	 * Exception thrown for bean introspection failures.
	 */
	@SuppressWarnings("serial")
	public class BeanIntrospectionException extends RuntimeException {

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public BeanIntrospectionException(String message) {
			super(message);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public BeanIntrospectionException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
