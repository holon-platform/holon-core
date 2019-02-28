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
package com.holonplatform.auth.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.holonplatform.auth.keys.KeyEncoding;
import com.holonplatform.auth.keys.KeyFormat;
import com.holonplatform.auth.keys.KeyReader;
import com.holonplatform.auth.keys.KeySource;

public class TestKeys {

	private static final byte[] TEST_PRIVATE_KEY = { 48, -126, 2, 120, 2, 1, 0, 48, 13, 6, 9, 42, -122, 72, -122, -9,
			13, 1, 1, 1, 5, 0, 4, -126, 2, 98, 48, -126, 2, 94, 2, 1, 0, 2, -127, -127, 0, -69, -12, -18, -112, 117,
			-72, 82, -72, 125, 13, -48, -108, 119, 17, 72, 42, 31, 49, 106, 47, 98, -106, 41, -124, -19, -119, -121,
			125, 97, -38, 18, 34, -68, 45, -45, 108, 92, -42, -97, 78, -23, 47, 94, 29, -15, -25, 60, 44, -118, 8, 97,
			102, 64, -97, -126, -3, 101, 74, 68, -52, 35, -21, -34, 118, -57, -91, -38, 15, 104, 71, 43, -90, -91, -27,
			-126, -104, -50, -4, -104, 31, -15, -117, 26, 64, -11, -86, -20, 10, -29, 90, -65, -30, 126, 35, -111, 3,
			123, -47, -125, 99, -107, -124, 65, -125, -33, 79, 65, 113, -58, 33, -10, -105, -95, -4, -125, -87, -73,
			-47, 31, -103, 104, -88, 98, 88, -99, 59, -28, -65, 2, 3, 1, 0, 1, 2, -127, -128, 117, -50, -122, 60, -53,
			92, 53, -108, -26, -28, -30, -56, 59, 124, -4, -125, 82, 59, -47, -73, 83, -114, -107, 5, 121, 49, 11, 88,
			62, 29, -59, -30, 57, -102, 107, 122, -17, 17, -94, -106, 126, 55, -81, 65, 11, -97, 8, -85, 77, -11, 4,
			-65, 103, 95, -45, 101, 105, -72, 76, 56, -69, -121, 26, 78, -69, -90, 25, -127, 82, -3, 37, -127, -16, 58,
			-10, -4, -94, -107, -3, 98, 87, -67, 23, 34, -47, -65, -118, 79, 118, 21, 36, 30, -50, -38, -121, 57, -95,
			60, -16, 31, -46, 68, -93, -64, -96, 24, -106, -96, 102, 77, 76, -39, 99, -111, -45, 69, -100, -65, 125, -6,
			85, 126, -45, -16, 114, 94, 116, -103, 2, 65, 0, -30, 64, -82, -119, -102, -98, -89, -70, 60, 58, 113, -100,
			-66, 8, 25, -61, -49, -54, 125, 29, 89, 13, -43, -55, 53, 126, -117, 85, -28, 103, -57, 122, -46, 112, 104,
			45, 74, -30, 27, -61, -92, -7, -24, 99, -31, -24, -124, 97, -30, -72, 9, -82, -58, 61, -56, -116, 18, -87,
			25, 115, -98, 23, -82, 59, 2, 65, 0, -44, -85, 68, -47, 7, 117, 126, -42, 101, 99, 38, -45, 34, -60, -34, 8,
			-42, 118, -7, -106, -115, -116, 84, 4, -125, -114, -7, 127, -36, -80, -81, -20, -68, 66, 100, 74, -42, 46,
			-112, -93, 62, 43, -52, -82, 40, -66, 56, -32, 43, -109, 85, 94, -99, 117, 123, -71, 50, -61, -3, 40, -67,
			28, -89, 77, 2, 65, 0, -107, 70, -25, -31, -110, 74, -85, -113, -116, -67, 98, -56, 111, -7, 24, 70, -63,
			-118, 112, 18, 52, -9, -109, -34, 53, -107, 80, -119, 83, 49, -59, -61, -101, -92, -34, -105, 67, -104, -5,
			-120, -110, 8, -16, -13, 53, -46, 27, -58, 25, -72, 35, 107, 85, 16, -34, -120, -52, -89, 21, 75, -37, 58,
			-12, -111, 2, 65, 0, -44, 45, -44, 72, -32, -78, 63, 75, -44, 119, -22, 66, 120, -52, 5, -30, -73, 102,
			-104, 121, -74, -37, -84, -17, -101, -6, 71, -65, 100, 5, -4, 95, -85, 44, 106, 55, -88, 29, 105, 61, 21,
			72, 73, -69, -16, -10, 104, 116, 111, -59, 93, 69, 58, -65, -2, 34, -74, 29, -120, 52, -33, 8, -7, 121, 2,
			65, 0, -86, -17, -69, -89, 22, -101, -9, 56, 68, -77, -124, -44, 90, -46, 24, -55, -128, 0, 34, -62, -16,
			42, -62, -75, -111, -60, -79, -45, 14, 40, -13, -91, 14, 15, 52, 55, -47, -33, 102, -45, 87, -6, 65, -10,
			45, 39, 36, 3, -36, 123, -17, 92, 7, -74, -95, -57, -21, -23, 45, 125, -93, 22, -30, -18 };

