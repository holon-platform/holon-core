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
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Stack;

import org.apache.commons.lang3.ClassUtils;

/**
 * Utilities for types inspection and management.
 * 
 * @since 2.0.0
 */
public final class TypeUtils implements Serializable {

	private static final long serialVersionUID = -5065058539442356460L;

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private TypeUtils() {
	}

	/**
	 * CHeck if given object is a {@link Class}
	 * @param obj Object to check
	 * @return <code>true</code> if given object is a class
	 */
	public static boolean isClass(Object obj) {
		return obj instanceof Class;
	}

	/**
	 * Check if a class is a {@link CharSequence}
	 * @param type Type to check
	 * @return <code>true</code> if it is a CharSequence
	 */
	public static boolean isCharSequence(Class<?> type) {
		return type != null && CharSequence.class.isAssignableFrom(type);
	}

	/**
	 * Check if a class is a {@link String}
	 * @param type Type to check
	 * @return <code>true</code> if it is a string
	 */
	public static boolean isString(Class<?> type) {
		return type != null && String.class.isAssignableFrom(type);
	}

	/**
	 * Check if a class is a {@link Character}
	 * @param type Type to check
	 * @return <code>true</code> if it is a character
	 */
	public static boolean isCharacter(Class<?> type) {
		return type != null && Character.class.isAssignableFrom(type) || char.class == type;
	}

	/**
	 * Check if a class is a {@link Date}
	 * @param type Type to check
	 * @return <code>true</code> if it is a date
	 */
	public static boolean isDate(Class<?> type) {
		return type != null && Date.class.isAssignableFrom(type);
	}

	/**
	 * Check if a class is a {@link Calendar}
	 * @param type Type to check
	 * @return <code>true</code> if it is a Calendar
	 */
	public static boolean isCalendar(Class<?> type) {
		return type != null && Calendar.class.isAssignableFrom(type);
	}

	/**
	 * Check if a class is a {@link Temporal}
	 * @param type Type to check
	 * @return <code>true</code> if it is a Temporal type
	 */
	public static boolean isTemporal(Class<?> type) {
		return type != null && Temporal.class.isAssignableFrom(type);
	}

	/**
	 * Check if a class is a Local {@link Temporal}
	 * @param type Type to check
	 * @return <code>true</code> if it is a {@link LocalDate}, a {@link LocalTime} or a {@link LocalDateTime}
	 */
	public static boolean isLocalTemporal(Class<?> type) {
		return type != null && (LocalDate.class.isAssignableFrom(type) || LocalDateTime.class.isAssignableFrom(type)
				|| LocalTime.class.isAssignableFrom(type));
	}

	/**
	 * Check if a class is a {@link Temporal}, a {@link Calendar} or {@link Date} type
	 * @param type Type to check
	 * @return <code>true</code> if it is a Temporal, Calendar or Date type
	 */
	public static boolean isTemporalOrCalendar(Class<?> type) {
		return type != null && (Temporal.class.isAssignableFrom(type) || Calendar.class.isAssignableFrom(type)
				|| Date.class.isAssignableFrom(type));
	}

	/**
	 * Check if a class is a number ({@link Number} subclass or numeric primitive class)
	 * @param type Type to check
	 * @return <code>true</code> if it is a number
	 */
	public static boolean isNumber(Class<?> type) {
		return type != null && (Number.class.isAssignableFrom(type) || int.class == type || long.class == type
				|| double.class == type || float.class == type || short.class == type);
	}

	/**
	 * Check if a class is an integer (primitive or wrapper)
	 * @param type Type to check
	 * @return <code>true</code> if it is an integer
	 */
	public static boolean isInteger(Class<?> type) {
		return type != null && Integer.class.isAssignableFrom(type) || int.class == type;
	}

	/**
	 * Check if a class is a long (primitive or wrapper)
	 * @param type Type to check
	 * @return <code>true</code> if it is a long
	 */
	public static boolean isLong(Class<?> type) {
		return type != null && Long.class.isAssignableFrom(type) || long.class == type;
	}

	/**
	 * Check if a class is a short (primitive or wrapper)
	 * @param type Type to check
	 * @return <code>true</code> if it is a short
	 */
	public static boolean isShort(Class<?> type) {
		return type != null && Short.class.isAssignableFrom(type) || short.class == type;
	}

