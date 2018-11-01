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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.holonplatform.core.Context;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertyRenderer;
import com.holonplatform.core.property.PropertyRendererRegistry;
import com.holonplatform.core.property.PropertyRendererRegistry.NoSuitableRendererAvailableException;
import com.holonplatform.core.test.data.TestPropertySet;

public class TestPropertyRenderers {

	@Test
	public void testPropertyRendererRegistry() {

		PropertyRendererRegistry r = PropertyRendererRegistry.getDefault();
		assertNotNull(r);

		PropertyRendererRegistry r2 = PropertyRendererRegistry.getDefault(ClassUtils.getDefaultClassLoader());
		assertNotNull(r2);
		assertEquals(r, r2);

		PropertyRendererRegistry r3 = PropertyRendererRegistry.get();
		assertNotNull(r3);

	}

	@Test
	public void testPropertyRenderer() {

		final PropertyRendererRegistry r1 = PropertyRendererRegistry.create(true);
		assertNotNull(r1);

		final PropertyBox box = PropertyBox.create(TestPropertySet.PROPERTIES);
		box.setValue(TestPropertySet.NAME, "test");

		final PropertyRenderer<RenderTest, String> rnd = new PropertyRenderer<RenderTest, String>() {

			@Override
			public Class<? extends RenderTest> getRenderType() {
				return RenderTest.class;
			}

			@Override
			public RenderTest render(Property<? extends String> property) {
				return new RenderTest(box.getValue(property));
			}
		};

		r1.register(p -> String.class.isAssignableFrom(p.getType()), rnd);

		RenderTest rt = Context.get().executeThreadBound(PropertyRendererRegistry.CONTEXT_KEY, r1, () -> {
			return TestPropertySet.NAME.render(RenderTest.class);
		});

		assertNotNull(rt);
		assertEquals("test", rt.getValue());

		Context.get().executeThreadBound(PropertyRendererRegistry.CONTEXT_KEY, r1, () -> {
			assertTrue(TestPropertySet.NAME.renderIfAvailable(RenderTest.class).isPresent());
			assertFalse(TestPropertySet.NAME.renderIfAvailable(Number.class).isPresent());
		});

		PropertyRenderer<RenderTest, String> rnd2 = PropertyRenderer.create(RenderTest.class,
				p -> new RenderTest(p.getName()));

		assertNotNull(rnd2);

		rt = rnd2.render(TestPropertySet.NAME);
		assertNotNull(rt);
		assertEquals("name", rt.getValue());

		final PropertyRendererRegistry r2 = PropertyRendererRegistry.create(false);
		assertNotNull(r2);

		r2.register(p -> TestPropertySet.NAME.equals(p), rnd2);

		rt = Context.get().executeThreadBound(PropertyRendererRegistry.CONTEXT_KEY, r2, () -> {
			return TestPropertySet.NAME.render(RenderTest.class);
		});

		assertNotNull(rt);
		assertEquals("name", rt.getValue());

	}

	@Test
	public void testPropertyRendererNotAvailable() {
		Assertions.assertThrows(NoSuitableRendererAvailableException.class, () -> {
			TestPropertySet.NAME.render(NotAvailableRenderTest.class);
		});
	}

	private static class RenderTest {

		private final String value;

		public RenderTest(String value) {
			super();
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

	private static class NotAvailableRenderTest {

	}

}
