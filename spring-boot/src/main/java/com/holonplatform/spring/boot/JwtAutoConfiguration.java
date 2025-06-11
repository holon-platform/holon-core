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
package com.holonplatform.spring.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.holonplatform.auth.jwt.JwtConfigProperties;
import com.holonplatform.auth.jwt.JwtConfiguration;
import com.holonplatform.spring.EnvironmentConfigPropertyProvider;
import com.holonplatform.spring.boot.internal.ConditionalOnPropertyPrefix;

/**
 * Spring boot auto-configuration class to setup a {@link JwtConfiguration} bean (if not already present in the Spring
 * application context) using the <code>holon.jwt.*</code> configuration property set.
 * 
 * @since 5.0.0
 */
@AutoConfiguration
@ConditionalOnClass(name = "com.holonplatform.auth.jwt.JwtConfiguration")
public class JwtAutoConfiguration {

	@Configuration
	@ConditionalOnMissingBean(JwtConfiguration.class)
	@ConditionalOnPropertyPrefix("holon.jwt")
	public static class JwtConfigurationConfiguration {

		@Autowired
		private Environment environment;

		@Bean
		@ConditionalOnMissingBean
		public JwtConfiguration jwtConfiguration() {
			return JwtConfiguration.build(JwtConfigProperties.builder()
					.withPropertySource(EnvironmentConfigPropertyProvider.create(environment)).build());
		}

	}

}
