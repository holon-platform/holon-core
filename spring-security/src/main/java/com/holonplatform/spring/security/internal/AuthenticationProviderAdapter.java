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

import java.util.function.Function;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;

/**
 * Adapter to use an {@link Authenticator} as a Spring Security {@link AuthenticationProvider}.
 * 
 * @param <T> Authentication token type
 * @param <A> Spring Security Authentication type
 *
 * @since 5.1.0
 */
public class AuthenticationProviderAdapter<T extends AuthenticationToken, A extends Authentication>
		implements AuthenticationProvider {

	private final Authenticator<T> authenticator;

	private final Class<A> authenticationType;

	private final Function<A, T> converter;

	/**
	 * Constructor.
	 * @param authenticator Authenticator (not null)
	 * @param authenticationType Supported Spring Security {@link Authentication} type
	 * @param converter Function to convert the Spring Security {@link Authentication} into the required
	 *        {@link AuthenticationToken} type.
	 */
	public AuthenticationProviderAdapter(Authenticator<T> authenticator, Class<A> authenticationType,
			Function<A, T> converter) {
		super();
		ObjectUtils.argumentNotNull(authenticator, "Authenticator must be not null");
		ObjectUtils.argumentNotNull(converter, "Token converter must be not null");
		this.authenticator = authenticator;
		this.authenticationType = authenticationType;
		this.converter = converter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authenticationType.isAssignableFrom(authentication);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core
	 * .Authentication)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		if (authentication == null) {
			throw new AuthenticationServiceException("Null Authentication");
		}

		if (!TypeUtils.isAssignable(authenticationType, authentication.getClass())) {
			return null;
		}

		com.holonplatform.auth.Authentication authc;
		try {
			authc = authenticator.authenticate(converter.apply((A) authentication));
		} catch (com.holonplatform.auth.exceptions.UnknownAccountException e) {
			throw new UsernameNotFoundException("Unknown account", e);
		} catch (com.holonplatform.auth.exceptions.InvalidCredentialsException e) {
			throw new BadCredentialsException("Invalid credentials", e);
		} catch (com.holonplatform.auth.exceptions.ExpiredCredentialsException e) {
			throw new CredentialsExpiredException("Expired credentials", e);
		} catch (com.holonplatform.auth.exceptions.DisabledAccountException e) {
			throw new DisabledException("Disabled account", e);
		} catch (com.holonplatform.auth.exceptions.LockedAccountException e) {
			throw new LockedException("Locked account", e);
		} catch (com.holonplatform.auth.exceptions.AuthenticationException e) {
			throw new InternalAuthenticationServiceException("Internal authentication error", e);
		}

		return new SpringSecurityAuthenticationAdapter(authc);
	}

}
