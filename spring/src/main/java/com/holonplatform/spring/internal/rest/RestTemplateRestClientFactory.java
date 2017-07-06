/*
 * Copyright 2000-2017 Holon TDCN.
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
package com.holonplatform.spring.internal.rest;

import java.util.Optional;

import javax.annotation.Priority;

import org.springframework.web.client.RestTemplate;

import com.holonplatform.core.Context;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.http.exceptions.RestClientCreationException;
import com.holonplatform.http.rest.RestClient;
import com.holonplatform.http.rest.RestClientFactory;
import com.holonplatform.spring.SpringRestClient;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * {@link RestClientFactory} to create {@link RestTemplateRestClient} instances.
 *
 * @since 5.0.0
 */
@Priority(RestClientFactory.DEFAULT_PRIORITY + 200)
public class RestTemplateRestClientFactory implements RestClientFactory {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = SpringLogger.create();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientFactory#getRestClientImplementationClass()
	 */
	@Override
	public Class<?> getRestClientImplementationClass() {
		return SpringRestClient.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.rest.RestClientFactory#create(java.lang.ClassLoader)
	 */
	@Override
	public RestClient create(ClassLoader classLoader) throws RestClientCreationException {
		// Try to obtain a RestTemplate
		Optional<RestTemplate> restTemplate = Context.get().resource("restTemplate", RestTemplate.class, classLoader);
		if (restTemplate.isPresent()) {
			return new RestTemplateRestClient(restTemplate.get());
		}
		LOGGER.debug(() -> "No RestTemplate type Context resource available - RestClient creation skipped");
		return null;
	}

}