	/**
	 * Check if a class is a byte (primitive or wrapper)
	 * @param type Type to check
	 * @return <code>true</code> if it is a byte
	 */
	public static boolean isByte(Class<?> type) {
		return type != null && Byte.class.isAssignableFrom(type) || byte.class == type;
	}

	/**
	 * Check if a class is a double (primitive or wrapper)
	 * @param type Type to check
	 * @return <code>true</code> if it is a double
	 */
	public static boolean isDouble(Class<?> type) {
		return type != null && Double.class.isAssignableFrom(type) || double.class == type;
	}

	/**
	 * Check if a class is a float (primitive or wrapper)
	 * @param type Type to check
	 * @return <code>true</code> if it is a float
	 */
	public static boolean isFloat(Class<?> type) {
		return type != null && Float.class.isAssignableFrom(type) || float.class == type;
	}

	/**
	 * Check if a class is a BigDecimal
	 * @param type Type to check
	 * @return <code>true</code> if it is a BigDecimal
	 */
	public static boolean isBigDecimal(Class<?> type) {
		return type != null && BigDecimal.class.isAssignableFrom(type);
	}

	/**
	 * Check if a class is a boolean (primitive or wrapper)
	 * @param type Type to check
	 * @return <code>true</code> if it is a boolean
	 */
	public static boolean isBoolean(Class<?> type) {
		return type != null && Boolean.class.isAssignableFrom(type) || boolean.class == type;
	}

	/**
	 * Check if a class is an {@link Enum}
	 * @param type Type to check
	 * @return <code>true</code> if it is a Enum
	 */
	public static boolean isEnum(Class<?> type) {
		return type != null && Enum.class.isAssignableFrom(type);
	}

	/**
	 * Check if type is a primitive int
	 * @param cls Type to check
	 * @return <code>true</code> if primitive int
	 */
	public static boolean isPrimitiveInt(Class<?> cls) {
		return int.class == cls;
	}

	/**
	 * Check if type is a primitive long
	 * @param cls Type to check
	 * @return <code>true</code> if primitive long
	 */
	public static boolean isPrimitiveLong(Class<?> cls) {
		return long.class == cls;
	}

	/**
	 * Check if type is a primitive float
	 * @param cls Type to check
	 * @return <code>true</code> if primitive float
	 */
	public static boolean isPrimitiveFloat(Class<?> cls) {
		return float.class == cls;
	}

	/**
	 * Check if type is a primitive double
	 * @param cls Type to check
	 * @return <code>true</code> if primitive double
	 */
	public static boolean isPrimitiveDouble(Class<?> cls) {
		return double.class == cls;
	}

	/**
	 * Check if type is a primitive boolean
	 * @param cls Type to check
	 * @return <code>true</code> if primitive boolean
	 */
	public static boolean isPrimitiveBoolean(Class<?> cls) {
		return boolean.class == cls;
	}

	/**
	 * Check if type is an integer number (Long, Integer, Short or Byte, including primitives)
	 * @param cls Type to check
	 * @return <code>true</code> if integer number
	 */
	public static boolean isIntegerNumber(Class<?> cls) {
		return cls != null && (Integer.class.isAssignableFrom(cls) || Long.class.isAssignableFrom(cls)
				|| Short.class.isAssignableFrom(cls) || Byte.class.isAssignableFrom(cls) || cls == int.class
				|| cls == long.class || cls == short.class || cls == byte.class);
	}

	/**
	 * Check if type is a decimal number (Double, Float or BigDecimal, including primitives)
	 * @param cls Type to check
	 * @return <code>true</code> if decimal number
	 */
	public static boolean isDecimalNumber(Class<?> cls) {
		return cls != null && (Double.class.isAssignableFrom(cls) || Float.class.isAssignableFrom(cls)
				|| BigDecimal.class.isAssignableFrom(cls) || cls == double.class || cls == float.class);
	}

