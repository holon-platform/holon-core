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
package com.holonplatform.auth.internal;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import com.holonplatform.auth.Credentials;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.internal.utils.Hash;

/**
 * Default {@link Credentials} implementation
 *
 * @since 5.0.0
 */
public class DefaultCredentials implements Credentials {

	private static final long serialVersionUID = 6946829369159399255L;

	private byte[] secret;
	private String hashAlgorithm;
	private byte[] salt;
	private int hashIterations;
	private boolean base64Encoded = false;
	private boolean hexEncoded = false;
	private Date expireDate;

	/**
	 * Constructor
	 */
	public DefaultCredentials() {
		super();
	}

	/**
	 * Constructor
	 * @param secret Secret (e.g. password)
	 */
	public DefaultCredentials(String secret) {
		this(ConversionUtils.toBytes(secret), null, null, 0);
	}

	/**
	 * Constructor
	 * @param secret Secret (e.g. password)
	 * @param hashAlgorithm Hash algorithm used to encode secret
	 */
	public DefaultCredentials(String secret, String hashAlgorithm) {
		this(ConversionUtils.toBytes(secret), hashAlgorithm, null, 0);
	}

	/**
	 * Constructor
	 * @param secret Secret (e.g. password)
	 * @param hashAlgorithm Hash algorithm used to encode secret
	 * @param salt Salt used in secret hashing
	 * @param hashIterations Hash iterations performed to encode secret
	 */
	public DefaultCredentials(String secret, String hashAlgorithm, byte[] salt, int hashIterations) {
		this(ConversionUtils.toBytes(secret), hashAlgorithm, salt, hashIterations);
	}

	/**
	 * Constructor
	 * @param secret Secret (e.g. password)
	 * @param hashAlgorithm Hash algorithm used to encode secret
	 * @param salt Salt used in secret hashing
	 * @param hashIterations Hash iterations performed to encode secret
	 */
	public DefaultCredentials(String secret, String hashAlgorithm, String salt, int hashIterations) {
		this(ConversionUtils.toBytes(secret), hashAlgorithm, ConversionUtils.toBytes(salt), hashIterations);
	}

	/**
	 * Constructor
	 * @param secret Secret (e.g. password)
	 */
	public DefaultCredentials(byte[] secret) {
		this(secret, null, null, 0);
	}

	/**
	 * Constructor
	 * @param secret Secret (e.g. password)
	 * @param hashAlgorithm Hash algorithm used to encode secret
	 */
	public DefaultCredentials(byte[] secret, String hashAlgorithm) {
		this(secret, hashAlgorithm, null, 0);
	}

