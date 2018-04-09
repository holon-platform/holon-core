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

import static com.holonplatform.core.property.PathProperty.create;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
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
		RestClient client = RestClient.create(); // <1>

		client = RestClient.create("com.holonplatform.jaxrs.client.JaxrsRestClient"); // <2>

		client = RestClient.forTarget("https://host/api"); // <3>
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
		client.request().queryParameter("parameter", "value") // <1>
				.queryParameter("multiValueParameter", 1, 2, 3); // <2>
		// end::configuration3[]

		// tag::configuration4[]
		client.request().header("Accept", "text/plain"); // <1>
		client.request().header(HttpHeaders.ACCEPT, "text/plain"); // <2>
		client.request().accept("text/plain", "text/xml"); // <3>
		client.request().accept(MediaType.APPLICATION_JSON); // <4>
		client.request().acceptEncoding("gzip"); // <5>
		client.request().acceptCharset("utf-8"); // <6>
		client.request().acceptCharset(Charset.forName("utf-8")); // <7>
		client.request().acceptLanguage("en-CA"); // <8>
		client.request().acceptLanguage(Locale.US, Locale.GERMANY); // <9>
		client.request().cacheControl(CacheControl.builder().noCache(true).noStore(true).build()); // <10>
		// end::configuration4[]

		// tag::configuration5[]
		client.request().authorizationBasic("username", "password"); // <1>
		client.request().authorizationBearer("An389fz56xsr7"); // <2>
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

	@SuppressWarnings("rawtypes")
	public void request() {
		// tag::request[]
		RequestEntity<String> request1 = RequestEntity.text("test"); // <1>

		RequestEntity<TestData> request2 = RequestEntity.json(new TestData()); // <2>

		RequestEntity request3 = RequestEntity
				.form(RequestEntity.formBuilder().set("value1", "one").set("value2", "a", "b").build()); // <3>
		// end::request[]
	}

	public void request2() {
		// tag::request2[]
		RequestEntity<?> emptyRequest = RequestEntity.EMPTY; // <1>
		// end::request2[]
	}

	public void restype() {
		// tag::restype[]
		ResponseType<TestData> responseType1 = ResponseType.of(TestData.class); // <1>

		ResponseType<List<TestData>> responseType2 = ResponseType.of(TestData.class, List.class); // <2>
		// end::restype[]
	}

	public void response1() {
		// tag::response1[]
		ResponseEntity<TestData> response = RestClient.forTarget("https://rest.api.example/testget").request()
				.accept(MediaType.APPLICATION_JSON).get(TestData.class); // <1>

		HttpStatus status = response.getStatus(); // <2>
		int statusCode = response.getStatusCode(); // <3>
		long contentLength = response.getContentLength().orElse(-1L); // <4>
		Optional<String> value = response.getHeaderValue("HEADER_NAME"); // <5>
		// end::response1[]
	}

	public void response2() {
		// tag::response2[]
		ResponseEntity<TestData> response = RestClient.forTarget("https://rest.api.example/testget").request()
				.accept(MediaType.APPLICATION_JSON).get(TestData.class); // <1>

		boolean hasEntity = response.getPayload().isPresent(); // <2>

		Optional<TestData> entity = response.getPayload(); // <3>

		Optional<String> asString = response.as(String.class); // <4>
		// end::response2[]
	}

	@SuppressWarnings("resource")
	public void invocationMethods() {
		// tag::methods1[]
		final RestClient client = RestClient.forTarget("https://rest.api.example/test");

		ResponseEntity<TestData> response = client.request().get(TestData.class); // <1>
		response = client.request().get(ResponseType.of(TestData.class)); // <2>

		response = client.request().put(RequestEntity.json(new TestData()), TestData.class); // <3>
		// end::methods1[]

		// tag::methods2[]
		ResponseEntity<Void> response2 = client.request().post(RequestEntity.json(new TestData())); // <1>
		HttpStatus status = response2.getStatus(); // <2>
		// end::methods2[]

		// tag::methods3[]
		Optional<TestData> value = client.request().getForEntity(TestData.class); // <1>
		Optional<List<TestData>> values = client.request().getForEntity(ResponseType.of(TestData.class, List.class)); // <2>
		// end::methods3[]

		// tag::methods4[]
		try {
			client.request().getForEntity(TestData.class);
		} catch (UnsuccessfulResponseException e) {
			// got a response with a status code different from 2xx
			int httpStatusCode = e.getStatusCode(); // <1>
			Optional<HttpStatus> sts = e.getStatus(); // <2>
			ResponseEntity<?> theResponse = e.getResponse(); // <3>
		}
		// end::methods4[]

		// tag::methods5[]
		InputStream responseEntityStream = client.request().getForStream();
		// end::methods5[]

		// tag::methods6[]
		List<TestData> collectionOfValues = client.request().getAsList(TestData.class);
		// end::methods6[]

		// tag::methods7[]
		Optional<URI> locationHeaderURI = client.request().postForLocation(RequestEntity.json(new TestData()));
		// end::methods7[]

		// tag::methodsx[]
		RestClient restClient = RestClient.forTarget("http://api.example"); // Obtain a RestClient
		restClient.request(); // Request definition
		// end::methodsx[]
	}

	@SuppressWarnings("unchecked")
	public void propertiesInvocation() {
		// tag::properties[]
		final PathProperty<Integer> CODE = create("code", int.class);
		final PathProperty<String> VALUE = create("value", String.class);
		final PropertySet<?> PROPERTIES = PropertySet.of(CODE, VALUE);

		RestClient client = RestClient.create();

		PropertyBox box = client.request().target("https://rest.api.example").path("/apimethod").propertySet(PROPERTIES)
				.getForEntity(PropertyBox.class).orElse(null); // <1>

		Optional<PropertyBox> box2 = client.request().target("https://rest.api.example").path("/apimethod")
				.propertySet(CODE, VALUE).getForEntity(PropertyBox.class); // <2>

		List<PropertyBox> boxes = client.request().target("https://rest.api.example").path("/apimethod")
				.propertySet(PROPERTIES).getAsList(PropertyBox.class); // <3>
		// end::properties[]
	}

}
