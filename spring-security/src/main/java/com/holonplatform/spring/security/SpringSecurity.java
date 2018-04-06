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
package com.holonplatform.spring.security;

import java.util.function.Function;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.holonplatform.auth.AuthContext;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Authorizer;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.token.AccountCredentialsToken;
import com.holonplatform.spring.security.internal.AccountCredentialsAuthenticationManagerAuthenticator;
import com.holonplatform.spring.security.internal.AuthenticationProviderAdapter;
import com.holonplatform.spring.security.internal.DefaultAuthenticationManagerAuthenticator;
import com.holonplatform.spring.security.internal.PermissionGrantedAuthority;
import com.holonplatform.spring.security.internal.SpringSecurityAuthenticationHolder;
import com.holonplatform.spring.security.internal.SpringSecurityPermission;

/**
 * Entry point interface to deal with Spring Security integration with the Holon platform authentication and
 * authorization architecture.
 * <p>
 * A set of buider methods is provided to setup a bridge from the Holon platform authentication and authorization API
 * and the Spring Security architecture.
 * </p>
 *
 * @since 5.1.0
 */
public interface SpringSecurity {

	/**
	 * Create an {@link AuthContext} which uses the Spring Security {@link SecurityContext} as authentication holder.
	 * The default {@link SecurityContextHolder#getContext()} method is used to obtain the Spring Security
	 * {@link SecurityContext} reference.
	 * <p>
	 * The {@link AuthContext} is bound to a default {@link Realm} instance, configured with the default
	 * {@link Authorizer}.
	 * </p>
	 * @return A new {@link AuthContext} using the Spring Security {@link SecurityContext} as authentication holder
	 * @see #authContext(Realm)
	 */
	static AuthContext authContext() {
		return authContext(Realm.builder().withDefaultAuthorizer().build());
	}

	/**
	 * Create an {@link AuthContext} which uses the Spring Security {@link SecurityContext} as authentication holder.
	 * The default {@link SecurityContextHolder#getContext()} method is used to obtain the Spring Security
	 * {@link SecurityContext} reference.
	 * @param realm The {@link Realm} to which the auth context is bound (not null)
	 * @return A new {@link AuthContext} bound to given realm and using the Spring Security {@link SecurityContext} as
	 *         authentication holder
	 */
	static AuthContext authContext(Realm realm) {
		return AuthContext.create(realm, new SpringSecurityAuthenticationHolder());
	}

	/**
	 * Create an {@link AuthContext} which uses the Spring Security {@link SecurityContext} as authentication holder.
	 * The default {@link SecurityContextHolder#getContext()} method is used to obtain the Spring Security
	 * {@link SecurityContext} reference.
	 * <p>
	 * A default {@link Realm} is created using the default authorizer and registering an {@link Authenticator} hich
	 * supports {@link SpringSecurityAuthenticationToken} types and uses the Spring Security
	 * {@link AuthenticationManager} to perform the authentication operations.
	 * </p>
	 * @param authenticationManager The Spring Security {@link AuthenticationManager} (not null)
	 * @return A new {@link AuthContext} instance
	 * @see #authContext(Realm)
	 */
	static AuthContext authContext(AuthenticationManager authenticationManager) {
		return authContext(authenticationManager, false);
	}

	/**
	 * Create an {@link AuthContext} which uses the Spring Security {@link SecurityContext} as authentication holder.
	 * The default {@link SecurityContextHolder#getContext()} method is used to obtain the Spring Security
	 * {@link SecurityContext} reference.
	 * <p>
	 * A default {@link Realm} is created using the default authorizer and registering an {@link Authenticator} hich
	 * supports {@link SpringSecurityAuthenticationToken} types and uses the Spring Security
	 * {@link AuthenticationManager} to perform the authentication operations.
	 * </p>
	 * @param authenticationManager The Spring Security {@link AuthenticationManager} (not null)
	 * @param accountCredentialsAuthenticator Whether to register in Realm an Authenticator for the default
	 *        {@link AccountCredentialsToken} which uses the Spring Security {@link AuthenticationManager} to perform
	 *        the authentication operations.
	 * @return A new {@link AuthContext} instance
	 * @see #authContext(Realm)
	 */
	static AuthContext authContext(AuthenticationManager authenticationManager,
			boolean accountCredentialsAuthenticator) {
		final Realm.Builder realm = Realm.builder().withDefaultAuthorizer()
				.authenticator(authenticator(authenticationManager));
		if (accountCredentialsAuthenticator) {
			realm.authenticator(accountCredentialsAuthenticator(authenticationManager));
		}
		return AuthContext.create(realm.build(), new SpringSecurityAuthenticationHolder());
	}

