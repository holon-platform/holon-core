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
package com.holonplatform.spring.boot.test;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.holonplatform.core.Context;
import com.holonplatform.core.ContextScope;
import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.internal.context.BeanFactoryScope;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestBeanContextAutoConfiguration {

	@Configuration
	@EnableAutoConfiguration
	protected static class Config {

		@Bean
		public TenantResolver testResolver() {
			return TenantResolver.staticTenantResolver("test");
		}

	}

	@Test
	public void testScope() {

		Optional<ContextScope> scope = Context.get().scope(BeanFactoryScope.NAME);
		assertTrue(scope.isPresent());

		Optional<TenantResolver> tr = Context.get().resource(TenantResolver.CONTEXT_KEY, TenantResolver.class);
		assertTrue(tr.isPresent());

	}

}
