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
package com.holonplatform.spring.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
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

import com.holonplatform.core.internal.utils.TestUtils;
import com.holonplatform.http.HttpResponse;
import com.holonplatform.http.HttpStatus;
import com.holonplatform.http.exceptions.UnsuccessfulResponseException;
import com.holonplatform.http.rest.RequestEntity;
import com.holonplatform.http.rest.ResponseEntity;
import com.holonplatform.http.rest.RestClient;
import com.holonplatform.spring.SpringRestClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestRestClient.Config.class)
public class TestRestClient extends JerseyTest {

	@Configuration
	static class Config {

		@Bean
		public RestTemplate restTemplate() {
			RestTemplate rt = new RestTemplate();
			rt.getMessageConverters().add(new FormHttpMessageConverter());
			return rt;
		}

	}

	public TestRestClient() {
		super();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

	@Path("test")
	public static class TestResource {

		@GET
		@Path("data/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		public TestData getData(@PathParam("id") int id) {
			return new TestData(id, "value" + id);
		}
		
		@GET
		@Path("data2/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		public Response getData2(@PathParam("id") int id) {
			if (id < 0) {
				return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
						.entity(new ApiError("ERR000", "Invalid data")).build();
			}
			return Response.ok().type(MediaType.APPLICATION_JSON).entity(new TestData(id, "value" + id)).build();
		}

		@GET
		@Path("data")
		@Produces(MediaType.APPLICATION_JSON)
		public List<TestData> getBoxes() {
			List<TestData> boxes = new LinkedList<>();
			boxes.add(new TestData(1, "One"));
			boxes.add(new TestData(2, "Two"));
			return boxes;
		}

		@POST
		@Path("formParams")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response formParams(@FormParam("one") String one, @FormParam("two") Integer two) {
			assertNotNull(one);
			assertNotNull(two);
			if (!two.toString().equals(one)) {
				return Response.status(Status.BAD_REQUEST).build();
			}
			return Response.ok().build();
		}

		@PUT
		@Path("data/save")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response saveData(TestData data) {
			if (data == null || data.getCode() < 0) {
				return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
						.entity(new ApiError("ERR000", "Invalid data")).build();
			}
			return Response.accepted().build();
		}

	}

	public static class TestData {

		private int code;
		private String value;

		public TestData() {
			super();
		}

		public TestData(int code, String value) {
			super();
			this.code = code;
			this.value = value;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + code;
			return result;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestData other = (TestData) obj;
			if (code != other.code)
				return false;
			return true;
		}

	}
	
	public static class ApiError {

		private String code;
		private String message;

		public ApiError() {
			super();
		}

		public ApiError(String code, String message) {
			super();
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
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

		TestData td = client.request().path("test").path("data/{id}").resolve("id", 1).getForEntity(TestData.class).orElse(null);
		assertNotNull(td);
		assertEquals(1, td.getCode());

		List<TestData> tds = client.request().path("test").path("data").getAsList(TestData.class);
		assertNotNull(td);
		assertEquals(2, tds.size());

		HttpResponse<?> rsp = client.request().path("test").path("data/save")
				.put(RequestEntity.json(new TestData(7, "testPost")));
		assertNotNull(rsp);
		assertEquals(HttpStatus.ACCEPTED, rsp.getStatus());

		rsp = client.request().path("test").path("formParams")
				.post(RequestEntity.form(RequestEntity.formBuilder().set("one", "1").set("two", "1").build()));
		assertNotNull(rsp);
		assertEquals(HttpStatus.OK, rsp.getStatus());
	}
	
	@Test
	public void testInputStream() {
		final RestClient client = SpringRestClient.create(restTemplate).defaultTarget(getBaseUri());
		
		ResponseEntity<TestData> res = client.request().path("test").path("data2/{id}").resolve("id", 1)
				.get(TestData.class);
		assertNotNull(res);
		
		TestData td = res.getPayload().orElse(null);
		assertNotNull(td);
		
		String asString = res.as(String.class).orElse(null);
		assertNotNull(asString);
		
		td = res.as(TestData.class).orElse(null);
		assertNotNull(td);
		
	}
	
	@Test
	public void testErrors() {
		final RestClient client = SpringRestClient.create(restTemplate).defaultTarget(getBaseUri());

		ResponseEntity<TestData> r2 = client.request().path("test").path("data2/{id}").resolve("id", -1)
				.get(TestData.class);
		assertNotNull(r2);
		assertEquals(HttpStatus.BAD_REQUEST, r2.getStatus());

		ApiError error = r2.as(ApiError.class).orElse(null);
		assertNotNull(error);
		assertEquals("ERR000", error.getCode());

		TestUtils.expectedException(UnsuccessfulResponseException.class, () -> {
			client.request().path("test").path("data2/{id}").resolve("id", -1).getForEntity(TestData.class).orElse(null);
		});

		try {
			client.request().path("test").path("data2/{id}").resolve("id", -1).getForEntity(TestData.class).orElse(null);
		} catch (UnsuccessfulResponseException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatus().orElse(null));
			assertNotNull(e.getResponse());

			ApiError err = e.getResponse().as(ApiError.class).orElse(null);
			assertNotNull(err);
			assertEquals("ERR000", err.getCode());
		}
		
		
		ResponseEntity<?> rsp = client.request().path("test").path("data/save")
				.put(RequestEntity.json(new TestData(-1, "testErr")));

		assertNotNull(rsp);
		assertEquals(HttpStatus.BAD_REQUEST, rsp.getStatus());

		error = rsp.as(ApiError.class).orElse(null);
		assertNotNull(error);
		assertEquals("ERR000", error.getCode());
		
	}

}
