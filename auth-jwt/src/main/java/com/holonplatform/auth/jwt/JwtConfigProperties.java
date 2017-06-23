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
package com.holonplatform.auth.jwt;

import com.holonplatform.auth.Authentication;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.config.ConfigPropertySet;
import com.holonplatform.core.internal.config.DefaultConfigPropertySet;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * A {@link ConfigPropertySet} for JWT configuration.
 *
 * @since 5.0.0
 */
public interface JwtConfigProperties extends ConfigPropertySet {

	/**
	 * Configuration property set name
	 */
	public static final String NAME = "holon.jwt";

	/**
	 * Default JWT expire time
	 */
	public static final long DEFAULT_EXPIRE_TIME = 86400000L; // 1 day

	/**
	 * Default JWT signature algorithm
	 */
	public static final String DEFAULT_SIGNATURE_ALGORITHM = "HS256"; // HMAC using SHA-256

	/**
	 * Required JWT issuer
	 */
	public static final ConfigProperty<String> ISSUER = ConfigProperty.create("issuer", String.class);

	/**
	 * JWT sign shared key (base64 encoded)
	 */
	public static final ConfigProperty<String> SHARED_KEY = ConfigProperty.create("sharedkey-base64", String.class);

	/**
	 * JWT sign public key (base64 encoded)
	 */
	public static final ConfigProperty<String> PUBLIC_KEY = ConfigProperty.create("publickey-base64", String.class);

	/**
	 * JWT sign public key (file name)
	 */
	public static final ConfigProperty<String> PUBLIC_KEY_FILE = ConfigProperty.create("publickey-file", String.class);

	/**
	 * JWT sign private key (base64 encoded)
	 */
	public static final ConfigProperty<String> PRIVATE_KEY = ConfigProperty.create("privatekey-base64", String.class);

	/**
	 * JWT sign private key (file name)
	 */
	public static final ConfigProperty<String> PRIVATE_KEY_FILE = ConfigProperty.create("privatekey-file",
			String.class);

	/**
	 * JWT signature algorithm name
	 * @see SignatureAlgorithm
	 */
	public static final ConfigProperty<String> SIGNATURE_ALGORITHM = ConfigProperty.create("signature-algorithm",
			String.class);

	/**
	 * JWT token expire time in milliseconds (numeric)
	 */
	public static final ConfigProperty<Long> EXPIRE_TIME_MS = ConfigProperty.create("expire-ms", Long.class);

	/**
	 * JWT token expire time in seconds (numeric)
	 */
	public static final ConfigProperty<Long> EXPIRE_TIME_SECONDS = ConfigProperty.create("expire-seconds", Long.class);
	/**
	 * JWT token expire time in minutes (numeric)
	 */
	public static final ConfigProperty<Long> EXPIRE_TIME_MINUTES = ConfigProperty.create("expire-minutes", Long.class);
	/**
	 * JWT token expire time in hours (numeric)
	 */
	public static final ConfigProperty<Long> EXPIRE_TIME_HOURS = ConfigProperty.create("expire-hours", Long.class);
	/**
	 * JWT token expire time in days (numeric)
	 */
	public static final ConfigProperty<Long> EXPIRE_TIME_DAYS = ConfigProperty.create("expire-days", Long.class);

	/**
	 * Include {@link Authentication} details in JWT token as claims (boolean)
	 */
	public static final ConfigProperty<Boolean> INCLUDE_DETAILS = ConfigProperty.create("include-details",
			boolean.class);

	/**
	 * Include {@link Authentication} permissions in JWT token as claims (boolean)
	 */
	public static final ConfigProperty<Boolean> INCLUDE_PERMISSIONS = ConfigProperty.create("include-permissions",
			boolean.class);

	@Override
	default String getName() {
		return NAME;
	}

	/**
	 * Builder to create property set instances bound to a property data source
	 * @return ConfigPropertySet builder
	 */
	static Builder<JwtConfigProperties> builder() {
		return new DefaultConfigPropertySet.DefaultBuilder<>(new JwtConfigPropertiesImpl());
	}

	/**
	 * Default implementation
	 */
	static class JwtConfigPropertiesImpl extends DefaultConfigPropertySet implements JwtConfigProperties {

		public JwtConfigPropertiesImpl() {
			super(NAME);
		}

	}

}
