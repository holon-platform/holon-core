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
package com.holonplatform.http.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.holonplatform.core.internal.utils.TestUtils;
import com.holonplatform.http.internal.HttpUtils;

public class TestHttp {

	@Test
	public void testHttp() throws URISyntaxException {

		TestUtils.checkUtilityClass(HttpUtils.class);

		assertFalse(HttpUtils.isSecure(null));
		assertTrue(HttpUtils.isSecure(new URI("https://example.com/test")));
	}

	@Test
	public void testLocale() {

		String header = null;

		List<Locale> locales = HttpUtils.getAcceptLanguageLocales(header);
		assertNotNull(locales);
		assertEquals(0, locales.size());

		header = "it-IT";

		locales = HttpUtils.getAcceptLanguageLocales(header);
		assertNotNull(locales);
		assertEquals(1, locales.size());
		assertEquals("it", locales.get(0).getLanguage());
		assertEquals("IT", locales.get(0).getCountry());

		header = "it-IT,it;q=0.8,en-US;q=0.5,en;q=0.3";

		locales = HttpUtils.getAcceptLanguageLocales(header);
		assertNotNull(locales);
		assertEquals(4, locales.size());

		assertEquals("it", locales.get(0).getLanguage());
		assertEquals("IT", locales.get(0).getCountry());

	}

}
