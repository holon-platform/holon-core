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
package com.holonplatform.spring.boot.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;

import com.holonplatform.http.rest.RestClient;
import com.holonplatform.spring.SpringRestClient;

@SpringBootTest
public class TestRestClientConfiguration {

	@Configuration
	@EnableAutoConfiguration
	protected static class Config {

	}

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	@Test
	public void testConfig() {
		assertNotNull(restTemplateBuilder);

		RestClient rc = RestClient.create();
		assertNotNull(rc);

		rc = RestClient.create(SpringRestClient.class.getName());
		assertNotNull(rc);
	}

}
