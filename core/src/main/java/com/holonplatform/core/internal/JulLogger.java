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
package com.holonplatform.core.internal;

import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * {@link Logger} implementation using java.util.logging
 *
 * @since 5.0.0
 */
public class JulLogger implements Logger {

	private static final long serialVersionUID = -2304539770710171900L;

	private final java.util.logging.Logger logger;

	/**
	 * Constructor
	 * @param loggerName Logger name (not null)
	 */
	public JulLogger(String loggerName) {
		super();
		ObjectUtils.argumentNotNull(loggerName, "Logger name must be not null");
		logger = java.util.logging.Logger.getLogger(loggerName);
	}

	/**
	 * Get the Logger
	 * @return the logger
	 */
	protected java.util.logging.Logger getLogger() {
		return logger;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.Logger#log(com.holonplatform.core.internal.Logger.Level, java.lang.String)
	 */
	@Override
	public void log(Level level, String message) {
		ObjectUtils.argumentNotNull(level, "Log level must be not null");
		switch (level) {
		case DEBUG:
			logger.fine(message);
			break;
		case ERROR:
			logger.severe(message);
			break;
		case INFORMATION:
			logger.info(message);
			break;
		case WARNING:
			logger.warning(message);
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.Logger#log(com.holonplatform.core.internal.Logger.Level, java.lang.String,
	 * java.lang.Throwable)
	 */
	@Override
	public void log(Level level, String message, Throwable throwable) {
		ObjectUtils.argumentNotNull(level, "Log level must be not null");
		switch (level) {
		case DEBUG:
			logger.log(java.util.logging.Level.FINE, message, throwable);
			break;
		case ERROR:
			logger.log(java.util.logging.Level.SEVERE, message, throwable);
			break;
		case INFORMATION:
			logger.log(java.util.logging.Level.INFO, message, throwable);
			break;
		case WARNING:
			logger.log(java.util.logging.Level.WARNING, message, throwable);
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.Logger#isEnabled(com.holonplatform.core.internal.Logger.Level)
	 */
	@Override
	public boolean isEnabled(Level level) {
		if (level != null) {
			switch (level) {
			case DEBUG:
				return logger.isLoggable(java.util.logging.Level.FINE);
			case ERROR:
				return logger.isLoggable(java.util.logging.Level.SEVERE);
			case INFORMATION:
				return logger.isLoggable(java.util.logging.Level.INFO);
			case WARNING:
				return logger.isLoggable(java.util.logging.Level.WARNING);
			default:
				break;
			}
		}
		return false;
	}

}
