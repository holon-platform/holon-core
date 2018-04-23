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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.holonplatform.core.exceptions.TypeMismatchException;

/**
 * Utility class to convert java types.
 *
 * @since 5.0.0
 */
public final class ConversionUtils implements Serializable {

	private static final long serialVersionUID = 2228952913980538703L;

	/**
	 * UTF-8 charset name
	 */
	public static final String UTF8_ENCODING = "UTF-8";

	private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

	private static final Set<String> BOOLEAN_TRUE_VALUES = new HashSet<>(4);
	private static final Set<String> BOOLEAN_FALSE_VALUES = new HashSet<>(4);

	static {
		BOOLEAN_TRUE_VALUES.add("true");
		BOOLEAN_TRUE_VALUES.add("on");
		BOOLEAN_TRUE_VALUES.add("yes");
		BOOLEAN_TRUE_VALUES.add("1");

		BOOLEAN_FALSE_VALUES.add("false");
		BOOLEAN_FALSE_VALUES.add("off");
		BOOLEAN_FALSE_VALUES.add("no");
		BOOLEAN_FALSE_VALUES.add("0");
	}

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private ConversionUtils() {
	}

	/**
	 * Convert an {@link Iterable} to a {@link List} which contains all Iterable elements.
	 * @param <T> Type of the elements in the list
	 * @param iterable Iterable to convert
	 * @return List of Iterable elements. If given <code>iterable</code> is <code>null</code>, an empty list is returned
	 */
	public static <T> List<T> iterableAsList(Iterable<T> iterable) {
		if (iterable != null) {
			if (iterable instanceof List) {
				return (List<T>) iterable;
			}
			List<T> list = new ArrayList<>();
			iterable.forEach(list::add);
			return list;
		}
		return Collections.emptyList();
	}

