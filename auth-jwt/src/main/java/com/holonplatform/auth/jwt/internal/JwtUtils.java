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

import com.holonplatform.auth.exceptions.ExpiredCredentialsException;
import com.holonplatform.auth.jwt.JwtConfigProperties;
import com.holonplatform.auth.jwt.JwtConfiguration;
import com.holonplatform.auth.jwt.JwtConfiguration.InvalidJwtConfigurationException;
import com.holonplatform.auth.keys.KeyEncoding;
import com.holonplatform.auth.keys.KeyFormat;
import com.holonplatform.auth.keys.KeyReader;
import com.holonplatform.auth.keys.KeySource;
import com.holonplatform.core.config.ConfigPropertyProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * JWT handling utility class
 * 
 * @since 5.0.0
 */
public final class JwtUtils implements Serializable {

	private static final long serialVersionUID = -1006362108842428239L;

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
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
	 * @param token Token to check
	 * @param signingKey Signing key
	 * @return <code>true</code> if it is a JWT token
	 */
	public static boolean isJWT(String token, byte[] signingKey) {
		return checkJWT(token, signingKey);
	}

	/**
	 * Check if given <code>token</code> is a JWT token (signed)
	 * @param token Token to check
	 * @param signingKey Signing key
	 * @return <code>true</code> if it is a JWT token
	 * @throws ExpiredCredentialsException If token is a JWT token but has expired
	 */
	public static boolean isJWT(String token, Key signingKey) {
		return checkJWT(token, signingKey);
	}

	/**
	 * Check if given <code>token</code> is a JWT token
	 * @param token Token to check
	 * @param signingKey Optional signing key
	 * @return <code>true</code> if it is a JWT token
	 */
	@SuppressWarnings("unused")
	private static boolean checkJWT(String token, Object signingKey) {
		if (token != null && !token.trim().equals("")) {

			JwtParser parser = Jwts.parser();

			if (signingKey != null) {
				if (Key.class.isAssignableFrom(signingKey.getClass())) {
					parser.setSigningKey((Key) signingKey);
				} else {
					parser.setSigningKey((byte[]) signingKey);
				}
			}

			try {
				parser.parse(token);
			} catch (ExpiredJwtException e) {
				// ignore
			} catch (MalformedJwtException e) {
				return false;
			} catch (SignatureException e) {
				// ignore
			} catch (IllegalArgumentException e) {
				return false;
			}

			return true;

		}
		return false;
	}

	/**
	 * Check if given <code>signatureAlgorithm</code> requires a symmetric (shared) key
	 * @param signatureAlgorithm SignatureAlgorithm
	 * @return <code>true</code> if given algorithm requires a symmetric (shared) key
	 */
	public static boolean isSymmetric(SignatureAlgorithm signatureAlgorithm) {
		return SignatureAlgorithm.HS256 == signatureAlgorithm || SignatureAlgorithm.HS384 == signatureAlgorithm
				|| SignatureAlgorithm.HS512 == signatureAlgorithm;
	}

