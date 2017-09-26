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
package com.holonplatform.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.holonplatform.core.internal.utils.AnnotationUtils;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.internal.utils.FormatUtils;
import com.holonplatform.core.internal.utils.TestUtils;
import com.holonplatform.core.internal.utils.TypeUtils;
import com.holonplatform.core.test.data.TestClass;
import com.holonplatform.core.test.data.TestEnum;
import com.holonplatform.core.test.data.TestMetaAnnotated;
import com.holonplatform.core.test.data.TestMetaAnnotated2;
import com.holonplatform.core.test.data.TestMetaAnnotation;
import com.holonplatform.core.test.data.TestNested;

/**
 * Utility classes test
 */
public class TestUtilities {

	@Test
	public void testWellDefined() {
		TestUtils.checkUtilityClass(TestUtils.class);
		TestUtils.checkUtilityClass(AnnotationUtils.class);
		TestUtils.checkUtilityClass(ClassUtils.class);
		TestUtils.checkUtilityClass(ConversionUtils.class);
		TestUtils.checkUtilityClass(FormatUtils.class);
		TestUtils.checkUtilityClass(TypeUtils.class);
	}

	@Test
	public void testFormatUtils() {
		String str = FormatUtils.trimAll(null);
		assertNull(str);
		str = FormatUtils.trimAll("");
		assertEquals("", str);
		str = FormatUtils.trimAll(" a b c  ");
		assertEquals("abc", str);
	}

	@Test
	public void testAnnotationUtils() {
		String val = AnnotationUtils.getStringValue("");
		assertNull(val);
		val = AnnotationUtils.getStringValue(null);
		assertNull(val);

		List<TestMetaAnnotation> annotations = AnnotationUtils.getAnnotations(MetaTest1.class,
				TestMetaAnnotation.class);
		assertNotNull(annotations);
		assertEquals(1, annotations.size());
		assertEquals("mt1", annotations.get(0).value());

		annotations = AnnotationUtils.getAnnotations(MetaTest2.class, TestMetaAnnotation.class);
		assertNotNull(annotations);
		assertEquals(3, annotations.size());
		assertTrue(hasAnnotationValue(annotations, "mt2"));
		assertTrue(hasAnnotationValue(annotations, "mt3"));
		assertTrue(hasAnnotationValue(annotations, "mt4"));

		annotations = AnnotationUtils.getAnnotations(MetaTest3.class, TestMetaAnnotation.class);
		assertNotNull(annotations);
		assertEquals(2, annotations.size());
		assertTrue(hasAnnotationValue(annotations, "m1"));
		assertTrue(hasAnnotationValue(annotations, "m2"));

		annotations = AnnotationUtils.getAnnotations(MetaTest4.class, TestMetaAnnotation.class);
		assertNotNull(annotations);
		assertEquals(4, annotations.size());
		assertTrue(hasAnnotationValue(annotations, "m1"));
		assertTrue(hasAnnotationValue(annotations, "m2"));
		assertTrue(hasAnnotationValue(annotations, "mt5"));
		assertTrue(hasAnnotationValue(annotations, "mt6"));

		annotations = AnnotationUtils.getAnnotations(MetaTest5.class, TestMetaAnnotation.class);
		assertNotNull(annotations);
		assertEquals(4, annotations.size());
		assertTrue(hasAnnotationValue(annotations, "m1"));
		assertTrue(hasAnnotationValue(annotations, "m2"));
		assertTrue(hasAnnotationValue(annotations, "mx1"));
		assertTrue(hasAnnotationValue(annotations, "mx2"));

	}

	private static boolean hasAnnotationValue(List<TestMetaAnnotation> annotations, String value) {
		for (TestMetaAnnotation a : annotations) {
			if (a.value().equals(value)) {
				return true;
			}
		}
		return false;
	}

	@TestMetaAnnotation("mt1")
	public static class MetaTest1 {

	}

	@TestMetaAnnotation("mt2")
	@TestMetaAnnotation("mt3")
	@TestMetaAnnotation("mt4")
	public static class MetaTest2 {

	}

	@TestMetaAnnotated
	public static class MetaTest3 {

	}

	@TestMetaAnnotation("mt5")
	@TestMetaAnnotation("mt6")
	@TestMetaAnnotated
	public static class MetaTest4 {

	}

	@TestMetaAnnotated2
	public static class MetaTest5 {

	}