	/**
	 * Constructor
	 * @param secret Secret (e.g. password)
	 * @param hashAlgorithm Hash algorithm used to encode secret
	 * @param salt Salt used in secret hashing
	 * @param hashIterations Hash iterations performed to encode secret
	 */
	public DefaultCredentials(byte[] secret, String hashAlgorithm, byte[] salt, int hashIterations) {
		super();
		this.secret = secret;
		this.hashAlgorithm = hashAlgorithm;
		this.salt = salt;
		this.hashIterations = hashIterations;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.credentials.Credential#getSecret()
	 */
	@Override
	public byte[] getSecret() {
		return secret;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.credentials.Credential#getHashAlgorithm()
	 */
	@Override
	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.credentials.Credential#getSalt()
	 */
	@Override
	public byte[] getSalt() {
		return salt;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.credentials.Credential#getHashIterations()
	 */
	@Override
	public int getHashIterations() {
		return hashIterations;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.credentials.Credential#isBase64Encoded()
	 */
	@Override
	public boolean isBase64Encoded() {
		return base64Encoded;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.credentials.Credential#isHexEncoded()
	 */
	@Override
	public boolean isHexEncoded() {
		return hexEncoded;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.credentials.Credential#getExpireDate()
	 */
	@Override
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * Set secret
	 * @param secret the secret to set
	 */
	public void setSecret(byte[] secret) {
		this.secret = secret;
	}

	/**
	 * Set secret as UTF-8 String
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = ConversionUtils.toBytes(secret);
	}

	/**
	 * Set hash algorithm name
	 * @param hashAlgorithm the hashAlgorithm to set
	 */
	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	/**
	 * Set salt data used to hash secret
	 * @param salt the salt to set
	 */
	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	/**
	 * Set salt as UTF-8 String
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = ConversionUtils.toBytes(salt);
	}

	/**
	 * Set hash iterations performed to encode secret
	 * @param hashIterations the hashIterations to set
	 */
	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}

	/**
	 * Set whether secret is encoded using Base64
	 * @param base64Encoded <code>true</code> if secret is encoded using Base64
	 */
	public void setBase64Encoded(boolean base64Encoded) {
		this.base64Encoded = base64Encoded;
	}

	/**
	 * Set whether secret is hex encoded
	 * @param hexEncoded <code>true</code> if secret is hex encoded
	 */
	public void setHexEncoded(boolean hexEncoded) {
		this.hexEncoded = hexEncoded;
	}

	/**
	 * Set credential expire date
	 * @param expireDate Expire date
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * Default {@link Builder} implementation.
	 */
	public static class CredentialsBuilder implements Builder {

		private final DefaultCredentials credentials;

		/**
		 * Constrcuctor
		 */
		public CredentialsBuilder() {
			super();
			this.credentials = new DefaultCredentials();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#secret(java.lang.String)
		 */
		@Override
		public Builder secret(String secret) {
			this.credentials.setSecret(secret);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#secret(byte[])
		 */
		@Override
		public Builder secret(byte[] secret) {
			this.credentials.setSecret(secret);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#hashAlgorithm(java.lang.String)
		 */
		@Override
		public Builder hashAlgorithm(String hashAlgorithm) {
			this.credentials.setHashAlgorithm(hashAlgorithm);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#hashIterations(int)
		 */
		@Override
		public Builder hashIterations(int hashIterations) {
			this.credentials.setHashIterations(hashIterations);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#salt(java.lang.String)
		 */
		@Override
		public Builder salt(String salt) {
			this.credentials.setSalt(salt);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#salt(byte[])
		 */
		@Override
		public Builder salt(byte[] salt) {
			this.credentials.setSalt(salt);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#expireDate(java.util.Date)
		 */
		@Override
		public Builder expireDate(Date expireDate) {
			this.credentials.setExpireDate(expireDate);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#base64Encoded()
		 */
		@Override
		public Builder base64Encoded() {
			this.credentials.setBase64Encoded(true);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#hexEncoded()
		 */
		@Override
		public Builder hexEncoded() {
			this.credentials.setHexEncoded(true);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#build()
		 */
		@Override
		public Credentials build() {
			return credentials;
		}

	}

	// Encoder

	/**
	 * Default {@link Encoder} implementation.
	 */
	public static class CredentialsEncoder implements Encoder {

		private String secret;
		private byte[] salt;
		private String algorithmName;
		private int iterations = 1;
		private String charset = "UTF-8";

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#secret(java.lang.String)
		 */
		@Override
		public Encoder secret(String secret) {
			this.secret = secret;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#salt(byte[])
		 */
		@Override
		public Encoder salt(byte[] salt) {
			this.salt = salt;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.credentials.CredentialsBuilder#salt(java.lang.String)
		 */
		@Override
		public Encoder salt(String salt) {
			if (salt != null) {
				this.salt = ConversionUtils.toBytes(salt);
			}
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#hashAlgorithm(java.lang.String)
		 */
		@Override
		public Encoder hashAlgorithm(String algorithm) {
			this.algorithmName = algorithm;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#hashMD5()
		 */
		@Override
		public Encoder hashMD5() {
			return hashAlgorithm(HASH_MD5);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#hashSHA1()
		 */
		@Override
		public Encoder hashSHA1() {
			return hashAlgorithm(HASH_SHA_1);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#hashSHA256()
		 */
		@Override
		public Encoder hashSHA256() {
			return hashAlgorithm(HASH_SHA_256);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#hashSHA384()
		 */
		@Override
		public Encoder hashSHA384() {
			return hashAlgorithm(HASH_SHA_384);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#hashSHA512()
		 */
		@Override
		public Encoder hashSHA512() {
			return hashAlgorithm(HASH_SHA_512);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#hashIterations(int)
		 */
		@Override
		public Encoder hashIterations(int iterations) {
			this.iterations = iterations;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#charset(java.lang.String)
		 */
		@Override
		public Encoder charset(String charset) {
			this.charset = charset;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#build()
		 */
		@Override
		public byte[] build() {
			if (secret == null) {
				throw new IllegalStateException("Missing secret text to hash");
			}

			try {
				String cs = (charset != null) ? charset : "UTF-8";
				byte[] secretBytes = ConversionUtils.toBytes(secret, cs);

				if (algorithmName == null) {
					return secretBytes;
				}

				// hash secret
				return Hash.hash(algorithmName, secretBytes, salt, iterations);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.credentials.CredentialsBuilder#buildAndEncodeBase64()
		 */
		@Override
		public String buildAndEncodeBase64() {
			return Base64.getEncoder().encodeToString(build());
		}

	}

}
