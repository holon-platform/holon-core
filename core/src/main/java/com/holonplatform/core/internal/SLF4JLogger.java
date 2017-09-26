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

import org.slf4j.LoggerFactory;

import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * SLF4J {@link Logger} implementation.
 *
 * @since 5.0.0
 */
public class SLF4JLogger implements Logger {

	private static final long serialVersionUID = -3497380531643222410L;

	private final org.slf4j.Logger logger;

	/**
	 * Constructor
	 * @param loggerName Logger name (not null)
	 */
	public SLF4JLogger(String loggerName) {
		super();
		ObjectUtils.argumentNotNull(loggerName, "Logger name must be not null");
		logger = LoggerFactory.getLogger(loggerName);
	}

	/**
	 * Get the SLF4J logger.
	 * @return the logger
	 */
	protected org.slf4j.Logger getLogger() {
		return logger;
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
				return logger.isDebugEnabled();
			case ERROR:
				return logger.isErrorEnabled();
			case INFORMATION:
				return logger.isInfoEnabled();
			case WARNING:
				return logger.isWarnEnabled();
			default:
				break;
			}
		}
		return false;
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
			logger.debug(message);
			break;
		case ERROR:
			logger.error(message);
			break;
		case INFORMATION:
			logger.info(message);
			break;
		case WARNING:
			logger.warn(message);
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
			logger.debug(message, throwable);
			break;
		case ERROR:
			logger.error(message, throwable);
			break;
		case INFORMATION:
			logger.info(message, throwable);
			break;
		case WARNING:
			logger.warn(message, throwable);
			break;
		default:
			break;
		}
	}

}
