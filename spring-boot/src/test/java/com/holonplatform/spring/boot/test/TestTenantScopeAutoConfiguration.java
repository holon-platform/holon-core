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
package com.holonplatform.spring.boot.test;

import java.io.Serializable;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.ScopeTenant;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestTenantScopeAutoConfiguration {

	private static final ThreadLocal<String> CURRENT_TENANT_ID = new ThreadLocal<>();

	@SuppressWarnings("serial")
	static class TestResource implements Serializable {
	}

	@Configuration
	@EnableAutoConfiguration
	protected static class Config {

		@Bean
		public TenantResolver testResolver() {
			return () -> Optional.ofNullable(CURRENT_TENANT_ID.get());
		}

		@Bean
		@ScopeTenant
		public TestResource testResource() {
			return new TestResource();
		}

	}

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testScope() {
		TestResource srv1;
		try {
			CURRENT_TENANT_ID.set("T1");
			srv1 = applicationContext.getBean(TestResource.class);
			Assert.assertNotNull(srv1);
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		TestResource srv2;
		try {
			CURRENT_TENANT_ID.set("T2");
			srv2 = applicationContext.getBean(TestResource.class);
			Assert.assertNotNull(srv2);
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		Assert.assertNotEquals(srv1, srv2);

		TestResource srv3;
		try {
			CURRENT_TENANT_ID.set("T1");
			srv3 = applicationContext.getBean(TestResource.class);
			Assert.assertNotNull(srv3);
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		Assert.assertEquals(srv1, srv3);
	}

}
