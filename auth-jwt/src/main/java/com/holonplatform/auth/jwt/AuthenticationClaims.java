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

import com.holonplatform.auth.Authentication;

/**
 * {@link Authentication} JWT claims.
 * 
 * @since 5.0.0
 */
public interface AuthenticationClaims {

	/**
	 * Claim name for {@link Authentication#isRoot()} attribute. Claim value will be a boolean.
	 */
	public final static String CLAIM_NAME_ROOT = "ATH$root";

	/**
	 * Claim name for {@link Authentication#getPermissions()}. Claim value will be a String array.
	 */
	public final static String CLAIM_NAME_PERMISSIONS = "ATH$prms";

	/**
	 * Get a claim value by name, if exists
	 * @param <T> Claim value type
	 * @param claimName Claim name
	 * @param requiredType Expected claim value type
	 * @return Claim value, or <code>null</code> if claim was not found or has no value
	 */
	<T> T get(String claimName, Class<T> requiredType);

}
