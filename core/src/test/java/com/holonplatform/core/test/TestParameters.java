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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.DefaultParameterSet;

/**
 * ParameterSet test
 */
public class TestParameters {

	@Test
	public void testParameterSet() {
		
		DefaultParameterSet ps = new DefaultParameterSet();

		assertFalse(ps.hasParameters());
		assertFalse(ps.hasParameter("test"));
		assertFalse(ps.hasNotNullParameter("test"));

		ps.addParameter("test", null);

		assertTrue(ps.hasParameters());
		assertFalse(ps.hasParameter("x"));

		assertTrue(ps.hasParameter("test"));
		assertFalse(ps.hasNotNullParameter("test"));

		ps.addParameter("test", "TEST");
		assertTrue(ps.hasNotNullParameter("test"));

		assertFalse(ps.getParameter("x").isPresent());

		Object value = ps.getParameter("test").orElse(null);
		assertNotNull(value);
		assertEquals("TEST", value);

		String sv = ps.getParameter("test", String.class).orElse(null);
		assertNotNull(sv);
		assertEquals("TEST", sv);
		
		sv = ps.getParameter("test", String.class, "DFT");
		assertNotNull(sv);
		assertEquals("TEST", sv);

		String dv = ps.getParameter("notExist", String.class, "DFT");
		assertNotNull(dv);
		assertEquals("DFT", dv);
		
		assertFalse(ps.getParameterIf("test", String.class, v -> v == null).isPresent());
		
		assertTrue(ps.getParameterIf("test", String.class, v -> v.length() > 3).isPresent());
		
		assertTrue(ps.hasParameterValue("test", "TEST"));
		assertFalse(ps.hasParameterValue("test", null));
		assertFalse(ps.hasParameterValue("test", "TESTX"));
		assertFalse(ps.hasParameterValue("testx", "TEST"));
		assertFalse(ps.hasParameterValue("testx", null));
		
		ps.addParameter("testx", null);
		
		assertTrue(ps.hasParameterValue("testx", null));
	}
	
	@Test
	public void testParameterSetConfigProperty() {
		
		final ConfigProperty<String> TEST = ConfigProperty.create("test", String.class);
		final ConfigProperty<String> TEST2 = ConfigProperty.create("x", String.class);
		final ConfigProperty<String> TEST3 = ConfigProperty.create("testx", String.class);
		final ConfigProperty<String> TEST4 = ConfigProperty.create("notExist", String.class);
		
		DefaultParameterSet ps = new DefaultParameterSet();

		assertFalse(ps.hasParameters());
		assertFalse(ps.hasParameter(TEST));
		assertFalse(ps.hasNotNullParameter(TEST));

		ps.addParameter(TEST.getKey(), null);

		assertTrue(ps.hasParameters());
		assertFalse(ps.hasParameter(TEST2));

		assertTrue(ps.hasParameter(TEST));
		assertFalse(ps.hasNotNullParameter(TEST));

		ps.addParameter(TEST.getKey(), "TEST");
		assertTrue(ps.hasNotNullParameter(TEST));

		assertFalse(ps.getParameter(TEST2).isPresent());

		Object value = ps.getParameter(TEST).orElse(null);
		assertNotNull(value);
		assertEquals("TEST", value);

		String sv = ps.getParameter(TEST).orElse(null);
		assertNotNull(sv);
		assertEquals("TEST", sv);
		
		sv = ps.getParameter(TEST, "DFT");
		assertNotNull(sv);
		assertEquals("TEST", sv);

		String dv = ps.getParameter(TEST4, "DFT");
		assertNotNull(dv);
		assertEquals("DFT", dv);
		
		assertFalse(ps.getParameterIf(TEST, v -> v == null).isPresent());
		
		assertTrue(ps.getParameterIf(TEST, v -> v.length() > 3).isPresent());
		
		assertTrue(ps.hasParameterValue(TEST, "TEST"));
		assertFalse(ps.hasParameterValue(TEST, null));
		assertFalse(ps.hasParameterValue(TEST, "TESTX"));
		assertFalse(ps.hasParameterValue(TEST3, "TEST"));
		assertFalse(ps.hasParameterValue(TEST3, null));
		
		ps.addParameter(TEST3.getKey(), null);
		
		assertTrue(ps.hasParameterValue(TEST3, null));
	}

	@Test(expected = TypeMismatchException.class)
	public void testParameterType() {
		ParameterSet ps = ParameterSet.builder().parameter("test2", Integer.valueOf(3))
				.build();
		ps.getParameter("test2", String.class);
	}
	
	@Test(expected = TypeMismatchException.class)
	public void testParameterTypeDft() {
		ParameterSet ps = ParameterSet.builder().parameter("test2", Integer.valueOf(3))
				.build();
		ps.getParameter("test2", String.class, "dft");
	}

	@Test
	public void testParameterBuilder() {

		ParameterSet ps = ParameterSet.builder().parameter("test", "TEST").parameter("test2", Integer.valueOf(3))
				.build();

		assertTrue(ps.hasParameter("test"));
		assertTrue(ps.hasNotNullParameter("test"));
		assertTrue(ps.hasParameter("test2"));
		assertTrue(ps.hasNotNullParameter("test2"));

		assertFalse(ps.hasParameter((String) null));
		assertFalse(ps.hasNotNullParameter((String) null));

		String sv = ps.getParameter("test", String.class).orElse(null);
		assertNotNull(sv);
		assertEquals("TEST", sv);

		Integer iv = ps.getParameter("test2", Integer.class).orElse(null);
		assertNotNull(iv);
		assertEquals(Integer.valueOf(3), iv);

		ParameterSet ps2 = ParameterSet.builder().parameters(ps).build();

		assertTrue(ps2.hasParameters());
		assertTrue(ps2.hasParameter("test"));
		assertTrue(ps2.hasNotNullParameter("test"));
		assertTrue(ps2.hasParameter("test2"));
		assertTrue(ps2.hasNotNullParameter("test2"));

		DefaultParameterSet ps3 = new DefaultParameterSet();
		ps3.addParameter("p1", Integer.valueOf(1));
		ps3.addParameter("p2", Integer.valueOf(2));
		ps3.removeParameter("p2");

		String ts = ps3.toString();
		assertNotNull(ts);

		Integer pv = ps3.getParameter("p1x", Integer.class, null);
		assertNull(pv);

		Integer pvi = ps3.getParameter("p1", Integer.class, null);
		assertEquals(Integer.valueOf(1), pvi);

		DefaultParameterSet ps4 = new DefaultParameterSet(null);
		assertFalse(ps4.hasParameters());

		ParameterSet ps5 = new DefaultParameterSet();
		ps5.toString();

	}

}
