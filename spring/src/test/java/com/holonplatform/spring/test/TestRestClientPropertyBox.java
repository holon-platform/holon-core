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

import static com.holonplatform.core.property.PathProperty.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.PropertySetRef;
import com.holonplatform.http.HttpResponse;
import com.holonplatform.http.HttpStatus;
import com.holonplatform.http.rest.RequestEntity;
import com.holonplatform.http.rest.RestClient;
import com.holonplatform.json.jackson.JacksonConfiguration;
import com.holonplatform.spring.EnableBeanContext;
import com.holonplatform.spring.SpringRestClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestRestClientPropertyBox.Config.class)
public class TestRestClientPropertyBox extends JerseyTest {

	public static final PathProperty<Integer> CODE = create("code", int.class);
	public static final PathProperty<String> VALUE = create("value", String.class);

	public static final PropertySet<?> PROPERTIES = PropertySet.of(CODE, VALUE);

	@Configuration
	@EnableBeanContext
	static class Config {

		@Bean
		public RestTemplate restTemplate() {
			final RestTemplate restTemplate = new RestTemplate();
			for (int i = 0; i < restTemplate.getMessageConverters().size(); i++) {
				final HttpMessageConverter<?> httpMessageConverter = restTemplate.getMessageConverters().get(i);
				if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
					restTemplate.getMessageConverters().set(i, mappingJackson2HttpMessageConverter());
				}
			}
			return restTemplate;
		}

		@Bean
		public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setObjectMapper(propertyBoxObjectMapper());
			return converter;
		}

		@Bean
		public ObjectMapper propertyBoxObjectMapper() {
			ObjectMapper om = new ObjectMapper();
			JacksonConfiguration.configure(om);
			return om;
		}

	}

	public TestRestClientPropertyBox() {
		super();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

	@Path("test")
	public static class TestResource {

		@GET
		@Path("box/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		public PropertyBox getBox(@PathParam("id") int id) {
			return PropertyBox.builder(PROPERTIES).set(CODE, id).set(VALUE, "value" + id).build();
		}

		@GET
		@Path("boxes")
		@Produces(MediaType.APPLICATION_JSON)
		public List<PropertyBox> getBoxes() {
			List<PropertyBox> boxes = new LinkedList<>();
			boxes.add(PropertyBox.builder(PROPERTIES).set(CODE, 1).set(VALUE, "value" + 1).build());
			boxes.add(PropertyBox.builder(PROPERTIES).set(CODE, 2).set(VALUE, "value" + 2).build());
			return boxes;
		}

		@POST
		@Path("box/post")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response postBox(@PropertySetRef(TestRestClientPropertyBox.class) PropertyBox box) {
			assertNotNull(box);
			return Response.accepted().build();
		}

		@PUT
		@Path("box/save")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response saveBox(@PropertySetRef(TestRestClientPropertyBox.class) PropertyBox box) {
			assertNotNull(box);
			return Response.accepted().build();
		}

	}

	@Autowired
	private RestTemplate restTemplate;

	@Override
	protected Application configure() {
		return new ResourceConfig(TestResource.class);
	}

	@Test
	public void testClient() {

		final RestClient client = SpringRestClient.create(restTemplate).defaultTarget(getBaseUri());

		PropertyBox box = client.request().path("test").path("box/{id}").resolve("id", 1).propertySet(PROPERTIES)
				.getForEntity(PropertyBox.class).orElse(null);
		assertNotNull(box);
		assertEquals(new Integer(1), box.getValue(CODE));
		assertEquals("value1", box.getValue(VALUE));

		HttpResponse<PropertyBox> rsp2 = client.request().path("test").path("box/{id}").resolve("id", 1)
				.propertySet(PROPERTIES).get(PropertyBox.class);
		assertEquals(HttpStatus.OK, rsp2.getStatus());
		box = rsp2.getPayload().orElse(null);
		assertNotNull(box);
		assertEquals(new Integer(1), box.getValue(CODE));
		assertEquals("value1", box.getValue(VALUE));

		List<PropertyBox> boxes = client.request().path("test").path("boxes").propertySet(PROPERTIES)
				.getAsList(PropertyBox.class);
		assertNotNull(boxes);
		assertEquals(2, boxes.size());

		box = boxes.get(0);
		assertNotNull(box);
		assertEquals(new Integer(1), box.getValue(CODE));
		assertEquals("value1", box.getValue(VALUE));

		box = boxes.get(1);
		assertNotNull(box);
		assertEquals(new Integer(2), box.getValue(CODE));
		assertEquals("value2", box.getValue(VALUE));

		List<Integer> codes = client.request().path("test").path("boxes").propertySet(PROPERTIES)
				.getAsList(PropertyBox.class).stream().map(p -> p.getValue(CODE)).collect(Collectors.toList());
		assertNotNull(codes);
		assertEquals(2, codes.size());

		List<String> values = client.request().path("test").path("boxes").propertySet(PROPERTIES)
				.getAsList(PropertyBox.class).stream().map(p -> p.getValue(VALUE)).collect(Collectors.toList());
		assertNotNull(values);
		assertEquals(2, values.size());

		PropertyBox postBox = PropertyBox.builder(PROPERTIES).set(CODE, 100).set(VALUE, "post").build();
		HttpResponse<Void> postResponse = client.request().path("test").path("box/post")
				.post(RequestEntity.json(postBox));
		assertEquals(HttpStatus.ACCEPTED, postResponse.getStatus());

	}

}
