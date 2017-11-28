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

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.holonplatform.http.HttpStatus;
import com.holonplatform.http.rest.ResponseEntity;
import com.holonplatform.http.rest.RestClient;
import com.holonplatform.spring.EnableBeanContext;
import com.holonplatform.spring.SpringRestClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestRestClientSecurity.Config.class)
public class TestRestClientSecurity extends JerseyTest {

	@Configuration
	@EnableBeanContext
	static class Config {

		@Bean
		public RestTemplate restTemplate() {
			RestTemplate rt = new RestTemplate();
			rt.getMessageConverters().add(new FormHttpMessageConverter());
			return rt;
		}

	}

	public TestRestClientSecurity() {
		super();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

	@Path("test")
	public static class TestResource {

		@PermitAll
		@GET
		@Path("permit")
		public Response permit() {
			return Response.ok().build();
		}

		@RolesAllowed("TEST")
		@GET
		@Path("role")
		public Response role() {
			return Response.ok().build();
		}

		@DenyAll
		@GET
		@Path("deny")
		public Response deny() {
			return Response.ok().build();
		}

	}

	@Autowired
	private RestTemplate restTemplate;

	@Override
	protected Application configure() {
		ResourceConfig cfg = new ResourceConfig(TestResource.class);
		cfg.register(RolesAllowedDynamicFeature.class);
		return cfg;
	}

	@Test
	public void testClient() {

		final RestClient client = SpringRestClient.create(restTemplate).defaultTarget(getBaseUri());

		ResponseEntity<?> rsp = client.request().path("test").path("permit").get(Void.class);
		assertEquals(HttpStatus.OK, rsp.getStatus());

		rsp = client.request().path("test").path("role").get(Void.class);
		assertEquals(HttpStatus.FORBIDDEN, rsp.getStatus());

		rsp = client.request().path("test").path("deny").get(Void.class);
		assertEquals(HttpStatus.FORBIDDEN, rsp.getStatus());

	}

}