	private static final byte[] TEST_PUBLIC_KEY = { 48, -127, -97, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5,
			0, 3, -127, -115, 0, 48, -127, -119, 2, -127, -127, 0, -69, -12, -18, -112, 117, -72, 82, -72, 125, 13, -48,
			-108, 119, 17, 72, 42, 31, 49, 106, 47, 98, -106, 41, -124, -19, -119, -121, 125, 97, -38, 18, 34, -68, 45,
			-45, 108, 92, -42, -97, 78, -23, 47, 94, 29, -15, -25, 60, 44, -118, 8, 97, 102, 64, -97, -126, -3, 101, 74,
			68, -52, 35, -21, -34, 118, -57, -91, -38, 15, 104, 71, 43, -90, -91, -27, -126, -104, -50, -4, -104, 31,
			-15, -117, 26, 64, -11, -86, -20, 10, -29, 90, -65, -30, 126, 35, -111, 3, 123, -47, -125, 99, -107, -124,
			65, -125, -33, 79, 65, 113, -58, 33, -10, -105, -95, -4, -125, -87, -73, -47, 31, -103, 104, -88, 98, 88,
			-99, 59, -28, -65, 2, 3, 1, 0, 1 };

	private static final String TEST_PRIVATE_KEY_BASE64 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALv07pB1uFK4fQ3QlHcRSCofMWovYpYphO2Jh31h2hIivC3TbFzWn07pL14d8ec8LIoIYWZAn4L9ZUpEzCPr3nbHpdoPaEcrpqXlgpjO/Jgf8YsaQPWq7ArjWr/ifiORA3vRg2OVhEGD309BccYh9peh/IOpt9EfmWioYlidO+S/AgMBAAECgYB1zoY8y1w1lObk4sg7fPyDUjvRt1OOlQV5MQtYPh3F4jmaa3rvEaKWfjevQQufCKtN9QS/Z1/TZWm4TDi7hxpOu6YZgVL9JYHwOvb8opX9Yle9FyLRv4pPdhUkHs7ahzmhPPAf0kSjwKAYlqBmTUzZY5HTRZy/ffpVftPwcl50mQJBAOJAromanqe6PDpxnL4IGcPPyn0dWQ3VyTV+i1XkZ8d60nBoLUriG8Ok+ehj4eiEYeK4Ca7GPciMEqkZc54XrjsCQQDUq0TRB3V+1mVjJtMixN4I1nb5lo2MVASDjvl/3LCv7LxCZErWLpCjPivMrii+OOArk1VenXV7uTLD/Si9HKdNAkEAlUbn4ZJKq4+MvWLIb/kYRsGKcBI095PeNZVQiVMxxcObpN6XQ5j7iJII8PM10hvGGbgja1UQ3ojMpxVL2zr0kQJBANQt1Ejgsj9L1HfqQnjMBeK3Zph5ttus75v6R79kBfxfqyxqN6gdaT0VSEm78PZodG/FXUU6v/4ith2INN8I+XkCQQCq77unFpv3OESzhNRa0hjJgAAiwvAqwrWRxLHTDijzpQ4PNDfR32bTV/pB9i0nJAPce+9cB7ahx+vpLX2jFuLu";
	private static final String TEST_PUBLIC_KEY_BASE64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC79O6QdbhSuH0N0JR3EUgqHzFqL2KWKYTtiYd9YdoSIrwt02xc1p9O6S9eHfHnPCyKCGFmQJ+C/WVKRMwj6952x6XaD2hHK6al5YKYzvyYH/GLGkD1quwK41q/4n4jkQN70YNjlYRBg99PQXHGIfaXofyDqbfRH5loqGJYnTvkvwIDAQAB";

