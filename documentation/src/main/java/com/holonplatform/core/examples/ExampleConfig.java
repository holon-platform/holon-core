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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.core.internal.utils.ClassUtils;

@SuppressWarnings("unused")
public class ExampleConfig {

	public void configProperty() {

		// tag::prop[]
		ConfigProperty<String> property = ConfigProperty.create("test", String.class); // <1>
		
		String key = property.getKey(); // <2>
		
		Class<String> type = property.getType(); // <3>
		// end::prop[]

	}
	
	public void configPropertyProviders() throws IOException {
		
		// tag::provider[]
		Map<String, Object> values = new HashMap<>();
		ConfigPropertyProvider provider = ConfigPropertyProvider.using(values); // <1>
		
		Properties properties = new Properties();
		provider = ConfigPropertyProvider.using(properties); // <2>
		
		provider = ConfigPropertyProvider.using("config.properties", ClassUtils.getDefaultClassLoader()); // <3>
		
		provider = ConfigPropertyProvider.usingSystemProperties(); // <4>
		// end::provider[]
		
	}
	
	public void parameterSet() {
		// tag::params[]
		final ConfigProperty<String> property = ConfigProperty.create("test", String.class);
		
		ParameterSet set = ParameterSet.builder()
				.parameter("testParameter", 1L) // <1>
				.parameter(property, "testValue") // <2>
				.build();
		
		boolean present = set.hasParameter("testParameter"); // <3>
		present = set.hasNotNullParameter("testParameter"); // <4>
		
		set.getParameter(property).ifPresent(p -> p.toString()); // <5>
		// end::params[]
	}
	

}
