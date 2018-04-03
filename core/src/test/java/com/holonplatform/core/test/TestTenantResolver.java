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

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

import com.holonplatform.core.Context;
import com.holonplatform.core.tenancy.TenantResolver;

public class TestTenantResolver {

	@Test
	public void testContext() {

		final TenantResolver tr = () -> Optional.of("T1");

		Context.get().threadScope().map(s -> s.put(TenantResolver.CONTEXT_KEY, tr));

		assertEquals(tr, TenantResolver.getCurrent().orElse(null));

		TenantResolver.execute("T2", () -> {
			assertEquals("T2", TenantResolver.getCurrent().map(r -> r.getTenantId().orElse(null)).orElse(null));
		});

		String result = TenantResolver.execute("T3", () -> {
			assertEquals("T3", TenantResolver.getCurrent().map(r -> r.getTenantId().orElse(null)).orElse(null));
			return "T3";
		});
		assertEquals("T3", result);

	}

	@Test
	public void testTenantResolver() {

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
