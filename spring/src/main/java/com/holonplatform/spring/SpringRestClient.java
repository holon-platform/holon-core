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
package com.holonplatform.spring;

import org.springframework.web.client.RestTemplate;

import com.holonplatform.http.RestClient;
import com.holonplatform.spring.internal.rest.RestTemplateRestClient;

/**
 * A {@link RestClient} using Spring {@link RestTemplate}.
 *
 * @since 5.0.0
 */
public interface SpringRestClient extends RestClient {

	/**
	 * Get the {@link RestTemplate} bound to this RestClient.
	 * @return The RestTemplate
	 */
	RestTemplate getRestTemplate();

	/**
	 * Create a {@link RestClient} using given <code>restTemplate</code>.
	 * @param restTemplate RestTemplate to use to perform invocations (not null)
	 * @return A new RestClient instance
	 */
	static RestClient create(RestTemplate restTemplate) {
		return new RestTemplateRestClient(restTemplate);
	}

}
