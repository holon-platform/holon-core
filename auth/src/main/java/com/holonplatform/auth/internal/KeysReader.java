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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.holonplatform.core.internal.utils.ConversionUtils;

/**
 * Class to read cryptographic keys (private/public) from various sources
 * 
 * TODO .p12 format
 * 
 * @since 5.0.0
 */
public final class KeysReader implements Serializable {

	private static final long serialVersionUID = 5730871474838891202L;

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private KeysReader() {
	}

	// Public --------------------------------------------------------------------

	/**
	 * Read a public key from given Base64 encoded String
	 * @param algorithm Algorithm to use (for example RSA)
	 * @param base64encoded Base64 encoded key
	 * @return PublicKey
	 * @throws SecurityException Error reading key
	 */
	public static PublicKey readPublicKey(String algorithm, String base64encoded) throws SecurityException {
		if (base64encoded == null) {
			throw new IllegalArgumentException("Null key");
		}
		return readPublicKey(algorithm, Base64.getDecoder().decode(base64encoded));
	}

	/**
	 * Read a public key from given bytes
	 * @param algorithm Algorithm to use (for example RSA)
	 * @param key Key bytes
	 * @return PublicKey
	 * @throws SecurityException Error reading key
	 */
	public static PublicKey readPublicKey(String algorithm, byte[] key) throws SecurityException {
		if (algorithm == null) {
			throw new IllegalArgumentException("Null algorithm name");
		}
		if (key == null || key.length == 0) {
			throw new IllegalArgumentException("Null key bytes");
		}
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			throw new SecurityException("Failed to read public key using algorithm " + algorithm, e);
		}
	}

	/**
	 * Read a public key from given stream
	 * @param algorithm Algorithm to use (for example RSA)
	 * @param is Key input stream
	 * @return PublicKey
	 * @throws SecurityException Error reading key
	 */
	public static PublicKey readPublicKeyFromStream(String algorithm, InputStream is) throws SecurityException {
		if (is == null) {
			throw new IllegalArgumentException("Null InputStream");
		}
		try {
			return readPublicKey(algorithm, ConversionUtils.convertInputStreamToBytes(is));
		} catch (IOException e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * Read a public key from given file
	 * @param algorithm Algorithm to use (for example RSA)
	 * @param fileName Key file name
	 * @return PublicKey
	 * @throws SecurityException Error reading key
	 */
	public static PublicKey readPublicKeyFromFile(String algorithm, String fileName) throws SecurityException {
		if (fileName == null) {
			throw new IllegalArgumentException("Null file name");
		}
		try (FileInputStream fis = new FileInputStream(new File(fileName))) {
			return readPublicKeyFromStream(algorithm, fis);
		} catch (FileNotFoundException e) {
			throw new SecurityException(e);
		} catch (IOException e) {
			throw new SecurityException(e);
		}
	}

	// Private -------------------------------------------------------------------

	/**
	 * Read a private key from given Base64 encoded String
	 * @param algorithm Algorithm to use (for example RSA)
	 * @param base64encoded Base64 encoded key
	 * @return PrivateKey
	 * @throws SecurityException Error reading key
	 */
	public static PrivateKey readPrivateKey(String algorithm, String base64encoded) throws SecurityException {
		if (base64encoded == null) {
			throw new IllegalArgumentException("Null key");
		}
		return readPrivateKey(algorithm, Base64.getDecoder().decode(base64encoded));
	}

	/**
	 * Read a private key from given bytes array in PKCS8 format
	 * @param algorithm Algorithm to use (for example RSA)
	 * @param key Key bytes
	 * @return PrivateKey
	 * @throws SecurityException Error reading key
	 */
	public static PrivateKey readPrivateKey(String algorithm, byte[] key) throws SecurityException {
		if (algorithm == null) {
			throw new IllegalArgumentException("Null algorithm name");
		}
		if (key == null || key.length == 0) {
			throw new IllegalArgumentException("Null key bytes");
		}
		try {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
			return keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			throw new SecurityException("Failed to read PKCS8 private key using algorithm " + algorithm, e);
		}
	}

	/**
	 * Read a private key from given stream in PKCS8 format
	 * @param algorithm Algorithm to use (for example RSA)
	 * @param is Key input stream
	 * @return PrivateKey
	 * @throws SecurityException Error reading key
	 */
	public static PrivateKey readPrivateKeyFromStream(String algorithm, InputStream is) throws SecurityException {
		if (is == null) {
			throw new IllegalArgumentException("Null InputStream");
		}
		try {
			return readPrivateKey(algorithm, ConversionUtils.convertInputStreamToBytes(is));
		} catch (IOException e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * Read a private key from given file in PKCS8 format
	 * @param algorithm Algorithm to use (for example RSA)
	 * @param fileName Key file name
	 * @return PrivateKey
	 * @throws SecurityException Error reading key
	 */
	public static PrivateKey readPrivateKeyFromFile(String algorithm, String fileName) throws SecurityException {
		if (fileName == null) {
			throw new IllegalArgumentException("Null file name");
		}
		try (FileInputStream fis = new FileInputStream(new File(fileName))) {
			return readPrivateKeyFromStream(algorithm, fis);
		} catch (FileNotFoundException e) {
			throw new SecurityException(e);
		} catch (IOException e) {
			throw new SecurityException(e);
		}
	}

}
