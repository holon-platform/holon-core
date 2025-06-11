/*
 * Copyright 2016-2018 Axioma srl.
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

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Authentication.Builder;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.ExpiredCredentialsException;
import com.holonplatform.auth.exceptions.InvalidTokenException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.auth.jwt.AuthenticationClaims;
import com.holonplatform.auth.jwt.JwtConfiguration;
import com.holonplatform.auth.jwt.JwtConfiguration.InvalidJwtConfigurationException;
import com.holonplatform.auth.jwt.JwtSignatureAlgorithm;
import com.holonplatform.auth.jwt.JwtTokenParser;
import com.holonplatform.core.internal.utils.ObjectUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

/**
 * Default {@link JwtTokenParser} implementation.
 *
 * @since 5.1.1
 */
public enum DefaultJwtTokenParser implements JwtTokenParser {

	INSTANCE;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.jwt.JwtTokenParser#parseJwt(com.holonplatform.auth.jwt
	 * .JwtConfiguration, java.lang.String)
	 */
	@Override
	public Builder parseJwt(JwtConfiguration configuration, String jwt)
			throws InvalidJwtConfigurationException, AuthenticationException {

		ObjectUtils.argumentNotNull(configuration, "JwtConfiguration must be not null");
		ObjectUtils.argumentNotNull(jwt, "JWT token must be not null");

		// decode and get claims

		Claims claims = null;

		try {

			if (configuration.getSignatureAlgorithm() != JwtSignatureAlgorithm.NONE) {
				// Token expected to be signed (JWS)
				if (configuration.getSignatureAlgorithm().isSymmetric()) {
					claims = Jwts.parser().setSigningKey(configuration.getSharedKey()
							.orElseThrow(() -> new UnexpectedAuthenticationException(
									"JWT authenticator not correctly configured: missing shared key for symmetric signature algorithm ["
											+ configuration.getSignatureAlgorithm().getDescription()
											+ "] - JWT configuration: [" + configuration + "]")))
							.build().parseClaimsJws(jwt).getBody();
				} else {
					claims = Jwts.parser().setSigningKey(configuration.getPublicKey()
							.orElseThrow(() -> new UnexpectedAuthenticationException(
									"JWT authenticator not correctly configured: missing public key for asymmetric signature algorithm ["
											+ configuration.getSignatureAlgorithm().getDescription()
											+ "] - JWT configuration: [" + configuration + "]")))
							.build().parseClaimsJws(jwt).getBody();
				}
			} else {
				// not signed (JWT)

				// if JWT configuration explicitly defines unsecured JWS allowed (alg : none)
				if (configuration.isAllowUnsecured()) {
					claims = Jwts.parser().unsecured().build().parseClaimsJwt(jwt).getBody();
				} else {
					claims = Jwts.parser().build().parseClaimsJwt(jwt).getBody();
				}
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

		// build Authentication

		Authentication.Builder auth = Authentication.builder(principalName).scheme("Bearer").root(false);

		// process claims
		claims.forEach((n, v) -> {
			if (AuthenticationClaims.CLAIM_NAME_PERMISSIONS.equals(n)) {
				if (configuration.isIncludePermissions()) {
					@SuppressWarnings("unchecked")
					Collection<String> permissions = (Collection<String>) v;
					if (permissions != null) {
						permissions.forEach(p -> auth.withPermission(Permission.create(p)));
					}
				}
			} else {
				if (configuration.isIncludeDetails()) {
					auth.withParameter(n, v);
				}
			}
		});

		return auth;
	}

}
