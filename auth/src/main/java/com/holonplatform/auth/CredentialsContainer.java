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
package com.holonplatform.auth;

import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.internal.DefaultCredentialsMatcher;

/**
 * Interface implemented by classes which provides credentials data
 * 
 * @since 5.0.0
 * 
 * @see Credentials
 */
@FunctionalInterface
public interface CredentialsContainer {

	/**
	 * Credentials data
	 * @return Credentials
	 */
	Object getCredentials();

	/**
	 * Build the default CredentialsMatcher
	 * @return Default CredentialsMatcher
	 */
	static CredentialsMatcher defaultMatcher() {
		return new DefaultCredentialsMatcher();
	}

	/**
	 * This interface represents classes able to determine if provided credentials matches account's stored credentials.
	 */
	@FunctionalInterface
	public interface CredentialsMatcher {

		/**
		 * Check if provided credentials match the given stored credentials
		 * @param provided Provided credentials
		 * @param stored Stored credentials to match
		 * @return <code>true</code> if the provided credentials match the stored credentials, <code>false</code>
		 *         otherwise.
		 * @throws AuthenticationException Error during credentials match (e.g. invalid or unknown credentials)
		 */
		boolean credentialsMatch(CredentialsContainer provided, CredentialsContainer stored)
				throws AuthenticationException;

	}

}
