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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.holonplatform.core.Validator;
import com.holonplatform.core.Validator.UnsupportedValidationTypeException;
import com.holonplatform.core.Validator.ValidationException;
import com.holonplatform.core.Validator.ValidatorSupport;
import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyConfiguration.PropertyConfigurationEditor;
import com.holonplatform.core.property.PropertyValueConverter;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Abstract {@link Property} implementation with {@link Localizable} support.
 * 
 * @param <T> Property value type
 * @param <P> Concrete property type
 * 
 * @since 5.0.0
 */
public abstract class AbstractProperty<T, P extends Property.Builder<T, P>>
		implements Property<T>, Property.Builder<T, P>, ValidatorSupport<T> {

	private static final long serialVersionUID = -6282091421537570564L;

	/**
	 * Property type (immutable)
	 */
	private final Class<? extends T> type;

	/**
	 * Property configuration
	 */
	private final PropertyConfigurationEditor configuration;

	/**
	 * Optional converter
	 */
	private PropertyValueConverter<T, ?> converter;

	/**
	 * Validators
	 */
	private List<Validator<T>> validators;

	/**
	 * Localizable: Default message
	 */
	private String message;

	/**
	 * Localizable: Message code
	 */
	private String messageCode;

	/**
	 * Localizable: Message arguments
	 */
	private Object[] messageArguments;

	/**
	 * Constructor
	 * @param type Property value type (not null)
	 */
	public AbstractProperty(Class<? extends T> type) {
		this(type, null);
	}

	/**
	 * Constructor with custom property configuration editor
	 * @param type Property value type (not null)
	 * @param configuration Property configuration editor. If <code>null</code>, a default
	 *        {@link PropertyConfigurationEditor} instance will be used
	 */
	public AbstractProperty(Class<? extends T> type, PropertyConfigurationEditor configuration) {
		super();
		ObjectUtils.argumentNotNull(type, "Property type must be not null");
		this.type = type;
		this.configuration = (configuration != null) ? configuration : PropertyConfiguration.create();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#getType()
	 */
	@Override
	public Class<? extends T> getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#getConfiguration()
	 */
	@Override
	public PropertyConfiguration getConfiguration() {
		return configuration;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#getConverter()
	 */
	@Override
	public Optional<PropertyValueConverter<T, ?>> getConverter() {
		return Optional.ofNullable(converter);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#localization(com.holonplatform.core.i18n.Localizable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public P localization(Localizable localizable) {
		ObjectUtils.argumentNotNull(localizable, "Localizable must be not null");
		message(localizable.getMessage());
		messageCode(localizable.getMessageCode());
		messageArguments(localizable.getMessageArguments());
		return (P) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#configuration(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public P configuration(String name, Object value) {
		ObjectUtils.argumentNotNull(name, "Configuration parameter name must be not null");
		configuration.addParameter(name, value);
		return (P) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#temporalType(com.holonplatform.core.temporal.TemporalType)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public P temporalType(TemporalType temporalType) {
		configuration.setTemporalType(temporalType);
		return (P) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#converter(com.holonplatform.core.property.
	 * PropertyValueConverter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public P converter(PropertyValueConverter<T, ?> converter) {
		this.converter = converter;
		return (P) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#converter(java.lang.Class, java.util.function.Function,
	 * java.util.function.Function)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <MODEL> P converter(Class<MODEL> modelType, Function<MODEL, T> fromModel, Function<T, MODEL> toModel) {
		this.converter = new CallbackPropertyValueConverter<>((Class<T>) getType(), modelType, fromModel, toModel);
		return (P) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.validator.Validatable#addValidator(com.holonplatform.core.validator.Validator)
	 */
	@Override
	public void addValidator(Validator<T> validator) {
		ObjectUtils.argumentNotNull(validator, "Validator must be not null");
		if (validators == null) {
			validators = new LinkedList<>();
		}
		validators.add(validator);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.validator.Validatable#removeValidator(com.holonplatform.core.validator.Validator)
	 */
	@Override
	public void removeValidator(Validator<T> validator) {
		if (validator != null && validators != null) {
			validators.remove(validator);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.validator.Validatable#getValidators()
	 */
	@Override
	public Collection<Validator<T>> getValidators() {
		return (validators != null) ? Collections.unmodifiableCollection(validators) : Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.ModelProperty.Builder#validator(com.holonplatform.core.validator.Validator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public P validator(Validator<T> validator) {
		addValidator(validator);
		return (P) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Validator.Validatable#validate(java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void validate(T value) throws ValidationException {
		LinkedList<ValidationException> failures = new LinkedList<>();
		for (Validator<T> validator : getValidators()) {
			try {
				validator.validate(value);
			} catch (UnsupportedValidationTypeException ut) {
				// try to use conveted value
				if (!getConverter().isPresent()) {
					throw ut;
				}
				try {
					((Validator) validator).validate(getConvertedValue(value));
				} catch (ValidationException cve) {
					failures.add(cve);
				}
			} catch (ValidationException ve) {
				failures.add(ve);
			}
		}
		if (!failures.isEmpty()) {
			throw (failures.size() == 1) ? failures.getFirst()
					: new ValidationException(failures.toArray(new ValidationException[failures.size()]));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable#getMessageCode()
	 */
	@Override
	public String getMessageCode() {
		return messageCode;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable#getMessageArguments()
	 */
	@Override
	public Object[] getMessageArguments() {
		return messageArguments;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable.Builder#messageCode(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public P messageCode(String messageCode) {
		this.messageCode = messageCode;
		return (P) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable.Builder#message(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public P message(String defaultMessage) {
		this.message = defaultMessage;
		return (P) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable.Builder#messageArguments(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public P messageArguments(Object... arguments) {
		this.messageArguments = arguments;
		return (P) this;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Property [type=" + ((getType() != null) ? getType().getName() : "null") + "]";
	}

}
