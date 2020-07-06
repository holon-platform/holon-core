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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.Validator;
import com.holonplatform.core.Validator.Validatable;
import com.holonplatform.core.Validator.ValidationException;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.utils.CommonMessages;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;
import com.holonplatform.core.objects.EqualsHandler;
import com.holonplatform.core.objects.HashCodeProvider;
import com.holonplatform.core.property.CollectionPropertyValueConverter;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.Property.PropertyAccessException;
import com.holonplatform.core.property.Property.PropertyNotFoundException;
import com.holonplatform.core.property.Property.PropertyReadException;
import com.holonplatform.core.property.Property.PropertyReadOnlyException;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.PropertyValueConverter;
import com.holonplatform.core.property.PropertyValueProvider;
import com.holonplatform.core.property.VirtualProperty;

/**
 * Abstract {@link PropertyBox} implementation.
 * <p>
 * Subclasses must implement {@link #getPropertyValue(Property)} method to provide actual value for given property. That
 * value will be processed by this class to check type consistency and apply any {@link PropertyValueConverter} bound to
 * target property.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see PropertyBox
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractPropertyBox implements PropertyBox {

	/*
	 * Property set (immutable)
	 */
	private final PropertySet<Property> propertySet;

	/*
	 * Whether to accept invalid property values (ignore property validators)
	 */
	private boolean invalidAllowed;

	/**
	 * Optional hash code provider
	 */
	private HashCodeProvider<PropertyBox> hashCodeProvider;

	/**
	 * Optional equals handler
	 */
	private EqualsHandler<PropertyBox> equalsHandler;

	/**
	 * Constructor
	 * @param propertySet PropertySet instance to use
	 */
	@SuppressWarnings("unchecked")
	public AbstractPropertyBox(PropertySet<? extends Property> propertySet) {
		super();
		ObjectUtils.argumentNotNull(propertySet, "PropertySet must be not null");
		this.propertySet = (PropertySet<Property>) propertySet;
	}

	/**
	 * Box property set
	 * @return PropertySet
	 */
	protected PropertySet<Property> getPropertySet() {
		return propertySet;
	}

	/**
	 * Get current PropertySet, throwing an {@link IllegalArgumentException} if it is <code>null</code>
	 * @return The property set
	 */
	protected PropertySet<Property> getAndCheckPropertySet() {
		if (propertySet == null) {
			throw new IllegalStateException("Null PropertySet");
		}
		return propertySet;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyBox#isInvalidAllowed()
	 */
	@Override
	public boolean isInvalidAllowed() {
		return invalidAllowed;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyBox#setInvalidAllowed(boolean)
	 */
	@Override
	public void setInvalidAllowed(boolean invalidAllowed) {
		this.invalidAllowed = invalidAllowed;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertySet#size()
	 */
	@Override
	public int size() {
		return getAndCheckPropertySet().size();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertySet#contains(com.holonplatform.core.property.Property)
	 */
	@Override
	public boolean contains(Property property) {
		return getAndCheckPropertySet().contains(property);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Property> iterator() {
		return getAndCheckPropertySet().iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertySet#stream()
	 */
	@Override
	public Stream<Property> stream() {
		return getAndCheckPropertySet().stream();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertySet#getIdentifiers()
	 */
	@Override
	public Set<Property> getIdentifiers() {
		return getAndCheckPropertySet().getIdentifiers();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertySet#getConfiguration()
	 */
	@Override
	public ParameterSet getConfiguration() {
		return getAndCheckPropertySet().getConfiguration();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyBox#containsValue(com.holonplatform.core.property.Property)
	 */
	@Override
	public <T> boolean containsValue(Property<T> property) {

		ObjectUtils.argumentNotNull(property, CommonMessages.MSG_PROPERTY_NOT_NULL);

		if (contains(property)) {
			// check virtual
			if (property instanceof VirtualProperty) {
				return getValueProviderPropertyValue((VirtualProperty<T>) property) != null;
			}
			// check value not null
			return getPropertyValue(property) != null;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyBox#getValue(com.holonplatform.core.property.Property)
	 */
	@Override
	public <T> T getValue(Property<T> property) throws PropertyAccessException {

		ObjectUtils.argumentNotNull(property, CommonMessages.MSG_PROPERTY_NOT_NULL);

		if (!contains(property)) {
			throw new PropertyNotFoundException(property, "Property " + property + " not found in property set");
		}

		try {
			return getAndCheckPropertyValue(property);
		} catch (PropertyAccessException e) {
			throw e;
		} catch (Exception e) {
			throw new PropertyReadException(property, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyBox#getValueIfPresent(com.holonplatform.core.property.Property)
	 */
	@Override
	public <T> Optional<T> getValueIfPresent(Property<T> property) throws PropertyAccessException {
		ObjectUtils.argumentNotNull(property, "Property must be not null");
		try {
			return Optional.ofNullable(getAndCheckPropertyValue(property));
		} catch (PropertyAccessException e) {
			throw e;
		} catch (Exception e) {
			throw new PropertyReadException(property, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyBox#validate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void validate() throws ValidationException {
		final LinkedList<ValidationException> failures = new LinkedList<>();
		forEach(p -> {
			try {
				p.validate(getValue(p));
			} catch (ValidationException e) {
				failures.add(e);
			}
		});
		if (!failures.isEmpty()) {
			throw (failures.size() == 1) ? failures.getFirst()
					: new ValidationException(failures.toArray(new ValidationException[failures.size()]));
		}
	}

	/**
	 * Get value for given property, checking type consistency and apply any {@link PropertyValueConverter} bound to
	 * property.
	 * <p>
	 * Actual property value must be provided by subclasses through {@link #getPropertyValue(Property)} method.
	 * </p>
	 * @param <T> Property type
	 * @param property Property for which obtain the value
	 * @return Property value
	 * @throws PropertyAccessException Error handling property value
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getAndCheckPropertyValue(Property<T> property) throws PropertyAccessException {
		// check value provider
		if (property instanceof VirtualProperty) {
			return getValueProviderPropertyValue((VirtualProperty<T>) property);
		}

		// use delegate method to obtain value
		Object value = getPropertyValue(property);

		// check type
		if (value != null) {
			if (!TypeUtils.isAssignable(value.getClass(), property.getType())) {
				throw new TypeMismatchException("Value type " + value.getClass().getName()
						+ " doesn't match property type " + property.getType().getName());
			}
		}

		return (T) value;
	}

	/**
	 * Get value of a {@link VirtualProperty} using {@link PropertyValueProvider#getPropertyValue(PropertyBox)}.
	 * @param <T> Property type
	 * @param property Property
	 * @return Property value
	 * @throws PropertyAccessException Error obtaining property value from provider
	 */
	protected <T> T getValueProviderPropertyValue(VirtualProperty<T> property) throws PropertyAccessException {
		PropertyValueProvider<T> valueProvider = property.getValueProvider();
		if (valueProvider == null) {
			throw new PropertyReadException(property, "Property " + property
					+ " declares to provide a value using a PropertyValueProvider, but returned PropertyValueProvider is null");
		}
		try {
			return valueProvider.getPropertyValue(this);
		} catch (Exception e) {
			throw new PropertyReadException(property, e);
		}
	}

	/**
	 * Check property value before putting it in PropertyBox.
	 * @param <T> Property and value type
	 * @param property Property
	 * @param value Property value
	 * @return Checked property value
	 * @throws TypeMismatchException Value is not consistent with property type
	 */
	@SuppressWarnings("unchecked")
	protected <T> T checkupPropertyValue(Property<T> property, T value) throws TypeMismatchException {
		return validatePropertyValue(property,
				property.getConverter().filter(c -> isModelTypeConvertible(value, c))
						.map(cv -> ((PropertyValueConverter<T, Object>) cv).fromModel(value, property))
						.orElseGet(() -> checkValueTypeConsistency(property, value)));
	}

	/**
	 * Check if given value is type compatible with the {@link PropertyValueConverter} model type.
	 * @param property Property
	 * @param value Value
	 * @param converter Converter
	 * @return <code>true</code> if given value is type compatible with the {@link PropertyValueConverter} model type,
	 *         <code>false</code> otherwise
	 */
	private static boolean isModelTypeConvertible(Object value, PropertyValueConverter converter) {
		if (value == null) {
			return true;
		}
		// check collection type
		if (CollectionPropertyValueConverter.class.isAssignableFrom(converter.getClass())) {
			final Class<?> elementType = ((CollectionPropertyValueConverter) converter).getModelElementType();
			if (!Collection.class.isAssignableFrom(value.getClass())) {
				return false;
			}
			final Collection collection = (Collection) value;
			if (collection.isEmpty()) {
				return true;
			}
			Object element = getNonNullCollectionElement(collection);
			if (element == null) {
				return false;
			}
			return TypeUtils.isAssignable(element.getClass(), elementType);
		}
		// default type check
		return TypeUtils.isAssignable(value.getClass(), converter.getModelType());
	}

	private static Object getNonNullCollectionElement(Collection collection) {
		for (Object element : collection) {
			if (element != null) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Validate given property <code>value</code>: all registered {@link Validator}s are invoked to perform value
	 * validation.
	 * <p>
	 * If {@link #isInvalidAllowed()} is <code>true</code>, validation is skipped and any property Validator is ignored.
	 * </p>
	 * @param <T> Property and value type
	 * @param property Property
	 * @param value Value to validate
	 * @return The validated property value
	 * @throws ValidationException If value is not valid
	 */
	protected <T> T validatePropertyValue(Property<T> property, T value) throws ValidationException {
		if (!isInvalidAllowed()) {
			property.validate(value);
		}
		return value;
	}

	/**
	 * Check the given <code>value</code> type is consistent with Property type
	 * @param property Property to check
	 * @param value Value to check
	 * @param <T> Property type
	 * @return Property value
	 * @throws TypeMismatchException If property type and value type are not consistent
	 */
	protected <T> T checkValueTypeConsistency(Property<T> property, T value) throws TypeMismatchException {
		if (value != null && !TypeUtils.isAssignable(value.getClass(), property.getType())) {
			throw new TypeMismatchException("Property " + property + " expected a value of type "
					+ property.getType().getName() + ", got a value of type: " + value.getClass().getName());
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyBox#setValue(com.holonplatform.core.property.Property,
	 * java.lang.Object)
	 */
	@Override
	public <T> void setValue(Property<T> property, T value) throws PropertyAccessException, ValidationException {
		setValue(property, value, false);
	}

	/**
	 * Set the value of given <code>property</code>.
	 * @param <T> Property and value type
	 * @param property Property for which to set the value (not null)
	 * @param value Property value to set
	 * @param ignoreReadOnly Whether to ignore the read-only property state. If <code>true</code>, the value will be
	 *        setted even if the property is read-only, otherwise a {@link PropertyReadOnlyException} is thrown
	 * @throws PropertyReadOnlyException If the <code>ignoreReadOnly</code> parameter is <code>false</code> and the
	 *         property is read-only
	 * @throws PropertyAccessException If an error occurred setting the property value
	 * @throws ValidationException If not {@link #isInvalidAllowed()} and property is {@link Validatable} and validation
	 *         against given value was not successful
	 */
	protected <T> void setValue(Property<T> property, T value, boolean ignoreReadOnly)
			throws PropertyAccessException, ValidationException {
		ObjectUtils.argumentNotNull(property, "Property must be not null");

		// check exists
		if (!contains(property)) {
			throw new PropertyNotFoundException(property, "Property " + property + " not found in property set");
		}

		// check not read-only
		if (!ignoreReadOnly && property.isReadOnly()) {
			throw new PropertyReadOnlyException(property);
		}

		// set the value
		setPropertyValue(property, checkupPropertyValue(property, value));
	}

	/**
	 * Get the provider to use to obtain the {@link PropertyBox} <code>hashCode</code>.
	 * @return Optional hash code provider
	 */
	protected Optional<HashCodeProvider<PropertyBox>> getHashCodeProvider() {
		return Optional.ofNullable(hashCodeProvider);
	}

	/**
	 * Set the provider to use to obtain the {@link PropertyBox} <code>hashCode</code>.
	 * @param hashCodeProvider the hash code provider to set
	 */
	protected void setHashCodeProvider(HashCodeProvider<PropertyBox> hashCodeProvider) {
		this.hashCodeProvider = hashCodeProvider;
	}

	/**
	 * Get the handler to use for the {@link PropertyBox} <code>equals</code> logic.
	 * @return Optional equals handler
	 */
	protected Optional<EqualsHandler<PropertyBox>> getEqualsHandler() {
		return Optional.ofNullable(equalsHandler);
	}

	/**
	 * Set the handler to use for the {@link PropertyBox} <code>equals</code> logic.
	 * @param equalsHandler the equals handler to set
	 */
	protected void setEqualsHandler(EqualsHandler<PropertyBox> equalsHandler) {
		this.equalsHandler = equalsHandler;
	}

	/**
	 * Get the {@link PropertyBox} hash code.
	 * <p>
	 * If an hash code provider function is provided, it is used to provide the hash code. Otherwise, the default
	 * {@link DefaultPropertyBoxEqualsHashCodeHandler} is used, relying on the {@link PropertyBox} identifier properties
	 * values, if available, to provide the object's hash code.
	 * </p>
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		return getHashCodeProvider().orElse(DefaultPropertyBoxEqualsHashCodeHandler.INSTANCE).hashCode(this)
				.orElse(super.hashCode());
	}

	/**
	 * Checks whether some other object is "equal to" this {@link PropertyBox}.
	 * <p>
	 * If an equals handler is provided, it is used to check objects equality. Otherwise, the default
	 * {@link DefaultPropertyBoxEqualsHashCodeHandler} is used, relying on the {@link PropertyBox} identifier properties
	 * values, if available, to check objects equality.
	 * </p>
	 * @return <code>true</code> if object are equal, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		return getEqualsHandler().orElse(DefaultPropertyBoxEqualsHashCodeHandler.INSTANCE).equals(this, obj);
	}

	// ------- Abstract methods

	/**
	 * Gets the actual value for given property.
	 * @param <T> Property type
	 * @param property Property for which obtain the value
	 * @return Property value
	 * @throws PropertyAccessException Error getting property value
	 */
	protected abstract <T> Object getPropertyValue(Property<T> property) throws PropertyAccessException;

	/**
	 * Sets the actual value for given property.
	 * @param <T> Property type
	 * @param property Property for which to set the value
	 * @param value The value to set (maybe null)
	 * @throws PropertyAccessException Error setting property value
	 */
	protected abstract <T> void setPropertyValue(Property<T> property, T value) throws PropertyAccessException;

}
