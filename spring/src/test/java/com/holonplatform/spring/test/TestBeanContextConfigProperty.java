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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.holonplatform.core.Context;
import com.holonplatform.core.ContextScope;
import com.holonplatform.spring.EnableBeanContext;
import com.holonplatform.spring.internal.context.BeanFactoryScope;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestBeanContextConfigProperty.Config.class)
@DirtiesContext
public class TestBeanContextConfigProperty {

	@Configuration
	@PropertySource("test.properties")
	@EnableBeanContext(lookupByType = false)
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

	}

}
