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

import static com.holonplatform.core.property.PathProperty.create;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.http.CacheControl;
import com.holonplatform.http.HttpHeaders;
import com.holonplatform.http.HttpStatus;
import com.holonplatform.http.MediaType;
import com.holonplatform.http.exceptions.UnsuccessfulResponseException;
import com.holonplatform.http.rest.RequestEntity;
import com.holonplatform.http.rest.ResponseEntity;
import com.holonplatform.http.rest.ResponseType;
import com.holonplatform.http.rest.RestClient;
import com.holonplatform.http.rest.RestClient.RequestDefinition;

@SuppressWarnings("unused")
public class ExampleRestClient {

	public void creation() throws URISyntaxException {
		// tag::creation[]
		// Create a RestClient using the default available implementation for current ClassLoader
		RestClient client = RestClient.create();

		// Create a RestClient using a specific implementation class name
		client = RestClient.create("com.holonplatform.jaxrs.client.JaxrsRestClient");

		// Create a RestClient using the default available implementation and set a default base URI
		client = RestClient.forTarget("https://host/api");
		// end::creation[]
	}

	public void configuration() throws URISyntaxException {
		// tag::configuration1[]
		RestClient client = RestClient.create();

		RequestDefinition request = client.request().target(URI.create("https://rest.api.example")); // <1>
		request = request.path("apimethod"); // <2>
		request = request.path("subpath"); // <3>
		// end::configuration1[]

		// tag::configuration2[]
		client.request().target("https://rest.api.example").path("/data/{name}/{id}").resolve("name", "test")
				.resolve("id", 123); // <1>

		Map<String, Object> templates = new HashMap<>(1);
		templates.put("id", "testValue");
		request = client.request().target("https://rest.api.example").path("/test/{id}").resolve(templates); // <2>
		// end::configuration2[]

		// tag::configuration3[]
		client.request().queryParameter("parameter", "value").queryParameter("multiValueParameter", 1, 2, 3);
		// end::configuration3[]

		// tag::configuration4[]
		client.request().header("Accept", "text/plain"); // Set an header providing name and value
		client.request().header(HttpHeaders.ACCEPT, "text/plain"); // Set an header providing name using HttpHeaders and
																	// value
		client.request().accept("text/plain", "text/xml"); // Set an Accept header with two values
		client.request().accept(MediaType.APPLICATION_JSON); // Set an Accept header using convenience method and
																// MediaType enumeration
		client.request().acceptEncoding("gzip"); // Set an Accept-Encoding header
		client.request().acceptCharset("utf-8"); // Set an Accept-Charset header
		client.request().acceptCharset(Charset.forName("utf-8")); // Set an Accept-Charset header
		client.request().acceptLanguage("en-CA"); // Set an Accept-Language header
		client.request().acceptLanguage(Locale.US, Locale.GERMANY); // Set an Accept-Language header using Locales
		client.request().cacheControl(CacheControl.builder().noCache(true).noStore(true).build()); // Set a
																									// Cache-Control
																									// header
		// end::configuration4[]

		// tag::configuration5[]
		client.request().authorizationBasic("username", "password"); // set an Authorization header using a Basic scheme
		client.request().authorizationBearer("An389fz56xsr7"); // set an Authorization header using a Bearer scheme
		// end::configuration5[]
	}

	public void defaults() throws URISyntaxException {
		// tag::defaults[]
		RestClient client = RestClient.create();

		client.defaultTarget(new URI("https://rest.api.example")); // <1>

		client.withDefaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-CA"); // <2>
		client.withDefaultHeader(HttpHeaders.ACCEPT_CHARSET, "utf-8"); // <3>
		// end::defaults[]
	}

	static class TestData {

	}

	public void methods() {
		// tag::post1[]
		ResponseEntity<Void> response1 = RestClient.forTarget("https://rest.api.example/testpost").request()
				.post(RequestEntity.json(new TestData()));

		ResponseEntity<TestData> response2 = RestClient.forTarget("https://rest.api.example/testpost").request()
				.post(RequestEntity.json(new TestData()), TestData.class);

		ResponseEntity<List<TestData>> response3 = RestClient.forTarget("https://rest.api.example/testpost").request()
				.post(RequestEntity.json(new TestData()), ResponseType.of(TestData.class, List.class));
		// end::post1[]

		// tag::get1[]
		try {

			Optional<TestData> data = RestClient.forTarget("https://rest.api.example/testget").request()
					.getForEntity(TestData.class);

			final ResponseType<List<TestData>> responseType = ResponseType.of(TestData.class, List.class);
			List<TestData> dataList = RestClient.forTarget("https://rest.api.example/testgetlist").request()
					.getForEntity(responseType).orElse(Collections.emptyList());

		} catch (UnsuccessfulResponseException e) {
			// got a response with a status code different from 2xx
			int httpStatusCode = e.getStatusCode();
			e.getStatus().ifPresent(status -> System.err.println(status.getDescription()));
			ResponseEntity<?> theResponse = e.getResponse();
		}
		// end::get1[]
	}

	@SuppressWarnings("rawtypes")
	public void request() {
		// tag::request[]
		RequestEntity<String> request1 = RequestEntity.text("test"); // <1>

		RequestEntity<TestData> request2 = RequestEntity.json(new TestData()); // <2>

		RequestEntity request3 = RequestEntity
				.form(RequestEntity.formBuilder().set("value1", "one").set("value2", "a", "b").build()); // <3>
		// end::request[]
	}

	public void restype() {
		// tag::restype[]
		ResponseType<List<TestData>> responseType = ResponseType.of(TestData.class, List.class);
		// end::restype[]
	}

	public void response() {
		// tag::response[]
		ResponseEntity<TestData> response = RestClient.forTarget("https://rest.api.example/testget").request()
				.accept(MediaType.APPLICATION_JSON).get(TestData.class); // <1>

		HttpStatus status = response.getStatus(); // <2>

		Optional<TestData> entity = response.getPayload(); // <3>

		Optional<String> asString = response.as(String.class); // <4>

		String header = response.getHeaderValue(HttpHeaders.LAST_MODIFIED).orElse(null); // <5>

		long contentLength = response.getContentLength().orElse(-1L); // <6>
		// end::response[]
	}

	// tag::properties[]
	static final PathProperty<Integer> CODE = create("code", int.class);
	static final PathProperty<String> VALUE = create("value", String.class);

	static final PropertySet<?> PROPERTIES = PropertySet.of(CODE, VALUE);

	public void propertiesInvocation() {
		RestClient client = RestClient.create();

		PropertyBox box = client.request().target("https://rest.api.example").path("/apimethod").propertySet(PROPERTIES)
				.getForEntity(PropertyBox.class).orElse(null); // <1>

		List<PropertyBox> boxes = client.request().target("https://rest.api.example").path("/apimethod")
				.propertySet(PROPERTIES).getAsList(PropertyBox.class); // <2>

	}
	// end::properties[]

}
