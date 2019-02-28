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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.CredentialsContainer;
import com.holonplatform.auth.CredentialsContainer.CredentialsMatcher;
import com.holonplatform.auth.exceptions.UnexpectedCredentialsException;
import com.holonplatform.auth.internal.DefaultCredentialsMatcher;
import com.holonplatform.core.internal.utils.ConversionUtils;

public class TestCredentials {

	@Test
	public void testUtils() throws IOException {

		assertThrows(IllegalArgumentException.class, () -> DefaultCredentialsMatcher.toBytes(null));

		assertThrows(IllegalArgumentException.class, () -> DefaultCredentialsMatcher.toBytes(Calendar.getInstance()));

		byte[] bytes = new byte[] { 1, 2, 3 };
		assertTrue(Arrays.equals(bytes, DefaultCredentialsMatcher.toBytes(bytes)));

		assertNotNull(DefaultCredentialsMatcher.toBytes(new char[] { 'a', 'b' }));

		Credentials crd = Credentials.builder().secret("test").build();
		assertTrue(Arrays.equals(DefaultCredentialsMatcher.toBytes("test"), DefaultCredentialsMatcher.toBytes(crd)));

		File file = File.createTempFile("testcrd_" + System.currentTimeMillis(), ".tmp");

		try (FileOutputStream fo = new FileOutputStream(file)) {
			fo.write(bytes);
			fo.flush();
		}

		assertTrue(Arrays.equals(bytes, DefaultCredentialsMatcher.toBytes(file)));

		try (FileInputStream stream = new FileInputStream(file)) {
			assertTrue(Arrays.equals(bytes, DefaultCredentialsMatcher.toBytes(stream)));
		}

		file.delete();

		assertThrows(RuntimeException.class,
				() -> DefaultCredentialsMatcher.toBytes(new File("xxxxxxxxxxxxxxxxxx.yyyy")));

		crd = Credentials.builder().secret("test").secret(ConversionUtils.toBytes("test")).salt((byte[]) null)
				.hexEncoded().build();
		assertTrue(crd.isHexEncoded());

		crd = Credentials.builder().secret("test").hashAlgorithm(Credentials.Encoder.HASH_MD5).build();
		assertEquals("MD5", crd.getHashAlgorithm());

		crd = Credentials.builder().secret("test").hashAlgorithm(Credentials.Encoder.HASH_MD5).hashIterations(7)
				.build();
		assertEquals("MD5", crd.getHashAlgorithm());
		assertEquals(7, crd.getHashIterations());

		bytes = new byte[] { 1, 2, 3 };

		crd = Credentials.builder().secret("test").hashAlgorithm(Credentials.Encoder.HASH_MD5).hashIterations(7)
				.salt(bytes).build();
		assertEquals("MD5", crd.getHashAlgorithm());
		assertEquals(7, crd.getHashIterations());
		assertTrue(Arrays.equals(bytes, crd.getSalt()));

		crd = Credentials.builder().secret(bytes).build();
		assertTrue(Arrays.equals(bytes, crd.getSecret()));

		crd = Credentials.builder().secret(bytes).hashAlgorithm(Credentials.Encoder.HASH_SHA_512).build();
		assertTrue(Arrays.equals(bytes, crd.getSecret()));

		crd = Credentials.builder().secret("test").hashAlgorithm(Credentials.Encoder.HASH_MD5).hashIterations(9)
				.salt(new String(bytes)).build();
		assertEquals("MD5", crd.getHashAlgorithm());
		assertEquals(9, crd.getHashIterations());
		assertTrue(Arrays.equals(bytes, crd.getSalt()));
	}

	@Test
	public void testCredentialsEncoder() throws UnsupportedEncodingException {

		assertThrows(IllegalStateException.class, () -> Credentials.encoder().build());

		final String secret = "test";
		final byte[] secretBytes = ConversionUtils.toBytes(secret);

		byte[] bytes = Credentials.encoder().secret(secret).build();
		assertTrue(Arrays.equals(secretBytes, bytes));

		String sb = Credentials.encoder().secret(secret).buildAndEncodeBase64();
		assertEquals(Base64.getEncoder().encodeToString(secret.getBytes("UTF-8")), sb);

		bytes = Credentials.encoder().secret(secret).hashMD5().build();
		assertNotNull(bytes);
		bytes = Credentials.encoder().secret(secret).hashSHA1().build();
		assertNotNull(bytes);
		bytes = Credentials.encoder().secret(secret).hashSHA256().build();
		assertNotNull(bytes);
		bytes = Credentials.encoder().secret(secret).hashSHA384().build();
		assertNotNull(bytes);
		bytes = Credentials.encoder().secret(secret).hashSHA512().charset("UTF-8").build();
		assertNotNull(bytes);

		assertThrows(RuntimeException.class, () -> Credentials.encoder().secret("test").hashAlgorithm("xxx").build());

	}

	@Test
	public void testMatcher() {

		final CredentialsMatcher matcher = CredentialsContainer.defaultMatcher();

		final CredentialsContainer nc = new CredentialsContainer() {

			@Override
			public Object getCredentials() {
				return null;
			}
		};

		final CredentialsContainer cc = new CredentialsContainer() {

			@Override
			public Object getCredentials() {
				return "test";
			}
		};

		assertThrows(UnexpectedCredentialsException.class, () -> matcher.credentialsMatch(nc, cc));
		assertThrows(UnexpectedCredentialsException.class, () -> matcher.credentialsMatch(cc, nc));
		assertThrows(UnexpectedCredentialsException.class, () -> matcher.credentialsMatch(null, null));

		final CredentialsContainer cc2 = new CredentialsContainer() {

			@Override
			public Object getCredentials() {
				return "test";
			}
		};

		assertTrue(matcher.credentialsMatch(cc, cc2));

		final CredentialsContainer ncl = new CredentialsContainer() {

			@Override
			public Object getCredentials() {
				return Long.valueOf(3L);
			}
		};

		assertTrue(matcher.credentialsMatch(ncl, ncl));

		CredentialsMatcher fm = (p, s) -> p.getCredentials().equals(s.getCredentials());

		assertTrue(fm.credentialsMatch(cc, cc2));

	}

}
