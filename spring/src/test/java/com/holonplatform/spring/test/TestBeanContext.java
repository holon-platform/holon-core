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
package com.holonplatform.spring.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.holonplatform.core.Context;
import com.holonplatform.core.ContextScope;
import com.holonplatform.spring.EnableBeanContext;
import com.holonplatform.spring.internal.context.BeanFactoryScope;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestBeanContext.Config.class)
@DirtiesContext
public class TestBeanContext {

	@Configuration
	@EnableBeanContext
	protected static class Config {

		@Bean
		public ResourceTest testResource() {
			return new ResourceTest(1);
		}

	}

	@Test
	public void testScope() {

		Optional<ContextScope> scope = Context.get().scope(BeanFactoryScope.NAME);
		assertTrue(scope.isPresent());

		Optional<ResourceTest> tr = Context.get().resource(ResourceTest.CONTEXT_KEY, ResourceTest.class);
		assertTrue(tr.isPresent());

		final ResourceTest thr = new ResourceTest(2);

		Context.get().threadScope().ifPresent((s) -> s.put(ResourceTest.CONTEXT_KEY, thr));

		tr = Context.get().resource(ResourceTest.CONTEXT_KEY, ResourceTest.class);
		assertTrue(tr.isPresent());
		assertEquals(thr, tr.get());

		Context.get().threadScope().ifPresent((s) -> s.remove(ResourceTest.CONTEXT_KEY));

		tr = Context.get().resource(ResourceTest.CONTEXT_KEY, ResourceTest.class);
		assertTrue(tr.isPresent());
		assertEquals(1, tr.get().getId());

	}

}
