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
package com.holonplatform.core.internal.utils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

/**
 * Utility methods for test purposes.
 * 
 * @since 5.0.0
 */
public final class TestUtils implements Serializable {

	private static final long serialVersionUID = -5706500413665856724L;

	private TestUtils() {
	}

	/**
	 * Check utility class well defined: is final, has only one private constructor and all methods are static
	 * @param clazz Class to check
	 */
	public static void checkUtilityClass(final Class<?> clazz) {
		try {
			Assertions.assertTrue(Modifier.isFinal(clazz.getModifiers()), "class must be final");
			Assertions.assertEquals(1, clazz.getDeclaredConstructors().length, "There must be only one constructor");
			final Constructor<?> constructor = clazz.getDeclaredConstructor();
			if (constructor.isAccessible() || !Modifier.isPrivate(constructor.getModifiers())) {
				Assertions.fail("constructor is not private");
			}
			constructor.setAccessible(true);
			constructor.newInstance();
			constructor.setAccessible(false);
			for (final Method method : clazz.getMethods()) {
				if (!Modifier.isStatic(method.getModifiers()) && method.getDeclaringClass().equals(clazz)) {
					Assertions.fail("there exists a non-static method:" + method);
				}
			}
		} catch (SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Java synthetic enum methods test for code coverage
	 * @param <E> Enum type
	 * @param enm Enum o check
	 */
	@SuppressWarnings("rawtypes")
	public static <E extends Enum> void checkEnum(final Class<E> enm) {
		E[] es = enm.getEnumConstants();

		if (es == null || es.length == 0) {
			Assertions.fail("enum class has no constants: " + enm);
			return;
		}

		try {
			final Method valuesOf = enm.getMethod("valueOf", String.class);
			java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {
				@Override
				public Void run() {
					valuesOf.setAccessible(true);
					return null;
				}
			});
			valuesOf.invoke(null, es[0].name());
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Test an expected exception is thrown using a {@link Callable} operation
	 * @param exceptionClass Expected exception
	 * @param operation Operation to execute
	 */
	public static void expectedException(Class<? extends Throwable> exceptionClass, Executable operation) {
		Assertions.assertThrows(exceptionClass, operation);
	}

	public static void assertInstanceOf(Object object, Class<?> type) {
		if (!type.isInstance(object)) {
			Assertions.fail("Expected object type [" + type + "] but got type ["
					+ ((object == null) ? "NULL" : object.getClass().getName() + "]"));
		}
	}

}
