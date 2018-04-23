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
package com.holonplatform.auth.keys;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import com.holonplatform.auth.internal.keys.DefaultKeyReader;

/**
 * Security keys reader.
 * <p>
 * Provide methods to read public and private security keys from different sources, using the {@link KeySource}
 * interface to support several key sources, and with different key formats and encodings.
 * </p>
 * 
 * @since 5.1.0
 */
public interface KeyReader {

	/**
	 * Parameter which can be used when reading a key from a Key Store to provide the Key Store password
	 */
	public static String PARAMETER_KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD";

	/**
	 * Parameter which can be used when reading a key from a Key Store to provide the key alias name in the Key Store
	 */
	public static String PARAMETER_KEYSTORE_KEY_ALIAS = "KEYSTORE_KEY_ALIAS";

	/**
	 * Parameter which can be used when reading a key from a Key Store to provide the key recovering password for a key
	 * alias
	 */
	public static String PARAMETER_KEYSTORE_KEY_PASSWORD = "KEYSTORE_KEY_PASSWORD";

	/**
	 * Read a {@link PublicKey} from given key <code>source</code>.
	 * <p>
	 * When a key store type format is used (such as {@link KeyFormat#PKCS12}), the standard
	 * {@link #PARAMETER_KEYSTORE_PASSWORD}, {@link #PARAMETER_KEYSTORE_KEY_ALIAS} and
	 * {@link #PARAMETER_KEYSTORE_KEY_PASSWORD} parameters must be provided to specify the key store password, the key
	 * alias name and the optional key recovering password.
	 * </p>
	 * @param source Key source (not null)
	 * @param algorithm Key algorithm. Must be a supported key factory algorithm, see
	 *        {@link KeyFactory#getInstance(String)} (not null)
	 * @param format Key format (not null)
	 * @param encoding Key source encoding (not null)
	 * @param parameters Optional parameters
	 * @return The {@link PublicKey} representation of the decoded key
	 * @throws SecurityException If the key source is not valid or a decoding error occurred
	 */
	PublicKey publicKey(KeySource source, String algorithm, KeyFormat format, KeyEncoding encoding,
			Map<String, String> parameters) throws SecurityException;

	/**
	 * Read a {@link PrivateKey} from given key <code>source</code>.
	 * <p>
	 * When a key store type format is used (such as {@link KeyFormat#PKCS12}), the standard
	 * {@link #PARAMETER_KEYSTORE_PASSWORD}, {@link #PARAMETER_KEYSTORE_KEY_ALIAS} and
	 * {@link #PARAMETER_KEYSTORE_KEY_PASSWORD} parameters must be provided to specify the key store password, the key
	 * alias name and the optional key recovering password.
	 * </p>
	 * @param source Key source (not null)
	 * @param algorithm Key algorithm. Must be a supported key factory algorithm, see
	 *        {@link KeyFactory#getInstance(String)} (not null)
	 * @param format Key format (not null)
	 * @param encoding Key source encoding (not null)
	 * @param parameters Optional parameters
	 * @return The {@link PrivateKey} representation of the decoded key
	 * @throws SecurityException If the key source is not valid or a decoding error occurred
	 */
	PrivateKey privateKey(KeySource source, String algorithm, KeyFormat format, KeyEncoding encoding,
			Map<String, String> parameters) throws SecurityException;

	/**
	 * Get the default {@link KeyReader}.
	 * @return The default {@link KeyReader}
	 */
	static KeyReader getDefault() {
		return DefaultKeyReader.INSTANCE;
	}

}
