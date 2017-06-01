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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.junit.Test;

import com.holonplatform.core.Context;
import com.holonplatform.core.beans.BeanConfigProperties;
import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.core.internal.config.DefaultConfig;
import com.holonplatform.core.internal.config.PrefixedConfigPropertyProvider;
import com.holonplatform.core.internal.config.PropertiesConfigProvider;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.TestUtils;
import com.holonplatform.core.tenancy.TenantResolver;

public class TestCore {

	@Test
	public void testCore() {
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

		// prefixed

		PrefixedConfigPropertyProvider pc = new PrefixedConfigPropertyProvider(cfg, "test.");

		assertFalse(pc.containsProperty("other"));
		assertTrue(pc.containsProperty("prop"));
		assertEquals("testval", pc.getProperty("prop", String.class));

	}

	@Test
	public void testTenant() {

		TenantResolver tr = TenantResolver.staticTenantResolver("test");

		assertTrue(tr.getTenantId().isPresent());
		assertEquals("test", tr.getTenantId().orElse(null));

		Optional<TenantResolver> resolver = Context.get().resource(TenantResolver.CONTEXT_KEY, TenantResolver.class);
		assertFalse(resolver.isPresent());

		Context.get().threadScope().map((s) -> s.put(TenantResolver.CONTEXT_KEY, tr));

		resolver = Context.get().resource(TenantResolver.CONTEXT_KEY, TenantResolver.class);
		assertTrue(resolver.isPresent());
		assertEquals("test", resolver.get().getTenantId().orElse(null));

		Context.get().threadScope().ifPresent(s -> s.remove(TenantResolver.CONTEXT_KEY));
		resolver = Context.get().resource(TenantResolver.CONTEXT_KEY, TenantResolver.class);
		assertFalse(resolver.isPresent());

	}

}
