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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Test;

import com.holonplatform.core.Path;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.property.NumericProperty;
import com.holonplatform.core.property.PathPropertySetAdapter;
import com.holonplatform.core.property.PathPropertySetAdapter.PathConverter;
import com.holonplatform.core.property.PathPropertySetAdapter.PathMatcher;
import com.holonplatform.core.property.PathPropertySetAdapter.PropertyPath;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.StringProperty;

public class TestPathPropertySetAdapter {

	private static final StringProperty P1 = StringProperty.create("p1");
	private static final NumericProperty<Integer> P2 = NumericProperty.integerType("p2");
	private static final StringProperty P3 = StringProperty.create("p3").parent(P1);

	private static final PropertySet<?> SET = PropertySet.builderOf(P1, P2, P3).identifier(P1).build();

	private static final StringProperty PX = StringProperty.create("px");

	@Test
	public void testByPath() {

		final Path<String> PT1 = Path.of("p1", String.class);
		final Path<Integer> PT2 = Path.of("p2", Integer.class);
		final Path<String> PT3 = Path.of("p3", String.class).parent(PT1);

		final Path<String> PTX = Path.of("px", String.class);

		final PathPropertySetAdapter adapter = PathPropertySetAdapter.create(SET);

		assertTrue(adapter.contains(P1));
		assertTrue(adapter.contains(P2));
		assertTrue(adapter.contains(P3));
		assertFalse(adapter.contains(PX));

		assertTrue(adapter.contains(PT1));
		assertTrue(adapter.contains(PT2));
		assertTrue(adapter.contains(PT3));
		assertFalse(adapter.contains(Path.of("p3", String.class)));
		assertFalse(adapter.contains(PTX));

		assertTrue(adapter.getProperty(P1).isPresent());
		assertTrue(adapter.getProperty(P2).isPresent());
		assertTrue(adapter.getProperty(P3).isPresent());
		assertFalse(adapter.getProperty(PX).isPresent());

		assertTrue(adapter.getProperty(PT1).isPresent());
		assertTrue(adapter.getProperty(PT2).isPresent());
		assertTrue(adapter.getProperty(PT3).isPresent());
		assertFalse(adapter.getProperty(PTX).isPresent());

		assertEquals(P1, adapter.getProperty(PT1).orElse(null));
		assertEquals(P2, adapter.getProperty(PT2).orElse(null));
		assertEquals(P3, adapter.getProperty(PT3).orElse(null));

		assertEquals("p1", adapter.getPath(P1).map(p -> p.relativeName()).orElse(null));
		assertEquals("p2", adapter.getPath(P2).map(p -> p.relativeName()).orElse(null));
		assertEquals("p1.p3", adapter.getPath(P3).map(p -> p.relativeName()).orElse(null));

		assertFalse(adapter.getPath(PX).isPresent());

		Set<Path<?>> ids = adapter.getPathIdentifiers();

		assertNotNull(ids);
		assertEquals(1, ids.size());

		assertEquals("p1", ids.iterator().next().relativeName());

		Stream<Path<?>> ps = adapter.paths();

		assertNotNull(ps);
		assertEquals(3, ps.count());

		assertEquals("p1", adapter.paths().filter(p -> "p1".equals(p.relativeName())).limit(1).findFirst()
				.map(p -> p.relativeName()).orElse(null));
		assertEquals("p2", adapter.paths().filter(p -> "p2".equals(p.relativeName())).limit(1).findFirst()
				.map(p -> p.relativeName()).orElse(null));
		assertEquals("p1.p3", adapter.paths().filter(p -> "p1.p3".equals(p.relativeName())).limit(1).findFirst()
				.map(p -> p.relativeName()).orElse(null));

		Stream<PropertyPath<?>> pps = adapter.propertyPaths();

		assertNotNull(pps);
		assertEquals(3, pps.count());

		assertEquals(P1, adapter.propertyPaths().filter(p -> "p1".equals(p.getPath().relativeName())).limit(1)
				.findFirst().map(p -> p.getProperty()).orElse(null));
		assertEquals(P2, adapter.propertyPaths().filter(p -> "p2".equals(p.getPath().relativeName())).limit(1)
				.findFirst().map(p -> p.getProperty()).orElse(null));
		assertEquals(P3, adapter.propertyPaths().filter(p -> "p1.p3".equals(p.getPath().relativeName())).limit(1)
				.findFirst().map(p -> p.getProperty()).orElse(null));
	}

	@Test
	public void testByName() {

		final PathPropertySetAdapter adapter = PathPropertySetAdapter.create(SET);

		assertTrue(adapter.contains("p1"));
		assertTrue(adapter.contains("p2"));
		assertTrue(adapter.contains("p3"));
		assertFalse(adapter.contains("px"));

		assertTrue(adapter.getProperty("p1").isPresent());
		assertTrue(adapter.getProperty("p2").isPresent());
		assertTrue(adapter.getProperty("p3").isPresent());
		assertFalse(adapter.getProperty("px").isPresent());

		assertEquals(P1, adapter.getProperty("p1").orElse(null));
		assertEquals(P2, adapter.getProperty("p2").orElse(null));
		assertEquals(P3, adapter.getProperty("p3").orElse(null));

		assertEquals(P1, adapter.getProperty("p1", String.class).orElse(null));
		assertEquals(P2, adapter.getProperty("p2", Integer.class).orElse(null));
		assertEquals(P3, adapter.getProperty("p3", String.class).orElse(null));

		assertEquals(P2, adapter.getProperty("p2", Number.class).orElse(null));

		Stream<String> ps = adapter.names();

		assertNotNull(ps);
		assertEquals(3, ps.count());

		assertEquals("p1", adapter.names().filter(n -> "p1".equals(n)).limit(1).findFirst().orElse(null));
		assertEquals("p2", adapter.names().filter(n -> "p2".equals(n)).limit(1).findFirst().orElse(null));
		assertEquals("p3", adapter.names().filter(n -> "p3".equals(n)).limit(1).findFirst().orElse(null));

	}

	@Test(expected = TypeMismatchException.class)
	public void testByNameType() {
		final PathPropertySetAdapter adapter = PathPropertySetAdapter.create(SET);
		adapter.getProperty("p2", String.class);
	}

	@Test
	public void testPathMatcher() {

		final PathMatcher pm = (p1, p2) -> {
			if (p1 != null && p2 != null) {
				return p1.relativeName().equalsIgnoreCase(p2.relativeName());
			}
			return false;
		};

		final PathPropertySetAdapter adapter = PathPropertySetAdapter.builder(SET).pathMatcher(pm).build();

		assertTrue(adapter.contains(Path.of("P1", String.class)));
		assertFalse(adapter.contains(Path.of("xxx", String.class)));
	}

	@Test
	public void testPathConverter() {

		final PathConverter pc = new PathConverter() {

			@Override
			public <T> Optional<Path<T>> convert(Property<T> property) {
				if (property != null && property instanceof Path) {
					return Optional.of(Path.of(property.getName().toUpperCase(), property.getType()));
				}
				return Optional.empty();
			}

		};

		final PathPropertySetAdapter adapter = PathPropertySetAdapter.builder(SET).pathConverter(pc).build();

		assertTrue(adapter.contains(Path.of("P1", String.class)));
		assertFalse(adapter.contains(Path.of("p1", String.class)));
	}

}
