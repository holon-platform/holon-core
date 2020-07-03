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

import java.io.Serializable;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.auth.exceptions.ExpiredCredentialsException;
import com.holonplatform.auth.jwt.JwtConfigProperties;
import com.holonplatform.auth.jwt.JwtConfiguration;
import com.holonplatform.auth.jwt.JwtConfiguration.InvalidJwtConfigurationException;
import com.holonplatform.auth.jwt.JwtSignatureAlgorithm;
import com.holonplatform.auth.keys.KeyEncoding;
import com.holonplatform.auth.keys.KeyFormat;
import com.holonplatform.auth.keys.KeyReader;
import com.holonplatform.auth.keys.KeySource;
import com.holonplatform.core.config.ConfigPropertyProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;

/**
 * JWT handling utility class
 * 
 * @since 5.0.0
 */
public final class JwtUtils implements Serializable {

	private static final long serialVersionUID = -1006362108842428239L;

	/*
	 * Empty private constructor: this class is intended only to provide constants
	 * ad utility methods.
	 */
	private JwtUtils() {
	}

	/**
	 * Check if given <code>token</code> is a JWT token (unsigned)
	 * @param token Token to check
	 * @return <code>true</code> if it is a JWT token
	 */
	public static boolean isJWT(String token) {
		return checkJWT(token, null);
	}

	/**
	 * Check if given <code>token</code> is a JWT token (signed)
	 * @param token      Token to check
	 * @param signingKey Signing key
	 * @return <code>true</code> if it is a JWT token
	 */
	public static boolean isJWT(String token, byte[] signingKey) {
		return checkJWT(token, signingKey);
	}

	/**
	 * Check if given <code>token</code> is a JWT token (signed)
	 * @param token      Token to check
	 * @param signingKey Signing key
	 * @return <code>true</code> if it is a JWT token
	 * @throws ExpiredCredentialsException If token is a JWT token but has expired
	 */
	public static boolean isJWT(String token, Key signingKey) {
		return checkJWT(token, signingKey);
	}

	/**
	 * Check if given <code>token</code> is a JWT token
	 * @param token      Token to check
	 * @param signingKey Optional signing key
	 * @return <code>true</code> if it is a JWT token
	 */
	@SuppressWarnings("unused")
	private static boolean checkJWT(String token, Object signingKey) {
		if (token != null && !token.trim().equals("")) {

			JwtParserBuilder parser = Jwts.parserBuilder();

			if (signingKey != null) {
				if (Key.class.isAssignableFrom(signingKey.getClass())) {
					parser = parser.setSigningKey((Key) signingKey);
				} else {
					parser = parser.setSigningKey((byte[]) signingKey);
				}
			}

			try {
				parser.build().parse(token);
			} catch (ExpiredJwtException | SignatureException e) {
				// ignore
			} catch (MalformedJwtException | IllegalArgumentException e) {
				return false;
			}

			return true;

		}
		return false;
	}

