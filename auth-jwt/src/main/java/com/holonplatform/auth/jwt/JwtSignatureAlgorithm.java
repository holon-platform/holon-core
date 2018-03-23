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
package com.holonplatform.auth.jwt;

/**
 * JWT signature algorithms enumeration.
 *
 * @since 5.1.0
 */
public enum JwtSignatureAlgorithm {

	NONE("none", "No signature"),

	HS256("HS256", "HMAC using SHA-256"),

	HS384("HS384", "HMAC using SHA-384"),

	HS512("HS512", "HMAC using SHA-512"),

	RS256("RS256", "RSASSA-PKCS-v1_5 using SHA-256"),

	RS384("RS384", "RSASSA-PKCS-v1_5 using SHA-384"),

	RS512("RS512", "RSASSA-PKCS-v1_5 using SHA-512"),

	ES256("ES256", "ECDSA using P-256 and SHA-256"),

	ES384("ES384", "ECDSA using P-384 and SHA-384"),

	ES512("ES512", "ECDSA using P-512 and SHA-512"),

	PS256("PS256", "RSASSA-PSS using SHA-256 and MGF1 with SHA-256"),

	PS384("PS384", "RSASSA-PSS using SHA-384 and MGF1 with SHA-384"),

	PS512("PS512", "RSASSA-PSS using SHA-512 and MGF1 with SHA-512");

	private final String value;
	private final String description;

	private JwtSignatureAlgorithm(String value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * Get the algorithm name.
	 * @return the algorithm name
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Get the algorithm description.
	 * @return the algorithm description
	 */
	public String getDescription() {
		return description;
	}

}
