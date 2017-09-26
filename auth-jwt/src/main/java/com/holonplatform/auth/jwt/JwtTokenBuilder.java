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
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.jwt.JwtConfiguration.InvalidJwtConfigurationException;
import com.holonplatform.auth.jwt.internal.JwtUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT builder using {@link Authentication} objects
 * 
 * @since 5.0.0
 */
public final class JwtTokenBuilder implements Serializable {

	private static final long serialVersionUID = -4638304194366760813L;

	/**
	 * Enumeration of {@link Authentication} parts
	 */
	public static enum AuthPart {

		/**
		 * Permissions
		 */
		PERMISSIONS,

		/**
		 * Details
		 */
		DETAILS;

	}

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private JwtTokenBuilder() {
	}

	/**
	 * Build a JWT token using given {@link JwtConfiguration} for given {@link Authentication}.
	 * @param configuration JWT configuration (not null)
	 * @param authentication Authentication for which to create the token (not null)
	 * @param jwtTokenId Optional JWT token id (jit claim)
	 * @return JWT token
	 * @throws InvalidJwtConfigurationException Invalid JWT configuration
	 */
	public static String buildJwtToken(JwtConfiguration configuration, Authentication authentication, String jwtTokenId)
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

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.NONE;
		if (configuration.getSignatureAlgorithm() != null) {
			signatureAlgorithm = SignatureAlgorithm.forName(configuration.getSignatureAlgorithm());
		}

		if (signatureAlgorithm == SignatureAlgorithm.NONE) {
			// no signature
			return JwtTokenBuilder.buildJWT(authentication, jwtTokenId, configuration.getIssuer(), expire, parts);
		} else {
			boolean symmetric = JwtUtils.isSymmetric(signatureAlgorithm);
			if (symmetric) {
				byte[] key = configuration.getSharedKey();
				if (key == null) {
					throw new InvalidJwtConfigurationException("Missing shared signing key");
				}
				return JwtTokenBuilder.buildJWT(authentication, jwtTokenId, configuration.getIssuer(), expire,
						signatureAlgorithm, key, parts);
			} else {
				Key privateKey = configuration.getPrivateKey();
				if (privateKey == null) {
					throw new InvalidJwtConfigurationException("Missing private signing key");
				}
				return JwtTokenBuilder.buildJWT(authentication, jwtTokenId, configuration.getIssuer(), expire,
						signatureAlgorithm, privateKey, parts);
			}
		}

	}

	/**
	 * Build a not signed JSON Web Token form given {@link Authentication}
	 * @param authentication Authentication source
	 * @param id Optional JWT id (jit)
	 * @param issuer Optional Issuer name
	 * @param timeToLiveMs Optional time to live for token expiration (<code>null</code> or less than 0 means token not
	 *        expires)
	 * @param includeParts Optional Authentication parts to include as claims in JWT
	 * @return JWT String
	 * @see AuthPart
	 */
	public static String buildJWT(Authentication authentication, String id, String issuer, Long timeToLiveMs,
			AuthPart... includeParts) {
		return buildJWT(authentication, id, issuer, timeToLiveMs, null, null, (byte[]) null, includeParts);
	}

	/**
	 * Build a JSON Web Token form given {@link Authentication}
	 * @param authentication Authentication source
	 * @param id Optional JWT id (jit)
	 * @param issuer Optional Issuer name
	 * @param timeToLiveMs Optional time to live for token expiration (<code>null</code> or less than 0 means token not
	 *        expires)
	 * @param algorithm Signature algorithm to use with given key to sign JWT. Only meaningful if
	 *        <code>signingKey</code> is not <code>null</code>
	 * @param privateKey Optional {@link Key} to sign JWT using given <code>algorithm</code>
	 * @param includeParts Optional Authentication parts to include as claims in JWT
	 * @return JWT String
	 * @see AuthPart
	 */
	public static String buildJWT(Authentication authentication, String id, String issuer, Long timeToLiveMs,
			SignatureAlgorithm algorithm, Key privateKey, AuthPart... includeParts) {
		return buildJWT(authentication, id, issuer, timeToLiveMs, algorithm, privateKey, null, includeParts);
	}

	/**
	 * Build a JSON Web Token form given {@link Authentication}
	 * @param authentication Authentication source
	 * @param id Optional JWT id (jit)
	 * @param issuer Optional Issuer name
	 * @param timeToLiveMs Optional time to live for token expiration (<code>null</code> or less than 0 means token not
	 *        expires)
	 * @param algorithm Signature algorithm to use with given key to sign JWT. Only meaningful if
	 *        <code>signingKey</code> is not <code>null</code>
	 * @param base64EncodedKey Optional Base64 encoded key to sign JWT using given <code>algorithm</code>
	 * @param includeParts Optional Authentication parts to include as claims in JWT
	 * @return JWT String
	 * @see AuthPart
	 */
	public static String buildJWT(Authentication authentication, String id, String issuer, Long timeToLiveMs,
			SignatureAlgorithm algorithm, String base64EncodedKey, AuthPart... includeParts) {
		return buildJWT(authentication, id, issuer, timeToLiveMs, algorithm, null,
				(base64EncodedKey != null) ? Base64.getDecoder().decode(base64EncodedKey) : null, includeParts);
	}

	/**
	 * Build a JSON Web Token form given {@link Authentication}
	 * @param authentication Authentication source
	 * @param id Optional JWT id (jit)
	 * @param issuer Optional Issuer name
	 * @param timeToLiveMs Optional time to live for token expiration (<code>null</code> or less than 0 means token not
	 *        expires)
	 * @param algorithm Signature algorithm to use with given key to sign JWT. Only meaningful if
	 *        <code>signingKey</code> is not <code>null</code>
	 * @param signingKey Optional Base64 encoded key to sign JWT using given <code>algorithm</code>
	 * @param includeParts Optional Authentication parts to include as claims in JWT
	 * @return JWT String
	 * @see AuthPart
	 */
	public static String buildJWT(Authentication authentication, String id, String issuer, Long timeToLiveMs,
			SignatureAlgorithm algorithm, byte[] signingKey, AuthPart... includeParts) {
		return buildJWT(authentication, id, issuer, timeToLiveMs, algorithm, null, signingKey, includeParts);
	}

	/*
	 * Build a JSON Web Token form given {@link Authentication} If private key is not null, that will be used. Signing
	 * key (if not null) otherwise.
	 */
	private static String buildJWT(Authentication authentication, String id, String issuer, Long timeToLiveMs,
			SignatureAlgorithm algorithm, Key privateKey, byte[] signingKey, AuthPart... includeParts) {

		if (authentication == null) {
			throw new IllegalArgumentException("Null Authentication");
		}

		JwtBuilder builder = createJWT(id, authentication.getName(), issuer,
				(timeToLiveMs != null) ? timeToLiveMs.longValue() : -1);

		// sign

		if (privateKey != null || signingKey != null) {
			if (algorithm == null) {
				throw new IllegalArgumentException("Null signature algorithm");
			}
			if (privateKey != null) {
				builder.signWith(algorithm, privateKey);
			} else {
				builder.signWith(algorithm, signingKey);
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
	private static JwtBuilder createJWT(String id, String subject, String issuer, long timeToLiveMs) {

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

		return builder;

	}

}
