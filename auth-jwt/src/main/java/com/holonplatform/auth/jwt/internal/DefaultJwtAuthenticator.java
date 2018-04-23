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
package com.holonplatform.auth.jwt.internal;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.ExpiredCredentialsException;
import com.holonplatform.auth.exceptions.InvalidTokenException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.auth.jwt.AuthenticationClaims;
import com.holonplatform.auth.jwt.JwtAuthenticator;
import com.holonplatform.auth.jwt.JwtConfiguration;
import com.holonplatform.auth.jwt.JwtSignatureAlgorithm;
import com.holonplatform.auth.token.BearerAuthenticationToken;
import com.holonplatform.core.internal.utils.ObjectUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Default {@link JwtAuthenticator} implementation.
 * 
 * @since 5.0.0
 */
public class DefaultJwtAuthenticator implements JwtAuthenticator {

	/**
	 * JWT configuration
	 */
	private JwtConfiguration configuration;

	/**
	 * JWT issuers to check
	 */
	private final Collection<String> issuers = new LinkedList<>();

	/**
	 * JWT required claims
	 */
	private final Collection<String> requiredClaims = new LinkedList<>();

	/**
	 * Constructor
	 */
	public DefaultJwtAuthenticator() {
		super();
	}

	/**
	 * Add an allowed JWT issuer
	 * @param issuer Issuer to add
	 */
	protected void addIssuer(String issuer) {
		issuers.add(issuer);
	}

	/**
	 * Add a required claim
	 * @param requiredClaim Claim to add
	 */
	protected void addRequiredClaim(String requiredClaim) {
		requiredClaims.add(requiredClaim);
	}