	/**
	 * Convert given {@link Enumeration} into a {@link Stream}.
	 * @param <T> Enumeration elements type
	 * @param e Enumeration to convert
	 * @return Stream on enumeration
	 */
	public static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() {
			@Override
			public T next() {
				return e.nextElement();
			}

			@Override
			public boolean hasNext() {
				return e.hasMoreElements();
			}
		}, Spliterator.ORDERED), false);
	}

	/**
	 * Try to convert given <code>value</code> to required Enum type, using ordinal or constant name according to value
	 * type.
	 * @param <T> Enum type
	 * @param type Enum type
	 * @param value Value to convert
	 * @return Value converted to Enum type, or <code>null</code> if given value was null
	 * @throws IllegalArgumentException Failed to convert to Enum type
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends Enum<T>> T convertEnumValue(Class<T> type, Object value) throws IllegalArgumentException {
		if (type == null) {
			throw new IllegalArgumentException("Type class must not be null");
		}

		Class<?> enumType = type;
		while (enumType != null && !enumType.isEnum()) {
			enumType = enumType.getSuperclass();
		}
		if (enumType == null) {
			throw new IllegalArgumentException("The target type " + type + " does not refer to an enum");
		}

		Class<Enum> enm = (Class<Enum>) enumType;

		if (value != null) {
			if (TypeUtils.isEnum(value.getClass())) {
				try {
					return (T) value;
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				}
			} else if (TypeUtils.isNumber(value.getClass())) {
				Integer ordinal = convertNumberToTargetClass((Number) value, int.class);
				Enum[] enmValues = enm.getEnumConstants();
				if (ordinal == null || ordinal.intValue() >= enmValues.length) {
					throw new IllegalArgumentException("Unable to convert to required Enum type " + type
							+ ": ordinal value " + ordinal + " is out of range");
				}
				return (T) enmValues[ordinal.intValue()];
			} else if (TypeUtils.isString(value.getClass())) {
				return (T) Enum.valueOf(enm, (String) value);
			} else {
				throw new IllegalArgumentException(
						"Unable to convert type " + value.getClass() + " to required Enum type");
			}
		}
		return null;
	}

	/**
	 * Try to converted a String value to given <code>type</code>.
	 * <p>
	 * Supported types are: Characters, Booleans, Numbers and Enums.
	 * </p>
	 * @param <T> Type to obtain
	 * @param str String to convert
	 * @param type Expected conversion type
	 * @return Converted string
	 * @throws IllegalArgumentException Conversion failed
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T convertStringValue(String str, Class<T> type) throws IllegalArgumentException {
		if (str != null && str.length() > 0) {

			if (type == null) {
				throw new IllegalArgumentException("Type class must not be null");
			}

			String value = str.trim();

			if (TypeUtils.isString(type)) {
				return (T) value;
			}

			if (TypeUtils.isCharacter(type)) {
				if (value.length() > 1) {
					throw new IllegalArgumentException(
							"Can only convert a String with length of 1 to a Character: " + value);
				}
				return (T) Character.valueOf(value.charAt(0));
			}

			if (TypeUtils.isBoolean(type)) {
				String bv = value.toLowerCase();
				if (BOOLEAN_TRUE_VALUES.contains(bv)) {
					return (T) Boolean.TRUE;
				} else if (BOOLEAN_FALSE_VALUES.contains(bv)) {
					return (T) Boolean.FALSE;
				} else {
					throw new IllegalArgumentException("Invalid boolean value '" + value + "'");
				}
			}

			if (TypeUtils.isEnum(type)) {
				Class<?> enumType = type;
				while (enumType != null && !enumType.isEnum()) {
					enumType = enumType.getSuperclass();
				}
				if (enumType == null) {
					throw new IllegalArgumentException("The target type " + type + " does not refer to an enum");
				}
				final Class<Enum> enm = (Class<Enum>) enumType;
				return (T) Enum.valueOf(enm, value);
			}

			if (TypeUtils.isNumber(type)) {
				return (T) parseNumber(value, (Class<? extends Number>) type);
			}

			throw new IllegalArgumentException("Unsupported String conversion class: " + type);

		}
		return null;
	}

	/**
	 * Parse the given String value into a {@link Number} instance of the given target class. Supports numbers in hex
	 * format (with leading "0x", "0X", or "#") as well.
	 * @param <T> Number type
	 * @param value the String to convert
	 * @param targetClass the target class to parse into
	 * @return the parsed number
	 * @throws IllegalArgumentException If parsing failed
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T parseNumber(String value, Class<T> targetClass) {

		if (value == null) {
			throw new IllegalArgumentException("Sring value must not be null");
		}
		if (targetClass == null) {
			throw new IllegalArgumentException("Target class must not be null");
		}

		String trimmed = FormatUtils.trimAll(value);

		if (TypeUtils.isByte(targetClass)) {
			return (T) (FormatUtils.isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed));
		} else if (TypeUtils.isShort(targetClass)) {
			return (T) (FormatUtils.isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed));
		} else if (TypeUtils.isInteger(targetClass)) {
			return (T) (FormatUtils.isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed));
		} else if (TypeUtils.isLong(targetClass)) {
			return (T) (FormatUtils.isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed));
		} else if (BigInteger.class == targetClass) {
			return (T) (FormatUtils.isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed));
		} else if (TypeUtils.isFloat(targetClass)) {
			return (T) Float.valueOf(trimmed);
		} else if (TypeUtils.isDouble(targetClass)) {
			return (T) Double.valueOf(trimmed);
		} else if (BigDecimal.class == targetClass || Number.class == targetClass) {
			return (T) new BigDecimal(trimmed);
		} else {
			throw new IllegalArgumentException(
					"Cannot convert String [" + value + "] to target class [" + targetClass.getName() + "]");
		}
	}

	/**
	 * Try to convert to convert Number type classes.
	 * @param <T> Number type
	 * @param number Number to convert
	 * @param targetClass Expected conversion type
	 * @return Converted number
	 * @throws IllegalArgumentException Conversion failed
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass)
			throws IllegalArgumentException {

		if (number == null) {
			throw new IllegalArgumentException("Number must not be null");
		}
		if (targetClass == null) {
			throw new IllegalArgumentException("Target class must not be null");
		}

		if (targetClass.isInstance(number)) {
			return (T) number;
		}

		else if (Byte.class == targetClass || byte.class == targetClass) {
			long value = number.longValue();
			if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
				throw new IllegalArgumentException("Could not convert number [" + number + "] of type ["
						+ number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
			}
			return (T) Byte.valueOf(number.byteValue());
		} else if (Short.class == targetClass || short.class == targetClass) {
			long value = number.longValue();
			if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
				throw new IllegalArgumentException("Could not convert number [" + number + "] of type ["
						+ number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
			}
			return (T) Short.valueOf(number.shortValue());
		} else if (TypeUtils.isInteger(targetClass)) {
			long value = number.longValue();
			if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Could not convert number [" + number + "] of type ["
						+ number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
			}
			return (T) Integer.valueOf(number.intValue());
		} else if (TypeUtils.isLong(targetClass)) {
			BigInteger bigInt = null;
			if (number instanceof BigInteger) {
				bigInt = (BigInteger) number;
			} else if (number instanceof BigDecimal) {
				bigInt = ((BigDecimal) number).toBigInteger();
			}
			if (bigInt != null && (bigInt.longValue() < Long.MIN_VALUE || bigInt.longValue() > Long.MAX_VALUE)) {
				throw new IllegalArgumentException("Could not convert number [" + number + "] of type ["
						+ number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
			}
			return (T) Long.valueOf(number.longValue());
		} else if (BigInteger.class == targetClass) {
			if (number instanceof BigDecimal) {
				return (T) ((BigDecimal) number).toBigInteger();
			} else {
				return (T) BigInteger.valueOf(number.longValue());
			}
		} else if (TypeUtils.isFloat(targetClass)) {
			return (T) Float.valueOf(number.floatValue());
		} else if (TypeUtils.isDouble(targetClass)) {
			return (T) Double.valueOf(number.doubleValue());
		} else if (TypeUtils.isBigDecimal(targetClass)) {
			return (T) new BigDecimal(number.toString());
		} else {
			throw new IllegalArgumentException("Could not convert number [" + number + "] of type ["
					+ number.getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]");
		}
	}

	private static BigInteger decodeBigInteger(String value) {
		int radix = 10;
		int index = 0;
		boolean negative = false;
		if (value.startsWith("-")) {
			negative = true;
			index++;
		}
		// radix
		if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
			index += 2;
			radix = 16;
		} else if (value.startsWith("#", index)) {
			index++;
			radix = 16;
		} else if (value.startsWith("0", index) && value.length() > 1 + index) {
			index++;
			radix = 8;
		}
		BigInteger result = new BigInteger(value.substring(index), radix);
		return (negative ? result.negate() : result);
	}

	/**
	 * Convert a {@link Calendar} value into a {@link LocalDate}.
	 * @param calendar Calendar to convert (may be null)
	 * @return Converted {@link LocalDate}, or <code>null</code> if given calendar was <code>null</code>
	 */
	public static LocalDate toLocalDate(Calendar calendar) {
		if (calendar != null) {
			return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
					calendar.get(Calendar.DAY_OF_MONTH));
		}
		return null;
	}

	/**
	 * Convert a {@link Date} value into a {@link LocalDate}, using default calendar.
	 * @param date Date to convert (may be null)
	 * @return Converted {@link LocalDate}, or <code>null</code> if given date was <code>null</code>
	 */
	public static LocalDate toLocalDate(Date date) {
		if (date != null) {
			final Calendar c = Calendar.getInstance();
			c.setTime(date);
			return LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		}
		return null;
	}

	/**
	 * Convert a {@link LocalDate} value into a {@link Date}, using default calendar.
	 * @param date Date to convert (may be null)
	 * @return Converted {@link Date}, or <code>null</code> if given date was <code>null</code>
	 */
	public static Date fromLocalDate(LocalDate date) {
		if (date != null) {
			final Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, date.getYear());
			c.set(Calendar.MONTH, date.getMonthValue() - 1);
			c.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
			c.set(Calendar.HOUR, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTime();
		}
		return null;
	}

	/**
	 * Convert a {@link Calendar} value into a {@link LocalDateTime}.
	 * @param calendar Calendar to convert (may be null)
	 * @return Converted {@link LocalDateTime}, or <code>null</code> if given calendar was <code>null</code>
	 */
	public static LocalDateTime toLocalDateTime(Calendar calendar) {
		if (calendar != null) {
			return LocalDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
					calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
					(int) TimeUnit.MILLISECONDS.toNanos(calendar.get(Calendar.MILLISECOND)));
		}
		return null;
	}

	/**
	 * Convert a {@link Date} value into a {@link LocalDateTime}, using default calendar.
	 * @param dateTime Date to convert (may be null)
	 * @return Converted {@link LocalDateTime}, or <code>null</code> if given date was <code>null</code>
	 */
	public static LocalDateTime toLocalDateTime(Date dateTime) {
		if (dateTime != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(dateTime);
			return LocalDateTime.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),
					(int) TimeUnit.MILLISECONDS.toNanos(c.get(Calendar.MILLISECOND)));
		}
		return null;
	}

	/**
	 * Convert a {@link LocalDateTime} value into a {@link Date}, using default calendar.
	 * @param dateTime Date/time to convert (may be null)
	 * @return Converted {@link Date}, or <code>null</code> if given date was <code>null</code>
	 */
	public static Date fromLocalDateTime(LocalDateTime dateTime) {
		if (dateTime != null) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, dateTime.getYear());
			c.set(Calendar.MONTH, dateTime.getMonthValue() - 1);
			c.set(Calendar.DAY_OF_MONTH, dateTime.getDayOfMonth());
			c.set(Calendar.HOUR_OF_DAY, dateTime.getHour());
			c.set(Calendar.MINUTE, dateTime.getMinute());
			c.set(Calendar.SECOND, dateTime.getSecond());
			c.set(Calendar.MILLISECOND, (int) TimeUnit.NANOSECONDS.toMillis(dateTime.getNano()));
			return c.getTime();
		}
		return null;
	}

	/**
	 * Convert a {@link Date} value into a {@link LocalTime}, using default calendar.
	 * @param date Date to convert (may be null)
	 * @return Converted {@link LocalTime}, or <code>null</code> if given date was <code>null</code>
	 */
	public static LocalTime toLocalTime(Date date) {
		if (date != null) {
			final Calendar c = Calendar.getInstance();
			c.setTime(date);
			return LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),
					(int) TimeUnit.MILLISECONDS.toNanos(c.get(Calendar.MILLISECOND)));
		}
		return null;
	}

	/**
	 * Convert given {@link InputStream} to a bytes array
	 * @param is InputStream to convert
	 * @return Array of bytes of given stream, or <code>null</code> if stream was null
	 * @throws IOException Error in stream conversion
	 */
	public static byte[] convertInputStreamToBytes(InputStream is) throws IOException {
		if (is != null) {
			try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
				int nRead;
				byte[] data = new byte[1024];
				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}
				buffer.flush();
				return buffer.toByteArray();
			}
		}
		return null;
	}

	/**
	 * Convert given String into bytes using given <code>encoding</code>
	 * @param source String to convert
	 * @param encoding Encoding charset
	 * @return Converted bytes
	 * @throws UnsupportedEncodingException Given encoding (charset name) is not supported
	 */
	public static byte[] toBytes(String source, String encoding) throws UnsupportedEncodingException {
		return (source != null) ? source.getBytes(encoding) : null;
	}

	/**
	 * Convert given String into bytes using UTF-8 default encoding
	 * @param source String to convert
	 * @return Converted bytes
	 */
	public static byte[] toBytes(String source) {
		try {
			return (source != null) ? source.getBytes(UTF8_ENCODING) : null;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Convert given char array into bytes using UTF-8 default encoding
	 * @param source Char array to convert
	 * @return Converted bytes
	 */
	public static byte[] toBytes(char[] source) {
		try {
			return (source != null) ? toBytes(new String(source), UTF8_ENCODING) : null;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Convert a byte array into hexadecimal characters String.
	 * @param bytes Bytes to convert
	 * @return The array hexadecimal reprentation, or <code>null</code> if array was null
	 */
	public static String bytesToHex(byte[] bytes) {
		if (bytes != null) {
			char[] hexChars = new char[bytes.length * 2];
			for (int j = 0; j < bytes.length; j++) {
				int v = bytes[j] & 0xFF;
				hexChars[j * 2] = HEX_CHARS[v >>> 4];
				hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
			}
			return new String(hexChars);
		}
		return null;
	}

	/**
	 * Flush the contents of a {@link Reader} into a String.
	 * @param reader Reader to flush (not null)
	 * @param closeReader Whether to close the Reader after it has been read
	 * @return A String with the charachters read from given <code>reader</code>
	 * @throws IOException Read error
	 */
	public static String readerToString(Reader reader, boolean closeReader) throws IOException {
		ObjectUtils.argumentNotNull(reader, "Reader must be not null");
		char[] arr = new char[8 * 1024];
		StringBuilder buffer = new StringBuilder();
		int numCharsRead;
		while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
			buffer.append(arr, 0, numCharsRead);
		}
		if (closeReader) {
			reader.close();
		}
		return buffer.toString();
	}

	/**
	 * Try to convert given value to target type, if a default conversion logic is available.
	 * @param <T> Target type
	 * @param value Value to convert
	 * @param targetType Target type (not null)
	 * @return Converted value or <code>null</code> if value was <code>null</code>
	 * @throws TypeMismatchException If a conversion logic is not available
	 * @since 5.1.0
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T convert(Object value, Class<T> targetType) throws TypeMismatchException {
		ObjectUtils.argumentNotNull(targetType, "Target type must be not null");
		if (value != null) {

			if (TypeUtils.isAssignable(value.getClass(), targetType)) {
				return (T) value;
			}

			// enum
			if (TypeUtils.isEnum(targetType)) {
				return (T) ConversionUtils.convertEnumValue((Class<Enum>) targetType, value);
			}

			// number
			if (TypeUtils.isNumber(targetType) && TypeUtils.isNumber(value.getClass())) {
				return (T) ConversionUtils.convertNumberToTargetClass((Number) value, (Class<Number>) targetType);
			}

			// date and times
			if (Date.class.isAssignableFrom(value.getClass())) {
				if (LocalDate.class.isAssignableFrom(targetType)) {
					return (T) ConversionUtils.toLocalDate((Date) value);
				}
				if (LocalDateTime.class.isAssignableFrom(targetType)) {
					return (T) ConversionUtils.toLocalDateTime((Date) value);
				}
				if (LocalTime.class.isAssignableFrom(targetType)) {
					return (T) ConversionUtils.toLocalTime((Date) value);
				}
			}

			if (Timestamp.class.isAssignableFrom(value.getClass())) {
				if (LocalDateTime.class.isAssignableFrom(targetType)) {
					return (T) ((Timestamp) value).toLocalDateTime();
				}
				if (LocalDate.class.isAssignableFrom(targetType)) {
					return (T) ((Timestamp) value).toLocalDateTime().toLocalDate();
				}
				if (LocalTime.class.isAssignableFrom(targetType)) {
					return (T) ((Timestamp) value).toLocalDateTime().toLocalTime();
				}
				if (java.util.Date.class.isAssignableFrom(targetType)) {
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(((Timestamp) value).getTime());
					return (T) c.getTime();
				}
			}

			if (Time.class.isAssignableFrom(value.getClass())) {
				if (LocalTime.class.isAssignableFrom(targetType)) {
					return (T) ((Time) value).toLocalTime();
				}
			}

			// String to Reader
			if (TypeUtils.isString(value.getClass()) && Reader.class.isAssignableFrom(targetType)) {
				return (T) new StringReader((String) value);
			}

			throw new TypeMismatchException(
					"Cannot convert value type [" + value.getClass() + "] to type [" + targetType + "]");
		}

		return null;
	}

}
