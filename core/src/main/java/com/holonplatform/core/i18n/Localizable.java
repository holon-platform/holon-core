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
package com.holonplatform.core.i18n;

import com.holonplatform.core.internal.i18n.LocalizableMessage.MessageBuilder;

/**
 * Provides message code, arguments and default value to obtain a localized message.
 * 
 * @since 5.0.0
 * 
 * @see LocalizationContext
 */
public interface Localizable {

	/**
	 * Get the message code to use to obtain a localized message
	 * @return Message code
	 */
	String getMessageCode();

	/**
	 * Get the default message to use if a {@link #getMessageCode()} is not available or a localized message which
	 * corresponds to the message code cannot be found or no localization handler is available for message translation.
	 * @return Default message
	 */
	String getMessage();

	/**
	 * Optional arguments to use for message localization.
	 * <p>
	 * By default this method returns <code>null</code>. Override it to provide message localization arguments.
	 * </p>
	 * <p>
	 * Arguments resolution process is dependend from concrete localization handler. For example, a predefined argument
	 * placeholder character may be used to define arguments substitution positions within the localized message.
	 * </p>
	 * @return Message localization arguments, or <code>null</code> if none
	 */
	default Object[] getMessageArguments() {
		return null;
	}

	// Builder

	/**
	 * Get a builder to create a {@link Localizable} instance
	 * @return Localizable builder
	 */
	static LocalizableBuilder builder() {
		return new MessageBuilder();
	}

	/**
	 * {@link Localizable} builder
	 */
	public interface LocalizableBuilder extends Builder<LocalizableBuilder> {

		/**
		 * Build the Localizable
		 * @return Localizable instance
		 */
		Localizable build();

	}

	/**
	 * Base interface for {@link Localizable} building
	 * 
	 * @param <B> Concrete builder type
	 */
	public interface Builder<B extends Builder<B>> {

		/**
		 * Set the message code to use to obtain a localized message
		 * @param messageCode Message code to set
		 * @return this
		 */
		B messageCode(String messageCode);

		/**
		 * Set the default message to use if a {@link #getMessageCode()} is not available or a localized message which
		 * corresponds to the message code cannot be found or no localization handler is available for message
		 * translation.
		 * @param defaultMessage Default message to set
		 * @return this
		 */
		B message(String defaultMessage);

		/**
		 * Set the arguments to use for message localization.
		 * <p>
		 * Arguments resolution process is dependend from concrete localization handler. For example, a predefined
		 * argument placeholder character may be used to define arguments substitution positions within the localized
		 * message.
		 * </p>
		 * @param arguments Message localization arguments
		 * @return this
		 */
		B messageArguments(Object... arguments);

		/**
		 * Set the message localization attributes cloning them from given {@link Localizable}.
		 * <p>
		 * NOTE: current message localization attributes (message, message code and attributes) will be overriden by the
		 * given <code>localizable</code> attributes values.
		 * </p>
		 * @param localizable The {@link Localizable} instance form which to clone the message localization attributes
		 *        (not null)
		 * @return this
		 */
		B message(Localizable localizable);

	}

	/**
	 * Exception thrown for localization related errors.
	 */
	public class LocalizationException extends RuntimeException {

		private static final long serialVersionUID = -2985914284382251319L;

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public LocalizationException(String message) {
			super(message);
		}

		/**
		 * Constructor with nested exception
		 * @param cause Nested exception
		 */
		public LocalizationException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public LocalizationException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
