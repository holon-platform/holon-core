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
package com.holonplatform.auth.jwt;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.keys.KeyEncoding;
import com.holonplatform.auth.keys.KeyFormat;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.config.ConfigPropertySet;
import com.holonplatform.core.internal.config.DefaultConfigPropertySet;

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
	 * Required JWT issuer
	 */
	public static final ConfigProperty<String> ISSUER = ConfigProperty.create("issuer", String.class);

	// ------- Sign

	/**
	 * JWT signature algorithm name. Must be one of those listed in the {@link JwtSignatureAlgorithm} enumeration.
	 * @see JwtSignatureAlgorithm
	 */
	public static final ConfigProperty<JwtSignatureAlgorithm> SIGNATURE_ALGORITHM = ConfigProperty
			.create("signature-algorithm", JwtSignatureAlgorithm.class);

	// ------- Shared key

	/**
	 * JWT sign shared key (base64 encoded)
	 */
	public static final ConfigProperty<String> SHARED_KEY = ConfigProperty.create("sharedkey-base64", String.class);

	// ------- Key pairs

	public static final String KEY_SOURCE_FILE_PREFIX = "file:";
	public static final String KEY_SOURCE_CLASSPATH_PREFIX = "classpath:";

	// ------- Private key

	/**
	 * JWT sign private key
	 */
	public static final ConfigProperty<String> PRIVATE_KEY_SOURCE = ConfigProperty.create("privatekey.source",
			String.class);

	/**
	 * JWT sign private key format. Must be one of {@link KeyFormat} enumeration values.
	 * <p>
	 * The default private key format is {@link KeyFormat#PKCS8}.
	 * </p>
	 */
	public static final ConfigProperty<KeyFormat> PRIVATE_KEY_FORMAT = ConfigProperty.create("privatekey.format",
			KeyFormat.class);

	/**
	 * JWT sign private key encoding. Must be one of {@link KeyEncoding} enumeration values.
	 * <p>
	 * The default private key encoding is {@link KeyEncoding#BASE64}.
	 * </p>
	 */
	public static final ConfigProperty<KeyEncoding> PRIVATE_KEY_ENCODING = ConfigProperty.create("privatekey.encoding",
			KeyEncoding.class);

	/**
	 * The key store password to use when the JWT sign private key is provided using a key store format, such as
	 * {@link KeyFormat#PKCS12}
	 */
	public static final ConfigProperty<String> PRIVATE_KEY_STORE_PASSWORD = ConfigProperty
			.create("privatekey.store.password", String.class);

	/**
	 * The key store alias name to use when the JWT sign private key is provided using a key store format, such as
	 * {@link KeyFormat#PKCS12}
	 */
	public static final ConfigProperty<String> PRIVATE_KEY_STORE_ALIAS = ConfigProperty.create("privatekey.store.alias",
			String.class);

	/**
	 * The key recovering password to use when the JWT sign private key is provided using a key store format, such as
	 * {@link KeyFormat#PKCS12}
	 */
	public static final ConfigProperty<String> PRIVATE_KEY_STORE_ALIAS_PASSWORD = ConfigProperty
			.create("privatekey.store.alias-password", String.class);

	// ------- Public key

	/**
	 * JWT sign public key
	 */
	public static final ConfigProperty<String> PUBLIC_KEY_SOURCE = ConfigProperty.create("publickey.source",
			String.class);

	/**
	 * JWT sign public key format. Must be one of {@link KeyFormat} enumeration values.
	 * <p>
	 * The default public key format is {@link KeyFormat#X509}.
	 * </p>
	 */
	public static final ConfigProperty<KeyFormat> PUBLIC_KEY_FORMAT = ConfigProperty.create("publickey.format",
			KeyFormat.class);

	/**
	 * JWT sign public key encoding. Must be one of {@link KeyEncoding} enumeration values.
	 * <p>
	 * The default public key encoding is {@link KeyEncoding#BASE64}.
	 * </p>
	 */
	public static final ConfigProperty<KeyEncoding> PUBLIC_KEY_ENCODING = ConfigProperty.create("publickey.encoding",
			KeyEncoding.class);

	/**
	 * The key store password to use when the JWT sign public key is provided using a key store format, such as
	 * {@link KeyFormat#PKCS12}
	 */
	public static final ConfigProperty<String> PUBLIC_KEY_STORE_PASSWORD = ConfigProperty
			.create("publickey.store.password", String.class);

	/**
	 * The key store alias name to use when the JWT sign public key is provided using a key store format, such as
	 * {@link KeyFormat#PKCS12}
	 */
	public static final ConfigProperty<String> PUBLIC_KEY_STORE_ALIAS = ConfigProperty.create("publickey.store.alias",
			String.class);

	/**
	 * The key recovering password to use when the JWT sign public key is provided using a key store format, such as
	 * {@link KeyFormat#PKCS12}
	 */
	public static final ConfigProperty<String> PUBLIC_KEY_STORE_ALIAS_PASSWORD = ConfigProperty
			.create("publickey.store.alias-password", String.class);

	// ------- Deprecated

	/**
	 * JWT sign public key (base64 encoded)
	 * @deprecated Use {@link #PUBLIC_KEY_SOURCE} and related configuration properties to configure the JWT sign public
	 *             key. The key encoding can be specified using the {@link #PUBLIC_KEY_ENCODING} configuration property
	 */
	@Deprecated
	public static final ConfigProperty<String> PUBLIC_KEY = ConfigProperty.create("publickey-base64", String.class);

	/**
	 * JWT sign public key (file name)
	 * @deprecated Use {@link #PUBLIC_KEY_SOURCE} and related configuration properties to configure the JWT sign public
	 *             key. The key source can be specified using the default prefixes {@link #KEY_SOURCE_FILE_PREFIX} and
	 *             {@link #KEY_SOURCE_CLASSPATH_PREFIX}
	 */
	@Deprecated
	public static final ConfigProperty<String> PUBLIC_KEY_FILE = ConfigProperty.create("publickey-file", String.class);

	/**
	 * JWT sign private key (base64 encoded)
	 * @deprecated Use {@link #PRIVATE_KEY_SOURCE} and related configuration properties to configure the JWT sign
	 *             private key. The key encoding can be specified using the {@link #PRIVATE_KEY_ENCODING} configuration
	 *             property
	 */
	@Deprecated
	public static final ConfigProperty<String> PRIVATE_KEY = ConfigProperty.create("privatekey-base64", String.class);

	/**
	 * JWT sign private key (file name)
	 * @deprecated Use {@link #PRIVATE_KEY_SOURCE} and related configuration properties to configure the JWT sign
	 *             private key. The key source can be specified using the default prefixes
	 *             {@link #KEY_SOURCE_FILE_PREFIX} and {@link #KEY_SOURCE_CLASSPATH_PREFIX}
	 */
	@Deprecated
	public static final ConfigProperty<String> PRIVATE_KEY_FILE = ConfigProperty.create("privatekey-file",
			String.class);

	// ------ Expiring

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

	// ------ Authentication

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

	// -------

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
