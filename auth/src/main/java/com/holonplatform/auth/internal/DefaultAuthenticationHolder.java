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
package com.holonplatform.auth.internal;

import java.util.Optional;

import com.holonplatform.auth.AuthContext.AuthenticationHolder;
import com.holonplatform.auth.Authentication;

/**
 * Default {@link AuthenticationHolder} implementation.
 *
 * @since 5.1.0
 */
public class DefaultAuthenticationHolder implements AuthenticationHolder {

	/*
	 * Current authentication
	 */
	private Authentication authentication;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext.AuthenticationHolder#getAuthentication()
	 */
	@Override
	public Optional<Authentication> getAuthentication() {
		return Optional.ofNullable(authentication);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.auth.AuthContext.AuthenticationHolder#setAuthentication(com.holonplatform.auth.Authentication)
	 */
	@Override
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

}
