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
import com.holonplatform.core.objects.EqualsHandler;
import com.holonplatform.core.objects.HashCodeProvider;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyConfiguration.PropertyConfigurationEditor;
import com.holonplatform.core.property.PropertyValueConverter;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Abstract {@link Property} implementation and builder.
 * 
 * @param <T> Property value type
 * @param <P> Property type
 * @param <B> Concrete property type
 * 
 * @since 5.0.0
 */
public abstract class AbstractProperty<T, P extends Property<T>, B extends Property.Builder<T, P, B>>
		implements Property<T>, Property.Builder<T, P, B>, ValidatorSupport<T> {

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
	private transient Object[] messageArguments;

	/**
	 * Optional hash code provider
	 */
	private HashCodeProvider<? super P> hashCodeProvider;

	/**
	 * Optional equals handler
	 */
	private EqualsHandler<? super P> equalsHandler;

	/**
	 * Constructor
	 * @param type Property type (not null)
	 */
	public AbstractProperty(Class<? extends T> type) {
		super();
		ObjectUtils.argumentNotNull(type, "Property type must be not null");
		this.type = type;
		this.configuration = PropertyConfiguration.create();
	}

	/**
	 * Get the actual property instance.
	 * @return the actual property instance
	 */
	protected abstract P getActualProperty();

	/**
	 * Get the actual property builder.
	 * @return the actual property builder
	 */
	protected abstract B getActualBuilder();

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
	@Override
	public B localization(Localizable localizable) {
		ObjectUtils.argumentNotNull(localizable, "Localizable must be not null");
		message(localizable.getMessage());
		messageCode(localizable.getMessageCode());
		messageArguments(localizable.getMessageArguments());
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#withConfiguration(java.lang.String, java.lang.Object)
	 */
	@Override
	public B withConfiguration(String parameterName, Object value) {
		ObjectUtils.argumentNotNull(parameterName, "Configuration parameter name must be not null");
		configuration.addParameter(parameterName, value);
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#temporalType(com.holonplatform.core.temporal.TemporalType)
	 */
	@Override
	public B temporalType(TemporalType temporalType) {
		configuration.setTemporalType(temporalType);
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#converter(com.holonplatform.core.property.
	 * PropertyValueConverter)
	 */
	@Override
	public B converter(PropertyValueConverter<T, ?> converter) {
		this.converter = converter;
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#converter(java.lang.Class, java.util.function.Function,
	 * java.util.function.Function)
	 */
	@Override
	public <MODEL> B converter(Class<MODEL> modelType, Function<MODEL, T> fromModel, Function<T, MODEL> toModel) {
		this.converter = new CallbackPropertyValueConverter<>(getType(), modelType, fromModel, toModel);
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getType() == null) {
			throw new InvalidExpressionException("Null property type");
		}
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
	 * @see com.holonplatform.core.property.Property.Builder#withValidator(com.holonplatform.core.Validator)
	 */
	@Override
	public B withValidator(Validator<T> validator) {
		addValidator(validator);
		return getActualBuilder();
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
	public B messageCode(String messageCode) {
		this.messageCode = messageCode;
		return (B) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable.Builder#message(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public B message(String defaultMessage) {
		this.message = defaultMessage;
		return (B) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable.Builder#messageArguments(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public B messageArguments(Object... arguments) {
		this.messageArguments = arguments;
		return (B) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable.Builder#message(com.holonplatform.core.i18n.Localizable)
	 */
	@Override
	public B message(Localizable localizable) {
		ObjectUtils.argumentNotNull(localizable, "Localizable must be not null");
		this.message = localizable.getMessage();
		this.messageCode = localizable.getMessageCode();
		this.messageArguments = localizable.getMessageArguments();
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#equalsHandler(com.holonplatform.core.objects.EqualsHandler)
	 */
	@Override
	public B equalsHandler(EqualsHandler<? super P> equalsHandler) {
		this.equalsHandler = equalsHandler;
		return getActualBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property.Builder#hashCodeProvider(com.holonplatform.core.objects.
	 * HashCodeProvider)
	 */
	@Override
	public B hashCodeProvider(HashCodeProvider<? super P> hashCodeProvider) {
		this.hashCodeProvider = hashCodeProvider;
		return getActualBuilder();
	}

	/**
	 * Get the provider to use to obtain the property <code>hashCode</code>.
	 * @return Optional hash code provider
	 */
	protected Optional<HashCodeProvider<? super P>> getHashCodeProvider() {
		return Optional.ofNullable(hashCodeProvider);
	}

	/**
	 * Get the handler to use for the property <code>equals</code> logic.
	 * @return Optional equals handler
	 */
	protected Optional<EqualsHandler<? super P>> getEqualsHandler() {
		return Optional.ofNullable(equalsHandler);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getHashCodeProvider().flatMap(provider -> provider.hashCode(getActualProperty()))
				.orElse(super.hashCode());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return getEqualsHandler().map(handler -> handler.equals(getActualProperty(), obj)).orElse(super.equals(obj));
	}

	/**
	 * Clone this property configuration using given builder.
	 * @param <CB> Actual property builder
	 * @param builder The builder to use to clone the property configuration
	 */
	protected <CB extends Property.Builder<T, P, B>> void cloneProperty(CB builder) {
		// localizable
		builder.localization(this);
		// converter
		getConverter().ifPresent(c -> builder.converter(c));
		// validators
		getValidators().forEach(v -> builder.withValidator(v));
		// identity
		getHashCodeProvider().ifPresent(h -> builder.hashCodeProvider(h));
		getEqualsHandler().ifPresent(h -> builder.equalsHandler(h));
		// configuration
		getConfiguration().getTemporalType().ifPresent(t -> builder.temporalType(t));
		getConfiguration().forEachParameter((n, v) -> builder.withConfiguration(n, v));
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
