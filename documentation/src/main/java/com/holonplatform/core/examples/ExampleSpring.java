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
package com.holonplatform.core.examples;

import java.net.URISyntaxException;
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

@SuppressWarnings("unused")
public class ExampleSpring {

	// tag::context[]
	class TestResource {
	}

	@Configuration
	@EnableBeanContext
	class SpringConfig {

		@Bean(name = "testResource")
		public TestResource testResource() {
			return new TestResource();
		}

	}

	public void getContextResource() {
		// lookup using resurce name which matches the bean name
		Optional<TestResource> resource = Context.get().resource("testResource", TestResource.class);
		// lookup by type
		resource = Context.get().resource(TestResource.class);
	}
	// end::context[]

	// tag::tenant[]
	@Configuration
	@EnableTenantScope
	class TenantScopeConfig {

		@Bean
		public TenantResolver tenantResolver() {
			return () -> Optional.of("test"); // provide a meaningful current tenant id resolution strategy...
		}

		@Bean
		@ScopeTenant
		public TestResource testResource() {
			// a different instance of the bean will be provided for each tenant id
			return new TestResource();
		}

	}
	// end::tenant[]

	// tag::env[]
	@Autowired
	Environment environment;

	public void env() {
		// build a ConfigPropertyProvider using Spring Environment as property source
		ConfigPropertyProvider provider = EnvironmentConfigPropertyProvider.create(environment);

		String value = provider.getProperty("test.property.name", String.class);
	}
	// end::env[]

	// tag::restclient[]
	@Autowired
	RestTemplate restTemplate;

	public void restclient() throws URISyntaxException {

		// Create a SpringRestClient
		RestClient client = SpringRestClient.create(restTemplate);

		// obtain the RestClient using default creation methods
		client = RestClient.create();

		client = RestClient.create(SpringRestClient.class.getName());
	}
	// end::restclient[]

}
