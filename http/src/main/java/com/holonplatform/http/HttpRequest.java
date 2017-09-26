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
package com.holonplatform.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a generic HTTP request
 * 
 * @since 5.0.0
 */
public interface HttpRequest extends HttpMessage<String> {

	/**
	 * Get the request method
	 * @return Request method
	 */
	HttpMethod getMethod();

	/**
	 * Get the path of the current request relative to the base URI as a string
	 * @return The relative URI path (may be null)
	 */
	String getRequestPath();

	/**
	 * Get the fully qualified name of the client host or the last proxy that sent the request.
	 * @return The request host
	 */
	String getRequestHost();

	/**
	 * Get a request URI query parameter by name. If parameter is multi-value, the values are joined together and
	 * separated by a ',' character.
	 * @param name Parameter name
	 * @return Parameter value, or an empty Optional if not found
	 */
	Optional<String> getRequestParameter(String name);

	/**
	 * Get a request URI query parameter by name as multi-value
	 * @param name Parameter name
	 * @return Parameter values, or an empty Optional if not found
	 */
	Optional<List<String>> getMultiValueRequestParameter(String name);

	/**
	 * Get all request URI query parameters
	 * @return Map of query parameters names and values
	 */
	Map<String, List<String>> getRequestParameters();

	/**
	 * Get a request Cookie by name
	 * @param name Cookie name (not null)
	 * @return Cookie with given name, or an empty Optional if not present
	 */
	Optional<Cookie> getRequestCookie(String name);

	/**
	 * Get request body input stream
	 * @return Request body input stream
	 * @throws IOException I/O error
	 * @throws UnsupportedOperationException If underlying concrete implementation does not support message content
	 *         access
	 */
	InputStream getBody() throws IOException, UnsupportedOperationException;

}
