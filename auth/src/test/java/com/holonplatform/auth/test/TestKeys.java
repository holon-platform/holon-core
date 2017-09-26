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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

import org.junit.Test;

import com.holonplatform.auth.internal.KeysReader;
import com.holonplatform.core.internal.utils.TestUtils;

public class TestKeys {

	@SuppressWarnings("resource")
	@Test
	public void testReader() throws NoSuchAlgorithmException, IOException {

		TestUtils.checkUtilityClass(KeysReader.class);

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair keyPair = keyGen.genKeyPair();

		final PrivateKey privateKey = keyPair.getPrivate();
		final PublicKey publicKey = keyPair.getPublic();

		byte[] prv = privateKey.getEncoded();
		byte[] pub = publicKey.getEncoded();

		String prv64 = Base64.getEncoder().encodeToString(prv);
		String pub64 = Base64.getEncoder().encodeToString(pub);

		assertEquals(publicKey, KeysReader.readPublicKey("RSA", pub));
		assertEquals(publicKey, KeysReader.readPublicKey("RSA", pub64));

		assertEquals(privateKey, KeysReader.readPrivateKey("RSA", prv));
		assertEquals(privateKey, KeysReader.readPrivateKey("RSA", prv64));

		String fn = "testkey_pub_" + UUID.randomUUID();
		File file = File.createTempFile(fn, ".key");

		FileOutputStream keyfos = new FileOutputStream(file);
		keyfos.write(pub);
		keyfos.close();

		assertEquals(publicKey, KeysReader.readPublicKeyFromFile("RSA", file.getPath()));

		file.delete();

		fn = "testkey_prv_" + UUID.randomUUID();
		file = File.createTempFile(fn, ".key");

		keyfos = new FileOutputStream(file);
		keyfos.write(prv);
		keyfos.close();

		assertEquals(privateKey, KeysReader.readPrivateKeyFromFile("RSA", file.getPath()));

		file.delete();

		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPublicKey("RSA", (String) null);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPrivateKey("RSA", (String) null);
			}
		});

		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPublicKey(null, new byte[] {});
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPublicKey("RSA", new byte[] {});
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPublicKey("RSA", (byte[]) null);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPrivateKey(null, new byte[] {});
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPrivateKey("RSA", new byte[] {});
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPrivateKey("RSA", (byte[]) null);
			}
		});

		TestUtils.expectedException(SecurityException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPublicKey("RSA", new byte[] { 1 });
			}
		});
		TestUtils.expectedException(SecurityException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPrivateKey("RSA", new byte[] { 1 });
			}
		});

		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPublicKeyFromStream("RSA", null);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPublicKeyFromFile("RSA", null);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPrivateKeyFromStream("RSA", null);
			}
		});
		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				KeysReader.readPrivateKeyFromFile("RSA", null);
			}
		});

	}

}
