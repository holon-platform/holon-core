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
package com.holonplatform.core.internal;

import java.io.Serializable;
import java.util.function.Supplier;

import com.holonplatform.core.internal.utils.ClassUtils;

/**
 * Logger service.
 * 
 * @since 5.0.0
 */
public interface Logger extends Serializable {

	/**
	 * Whether QueryDSL is available from classpath of current ClassLoader
	 */
	public static final boolean SLF4J_PRESENT = ClassUtils.isPresent("org.slf4j.LoggerFactory",
			ClassUtils.getDefaultClassLoader());

	/**
	 * Log levels
	 */
	public enum Level {

		/**
		 * Information
		 */
		INFORMATION,

		/**
		 * Warning
		 */
		WARNING,

		/**
		 * Error
		 */
		ERROR,

		/**
		 * Debug
		 */
		DEBUG;

	}

	/**
	 * Log a message
	 * @param level Log level (not null)
	 * @param message Message to log
	 */
	void log(Level level, String message);

	/**
	 * Log a message and a {@link Throwable} exception.
	 * @param level Log level (not null)
	 * @param message Message to log
	 * @param throwable Exception to log
	 */
	void log(Level level, String message, Throwable throwable);

	/**
	 * Check whether given log level is enabled
	 * @param level Level to check
	 * @return <code>true</code> if enabled, <code>false</code> otherwise
	 */
	boolean isEnabled(Level level);

	/**
	 * Log a {@link Level#INFORMATION} type message.
	 * @param message Message to log
	 */
	default void info(String message) {
		log(Level.INFORMATION, message);
	}

	/**
	 * Log a {@link Level#INFORMATION} type message and exception.
	 * @param message Message to log
	 * @param throwable Exception to log
	 */
	default void info(String message, Throwable throwable) {
		log(Level.INFORMATION, message);
	}

	/**
	 * Log a {@link Level#WARNING} type message.
	 * @param message Message to log
	 */
	default void warn(String message) {
		log(Level.WARNING, message);
	}

	/**
	 * Log a {@link Level#WARNING} type message and exception.
	 * @param message Message to log
	 * @param throwable Exception to log
	 */
	default void warn(String message, Throwable throwable) {
		log(Level.WARNING, message);
	}

	/**
	 * Log a {@link Level#ERROR} type message.
	 * @param message Message to log
	 */
	default void error(String message) {
		log(Level.ERROR, message);
	}

	/**
	 * Log a {@link Level#ERROR} type message and exception.
	 * @param message Message to log
	 * @param throwable Exception to log
	 */
	default void error(String message, Throwable throwable) {
		log(Level.ERROR, message);
	}

	/**
	 * Log a {@link Level#DEBUG} type message, if debug log level is enabled.
	 * @param messageSupplier Message supplier
	 */
	default void debug(final Supplier<String> messageSupplier) {
		if (isEnabled(Level.DEBUG)) {
			log(Level.DEBUG, messageSupplier.get());
		}
	}

	/**
	 * Log a {@link Level#DEBUG} type message and exception, if debug log level is enabled.
	 * @param messageSupplier Message supplier
	 * @param throwable Exception to log
	 */
	default void debug(final Supplier<String> messageSupplier, final Throwable throwable) {
		if (isEnabled(Level.DEBUG)) {
			log(Level.DEBUG, messageSupplier.get(), throwable);
		}
	}

	/**
	 * Create a default logger instance.
	 * @param loggerName Logger name (not null)
	 * @return New logger
	 */
	static Logger create(String loggerName) {
		return SLF4J_PRESENT ? new SLF4JLogger(loggerName) : new JulLogger(loggerName);
	}

}