	@SuppressWarnings("boxing")
	@Test
	public void testConversionUtils() throws IOException {

		// iterable

		List<?> list = ConversionUtils.iterableAsList(null);
		assertNotNull(list);

		List<String> il = new LinkedList<>();
		il.add("a");
		il.add("b");

		List<String> cl = ConversionUtils.iterableAsList(il);
		assertNotNull(cl);
		assertEquals(2, cl.size());
		assertEquals("a", cl.get(0));
		assertEquals("b", cl.get(1));

		// strings

		String str = ConversionUtils.convertStringValue(null, String.class);
		assertNull(str);

		str = ConversionUtils.convertStringValue("", String.class);
		assertNull(str);

		str = ConversionUtils.convertStringValue("test", String.class);
		assertNotNull(str);
		assertEquals("test", str);

		Boolean bl = ConversionUtils.convertStringValue("true", Boolean.class);
		assertNotNull(bl);
		assertEquals(Boolean.TRUE, bl);

		bl = ConversionUtils.convertStringValue("false", Boolean.class);
		assertNotNull(bl);
		assertEquals(Boolean.FALSE, bl);

		Integer it = ConversionUtils.convertStringValue("3", Integer.class);
		assertNotNull(it);
		assertEquals(new Integer(3), it);

		Double dbl = ConversionUtils.convertStringValue("3.7", Double.class);
		assertNotNull(dbl);
		assertEquals(new Double(3.7), dbl);

		TestEnum en = ConversionUtils.convertStringValue("ONE", TestEnum.class);
		assertNotNull(en);
		assertEquals(TestEnum.ONE, en);

		char c = ConversionUtils.convertStringValue("c", char.class);
		assertNotNull(c);
		assertEquals('c', c);

		// enums

		TestEnum enm = ConversionUtils.convertEnumValue(TestEnum.class, TestEnum.ONE);
		assertNotNull(enm);
		assertEquals(TestEnum.ONE, enm);

		enm = ConversionUtils.convertEnumValue(TestEnum.class, 0);
		assertNotNull(enm);
		assertEquals(TestEnum.ONE, enm);

		enm = ConversionUtils.convertEnumValue(TestEnum.class, "ONE");
		assertNotNull(enm);
		assertEquals(TestEnum.ONE, enm);

		enm = ConversionUtils.convertEnumValue(TestEnum.class, null);
		assertNull(enm);

		// numbers

		it = ConversionUtils.parseNumber("3", Integer.class);
		assertNotNull(it);
		assertEquals(new Integer(3), it);

		Long lng = ConversionUtils.parseNumber("3600", long.class);
		assertNotNull(lng);
		assertEquals(new Long(3600), lng);

		dbl = ConversionUtils.parseNumber("3.7", Double.class);
		assertNotNull(dbl);
		assertEquals(new Double(3.7), dbl);

		Float fl = ConversionUtils.parseNumber("3.7", Float.class);
		assertNotNull(fl);
		assertEquals(new Float(3.7), fl);

		Byte bt = ConversionUtils.parseNumber("10", byte.class);
		assertNotNull(bt);

		Short sh = ConversionUtils.parseNumber("255", short.class);
		assertNotNull(sh);
		assertEquals(Short.valueOf((short) 255), sh);

		BigInteger bigint = BigInteger.valueOf(3);
		BigInteger ig = ConversionUtils.parseNumber("3", BigInteger.class);
		assertEquals(bigint, ig);

		ig = ConversionUtils.parseNumber("0x3", BigInteger.class);
		assertEquals(bigint, ig);

		ig = ConversionUtils.parseNumber("0X3", BigInteger.class);
		assertEquals(bigint, ig);

		ig = ConversionUtils.parseNumber("#3", BigInteger.class);
		assertEquals(bigint, ig);

		bigint = BigInteger.valueOf(-3);
		ig = ConversionUtils.parseNumber("-0x3", BigInteger.class);
		assertEquals(bigint, ig);

		long lg = ConversionUtils.convertNumberToTargetClass(new Integer(3), long.class);
		assertNotNull(lg);
		assertEquals(3L, lg);

		lg = ConversionUtils.convertNumberToTargetClass(new Long(3), long.class);
		assertNotNull(lg);
		assertEquals(3L, lg);

		lg = ConversionUtils.convertNumberToTargetClass(new BigInteger("3"), long.class);
		assertNotNull(lg);
		assertEquals(3L, lg);

		lg = ConversionUtils.convertNumberToTargetClass(new BigDecimal(3), long.class);
		assertNotNull(lg);
		assertEquals(3L, lg);

		BigInteger bi = ConversionUtils.convertNumberToTargetClass(3L, BigInteger.class);
		assertNotNull(bi);
		assertEquals(3, bi.intValue());

		bi = ConversionUtils.convertNumberToTargetClass(new BigDecimal(3), BigInteger.class);
		assertNotNull(bi);
		assertEquals(3, bi.intValue());

		float ft = ConversionUtils.convertNumberToTargetClass(new Integer(3), float.class);
		assertNotNull(ft);
		assertEquals(new Double(3), new Double(ft));

		Integer same = ConversionUtils.convertNumberToTargetClass(new Integer(3), Integer.class);
		assertNotNull(same);
		assertEquals(new Integer(3), same);

		byte b = ConversionUtils.convertNumberToTargetClass(new Integer(3), byte.class);
		assertNotNull(b);
		assertEquals(new Integer(3), new Integer(b));

		short shrt = ConversionUtils.convertNumberToTargetClass(new Integer(3), short.class);
		assertNotNull(shrt);
		assertEquals(new Integer(3), new Integer(shrt));

		double db = ConversionUtils.convertNumberToTargetClass(1, double.class);
		assertNotNull(db);
		assertEquals(1, (int) db);

		BigDecimal bd = ConversionUtils.convertNumberToTargetClass(1, BigDecimal.class);
		assertNotNull(bd);
		assertEquals(1, bd.intValue());

		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertNumberToTargetClass(new Integer(Short.MAX_VALUE + 1), short.class);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertNumberToTargetClass(new Integer(Short.MIN_VALUE - 1), short.class);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertNumberToTargetClass(new Integer(Byte.MAX_VALUE + 1), byte.class);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertNumberToTargetClass(new Integer(Byte.MIN_VALUE - 1), byte.class);
			}
		});

		// stream

		assertNull(ConversionUtils.convertInputStreamToBytes(null));

		byte[] bytes = new byte[] { 1, 2, 3 };

		assertTrue(Arrays.equals(bytes, ConversionUtils.convertInputStreamToBytes(new ByteArrayInputStream(bytes))));

	}

	@Test
	public void testConversionUtilsErrors() {
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertStringValue("test", null);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertStringValue("aa", char.class);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertStringValue("xxx", boolean.class);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertEnumValue(null, TestEnum.ONE);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertEnumValue(TestEnum.class, Boolean.TRUE);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertNumberToTargetClass(new Integer(3), null);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertNumberToTargetClass(null, long.class);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.convertStringValue("invalid", TestNested.class);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.parseNumber("invalid", null);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ConversionUtils.parseNumber(null, Integer.class);
			}
		});
	}

	@SuppressWarnings("boxing")
	@Test
	public void testTypeUtils() {

		assertTrue(TypeUtils.isClass(Object.class));

		Character cr = new Character('r');
		assertTrue(TypeUtils.isCharacter(cr.getClass()));

		assertTrue(TypeUtils.isBigDecimal(new BigDecimal(3).getClass()));
		assertTrue(TypeUtils.isDecimalNumber(new BigDecimal(3).getClass()));

		assertTrue(TypeUtils.isDouble(new Double(3).getClass()));

		assertTrue(TypeUtils.isBoolean(Boolean.TRUE.getClass()));

		assertTrue(TypeUtils.isLocalTemporal(LocalDate.of(1979, Month.MARCH, 9).getClass()));

		assertFalse(TypeUtils.isPrimitiveInt(null));
		assertFalse(TypeUtils.isPrimitiveLong(null));
		assertFalse(TypeUtils.isPrimitiveFloat(null));
		assertFalse(TypeUtils.isPrimitiveDouble(null));
		assertFalse(TypeUtils.isPrimitiveBoolean(null));
		assertFalse(TypeUtils.isDouble(null));
		assertFalse(TypeUtils.isLong(null));
		assertFalse(TypeUtils.isFloat(null));
		assertFalse(TypeUtils.isByte(null));
		assertFalse(TypeUtils.isShort(null));
		assertFalse(TypeUtils.isCharacter(null));
		assertFalse(TypeUtils.isInteger(null));
		assertFalse(TypeUtils.isDate(null));
		assertFalse(TypeUtils.isCalendar(null));
		assertFalse(TypeUtils.isTemporal(null));
		assertFalse(TypeUtils.isTemporalOrCalendar(null));
		assertFalse(TypeUtils.isString(null));
		assertFalse(TypeUtils.isEnum(null));
		assertFalse(TypeUtils.isBoolean(null));
		assertFalse(TypeUtils.isBigDecimal(null));
		assertFalse(TypeUtils.isDecimalNumber(null));
		assertFalse(TypeUtils.isNumber(null));

		Object o = 3;
		assertTrue(TypeUtils.isInteger(o.getClass()));

		o = 3f;
		assertTrue(TypeUtils.isFloat(o.getClass()));
		assertTrue(TypeUtils.isDecimalNumber(o.getClass()));

		o = 3d;
		assertTrue(TypeUtils.isDouble(o.getClass()));
		assertTrue(TypeUtils.isDecimalNumber(o.getClass()));

		assertTrue(TypeUtils.isDouble(new Double(2.7).getClass()));
		assertTrue(TypeUtils.isDecimalNumber(new Double(2.7).getClass()));

		Class<?> cl = int.class;
		assertTrue(TypeUtils.isPrimitiveInt(cl));
		assertTrue(TypeUtils.isNumber(cl));
		cl = double.class;
		assertTrue(TypeUtils.isPrimitiveDouble(cl));
		assertTrue(TypeUtils.isDecimalNumber(cl));
		assertTrue(TypeUtils.isNumber(cl));
		assertTrue(TypeUtils.isDouble(cl));
		cl = float.class;
		assertTrue(TypeUtils.isPrimitiveFloat(cl));
		assertTrue(TypeUtils.isDecimalNumber(cl));
		assertTrue(TypeUtils.isNumber(cl));
		cl = boolean.class;
		assertTrue(TypeUtils.isPrimitiveBoolean(cl));
		assertTrue(TypeUtils.isBoolean(cl));
		cl = long.class;
		assertTrue(TypeUtils.isPrimitiveLong(cl));
		assertTrue(TypeUtils.isLong(cl));
		assertTrue(TypeUtils.isNumber(cl));
		cl = short.class;
		assertTrue(TypeUtils.isNumber(cl));
		assertTrue(TypeUtils.isShort(cl));
		cl = byte.class;
		assertTrue(TypeUtils.isByte(cl));
		cl = char.class;
		assertTrue(TypeUtils.isCharacter(cl));

		assertTrue(TypeUtils.isByte(new Byte("127").getClass()));

	}

	@Test
	public void testClassUtils() throws ClassNotFoundException {
		boolean present = ClassUtils.isPresent("notpresent.class.name", getClass().getClassLoader());
		assertFalse(present);
		present = ClassUtils.isPresent("com.holonplatform.core.Context", getClass().getClassLoader());
		assertTrue(present);

		Class<?> tc = ClassUtils.forName("com.holonplatform.core.test.data.TestClass", getClass().getClassLoader());
		assertTrue(TypeUtils.isAssignable(TestClass.class, tc));

		tc = ClassUtils.forName("com.holonplatform.core.test.data.TestClass", null);
		assertTrue(TypeUtils.isAssignable(TestClass.class, tc));

		Method[] ms = TestClass.class.getDeclaredMethods();

		Method m = getMethodByName(ms, "getTest");
		assertTrue(ClassUtils.isGetterMethod(m));

		m = getMethodByName(ms, "isTestBoolean");
		assertTrue(ClassUtils.isGetterMethod(m));

		m = getMethodByName(ms, "getSomething");
		assertFalse(ClassUtils.isGetterMethod(m));

		m = getMethodByName(ms, "getNothing");
		assertFalse(ClassUtils.isGetterMethod(m));

		m = getMethodByName(ms, "setTest");
		assertTrue(ClassUtils.isSetterMethod(m));

		m = getMethodByName(ms, "setTestBoolean");
		assertTrue(ClassUtils.isSetterMethod(m));

		m = getMethodByName(ms, "setNothing");
		assertFalse(ClassUtils.isSetterMethod(m));

		m = getMethodByName(ms, "nothing");
		assertFalse(ClassUtils.isGetterMethod(m));

		m = getMethodByName(ms, "nothing");
		assertFalse(ClassUtils.isSetterMethod(m));

	}

	private static Method getMethodByName(Method[] methods, String name) {
		for (Method m : methods) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

}
