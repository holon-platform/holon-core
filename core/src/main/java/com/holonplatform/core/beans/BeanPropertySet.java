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
package com.holonplatform.core.beans;

import java.util.Optional;

import com.holonplatform.core.HasConfiguration;
import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.Path;
import com.holonplatform.core.Validator.ValidationException;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.datastore.DataMappable;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property.PropertyAccessException;
import com.holonplatform.core.property.Property.PropertyNotFoundException;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link PropertySet} collecting and providing Java Bean property set and configuration as {@link PathProperty}
 * property type elements.
 * 
 * @param <T> Bean class to which this property set refers
 * 
 * @see BeanIntrospector
 */
public interface BeanPropertySet<T> extends PropertySet<PathProperty<?>>, BeanPropertyInspector, DataMappable {

	/**
	 * Get the bean class to which this property set refers.
	 * @return the bean class
	 */
	Class<? extends T> getBeanClass();

	/**
	 * Read the value of the property with given <code>propertyName</code> from given bean instance.
	 * @param propertyName Name of the property to read (not null)
	 * @param instance Bean instance from which to read the property value (not null)
	 * @param <V> Property and result type
	 * @return The property value
	 * @throws PropertyNotFoundException If a property with given name is not found in this property set
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If actual property type and expected type mismatch
	 */
	<V> V read(String propertyName, T instance);

	/**
	 * Read the value of the property bound to given <code>path</code> from given bean instance, using full path name to
	 * match the bean property to read.
	 * @param <V> Path and value type
	 * @param path Property path to read (not null)
	 * @param instance Bean instance from which to read the property value (not null)
	 * @return The property value
	 * @throws PropertyNotFoundException If a property wich corresponds to given path name is not found in this property
	 *         set
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If actual property type and expected type mismatch
	 */
	<V> V read(Path<V> path, T instance);

	/**
	 * Write the <code>value</code> of the property with given <code>propertyName</code> to given bean instance.
	 * @param propertyName Name of the property to write (not null)
	 * @param value Value to write (may be null)
	 * @param instance Bean instance to which to write the property value
	 * @throws PropertyNotFoundException If a property with given name is not found in this property set
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If actual property type and expected type mismatch
	 */
	void write(String propertyName, Object value, T instance);

