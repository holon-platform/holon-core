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

/**
 * Common Object handling utility class.
 * 
 * @since 5.0.0
 */
public final class ObjectUtils implements Serializable {

	private static final long serialVersionUID = 4394569694541465276L;

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private ObjectUtils() {
	}

	/**
	 * Checks that the specified method or constructor argument is not <code>null</code>.
	 * @param <T> Argument type
	 * @param argument Argument to check
	 * @param message Exception message if given <code>argument</code> is <code>null</code>
	 * @return <code>argument</code> if not <code>null</code>
	 * @throws IllegalArgumentException if given <code>argument</code> if <code>null</code>
	 */
	public static <T> T argumentNotNull(T argument, String message) {
		if (argument == null) {
			throw new IllegalArgumentException(message);
		}
		return argument;
	}

	/**
	 * Checks if a {@link CharSequence} is empty ("") or <code>null</code>.
	 * @param cs the CharSequence to check, may be <code>null</code>
	 * @return <code>true</code> if the CharSequence is empty or <code>null</code>
	 */
	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * Checks whether the given <code>array</code> contains the given <code>value</code>.
	 * @param <T> Value type
	 * @param array Array to check
	 * @param value Value to lookup
	 * @return <code>true</code> if given value is present in given array, <code>false</code> otherwise
	 */
	public static <T> boolean contains(T[] array, T value) {
		if (array != null && value != null) {
			for (T element : array) {
				if (value.equals(element)) {
					return true;
				}
			}
		}
		return false;
	}

}
