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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.holonplatform.auth.AuthContext;
import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Authentication.AuthenticationListener;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Authorizer;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
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
public class DefaultAuthContext implements AuthContext {

	/*
	 * Realm
	 */
	private final Realm realm;

	/*
	 * Authentication holder
	 */
	private final AuthenticationHolder authenticationHolder;

	/*
	 * Authentication listeners
	 */
	private List<AuthenticationListener> authenticationListeners;

	/**
	 * Constructor using the default {@link AuthenticationHolder}.
	 * @param realm the Realm which will be used as {@link Authenticator} and {@link Authorizer} (not null)
	 */
	public DefaultAuthContext(Realm realm) {
		this(realm, new DefaultAuthenticationHolder());
	}

	/**
	 * Constructor.
	 * @param realm the Realm which will be used as {@link Authenticator} and {@link Authorizer} (not null)
	 * @param authenticationHolder {@link Authentication} holder (not null)
	 */
	public DefaultAuthContext(Realm realm, AuthenticationHolder authenticationHolder) {
		super();
		ObjectUtils.argumentNotNull(realm, "Realm must be not null");
		ObjectUtils.argumentNotNull(authenticationHolder, "AuthenticationHolder must be not null");
		this.realm = realm;
		this.authenticationHolder = authenticationHolder;
	}

	/**
	 * Get the {@link Realm} bound to this auth context.
	 * @return The auth context realm
	 */
	protected Realm getRealm() {
		return realm;
	}

	/**
	 * Get the {@link AuthenticationHolder} bound to this auth context.
	 * @return the auth context Authentication holder
	 */
	protected AuthenticationHolder getAuthenticationHolder() {
		return authenticationHolder;
	}

	/**
	 * Set the current Authentication
	 * @param authentication Authentication to set
	 */
	protected void setAuthentication(Authentication authentication) {
		setAuthentication(authentication, true);
	}

	/**
	 * Set the current {@link Authentication}.
	 * @param fireEvents Whether to trigger or not any registered {@link AuthenticationListener}
	 * @param authentication Authentication to set, may be null
	 */
	protected void setAuthentication(Authentication authentication, boolean fireEvents) {
		getAuthenticationHolder().setAuthentication(authentication);

		if (fireEvents) {
			fireAuthenticationListeners(authentication);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#getAuthentication()
	 */
	@Override
	public Optional<Authentication> getAuthentication() {
		return getAuthenticationHolder().getAuthentication();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#authenticate(com.holonplatform.auth.AuthenticationToken)
	 */
	@Override
	public Authentication authenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
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
	public Optional<Authentication> unauthenticate() {
		Optional<Authentication> authc = getAuthentication();
		if (authc.isPresent()) {
			// remove authentication
			setAuthentication(null);
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
	public boolean isPermitted(Permission... permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermitted(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermitted(java.lang.String[])
	 */
	@Override
	public boolean isPermitted(String... permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermitted(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermittedAny(com.holonplatform.auth.Permission[])
	 */
	@Override
	public boolean isPermittedAny(Permission... permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermittedAny(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermittedAny(java.lang.String[])
	 */
	@Override
	public boolean isPermittedAny(String... permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermittedAny(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermitted(java.util.Collection)
	 */
	@Override
	public boolean isPermitted(Collection<? extends Permission> permissions) {
		return getAuthentication().map((a) -> getAuthorizer().isPermitted(a, permissions)).orElse(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.AuthContext#isPermittedAny(com.holonplatform.auth.Authentication,
	 * java.util.Collection)
	 */
	@Override
	public boolean isPermittedAny(Collection<? extends Permission> permissions) {
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
		if (authenticationListener != null) {
			if (authenticationListeners == null) {
				authenticationListeners = new LinkedList<>();
			}
			authenticationListeners.add(authenticationListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.auth.events.AuthenticationNotifier#removeAuthenticationListener(com.holonplatform.auth.events
	 * .AuthenticationListener)
	 */
	@Override
	public void removeAuthenticationListener(AuthenticationListener authenticationListener) {
		if (authenticationListeners != null && authenticationListener != null) {
			authenticationListeners.remove(authenticationListener);
		}
	}

	/**
	 * Get the registered {@link AuthenticationListener}s.
	 * @return the registered authentication listeners, an empty List if none
	 */
	protected List<AuthenticationListener> getAuthenticationListeners() {
		return (authenticationListeners != null) ? authenticationListeners : Collections.emptyList();
	}

	/**
	 * Fire any registered {@link AuthenticationListener}
	 * @param authentication Authentication
	 */
	protected void fireAuthenticationListeners(final Authentication authentication) {
		for (AuthenticationListener authenticationListener : getAuthenticationListeners()) {
			authenticationListener.onAuthentication(authentication);
		}
	}

}
