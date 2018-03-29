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
		
		// test wrong logger name
		Logger logger2 = Logger.create("com.acme.core.test.boh");
		assertNotNull(logger2);
		logger2.info("test logger2");
		
		Logger logger3 = Logger.create("com.acme.core.test.boh");
		assertNotNull(logger3);
		logger3.info("test logger3");

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
			
			logger.debug(() -> "hello" , new IllegalArgumentException());
		});

	}

}
