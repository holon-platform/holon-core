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
package com.holonplatform.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.holonplatform.core.Validator.ValidationException;
import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.internal.utils.TestUtils;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.temporal.TemporalType;
import com.holonplatform.core.test.data.TestBeanPropertyBean;
import com.holonplatform.core.test.data.TestEnum;
import com.holonplatform.core.test.data.TestEnum2;

public class TestBeanIntrospector {

	@Test
	public void testProperties() {

		BeanPropertySet<TestBeanPropertyBean> set = BeanIntrospector.get().getPropertySet(TestBeanPropertyBean.class);

		assertTrue(set.getProperty("name").isPresent());
		assertTrue(set.getProperty("numbool").isPresent());
		assertTrue(set.getProperty("enm").isPresent());
		assertTrue(set.getProperty("enmOrdinal").isPresent());
		assertTrue(set.getProperty("date").isPresent());
		assertTrue(set.getProperty("lng").isPresent());
		assertTrue(set.getProperty("notneg").isPresent());
		assertTrue(set.getProperty("intval").isPresent());
		assertTrue(set.getProperty("legacyDate").isPresent());

		assertFalse(set.getProperty("ignore").isPresent());

		assertEquals("TheName", set.getProperty("name").get().getMessage());

		assertNotNull(set.getProperty("numbool").get().getConverter());
		assertNotNull(set.getProperty("enm").get().getConverter());
		assertNotNull(set.getProperty("date").get().getConverter());

		assertEquals(TemporalType.DATE_TIME,
				set.getProperty("legacyDate").get().getConfiguration().getTemporalType().orElse(null));

		assertEquals(boolean.class, set.getProperty("numbool").get().getType());
		assertEquals(TestEnum2.class, set.getProperty("enm").get().getType());
		assertEquals(TestEnum.class, set.getProperty("enmOrdinal").get().getType());
		assertEquals(LocalDate.class, set.getProperty("date").get().getType());

		PathProperty<Boolean> property1 = set.requireProperty("numbool");
		assertEquals(Integer.valueOf(1), property1.getConvertedValue(Boolean.TRUE));

		PathProperty<TestEnum2> property2 = set.requireProperty("enm");
		assertEquals(TestEnum2.B.getStringValue(), property2.getConvertedValue(TestEnum2.B));

		PathProperty<LocalDate> property3 = set.requireProperty("date");
		Date date = (Date) property3.getConvertedValue(LocalDate.of(1979, Month.MARCH, 9));
		assertNotNull(date);

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		assertEquals(1979, c.get(Calendar.YEAR));
		assertEquals(2, c.get(Calendar.MONTH));
		assertEquals(9, c.get(Calendar.DAY_OF_MONTH));

		set.requireProperty("name").validate("xxx");

		TestUtils.expectedException(ValidationException.class, () -> set.requireProperty("name").validate(null));

		TestUtils.expectedException(ValidationException.class, () -> set.requireProperty("name").validate("   "));

		try {
			set.requireProperty("name").validate(null);
		} catch (ValidationException e) {
			assertEquals("Name is required; Name is empty", e.getMessage());
		}

		set.requireProperty("numbool").validate(0);
		set.requireProperty("numbool").validate(1);

		TestUtils.expectedException(ValidationException.class, () -> set.requireProperty("intval").validate(-1));
		TestUtils.expectedException(ValidationException.class, () -> set.requireProperty("intval").validate(11));

		try {
			set.requireProperty("intval").validate(11);
		} catch (ValidationException e) {
			assertEquals("0-10 range", e.getMessage());
			assertEquals("test-mc", e.getMessageCode());
		}

		set.requireProperty("lng").validate(7L);

		TestUtils.expectedException(ValidationException.class, () -> set.requireProperty("lng").validate(3L));

		try {
			set.requireProperty("lng").validate(0L);
		} catch (ValidationException e) {
			assertEquals("Must be 7", e.getMessage());
		}

		TestUtils.expectedException(ValidationException.class, () -> set.requireProperty("notneg").validate(-1));

		assertTrue(set.requireProperty("notneg").getConfiguration().hasNotNullParameter("k1"));
		assertEquals("v1", set.requireProperty("notneg").getConfiguration().getParameter("k1", String.class, null));

		assertTrue(set.requireProperty("notneg").getConfiguration().hasNotNullParameter("k2"));
		assertEquals("v2", set.requireProperty("notneg").getConfiguration().getParameter("k2", String.class, null));

	}

	@Test
	public void testPropertyReadWrite() {

		BeanPropertySet<TestBeanPropertyBean> set = BeanIntrospector.get().getPropertySet(TestBeanPropertyBean.class);

		TestBeanPropertyBean instance = new TestBeanPropertyBean();

		set.write("name", "test", instance);
		set.write("lng", 7L, instance);
		set.write("notneg", 1, instance);
		set.write("numbool", Boolean.TRUE, instance);
		set.write("enm", TestEnum2.B, instance);
		set.write("enmOrdinal", TestEnum.ONE, instance);

		assertEquals("test", set.read("name", instance));
		assertEquals(Boolean.TRUE, set.read("numbool", instance));
		assertEquals(TestEnum2.B, set.read("enm", instance));
		assertEquals(TestEnum.ONE, set.read("enmOrdinal", instance));
		assertEquals(Long.valueOf(7), set.read("lng", instance));
		assertEquals(Integer.valueOf(1), set.read("notneg", instance));

		PathProperty<Integer> modelEnum = PathProperty.create("enmOrdinal", Integer.class);
		PropertyBox pb = PropertyBox.create(modelEnum);

		set.read(pb, instance);

	}

	@Test
	public void testPropertyBox() {

		BeanPropertySet<TestBeanPropertyBean> set = BeanIntrospector.get().getPropertySet(TestBeanPropertyBean.class);

		TestBeanPropertyBean instance = new TestBeanPropertyBean();
		instance.setName("test");
		instance.setNotneg(1);
		instance.setNumbool(true);
		instance.setLng(7L);
		instance.setEnm(TestEnum2.B);

		// read

		PropertyBox pb = set.read(instance);

		assertNotNull(pb);

		assertTrue(pb.containsValue(set.requireProperty("name")));
		assertTrue(pb.containsValue(set.requireProperty("numbool")));
		assertTrue(pb.containsValue(set.requireProperty("enm")));
		assertTrue(pb.containsValue(set.requireProperty("lng")));
		assertTrue(pb.containsValue(set.requireProperty("notneg")));

		assertEquals("test", pb.getValue(set.requireProperty("name")));
		assertEquals(Boolean.TRUE, pb.getValue(set.requireProperty("numbool")));
		assertEquals(TestEnum2.B, pb.getValue(set.requireProperty("enm")));
		assertEquals(Long.valueOf(7), pb.getValue(set.requireProperty("lng")));
		assertEquals(Integer.valueOf(1), pb.getValue(set.requireProperty("notneg")));

		PropertyBox box = PropertyBox.create(set.requireProperty("name"), set.requireProperty("enm"));
		box = set.read(box, instance);

		assertEquals("test", box.getValue(set.requireProperty("name")));
		assertEquals(TestEnum2.B, box.getValue(set.requireProperty("enm")));

		// write

		PropertyBox wb = PropertyBox.builder(set).set(set.requireProperty("name"), "test2")
				.set(set.requireProperty("numbool"), Boolean.FALSE).set(set.requireProperty("enm"), TestEnum2.A)
				.set(set.requireProperty("lng"), 7L).build();

		set.write(wb, instance);

	}

}
