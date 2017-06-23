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
package com.holonplatform.core.internal.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Class management and inspection utilities.
 *
 * @since 5.0.0
 */
public final class ClassUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private ClassUtils() {
	}

	/**
	 * Determine whether the Class identified by the supplied name is present and can be loaded.
	 * @param className The name of the class to check
	 * @param classLoader Class loader to use, <code>null</code> to use default
	 * @return <code>false</code> if either the class or one of its dependencies is not present or cannot be loaded,
	 *         <code>true</code> otherwise
	 */
	public static boolean isPresent(String className, ClassLoader classLoader) {
		try {
			forName(className, classLoader);
			return true;
		} catch (@SuppressWarnings("unused") Throwable ex) {
			// Class or one of its dependencies is not present...
			return false;
		}
	}

	/**
	 * Create class instance using given class name and {@link #getDefaultClassLoader()} ClassLoader
	 * @param name Class name
	 * @return Class instance
	 * @throws ClassNotFoundException Class definition with given name was not found
	 */
	public static Class<?> forName(String name) throws ClassNotFoundException {
		return forName(name, getDefaultClassLoader());
	}

	/**
	 * Create class instance using given class name and classloader
	 * @param name Class name
	 * @param classLoader Class loader to use, or <code>null</code> to use default
	 * @return Class instance
	 * @throws ClassNotFoundException Class definition with given name was not found
	 */
	public static Class<?> forName(String name, ClassLoader classLoader) throws ClassNotFoundException {
		assert name != null : "Class name must be not null";

		ClassLoader cl = classLoader;
		if (cl == null) {
			cl = getDefaultClassLoader();
		}
		return (cl != null ? cl.loadClass(name) : Class.forName(name));
	}

	/**
	 * Return the default ClassLoader: the thread context ClassLoader, if available; the ClassLoader that loaded the
	 * ClassUtils class will be used as fallback.
	 * @return Default ClassLoader
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (@SuppressWarnings("unused") Throwable ex) {
			// Cannot access thread context ClassLoader
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassUtils.class.getClassLoader();
			if (cl == null) {
				// null indicates the bootstrap ClassLoader
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (@SuppressWarnings("unused") Throwable ex) {
					// Cannot access system ClassLoader
				}
			}
		}
		return cl;
	}

	/**
	 * Check if a method is a JavaBean getter method
	 * @param method Method
	 * @return True if getter
	 */
	public static boolean isGetterMethod(Method method) {
		if (!method.getName().startsWith("get") && !method.getName().startsWith("is"))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (void.class.equals(method.getReturnType()))
			return false;
		return true;
	}

	/**
	 * Check if a method is a JavaBean setter method
	 * @param method Method
	 * @return True if setter
	 */
	public static boolean isSetterMethod(Method method) {
		if (!method.getName().startsWith("set"))
			return false;
		if (method.getParameterTypes().length != 1)
			return false;
		return true;
	}

	/**
	 * Load a {@link Properties} instance reading properties key/values from given file name. The file reference is
	 * obtained using default (current thread or system) {@link ClassLoader}.
	 * 
	 * <p>
	 * See {@link ClassLoader#getResource(String)} for informations about file name resolution strategy.
	 * </p>
	 * 
	 * @param filename File name to read
	 * @param lenient if <code>true</code>, when file is not found returns <code>null</code> without throwing any
	 *        exception
	 * @return {@link Properties} instance containing all the properties read from file, if any
	 * @throws IOException Error reading properties file
	 */
	public static Properties loadProperties(String filename, boolean lenient) throws IOException {
		return loadProperties(filename, ClassUtils.getDefaultClassLoader(), lenient);
	}

	/**
	 * Load a {@link Properties} instance reading properties key/values from given file name. The file reference is
	 * obtained using given {@link ClassLoader}.
	 * 
	 * <p>
	 * See {@link ClassLoader#getResource(String)} for informations about file name resolution strategy.
	 * </p>
	 * 
	 * @param filename File name to read
	 * @param classLoader ClassLoader to use
	 * @param lenient if <code>true</code>, when file is not found returns <code>null</code> without throwing any
	 *        exception
	 * @return {@link Properties} instance containing all the properties read from file, if any
	 * @throws IOException Error reading properties file
	 */
	public static Properties loadProperties(String filename, ClassLoader classLoader, boolean lenient)
			throws IOException {

		ObjectUtils.argumentNotNull(filename, "File name must be not null");

		ClassLoader cl = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();

		try (InputStream is = cl.getResourceAsStream(filename)) {
			if (is == null) {
				if (lenient) {
					return null;
				}
				throw new IOException("Properties file not found: " + filename);
			}
			Properties properties = new Properties();
			properties.load(is);
			return properties;
		}

	}

}
