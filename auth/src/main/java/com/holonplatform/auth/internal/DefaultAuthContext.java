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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.holonplatform.auth.AuthContext;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Authorizer;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.Authentication.AuthenticationListener;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.messaging.Message;

/**
 * Default {@link AuthContext} implementation using a {@link Realm} to perform authentication and authorization
 * operations.
 * 
 * @since 5.0.0
 */
@SuppressWarnings("boxing")
public class DefaultAuthContext implements AuthContext {

	/*
	 * Realm (immutable)
	 */
	private final Realm realm;

	/*
	 * Current authentication
	 */
	private Authentication authentication;

	/**
	 * Constructor
	 * @param realm Realm which will be used as {@link Authenticator} and {@link Authorizer} (not null)
	 */
	public DefaultAuthContext(Realm realm) {
		super();
		ObjectUtils.argumentNotNull(realm, "Realm must be not null");
		this.realm = realm;
	}

	/**
	 * AuthContext Realm
	 * @return Realm
	 */
	protected Realm getRealm() {
		return realm;
	}

	/**
	 * Set the current Authentication
	 * @param authentication Authentication to set
	 */
	protected void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#getAuthentication()
	 */
	@Override
	public synchronized Optional<Authentication> getAuthentication() {
		return Optional.ofNullable(authentication);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#authenticate(com.holonplatform.auth.AuthenticationToken)
	 */
	@Override
	public synchronized Authentication authenticate(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		Authentication authentication = getRealm().authenticate(authenticationToken);
		if (authentication == null) {
			throw new UnexpectedAuthenticationException("Authenticator returned a null Authentication");
		}
		setAuthentication(authentication);
		return authentication;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#authenticate(com.holonplatform.core.messaging.Message,
	 * java.lang.String[])
	 */
	@Override
	public Authentication authenticate(Message<?, ?> message, String... schemes) throws AuthenticationException {
		Authentication authentication = getRealm().authenticate(message, schemes);
		if (authentication == null) {
			throw new UnexpectedAuthenticationException("Authenticator returned a null Authentication");
		}
		setAuthentication(authentication);
		return authentication;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#unauthenticate()
	 */
	@Override
	public synchronized Optional<Authentication> unauthenticate() {
		Optional<Authentication> authc = getAuthentication();
		if (authc.isPresent()) {
			// remove authentication
			setAuthentication(null);
			// notify unauthenticated
			List<AuthenticationListener> authenticationListeners = getRealm().getAuthenticationListeners();
			if (authenticationListeners != null) {
				for (AuthenticationListener authenticationListener : authenticationListeners) {
					authenticationListener.onAuthentication(null);
				}
			}
		}
		return authc;
	}

	/**
	 * Get {@link Authorizer} to use to check permissions
	 * @return By default the {@link Realm} instance
	 */
	protected Authorizer<Permission> getAuthorizer() {
		return getRealm();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermitted(com.holonplatform.auth.Permission[])
	 */
	@Override
	public synchronized boolean isPermitted(Permission... permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermitted(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermitted(java.lang.String[])
	 */
	@Override
	public synchronized boolean isPermitted(String... permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermitted(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermittedAny(com.holonplatform.auth.Permission[])
	 */
	@Override
	public synchronized boolean isPermittedAny(Permission... permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermittedAny(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermittedAny(java.lang.String[])
	 */
	@Override
	public synchronized boolean isPermittedAny(String... permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermittedAny(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermitted(java.util.Collection)
	 */
	@Override
	public synchronized boolean isPermitted(Collection<Permission> permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermitted(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermittedAny(com.holonplatform.auth.Authentication,
	 * java.util.Collection)
	 */
	@Override
	public synchronized boolean isPermittedAny(Collection<Permission> permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermittedAny(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.auth.events.AuthenticationNotifier#addAuthenticationListener(com.holonplatform.auth.events.
	 * AuthenticationListener)
	 */
	@Override
	public void addAuthenticationListener(AuthenticationListener authenticationListener) {
		getRealm().addAuthenticationListener(authenticationListener);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.auth.events.AuthenticationNotifier#removeAuthenticationListener(com.holonplatform.auth.events
	 * .AuthenticationListener)
	 */
	@Override
	public void removeAuthenticationListener(AuthenticationListener authenticationListener) {
		getRealm().removeAuthenticationListener(authenticationListener);
	}

}
