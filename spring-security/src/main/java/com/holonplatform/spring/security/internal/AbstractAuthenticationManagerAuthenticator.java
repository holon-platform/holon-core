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
package com.holonplatform.spring.security.internal;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.DisabledAccountException;
import com.holonplatform.auth.exceptions.ExpiredCredentialsException;
import com.holonplatform.auth.exceptions.InvalidCredentialsException;
import com.holonplatform.auth.exceptions.InvalidTokenException;
import com.holonplatform.auth.exceptions.LockedAccountException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.exceptions.UnknownAccountException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.spring.security.SpringSecurityAuthentication;

/**
 * Abstract class for {@link AuthenticationManager} based {@link Authenticator}s.
 * 
 * @param <T> AuthenticationToken type
 * 
 * @since 5.1.0
 */
public abstract class AbstractAuthenticationManagerAuthenticator<T extends AuthenticationToken>
		implements Authenticator<T> {

	/**
	 * Spring Security {@link AuthenticationManager}
	 */
	private final AuthenticationManager authenticationManager;

	/**
	 * Constructor.
	 * @param authenticationManager Spring Security {@link AuthenticationManager} (not null)
	 */
	public AbstractAuthenticationManagerAuthenticator(AuthenticationManager authenticationManager) {
		super();
		ObjectUtils.argumentNotNull(authenticationManager, "AuthenticationManager must be not null");
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Get the Spring Security {@link AuthenticationManager}.
	 * @return The authentication manager
	 */
	protected AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	/**
	 * Get the Spring Security {@link org.springframework.security.core.Authentication} from given authentication token.
	 * @param authenticationToken Authentication token
	 * @return the Spring Security {@link org.springframework.security.core.Authentication} represented by given token
	 */
	protected abstract org.springframework.security.core.Authentication getAuthentication(T authenticationToken);

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authenticator#authenticate(com.holonplatform.auth.AuthenticationToken)
	 */
	@Override
	public Authentication authenticate(T authenticationToken) throws AuthenticationException {

		if (authenticationToken == null) {
			throw new InvalidTokenException("Null authentication token");
		}

		org.springframework.security.core.Authentication authentication = getAuthentication(authenticationToken);

		if (authentication == null) {
			throw new InvalidTokenException("Invalid authentication token: missing Spring Security Authentication");
		}

		try {
			authentication = authenticationManager.authenticate(authentication);
		} catch (UsernameNotFoundException e) {
			throw new UnknownAccountException(e.getMessage());
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException(e.getMessage());
		} catch (CredentialsExpiredException | AccountExpiredException e) {
			throw new ExpiredCredentialsException(e.getMessage());
		} catch (DisabledException e) {
			throw new DisabledAccountException(e.getMessage());
		} catch (LockedException e) {
			throw new LockedAccountException(e.getMessage());
		} catch (Exception e) {
			throw new UnexpectedAuthenticationException(e.getMessage(), e);
		}

		return SpringSecurityAuthentication.create(authentication);

	}

}
