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
package com.holonplatform.auth;

import java.io.Serializable;
import java.util.Date;

import com.holonplatform.auth.internal.DefaultCredentials;
import com.holonplatform.core.internal.utils.Hash;

/**
 * Interface to represent credential data (such a passwords or account keys)
 * 
 * @since 5.0.0
 */
public interface Credentials extends Serializable {

	/**
	 * Get secret data (e.g. password)
	 * @return Secret
	 */
	byte[] getSecret();

	/**
	 * Get hash algorithm used to encode secret. If <code>null</code>, means secret is not hashed and {@link #getSalt()}
	 * or {@link #getHashIterations()} must be ignored.
	 * @return Hash algorithm name
	 */
	String getHashAlgorithm();

	/**
	 * Get salt data used to hash secret
	 * @return Salt data
	 */
	byte[] getSalt();

	/**
	 * Get hash iterations performed for secret encoding
	 * @return Hash iterations
	 */
	int getHashIterations();

	/**
	 * Whether secret is encoded using Base64. If <code>true</code> and hash algorithm is specified, any salt data will
	 * be considered Base64 encoded too.
	 * @return <code>true</code> if secret is encoded using Base64
	 */
	boolean isBase64Encoded();

	/**
	 * Whether secret is encoded using hexademical representation
	 * @return <code>true</code> if secret is hex encoded
	 */
	boolean isHexEncoded();

	/**
	 * Credential expire date
	 * @return Credential expire date, or <code>null</code> if credential never expires
	 */
	Date getExpireDate();

	// Builder and encoder

	/**
	 * Builder to create Credentials instance
	 * @return CredentialsBuilder
	 */
	static Builder builder() {
		return new DefaultCredentials.CredentialsBuilder();
	}

	/**
	 * Credentials encoder
	 * @return CredentialsEncoder
	 */
	static Encoder encoder() {
		return new DefaultCredentials.CredentialsEncoder();
	}

	/**
	 * Builder to create {@link Credentials} instances.
	 */
	public interface Builder {

		/**
		 * Set secret (e.g. password)
		 * @param secret Secret to set
		 * @return this
		 */
		Builder secret(String secret);

		/**
		 * Set secret (e.g. password)
		 * @param secret Secret to set as byte array
		 * @return this
		 */
		Builder secret(byte[] secret);

		/**
		 * Set hash algorithm name used to encode secret
		 * @param hashAlgorithm Hash algorithm name
		 * @return this
		 */
		Builder hashAlgorithm(String hashAlgorithm);

		/**
		 * Set hash iterations performed for secret encoding
		 * @param hashIterations Hash iterations
		 * @return this
		 */
		Builder hashIterations(int hashIterations);

		/**
		 * Set salt data used to hash secret
		 * @param salt Salt string
		 * @return this
		 */
		Builder salt(String salt);

		/**
		 * Set salt data used to hash secret
		 * @param salt Salt data as byte array
		 * @return this
		 */
		Builder salt(byte[] salt);

		/**
		 * Set credential expire date
		 * @param expireDate Expire date
		 * @return this
		 */
		Builder expireDate(Date expireDate);

		/**
		 * Set secret is encoded using Base64. If hash algorithm is specified, any salt data will be considered Base64
		 * encoded too.
		 * @return this
		 */
		Builder base64Encoded();

		/**
		 * Set secret is encoded using hexademical representation
		 * @return this
		 */
		Builder hexEncoded();

		/**
		 * Build Credentials instance
		 * @return Credentials
		 */
		Credentials build();

	}

	/**
	 * Builder to encode credentials using hash for secure storing.
	 */
	public interface Encoder {

		/**
		 * MD2 hash algorithm name
		 */
		public static final String HASH_MD2 = "MD2";
		/**
		 * MD5 hash algorithm name
		 */
		public static final String HASH_MD5 = "MD5";
		/**
		 * SHA-1 hash algorithm name
		 */
		public static final String HASH_SHA_1 = "SHA-1";
		/**
		 * SHA-256 hash algorithm name
		 */
		public static final String HASH_SHA_256 = "SHA-256";
		/**
		 * SHA-384 hash algorithm name
		 */
		public static final String HASH_SHA_384 = "SHA-384";
		/**
		 * SHA-512 hash algorithm name
		 */
		public static final String HASH_SHA_512 = "SHA-512";

		/**
		 * Secret text to encode
		 * @param secret Secret text (for example a password)
		 * @return this
		 */
		Encoder secret(String secret);

		/**
		 * Optional salt to use for hashing
		 * @param salt Salt data
		 * @return this
		 * @see Hash#generateSalt()
		 */
		Encoder salt(byte[] salt);

		/**
		 * Optional salt to use for hashing
		 * @param salt Salt string
		 * @return this
		 * @see Hash#generateSalt()
		 */
		Encoder salt(String salt);

		/**
		 * Hash algorithm to use. Default is SHA-256
		 * @param algorithm Hash algorithm name
		 * @return this
		 */
		Encoder hashAlgorithm(String algorithm);

		/**
		 * Apply MD5 hash algorithm
		 * @return this
		 */
		Encoder hashMD5();

		/**
		 * Apply SHA-1 hash algorithm
		 * @return this
		 */
		Encoder hashSHA1();

		/**
		 * Apply SHA-256 hash algorithm
		 * @return this
		 */
		Encoder hashSHA256();

		/**
		 * Apply SHA-384 hash algorithm
		 * @return this
		 */
		Encoder hashSHA384();

		/**
		 * Apply SHA-512 hash algorithm
		 * @return this
		 */
		Encoder hashSHA512();

		/**
		 * Set hash iterations to perform. Default is 1.
		 * @param iterations Hash iterations
		 * @return this
		 */
		Encoder hashIterations(int iterations);

		/**
		 * Secret encoding charset name. By default UTF-8 is assumed.
		 * @param charset Charset name
		 * @return this
		 */
		Encoder charset(String charset);

		/**
		 * Hash given secret
		 * @return Hashed secret as bytes array
		 */
		byte[] build();

		/**
		 * Hash given secret and encode using Base64
		 * @return Base64 encoded hashed secret
		 */
		String buildAndEncodeBase64();

	}

}