	/**
	 * Build a {@link JwtConfiguration} instance form given {@link ConfigPropertyProvider} using configuration property
	 * keys listed in {@link JwtConfiguration}.
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

			// inclusions
			cfg.setIncludeDetails(config.getConfigPropertyValue(JwtConfigProperties.INCLUDE_DETAILS, Boolean.TRUE));
			cfg.setIncludePermissions(
					config.getConfigPropertyValue(JwtConfigProperties.INCLUDE_PERMISSIONS, Boolean.TRUE));

			// signing algorithm

			SignatureAlgorithm signatureAlgorithm = null;

			String algoName = config.getConfigPropertyValue(JwtConfigProperties.SIGNATURE_ALGORITHM, null);
			if (algoName != null) {
				signatureAlgorithm = SignatureAlgorithm.forName(algoName);
				cfg.setSignatureAlgorithm(algoName);
			} else {
				signatureAlgorithm = SignatureAlgorithm.HS256;
				cfg.setSignatureAlgorithm(JwtConfigProperties.DEFAULT_SIGNATURE_ALGORITHM);
			}

			// keys
			if (signatureAlgorithm != SignatureAlgorithm.NONE) {

				boolean symmetric = JwtUtils.isSymmetric(signatureAlgorithm);

				if (symmetric) {
					String sharedKey = config.getConfigPropertyValue(JwtConfigProperties.SHARED_KEY, null);
					if (sharedKey == null) {
						throw new InvalidJwtConfigurationException("A shared (symmetric) key is "
								+ "required for signature algorithm " + signatureAlgorithm.getDescription());
					}
					cfg.setSharedKey(Base64.getDecoder().decode(sharedKey));
				} else {

					String keyAlgorithm = getAsymmetricKeyAlgorithm(signatureAlgorithm);

					if (keyAlgorithm == null) {
						throw new InvalidJwtConfigurationException(
								"Unsupported JWT signature algorithm: " + signatureAlgorithm.getValue());
					}

					cfg.setPrivateKey(loadPrivateKey(keyAlgorithm, config));
					cfg.setPublicKey(loadPublicKey(keyAlgorithm, config));

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
	@SuppressWarnings("incomplete-switch")
	private static String getAsymmetricKeyAlgorithm(SignatureAlgorithm sa) {
		switch (sa) {
		case ES256:
			return "EC";
		case ES384:
			return "EC";
		case ES512:
			return "EC";
		case PS256:
			return "RSA";
		case PS384:
			return "RSA";
		case PS512:
			return "RSA";
		case RS256:
			return "RSA";
		case RS384:
			return "RSA";
		case RS512:
			return "RSA";
		default:
			break;
		}
		return null;
	}

	private static PrivateKey loadPrivateKey(String algorithm, JwtConfigProperties config)
			throws InvalidJwtConfigurationException {

		final KeySource source = buildKeySource(true, config);
		final KeyFormat format = config.getConfigPropertyValue(JwtConfigProperties.PRIVATE_KEY_FORMAT, KeyFormat.PKCS8);
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

		try {
			return KeyReader.getDefault().privateKey(source, algorithm, format, encoding, parameters);
		} catch (Exception e) {
			throw new InvalidJwtConfigurationException("Failed to load JWT sign private key from configuration", e);
		}
	}

	private static PublicKey loadPublicKey(String algorithm, JwtConfigProperties config)
			throws InvalidJwtConfigurationException {

		final KeySource source = buildKeySource(true, config);
		final KeyFormat format = config.getConfigPropertyValue(JwtConfigProperties.PUBLIC_KEY_FORMAT, KeyFormat.X509);
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

		try {
			return KeyReader.getDefault().publicKey(source, algorithm, format, encoding, parameters);
		} catch (Exception e) {
			throw new InvalidJwtConfigurationException("Failed to load JWT sign public key from configuration", e);
		}
	}

	@SuppressWarnings("deprecation")
	private static KeySource buildKeySource(boolean privateKey, JwtConfigProperties config)
			throws InvalidJwtConfigurationException {

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
			throw new InvalidJwtConfigurationException("Missing JWT signing key source - Check configuration property ["
					+ JwtConfigProperties.NAME + "." + (privateKey ? JwtConfigProperties.PRIVATE_KEY_SOURCE.getKey()
							: JwtConfigProperties.PUBLIC_KEY_SOURCE.getKey())
					+ "]");
		}

		if (source.startsWith(JwtConfigProperties.KEY_SOURCE_FILE_PREFIX)) {
			if (source.length() == JwtConfigProperties.KEY_SOURCE_FILE_PREFIX.length()) {
				throw new InvalidJwtConfigurationException("Invalid JWT signing key source file name: " + source);
			}
			return KeySource.file(source.substring(JwtConfigProperties.KEY_SOURCE_FILE_PREFIX.length()));
		}

		if (source.startsWith(JwtConfigProperties.KEY_SOURCE_CLASSPATH_PREFIX)) {
			if (source.length() == JwtConfigProperties.KEY_SOURCE_CLASSPATH_PREFIX.length()) {
				throw new InvalidJwtConfigurationException(
						"Invalid JWT signing key source classpath resource name: " + source);
			}
			return KeySource.resource(source.substring(JwtConfigProperties.KEY_SOURCE_CLASSPATH_PREFIX.length()));
		}

		return KeySource.string(source);
	}

}
