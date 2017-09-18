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
package com.holonplatform.core.property;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

import com.holonplatform.core.Context;
import com.holonplatform.core.Validator;
import com.holonplatform.core.Validator.Validatable;
import com.holonplatform.core.Validator.ValidationException;
import com.holonplatform.core.internal.property.DefaultPropertyBox;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property.PropertyAccessException;
import com.holonplatform.core.property.Property.PropertyNotFoundException;
import com.holonplatform.core.property.Property.PropertyReadOnlyException;

/**
 * PropertyBox is the base container for {@link Property} values, provinding methods to get and set property values,
 * performing value validation and conversions according to property configurations. The set of the properties managed
 * by a PropertyBox is well defined through {@link PropertySet} abstraction.
 * 
 * @since 5.0.0
 */
@SuppressWarnings("rawtypes")
public interface PropertyBox extends PropertySet<Property> {

	/**
	 * Check if this box contains given <code>property</code> with a not <code>null</code> value.
	 * @param property Property to check (not null)
	 * @param <T> Property type
	 * @return <code>true</code> if box contains given property and the property value is not <code>null</code>
	 */
	<T> boolean containsValue(Property<T> property);

	/**
	 * Get the value for given <code>property</code>.
	 * @param <T> Property and value type
	 * @param property Property for which obtain the value (not null)
	 * @return Property value, or <code>null</code> if no value is associated to given property
	 * @throws PropertyNotFoundException If property is not present in the box property set
	 * @throws PropertyAccessException If an error occurred reading the property value
	 */
	<T> T getValue(Property<T> property);

	/**
	 * Get the value for given <code>property</code>, if property is present in this box and has a not <code>null</code>
	 * value.
	 * <p>
	 * Unlike {@link #getValue(Property)}, this method does not throw a {@link PropertyNotFoundException} when the
	 * specified property is not part of this box property set, an empty Optional is returned instead. An empty Optional
	 * is returned also when the property is present but has no value.
	 * </p>
	 * @param <T> Property and value type
	 * @param property Property for which obtain the value (not null)
	 * @return Property value, or an empty Optional if property is not present in this box or has no value
	 * @throws PropertyAccessException If an error occurred reading the property value
	 */
	<T> Optional<T> getValueIfPresent(Property<T> property);

	/**
	 * Get the {@link Stream} of all the {@link Property}s of the property set bound to this {@link PropertyBox} and
	 * their values, using the {@link PropertyValue} representation.
	 * <p>
	 * All the properties will be part of the stream, even the ones without a value. If a property has not a value in
	 * this {@link PropertyBox}, <code>null</code> will be returned by {@link PropertyValue#getValue()}.
	 * </p>
	 * @param <T> Property and value type
	 * @return a {@link PropertyValue} stream with all the property values
	 */
	<T> Stream<PropertyValue<T>> propertyValues();

	/**
	 * Set the value of given <code>property</code>.
	 * @param <T> Property and value type
	 * @param property Property for which to set the value (not null)
	 * @param value Value to set
	 * @throws PropertyNotFoundException If property is not present in the box property set
	 * @throws PropertyAccessException If an error occurred setting the property value
	 * @throws PropertyReadOnlyException If the property is read-only
	 * @throws ValidationException If not {@link #isInvalidAllowed()} and property is {@link Validatable} and validation
	 *         against given value was not successful
	 */
	<T> void setValue(Property<T> property, T value);

	/**
	 * Gets whether to accept invalid property values when using {@link #setValue(Property, Object)} to set a property
	 * value, i.e. ignores any {@link Validator} registered for property. If invalid values are not allowed and any
	 * property {@link Validator} do not validate the value, a {@link ValidationException} is thrown by
	 * {@link #setValue(Property, Object)}.
	 * <p>
	 * Default value is <code>false</code>.
	 * </p>
	 * @return <code>true</code> if accept invalid property values (ignore validators), <code>false</code> otherwise
	 */
	boolean isInvalidAllowed();

	/**
	 * Set whether to accept invalid property values when using {@link #setValue(Property, Object)} to set a property
	 * value, i.e. to ignore any {@link Validator} registered for property. If invalid values are not allowed and any
	 * property {@link Validator} do not validate the value, a {@link ValidationException} is thrown by
	 * {@link #setValue(Property, Object)}.
	 * @param invalidAllowed <code>true</code> to accept invalid property values (ignore validators), <code>false</code>
	 *        otherwise
	 */
	void setInvalidAllowed(boolean invalidAllowed);

	/**
	 * Checks the validity of the value of each property in the box against every registered property validator, if any.
	 * If one or more value is not valid, an {@link ValidationException} is thrown.
	 * @throws ValidationException One or more property value is not valid
	 */
	void validate() throws ValidationException;

	// Helpers

	/**
	 * Clone this PropertyBox, i.e. create a new PropertyBox with same property set of this box and copy all the
	 * property values from this box to the newly created box.
	 * @return A new, cloned, PropertyBox instance
	 */
	default PropertyBox cloneBox() {
		return builder(this).invalidAllowed(this.isInvalidAllowed()).copyValues(this).build();
	}

