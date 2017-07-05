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
import com.holonplatform.http.HttpResponse;
import com.holonplatform.http.HttpStatus;
import com.holonplatform.http.MediaType;
import com.holonplatform.http.rest.RequestEntity;
import com.holonplatform.http.rest.RestClient;
import com.holonplatform.http.rest.RestClient.RequestDefinition;

@SuppressWarnings("unused")
public class ExampleRestClient {

	public void configuration() throws URISyntaxException {
		// tag::configuration1[]
		RestClient client = getRestClient(); // use one of the available implementations to obtain a RestClient instance

		RequestDefinition request = client.request().target(new URI("https://rest.api.example")); // <1>
		request = request.path("/apimethod"); // <2>
		// end::configuration1[]

		// tag::configuration2[]
		client.request().resolve("templateVar1", "value1").resolve("templateVar2", 123); // <1>

		Map<String, Object> templates = new HashMap<>(1);
		templates.put("templateVar3", "testValue");
		request = client.request().resolve(templates); // <2>
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
		RestClient client = getRestClient(); // use one of the available implementations to obtain a RestClient instance

		client.defaultTarget(new URI("https://rest.api.example")); // <1>

		client.withDefaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-CA"); // <2>
		// end::defaults[]
	}

	static class TestData {

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

	}

	public void invocation() throws URISyntaxException {
		// tag::invocation[]
		RestClient client = getRestClient(); // use one of the available implementations to obtain a RestClient instance

		HttpResponse<String> response = client.request().target(new URI("https://rest.api.example")).path("/apimethod")
				.accept(MediaType.TEXT_PLAIN).get(String.class); // <1>

		Optional<String> responseValue = client.request().target(new URI("https://rest.api.example")).path("/apimethod")
				.accept(MediaType.TEXT_PLAIN).getForEntity(String.class); // <2>

		List<TestData> responseValues = client.request().target(new URI("https://rest.api.example")).path("test/{id}")
				.resolve("id", 1).accept(MediaType.APPLICATION_JSON).getAsList(TestData.class); // <3>

		client = client.defaultTarget(new URI("https://rest.api.example"));

		HttpResponse<Void> postResponse = client.request().path("/apimethod")
				.post(RequestEntity.form(RequestEntity.formBuilder().set("one", "1").set("two", "1").build())); // <4>
		HttpStatus status = postResponse.getStatus(); // <5>
		Optional<URI> location = postResponse.getLocation(); // <6>

		HttpResponse<TestData> dataResponse = client.request().path("postdata").queryParameter("id", 1)
				.post(RequestEntity.EMPTY, TestData.class); // <7>

		HttpResponse<Void> putResponse = client.request().path("data/save")
				.put(RequestEntity.json(new TestData(7, "testPost"))); // <8>
		// end::invocation[]
	}

	// tag::properties[]
	static final PathProperty<Integer> CODE = create("code", int.class);
	static final PathProperty<String> VALUE = create("value", String.class);

	static final PropertySet<?> PROPERTIES = PropertySet.of(CODE, VALUE);

	public void propertiesInvocation() throws URISyntaxException {
		RestClient client = getRestClient(); // use one of the available implementations to obtain a RestClient instance

		PropertyBox box = client.request().target(new URI("https://rest.api.example")).path("/apimethod")
				.propertySet(PROPERTIES).getForEntity(PropertyBox.class).orElse(null); // <1>

		List<PropertyBox> boxes = client.request().target(new URI("https://rest.api.example")).path("/apimethod")
				.propertySet(PROPERTIES).getAsList(PropertyBox.class); // <2>

	}
	// end::properties[]

	@SuppressWarnings("static-method")
	private RestClient getRestClient() {
		return null;
	}

}
