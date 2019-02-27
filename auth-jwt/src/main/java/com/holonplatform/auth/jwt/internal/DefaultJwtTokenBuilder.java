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

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.jwt.AuthenticationClaims;
import com.holonplatform.auth.jwt.JwtConfiguration;
import com.holonplatform.auth.jwt.JwtConfiguration.InvalidJwtConfigurationException;
import com.holonplatform.auth.jwt.JwtSignatureAlgorithm;
import com.holonplatform.auth.jwt.JwtTokenBuilder;
import com.holonplatform.core.internal.utils.ObjectUtils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Default {@link JwtTokenBuilder} implementation.
 *
 * @since 5.1.0
 */
public enum DefaultJwtTokenBuilder implements JwtTokenBuilder {

	INSTANCE;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.jwt.JwtTokenBuilder#buildJwt(com.holonplatform.auth.jwt.JwtConfiguration,
	 * com.holonplatform.auth.Authentication, java.lang.String)
	 */
	@Override
	public String buildJwt(JwtConfiguration configuration, Authentication authentication, String jwtTokenId)
			throws InvalidJwtConfigurationException {
		ObjectUtils.argumentNotNull(configuration, "JwtConfiguration must be not null");
		ObjectUtils.argumentNotNull(authentication, "Authentication must be not null");

		AuthPart[] parts = null;
		if (configuration.isIncludeDetails() && configuration.isIncludePermissions()) {
			parts = new AuthPart[] { AuthPart.DETAILS, AuthPart.PERMISSIONS };
		} else if (configuration.isIncludeDetails()) {
			parts = new AuthPart[] { AuthPart.DETAILS };
		} else if (configuration.isIncludePermissions()) {
			parts = new AuthPart[] { AuthPart.PERMISSIONS };
		}

		Long expire = (configuration.getExpireTime() > 0) ? configuration.getExpireTime() : null;
		boolean nbf = configuration.isNotBeforeNow();

		if (configuration.getSignatureAlgorithm() == JwtSignatureAlgorithm.NONE) {
			// no signature
			return buildJWT(authentication, jwtTokenId, configuration.getIssuer().orElse(null), expire, nbf, parts);
		} else {

			final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm
					.forName(configuration.getSignatureAlgorithm().getValue());

			if (configuration.getSignatureAlgorithm().isSymmetric()) {
				byte[] key = configuration.getSharedKey()
						.orElseThrow(() -> new InvalidJwtConfigurationException(
								"Missing shared key for symmetric JWT signature algorithm ["
										+ signatureAlgorithm.getDescription() + "] - JWT configuration: ["
										+ configuration + "]"));
				return buildJWT(authentication, jwtTokenId, configuration.getIssuer().orElse(null), expire, nbf,
						signatureAlgorithm, key, parts);
			} else {
				Key privateKey = configuration.getPrivateKey()
						.orElseThrow(() -> new InvalidJwtConfigurationException(
								"Missing JWT private signing key for asymmetric JWT signature algorithm ["
										+ signatureAlgorithm.getDescription() + "] - JWT configuration: ["
										+ configuration + "]"));
				return buildJWT(authentication, jwtTokenId, configuration.getIssuer().orElse(null), expire, nbf,
						signatureAlgorithm, privateKey, parts);
			}
		}

	}

	/**
	 * Build a not signed JSON Web Token form given {@link Authentication}.
	 * @param authentication Authentication source
	 * @param id Optional JWT id (jit)
	 * @param issuer Optional Issuer name
	 * @param timeToLiveMs Optional time to live for token expiration (<code>null</code> or less than 0 means token not
	 *        expires)
	 * @param notBeforeNow Whether to set the <code>nbf</code> (not before) claim to current timestamp
	 * @param includeParts Optional Authentication parts to include as claims in JWT
	 * @return JWT String
	 */
	private static String buildJWT(Authentication authentication, String id, String issuer, Long timeToLiveMs,
			boolean notBeforeNow, AuthPart... includeParts) {
		return buildJWT(authentication, id, issuer, timeToLiveMs, notBeforeNow, null, null, (byte[]) null,
				includeParts);
	}

	/**
	 * Build a JSON Web Token form given {@link Authentication}
	 * @param authentication Authentication source
	 * @param id Optional JWT id (jit)
	 * @param issuer Optional Issuer name
	 * @param timeToLiveMs Optional time to live for token expiration (<code>null</code> or less than 0 means token not
	 *        expires)
	 * @param notBeforeNow Whether to set the <code>nbf</code> (not before) claim to current timestamp
	 * @param algorithm Signature algorithm to use with given key to sign JWT. Only meaningful if
	 *        <code>signingKey</code> is not <code>null</code>
	 * @param privateKey Optional {@link Key} to sign JWT using given <code>algorithm</code>
	 * @param includeParts Optional Authentication parts to include as claims in JWT
	 * @return JWT String
	 */
	private static String buildJWT(Authentication authentication, String id, String issuer, Long timeToLiveMs,
			boolean notBeforeNow, SignatureAlgorithm algorithm, Key privateKey, AuthPart... includeParts) {
		return buildJWT(authentication, id, issuer, timeToLiveMs, notBeforeNow, algorithm, privateKey, null,
				includeParts);
	}

	/**
	 * Build a JSON Web Token form given {@link Authentication}
	 * @param authentication Authentication source
	 * @param id Optional JWT id (jit)
	 * @param issuer Optional Issuer name
	 * @param timeToLiveMs Optional time to live for token expiration (<code>null</code> or less than 0 means token not
	 *        expires)
	 * @param notBeforeNow Whether to set the <code>nbf</code> (not before) claim to current timestamp
	 * @param algorithm Signature algorithm to use with given key to sign JWT. Only meaningful if
	 *        <code>signingKey</code> is not <code>null</code>
	 * @param signingKey Optional Base64 encoded key to sign JWT using given <code>algorithm</code>
	 * @param includeParts Optional Authentication parts to include as claims in JWT
	 * @return JWT String
	 */
	private static String buildJWT(Authentication authentication, String id, String issuer, Long timeToLiveMs,
			boolean notBeforeNow, SignatureAlgorithm algorithm, byte[] signingKey, AuthPart... includeParts) {
		return buildJWT(authentication, id, issuer, timeToLiveMs, notBeforeNow, algorithm, null, signingKey,
				includeParts);
	}

	/**
	 * Build a JWT from given {@link Authentication}.
	 * @param authentication Authentication (not null)
	 * @param id Optional JWT id (jit)
	 * @param issuer Optional Issuer name
	 * @param timeToLiveMs Optional time to live for token expiration (<code>null</code> or less than 0 means token not
	 *        expires)
	 * @param notBeforeNow Whether to set the <code>nbf</code> (not before) claim to current timestamp
	 * @param algorithm Signature algorithm to use with given key to sign JWT. Only meaningful if
	 *        <code>signingKey</code> is not <code>null</code>
	 * @param privateKey Optional {@link Key} to sign JWT using given an asymmetric algorithm
	 * @param signingKey Optional Base64 encoded key to sign JWT using a symmetric algorithm
	 * @param includeParts Authentication parts to include as claims in JWT
	 * @return The JWT String representation
	 */
	private static String buildJWT(Authentication authentication, String id, String issuer, Long timeToLiveMs,
			boolean notBeforeNow, SignatureAlgorithm algorithm, Key privateKey, byte[] signingKey,
			AuthPart... includeParts) {

		if (authentication == null) {
			throw new IllegalArgumentException("Null Authentication");
		}

		JwtBuilder builder = createJWT(id, authentication.getName(), issuer,
				(timeToLiveMs != null) ? timeToLiveMs.longValue() : -1, notBeforeNow);

		// sign

		if (privateKey != null || signingKey != null) {
			if (algorithm == null) {
				throw new IllegalArgumentException("Null signature algorithm");
			}
			if (privateKey != null) {
				builder.signWith(privateKey, algorithm);
			} else {
				builder.signWith(Keys.hmacShaKeyFor(signingKey), algorithm);
			}
		}

		// auth parts

		if (includeParts != null) {
			for (AuthPart part : includeParts) {
				processAuthPart(builder, authentication, part);
			}
		}

		return builder.compact();
	}

	// ---------------------------------------------------------------------------

	/*
	 * Process required AuthPart
	 */
	private static void processAuthPart(JwtBuilder builder, Authentication authentication, AuthPart part) {
		if (part != null) {
			switch (part) {
			case PERMISSIONS:
				processAuthPermissions(builder, authentication);
				break;
			case DETAILS:
				processAuthDetails(builder, authentication);
				break;
			default:
				break;
			}
		}
	}

	/*
	 * Add isRoot and authentication permissions as JWT claims
	 */
	private static void processAuthPermissions(JwtBuilder builder, Authentication authentication) {
		builder.claim(AuthenticationClaims.CLAIM_NAME_ROOT, Boolean.valueOf(authentication.isRoot()));

		Collection<Permission> permissions = authentication.getPermissions();
		if (permissions != null && !permissions.isEmpty()) {
			Collection<String> ps = new ArrayList<>(permissions.size());
			for (Permission permission : permissions) {
				permission.getPermission().ifPresent(p -> ps.add(p));
			}
			if (!ps.isEmpty()) {
				builder.claim(AuthenticationClaims.CLAIM_NAME_PERMISSIONS, ps);
			}
		}
	}

	/*
	 * Add authentication details as JWT claims
	 */
	private static void processAuthDetails(JwtBuilder builder, final Authentication authentication) {
		authentication.forEachParameter((name, value) -> builder.claim(name, value));
	}

	/*
	 * Create JWT builder
	 */
	private static JwtBuilder createJWT(String id, String subject, String issuer, long timeToLiveMs,
			boolean notBeforeNow) {

		long nowMs = System.currentTimeMillis();
		Date now = new Date(nowMs);

		JwtBuilder builder = Jwts.builder();
		builder.setIssuedAt(now);

		if (id != null) {
			builder.setId(id);
		}

		if (subject != null) {
			builder.setSubject(subject);
		}

		if (issuer != null) {
			builder.setIssuer(issuer);
		}

		if (timeToLiveMs >= 0) {
			long expireMs = nowMs + timeToLiveMs;
			builder.setExpiration(new Date(expireMs));
		}

		if (notBeforeNow) {
			builder.setNotBefore(new Date());
		}

		return builder;

	}

}
