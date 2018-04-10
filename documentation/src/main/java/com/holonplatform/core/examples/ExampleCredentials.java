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
package com.holonplatform.core.examples;

import java.util.Date;

import com.holonplatform.auth.Credentials;

@SuppressWarnings("unused")
public class ExampleCredentials {

	public void credentials() {
		// tag::credentials[]
		Credentials credentials = Credentials.builder().secret("test").build(); // <1>

		credentials = Credentials.builder().secret("test").hashAlgorithm(Credentials.Encoder.HASH_MD5).build(); // <2>

		credentials = Credentials.builder().secret("test").hashAlgorithm(Credentials.Encoder.HASH_MD5).hashIterations(7)
				.salt(new byte[] { 1, 2, 3 }).build(); // <3>

		credentials = Credentials.builder().secret("test").hashAlgorithm(Credentials.Encoder.HASH_MD5).base64Encoded()
				.build(); // <4>

		credentials = Credentials.builder().secret("test").expireDate(new Date()).build(); // <5>
		// end::credentials[]
	}

	public void encoder() {
		// tag::encoder[]
		String encoded = Credentials.encoder().secret("test").buildAndEncodeBase64(); // <1>

		byte[] bytes = Credentials.encoder().secret("test").hashSHA256().build(); // <2>

		encoded = Credentials.encoder().secret("test").hashSHA256().salt(new byte[] { 1, 2, 3 }).buildAndEncodeBase64(); // <3>

		encoded = Credentials.encoder().secret("test").hashSHA512().charset("UTF-8").buildAndEncodeBase64(); // <4>
		// end::encoder[]
	}

}
