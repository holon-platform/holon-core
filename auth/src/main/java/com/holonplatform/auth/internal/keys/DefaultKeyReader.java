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
package com.holonplatform.auth.internal.keys;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.auth.keys.KeyEncoding;
import com.holonplatform.auth.keys.KeyFormat;
import com.holonplatform.auth.keys.KeyReader;
import com.holonplatform.auth.keys.KeySource;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link KeyReader} implementation.
 *
 * @since 5.1.0
 */
public enum DefaultKeyReader implements KeyReader {

	/**
	 * Singleton instance
	 */
	INSTANCE;

	@Override
	public PublicKey publicKey(KeySource source, String algorithm, KeyFormat format, KeyEncoding encoding,
			Map<String, String> parameters) throws SecurityException {
		ObjectUtils.argumentNotNull(source, "Key source must be not null");
		ObjectUtils.argumentNotNull(algorithm, "Key algorithm must be not null");
		ObjectUtils.argumentNotNull(format, "Key format must be not null");
		ObjectUtils.argumentNotNull(encoding, "Key encoding must be not null");

		try {
			final byte[] bytes = source.getBytes();

			if (bytes == null || bytes.length == 0) {
				throw new SecurityException("The public key source bytes are null or empty [" + source + "]");
			}

			// encoding
			byte[] keySource = decodeKeySource(bytes, encoding);

			// format
			if (format == KeyFormat.PKCS11 || format == KeyFormat.PKCS12) {
				Key key = readKeyFromKeyStore(false, keySource, KeyStore.getInstance(format.name()), parameters);
				if (key == null) {
					throw new SecurityException("The public key is not available from " + format.name()
							+ " KeyStore with source [" + source + "]");
				}
				return (PublicKey) key;
			}

			// use a key factory
			KeySpec keySpec = getKeySpec(format, keySource).orElseThrow(() -> new SecurityException(
					"Unsupported key format " + format.name() + " - key source [" + source + "]"));

			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

			return keyFactory.generatePublic(keySpec);

		} catch (SecurityException se) {
			throw se;
		} catch (Exception e) {
			throw new SecurityException("Failed to read a public key from source [" + source + "]", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.keys.KeyReader#privateKey(com.holonplatform.auth.keys.KeySource, java.lang.String,
	 * com.holonplatform.auth.keys.KeyFormat, com.holonplatform.auth.keys.KeyEncoding)
	 */
	@Override
	public PrivateKey privateKey(KeySource source, String algorithm, KeyFormat format, KeyEncoding encoding,
			Map<String, String> parameters) throws SecurityException {
		ObjectUtils.argumentNotNull(source, "Key source must be not null");
		ObjectUtils.argumentNotNull(algorithm, "Key algorithm must be not null");
		ObjectUtils.argumentNotNull(format, "Key format must be not null");
		ObjectUtils.argumentNotNull(encoding, "Key encoding must be not null");

		try {
			final byte[] bytes = source.getBytes();

			if (bytes == null || bytes.length == 0) {
				throw new SecurityException("The private key source bytes are null or empty [" + source + "]");
			}

			// encoding
			byte[] keySource = decodeKeySource(bytes, encoding);

			// format
			if (format == KeyFormat.PKCS11 || format == KeyFormat.PKCS12) {
				Key key = readKeyFromKeyStore(true, keySource, KeyStore.getInstance(format.name()), parameters);
				if (key == null) {
					throw new SecurityException("The private key is not available from " + format.name()
							+ " KeyStore with source [" + source + "]");
				}
				return (PrivateKey) key;
			}

			// use a key factory
			KeySpec keySpec = getKeySpec(format, keySource).orElseThrow(() -> new SecurityException(
					"Unsupported key format " + format.name() + " - key source [" + source + "]"));

			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

			return keyFactory.generatePrivate(keySpec);

		} catch (SecurityException se) {
			throw se;
		} catch (Exception e) {
			throw new SecurityException("Failed to read a private key from source [" + source + "]", e);
		}
	}

	/**
	 * Get the {@link KeySpec} instance to use with given key format, if available.
	 * @param format Key format
	 * @param keySource Key source
	 * @return Optional key spec
	 */
	private static Optional<KeySpec> getKeySpec(KeyFormat format, byte[] keySource) {
		switch (format) {
		case PKCS8:
			return Optional.of(new PKCS8EncodedKeySpec(keySource));
		case X509:
			return Optional.of(new X509EncodedKeySpec(keySource));
		case PKCS11:
		case PKCS12:
		default:
			break;
		}
		return Optional.empty();
	}

	/**
	 * Decode given key source using the specified encoding.
	 * @param source Key source
	 * @param encoding Key encoding
	 * @return Decoded key
	 */
	private static byte[] decodeKeySource(byte[] source, KeyEncoding encoding) {
		switch (encoding) {
		case BASE64:
			return Base64.getDecoder().decode(source);
		case PEM:
			return Base64.getDecoder().decode(extractPEMContent(new String(source)));
		case NONE:
		default:
			break;
		}
		return source;
	}

	/**
	 * Extract key content from a PEM format String.
	 * @param source PEM source
	 * @return The key content
	 */
	private static String extractPEMContent(String source) {
		String pemSource = source;
		int idx = pemSource.indexOf("-----BEGIN ");
		if (idx > -1) {
			pemSource = pemSource.substring("-----BEGIN ".length());
			idx = pemSource.indexOf(" KEY-----");
			if (idx > -1) {
				pemSource = pemSource.substring(idx + " KEY-----".length());
				idx = pemSource.lastIndexOf("-----END ");
				if (idx > -1) {
					pemSource = pemSource.substring(0, idx);
				}
			}
		}
		return pemSource.trim().replace("\n", "").replace("\r", "").replace("\t", "");
	}

	/**
	 * Read a key from a key store.
	 * @param privateKey Whether to obtain a private key or a public key
	 * @param keySource Key source
	 * @param store Key store
	 * @param parameters Key store parameters
	 * @return The key read from the key store, or <code>null</code> if not available
	 * @throws NoSuchAlgorithmException Key store error
	 * @throws CertificateException Key store error
	 * @throws IOException Key store error
	 * @throws UnrecoverableKeyException Key store error
	 * @throws KeyStoreException Key store error
	 */
	private static Key readKeyFromKeyStore(boolean privateKey, byte[] keySource, KeyStore store,
			Map<String, String> parameters) throws NoSuchAlgorithmException, CertificateException, IOException,
			UnrecoverableKeyException, KeyStoreException {
		final String password = (parameters != null) ? parameters.get(KeyReader.PARAMETER_KEYSTORE_PASSWORD) : null;
		final String alias = (parameters != null) ? parameters.get(KeyReader.PARAMETER_KEYSTORE_KEY_ALIAS) : null;
		final String aliasPassword = (parameters != null) ? parameters.get(KeyReader.PARAMETER_KEYSTORE_KEY_PASSWORD)
				: null;

		if (alias == null) {
			throw new KeyStoreException("Missing KeyStore key alias name");
		}

		final char[] pwd = (password != null) ? password.toCharArray() : null;
		store.load(new ByteArrayInputStream(keySource), pwd);

		String keyPassword = (aliasPassword != null) ? aliasPassword : password;
		final char[] keyPwd = (keyPassword != null) ? keyPassword.toCharArray() : null;

		Key key = store.getKey(alias, keyPwd);

		if (key != null) {
			if (privateKey) {
				return key;
			} else if (key instanceof PrivateKey) {
				// Get certificate of public key
				Certificate cert = store.getCertificate(alias);
				if (cert != null) {
					return cert.getPublicKey();
				}
			}
		}

		return key;
	}

}
