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

import static com.holonplatform.core.test.TestProperty.P1;
import static com.holonplatform.core.test.TestProperty.P10;
import static com.holonplatform.core.test.TestProperty.P11;
import static com.holonplatform.core.test.TestProperty.P12;
import static com.holonplatform.core.test.TestProperty.P13;
import static com.holonplatform.core.test.TestProperty.P14;
import static com.holonplatform.core.test.TestProperty.P15;
import static com.holonplatform.core.test.TestProperty.P2;
import static com.holonplatform.core.test.TestProperty.P3;
import static com.holonplatform.core.test.TestProperty.P4;
import static com.holonplatform.core.test.TestProperty.P5;
import static com.holonplatform.core.test.TestProperty.P6;
import static com.holonplatform.core.test.TestProperty.P7;
import static com.holonplatform.core.test.TestProperty.P8;
import static com.holonplatform.core.test.TestProperty.P9;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.Context;
import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.i18n.LocalizationContext;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertyValuePresenter;
import com.holonplatform.core.property.PropertyValuePresenterRegistry;
import com.holonplatform.core.test.data.TestPropertySet;

public class TestPropertyPresenters {

	@Test
	public void testPropertyValuePresenter() {

		assertEquals("str", P1.present("str"));

		Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY,
				LocalizationContext.builder().withInitialLocale(Locale.ITALY)
						.withDefaultBooleanLocalization(Boolean.TRUE, Localizable.builder().message("isTrue").build())
						.withDefaultBooleanLocalization(Boolean.FALSE, Localizable.builder().message("isFalse").build())
						.build(),
				() -> {
					assertEquals("7", P2.present(7));
					assertEquals("1.300", P2.present(1300));

					assertEquals("3.500,00", P4.present(3500d));
					assertEquals("3.500,42", P4.present(3500.42d));
					assertEquals("3.500,01", P4.present(3500.007d));

					assertEquals("isTrue", P3.present(Boolean.TRUE));
					assertEquals("isFalse", P3.present(Boolean.FALSE));
					assertEquals("isFalse", P3.present(null));
				});

		assertEquals("B", P5.present(TestProperty.TestEnum.B));

		assertEquals("valueA", P6.present(TestProperty.TestEnum2.A));

		final Calendar c = Calendar.getInstance(Locale.ITALIAN);
		c.set(Calendar.DAY_OF_MONTH, 9);
		c.set(Calendar.MONTH, 2);
		c.set(Calendar.YEAR, 1979);
		c.set(Calendar.HOUR_OF_DAY, 18);
		c.set(Calendar.MINUTE, 30);
		c.set(Calendar.SECOND, 15);
		c.set(Calendar.MILLISECOND, 0);

		Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY,
				LocalizationContext.builder().withInitialLocale(Locale.ITALY).build(), () -> {

					assertEquals("09/03/79, 18:30", P7.present(c.getTime()));

					final LocalDate date = LocalDate.of(1979, Month.MARCH, 9);
					assertEquals("09/03/79", P8.present(date));

					final LocalTime time = LocalTime.of(18, 30, 15);
					assertEquals("18:30", P9.present(time));

					final LocalDateTime dt = LocalDateTime.of(1979, Month.MARCH, 9, 18, 30, 15);
					assertEquals("09/03/79, 18:30", P10.present(dt));

				});

		String[] sa = new String[] { "a", "b", "c" };

		assertEquals("a;b;c", P11.present(sa));

		assertEquals("TestCaptionableCaption", P14.present(new TestProperty.TestCaptionable()));

		PropertyBox box = PropertyBox.builder(P1, P2, P13).set(P1, "p1").set(P2, 2).build();

		assertEquals("VRT:p1", P13.present(box.getValue(P13)));

		assertEquals("1300", P15.present(1300));

		Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY,
				LocalizationContext.builder().withInitialLocale(Locale.US).build(), () -> {
					assertEquals("35;57.8", P12.present(new Double[] { 35d, 57.8d }));
				});

		Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY,
				LocalizationContext.builder().withInitialLocale(Locale.ITALY).build(), () -> {
					assertEquals("35;57,8", P12.present(new Double[] { 35d, 57.8d }));
				});

		assertEquals("p1", box.present(P1));
	}

	@Test
	public void testPropertyPresenter() {

		final PropertyValuePresenter<String> np = (p, v) -> p.getMessage() + ":" + v;

		PropertyValuePresenterRegistry.get().register(p -> TestPropertySet.NAME.equals(p), np);

		String pv = np.present(TestPropertySet.NAME, "v");

		assertEquals("Name:v", pv);
	}

	@Test
	public void testPropertyValuePresenterRegistry() {

		PropertyValuePresenterRegistry registry = PropertyValuePresenterRegistry.create(true);

		registry.register(p -> p.getConfiguration().hasNotNullParameter("testpar"), (p, v) -> "TEST_PRS");

		final PathProperty<Integer> prp = PathProperty.create("test", Integer.class).withConfiguration("testpar", "x");

		assertEquals("1", prp.present(1));

		assertEquals("TEST_PRS", Context.get().executeThreadBound(PropertyValuePresenterRegistry.CONTEXT_KEY, registry,
				() -> prp.present(1)));

		registry = PropertyValuePresenterRegistry.getDefault();

		assertNotNull(registry);

		PropertyValuePresenterRegistry registry2 = PropertyValuePresenterRegistry
				.getDefault(ClassUtils.getDefaultClassLoader());

		assertEquals(registry, registry2);
	}

}
