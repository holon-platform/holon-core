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

import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import com.holonplatform.core.Path;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.property.BooleanProperty;
import com.holonplatform.core.property.NumericProperty;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property.PropertyNotFoundException;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.property.TemporalProperty;

/**
 * Interface to obtain bean property references as {@link PathProperty} from a bean property set.
 * <p>
 * Bean properties are accessed by name. For nested properties, the default property name hierarchy notation using
 * {@link Path#PATH_HIERARCHY_SEPARATOR} as separator character is used.
 * </p>
 * 
 * @since 5.1.0
 * 
 * @see BeanPropertySet
 */
public interface BeanPropertyInspector {

	/**
	 * Get all available bean {@link PathProperty}s as a {@link Stream}.
	 * @return All available bean properties stream
	 */
	Stream<PathProperty<?>> stream();

	/**
	 * Get the bean property with given <code>propertyName</code>, if available. For nested properties, the default
	 * property name hierarchy notation using {@link Path#PATH_HIERARCHY_SEPARATOR} as separator character is used.
	 * @param <T> Property type
	 * @param propertyName The property name (not null)
	 * @return The bean property with given name, or an empty Optional if not found
	 */
	<T> Optional<PathProperty<T>> getProperty(String propertyName);

	/**
	 * Get the bean property with given <code>propertyName</code> and given <code>type</code>, if available. For nested
	 * properties, the default property name hierarchy notation using {@link Path#PATH_HIERARCHY_SEPARATOR} as separator
	 * character is used.
	 * @param <T> Property type
	 * @param propertyName Property name (not null)
	 * @param type Expected property type (not null)
	 * @return The bean property with given name, or an empty Optional if not found
	 * @throws TypeMismatchException If the given type is not consistent with actual property type
	 */
	<T> Optional<PathProperty<T>> getProperty(String propertyName, Class<T> type);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link StringProperty}, if available.
	 * <p>
	 * If the bean property which corresponds to given property name is not of {@link String} type, a
	 * {@link TypeMismatchException} is thrown.
	 * </p>
	 * @param propertyName The property name (not null)
	 * @return The bean property with given name, if available
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	Optional<StringProperty> getPropertyString(String propertyName);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link BooleanProperty}, if available.
	 * <p>
	 * If the bean property which corresponds to given property name is not of {@link Boolean} type, a
	 * {@link TypeMismatchException} is thrown.
	 * </p>
	 * @param propertyName The property name (not null)
	 * @return The bean property with given name, if available
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	Optional<BooleanProperty> getPropertyBoolean(String propertyName);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link NumericProperty}, if available.
	 * <p>
	 * If the bean property which corresponds to given property name is not of {@link Number} type, a
	 * {@link TypeMismatchException} is thrown.
	 * </p>
	 * @param <N> Numeric property type
	 * @param propertyName The property name (not null)
	 * @return The bean property with given name, if available
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	<N extends Number> Optional<NumericProperty<N>> getPropertyNumeric(String propertyName);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link NumericProperty}, if available.
	 * <p>
	 * If the bean property which corresponds to given property name is not of {@link Number} type, a
	 * {@link TypeMismatchException} is thrown.
	 * </p>
	 * @param <N> Numeric property type
	 * @param propertyName The property name (not null)
	 * @param type Expected property type (not null)
	 * @return The bean property with given name, if available
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	<N extends Number> Optional<NumericProperty<N>> getPropertyNumeric(String propertyName, Class<N> type);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link TemporalProperty}, if available.
	 * <p>
	 * If the bean property which corresponds to given property name is not of temporal type, a
	 * {@link TypeMismatchException} is thrown. Temporal type include {@link Date} types and {@link Temporal} types.
	 * </p>
	 * @param <T> Temporal property type
	 * @param propertyName The property name (not null)
	 * @return The bean property with given name, if available
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	<T> Optional<TemporalProperty<T>> getPropertyTemporal(String propertyName);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link TemporalProperty}, if available.
	 * <p>
	 * If the bean property which corresponds to given property name is not of temporal type, a
	 * {@link TypeMismatchException} is thrown. Temporal type include {@link Date} types and {@link Temporal} types.
	 * </p>
	 * @param <T> Temporal property type
	 * @param propertyName The property name (not null)
	 * @param type Expected property type (not null)
	 * @return The bean property with given name, if available
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	<T> Optional<TemporalProperty<T>> getPropertyTemporal(String propertyName, Class<T> type);

	// ------- Direct

	/**
	 * Get the bean property with given <code>propertyName</code>.
	 * @param <T> Property type
	 * @param propertyName The property name (not null)
	 * @return The bean property with given name, or an empty Optional if not found
	 * @throws PropertyNotFoundException If property with given name was not found in bean property set
	 */
	<T> PathProperty<T> property(String propertyName);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link PathProperty} of given <code>type</code>.
	 * @param <T> Property type
	 * @param propertyName The property name (not null)
	 * @param type Expected property type
	 * @return The bean property with given name, or an empty Optional if not found
	 * @throws PropertyNotFoundException If property with given name was not found in bean property set
	 * @throws TypeMismatchException If the expected type is not consistent with the actual property type
	 */
	<T> PathProperty<T> property(String propertyName, Class<T> type);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link StringProperty}.
	 * <p>
	 * If the bean property which corresponds to given property name is not of {@link String} type, a
	 * {@link TypeMismatchException} is thrown.
	 * </p>
	 * @param propertyName The property name (not null)
	 * @return The bean property with given name
	 * @throws PropertyNotFoundException If property with given name was not found in bean property set
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	StringProperty propertyString(String propertyName);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link BooleanProperty}.
	 * <p>
	 * If the bean property which corresponds to given property name is not of {@link Boolean} type, a
	 * {@link TypeMismatchException} is thrown.
	 * </p>
	 * @param propertyName The property name (not null)
	 * @return The bean property with given name
	 * @throws PropertyNotFoundException If property with given name was not found in bean property set
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	BooleanProperty propertyBoolean(String propertyName);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link NumericProperty}.
	 * <p>
	 * If the bean property which corresponds to given property name is not of {@link Number} type, a
	 * {@link TypeMismatchException} is thrown.
	 * </p>
	 * @param <N> Numeric property type
	 * @param propertyName The property name (not null)
	 * @return The bean property with given name
	 * @throws PropertyNotFoundException If property with given name was not found in bean property set
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	<N extends Number> NumericProperty<N> propertyNumeric(String propertyName);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link NumericProperty}.
	 * <p>
	 * If the bean property which corresponds to given property name is not of {@link Number} type, a
	 * {@link TypeMismatchException} is thrown.
	 * </p>
	 * @param <N> Numeric property type
	 * @param propertyName The property name (not null)
	 * @param type Expected property type (not null)
	 * @return The bean property with given name
	 * @throws PropertyNotFoundException If property with given name was not found in bean property set
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	<N extends Number> NumericProperty<N> propertyNumeric(String propertyName, Class<N> type);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link TemporalProperty}.
	 * <p>
	 * If the bean property which corresponds to given property name is not of temporal type, a
	 * {@link TypeMismatchException} is thrown. Temporal type include {@link Date} types and {@link Temporal} types.
	 * </p>
	 * @param <T> Temporal property type
	 * @param propertyName The property name (not null)
	 * @return The bean property with given name
	 * @throws PropertyNotFoundException If property with given name was not found in bean property set
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	<T> TemporalProperty<T> propertyTemporal(String propertyName);

	/**
	 * Get the bean property with given <code>propertyName</code> as a {@link TemporalProperty}.
	 * <p>
	 * If the bean property which corresponds to given property name is not of temporal type, a
	 * {@link TypeMismatchException} is thrown. Temporal type include {@link Date} types and {@link Temporal} types.
	 * </p>
	 * @param <T> Temporal property type
	 * @param propertyName The property name (not null)
	 * @param type Expected property type (not null)
	 * @return The bean property with given name
	 * @throws PropertyNotFoundException If property with given name was not found in bean property set
	 * @throws TypeMismatchException If the type of the bean property which corresponds to given property name is not
	 *         compatible with the required type
	 */
	<T> TemporalProperty<T> propertyTemporal(String propertyName, Class<T> type);

	// ------- Deprecated

	/**
	 * Get the bean property with given <code>propertyName</code>.
	 * @param <T> Property type
	 * @param propertyName Property name (not null)
	 * @return The bean property with given name
	 * @throws PropertyNotFoundException If property with given name was not found in bean property set
	 * @deprecated Use {@link #property(String)}
	 */
	@Deprecated
	default <T> PathProperty<T> requireProperty(String propertyName) {
		return property(propertyName);
	}

	/**
	 * Get the bean property with given <code>propertyName</code> and given <code>type</code>.
	 * @param <T> Property type
	 * @param propertyName Property name (not null)
	 * @param type Property type
	 * @return The bean property with given name
	 * @throws PropertyNotFoundException If property with given name was not found in bean property set
	 * @throws TypeMismatchException If the given type is not consistent with actual property type
	 * @deprecated Use {@link #property(String, Class)}
	 */
	@Deprecated
	default <T> PathProperty<T> requireProperty(String propertyName, Class<T> type) {
		return property(propertyName, type);
	}

}
