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
package com.holonplatform.auth.jwt.internal;

import java.security.Key;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.jwt.JwtConfiguration;

/**
 * {@link JwtConfiguration} implementation
 * 
 * @since 5.0.0
 */
public class DefaultJwtConfiguration implements JwtConfiguration {

	private static final long serialVersionUID = -4719475334415057906L;

	/*
	 * Issuer
	 */
	private String issuer;

	/*
	 * Signature algorithm
	 */
	private String signatureAlgorithm;

	/*
	 * Shared key
	 */
	private byte[] sharedKey;

	/*
	 * Public key
	 */
	private Key publicKey;

	/*
	 * Private key
	 */
	private Key privateKey;

	/*
	 * Expire time
	 */
	private long expireTime;

	/*
	 * Include authentication details
	 */
	private boolean includeDetails;

	/*
	 * Include authentication permissions
	 */
	private boolean includePermissions;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jaxrs.server.jwt.JwtConfiguration#getIssuer()
	 */
	@Override
	public String getIssuer() {
		return issuer;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jaxrs.server.jwt.JwtConfiguration#getSignatureAlgorithm()
	 */
	@Override
	public String getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jaxrs.server.jwt.JwtConfiguration#getSharedKey()
	 */
	@Override
	public byte[] getSharedKey() {
		return sharedKey;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jaxrs.server.jwt.JwtConfiguration#getPublicKey()
	 */
	@Override
	public Key getPublicKey() {
		return publicKey;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jaxrs.server.jwt.JwtConfiguration#getPrivateKey()
	 */
	@Override
	public Key getPrivateKey() {
		return privateKey;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jaxrs.server.jwt.JwtConfiguration#getExpireTime()
	 */
	@Override
	public long getExpireTime() {
		return expireTime;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jaxrs.server.jwt.JwtConfiguration#isIncludeDetails()
	 */
	@Override
	public boolean isIncludeDetails() {
		return includeDetails;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jaxrs.server.jwt.JwtConfiguration#isIncludePermissions()
	 */
	@Override
	public boolean isIncludePermissions() {
		return includePermissions;
	}

	/**
	 * JWT token issuer
	 * @param issuer the issuer to set
	 */
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	/**
	 * Set JWT token signature algorithm name
	 * @param signatureAlgorithm Signature algorithm name
	 */
	public void setSignatureAlgorithm(String signatureAlgorithm) {
		this.signatureAlgorithm = signatureAlgorithm;
	}

	/**
	 * Set JWT signing shared key to use with symmetric signing algorithms (such as HMAC)
	 * @param sharedKey the key to set
	 */
	public void setSharedKey(byte[] sharedKey) {
		this.sharedKey = sharedKey;
	}

	/**
	 * Set JWT signing public key to use with asymmetric signing algorithms (such as RSA)
	 * @param publicKey the key to set
	 */
	public void setPublicKey(Key publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * Set JWT signing private key to use with asymmetric signing algorithms (such as RSA)
	 * @param privateKey the key to set
	 */
	public void setPrivateKey(Key privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * Set JWT token expire time
	 * @param expireTime Expire time in milliseconds
	 */
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	/**
	 * Set whether to include {@link Authentication} details in JWT token generation
	 * @param includeDetails <code>true</code> to include {@link Authentication} details
	 */
	public void setIncludeDetails(boolean includeDetails) {
		this.includeDetails = includeDetails;
	}

	/**
	 * Set whether to include {@link Authentication} permissions in JWT token generation
	 * @param includePermissions <code>true</code> to include {@link Authentication} permissions
	 */
	public void setIncludePermissions(boolean includePermissions) {
		this.includePermissions = includePermissions;
	}

	// Builder

	/**
	 * Default {@link Builder} implementation.
	 */
	public static class ConfigurationBuilder implements Builder {

		private final DefaultJwtConfiguration configuration;

		/**
		 * Constructor
		 */
		public ConfigurationBuilder() {
			super();
			this.configuration = new DefaultJwtConfiguration();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtConfigurationBuilder#issuer(java.lang.String)
		 */
		@Override
		public Builder issuer(String issuer) {
			this.configuration.setIssuer(issuer);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtConfigurationBuilder#signatureAlgorithm(java.lang.String)
		 */
		@Override
		public Builder signatureAlgorithm(String signatureAlgorithm) {
			this.configuration.setSignatureAlgorithm(signatureAlgorithm);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtConfigurationBuilder#sharedKey(byte[])
		 */
		@Override
		public Builder sharedKey(byte[] sharedKey) {
			this.configuration.setSharedKey(sharedKey);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtConfigurationBuilder#publicKey(java.security.Key)
		 */
		@Override
		public Builder publicKey(Key publicKey) {
			this.configuration.setPublicKey(publicKey);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtConfigurationBuilder#privateKey(java.security.Key)
		 */
		@Override
		public Builder privateKey(Key privateKey) {
			this.configuration.setPrivateKey(privateKey);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtConfigurationBuilder#expireTime(long)
		 */
		@Override
		public Builder expireTime(long expireTime) {
			this.configuration.setExpireTime(expireTime);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtConfigurationBuilder#includeDetails(boolean)
		 */
		@Override
		public Builder includeDetails(boolean includeDetails) {
			this.configuration.setIncludeDetails(includeDetails);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtConfigurationBuilder#includePermissions(boolean)
		 */
		@Override
		public Builder includePermissions(boolean includePermissions) {
			this.configuration.setIncludePermissions(includePermissions);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtConfigurationBuilder#build()
		 */
		@Override
		public JwtConfiguration build() {
			return configuration;
		}

	}

}