	@Test
	public void testBase() {
		assertNotNull(KeyReader.getDefault());
	}

	@Test
	public void testKeySource() throws IOException {

		KeySource ks = KeySource.bytes(TEST_PRIVATE_KEY);
		assertTrue(Arrays.equals(TEST_PRIVATE_KEY, ks.getBytes()));

	}

	@Test
	public void testPublicKey() {
		final KeyReader keyReader = KeyReader.getDefault();

		PublicKey key = keyReader.publicKey(KeySource.bytes(TEST_PUBLIC_KEY), "RSA", KeyFormat.X509, KeyEncoding.NONE,
				null);
		assertNotNull(key);
		assertTrue(Arrays.equals(TEST_PUBLIC_KEY, key.getEncoded()));

		key = keyReader.publicKey(KeySource.string(TEST_PUBLIC_KEY_BASE64), "RSA", KeyFormat.X509, KeyEncoding.BASE64,
				null);
		assertNotNull(key);
		assertTrue(Arrays.equals(TEST_PUBLIC_KEY, key.getEncoded()));

		key = keyReader.publicKey(KeySource.resource("test_public_key_1.txt"), "RSA", KeyFormat.X509, KeyEncoding.PEM,
				null);
		assertNotNull(key);
		assertTrue(Arrays.equals(TEST_PUBLIC_KEY, key.getEncoded()));

		key = keyReader.publicKey(KeySource.resource("test_public_key_2.txt"), "RSA", KeyFormat.X509, KeyEncoding.PEM,
				null);
		assertNotNull(key);
		assertTrue(Arrays.equals(TEST_PUBLIC_KEY, key.getEncoded()));

		Map<String, String> parameters = new HashMap<>();
		parameters.put(KeyReader.PARAMETER_KEYSTORE_PASSWORD, "TEST_PASSWORD");
		parameters.put(KeyReader.PARAMETER_KEYSTORE_KEY_ALIAS, "TEST_ALIAS");

		key = keyReader.publicKey(KeySource.resource("test_keystore.p12"), "RSA", KeyFormat.PKCS12, KeyEncoding.NONE,
				parameters);
		assertNotNull(key);
	}

	@Test
	public void testPrivateKey() {
		final KeyReader keyReader = KeyReader.getDefault();

		PrivateKey key = keyReader.privateKey(KeySource.bytes(TEST_PRIVATE_KEY), "RSA", KeyFormat.PKCS8,
				KeyEncoding.NONE, null);
		assertNotNull(key);
		assertTrue(Arrays.equals(TEST_PRIVATE_KEY, key.getEncoded()));

		key = keyReader.privateKey(KeySource.string(TEST_PRIVATE_KEY_BASE64), "RSA", KeyFormat.PKCS8,
				KeyEncoding.BASE64, null);
		assertNotNull(key);
		assertTrue(Arrays.equals(TEST_PRIVATE_KEY, key.getEncoded()));

		key = keyReader.privateKey(KeySource.resource("test_private_key_1.txt"), "RSA", KeyFormat.PKCS8,
				KeyEncoding.PEM, null);
		assertNotNull(key);
		assertTrue(Arrays.equals(TEST_PRIVATE_KEY, key.getEncoded()));

		key = keyReader.privateKey(KeySource.resource("test_private_key_2.txt"), "RSA", KeyFormat.PKCS8,
				KeyEncoding.PEM, null);
		assertNotNull(key);
		assertTrue(Arrays.equals(TEST_PRIVATE_KEY, key.getEncoded()));

		Map<String, String> parameters = new HashMap<>();
		parameters.put(KeyReader.PARAMETER_KEYSTORE_PASSWORD, "TEST_PASSWORD");
		parameters.put(KeyReader.PARAMETER_KEYSTORE_KEY_ALIAS, "TEST_ALIAS");

		key = keyReader.privateKey(KeySource.resource("test_keystore.p12"), "RSA", KeyFormat.PKCS12, KeyEncoding.NONE,
				parameters);
		assertNotNull(key);
	}

}