	/**
	 * Write the <code>value</code> bound to given <code>path</code> to given bean instance, using full path name to
	 * match the bean property to write.
	 * @param <P> Path and value type
	 * @param path Property path to write (not null)
	 * @param value Value to write (may be null)
	 * @param instance Bean instance to which to write the property value
	 * @throws PropertyNotFoundException If a property wich corresponds to given path name is not found in this property
	 *         set
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If actual property type and expected type mismatch
	 */
	<P> void write(Path<P> path, P value, T instance);

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
	 * @param propertyBox PropertyBox into which to write the property values (not null)
	 * @param instance Bean instance from which read the property values (not null)
	 * @param ignoreMissing <code>true</code> to ignore properties of the PropertyBox property set which are not present
	 *        as bean property. If <code>false</code>, when a property of the PropertyBox property set does not match
	 *        with any of the bean properties, a {@link PropertyNotFoundException} is thrown.
	 * @return The updated PropertyBox
	 * @throws PropertyNotFoundException If <code>ignoreMissing</code> is <code>false</code> and a property of the
	 *         PropertyBox property set does not match with any of the bean properties
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If the bean property type and the PropertyBox property type mismatch for a property
	 * @throws ValidationException If not {@link PropertyBox#isInvalidAllowed()} for given property box and one of the
	 *         property values validation failed
	 */
	PropertyBox read(PropertyBox propertyBox, T instance, boolean ignoreMissing);

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
	default PropertyBox read(PropertyBox propertyBox, T instance) {
		return read(propertyBox, instance, false);
	}

	/**
	 * Read the property values from given bean instance into a {@link PropertyBox} with this property set.
	 * <p>
	 * By default, the created PropertyBox allows invalid values, so no property value validation is performed.
	 * </p>
	 * <p>
	 * Any {@link PropertyValueConverter} will be applied to read values from bean data model.
	 * </p>
	 * @param instance Bean instance from which read the property values (not null)
	 * @return The PropertyBox containing the property values read from the given bean instance
	 * @throws PropertyAccessException Error accessing bean properties
	 */
	default PropertyBox read(T instance) {
		return read(PropertyBox.builder(this).invalidAllowed(true).build(), instance, false);
	}

	/**
	 * Write the property values contained into given {@link PropertyBox} into given bean instance.
	 * <p>
	 * The matching between the PropertyBox properties and the bean properties is performed by property name, so only
	 * the PropertyBox properties which implements {@link PathProperty} will be taken into account, using
	 * {@link Path#getName()} as property name.
	 * </p>
	 * <p>
	 * Any {@link PropertyValueConverter} will be applied to write values to bean data model.
	 * </p>
	 * @param propertyBox PropertyBox from which read the property values (not null)
	 * @param instance Bean instance to which to write the property values (not null)
	 * @param ignoreMissing <code>true</code> to ignore properties of the PropertyBox property set which are not present
	 *        as bean property. If <code>false</code>, when a property of the PropertyBox property set does not match
	 *        with any of the bean properties, a {@link PropertyNotFoundException} is thrown.
	 * @return The updated bean instance
	 * @throws PropertyNotFoundException If <code>ignoreMissing</code> is <code>false</code> and a property of the
	 *         PropertyBox property set does not match with any of the bean properties
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If the bean property type and the PropertyBox property type mismatch for a property
	 */
	T write(PropertyBox propertyBox, T instance, boolean ignoreMissing);

	/**
	 * Write the property values contained into given {@link PropertyBox} into given bean instance.
	 * <p>
	 * The matching between the PropertyBox properties and the bean properties is performed by property name, so only
	 * the PropertyBox properties which implements {@link PathProperty} will be taken into account, using
	 * {@link Path#getName()} as property name.
	 * </p>
	 * <p>
	 * Any {@link PropertyValueConverter} will be applied to write values to bean data model.
	 * </p>
	 * @param propertyBox PropertyBox from which read the property values (not null)
	 * @param instance Bean instance to which to write the property values (not null)
	 * @return The updated bean instance
	 * @throws PropertyNotFoundException If a property of the PropertyBox property set does not match with any of the
	 *         bean properties
	 * @throws PropertyAccessException Error accessing bean properties
	 * @throws TypeMismatchException If the bean property type and the PropertyBox property type mismatch for a property
	 */
	default T write(PropertyBox propertyBox, T instance) {
		return write(propertyBox, instance, false);
	}

	// ------- Data mappings

	/**
	 * Get the data model path name to which this bean property set is mapped.
	 * <p>
	 * If the {@link DataMappable#PATH} configuration property is present, the property value is returned. Otherwise,
	 * the simple bean class name is returned.
	 * </p>
	 * <p>
	 * The {@link DataPath} annotation can be used on bean class to declare the data path at bean introspection time.
	 * </p>
	 * @return The data model path name
	 */
	@Override
	default Optional<String> getDataPath() {
		return getConfiguration().getParameter(DataMappable.PATH);
	}

	// ------- Creation

	/**
	 * Create a bean property set using default {@link BeanIntrospector}.
	 * @param <T> Bean type
	 * @param beanClass Bean class for which to create the property set (not null)
	 * @return {@link PropertySet} of the properties detected from given bean class
	 */
	static <T> BeanPropertySet<T> create(Class<? extends T> beanClass) {
		return create(beanClass, null);
	}

	/**
	 * Create a bean property set using default {@link BeanIntrospector}. If a <code>parentPath</code> is specified, it
	 * will be setted as bean root properties parent path.
	 * @param <T> Bean type
	 * @param beanClass Bean class for which to create the property set (not null)
	 * @param parentPath Optional parent path to set as bean root properties parent path
	 * @return {@link PropertySet} of the properties detected from given bean class
	 */
	static <T> BeanPropertySet<T> create(Class<? extends T> beanClass, Path<?> parentPath) {
		return BeanIntrospector.get().getPropertySet(beanClass, parentPath);
	}

	// Builder for property set post processing

	/**
	 * {@link BeanPropertySet} builder.
	 * 
	 * @param <T> Bean type
	 * @param <B> Concrete bean property set type
	 * 
	 * @since 5.1.0
	 */
	public interface Builder<T, B extends BeanPropertySet<T>> extends HasConfiguration<ParameterSet> {

		/**
		 * Set given property names as {@link BeanPropertySet} identifier properties. Any previously declared identifier
		 * property will be replaced by given identifier properties.
		 * <p>
		 * The property names to declare as identifiers must be present in the bean property set.
		 * </p>
		 * @param propertyNames The property names to declare as property set identifiers
		 * @return this
		 * @throws IllegalStateException If one of the property name to declare as identifier is not part of the bean
		 *         property set
		 */
		Builder<T, B> identifiers(String... propertyNames);

		/**
		 * Add a {@link BeanPropertySet} configuration parameter.
		 * @param name The parameter name to add (not null)
		 * @param value The configuration parameter value
		 * @return this
		 */
		Builder<T, B> configuration(String name, Object value);

		/**
		 * Add a {@link BeanPropertySet} configuration parameter using a {@link ConfigProperty}, with
		 * {@link ConfigProperty#getKey()} as parameter name.
		 * @param <C> Config property type
		 * @param configurationProperty The {@link ConfigProperty} to add (not null)
		 * @param value The configuration property value
		 * @return this
		 */
		default <C> Builder<T, B> configuration(ConfigProperty<C> configurationProperty, C value) {
			ObjectUtils.argumentNotNull(configurationProperty, "Configuration property must be not null");
			return configuration(configurationProperty.getKey(), value);
		}

		/**
		 * Build the {@link BeanPropertySet} instance.
		 * @return the {@link BeanPropertySet} instance
		 */
		B build();

	}

}
