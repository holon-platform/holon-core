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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.junit.Test;

import com.holonplatform.core.beans.BeanConfigProperties;
import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.core.internal.config.DefaultConfig;
import com.holonplatform.core.internal.config.PrefixedConfigPropertyProvider;
import com.holonplatform.core.internal.config.PropertiesConfigProvider;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.TestUtils;

public class TestConfig {

	@Test
	public void testUtils() {
		TestUtils.checkUtilityClass(DefaultConfig.class);
	}

	@Test
	public void testConfig() throws IOException {

		Properties props = ClassUtils.loadProperties("holon.properties", false);
		assertNotNull(props);
		assertEquals("test", props.getProperty("holon.test.property"));

		ConfigPropertyProvider cpp = ConfigPropertyProvider.using(props);
		assertEquals("test", cpp.getProperty("holon.test.property", String.class));

		props = ClassUtils.loadProperties("holon.properties", getClass().getClassLoader(), true);
		assertNotNull(props);
		assertEquals("test", props.getProperty("holon.test.property"));

		props = ClassUtils.loadProperties("holon.properties", null, false);
		assertNotNull(props);
		assertEquals("test", props.getProperty("holon.test.property"));

		TestUtils.expectedException(IllegalArgumentException.class, new Callable<Properties>() {

			@Override
			public Properties call() throws Exception {
				return ClassUtils.loadProperties(null, false);
			}
		});

		TestUtils.expectedException(IOException.class, new Callable<Properties>() {

			@Override
			public Properties call() throws Exception {
				return ClassUtils.loadProperties("notexists.properties", false);
			}
		});

		cpp = ConfigPropertyProvider.using("holon.properties", ClassUtils.getDefaultClassLoader());
		assertEquals("test", cpp.getProperty("holon.test.property", String.class));

		cpp = ConfigPropertyProvider.using(ClassUtils.getDefaultClassLoader().getResourceAsStream("holon.properties"));
		assertEquals("test", cpp.getProperty("holon.test.property", String.class));

		BeanConfigProperties bcp = BeanConfigProperties.builder().withDefaultPropertySources().build();

		assertTrue(bcp.isBeanIntrospectorCacheEnabled());

		System.setProperty(
				BeanConfigProperties.NAME + "." + BeanConfigProperties.BEAN_INTROSPECTOR_CACHE_ENABLED.getKey(),
				"false");
		assertFalse(bcp.isBeanIntrospectorCacheEnabled());

		System.setProperty(
				BeanConfigProperties.NAME + "." + BeanConfigProperties.BEAN_INTROSPECTOR_CACHE_ENABLED.getKey(),
				"true");

		Properties ps = new Properties();
		ps.setProperty("other", "test");
		ps.setProperty("prop", "testp");
		ps.setProperty("test.prop", "testval");

		final PropertiesConfigProvider cfg = new PropertiesConfigProvider(ps);
		assertTrue(cfg.containsProperty("other"));
		assertFalse(cfg.containsProperty("x"));

		String val = cfg.getProperty("other", String.class);
		assertEquals("test", val);

		val = cfg.getProperty("z", String.class);
		assertNull(val);

		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				cfg.getProperty(null, null);
			}

		});

		Map<String, Object> map = new HashMap<>();
		map.put("other", "test");
		map.put("prop", "testp");
		map.put("test.prop", "testval");

		cpp = ConfigPropertyProvider.using(map);
		assertTrue(cpp.containsProperty("other"));
		assertFalse(cpp.containsProperty("x"));

		val = cpp.getProperty("other", String.class);
		assertEquals("test", val);

		// prefixed

		PrefixedConfigPropertyProvider pc = new PrefixedConfigPropertyProvider(cfg, "test.");

		assertFalse(pc.containsProperty("other"));
		assertTrue(pc.containsProperty("prop"));
		assertEquals("testval", pc.getProperty("prop", String.class));

	}

	@Test
	public void testConfigPropertyNames() {

		BeanConfigProperties ps = BeanConfigProperties.builder().withDefaultPropertySources().build();

		String name = ps.getConfigPropertyName(BeanConfigProperties.BEAN_INTROSPECTOR_CACHE_ENABLED);

		assertNotNull(name);
		assertEquals(ps.getName() + "." + BeanConfigProperties.BEAN_INTROSPECTOR_CACHE_ENABLED.getKey(), name);

	}

}