	/**
	 * Set the JWT configuration
	 * @param configuration the JWT configuration to set
	 */
	protected void setConfiguration(JwtConfiguration configuration) {
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.jwt.JwtAuthenticator#getConfiguration()
	 */
	@Override
	public JwtConfiguration getConfiguration() {
		return configuration;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.jwt.JwtAuthenticator#getIssuers()
	 */
	@Override
	public Collection<String> getIssuers() {
		return issuers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.jwt.JwtAuthenticator#getRequiredClaims()
	 */
	@Override
	public Collection<String> getRequiredClaims() {
		return requiredClaims;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.authc.AuthenticationTokenResolver#getTokenType()
	 */
	@Override
	public Class<? extends BearerAuthenticationToken> getTokenType() {
		return BearerAuthenticationToken.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.authc.AuthenticationTokenResolver#authenticate(com.holonplatform.auth.
	 * AuthenticationToken)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Authentication authenticate(BearerAuthenticationToken authenticationToken) throws AuthenticationException {

		// check configuration
		if (getConfiguration() == null) {
			throw new UnexpectedAuthenticationException(
					"JWT authenticator not correctly configured: missing JWTConfiguration");
		}

		// validate
		if (authenticationToken == null) {
			throw new UnexpectedAuthenticationException("Null authentication token");
		}

		// Get JWT token
		String jwt = (String) authenticationToken.getCredentials();
		if (jwt == null) {
			throw new UnexpectedAuthenticationException("Missing JWT token");
		}

		// decode and get claims

		Claims claims = null;

		try {

			JwtParser parser = Jwts.parser();

			if (getConfiguration().getSignatureAlgorithm() != JwtSignatureAlgorithm.NONE) {
				// Token expected to be signed (JWS)
				if (getConfiguration().getSignatureAlgorithm().isSymmetric()) {
					parser = parser.setSigningKey(getConfiguration().getSharedKey()
							.orElseThrow(() -> new UnexpectedAuthenticationException(
									"JWT authenticator not correctly configured: missing shared key for symmetric signature algorithm ["
											+ getConfiguration().getSignatureAlgorithm().getDescription()
											+ "] - JWT configuration: [" + getConfiguration() + "]")));
				} else {
					parser = parser.setSigningKey(getConfiguration().getPublicKey()
							.orElseThrow(() -> new UnexpectedAuthenticationException(
									"JWT authenticator not correctly configured: missing public key for asymmetric signature algorithm ["
											+ getConfiguration().getSignatureAlgorithm().getDescription()
											+ "] - JWT configuration: [" + getConfiguration() + "]")));
				}
				claims = parser.parseClaimsJws(jwt).getBody();
			} else {
				// not signed (JWT)
				claims = parser.parseClaimsJwt(jwt).getBody();
			}

		} catch (@SuppressWarnings("unused") ExpiredJwtException eje) {
			throw new ExpiredCredentialsException("Expired JWT token");
		} catch (@SuppressWarnings("unused") MalformedJwtException | UnsupportedJwtException mje) {
			throw new InvalidTokenException("Malformed or unsupported JWT token");
		} catch (@SuppressWarnings("unused") SignatureException sje) {
			throw new InvalidTokenException("Invalid JWT token signature");
		} catch (Exception e) {
			throw new UnexpectedAuthenticationException(ExceptionUtils.getRootCauseMessage(e), e);
		}

		// check claims
		if (claims == null) {
			throw new UnexpectedAuthenticationException("No valid claims found in JWT token");
		}

		String principalName = claims.getSubject();
		if (principalName == null) {
			throw new UnknownAccountException("No principal id (subject) found in JWT token");
		}

		// check required claims
		Collection<String> required = getRequiredClaims();
		if (required != null && !required.isEmpty()) {
			for (String claim : required) {
				Object value = claims.get(claim);
				if (value == null) {
					throw new InvalidTokenException("Missing required JWT claim: " + claim);
				}
			}
		}

		// check issuer

		Collection<String> issuers = getIssuers();
		if (issuers != null && !issuers.isEmpty()) {
			String tokenIssuer = claims.getIssuer();
			if (tokenIssuer == null) {
				throw new InvalidTokenException("Missing required JWT Issuer");
			}

			if (!issuers.contains(tokenIssuer)) {
				throw new InvalidTokenException("JWT Issuer mismatch");
			}
		}

		// build Authentication

		Authentication.Builder auth = Authentication.builder(principalName).scheme("Bearer").root(false);

		// set claims as details
		claims.forEach((n, v) -> {
			if (AuthenticationClaims.CLAIM_NAME_PERMISSIONS.equals(n)) {
				Collection<String> permissions = (Collection<String>) v;
				if (permissions != null) {
					permissions.forEach(p -> auth.permission(Permission.create(p)));
				}
			} else {
				auth.parameter(n, v);
			}
		});

		return auth.build();
	}

	// Builder

	/**
	 * Default {@link Builder} implementation
	 */
	public static class DefaultBuilder implements Builder {

		private final DefaultJwtAuthenticator authenticator;

		/**
		 * Constructor
		 */
		public DefaultBuilder() {
			super();
			this.authenticator = new DefaultJwtAuthenticator();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.JwtAuthenticator.Builder#configuration(com.holonplatform.auth.jwt.
		 * JwtConfiguration)
		 */
		@Override
		public Builder configuration(JwtConfiguration configuration) {
			ObjectUtils.argumentNotNull(configuration, "JwtConfiguration must be not null");
			this.authenticator.setConfiguration(configuration);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtAuthenticatorBuilder#issuer(java.lang.String)
		 */
		@Override
		public Builder issuer(String issuer) {
			ObjectUtils.argumentNotNull(issuer, "Issuer must be not null");
			this.authenticator.addIssuer(issuer);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtAuthenticatorBuilder#requiredClaim(java.lang.String)
		 */
		@Override
		public Builder requiredClaim(String claim) {
			ObjectUtils.argumentNotNull(claim, "Claim must be not null");
			this.authenticator.addRequiredClaim(claim);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.jwt.internal.JwtAuthenticatorBuilder#build()
		 */
		@Override
		public JwtAuthenticator build() {
			return authenticator;
		}

	}

}
