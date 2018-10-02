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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.i18n.Caption;
import com.holonplatform.core.property.ListPathProperty;
import com.holonplatform.core.property.ListVirtualProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.SetPathProperty;
import com.holonplatform.core.property.SetVirtualProperty;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.test.data.TestPropertySet;
import com.holonplatform.test.TestUtils;

public class TestCollectionProperty {

	@Test
	public void testListPathProperty() {

		ListPathProperty<String> p = ListPathProperty.create("test", String.class);

		assertEquals(List.class, p.getType());
		assertEquals(String.class, p.getElementType());

		List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");

		PropertyBox pb = PropertyBox.create(p);
		pb.setValue(p, list);

		List<String> v = pb.getValue(p);
		assertNotNull(v);
		assertEquals(list, v);

		final ListPathProperty<PropertyBox> p2 = ListPathProperty.propertyBox("test", TestPropertySet.PROPERTIES);
		assertNotNull(p2);
		assertEquals(PropertyBox.class, p2.getElementType());
		assertEquals(TestPropertySet.PROPERTIES,
				p2.getConfiguration().getParameter(PropertySet.PROPERTY_CONFIGURATION_ATTRIBUTE).orElse(null));

	}

	@Test
	public void testSetPathProperty() {

		SetPathProperty<String> p = SetPathProperty.create("test", String.class);

		assertEquals(Set.class, p.getType());
		assertEquals(String.class, p.getElementType());

		Set<String> list = new HashSet<>();
		list.add("a");
		list.add("b");

		PropertyBox pb = PropertyBox.create(p);
		pb.setValue(p, list);

		Set<String> v = pb.getValue(p);
		assertNotNull(v);
		assertEquals(list, v);

		TestUtils.expectedException(IllegalArgumentException.class, () -> SetPathProperty.propertyBox("test", null));

		final SetPathProperty<PropertyBox> p2 = SetPathProperty.propertyBox("test", TestPropertySet.PROPERTIES);
		assertNotNull(p2);
		assertEquals(PropertyBox.class, p2.getElementType());
		assertEquals(TestPropertySet.PROPERTIES,
				p2.getConfiguration().getParameter(PropertySet.PROPERTY_CONFIGURATION_ATTRIBUTE).orElse(null));

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testElementConverter() {

		ListPathProperty<String> p = ListPathProperty.create("test", String.class).elementConverter(Integer.class,
				v -> String.valueOf(v), v -> Integer.valueOf(v));

		assertTrue(p.getConverter().isPresent());

		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");

		Object model = p.getModelValue(list);
		assertNotNull(model);
		assertTrue(List.class.isAssignableFrom(model.getClass()));

		List<Integer> ml = (List<Integer>) model;
		assertEquals(2, ml.size());
		assertEquals(Integer.valueOf(1), ml.get(0));
		assertEquals(Integer.valueOf(2), ml.get(1));

		PropertyBox pb = PropertyBox.create(p);
		pb.setValue(p, list);

		List<Integer> ilist = new ArrayList<>();
		ilist.add(1);
		ilist.add(2);

		pb = PropertyBox.create(p);
		pb.setValue(p, (List) ilist);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testConverter() {

		ListPathProperty<Integer> p = ListPathProperty.create("test", Integer.class).converter(Integer[].class,
				v -> Arrays.asList(v), v -> v.toArray(new Integer[0]));

		List<Integer> ilist = new ArrayList<>();
		ilist.add(1);
		ilist.add(2);

		Object model = p.getModelValue(ilist);
		assertNotNull(model);
		assertTrue(Integer[].class == model.getClass());

		Integer[] ia = (Integer[]) model;
		assertEquals(2, ia.length);
		assertEquals(Integer.valueOf(1), ia[0]);
		assertEquals(Integer.valueOf(2), ia[1]);

		PropertyBox pb = PropertyBox.create(p);
		pb.setValue(p, ilist);

		Integer[] values = { 1, 2, 3 };

		pb = PropertyBox.create(p);
		pb.setValue((Property) p, values);

		List<Integer> ival = pb.getValue(p);
		assertNotNull(ival);
		assertEquals(3, ival.size());
		assertEquals(Integer.valueOf(1), ival.get(0));
		assertEquals(Integer.valueOf(2), ival.get(1));
		assertEquals(Integer.valueOf(3), ival.get(2));

	}

	@Test
	public void testVirtual() {

		final StringProperty sp = StringProperty.create("test");

		final ListVirtualProperty<String> lvp = ListVirtualProperty.create(String.class, pb -> {
			String value = pb.getValue(sp);
			if (value != null) {
				List<String> l = new ArrayList<>();
				for (char c : value.toCharArray()) {
					l.add(String.valueOf(c));
				}
				return l;
			}
			return Collections.emptyList();
		});

		final SetVirtualProperty<String> svp = SetVirtualProperty.create(String.class, pb -> {
			String value = pb.getValue(sp);
			if (value != null) {
				Set<String> l = new HashSet<>();
				for (char c : value.toCharArray()) {
					l.add(String.valueOf(c));
				}
				return l;
			}
			return Collections.emptySet();
		});

		PropertyBox pb = PropertyBox.builder(sp, lvp, svp).set(sp, "abc").build();

		List<String> list = pb.getValue(lvp);
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("abc", list.stream().collect(Collectors.joining("")));

		Set<String> set = pb.getValue(svp);
		assertNotNull(set);
		assertEquals(3, set.size());
		assertTrue(set.contains("a"));
		assertTrue(set.contains("b"));
		assertTrue(set.contains("c"));
	}

	@Test
	public void testPresentation() {

		ListPathProperty<String> LP = ListPathProperty.create("test", String.class);
		SetPathProperty<String> SP = SetPathProperty.create("test", String.class);

		List<String> list = Arrays.asList("a", "b", "c");
		String value = LP.present(list);

		assertNotNull(value);
		assertEquals("a,b,c", value);

		Set<String> set = new HashSet<>(Arrays.asList("a", "b", "c"));
		value = SP.present(set);

		assertNotNull(value);
		assertEquals("a,b,c", value);

		ListPathProperty<CEnum> LEP = ListPathProperty.create("test", CEnum.class);

		List<CEnum> elist = Arrays.asList(CEnum.B, CEnum.A);
		value = LEP.present(elist);

		assertNotNull(value);
		assertEquals("valueB,valueA", value);

	}

	static enum CEnum {

		@Caption("valueA")
		A,

		@Caption("valueB")
		B;

	}

}
