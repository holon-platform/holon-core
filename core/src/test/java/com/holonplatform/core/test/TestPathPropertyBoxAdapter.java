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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.Path;
import com.holonplatform.core.property.NumericProperty;
import com.holonplatform.core.property.PathPropertyBoxAdapter;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.StringProperty;

public class TestPathPropertyBoxAdapter {

	private static final StringProperty P1 = StringProperty.create("p1");
	private static final NumericProperty<Integer> P2 = NumericProperty.integerType("p2");
	private static final StringProperty P3 = StringProperty.create("p3").parent(P1);

	private static final PropertySet<?> SET = PropertySet.builderOf(P1, P2, P3).identifier(P1).build();

	@Test
	public void testAdapter() {

		final Path<String> PT1 = Path.of("p1", String.class);
		final Path<Integer> PT2 = Path.of("p2", Integer.class);
		final Path<String> PT3 = Path.of("p3", String.class).parent(PT1);

		final Path<String> PTX = Path.of("px", String.class);

		PropertyBox box = PropertyBox.builder(SET).set(P1, "test1").set(P2, 7).set(P3, "test2").build();

		PathPropertyBoxAdapter adapter = PathPropertyBoxAdapter.create(box);

		assertTrue(adapter.containsValue(PT1));
		assertTrue(adapter.containsValue(PT2));
		assertTrue(adapter.containsValue(PT3));
		assertFalse(adapter.containsValue(PTX));

		assertEquals("test1", adapter.getValue(PT1).orElse(null));
		assertEquals(Integer.valueOf(7), adapter.getValue(PT2).orElse(null));
		assertEquals("test2", adapter.getValue(PT3).orElse(null));

		box = PropertyBox.builder(SET).build();
		adapter = PathPropertyBoxAdapter.create(box);

		adapter.setValue(PT1, "test1");
		adapter.setValue(PT2, 7);
		adapter.setValue(PT3, "test2");

		assertEquals("test1", adapter.getValue(PT1).orElse(null));
		assertEquals(Integer.valueOf(7), adapter.getValue(PT2).orElse(null));
		assertEquals("test2", adapter.getValue(PT3).orElse(null));
	}

}
