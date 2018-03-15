package com.holonplatform.core.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.Logger.Level;
import com.holonplatform.core.internal.utils.TestUtils;

/**
 * {@link Logger} unit tests.
 * 
 * @since 5.1.0
 */
public class TestLogger {

	private final static String TEST_LOGGER_NAME = "com.holonplatform.core.test.logger";

	@Test
	public void testSetup() {

		assertTrue(Logger.SLF4J_PRESENT);

		Logger logger = Logger.create(TEST_LOGGER_NAME);
		assertNotNull(logger);

	}

	@Test
	public void testMethods() {

		Logger logger = Logger.create(TEST_LOGGER_NAME);
		assertNotNull(logger);

		logger.log(Level.INFORMATION, "test logger");
		logger.info("test logger");
		logger.warn("test logger");
		logger.error("test logger");

		logger.debug(() -> "test" + " logger");

		TestUtils.expectedException(IllegalArgumentException.class, () -> {
			logger.debug(null);
			logger.debug(null, new Exception());
		});

	}

}
