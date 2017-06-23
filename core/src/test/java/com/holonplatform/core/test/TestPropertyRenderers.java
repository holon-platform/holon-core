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

import static org.junit.Assert.*;

import org.junit.Test;

import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertyRenderer;
import com.holonplatform.core.property.PropertyRendererRegistry;
import com.holonplatform.core.property.PropertyValuePresenter;
import com.holonplatform.core.property.PropertyValuePresenterRegistry;
import com.holonplatform.core.test.data.TestPropertySet;

public class TestPropertyRenderers {

	@Test
	public void testPropertyPresenter() {

		final PropertyValuePresenter<String> np = (p, v) -> p.getMessage() + ":" + v;

		PropertyValuePresenterRegistry.get().register(p -> TestPropertySet.NAME.equals(p), np);

		String pv = np.present(TestPropertySet.NAME, "v");

		assertEquals("Name:v", pv);
	}

	@Test
	public void testPropertyRenderer() {

		final PropertyBox box = PropertyBox.create(TestPropertySet.PROPERTIES);
		box.setValue(TestPropertySet.NAME, "test");

		final PropertyRenderer<RenderTest, String> rnd = new PropertyRenderer<RenderTest, String>() {

			@Override
			public Class<? extends RenderTest> getRenderType() {
				return RenderTest.class;
			}

			@Override
			public RenderTest render(Property<String> property) {
				return new RenderTest(box.getValue(property));
			}
		};

		PropertyRendererRegistry.get().register(p -> String.class.isAssignableFrom(p.getType()), rnd);

		RenderTest rt = TestPropertySet.NAME.render(RenderTest.class);
		assertNotNull(rt);
		assertEquals("test", rt.getValue());

		assertTrue(TestPropertySet.NAME.renderIfAvailable(RenderTest.class).isPresent());

		assertFalse(TestPropertySet.NAME.renderIfAvailable(Number.class).isPresent());

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

}