	/**
	 * Clone this PropertyBox using given <code>propertySet</code>, i.e. create a new PropertyBox and copy all given set
	 * property values from this box to the newly created box.
	 * @param <P> Actual property type
	 * @param propertySet Property set of the cloned PropertyBox (not null)
	 * @return A new, cloned, PropertyBox instance with given property set
	 */
	@SuppressWarnings("unchecked")
	default <P extends Property> PropertyBox cloneBox(PropertySet<P> propertySet) {
		ObjectUtils.argumentNotNull(propertySet, "Property set must be not null");
		Builder builder = builder(propertySet).invalidAllowed(true);
		propertySet.forEach(p -> {
			if (!p.isReadOnly()) {
				this.getValueIfPresent(p).ifPresent(v -> builder.set(p, v));
			}
		});
		return builder.invalidAllowed(this.isInvalidAllowed()).build();
	}

	/**
	 * Clone this PropertyBox using given <code>propertySet</code>, i.e. create a new PropertyBox and copy all given set
	 * property values from this box to the newly created box.
	 * @param <P> Actual property type
	 * @param propertySet Property set of the cloned PropertyBox (not null)
	 * @return A new, cloned, PropertyBox instance with given property set
	 */
	@SuppressWarnings("unchecked")
	default <P extends Property> PropertyBox cloneBox(P... propertySet) {
		ObjectUtils.argumentNotNull(propertySet, "Property set must be not null");
		return cloneBox(PropertySet.of(propertySet));
	}

	/**
	 * Present given <code>property</code> value, obtained from this PropertyBox, as a {@link String}, using current
	 * {@link PropertyValuePresenterRegistry} if available as {@link Context} resource to obtain a suitable
	 * {@link PropertyValuePresenter}, or default presenter {@link PropertyValuePresenterRegistry#getDefault()}
	 * otherwise.
	 * @param <T> Property type
	 * @param property Property to present
	 * @return String presentation of the given <code>value</code>
	 * @throws PropertyNotFoundException Property is not present in box property set
	 * @throws PropertyAccessException Failed to read property value (value type is not consistent with property type or
	 *         other internal errors)
	 */
	default <T> String present(Property<T> property) {
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		return property.present(getValue(property));
	}

	// Builders

	/**
	 * Shorter method to create a PropertyBox with given <code>properties</code> set.
	 * @param <P> Actual property type
	 * @param properties Set of properties of the PropertyBox to create
	 * @return Builder
	 */
	static <P extends Property> PropertyBox create(Iterable<P> properties) {
		return builder(properties).build();
	}

	/**
	 * Shorter method to create a PropertyBox with given <code>properties</code> set.
	 * @param <P> Actual property type
	 * @param properties Set of properties of the PropertyBox to create
	 * @return Builder
	 */
	@SafeVarargs
	static <P extends Property> PropertyBox create(P... properties) {
		return builder(properties).build();
	}

	/**
	 * Builder to create and populate a PropertyBox.
	 * @param <P> Actual property type
	 * @param properties Set of properties of the PropertyBox to create
	 * @return Builder
	 */
	static <P extends Property> Builder builder(Iterable<P> properties) {
		return new DefaultPropertyBox.PropertyBoxBuilder(properties);
	}

	/**
	 * Builder to create and populate a PropertyBox.
	 * @param <P> Actual property type
	 * @param properties Set of properties of the PropertyBox to create
	 * @return Builder
	 */
	@SafeVarargs
	static <P extends Property> Builder builder(P... properties) {
		return new DefaultPropertyBox.PropertyBoxBuilder(properties);
	}

	/**
	 * Represents a {@link Property} value.
	 * @param <T> Value type
	 */
	public interface PropertyValue<T> extends Serializable {

		/**
		 * Get the {@link Property}.
		 * @return The property
		 */
		Property<T> getProperty();

		/**
		 * Get the property value.
		 * @return the property value (may be null)
		 */
		T getValue();

		/**
		 * Checks whether this property has a value, i.e. the property value is not <code>null</code>.
		 * @return <code>true</code> if this property has a value, <code>false</code> otherwise
		 */
		default boolean hasValue() {
			return getValue() != null;
		}

	}

	// Builder

	/**
	 * Builder to build {@link PropertyBox} instances.
	 */
	public interface Builder {

		/**
		 * Set whether to accept invalid property values when using {@link #setValue(Property, Object)} to set a
		 * property value, i.e. to ignore any {@link Validator} registered for property. If invalid values are not
		 * allowed and any property {@link Validator} do not validate the value, a {@link ValidationException} is thrown
		 * by {@link #setValue(Property, Object)}.
		 * @param invalidAllowed <code>true</code> to accept invalid property values (ignore validators),
		 *        <code>false</code> otherwise
		 * @return this
		 */
		Builder invalidAllowed(boolean invalidAllowed);

		/**
		 * Set value of given <code>property</code>. Value type must be consistent with declared {@link Property} type.
		 * @param <T> Property type
		 * @param property Property for which to set the value (not null)
		 * @param value Value to set
		 * @return this
		 */
		<T> Builder set(Property<T> property, T value);

		/**
		 * Set value of given <code>property</code>, ignoring property read-only state.
		 * @param <T> Property type
		 * @param property Property for which to set the value (not null)
		 * @param value Value to set
		 * @return this
		 */
		<T> Builder setIgnoreReadOnly(Property<T> property, T value);

		/**
		 * Copy the given <code>propertyBox</code> property values only for properties of the source PropertyBox that
		 * have a matching with a property of the PropertyBox to build
		 * @param source PropertyBox from which to copy the values
		 * @return this
		 */
		Builder copyValues(PropertyBox source);

		/**
		 * Build the {@link PropertyBox}
		 * @return PropertyBox instance
		 */
		PropertyBox build();

	}

}