	/**
	 * Build a {@link JwtConfiguration} instance form given
	 * {@link ConfigPropertyProvider} using configuration property keys listed in
	 * {@link JwtConfiguration}.
	 * @param config ConfigPropertyProvider
	 * @return JwtConfiguration
	 * @throws InvalidJwtConfigurationException Error building configuration
	 */
	public static JwtConfiguration buildFromConfig(JwtConfigProperties config) throws InvalidJwtConfigurationException {
		if (config == null) {
			throw new InvalidJwtConfigurationException("Null ConfigPropertyProvider");
		}

		try {
			DefaultJwtConfiguration cfg = new DefaultJwtConfiguration();

			// issuer
			cfg.setIssuer(config.getConfigPropertyValue(JwtConfigProperties.ISSUER, null));

			// expire time
			Long expireMs = null;
			// milliseconds
			Long expire = config.getConfigPropertyValue(JwtConfigProperties.EXPIRE_TIME_MS, null);
			if (expire != null) {
				expireMs = expire;
			}
			// seconds
			if (expireMs == null) {
				expire = config.getConfigPropertyValue(JwtConfigProperties.EXPIRE_TIME_SECONDS, null);
				if (expire != null) {
					expireMs = Long.valueOf(expire.longValue() * 1000);
				}
			}
			// minutes
			if (expireMs == null) {
				expire = config.getConfigPropertyValue(JwtConfigProperties.EXPIRE_TIME_MINUTES, null);
				if (expire != null) {
					expireMs = Long.valueOf(expire.longValue() * 60 * 1000);
				}
			}
			// hours
			if (expireMs == null) {
				expire = config.getConfigPropertyValue(JwtConfigProperties.EXPIRE_TIME_HOURS, null);
				if (expire != null) {
					expireMs = Long.valueOf(expire.longValue() * 60 * 60 * 1000);
				}
			}
			// days
			if (expireMs == null) {
				expire = config.getConfigPropertyValue(JwtConfigProperties.EXPIRE_TIME_DAYS, null);
				if (expire != null) {
					expireMs = Long.valueOf(expire.longValue() * 24 * 60 * 60 * 1000);
				}
			}

			if (expireMs == null) {
				expireMs = Long.valueOf(JwtConfigProperties.DEFAULT_EXPIRE_TIME);
			}

			cfg.setExpireTime((expireMs > 0) ? expireMs.longValue() : 0);

			// nbf
			if (config.getConfigPropertyValue(JwtConfigProperties.NOT_BEFORE_NOW, Boolean.FALSE)) {
				cfg.setNotBeforeNow(true);
			}

			// inclusions
			cfg.setIncludeDetails(config.getConfigPropertyValue(JwtConfigProperties.INCLUDE_DETAILS, Boolean.TRUE));
			cfg.setIncludePermissions(
					config.getConfigPropertyValue(JwtConfigProperties.INCLUDE_PERMISSIONS, Boolean.TRUE));

			// signing algorithm

			final JwtSignatureAlgorithm jwtSignatureAlgorithm = config
					.getConfigPropertyValue(JwtConfigProperties.SIGNATURE_ALGORITHM).orElse(JwtSignatureAlgorithm.NONE);
			cfg.setSignatureAlgorithm(jwtSignatureAlgorithm);

			final SignatureAlgorithm signatureAlgorithm = (jwtSignatureAlgorithm != JwtSignatureAlgorithm.NONE)
					? SignatureAlgorithm.forName(jwtSignatureAlgorithm.getValue())
					: SignatureAlgorithm.NONE;

			// keys
			if (jwtSignatureAlgorithm != JwtSignatureAlgorithm.NONE) {

				if (jwtSignatureAlgorithm.isSymmetric()) {
					String sharedKey = config.getConfigPropertyValue(JwtConfigProperties.SHARED_KEY, null);
					if (sharedKey == null) {
						throw new InvalidJwtConfigurationException("A shared (symmetric) key is "
								+ "required for signature algorithm " + signatureAlgorithm.getDescription());
					}
					cfg.setSharedKey(Base64.getDecoder().decode(sharedKey));
				} else {

					String keyAlgorithm = getAsymmetricKeyAlgorithm(signatureAlgorithm)
							.orElseThrow(() -> new InvalidJwtConfigurationException(
									"Unsupported JWT signature algorithm: " + signatureAlgorithm.getValue()));

					loadPrivateKey(keyAlgorithm, config).ifPresent(cfg::setPrivateKey);
					loadPublicKey(keyAlgorithm, config).ifPresent(cfg::setPublicKey);

				}

			}

			return cfg;

		} catch (Exception e) {
			throw new InvalidJwtConfigurationException(e);
		}

	}

	/*
	 * KeyFactory algorithm name for given SignatureAlgorithm
	 */
	private static Optional<String> getAsymmetricKeyAlgorithm(SignatureAlgorithm sa) {
		switch (sa) {
		case ES256:
			return Optional.of("EC");
		case ES384:
			return Optional.of("EC");
		case ES512:
			return Optional.of("EC");
		case PS256:
			return Optional.of("RSA");
		case PS384:
			return Optional.of("RSA");
		case PS512:
			return Optional.of("RSA");
		case RS256:
			return Optional.of("RSA");
		case RS384:
			return Optional.of("RSA");
		case RS512:
			return Optional.of("RSA");
		default:
			break;
		}
		return Optional.empty();
	}

	/**
	 * Load a private key for given signing algorithm and using given JWT
	 * configuration properties.
	 * @param algorithm Signing algorithm
	 * @param config    JWT configuration properties
	 * @return The private key, if available
	 */
	private static Optional<PrivateKey> loadPrivateKey(String algorithm, JwtConfigProperties config)
			throws InvalidJwtConfigurationException {
		return buildKeySource(true, config).map(source -> {
			final KeyFormat format = config.getConfigPropertyValue(JwtConfigProperties.PRIVATE_KEY_FORMAT,
					KeyFormat.PKCS8);
			final KeyEncoding encoding = config.getConfigPropertyValue(JwtConfigProperties.PRIVATE_KEY_ENCODING,
					KeyEncoding.BASE64);

			final Map<String, String> parameters = new HashMap<>(4);
			config.getConfigPropertyValue(JwtConfigProperties.PRIVATE_KEY_STORE_PASSWORD).ifPresent(v -> {
				parameters.put(KeyReader.PARAMETER_KEYSTORE_PASSWORD, v);
			});
			config.getConfigPropertyValue(JwtConfigProperties.PRIVATE_KEY_STORE_ALIAS).ifPresent(v -> {
				parameters.put(KeyReader.PARAMETER_KEYSTORE_KEY_ALIAS, v);
			});
			config.getConfigPropertyValue(JwtConfigProperties.PRIVATE_KEY_STORE_ALIAS_PASSWORD).ifPresent(v -> {
				parameters.put(KeyReader.PARAMETER_KEYSTORE_KEY_PASSWORD, v);
			});

			return KeyReader.getDefault().privateKey(source, algorithm, format, encoding, parameters);

		});
	}

