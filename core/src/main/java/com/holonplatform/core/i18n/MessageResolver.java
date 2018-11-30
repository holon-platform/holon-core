/*
 * Copyright 2016-2018 Axioma srl.
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

import java.io.Serializable;
import java.util.Locale;
import java.util.Optional;

/**
 * Messages localization resolver, with message arguments support.
 *
 * @since 5.2.0
 */
public interface MessageResolver extends Serializable {

	/**
	 * Get the localized message which corresponds to given message localization <code>code</code>, if available, using
	 * the given <code>locale</code>.
	 * @param locale The {@link Locale} for which to obtain the message localization (not null)
	 * @param code The message localization code (not null)
	 * @param arguments Optional message localization arguments
	 * @return Optional localized message
	 */
	Optional<String> getMessage(Locale locale, String code, Object... arguments);

	/**
	 * Get the localized message which corresponds to given message localization <code>code</code>, using the given
	 * <code>locale</code>.
	 * @param locale The {@link Locale} for which to obtain the message localization (not null)
	 * @param code The message localization code (not null)
	 * @param defaultMessage The default message to use if the message localization is not available (not null)
	 * @param arguments Optional message localization arguments
	 * @return The localized message, or the <code>defaultMessage</code> if the message localization is not available
	 */
	String getMessage(Locale locale, String code, String defaultMessage, Object... arguments);

}
