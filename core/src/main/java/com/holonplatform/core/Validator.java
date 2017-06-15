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
package com.holonplatform.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.i18n.LocalizationContext;
import com.holonplatform.core.i18n.MessageProvider;
import com.holonplatform.core.internal.BuiltinValidator;
import com.holonplatform.core.internal.DefaultValidator;
import com.holonplatform.core.internal.ValidatorDescriptor;
import com.holonplatform.core.internal.utils.CalendarUtils;
import com.holonplatform.core.internal.utils.FormatUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;

/**
 * Validate a value to check if it is valid.
 * <p>
 * For {@link Validatable} classes, {@link #validate(Object)} method can be used to check if a value is valid. An
 * {@link ValidationException} with an appropriate validation error message is thrown if the value is not valid.
 * </p>
 * <p>
 * {@link ValidatorSupport} interface should be implemented by classes which declare support for {@link Validator}s.
 * </p>
 * <p>
 * This interface provides several static methods to obtain builtin Validators for common use cases.
 * </p>
 * 
 * @param <T> Validation target value type
 * 
 * @since 5.0.0
 */
@FunctionalInterface
public interface Validator<T> extends Serializable {

	/**
	 * Validate given <code>value</code>. If the value is not valid, an {@link ValidationException} is thrown.
	 * <p>
	 * The {@link ValidationException} is {@link Localizable}, providing optional message code and arguments for
	 * validation message localization.
	 * </p>
	 * @param value The value to validate (may be null)
	 * @throws ValidationException If the value is not valid, providing the validation error message.
	 */
	void validate(T value) throws ValidationException;

	/**
	 * Declares support for adding and removing {@link Validator}s.
	 * @param <T> Validation data type
	 */
	public interface ValidatorSupport<T> {

		/**
		 * Adds a validator.
		 * @param validator The validator to add (not null)
		 */
		void addValidator(Validator<T> validator);

		/**
		 * Removes given <code>validator</code>, if it was registered.
		 * @param validator The validator to remove
		 */
		void removeValidator(Validator<T> validator);

	}

	/**
	 * Declares the support for value validation using {@link Validator}s.
	 * @param <T> Supported validators type
	 */
	public interface Validatable<T> {

		/**
		 * Get the registered validators.
		 * @return Registered validator, or an empty collection if none
		 */
		Collection<Validator<T>> getValidators();

		/**
		 * Checks the validity of the given <code>value</code> against every registered validator, if any. If the value
		 * is not valid, an {@link ValidationException} is thrown.
		 * <p>
		 * The {@link ValidationException} is {@link Localizable}, providing optional message code and arguments for
		 * validation message localization.
		 * </p>
		 * @param value Value to validate
		 * @throws ValidationException If the value is not valid, providing the validation error message.
		 */
		default void validate(T value) throws ValidationException {
			LinkedList<ValidationException> failures = new LinkedList<>();
			for (Validator<T> validator : getValidators()) {
				try {
					validator.validate(value);
				} catch (ValidationException ve) {
					failures.add(ve);
				}
			}
			if (!failures.isEmpty()) {
				throw (failures.size() == 1) ? failures.getFirst()
						: new ValidationException(failures.toArray(new ValidationException[failures.size()]));
			}
		}

	}

	/**
	 * Interface to handle the {@link ValidationException} thrown as consequence of a validation.
	 */
	@FunctionalInterface
	public interface ValidationErrorHandler {

		/**
		 * Handle given {@link ValidationException}.
		 * @param error The {@link ValidationException} to handle
		 */
		void handleValidationError(ValidationException error);

	}

	// Builders

	/**
	 * Create a {@link Validator} which uses given {@link Predicate} to perform value validation and the given
	 * localizable <code>message</code> as invalid value message when the predicate condition is not satisfied.
	 * @param <T> Value type
	 * @param predicate Predicate to use to check if value is valid (not null)
	 * @param message Invalid value message (not null)
	 * @return A new {@link Validator} using given {@link Predicate} and invalid value message
	 */
	static <T> Validator<T> create(Predicate<T> predicate, Localizable message) {
		return new DefaultValidator<>(predicate, message);
	}

	/**
	 * Create a {@link Validator} which uses given {@link Predicate} to perform value validation and the given
	 * localizable <code>message</code> as invalid value message when the predicate condition is not satisfied.
	 * @param <T> Value type
	 * @param predicate Predicate to use to check if value is valid (not null)
	 * @param message Invalid value message
	 * @param messageCode Invalid value message localization code
	 * @param messageAguments Optional message localization arguments
	 * @return A new {@link Validator} using given {@link Predicate} and invalid value message
	 */
	static <T> Validator<T> create(Predicate<T> predicate, String message, String messageCode,
			Object... messageAguments) {
		return new DefaultValidator<>(predicate, Localizable.builder().message(message).messageCode(messageCode)
				.messageArguments(messageAguments).build());
	}

	/**
	 * Create a {@link Validator} which uses given {@link Predicate} to perform value validation and the given
	 * localizable <code>message</code> as invalid value message when the predicate condition is not satisfied.
	 * @param <T> Value type
	 * @param predicate Predicate to use to check if value is valid (not null)
	 * @param message Invalid value message
	 * @return A new {@link Validator} using given {@link Predicate} and invalid value message
	 */
	static <T> Validator<T> create(Predicate<T> predicate, String message) {
		return new DefaultValidator<>(predicate, Localizable.builder().message(message).build());
	}

	// ------- Builtin validators

	// Null

	/**
	 * Build a validator that checks that given value is <code>null</code> and uses default
	 * {@link ValidationMessage#NULL} localizable message as validation error message.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Validator type
	 * @return Validator
	 */
	static <T> Validator<T> isNull() {
		return isNull(ValidationMessage.NULL);
	}

	/**
	 * Build a validator that checks that given value is <code>null</code> and uses given {@link Localizable}
	 * <code>message</code> as validation error message.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Validator type
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T> Validator<T> isNull(Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return isNull(message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that given value is <code>null</code>.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Validator type
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T> Validator<T> isNull(String message, String messageCode) {
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null)
					throw new ValidationException(message, messageCode);
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.empty();
			}
		};
	}

	// Not null

	/**
	 * Build a validator that checks that given value is not <code>null</code> and uses default
	 * {@link ValidationMessage#NOT_NULL} localizable message as validation error message.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Validator type
	 * @return Validator
	 */
	static <T> Validator<T> notNull() {
		return notNull(ValidationMessage.NOT_NULL);
	}