	/**
	 * If given <code>type</code> is a primitive type, returns the corresponding object wrapper type.
	 * @param <T> Actual type
	 * @param type Type to box
	 * @return If type is a primitive type, the corresponding object wrapper type is returned. Otherwise, returns the
	 *         given type.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> box(Class<T> type) {
		ObjectUtils.argumentNotNull(type, "Type to unbox must be not null");
		if (int.class == type)
			return (Class<T>) Integer.class;
		if (long.class == type)
			return (Class<T>) Long.class;
		if (short.class == type)
			return (Class<T>) Short.class;
		if (byte.class == type)
			return (Class<T>) Byte.class;
		if (double.class == type)
			return (Class<T>) Double.class;
		if (float.class == type)
			return (Class<T>) Float.class;
		if (char.class == type)
			return (Class<T>) Character.class;
		if (boolean.class == type)
			return (Class<T>) Boolean.class;
		return type;
	}

	/**
	 * Checks if one Class can be assigned to a variable of another Class.
	 * 
	 * <p>
	 * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this method takes into account widenings of
	 * primitive classes and <code>null</code>s.
	 * </p>
	 *
	 * <p>
	 * Primitive widenings allow an int to be assigned to a long, float or double. This method returns the correct
	 * result for these cases.
	 * </p>
	 *
	 * <p>
	 * <code>null</code> may be assigned to any reference type. This method will return <code>true</code> if
	 * <code>null</code> is passed in and the toClass is non-primitive.
	 * </p>
	 * 
	 * @param cls the Class to check, may be null
	 * @param toClass the Class to try to assign into, returns false if null
	 * @return <code>true</code> if assignment possible
	 */
	public static boolean isAssignable(Class<?> cls, Class<?> toClass) {
		return ClassUtils.isAssignable(cls, toClass, true);
	}

	/**
	 * Return the type parameter of a generic type.
	 * @param clazz subClass of <code>baseClass</code> to analyze.
	 * @param baseClass base class having the type parameter the value of which we need to retrieve
	 * @return the parameterized type value
	 */
	@SuppressWarnings("rawtypes")
	public static Type getTypeArgument(Class<?> clazz, Class<?> baseClass) {
		Stack<Type> superclasses = new Stack<>();
		Type currentType;
		Class<?> currentClass = clazz;

		if (clazz.getGenericSuperclass() == Object.class) {
			currentType = clazz;
			superclasses.push(currentType);
		} else {

			do {
				currentType = currentClass.getGenericSuperclass();
				superclasses.push(currentType);
				if (currentType instanceof Class) {
					currentClass = (Class) currentType;
				} else if (currentType instanceof ParameterizedType) {
					currentClass = (Class) ((ParameterizedType) currentType).getRawType();
				}
			} while (!currentClass.equals(baseClass));

		}

		// find which one supplies type argument and return it
		TypeVariable tv = baseClass.getTypeParameters()[0];
		while (!superclasses.isEmpty()) {
			currentType = superclasses.pop();

			if (currentType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) currentType;
				Class<?> rawType = (Class) pt.getRawType();
				int argIndex = Arrays.asList(rawType.getTypeParameters()).indexOf(tv);
				if (argIndex > -1) {
					Type typeArg = pt.getActualTypeArguments()[argIndex];
					if (typeArg instanceof TypeVariable) {
						// type argument is another type variable - look for the value of that
						// variable in subclasses
						tv = (TypeVariable) typeArg;
						continue;
					} else {
						// found the value - return it
						return typeArg;
					}
				}
			}

			// needed type argument not supplied - break and throw exception
			break;
		}
		throw new IllegalArgumentException(currentType + " does not specify a type parameter");
	}

	/**
	 * Get the underlying class of a {@link Type}, if available.
	 * @param type the type for which to obtain the class
	 * @return the type class
	 */
	public static Optional<Class<?>> getRawType(Type type) {
		if (type instanceof Class) {
			return Optional.of((Class<?>) type);
		} else if (type instanceof ParameterizedType) {
			return Optional.of((Class<?>) ((ParameterizedType) type).getRawType());
		} else if (type instanceof GenericArrayType) {
			return getRawType(((GenericArrayType) type).getGenericComponentType())
					.map(c -> Array.newInstance(c, 0).getClass());
		} else if (type instanceof TypeVariable) {
			final TypeVariable<?> typeVar = (TypeVariable<?>) type;
			if (typeVar.getBounds() != null && typeVar.getBounds().length > 0) {
				return getRawType(typeVar.getBounds()[0]);
			}
		}
		return Optional.empty();
	}

}
