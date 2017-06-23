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

import java.io.Serializable;
import java.security.Key;
import java.util.Optional;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.jwt.internal.DefaultJwtConfiguration;
import com.holonplatform.auth.jwt.internal.JwtUtils;
import com.holonplatform.core.Context;

/**
 * JWT generation/authentication configuration attributes.
 * 
 * @since 5.0.0
 */
public interface JwtConfiguration extends Serializable {

	/**
	 * Default {@link Context} resource reference
	 */
	public static final String CONTEXT_KEY = JwtConfiguration.class.getName();

	/**
	 * JWT token issuer (iss)
	 * @return Issuer
	 */
	String getIssuer();

	/**
	 * JWT token signature algorithm
	 * @return Signature algorithm name
	 */
	String getSignatureAlgorithm();

	/**
	 * JWT signing shared key for signature algorithms such as HMAC
	 * @return JWT signing shared key
	 */
	byte[] getSharedKey();

	/**
	 * JWT signing public key for signature algorithms such as RSA
	 * @return JWT signing public key
	 */
	Key getPublicKey();

	/**
	 * JWT signing private key for signature algorithms such as RSA
	 * @return JWT signing private key
	 */
	Key getPrivateKey();

	/**
	 * JWT token expire time
	 * @return Expire time in milliseconds. <code>&lt;=0</code> means token never expires
	 */
	long getExpireTime();

	/**
	 * Whether to include {@link Authentication} details in JWT token at generation time
	 * @return <code>true</code> to include {@link Authentication} details in JWT token
	 */
	boolean isIncludeDetails();

	/**
	 * Whether to include {@link Authentication} permissions in JWT token at generation time
	 * @return <code>true</code> to include {@link Authentication} permissions in JWT token
	 */
	boolean isIncludePermissions();

	// Context resource

	/**
	 * Convenience method to obtain the current {@link JwtConfiguration} made available as {@link Context} resource,
	 * using default {@link ClassLoader}.
	 * <p>
	 * See {@link Context#resource(String, Class)} for details about context resources availability conditions.
	 * </p>
	 * @return Optional JwtConfiguration, empty if not available as context resource
	 */
	static Optional<JwtConfiguration> getCurrent() {
		return Context.get().resource(CONTEXT_KEY, JwtConfiguration.class);
	}

	// Builders

	/**
	 * Builder to create a {@link JwtConfiguration}
	 * @return JwtConfiguration builder
	 */
	static Builder builder() {
		return new DefaultJwtConfiguration.ConfigurationBuilder();
	}

	/**
	 * Build a {@link JwtConfiguration} instance using given configuration <code>properties</code>.
	 * @param properties JWT configuration property source
	 * @return JwtConfiguration
	 * @throws InvalidJwtConfigurationException Error building configuration
	 */
	static JwtConfiguration build(JwtConfigProperties properties) throws InvalidJwtConfigurationException {
		return JwtUtils.buildFromConfig(properties);
	}

	/**
	 * Builder to create {@link JwtConfiguration} instances.
	 */
	public interface Builder {

		/**
		 * Set JWT token issuer
		 * @param issuer the issuer to set
		 * @return this
		 */
		Builder issuer(String issuer);

		/**
		 * Set JWT token signature algorithm name
		 * @param signatureAlgorithm Signature algorithm name
		 * @return this
		 */
		Builder signatureAlgorithm(String signatureAlgorithm);

		/**
		 * Set JWT signing shared key to use with symmetric signing algorithms (such as HMAC)
		 * @param sharedKey the key to set
		 * @return this
		 */
		Builder sharedKey(byte[] sharedKey);

		/**
		 * Set JWT signing public key to use with asymmetric signing algorithms (such as RSA)
		 * @param publicKey the key to set
		 * @return this
		 */
		Builder publicKey(Key publicKey);

		/**
		 * Set JWT signing private key to use with asymmetric signing algorithms (such as RSA)
		 * @param privateKey the key to set
		 * @return this
		 */
		Builder privateKey(Key privateKey);

		/**
		 * Set JWT token expire time
		 * @param expireTime Expire time in milliseconds
		 * @return this
		 */
		Builder expireTime(long expireTime);

		/**
		 * Set whether to include {@link Authentication} details in JWT token generation
		 * @param includeDetails <code>true</code> to include {@link Authentication} details
		 * @return this
		 */
		Builder includeDetails(boolean includeDetails);

		/**
		 * Set whether to include {@link Authentication} permissions in JWT token generation
		 * @param includePermissions <code>true</code> to include {@link Authentication} permissions
		 * @return this
		 */
		Builder includePermissions(boolean includePermissions);

		/**
		 * Build {@link JwtConfiguration} instance
		 * @return JwtConfiguration
		 */
		JwtConfiguration build();

	}

	/**
	 * Exception for {@link JwtConfiguration} errors.
	 */
	@SuppressWarnings("serial")
	public class InvalidJwtConfigurationException extends RuntimeException {

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public InvalidJwtConfigurationException(String message) {
			super(message);
		}

		/**
		 * Constructor with nested exception
		 * @param cause Nested exception
		 */
		public InvalidJwtConfigurationException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public InvalidJwtConfigurationException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
