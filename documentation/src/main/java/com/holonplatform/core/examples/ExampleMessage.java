/*
 * Copyright 2016-2018 Axioma srl.
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import com.holonplatform.http.Cookie;
import com.holonplatform.http.HttpMessage;
import com.holonplatform.http.HttpMethod;
import com.holonplatform.http.HttpRequest;
import com.holonplatform.http.HttpResponse;
import com.holonplatform.http.HttpStatus;
import com.holonplatform.http.servlet.ServletHttpRequest;

@SuppressWarnings("unused")
public class ExampleMessage {

	public void httpHeaders() {
		// tag::httpheaders[]
		HttpMessage<String> message = getMessage();

		Optional<String> value = message.getHeaderValue("HEADER_NAME"); // <1>
		Optional<Date> date = message.getDate(); // <2>
		Optional<URI> location = message.getLocation(); // <3>
		Optional<Long> length = message.getContentLength(); // <4>
		Optional<Locale> locale = message.getLocale(); // <5>
		List<Locale> locales = message.getLocales(); // <6>

		Optional<String[]> basicAuth = message.getAuthorizationBasicCredentials(); // <7>
		Optional<String> bearerAuth = message.getAuthorizationBearer(); // <8>
		// end::httpheaders[]
	}

	@SuppressWarnings("resource")
	public void httpRequest() throws UnsupportedOperationException, IOException {
		// tag::httprequest[]
		HttpRequest message = getRequestMessage();

		HttpMethod method = message.getMethod(); // <1>
		String path = message.getRequestPath(); // <2>
		Optional<String> value = message.getRequestParameter("param1"); // <3>
		Optional<List<String>> values = message.getMultiValueRequestParameter("param2"); // <4>
		Optional<Cookie> cookie = message.getRequestCookie("cookie1"); // <5>

		Optional<String> body = message.getPayload(); // <6>
		InputStream bodyAsStream = message.getBody(); // <7>
		// end::httprequest[]
	}

	public void httpResponse() {
		// tag::httpresponse[]
		HttpResponse<String> message = getResponseMessage();

		int statusCode = message.getStatusCode(); // <1>
		HttpStatus status = message.getStatus(); // <2>
		// end::httpresponse[]
	}

	public void servletRequest() {
		// tag::servletrequest[]
		HttpServletRequest servletRequest = getServletRequest();

		HttpRequest request = ServletHttpRequest.create(servletRequest); // <1>
		// end::servletrequest[]
	}

	private static HttpMessage<String> getMessage() {
		return null;
	}

	private static HttpRequest getRequestMessage() {
		return null;
	}

	private static HttpResponse<String> getResponseMessage() {
		return null;
	}

	private static HttpServletRequest getServletRequest() {
		return null;
	}

}