	/**
	 * Create an {@link Authenticator} which supports {@link SpringSecurityAuthenticationToken} types and uses the
	 * Spring Security {@link AuthenticationManager} to perform the authentication operations. The
	 * {@link SpringSecurityAuthenticationToken#getAuthentication()} reference is used as authentication token whithin
	 * the Spring Security environment.
	 * @param authenticationManager The Spring Security {@link AuthenticationManager} (not null)
	 * @return The new {@link Authenticator} instance
	 */
	static Authenticator<SpringSecurityAuthenticationToken> authenticator(AuthenticationManager authenticationManager) {
		return new DefaultAuthenticationManagerAuthenticator(authenticationManager);
	}

	/**
	 * Create an {@link Authenticator} for the default {@link AccountCredentialsToken}, using the Spring Security
	 * {@link AuthenticationManager} to perform the authentication operations.
	 * @param authenticationManager The Spring Security {@link AuthenticationManager} (not null)
	 * @return The new {@link Authenticator} instance
	 */
	static Authenticator<AccountCredentialsToken> accountCredentialsAuthenticator(
			AuthenticationManager authenticationManager) {
		return new AccountCredentialsAuthenticationManagerAuthenticator(authenticationManager);
	}

	/**
	 * Create a Spring Security {@link AuthenticationProvider} using an {@link Authenticator} as concrete authentication
	 * provider. Authentication execeptions translation is applied by default.
	 * @param <T> Authentication token type
	 * @param <A> Spring Security {@link org.springframework.security.core.Authentication} type
	 * @param authenticator The concrete {@link Authenticator}
	 * @param authenticationType Supported Spring Security {@link org.springframework.security.core.Authentication} type
	 * @param converter Function to convert the Spring Security {@link Authentication} into the required
	 *        {@link AuthenticationToken} type
	 * @return A new {@link AuthenticationProvider} using given {@link Authenticator}
	 */
	static <T extends AuthenticationToken, A extends org.springframework.security.core.Authentication> AuthenticationProvider authenticationProvider(
			Authenticator<T> authenticator, Class<A> authenticationType, Function<A, T> converter) {
		return new AuthenticationProviderAdapter<>(authenticator, authenticationType, converter);
	}

	/**
	 * Get the {@link Authentication} representation of the given Spring Security Authentication.
	 * <p>
	 * If authentication details are available through
	 * {@link org.springframework.security.core.Authentication#getDetails()}, it will be available as an
	 * {@link Authentication} parameter with name {@link SpringSecurityAuthentication#AUTHENTICATION_DETAILS_KEY}.
	 * </p>
	 * @param authentication The Spring Security Authentication (not null)
	 * @return The {@link Authentication} representation of the Spring Security Authentication
	 */
	static Authentication asAuthentication(org.springframework.security.core.Authentication authentication) {
		return SpringSecurityAuthentication.create(authentication);
	}

	/**
	 * Get the {@link Permission} representation of the given Spring Security {@link GrantedAuthority}.
	 * @param authority The {@link GrantedAuthority} to be represented as a {@link Permission} (not null)
	 * @return The {@link Permission} representation of the Spring Security {@link GrantedAuthority}
	 */
	static Permission asPermission(GrantedAuthority authority) {
		return new SpringSecurityPermission(authority);
	}

	/**
	 * Get the Spring Security {@link GrantedAuthority} representation of the given {@link Permission}.
	 * @param permission The {@link Permission} to be represented as a {@link GrantedAuthority} (not null)
	 * @return The Spring Security {@link GrantedAuthority} representation of given {@link Permission}
	 */
	static GrantedAuthority asAuthority(Permission permission) {
		return new PermissionGrantedAuthority(permission);
	}

	/**
	 * Create a new {@link AuthenticationToken} using given Spring Security {@link Authentication} as concrete
	 * authentication token.
	 * @param authentication the Spring Security {@link Authentication} token (not null)
	 * @return A new {@link SpringSecurityAuthenticationToken}
	 */
	static SpringSecurityAuthenticationToken asAuthenticationToken(
			org.springframework.security.core.Authentication authentication) {
		return SpringSecurityAuthenticationToken.create(authentication);
	}

	/**
	 * Create an account credentials authentican token, using the Spring Security
	 * {@link UsernamePasswordAuthenticationToken} as concrete authentication token.
	 * @param accountId Account id (username)
	 * @param secret Account secret (password)
	 * @return A new {@link SpringSecurityAuthenticationToken} with given account credentials
	 */
	static SpringSecurityAuthenticationToken asAuthenticationToken(String accountId, String secret) {
		return SpringSecurityAuthenticationToken.account(accountId, secret);
	}

}
