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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.Test;

import com.holonplatform.core.Context;
import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.i18n.LocalizationContext;
import com.holonplatform.core.presentation.StringValuePresenter;

public class TestPresenter {

	@Test
	public void testStringValuePresenter() {

		StringValuePresenter presenter = StringValuePresenter.getDefault();
		assertNotNull(presenter);

		assertEquals("7", presenter.present(7));
		assertEquals("7", presenter.present(Integer.class, 7));

		assertEquals("7", presenter.present(7L));
		assertEquals("7", presenter.present(Long.class, 7L));

		Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY,
				LocalizationContext.builder().withInitialLocale(Locale.ITALY)
						.withDefaultBooleanLocalization(Boolean.TRUE, Localizable.builder().message("isTrue").build())
						.withDefaultBooleanLocalization(Boolean.FALSE, Localizable.builder().message("isFalse").build())
						.build(),
				() -> {

					final ParameterSet params = ParameterSet.builder()
							.parameter(StringValuePresenter.DECIMAL_POSITIONS, 2).build();

					assertEquals("7", presenter.present(7, params));
					assertEquals("1.300", presenter.present(1300, params));

					assertEquals("3.500,00", presenter.present(3500d, params));
					assertEquals("3.500,42", presenter.present(3500.42d, params));
					assertEquals("3.500,01", presenter.present(3500.007d, params));

					assertEquals("isTrue", presenter.present(Boolean.TRUE));
					assertEquals("isFalse", presenter.present(Boolean.FALSE));
					assertEquals("isFalse", presenter.present(Boolean.class, (Boolean) null));

					final ParameterSet params2 = ParameterSet.builder()
							.parameter(StringValuePresenter.DECIMAL_POSITIONS, 2)
							.parameter(StringValuePresenter.DISABLE_GROUPING, true).build();

					assertEquals("3500,00", presenter.present(3500d, params2));
					assertEquals("3500,42", presenter.present(3500.42d, params2));
					assertEquals("3500,01", presenter.present(3500.007d, params2));
				});

		assertNull(presenter.present(null));
		assertNull(presenter.present((Object) null,
				ParameterSet.builder().parameter(StringValuePresenter.DECIMAL_POSITIONS, 2).build()));

	}

}
