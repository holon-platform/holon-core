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
package com.holonplatform.core.internal.beans;

import java.math.BigDecimal;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Future;

import javax.annotation.Priority;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.validation.constraints.Size;

import com.holonplatform.core.Validator;
import com.holonplatform.core.Validator.PatternFlag;
import com.holonplatform.core.beans.BeanIntrospector.BeanIntrospectionException;
import com.holonplatform.core.beans.BeanProperty.Builder;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.beans.Email;
import com.holonplatform.core.beans.NotBlank;
import com.holonplatform.core.beans.NotEmpty;
import com.holonplatform.core.beans.NotNegative;
import com.holonplatform.core.beans.ValidationMessage;
import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.AnnotationUtils;
import com.holonplatform.core.internal.utils.ClassUtils;

/**
 * A {@link BeanPropertyPostProcessor} to setup property validators using standard Java bean validation API annotations.
 * <p>
 * Only builtin validation constraint annotations are supported: {@link DecimalMin}, {@link DecimalMax}, {@link Digits},
 * {@link Future}, {@link Min}, {@link Max}, {@link Null}, {@link NotNull}, {@link Past}, {@link Pattern}, {@link Size}.
 * In addition, extension validation constraint {@link NotEmpty}, {@link NotBlank}, {@link NotNegative} and
 * {@link Email} are supported.
 * </p>
 * <p>
 * {@link AssertFalse} and {@link AssertTrue} annotations are not supported.
 * </p>
 * <p>
 * If a {@link ValidationMessage} annotation is present on bean property, that is used to obtain the validation error
 * message. If not, the <code>message</code> attribute of validation constraint annotation is used as validation
 * message, and by convention if the message in included between braces is considered as a localization message code,
 * otherwise as a simple, not localizable, message.
 * </p>
 * <p>
 * <code>group</code> and <code>payload</code> annotation attributes are ignored.
 * </p>
 * 
 * @since 5.0.0
 */
@Priority(210)
public class BeanPropertyBeanValidationPostProcessor implements BeanPropertyPostProcessor {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = BeanLogger.create();

	/**
	 * Bean validation API presence in classpath for classloader
	 */
	private static final Map<ClassLoader, Boolean> BEAN_VALIDATION_API_PRESENT = new WeakHashMap<>();

