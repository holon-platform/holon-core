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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.Path;
import com.holonplatform.core.Validator;
import com.holonplatform.core.Validator.ValidationException;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.property.BooleanProperty;
import com.holonplatform.core.property.NumericProperty;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.property.TemporalProperty;
import com.holonplatform.core.temporal.TemporalType;

public class TestCloneableProperty {

	private static final ConfigProperty<Integer> CP = ConfigProperty.create("testcp", Integer.class);

	@Test
	public void testClone() {

		PathProperty<String> property = PathProperty.create("test", String.class).message("msg").messageCode("msg.code")
				.dataPath("dp").withConfiguration(CP, 7).temporalType(TemporalType.DATE_TIME)
				.withValidator(Validator.max(3)).converter(Long.class, v -> String.valueOf(v), v -> Long.valueOf(v))
				.hashCodeProvider(p -> Optional.of(1)).equalsHandler((p1, p2) -> p1.getConfiguration().getParameter(CP,
						0) == ((Property<?>) p2).getConfiguration().getParameter(CP, 0));

		PathProperty<String> cloned = property.clone(b -> {
		});

		assertNotNull(cloned);

		assertEquals("test", cloned.relativeName());
		assertEquals("msg", cloned.getMessage());
		assertEquals("msg.code", cloned.getMessageCode());
		assertEquals("dp", cloned.getDataPath().orElse(null));
		assertTrue(cloned.getConfiguration().hasNotNullParameter(CP));
		assertEquals(Integer.valueOf(7), cloned.getConfiguration().getParameter(CP).orElse(null));
		assertEquals(TemporalType.DATE_TIME, cloned.getTemporalType().orElse(null));

		assertTrue(isValid(cloned, "xxx"));
		assertFalse(isValid(cloned, "xxxx"));

		assertEquals(1, cloned.hashCode());

		PathProperty<String> p2 = PathProperty.create("p2", String.class).withConfiguration(CP, 7);

		assertTrue(cloned.equals(p2));

		assertEquals(Long.class, cloned.getModelType());

		assertEquals(Long.valueOf(789), cloned.getModelValue("789"));

	}

	@Test
	public void testCloneBuider() {

		final Path<String> parentPath = Path.of("parent", String.class);

		PathProperty<String> property = PathProperty.create("test", String.class).message("msg");

		PathProperty<String> cloned = property.clone(b -> b.message("mymsg").parent(parentPath));

		assertNotNull(cloned);
		assertEquals("mymsg", cloned.getMessage());
		assertEquals("parent.test", cloned.relativeName());

	}

	@Test
	public void testCloneSubTypes() {

		StringProperty p1 = StringProperty.create("test");
		StringProperty cloned = p1.clone(b -> {
		});

		assertNotNull(cloned);
		assertEquals("test", cloned.getName());
		assertEquals(String.class, cloned.getType());

		BooleanProperty p2 = BooleanProperty.create("test");
		BooleanProperty cloned2 = p2.clone(b -> {
		});

		assertNotNull(cloned2);
		assertEquals("test", cloned2.getName());
		assertEquals(Boolean.class, cloned2.getType());

		NumericProperty<Long> p3 = NumericProperty.longType("test");
		NumericProperty<Long> cloned3 = p3.clone(b -> {
		});

		assertNotNull(cloned3);
		assertEquals("test", cloned3.getName());
		assertEquals(Long.class, cloned3.getType());

		TemporalProperty<LocalDate> p4 = TemporalProperty.localDate("test");
		TemporalProperty<LocalDate> cloned4 = p4.clone(b -> {
		});

		assertNotNull(cloned4);
		assertEquals("test", cloned4.getName());
		assertEquals(LocalDate.class, cloned4.getType());
	}

	@SuppressWarnings("unused")
	private static <T> boolean isValid(Property<T> p, T v) {
		try {
			p.validate(v);
		} catch (ValidationException e) {
			return false;
		}
		return true;
	}

	@Test
	public void testDataTargetProperty() {

		StringProperty property = StringProperty.create("test");

		DataTarget<?> target = DataTarget.named("target");

		StringProperty cloned = target.property(property);

		assertNotNull(cloned);
		assertTrue(cloned.getParent().isPresent());
		assertEquals(target, cloned.getParent().orElse(null));

	}

}