	/**
	 * Build a validator that checks that given value is not <code>null</code> and uses given {@link Localizable}
	 * <code>message</code> as validation error message.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Validator type
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T> Validator<T> notNull(Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return notNull(message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that given value is not <code>null</code>.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Validator type
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T> Validator<T> notNull(String message, String messageCode) {
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v == null)
					throw new ValidationException(message, messageCode);
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().required().build());
			}
		};
	}

	// Not empty

	/**
	 * Build a validator that checks that given value is not <code>null</code> nor empty, and uses default
	 * {@link ValidationMessage#NOT_EMPTY} localizable message as validation error message.
	 * <p>
	 * Supported data types: {@link CharSequence}, {@link Collection}, {@link Map} and Arrays
	 * </p>
	 * @param <T> Validator type
	 * @return Validator
	 * @throws UnsupportedValidationTypeException If value to validate is of an unsupported data type
	 */
	static <T> Validator<T> notEmpty() {
		return notEmpty(ValidationMessage.NOT_EMPTY);
	}

	/**
	 * Build a validator that checks that given value is not <code>null</code> nor empty, and uses given
	 * {@link Localizable} <code>message</code> as validation error message.
	 * <p>
	 * Supported data types: {@link CharSequence}, {@link Collection}, {@link Map} and Arrays
	 * </p>
	 * @param <T> Validator type
	 * @param message Validation error message
	 * @return Validator
	 * @throws UnsupportedValidationTypeException If value to validate is of an unsupported data type
	 */
	static <T> Validator<T> notEmpty(Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return notEmpty(message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that given value is not <code>null</code> nor empty.
	 * <p>
	 * Supported data types: {@link CharSequence}, {@link Collection}, {@link Map} and Arrays
	 * </p>
	 * @param <T> Validator type
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 * @throws UnsupportedValidationTypeException If value to validate is of an unsupported data type
	 */
	@SuppressWarnings("serial")
	static <T> Validator<T> notEmpty(String message, String messageCode) {
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v == null) {
					throw new ValidationException(message, messageCode);
				}
				if (!v.getClass().isArray() && !CharSequence.class.isAssignableFrom(v.getClass())
						&& !Collection.class.isAssignableFrom(v.getClass())
						&& !Map.class.isAssignableFrom(v.getClass())) {
					// unsupported type
					throw new UnsupportedValidationTypeException(
							"Data type not supported by noEmpty validator: " + v.getClass().getName());
				}
				if (CharSequence.class.isAssignableFrom(v.getClass()) && ((CharSequence) v).length() == 0)
					throw new ValidationException(message, messageCode);
				if (Collection.class.isAssignableFrom(v.getClass()) && ((Collection<?>) v).isEmpty())
					throw new ValidationException(message, messageCode);
				if (Map.class.isAssignableFrom(v.getClass()) && ((Map<?, ?>) v).isEmpty())
					throw new ValidationException(message, messageCode);
				if (v.getClass().isArray() && ((Object[]) v).length == 0)
					throw new ValidationException(message, messageCode);
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().required().build());
			}
		};
	}

	// Not Blank

	/**
	 * Build a validator that checks that given value is not <code>null</code> nor empty, trimming spaces, and uses
	 * default {@link ValidationMessage#NOT_EMPTY} localizable message as validation error message.
	 * <p>
	 * Supported data types: {@link CharSequence}
	 * </p>
	 * @param <T> Validator type
	 * @return Validator
	 */
	static <T extends CharSequence> Validator<T> notBlank() {
		return notBlank(ValidationMessage.NOT_EMPTY);
	}

	/**
	 * Build a validator that checks that given value is not <code>null</code> nor empty, trimming spaces, and uses
	 * given {@link Localizable} <code>message</code> as validation error message.
	 * <p>
	 * Supported data types: {@link CharSequence}
	 * </p>
	 * @param <T> Validator type
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T extends CharSequence> Validator<T> notBlank(Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return notBlank(message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that given value is not <code>null</code> nor empty, trimming spaces.
	 * <p>
	 * Supported data types: {@link CharSequence}
	 * </p>
	 * @param <T> Validator type
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends CharSequence> Validator<T> notBlank(String message, String messageCode) {
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v == null || v.toString().trim().length() == 0)
					throw new ValidationException(message, messageCode);
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().required().build());
			}
		};
	}

	// Max

	/**
	 * Build a validator that checks that given value is lower than or equal to <code>max</code> value, and uses
	 * {@link ValidationMessage#MAX} as validation error message.
	 * <p>
	 * Supported data types: {@link CharSequence}, {@link Number}, {@link Collection}, {@link Map} and arrays.
	 * </p>
	 * <ul>
	 * <li>String: the string length is checked against given max value (converted to a long)</li>
	 * <li>Integer numbers: the number value is checked against given max value (converted to a long)</li>
	 * <li>Decimal numbers: the number value is checked against given max value</li>
	 * <li>Collections, Maps and arrays: size/length is checked against given max value</li>
	 * </ul>
	 * <p>
	 * If the data type only supports integer max value validation (for example, a String length), the given
	 * <code>max</code> value is treated as an integer value, casting it to an <code>int</code> or a <code>long</code>.
	 * </p>
	 * @param <T> Validator type
	 * @param max Max value
	 * @return Validator
	 * @throws UnsupportedValidationTypeException If value to validate is of an unsupported data type
	 */
	static <T> Validator<T> max(double max) {
		return max(max, ValidationMessage.MAX);
	}

	/**
	 * Build a validator that checks that given value is lower than or equal to <code>max</code> value, and uses given
	 * {@link Localizable} <code>message</code> as validation error message.
	 * <p>
	 * Supported data types: {@link CharSequence}, {@link Number}, {@link Collection}, {@link Map} and arrays.
	 * </p>
	 * <ul>
	 * <li>String: the string length is checked against given max value (converted to a long)</li>
	 * <li>Integer numbers: the number value is checked against given max value (converted to a long)</li>
	 * <li>Decimal numbers: the number value is checked against given max value</li>
	 * <li>Collections, Maps and arrays: size/length is checked against given max value</li>
	 * </ul>
	 * <p>
	 * If the data type only supports integer max value validation (for example, a String length), the given
	 * <code>max</code> value is treated as an integer value, casting it to an <code>int</code> or a <code>long</code>.
	 * </p>
	 * @param <T> Validator type
	 * @param max Max value
	 * @param message Validation error message
	 * @return Validator
	 * @throws UnsupportedValidationTypeException If value to validate is of an unsupported data type
	 */
	static <T> Validator<T> max(double max, Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return max(max, message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that given value is lower than or equal to <code>max</code> value.
	 * <p>
	 * Supported data types: {@link CharSequence}, {@link Number}, {@link Collection}, {@link Map} and arrays.
	 * </p>
	 * <ul>
	 * <li>String: the string length is checked against given max value (converted to a long)</li>
	 * <li>Integer numbers: the number value is checked against given max value (converted to a long)</li>
	 * <li>Decimal numbers: the number value is checked against given max value</li>
	 * <li>Collections, Maps and arrays: size/length is checked against given max value</li>
	 * </ul>
	 * <p>
	 * If the data type only supports integer max value validation (for example, a String length), the given
	 * <code>max</code> value is treated as an integer value, casting it to an <code>int</code> or a <code>long</code>.
	 * </p>
	 * @param <T> Validator type
	 * @param max Max value
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 * @throws UnsupportedValidationTypeException If value to validate is of an unsupported data type
	 */
	@SuppressWarnings("serial")
	static <T> Validator<T> max(double max, String message, String messageCode) {
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null) {

					if (!v.getClass().isArray() && !TypeUtils.isNumber(v.getClass())
							&& !CharSequence.class.isAssignableFrom(v.getClass())
							&& !Collection.class.isAssignableFrom(v.getClass())
							&& !Map.class.isAssignableFrom(v.getClass())) {
						// unsupported type
						throw new UnsupportedValidationTypeException(
								"Data type not supported by max validator: " + v.getClass().getName());
					}

					if (TypeUtils.isNumber(v.getClass())) {
						if (TypeUtils.isDecimalNumber(v.getClass())) {
							if (((Number) v).doubleValue() > max) {
								throw new ValidationException(message, messageCode, max);
							}
						} else {
							if (((Number) v).longValue() > (long) max) {
								throw new ValidationException(message, messageCode, (long) max);
							}
						}
					}
					if (CharSequence.class.isAssignableFrom(v.getClass()) && ((CharSequence) v).length() > (int) max)
						throw new ValidationException(message, messageCode, (long) max);
					if (Collection.class.isAssignableFrom(v.getClass()) && ((Collection<?>) v).size() > (int) max)
						throw new ValidationException(message, messageCode, (long) max);
					if (Map.class.isAssignableFrom(v.getClass()) && ((Map<?, ?>) v).size() > (int) max)
						throw new ValidationException(message, messageCode, (long) max);
					if (v.getClass().isArray() && ((Object[]) v).length > (int) max)
						throw new ValidationException(message, messageCode, (long) max);
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().max(max).build());
			}
		};
	}

	// Min

	/**
	 * Build a validator that checks that given value is greater than or equal to <code>min</code> value, and uses
	 * {@link ValidationMessage#MAX} as validation error message.
	 * <p>
	 * Null values are ignored.
	 * </p>
	 * <p>
	 * Supported data types: {@link CharSequence}, {@link Number}, {@link Collection}, {@link Map} and arrays.
	 * </p>
	 * <ul>
	 * <li>String: the string length is checked against given min value (converted to a long)</li>
	 * <li>Integer numbers: the number value is checked against given min value (converted to a long)</li>
	 * <li>Decimal numbers: the number value is checked against given min value</li>
	 * <li>Collections, Maps and arrays: size/length is checked against given min value</li>
	 * </ul>
	 * <p>
	 * If the data type only supports integer max value validation (for example, a String length), the given
	 * <code>max</code> value is treated as an integer value, casting it to an <code>int</code> or a <code>long</code>.
	 * </p>
	 * @param <T> Validator type
	 * @param min Min value
	 * @return Validator
	 * @throws UnsupportedValidationTypeException If value to validate is of an unsupported data type
	 */
	static <T> Validator<T> min(double min) {
		return min(min, ValidationMessage.MIN);
	}

	/**
	 * Build a validator that checks that given value is greater than or equal to <code>min</code> value, and uses given
	 * {@link Localizable} <code>message</code> as validation error message.
	 * <p>
	 * Null values are ignored.
	 * </p>
	 * <p>
	 * Supported data types: {@link CharSequence}, {@link Number}, {@link Collection}, {@link Map} and arrays.
	 * </p>
	 * <ul>
	 * <li>String: the string length is checked against given min value (converted to a long)</li>
	 * <li>Integer numbers: the number value is checked against given min value (converted to a long)</li>
	 * <li>Decimal numbers: the number value is checked against given min value</li>
	 * <li>Collections, Maps and arrays: size/length is checked against given min value</li>
	 * </ul>
	 * <p>
	 * If the data type only supports integer max value validation (for example, a String length), the given
	 * <code>max</code> value is treated as an integer value, casting it to an <code>int</code> or a <code>long</code>.
	 * </p>
	 * @param <T> Validator type
	 * @param min Min value
	 * @param message Validation error message
	 * @return Validator
	 * @throws UnsupportedValidationTypeException If value to validate is of an unsupported data type
	 */
	static <T> Validator<T> min(double min, Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return min(min, message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that given value is greater than or equal to <code>min</code> value.
	 * <p>
	 * Null values are ignored.
	 * </p>
	 * <p>
	 * Supported data types: {@link CharSequence}, {@link Number}, {@link Collection}, {@link Map} and arrays.
	 * </p>
	 * <ul>
	 * <li>String: the string length is checked against given min value (converted to a long)</li>
	 * <li>Integer numbers: the number value is checked against given min value (converted to a long)</li>
	 * <li>Decimal numbers: the number value is checked against given min value</li>
	 * <li>Collections, Maps and arrays: size/length is checked against given min value</li>
	 * </ul>
	 * <p>
	 * If the data type only supports integer max value validation (for example, a String length), the given
	 * <code>max</code> value is treated as an integer value, casting it to an <code>int</code> or a <code>long</code>.
	 * </p>
	 * @param <T> Validator type
	 * @param min Min value
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 * @throws UnsupportedValidationTypeException If value to validate is of an unsupported data type
	 */
	@SuppressWarnings("serial")
	static <T> Validator<T> min(double min, String message, String messageCode) {
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null) {

					if (!v.getClass().isArray() && !TypeUtils.isNumber(v.getClass())
							&& !CharSequence.class.isAssignableFrom(v.getClass())
							&& !Collection.class.isAssignableFrom(v.getClass())
							&& !Map.class.isAssignableFrom(v.getClass())) {
						// unsupported type
						throw new UnsupportedValidationTypeException(
								"Data type not supported by min validator: " + v.getClass().getName());
					}

					if (TypeUtils.isNumber(v.getClass())) {
						if (TypeUtils.isDecimalNumber(v.getClass())) {
							if (((Number) v).doubleValue() < min) {
								throw new ValidationException(message, messageCode, min);
							}
						} else {
							if (((Number) v).longValue() < (long) min) {
								throw new ValidationException(message, messageCode, (long) min);
							}
						}
					}
					if (CharSequence.class.isAssignableFrom(v.getClass()) && ((CharSequence) v).length() < (int) min)
						throw new ValidationException(message, messageCode, (long) min);
					if (Collection.class.isAssignableFrom(v.getClass()) && ((Collection<?>) v).size() < (int) min)
						throw new ValidationException(message, messageCode, (long) min);
					if (Map.class.isAssignableFrom(v.getClass()) && ((Map<?, ?>) v).size() < (int) min)
						throw new ValidationException(message, messageCode, (long) min);
					if (v.getClass().isArray() && ((Object[]) v).length < (int) min)
						throw new ValidationException(message, messageCode, (long) min);
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().min(min).build());
			}
		};
	}

	// Pattern

	/**
	 * Build a validator that checks that given value matches a regular expression, and uses
	 * {@link ValidationMessage#PATTERN} as validation error message.
	 * <p>
	 * Supported data types: {@link CharSequence}
	 * </p>
	 * @param <T> Validator type
	 * @param regex Regular expression to match
	 * @param flags Optional {@link PatternFlag} to considered when resolving the regular expression
	 * @return Validator
	 */
	static <T extends CharSequence> Validator<T> pattern(String regex, PatternFlag... flags) {
		return pattern(regex, ValidationMessage.PATTERN, flags);
	}

	/**
	 * Build a validator that checks that given value matches a regular expression, and uses given {@link Localizable}
	 * <code>message</code> as validation error message.
	 * <p>
	 * Supported data types: {@link CharSequence}
	 * </p>
	 * @param <T> Validator type
	 * @param regex Regular expression to match
	 * @param message Localizable error message
	 * @param flags Optional {@link PatternFlag} to considered when resolving the regular expression
	 * @return Validator
	 */
	static <T extends CharSequence> Validator<T> pattern(String regex, Localizable message, PatternFlag... flags) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return pattern(regex, message.getMessage(), message.getMessageCode(), flags);
	}

	/**
	 * Build a validator that checks that given value matches a regular expression.
	 * <p>
	 * Supported data types: {@link CharSequence}
	 * </p>
	 * @param <T> Validator type
	 * @param regex Regular expression to match
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @param flags Optional {@link PatternFlag} to considered when resolving the regular expression
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends CharSequence> Validator<T> pattern(String regex, String message, String messageCode,
			PatternFlag... flags) {
		ObjectUtils.argumentNotNull(regex, "Regular expression must be not null");
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null) {
					if (!Pattern.compile(regex, PatternFlag.asBitValue(flags)).matcher(v).matches()) {
						throw new ValidationException(message, messageCode);
					}
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().pattern(regex).build());
			}
		};
	}

	// In

	/**
	 * Build a validator that checks that given value is not <code>null</code> and equals to one of the given values,
	 * using default {@link ValidationMessage#IN} ad validation error message.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Value and validator type
	 * @param values Values to match
	 * @return Validator
	 */
	@SafeVarargs
	static <T> Validator<T> in(T... values) {
		return in(ValidationMessage.IN, values);
	}

	/**
	 * Build a validator that checks that given value is not <code>null</code> and equals to one of the given values,
	 * using given {@link Localizable} message as validation error.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Value and validator type
	 * @param message Validation error message
	 * @param values Values to match
	 * @return Validator
	 */
	@SafeVarargs
	static <T> Validator<T> in(Localizable message, T... values) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return in(message.getMessage(), message.getMessageCode(), values);
	}

	/**
	 * Build a validator that checks that given value is not <code>null</code> and equals to one of the given values.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Value and validator type
	 * @param values Values to match
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	@SafeVarargs
	static <T> Validator<T> in(String message, String messageCode, T... values) {
		if (values == null || values.length == 0) {
			throw new IllegalArgumentException("Value must be not null and not empty");
		}
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null) {
					for (T value : values) {
						if (v.equals(value)) {
							return;
						}
					}
				}
				throw new ValidationException(message, messageCode);
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().in(values).build());
			}
		};
	}

	// Not in

	/**
	 * Build a validator that checks that given value not equals to any of the given values, using default
	 * {@link ValidationMessage#NOT_IN} ad validation error message.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Value and validator type
	 * @param values Values to exclude
	 * @return Validator
	 */
	@SafeVarargs
	static <T> Validator<T> notIn(T... values) {
		return notIn(ValidationMessage.NOT_IN, values);
	}

	/**
	 * Build a validator that checks that given value not equals to any of the given values, using given
	 * {@link Localizable} message as validation error.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Value and validator type
	 * @param message Validation error message
	 * @param values Values to exclude
	 * @return Validator
	 */
	@SafeVarargs
	static <T> Validator<T> notIn(Localizable message, T... values) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return notIn(message.getMessage(), message.getMessageCode(), values);
	}

	/**
	 * Build a validator that checks that given value not equals to any of the given values.
	 * <p>
	 * Supported data types: all
	 * </p>
	 * @param <T> Value and validator type
	 * @param values Values to exclude
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	@SafeVarargs
	static <T> Validator<T> notIn(String message, String messageCode, T... values) {
		if (values == null || values.length == 0) {
			throw new IllegalArgumentException("Value must be not null and not empty");
		}
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null) {
					for (T value : values) {
						if (v.equals(value)) {
							throw new ValidationException(message, messageCode);
						}
					}
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().notIn(values).build());
			}
		};
	}

	// NotNegative

	/**
	 * Build a validator that checks that given {@link Number} value is not negative, using default
	 * {@link ValidationMessage#NOT_NEGATIVE} message as validation error message.
	 * <p>
	 * Supported data types: {@link Number}
	 * </p>
	 * @param <T> Validator type
	 * @return Validator
	 */
	static <T extends Number> Validator<T> notNegative() {
		return notNegative(ValidationMessage.NOT_NEGATIVE);
	}

	/**
	 * Build a validator that checks that given {@link Number} value is not negative, using given {@link Localizable}
	 * message as validation error message.
	 * <p>
	 * Supported data types: {@link Number}
	 * </p>
	 * @param <T> Validator type
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T extends Number> Validator<T> notNegative(Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return notNegative(message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that given {@link Number} value is not negative.
	 * <p>
	 * Supported data types: {@link Number}
	 * </p>
	 * @param <T> Validator type
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends Number> Validator<T> notNegative(String message, String messageCode) {
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null && Math.signum(v.doubleValue()) < 0) {
					throw new ValidationException(message, messageCode);
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().min(0).build());
			}
		};
	}

	// Digits

	/**
	 * Build a validator that checks that given {@link Number} value is a number within accepted range, using default
	 * {@link ValidationMessage#DIGITS} message as validation error message.
	 * <p>
	 * Supported data types: {@link Number}
	 * </p>
	 * @param <T> Validator type
	 * @param integral maximum number of integral digits accepted for this number (not negative)
	 * @param fractional maximum number of fractional digits accepted for this number (not negative)
	 * @return Validator
	 */
	static <T extends Number> Validator<T> digits(int integral, int fractional) {
		return digits(integral, fractional, ValidationMessage.DIGITS);
	}

	/**
	 * Build a validator that checks that given {@link Number} value is a number within accepted range, using given
	 * {@link Localizable} message as validation error message.
	 * <p>
	 * Supported data types: {@link Number}
	 * </p>
	 * @param <T> Validator type
	 * @param integral maximum number of integral digits accepted for this number (not negative)
	 * @param fractional maximum number of fractional digits accepted for this number (not negative)
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T extends Number> Validator<T> digits(int integral, int fractional, Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return digits(integral, fractional, message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that given {@link Number} value is a number within accepted range.
	 * <p>
	 * Supported data types: {@link Number}
	 * </p>
	 * @param <T> Validator type
	 * @param integral maximum number of integral digits accepted for this number (not negative)
	 * @param fractional maximum number of fractional digits accepted for this number (not negative)
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends Number> Validator<T> digits(int integral, int fractional, String message, String messageCode) {
		if (integral < 0) {
			throw new IllegalArgumentException("Integral digits max number cannot be negative");
		}
		if (fractional < 0) {
			throw new IllegalArgumentException("Fractional digits max number cannot be negative");
		}
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null) {
					String string = null;
					if (TypeUtils.isDecimalNumber(v.getClass())) {
						BigDecimal bd = (v instanceof BigDecimal) ? (BigDecimal) v
								: BigDecimal.valueOf(v.doubleValue());
						string = bd.stripTrailingZeros().toPlainString();
					} else {
						BigInteger bi = (v instanceof BigInteger) ? (BigInteger) v : BigInteger.valueOf(v.longValue());
						string = bi.toString();
					}
					if (string != null) {
						if (string.startsWith("-")) {
							string = string.substring(1);
						}
						int index = string.indexOf(".");
						int itg = index < 0 ? string.length() : index;
						int fct = index < 0 ? 0 : string.length() - index - 1;
						if (itg > integral) {
							throw new ValidationException(message, messageCode);
						}
						if (fct > fractional) {
							throw new ValidationException(message, messageCode);
						}
					}
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional
						.of(ValidatorDescriptor.builder().integerDigits(integral).fractionDigits(fractional).build());
			}
		};
	}

	// Past

	/**
	 * Build a validator that checks that given {@link Date} value is in the past, using default
	 * {@link ValidationMessage#PAST} message as validation error message.
	 * <p>
	 * Supported data types: {@link Date}
	 * </p>
	 * @param <T> Validator type
	 * @param includeTime Whether to include time in validation. If <code>false</code>, only year/month/day are
	 *        considered.
	 * @return Validator
	 */
	static <T extends Date> Validator<T> past(boolean includeTime) {
		return past(includeTime, ValidationMessage.PAST);
	}

	/**
	 * Build a validator that checks that given {@link Date} value is in the past, using given {@link Localizable}
	 * message as validation error message.
	 * <p>
	 * Supported data types: {@link Date}
	 * </p>
	 * @param <T> Validator type
	 * @param includeTime Whether to include time in validation. If <code>false</code>, only year/month/day are
	 *        considered.
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T extends Date> Validator<T> past(boolean includeTime, Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return past(includeTime, message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that given {@link Date} value is in the past.
	 * <p>
	 * Supported data types: {@link Date}
	 * </p>
	 * @param <T> Validator type
	 * @param includeTime Whether to include time in validation. If <code>false</code>, only year/month/day are
	 *        considered.
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends Date> Validator<T> past(boolean includeTime, String message, String messageCode) {
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null) {
					if (!includeTime) {
						Date today = CalendarUtils.floorTime(Calendar.getInstance()).getTime();
						Date date = CalendarUtils.floorTime(v);
						if (today.equals(date) || date.after(today)) {
							throw new ValidationException(message, messageCode);
						}
					} else {
						if (v.getTime() >= System.currentTimeMillis()) {
							throw new ValidationException(message, messageCode);
						}
					}
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().past().build());
			}
		};
	}

	// Future

	/**
	 * Build a validator that checks that given {@link Date} value is in the future, using default
	 * {@link ValidationMessage#FUTURE} message as validation error message.
	 * <p>
	 * Supported data types: {@link Date}
	 * </p>
	 * @param <T> Validator type
	 * @param includeTime Whether to include time in validation. If <code>false</code>, only year/month/day are
	 *        considered.
	 * @return Validator
	 */
	static <T extends Date> Validator<T> future(boolean includeTime) {
		return future(includeTime, ValidationMessage.FUTURE);
	}

	/**
	 * Build a validator that checks that given {@link Date} value is in the future, using given {@link Localizable}
	 * message as validation error message.
	 * <p>
	 * Supported data types: {@link Date}
	 * </p>
	 * @param <T> Validator type
	 * @param includeTime Whether to include time in validation. If <code>false</code>, only year/month/day are
	 *        considered.
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T extends Date> Validator<T> future(boolean includeTime, Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return future(includeTime, message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that given {@link Date} value is in the future.
	 * <p>
	 * Supported data types: {@link Date}
	 * </p>
	 * @param <T> Validator type
	 * @param includeTime Whether to include time in validation. If <code>false</code>, only year/month/day are
	 *        considered.
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends Date> Validator<T> future(boolean includeTime, String message, String messageCode) {
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null) {
					if (!includeTime) {
						Date today = CalendarUtils.floorTime(Calendar.getInstance()).getTime();
						Date date = CalendarUtils.floorTime(v);
						if (today.equals(date) || date.before(today)) {
							throw new ValidationException(message, messageCode);
						}
					} else {
						if (v.getTime() <= System.currentTimeMillis()) {
							throw new ValidationException(message, messageCode);
						}
					}
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().future().build());
			}
		};
	}

	// Less

	/**
	 * Build a validator that checks that a value is less than given <code>compareTo</code> value, and uses default
	 * {@link ValidationMessage#LESS_THAN} message as validation error message.
	 * <p>
	 * Supported data types: {@link Comparable}
	 * </p>
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @return Validator
	 */
	static <T extends Comparable<T>> Validator<T> lessThan(T compareTo) {
		return lessThan(compareTo, ValidationMessage.LESS_THAN);
	}

	/**
	 * Build a validator that checks that a value is less than given <code>compareTo</code> value, and uses given
	 * {@link Localizable} message as validation error message.
	 * <p>
	 * Supported data types: {@link Comparable}
	 * </p>
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T extends Comparable<T>> Validator<T> lessThan(T compareTo, Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return lessThan(compareTo, message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that a value is less than given <code>compareTo</code> value.
	 * <p>
	 * Supported data types: {@link Comparable}
	 * </p>
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends Comparable<T>> Validator<T> lessThan(T compareTo, String message, String messageCode) {
		ObjectUtils.argumentNotNull(compareTo, "Value to compare must be not null");
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null && v.compareTo(compareTo) >= 0) {
					throw new ValidationException(message, messageCode, compareTo);
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				if (TypeUtils.isNumber(compareTo.getClass())) {
					return Optional.of(ValidatorDescriptor.builder().max(((Number) compareTo)).exclusiveMax().build());
				}
				return Optional.empty();
			}
		};
	}

	/**
	 * Build a validator that checks that a value is less than or equal to given <code>compareTo</code> value, and uses
	 * default {@link ValidationMessage#LESS_OR_EQUAL} message as validation error message. Supported data types:
	 * {@link Comparable}.
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @return Validator
	 */
	static <T extends Comparable<T>> Validator<T> lessOrEqual(T compareTo) {
		return lessOrEqual(compareTo, ValidationMessage.LESS_OR_EQUAL);
	}

	/**
	 * Build a validator that checks that a value is less than or equal to given <code>compareTo</code> value, and uses
	 * given {@link Localizable} message as validation error message. Supported data types: {@link Comparable}.
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T extends Comparable<T>> Validator<T> lessOrEqual(T compareTo, Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return lessOrEqual(compareTo, message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that a value is less than or equal to given <code>compareTo</code> value.
	 * <p>
	 * Supported data types: {@link Comparable}
	 * </p>
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends Comparable<T>> Validator<T> lessOrEqual(T compareTo, String message, String messageCode) {
		ObjectUtils.argumentNotNull(compareTo, "Value to compare must be not null");
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null && v.compareTo(compareTo) > 0) {
					throw new ValidationException(message, messageCode, compareTo);
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				if (TypeUtils.isNumber(compareTo.getClass())) {
					return Optional.of(ValidatorDescriptor.builder().max(((Number) compareTo)).build());
				}
				return Optional.empty();
			}
		};
	}

	// Greater

	/**
	 * Build a validator that checks that a value is greater than given <code>compareTo</code> value, and uses default
	 * {@link ValidationMessage#LESS_THAN} message as validation error message.
	 * <p>
	 * Supported data types: {@link Comparable}
	 * </p>
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @return Validator
	 */
	static <T extends Comparable<T>> Validator<T> greaterThan(T compareTo) {
		return greaterThan(compareTo, ValidationMessage.GREATER_THAN);
	}

	/**
	 * Build a validator that checks that a value is greater than given <code>compareTo</code> value, and uses given
	 * {@link Localizable} message as validation error message.
	 * <p>
	 * Supported data types: {@link Comparable}
	 * </p>
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T extends Comparable<T>> Validator<T> greaterThan(T compareTo, Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return greaterThan(compareTo, message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that a value is greater than given <code>compareTo</code> value.
	 * <p>
	 * Supported data types: {@link Comparable}
	 * </p>
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends Comparable<T>> Validator<T> greaterThan(T compareTo, String message, String messageCode) {
		ObjectUtils.argumentNotNull(compareTo, "Value to compare must be not null");
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null && v.compareTo(compareTo) <= 0) {
					throw new ValidationException(message, messageCode, compareTo);
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				if (TypeUtils.isNumber(compareTo.getClass())) {
					return Optional.of(ValidatorDescriptor.builder().min(((Number) compareTo)).exclusiveMin().build());
				}
				return Optional.empty();
			}
		};
	}

	/**
	 * Build a validator that checks that a value is greater than or equal to given <code>compareTo</code> value, and
	 * uses default {@link ValidationMessage#GREATER_OR_EQUAL} message as validation error message. Supported data
	 * types: {@link Comparable}.
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @return Validator
	 */
	static <T extends Comparable<T>> Validator<T> greaterOrEqual(T compareTo) {
		return greaterOrEqual(compareTo, ValidationMessage.GREATER_OR_EQUAL);
	}

	/**
	 * Build a validator that checks that a value is greater than or equal to given <code>compareTo</code> value, and
	 * uses given {@link Localizable} message as validation error message. Supported data types: {@link Comparable}.
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T extends Comparable<T>> Validator<T> greaterOrEqual(T compareTo, Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return greaterOrEqual(compareTo, message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that a value is greater than or equal to given <code>compareTo</code> value.
	 * <p>
	 * Supported data types: {@link Comparable}
	 * </p>
	 * @param <T> Value and validator type
	 * @param compareTo Value to compare
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends Comparable<T>> Validator<T> greaterOrEqual(T compareTo, String message, String messageCode) {
		ObjectUtils.argumentNotNull(compareTo, "Value to compare must be not null");
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null && v.compareTo(compareTo) < 0) {
					throw new ValidationException(message, messageCode, compareTo);
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				if (TypeUtils.isNumber(compareTo.getClass())) {
					return Optional.of(ValidatorDescriptor.builder().min(((Number) compareTo)).build());
				}
				return Optional.empty();
			}
		};
	}

	// Email

	/**
	 * Build a validator that checks that the value is a valid e-mail address using RFC822 format rules, and uses
	 * default {@link ValidationMessage#EMAIL} message as validation error message.
	 * <p>
	 * Supported data types: {@link CharSequence}
	 * </p>
	 * @param <T> Value and validator type
	 * @return Validator
	 */
	static <T extends CharSequence> Validator<T> email() {
		return email(ValidationMessage.EMAIL);
	}

	/**
	 * Build a validator that checks that the value is a valid e-mail address using RFC822 format rules, and uses given
	 * {@link Localizable} message as validation error message.
	 * <p>
	 * Supported data types: {@link CharSequence}
	 * </p>
	 * @param <T> Value and validator type
	 * @param message Validation error message
	 * @return Validator
	 */
	static <T extends CharSequence> Validator<T> email(Localizable message) {
		ObjectUtils.argumentNotNull(message, "Validation error message must be not null");
		return email(message.getMessage(), message.getMessageCode());
	}

	/**
	 * Build a validator that checks that the value is a valid e-mail address using RFC822 format rules.
	 * <p>
	 * Supported data types: {@link CharSequence}
	 * </p>
	 * @param <T> Value and validator type
	 * @param message Validation error message
	 * @param messageCode Optional validation error message localization code
	 * @return Validator
	 */
	@SuppressWarnings("serial")
	static <T extends CharSequence> Validator<T> email(String message, String messageCode) {
		return new BuiltinValidator<T>() {

			@Override
			public void validate(T v) throws ValidationException {
				if (v != null) {
					if (!Pattern.compile(FormatUtils.EMAIL_RFC822_REGEXP_PATTERN).matcher(v).matches()) {
						throw new ValidationException(message, messageCode);
					}
				}
			}

			@Override
			public Optional<ValidatorDescriptor> getDescriptor() {
				return Optional.of(ValidatorDescriptor.builder().email().build());
			}
		};
	}

	// Support

	/**
	 * Validation message localization code common prefix
	 */
	static final String DEFAULT_MESSAGE_CODE_PREFIX = "holon.common.validation.message.";

	/**
	 * Validation messages for common validators
	 */
	static enum ValidationMessage implements Localizable {

		/**
		 * Default <em>null</em> validation error message
		 */
		NULL("Value must be null", DEFAULT_MESSAGE_CODE_PREFIX + "null"),

		/**
		 * Default <em>notNull</em> validation error message
		 */
		NOT_NULL("Value is required", DEFAULT_MESSAGE_CODE_PREFIX + "notnull"),

		/**
		 * Default <em>notEmpty</em> validation error message
		 */
		NOT_EMPTY("Value is required and must not be empty", DEFAULT_MESSAGE_CODE_PREFIX + "notempty"),

		/**
		 * Default <em>notBlank</em> validation error message
		 */
		NOT_BLANK("Value is required and must not be blank", DEFAULT_MESSAGE_CODE_PREFIX + "notblank"),

		/**
		 * Default <em>max</em> validation error message
		 */
		MAX("Value too large. Maximum is " + MessageProvider.DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER, DEFAULT_MESSAGE_CODE_PREFIX + "max"),

		/**
		 * Default <em>min</em> validation error message
		 */
		MIN("Value too small. Minimum is " + MessageProvider.DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER, DEFAULT_MESSAGE_CODE_PREFIX + "min"),

		/**
		 * Default <em>pattern</em> validation error message
		 */
		PATTERN("Invalid value", DEFAULT_MESSAGE_CODE_PREFIX + "pattern"),

		/**
		 * Default <em>in</em> validation error message
		 */
		IN("Invalid value", DEFAULT_MESSAGE_CODE_PREFIX + "in"),

		/**
		 * Default <em>notIn</em> validation error message
		 */
		NOT_IN("Invalid value", DEFAULT_MESSAGE_CODE_PREFIX + "notin"),

		/**
		 * Default <em>is</em> validation error message
		 */
		DIGITS("Invalid number", DEFAULT_MESSAGE_CODE_PREFIX + "digits"),

		/**
		 * Default <em>notNegative</em> validation error message
		 */
		NOT_NEGATIVE("Negative values are not allowed", DEFAULT_MESSAGE_CODE_PREFIX + "notnegative"),

		/**
		 * Default <em>past</em> validation error message
		 */
		PAST("Date must be in the past", DEFAULT_MESSAGE_CODE_PREFIX + "past"),

		/**
		 * Default <em>future</em> validation error message
		 */
		FUTURE("Date must be in the future", DEFAULT_MESSAGE_CODE_PREFIX + "future"),

		/**
		 * Default <em>lessThan</em> validation error message
		 */
		LESS_THAN("Value must be less than " + MessageProvider.DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER, DEFAULT_MESSAGE_CODE_PREFIX + "lt"),

		/**
		 * Default <em>lessOrEqual</em> validation error message
		 */
		LESS_OR_EQUAL("Value must be less than or equal to " + MessageProvider.DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER, DEFAULT_MESSAGE_CODE_PREFIX + "loe"),

		/**
		 * Default <em>greaterThan</em> validation error message
		 */
		GREATER_THAN("Value must be greater than " + MessageProvider.DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER, DEFAULT_MESSAGE_CODE_PREFIX + "gt"),

		/**
		 * Default <em>greaterOrEqual</em> validation error message
		 */
		GREATER_OR_EQUAL("Value must be greater than or equal to " + MessageProvider.DEFAULT_MESSAGE_ARGUMENT_PLACEHOLDER, DEFAULT_MESSAGE_CODE_PREFIX + "goe"),

		/**
		 * Default <em>email</em> validation error message
		 */
		EMAIL("Invalid e-mail address", DEFAULT_MESSAGE_CODE_PREFIX + "email");

		private final String message;
		private final String messageCode;

		/**
		 * Constructor
		 * @param message Error message
		 * @param messageCode Error message localization code
		 */
		private ValidationMessage(String message, String messageCode) {
			this.message = message;
			this.messageCode = messageCode;
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

	}

	/**
	 * Pattern validation regexp flags
	 */
	public enum PatternFlag {

		/**
		 * Enables Unix lines mode.
		 * @see java.util.regex.Pattern#UNIX_LINES
		 */
		UNIX_LINES(java.util.regex.Pattern.UNIX_LINES),

		/**
		 * Enables case-insensitive matching.
		 * @see java.util.regex.Pattern#CASE_INSENSITIVE
		 */
		CASE_INSENSITIVE(java.util.regex.Pattern.CASE_INSENSITIVE),

		/**
		 * Permits whitespace and comments in pattern.
		 * @see java.util.regex.Pattern#COMMENTS
		 */
		COMMENTS(java.util.regex.Pattern.COMMENTS),

		/**
		 * Enables multiline mode.
		 * @see java.util.regex.Pattern#MULTILINE
		 */
		MULTILINE(java.util.regex.Pattern.MULTILINE),

		/**
		 * Enables dotall mode.
		 * @see java.util.regex.Pattern#DOTALL
		 */
		DOTALL(java.util.regex.Pattern.DOTALL),

		/**
		 * Enables Unicode-aware case folding.
		 * @see java.util.regex.Pattern#UNICODE_CASE
		 */
		UNICODE_CASE(java.util.regex.Pattern.UNICODE_CASE),

		/**
		 * Enables canonical equivalence.
		 * @see java.util.regex.Pattern#CANON_EQ
		 */
		CANON_EQ(java.util.regex.Pattern.CANON_EQ);

		private final int value;

		private PatternFlag(int value) {
			this.value = value;
		}

		/**
		 * Get the regex flag value.
		 * @return flag value as defined in {@link java.util.regex.Pattern}
		 */
		public int getValue() {
			return value;
		}

		/**
		 * Get given flags as bit mask.
		 * @param flags Flags
		 * @return Bit mask value
		 */
		public static int asBitValue(PatternFlag[] flags) {
			int bm = 0;
			if (flags != null && flags.length > 0) {
				for (PatternFlag flg : flags) {
					bm = bm | flg.getValue();
				}
			}
			return bm;
		}

		/**
		 * Get the {@link PatternFlag} which corresponds to given regex flag value
		 * @param flag Regex flag
		 * @return PatternFlag, or <code>null</code> if none matches
		 */
		public static PatternFlag fromFlag(int flag) {
			for (PatternFlag pf : values()) {
				if (pf.getValue() == flag) {
					return pf;
				}
			}
			return null;
		}

	}

	// Exceptions

	/**
	 * Exception used to notify validation errors using {@link Validator#validate(Object)}.
	 * <p>
	 * This exception is {@link Localizable}, providing optional message code and arguments for validation message
	 * localization.
	 * </p>
	 * <p>
	 * ValidationException may act as a wrapper for multiple validation errors, accessible through {@link #getCauses()}
	 * method.
	 * </p>
	 */
	public class ValidationException extends RuntimeException implements Localizable {

		private static final long serialVersionUID = -6564869827469114206L;

		/**
		 * Localization message code
		 */
		private final String messageCode;

		/**
		 * Localization message arguments
		 */
		private final Object[] messageArguments;

		/**
		 * ValidationExceptions that caused this exception
		 */
		private ValidationException[] causes;

		/**
		 * Constructor with message
		 * @param message Validation error message
		 */
		public ValidationException(String message) {
			this(message, null, (Object[]) null);
		}

		/**
		 * Constructor with localized message
		 * @param message Default validation error message
		 * @param messageCode Validation error message code
		 * @param messageArguments Optional message localization arguments
		 */
		public ValidationException(String message, String messageCode, Object... messageArguments) {
			super(message);
			this.messageCode = messageCode;
			this.messageArguments = messageArguments;
		}

		/**
		 * Constructor with causes
		 * @param causes One or more {@link ValidationException}s that caused this exception
		 */
		public ValidationException(ValidationException... causes) {
			super(buildMultipleMessage(causes));
			this.messageCode = null;
			this.messageArguments = null;
			this.causes = causes;
		}

		private static String buildMultipleMessage(ValidationException... causes) {
			if (causes != null) {
				StringBuilder sb = new StringBuilder();
				for (ValidationException cause : causes) {
					if (cause.getMessage() != null) {
						if (sb.length() > 0) {
							sb.append("; ");
						}
						sb.append(cause.getMessage());
					}
				}
				return sb.toString();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Captionable#getCaptionMessageCode()
		 */
		@Override
		public String getMessageCode() {
			return messageCode;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Localizable#getMessageArguments()
		 */
		@Override
		public Object[] getMessageArguments() {
			return messageArguments;
		}

		/**
		 * Get the {@link ValidationException}s that caused this exception.
		 * @return Array of causes, <code>null</code> if none
		 */
		public ValidationException[] getCauses() {
			return causes;
		}

		/**
		 * Set the {@link ValidationException}s that caused this exception
		 * @param causes the causes to set
		 */
		public void setCauses(ValidationException[] causes) {
			this.causes = causes;
		}

		/**
		 * Get all the validation error messages carried by this validation exception.
		 * @return A list of validation error messages which correspond to inner validation exceptions, if any, or a
		 *         list with only one element which corresponds to the validation exception itself.
		 */
		public List<Localizable> getValidationMessages() {
			if (causes == null || causes.length == 0) {
				return Collections.singletonList(this);
			} else {
				List<Localizable> ls = new ArrayList<>(causes.length);
				for (ValidationException cause : causes) {
					ls.add(cause);
				}
				return ls;
			}
		}

		/**
		 * Get all the localized validation error messages carried by this validation exception.
		 * <p>
		 * For successfull localization, a {@link LocalizationContext} must be available as context resource using
		 * {@link LocalizationContext#getCurrent()} and must be localized.
		 * </p>
		 * @return A list of validation error messages which correspond to inner validation exceptions, if any, or a
		 *         list with only one element which corresponds to the validation exception itself.
		 */
		public List<String> getLocalizedValidationMessages() {
			List<String> ls = new LinkedList<>();
			getValidationMessages().forEach(l -> ls.add(LocalizationContext.translate(l, true)));
			return ls;
		}

	}

	/**
	 * Exception thrown by a {@link Validator} when given value to validate is of an unsupported type.
	 */
	public class UnsupportedValidationTypeException extends RuntimeException {

		private static final long serialVersionUID = 2476169346408053520L;

		/**
		 * Constructor
		 * @param message Error message
		 */
		public UnsupportedValidationTypeException(String message) {
			super(message);
		}

	}

}
