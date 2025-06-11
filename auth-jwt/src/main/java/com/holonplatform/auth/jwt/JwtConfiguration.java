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

import java.io.Serializable;
import java.security.Key;
import java.util.Base64;
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
	 * Get the JWT token issuer (iss)
	 * @return Optional issuer
	 */
	Optional<String> getIssuer();

	/**
	 * Get the JWT token signature algorithm.
	 * @return The signature algorithm, or {@link JwtSignatureAlgorithm#NONE} if the JWT token is not signed
	 */
	JwtSignatureAlgorithm getSignatureAlgorithm();

	/**
	 * Get the shared key to be used with symmetric JWT signature algorithms.
	 * @return Optional JWT signing shared key
	 */
	Optional<byte[]> getSharedKey();

	/**
	 * Get the private key to be used with asymmetric JWT signature algorithms.
	 * @return Optional private key
	 */
	Optional<Key> getPrivateKey();

	/**
	 * Get the public key to be used with asymmetric JWT signature algorithms.
	 * @return Optional public key
	 */
	Optional<Key> getPublicKey();

	/**
	 * Get JWT token expire time.
	 * @return The expire time in milliseconds. <code>&lt;=0</code> means token never expires
	 */
	long getExpireTime();

	/**
	 * Get whether to set the <code>nbf</code> (not before) JWT claim to the timestamp at which the token is created.
	 * @return <code>true</code> to set the <code>nbf</code> (not before) JWT claim to the timestamp at which the token
	 *         is created
	 */
	boolean isNotBeforeNow();

	/**
	 * Get whether to include {@link Authentication} details in JWT token at token generation time.
	 * @return <code>true</code> to include {@link Authentication} details in JWT token, <code>false</code> otherwise
	 */
	boolean isIncludeDetails();

	/**
	 * Get whether to include {@link Authentication} permissions in JWT token at token generation time.
	 * @return <code>true</code> to include {@link Authentication} permissions in JWT token, <code>false</code>
	 *         otherwise
	 */
	boolean isIncludePermissions();
	
	/**
	 * Get whether an unsecured JWS is allowed.
	 * @return <code>true</code> if unsecured is allowed, <code>false</code>
	 *         otherwise
	 */
	boolean isAllowUnsecured();

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
		 * Set JWT token signature algorithm
		 * @param signatureAlgorithm Signature algorithm
		 * @return this
		 */
		Builder signatureAlgorithm(JwtSignatureAlgorithm signatureAlgorithm);

		/**
		 * Set JWT signing shared key to use with symmetric signing algorithms (such as HMAC)
		 * @param sharedKey the key to set
		 * @return this
		 */
		Builder sharedKey(byte[] sharedKey);

		/**
		 * Set JWT signing shared key to use with symmetric signing algorithms (such as HMAC)
		 * @param sharedKeyBase64encoded the key (Base64 encoded) to set
		 * @return this
		 */
		default Builder sharedKeyBase64(String sharedKeyBase64encoded) {
			return sharedKey(Base64.getDecoder().decode(sharedKeyBase64encoded));
		}

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
		 * Whether to set the <code>nbf</code> (not before) JWT claim to the timestamp at which the token is created.
		 * @param notBeforeNow <code>true</code> to set the <code>nbf</code> (not before) JWT claim to the timestamp at
		 *        which the token is created.
		 * @return this
		 */
		Builder notBeforeNow(boolean notBeforeNow);

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
		 * Set whether to allow unsecured Jws that is Jws with alg:none
		 * @param allowUnsecured <code>true</code> to allow unsecured
		 * @return this
		 */
		Builder allowUnsecuredJws(boolean allowUnsecured);

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