	/**
	 * Load a public key for given signing algorithm and using given JWT
	 * configuration properties.
	 * @param algorithm Signing algorithm
	 * @param config    JWT configuration properties
	 * @return The public key, if available
	 */
	private static Optional<PublicKey> loadPublicKey(String algorithm, JwtConfigProperties config) {
		return buildKeySource(false, config).map(source -> {
			final KeyFormat format = config.getConfigPropertyValue(JwtConfigProperties.PUBLIC_KEY_FORMAT,
					KeyFormat.X509);
			final KeyEncoding encoding = config.getConfigPropertyValue(JwtConfigProperties.PUBLIC_KEY_ENCODING,
					KeyEncoding.BASE64);

			final Map<String, String> parameters = new HashMap<>(4);
			config.getConfigPropertyValue(JwtConfigProperties.PUBLIC_KEY_STORE_PASSWORD).ifPresent(v -> {
				parameters.put(KeyReader.PARAMETER_KEYSTORE_PASSWORD, v);
			});
			config.getConfigPropertyValue(JwtConfigProperties.PUBLIC_KEY_STORE_ALIAS).ifPresent(v -> {
				parameters.put(KeyReader.PARAMETER_KEYSTORE_KEY_ALIAS, v);
			});
			config.getConfigPropertyValue(JwtConfigProperties.PUBLIC_KEY_STORE_ALIAS_PASSWORD).ifPresent(v -> {
				parameters.put(KeyReader.PARAMETER_KEYSTORE_KEY_PASSWORD, v);
			});

			return KeyReader.getDefault().publicKey(source, algorithm, format, encoding, parameters);

		});
	}

	/**
	 * Build a {@link KeySource} using given JWT configuration properties.
	 * @param privateKey Whether to build a key source for a private key
	 * @param config     JWT configuration propertie
	 * @return Optional key source
	 */
	@SuppressWarnings("deprecation")
	private static Optional<KeySource> buildKeySource(boolean privateKey, JwtConfigProperties config) {

		String source = config.getConfigPropertyValue(
				privateKey ? JwtConfigProperties.PRIVATE_KEY_SOURCE : JwtConfigProperties.PUBLIC_KEY_SOURCE, null);

		if (source == null || source.trim().length() == 0) {
			// check compatibility
			source = config.getConfigPropertyValue(
					privateKey ? JwtConfigProperties.PRIVATE_KEY : JwtConfigProperties.PUBLIC_KEY, null);
			if (source == null || source.trim().length() == 0) {
				String sourceFile = config.getConfigPropertyValue(
						privateKey ? JwtConfigProperties.PRIVATE_KEY_FILE : JwtConfigProperties.PUBLIC_KEY_FILE, null);
				if (sourceFile != null && sourceFile.trim().length() > 0) {
					source = JwtConfigProperties.KEY_SOURCE_FILE_PREFIX + sourceFile;
				}
			}
		}

		if (source == null || source.trim().length() == 0) {
			return Optional.empty();
		}

		if (source.startsWith(JwtConfigProperties.KEY_SOURCE_FILE_PREFIX)) {
			if (source.length() == JwtConfigProperties.KEY_SOURCE_FILE_PREFIX.length()) {
				throw new InvalidJwtConfigurationException("Invalid JWT signing key source file name: " + source);
			}
			return Optional.of(KeySource.file(source.substring(JwtConfigProperties.KEY_SOURCE_FILE_PREFIX.length())));
		}

		if (source.startsWith(JwtConfigProperties.KEY_SOURCE_CLASSPATH_PREFIX)) {
			if (source.length() == JwtConfigProperties.KEY_SOURCE_CLASSPATH_PREFIX.length()) {
				throw new InvalidJwtConfigurationException(
						"Invalid JWT signing key source classpath resource name: " + source);
			}
			return Optional
					.of(KeySource.resource(source.substring(JwtConfigProperties.KEY_SOURCE_CLASSPATH_PREFIX.length())));
		}

		return Optional.of(KeySource.string(source));
	}

}
