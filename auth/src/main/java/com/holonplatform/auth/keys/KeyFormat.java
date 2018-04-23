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

/**
 * Enumeration of security keys formats.
 *
 * @since 5.1.0
 */
public enum KeyFormat {

	/**
	 * X.509  public key encoding
	 */
	X509,
	
	/**
	 * PKCS #8 private key encoding
	 */
	PKCS8,
	
	/**
	 * PKCS #11 format
	 */
	PKCS11,
	
	/**
	 * PKCS #12 key store format
	 */
	PKCS12
	
}
