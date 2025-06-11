/*
 * Copyright 2016-2018 Axioma srl.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.internal.property.NumericBooleanConverter;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertyValueConverter;
import com.holonplatform.core.property.PropertyValueConverter.PropertyConversionException;
import com.holonplatform.core.property.TemporalProperty;
import com.holonplatform.core.test.TestProperty.TestEnum;

public class TestPropertyValueConverter {

	@Test
	public void testPropertyConverters() {

		NumericBooleanConverter<Integer> pc = new NumericBooleanConverter<>(int.class);

		PathProperty<Boolean> p = PathProperty.create("test", boolean.class).converter(pc);

		assertNotNull(p.getConverter());

		Boolean cnv = pc.fromModel(null, p);
		assertNotNull(cnv);
		assertEquals(Boolean.FALSE, cnv);

		cnv = pc.fromModel(Integer.valueOf(0), p);
		assertNotNull(cnv);
		assertEquals(Boolean.FALSE, cnv);

		cnv = pc.fromModel(Integer.valueOf(1), p);
		assertNotNull(cnv);
		assertEquals(Boolean.TRUE, cnv);

		Integer mod = pc.toModel(Boolean.FALSE, p);
		assertNotNull(mod);
		assertEquals(Integer.valueOf(0), mod);

		mod = pc.toModel(Boolean.TRUE, p);
		assertNotNull(mod);
		assertEquals(Integer.valueOf(1), mod);

		NumericBooleanConverter<Long> pc2 = new NumericBooleanConverter<>(Long.class);

		PathProperty<Boolean> p2 = PathProperty.create("test", boolean.class).converter(pc2);

		Long lm = pc2.toModel(Boolean.FALSE, p2);
		assertNotNull(lm);
		assertEquals(Long.valueOf(0), lm);

		lm = pc2.toModel(Boolean.TRUE, p2);
		assertNotNull(lm);
		assertEquals(Long.valueOf(1), lm);

	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testNumericBooleanConverter() {

		NumericBooleanConverter<Long> pc2 = new NumericBooleanConverter<>(Long.class);

		PathProperty<Boolean> p2 = PathProperty.create("test", boolean.class)
				.converter(PropertyValueConverter.numericBoolean(Long.class));

		Long lm = pc2.toModel(Boolean.FALSE, p2);
		assertNotNull(lm);
		assertEquals(Long.valueOf(0), lm);

		lm = pc2.toModel(Boolean.TRUE, p2);
		assertNotNull(lm);
		assertEquals(Long.valueOf(1), lm);

		PathProperty<Boolean> p = PathProperty.create("test", boolean.class).converter(Integer.class,
				v -> v != null && v > 0, v -> v ? 1 : 0);

		assertEquals(Integer.valueOf(0), p.getConvertedValue(false));
		assertEquals(Integer.valueOf(1), p.getConvertedValue(true));

		@SuppressWarnings("unchecked")
		PropertyBox box = PropertyBox.builder(p).set((Property) p, Integer.valueOf(1)).build();
		assertTrue(box.getValue(p));

	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testEnumConverter() {

		final PathProperty<TestEnum> ENMP = PathProperty.create("testenum", TestEnum.class)
				.converter(PropertyValueConverter.enumByOrdinal());

		@SuppressWarnings("unchecked")
		PropertyBox eb = PropertyBox.builder(ENMP).set((Property) ENMP, Integer.valueOf(1)).build();
		assertEquals(TestEnum.B, eb.getValue(ENMP));

		assertEquals(Integer.valueOf(0), ENMP.getConvertedValue(TestEnum.A));

		final PathProperty<TestEnum> ENMP2 = PathProperty.create("testenum", TestEnum.class)
				.converter(PropertyValueConverter.enumByName());

		@SuppressWarnings("unchecked")
		PropertyBox eb2 = PropertyBox.builder(ENMP2).set((Property) ENMP2, "C").build();
		assertEquals(TestEnum.C, eb2.getValue(ENMP2));

		assertEquals("A", ENMP2.getConvertedValue(TestEnum.A));

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLocalDateConverter() {

		final Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 9);
		c.set(Calendar.MONTH, 2);
		c.set(Calendar.YEAR, 1979);
		c.set(Calendar.HOUR_OF_DAY, 18);
		c.set(Calendar.MINUTE, 30);
		c.set(Calendar.SECOND, 15);
		c.set(Calendar.MILLISECOND, 0);

		TemporalProperty<LocalDate> DP = TemporalProperty.localDate("dp");

		PropertyValueConverter<LocalDate, Date> cnv = PropertyValueConverter.localDate();

		LocalDate ld = cnv.fromModel(c.getTime(), DP);
		assertNotNull(ld);
		assertEquals(9, ld.getDayOfMonth());
		assertEquals(Month.MARCH, ld.getMonth());
		assertEquals(1979, ld.getYear());

		Date model = cnv.toModel(ld, DP);
		assertNotNull(model);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(model);

		assertEquals(9, c2.get(Calendar.DAY_OF_MONTH));
		assertEquals(2, c2.get(Calendar.MONTH));
		assertEquals(1979, c2.get(Calendar.YEAR));
		assertEquals(0, c2.get(Calendar.HOUR));
		assertEquals(0, c2.get(Calendar.MINUTE));
		assertEquals(0, c2.get(Calendar.SECOND));
		assertEquals(0, c2.get(Calendar.MILLISECOND));

		DP = TemporalProperty.localDate("dp").converter(PropertyValueConverter.localDate());

		PropertyBox pb = PropertyBox.builder(DP).set((Property) DP, c.getTime()).build();

		ld = pb.getValue(DP);
		assertNotNull(ld);
		assertEquals(9, ld.getDayOfMonth());
		assertEquals(Month.MARCH, ld.getMonth());
		assertEquals(1979, ld.getYear());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testLocalDateTimeConverter() {

		final Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 9);
		c.set(Calendar.MONTH, 2);
		c.set(Calendar.YEAR, 1979);
		c.set(Calendar.HOUR_OF_DAY, 18);
		c.set(Calendar.MINUTE, 30);
		c.set(Calendar.SECOND, 15);
		c.set(Calendar.MILLISECOND, 0);

		TemporalProperty<LocalDateTime> DP = TemporalProperty.localDateTime("dp");

		PropertyValueConverter<LocalDateTime, Date> cnv = PropertyValueConverter.localDateTime();

		LocalDateTime ld = cnv.fromModel(c.getTime(), DP);
		assertNotNull(ld);
		assertEquals(9, ld.getDayOfMonth());
		assertEquals(Month.MARCH, ld.getMonth());
		assertEquals(1979, ld.getYear());
		assertEquals(18, ld.getHour());
		assertEquals(30, ld.getMinute());
		assertEquals(15, ld.getSecond());

		Date model = cnv.toModel(ld, DP);
		assertNotNull(model);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(model);

		assertEquals(9, c2.get(Calendar.DAY_OF_MONTH));
		assertEquals(2, c2.get(Calendar.MONTH));
		assertEquals(1979, c2.get(Calendar.YEAR));
		assertEquals(18, c2.get(Calendar.HOUR_OF_DAY));
		assertEquals(30, c2.get(Calendar.MINUTE));
		assertEquals(15, c2.get(Calendar.SECOND));
		assertEquals(0, c2.get(Calendar.MILLISECOND));

		DP = TemporalProperty.localDateTime("dp").converter(PropertyValueConverter.localDateTime());

		PropertyBox pb = PropertyBox.builder(DP).set((Property) DP, c.getTime()).build();

		ld = pb.getValue(DP);
		assertNotNull(ld);
		assertEquals(9, ld.getDayOfMonth());
		assertEquals(Month.MARCH, ld.getMonth());
		assertEquals(1979, ld.getYear());
		assertEquals(18, ld.getHour());
		assertEquals(30, ld.getMinute());
		assertEquals(15, ld.getSecond());

	}

	@Test
	public void testPropertyConverterErrors() {
		assertThrows(PropertyConversionException.class, () -> {
			NumericBooleanConverter<InvalidNumberClass> pc = new NumericBooleanConverter<>(InvalidNumberClass.class);
			pc.toModel(Boolean.TRUE, null);
		});
	}

	@SuppressWarnings("serial")
	private class InvalidNumberClass extends Number {

		@Override
		public int intValue() {
			return 0;
		}

		@Override
		public long longValue() {
			return 0;
		}

		@Override
		public float floatValue() {
			return 0;
		}

		@Override
		public double doubleValue() {
			return 0;
		}

	}

}
