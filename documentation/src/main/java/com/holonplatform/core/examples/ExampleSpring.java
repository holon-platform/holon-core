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
package com.holonplatform.core.examples;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.holonplatform.core.Context;
import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.http.rest.RestClient;
import com.holonplatform.spring.EnableBeanContext;
import com.holonplatform.spring.EnableTenantScope;
import com.holonplatform.spring.EnvironmentConfigPropertyProvider;
import com.holonplatform.spring.ScopeTenant;
import com.holonplatform.spring.SpringRestClient;
import com.holonplatform.spring.TenantScopeManager;

@SuppressWarnings("unused")
public class ExampleSpring {

	class TestResource {
	}

	// tag::context[]
	@Configuration
	@EnableBeanContext // <1>
	class SpringConfig {

		@Bean(name = "testResource") // <2>
		public TestResource testResource() {
			return new TestResource();
		}

	}

	public void getContextResource() {
		// lookup by name and type
		Optional<TestResource> resource = Context.get().resource("testResource", TestResource.class); // <3>
		// lookup by type
		resource = Context.get().resource(TestResource.class); // <4>
	}
	// end::context[]

	public void env() {
		// tag::env[]
		org.springframework.core.env.Environment environment = obtainSpringEnvironment(); // <1>

		// build a ConfigPropertyProvider using Spring Environment as property source
		ConfigPropertyProvider provider = EnvironmentConfigPropertyProvider.create(environment); // <2>

		String value = provider.getProperty("test.property.name", String.class); // <3>
		// end::env[]
	}

	private static Environment obtainSpringEnvironment() {
		return null;
	}

	// tag::tenant[]
	@Configuration
	@EnableTenantScope // <1>
	class TenantScopeConfig {

		@Bean
		public TenantResolver tenantResolver() { // <2>
			// provide a meaningful current tenant id resolution strategy...
			return () -> Optional.of("test");
		}

		@Bean
		@ScopeTenant // <3>
		public TestResource testResource() {
			// a different instance of the bean will be provided for each tenant id
			return new TestResource();
		}

	}
	// end::tenant[]

	// tag::scopemanager[]
	@Autowired
	TenantScopeManager tenantScopeManager;

	void discardTenantScopedBeans() {
		tenantScopeManager.discardTenantBeanStore("a_tenant_id"); // <1>
	}
	// end::scopemanager[]

	public void restclient1() {
		// tag::restclient1[]
		RestTemplate restTemplate = getRestTemplate(); // <1>

		RestClient client = SpringRestClient.create(restTemplate); // <2>
		// end::restclient1[]
	}

	// tag::restclient2[]
	@Configuration
	@EnableBeanContext // <1>
	class Config {

		@Bean // <2>
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}

	}

	void restclient() {
		RestClient client = RestClient.create(); // <3>

		client = RestClient.create(SpringRestClient.class.getName()); // <4>
	}
	// end::restclient2[]

	private static RestTemplate getRestTemplate() {
		return null;
	}

}
