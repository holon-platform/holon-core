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
package com.holonplatform.core.internal.i18n;

import java.util.Arrays;

import com.holonplatform.core.i18n.Localizable;

/**
 * Default {@link Localizable} message implementation.
 *
 * @since 5.0.0
 */
public class LocalizableMessage implements Localizable {

	/*
	 * Message code
	 */
	private String messageCode;
	/*
	 * Default message
	 */
	private String message;
	/*
	 * Localization arguments
	 */
	private Object[] messageArguments;

	/**
	 * Default constructor
	 */
	public LocalizableMessage() {
		super();
	}

	/**
	 * Constructor
	 * @param messageCode Message localization code
	 * @param message Default message if message code not available or not localizable
	 * @param messageArguments Optional message localization arguments
	 */
	public LocalizableMessage(String messageCode, String message, Object... messageArguments) {
		super();
		this.messageCode = messageCode;
		this.message = message;
		this.messageArguments = messageArguments;
	}

	/**
	 * Set the message code to use to obtain a localized message
	 * @param messageCode the message code to set
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	/**
	 * Set the default message to use if a {@link #getMessageCode()} is not available or a localized message which
	 * corresponds to the message code cannot be found or no localization handler is available for message translation.
	 * @param message the default message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Set the arguments to use for message localization.
	 * <p>
	 * Arguments resolution process is dependend from concrete localization handler. For example, a predefined argument
	 * placeholder character may be used to define arguments substitution positions within the localized message.
	 * </p>
	 * @param messageArguments the message arguments to set
	 */
	public void setMessageArguments(Object[] messageArguments) {
		this.messageArguments = messageArguments;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocalizableMessage [messageCode=" + messageCode + ", message=" + message + ", messageArguments="
				+ Arrays.toString(messageArguments) + "]";
	}

	// Builder

	/**
	 * Default {@link LocalizableBuilder} implementation.
	 */
	public static class MessageBuilder implements LocalizableBuilder {

		private final LocalizableMessage instance = new LocalizableMessage();

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Localizable.Builder#messageCode(java.lang.String)
		 */
		@Override
		public MessageBuilder messageCode(String messageCode) {
			instance.setMessageCode(messageCode);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Localizable.Builder#message(java.lang.String)
		 */
		@Override
		public MessageBuilder message(String defaultMessage) {
			instance.setMessage(defaultMessage);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Localizable.Builder#messageArguments(java.lang.Object[])
		 */
		@Override
		public MessageBuilder messageArguments(Object... arguments) {
			instance.setMessageArguments(arguments);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.i18n.Localizable.Builder#build()
		 */
		@Override
		public Localizable build() {
			return instance;
		}

	}

}