	/**
	 * Checks whether bean validation API is available from classpath
	 * @param classLoader ClassLoader to use
	 * @return <code>true</code> if present
	 */
	private static boolean isBeanValidationApiPresent(ClassLoader classLoader) {
		if (BEAN_VALIDATION_API_PRESENT.containsKey(classLoader)) {
			Boolean present = BEAN_VALIDATION_API_PRESENT.get(classLoader);
			return (present != null && present.booleanValue());
		}
		boolean present = ClassUtils.isPresent("javax.validation.Validation", classLoader);
		BEAN_VALIDATION_API_PRESENT.put(classLoader, present);
		return present;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertyPostProcessor#processBeanProperty(com.holonplatform.core.beans.
	 * BeanProperty.Builder, java.lang.Class)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Builder<?> processBeanProperty(final Builder<?> property, Class<?> beanOrNestedClass) {

		if (isBeanValidationApiPresent(ClassUtils.getDefaultClassLoader())) {

			LOGGER.debug(() -> "Bean validation API found, processing constraint annotations");

			try {
				// null
				property.getAnnotation(Null.class).ifPresent(a -> {
					property.validator(Validator
							.isNull(getValidationMessage(property, a.message(), Validator.ValidationMessage.NULL)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + Null.class.getName() + "]");
				});
				// not null
				property.getAnnotation(NotNull.class).ifPresent(a -> {
					property.validator(Validator.notNull(
							getValidationMessage(property, a.message(), Validator.ValidationMessage.NOT_NULL)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + NotNull.class.getName() + "]");
				});
				// min
				property.getAnnotation(Min.class).ifPresent(a -> {
					property.validator(Validator.min(Long.valueOf(a.value()).doubleValue(),
							getValidationMessage(property, a.message(), Validator.ValidationMessage.MIN)));
					LOGGER.debug(
							() -> "BeanPropertyBeanValidationPostProcessor: added validator to property [" + property
									+ "] for constraint [" + Min.class.getName() + "] with value [" + a.value() + "]");
				});
				property.getAnnotation(DecimalMin.class).ifPresent(a -> {
					property.validator(Validator.min(new BigDecimal(a.value()).doubleValue(),
							getValidationMessage(property, a.message(), Validator.ValidationMessage.MIN)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + DecimalMin.class.getName() + "] with value ["
							+ a.value() + "]");
				});
				// max
				property.getAnnotation(Max.class).ifPresent(a -> {
					property.validator(Validator.max(Long.valueOf(a.value()).doubleValue(),
							getValidationMessage(property, a.message(), Validator.ValidationMessage.MAX)));
					LOGGER.debug(
							() -> "BeanPropertyBeanValidationPostProcessor: added validator to property [" + property
									+ "] for constraint [" + Max.class.getName() + "] with value [" + a.value() + "]");
				});
				property.getAnnotation(DecimalMax.class).ifPresent(a -> {
					property.validator(Validator.max(new BigDecimal(a.value()).doubleValue(),
							getValidationMessage(property, a.message(), Validator.ValidationMessage.MAX)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + DecimalMax.class.getName() + "] with value ["
							+ a.value() + "]");
				});
				// size
				property.getAnnotation(Size.class).ifPresent(a -> {
					property.validator(Validator.min(Integer.valueOf(a.min()).doubleValue(),
							getValidationMessage(property, a.message(), Validator.ValidationMessage.MIN)));
					property.validator(Validator.max(Integer.valueOf(a.max()).doubleValue(),
							getValidationMessage(property, a.message(), Validator.ValidationMessage.MAX)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + Size.class.getName() + "] with values [" + a.min() + "/"
							+ a.max() + "]");
				});
				// digits
				property.getAnnotation(Digits.class).ifPresent(a -> {
					((Builder) property).validator(Validator.digits(a.integer(), a.fraction(),
							getValidationMessage(property, a.message(), Validator.ValidationMessage.DIGITS)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + Digits.class.getName() + "] with values [" + a.integer()
							+ "/" + a.fraction() + "]");
				});
				// past
				property.getAnnotation(Past.class).ifPresent(a -> {
					((Builder) property).validator(Validator.past(false,
							getValidationMessage(property, a.message(), Validator.ValidationMessage.PAST)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + Past.class.getName() + "]");
				});
				// future
				property.getAnnotation(javax.validation.constraints.Future.class).ifPresent(a -> {
					((Builder) property).validator(Validator.future(false,
							getValidationMessage(property, a.message(), Validator.ValidationMessage.FUTURE)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + Future.class.getName() + "]");
				});
				// pattern
				property.getAnnotation(Pattern.class).ifPresent(a -> {
					((Builder) property).validator(Validator.pattern(a.regexp(),
							getValidationMessage(property, a.message(), Validator.ValidationMessage.PATTERN),
							convertPatternFlags(a.flags())));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + Pattern.class.getName() + "] with value [" + a.regexp()
							+ "]");
				});

				// ------- EXTENSIONS

				// not empty
				property.getAnnotation(NotEmpty.class).ifPresent(a -> {
					((Builder) property).validator(Validator.notEmpty(
							getValidationMessage(property, a.message(), Validator.ValidationMessage.NOT_EMPTY)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + NotEmpty.class.getName() + "]");
				});
				// not blank
				property.getAnnotation(NotBlank.class).ifPresent(a -> {
					((Builder) property).validator(Validator.notBlank(
							getValidationMessage(property, a.message(), Validator.ValidationMessage.NOT_BLANK)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + NotBlank.class.getName() + "]");
				});
				// not negative
				property.getAnnotation(NotNegative.class).ifPresent(a -> {
					((Builder) property).validator(Validator.notNegative(
							getValidationMessage(property, a.message(), Validator.ValidationMessage.NOT_NEGATIVE)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + NotNegative.class.getName() + "]");
				});
				// email
				property.getAnnotation(Email.class).ifPresent(a -> {
					((Builder) property).validator(Validator
							.email(getValidationMessage(property, a.message(), Validator.ValidationMessage.EMAIL)));
					LOGGER.debug(() -> "BeanPropertyBeanValidationPostProcessor: added validator to property ["
							+ property + "] for constraint [" + Email.class.getName() + "]");
				});

			} catch (Exception e) {
				throw new BeanIntrospectionException(
						"Failed to configure property validation for property [" + property.fullName() + "]", e);
			}

		} else {

			LOGGER.debug(() -> "Bean validation API not found, ignoring constraint annotations");

		}

		return property;
	}

	private static Localizable getValidationMessage(final Builder<?> property, String message,
			Validator.ValidationMessage defaultValidationMessage) {
		// check ValidationMessage annotation
		if (property.getAnnotation(ValidationMessage.class).isPresent()) {
			ValidationMessage vm = property.getAnnotation(ValidationMessage.class).get();
			return Localizable.builder().message(vm.message())
					.messageCode(AnnotationUtils.getStringValue(vm.messageCode())).build();
		}
		// get default message
		String defaultMessage = null;
		String messageCode = null;
		if (message.startsWith("{") && message.endsWith("}")) {
			messageCode = message.substring(1, message.length() - 1);
			if (messageCode.length() == 0) {
				messageCode = null;
				defaultMessage = "[INVALID]";
			}
		} else {
			defaultMessage = message;
		}
		if (defaultMessage == null && defaultValidationMessage != null) {
			defaultMessage = defaultValidationMessage.getMessage();
		}
		return Localizable.builder().message(defaultMessage).messageCode(messageCode).build();
	}

	private static PatternFlag[] convertPatternFlags(Flag[] flags) {
		if (flags != null && flags.length > 0) {
			PatternFlag[] pfs = new PatternFlag[flags.length];
			for (int i = 0; i < flags.length; i++) {
				pfs[i] = PatternFlag.fromFlag(flags[i].getValue());
			}
		}
		return new PatternFlag[0];
	}

}
