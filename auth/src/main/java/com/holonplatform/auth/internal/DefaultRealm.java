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

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Authentication.AuthenticationListener;
import com.holonplatform.auth.AuthenticationToken;
import com.holonplatform.auth.AuthenticationToken.AuthenticationTokenResolver;
import com.holonplatform.auth.Authenticator;
import com.holonplatform.auth.Authorizer;
import com.holonplatform.auth.Permission;
import com.holonplatform.auth.Realm;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.UnexpectedAuthenticationException;
import com.holonplatform.auth.exceptions.UnsupportedMessageException;
import com.holonplatform.auth.exceptions.UnsupportedPermissionException;
import com.holonplatform.auth.exceptions.UnsupportedTokenException;
import com.holonplatform.core.internal.utils.TypeUtils;
import com.holonplatform.core.messaging.Message;

/**
 * Default {@link Realm} implementation
 *
 * @since 5.0.0
 */
public class DefaultRealm implements Realm {

	/*
	 * Realm name
	 */
	private String name;

	/*
	 * Authenticators
	 */
	@SuppressWarnings("rawtypes")
	private final LinkedList<Authenticator> authenticators = new LinkedList<>();

	/*
	 * Authorizers
	 */
	@SuppressWarnings("rawtypes")
	private final LinkedList<Authorizer> authorizers = new LinkedList<>();

	/*
	 * Message token resolvers
	 */
	@SuppressWarnings("rawtypes")
	private final LinkedList<AuthenticationTokenResolver> authenticationTokenResolvers = new LinkedList<>();

	/*
	 * Authentication listeners
	 */
	private List<AuthenticationListener> authenticationListeners;

	/**
	 * Constructor
	 */
	public DefaultRealm() {
		this(null);
	}

	/**
	 * Constructor with Realm name.
	 * @param name Realm name
	 */
	public DefaultRealm(String name) {
		super();
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Realm#getName()
	 */
	@Override
	public Optional<String> getName() {
		return Optional.ofNullable(name);
	}

	/**
	 * Set Realm name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Available Authenticators
	 * @return Authenticators
	 */
	@SuppressWarnings("rawtypes")
	protected List<Authenticator> getAuthenticators() {
		return authenticators;
	}

	/**
	 * Available Authorizers
	 * @return Authorizers
	 */
	@SuppressWarnings("rawtypes")
	protected LinkedList<Authorizer> getAuthorizers() {
		return authorizers;
	}

	/**
	 * Available AuthenticationTokenResolvers
	 * @return AuthenticationTokenResolvers
	 */
	@SuppressWarnings("rawtypes")
	protected LinkedList<AuthenticationTokenResolver> getAuthenticationTokenResolvers() {
		return authenticationTokenResolvers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.MessageAuthenticator#getResolversForMessageType(java.lang.Class)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T extends Message> List<AuthenticationTokenResolver<T>> getResolversForMessageType(Class<T> messageType) {
		if (messageType != null) {
			LinkedList<AuthenticationTokenResolver<T>> resolvers = new LinkedList<>();
			for (AuthenticationTokenResolver resolver : getAuthenticationTokenResolvers()) {
				if (TypeUtils.isAssignable(messageType, resolver.getMessageType())) {
					resolvers.add(resolver);
				}
			}
			return resolvers;
		}
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authenticator#getTokenType()
	 */
	@Override
	public Class<? extends AuthenticationToken> getTokenType() {
		return AuthenticationToken.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Realm#addAuthenticator(com.holonplatform.auth.Authenticator)
	 */
	@Override
	public <T extends AuthenticationToken> void addAuthenticator(Authenticator<T> authenticator) {
		if (authenticator != null && !getAuthenticators().contains(authenticator)) {
			getAuthenticators().add(authenticator);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authenticator#supportsToken(java.lang.Class)
	 */
	@Override
	public boolean supportsToken(Class<? extends AuthenticationToken> authenticationTokenType) {
		if (authenticationTokenType != null) {
			for (Authenticator<?> authenticator : getAuthenticators()) {
				if (TypeUtils.isAssignable(authenticationTokenType, authenticator.getTokenType())) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authenticator#authenticate(com.holonplatform.auth.AuthenticationToken)
	 */
	@Override
	public Authentication authenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
		return authenticate(authenticationToken, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Realm#authenticate(com.holonplatform.auth.AuthenticationToken, boolean)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Authentication authenticate(AuthenticationToken authenticationToken, boolean fireEvents)
			throws AuthenticationException {

		if (authenticationToken == null) {
			throw new UnexpectedAuthenticationException("Null AuthenticationToken");
		}

		// get suitable Authenticator

		List<Authenticator> authenticators = getAuthenticators();

		if (authenticators.isEmpty()) {
			throw new UnsupportedTokenException("No Authenticator available. Check Realm configuration.");
		}

		Authenticator tokenAuthenticator = null;
		for (Authenticator authenticator : authenticators) {
			if (TypeUtils.isAssignable(authenticationToken.getClass(), authenticator.getTokenType())) {
				tokenAuthenticator = authenticator;
				break;
			}
		}

		if (tokenAuthenticator == null) {
			throw new UnsupportedTokenException(
					"Unsupported authentication token type: " + authenticationToken.getClass().getName());
		}

		// authenticate
		Authentication authc = tokenAuthenticator.authenticate(authenticationToken);

		if (authc == null) {
			throw new UnexpectedAuthenticationException("Authenticator " + tokenAuthenticator.getClass().getName()
					+ " returned a null Authentication for token: " + authenticationToken.getClass().getName());
		}

		// fire listeners
		if (fireEvents) {
			fireAuthenticationListeners(authc);
		}

		return authc;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.MessageAuthenticator#addAuthenticationTokenResolver(com.holonplatform.auth.
	 * AuthenticationTokenResolver)
	 */
	@Override
	public void addAuthenticationTokenResolver(AuthenticationTokenResolver<?> authenticationTokenResolver) {
		if (authenticationTokenResolver != null
				&& !getAuthenticationTokenResolvers().contains(authenticationTokenResolver)) {
			getAuthenticationTokenResolvers().add(authenticationTokenResolver);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.MessageAuthenticator#supportsMessage(java.lang.Class)
	 */
	@Override
	public boolean supportsMessage(Class<? extends Message<?, ?>> messageType) {
		if (messageType != null) {
			for (AuthenticationTokenResolver<?> resolver : getAuthenticationTokenResolvers()) {
				if (TypeUtils.isAssignable(messageType, resolver.getMessageType())) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.auth.Authenticator.MessageAuthenticator#authenticate(com.holonplatform.core.messaging.Message,
	 * java.lang.String[])
	 */
	@Override
	public Authentication authenticate(Message<?, ?> message, String... schemes) throws AuthenticationException {
		return authenticate(message, true, schemes);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Realm#authenticate(com.holonplatform.core.messaging.Message, boolean,
	 * java.lang.String[])
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Authentication authenticate(Message<?, ?> message, boolean fireEvents, String... schemes)
			throws AuthenticationException {

		if (message == null) {
			throw new UnexpectedAuthenticationException("Null Message");
		}

		// get suitable resolvers
		List<AuthenticationTokenResolver<Message>> resolvers = getResolversForMessageType(
				(Class<Message>) message.getClass());
		if (resolvers.isEmpty()) {
			throw new UnsupportedMessageException(
					"No AuthenticationTokenResolver available for message type " + message.getClass().getName());
		}

		// perform authentication

		return authenticate(resolveAuthenticationToken(message, resolvers, schemes).orElseThrow(
				() -> new UnsupportedMessageException("No AuthenticationTokenResolver resolved message" + message)),
				fireEvents);
	}

	/**
	 * Try to resolve an {@link AuthenticationToken} from given <code>message</code> using available resolvers fro
	 * specific message type.
	 * @param message Message
	 * @param resolvers Resolvers for given message type
	 * @param schemes Optional authentication schemes to use
	 * @return Resolved token, or empty if none of resolvers was able to resolve a token form given message
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Optional<AuthenticationToken> resolveAuthenticationToken(Message message,
			Iterable<AuthenticationTokenResolver<Message>> resolvers, String... schemes) {
		for (AuthenticationTokenResolver resolver : resolvers) {
			if (schemes == null || schemes.length == 0 || isResolverInSchemes(resolver, schemes)) {
				Optional<AuthenticationToken> resolved = resolver.getAuthenticationToken(message);
				if (resolved.isPresent()) {
					return resolved;
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Check if given resolver is bound to any of given scheme names checking
	 * {@link AuthenticationTokenResolver#getScheme()}
	 * @param resolver Resolver
	 * @param schemes Scheme names
	 * @return <code>true</code> if given resolver is bound to any of given scheme names
	 */
	@SuppressWarnings("rawtypes")
	protected boolean isResolverInSchemes(AuthenticationTokenResolver resolver, String... schemes) {
		if (schemes != null) {
			for (String scheme : schemes) {
				if (scheme != null && resolver.getScheme().isPresent()
						&& scheme.equalsIgnoreCase(resolver.getScheme().get().toString())) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#getPermissionType()
	 */
	@Override
	public Class<? extends Permission> getPermissionType() {
		return Permission.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#supportsPermission(java.lang.Class)
	 */
	@Override
	public boolean supportsPermission(Class<? extends Permission> permissionType) {
		if (permissionType != null) {
			for (Authorizer<?> authorizer : getAuthorizers()) {
				if (TypeUtils.isAssignable(permissionType, authorizer.getPermissionType())) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Realm#addAuthorizer(com.holonplatform.auth.Authorizer)
	 */
	@Override
	public <P extends Permission> void addAuthorizer(Authorizer<P> authorizer) {
		if (authorizer != null && !getAuthorizers().contains(authorizer)) {
			getAuthorizers().add(authorizer);
		}
	}

	/**
	 * Get {@link Authorizer} to use with given permission type
	 * <p>
	 * This first Authorizer registered in this Realm which is consistent with given <code>permissionType</code> is
	 * used.
	 * </p>
	 * @param permissionType Permission type
	 * @return Authorizer
	 * @throws UnsupportedPermissionException If no {@link Authorizer} is available for given permission type
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Authorizer<Permission> getAuthorizer(Class<? extends Permission> permissionType)
			throws UnsupportedPermissionException {
		Authorizer<Permission> permissionAuthorizer = null;
		for (Authorizer authorizer : getAuthorizers()) {
			if (TypeUtils.isAssignable(permissionType, authorizer.getPermissionType())) {
				permissionAuthorizer = authorizer;
				break;
			}
		}

		if (permissionAuthorizer == null) {
			throw new UnsupportedPermissionException("Unsupported permission type: " + permissionType.getName()
					+ " - No suitable Authorizer available in Realm");
		}

		return permissionAuthorizer;
	}

	/**
	 * Retrieve Permission type from given permission collection
	 * @param permissions Permissions
	 * @return Permission type
	 */
	protected Class<? extends Permission> getPermissionType(Collection<? extends Permission> permissions) {
		if (permissions != null && !permissions.isEmpty()) {
			Permission p = permissions.iterator().next();
			if (p != null) {
				return p.getClass();
			}
		}
		return Permission.class;
	}

	/**
	 * Retrieve Permission type from given permission array
	 * @param permissions Permissions
	 * @return Permission type
	 */
	protected Class<? extends Permission> getPermissionType(Permission[] permissions) {
		if (permissions != null && permissions.length > 0) {
			Permission p = permissions[0];
			if (p != null) {
				return p.getClass();
			}
		}
		return Permission.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermitted(com.holonplatform.auth.Authentication,
	 * com.holonplatform.auth.Permission[])
	 */
	@Override
	public boolean isPermitted(Authentication authentication, Permission... permissions) {
		return getAuthorizer(getPermissionType(permissions)).isPermitted(authentication, permissions);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermitted(com.holonplatform.auth.Authentication, java.lang.String[])
	 */
	@Override
	public boolean isPermitted(Authentication authentication, String... permissions) {
		return getAuthorizer(Permission.class).isPermitted(authentication, permissions);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermittedAny(com.holonplatform.auth.Authentication,
	 * com.holonplatform.auth.Permission[])
	 */
	@Override
	public boolean isPermittedAny(Authentication authentication, Permission... permissions) {
		return getAuthorizer(getPermissionType(permissions)).isPermittedAny(authentication, permissions);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermittedAny(com.holonplatform.auth.Authentication, java.lang.String[])
	 */
	@Override
	public boolean isPermittedAny(Authentication authentication, String... permissions) {
		return getAuthorizer(Permission.class).isPermittedAny(authentication, permissions);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermitted(com.holonplatform.auth.Authentication, java.util.Collection)
	 */
	@Override
	public boolean isPermitted(Authentication authentication, Collection<? extends Permission> permissions) {
		return getAuthorizer(getPermissionType(permissions)).isPermitted(authentication, permissions);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authorizer#isPermittedAny(com.holonplatform.auth.Authentication,
	 * java.util.Collection)
	 */
	@Override
	public boolean isPermittedAny(Authentication authentication, Collection<? extends Permission> permissions) {
		return getAuthorizer(getPermissionType(permissions)).isPermittedAny(authentication, permissions);
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

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.events.AuthenticationNotifier#getAuthenticationListeners()
	 */
	@Override
	public List<AuthenticationListener> getAuthenticationListeners() {
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultRealm [name=" + name + "]";
	}

	// Builder

	/**
	 * Default {@link Builder} implementation.
	 */
	public static class RealmBuilder implements Builder {

		private final DefaultRealm realm;

		/**
		 * Constructor
		 */
		public RealmBuilder() {
			super();
			this.realm = new DefaultRealm();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.RealmBuilder#name(java.lang.String)
		 */
		@Override
		public Builder name(String name) {
			this.realm.setName(name);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.RealmBuilder#authenticator(com.holonplatform.auth.Authenticator)
		 */
		@Override
		public <T extends AuthenticationToken> Builder authenticator(Authenticator<T> authenticator) {
			this.realm.addAuthenticator(authenticator);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.auth.internal.RealmBuilder#resolver(com.holonplatform.auth.AuthenticationTokenResolver)
		 */
		@Override
		public Builder resolver(AuthenticationTokenResolver<?> authenticationTokenResolver) {
			this.realm.addAuthenticationTokenResolver(authenticationTokenResolver);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.RealmBuilder#authorizer(com.holonplatform.auth.Authorizer)
		 */
		@Override
		public <P extends Permission> Builder authorizer(Authorizer<P> authorizer) {
			this.realm.addAuthorizer(authorizer);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.RealmBuilder#withDefaultAuthorizer()
		 */
		@Override
		public Builder withDefaultAuthorizer() {
			this.realm.addAuthorizer(Authorizer.create());
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.RealmBuilder#listener(com.holonplatform.auth.AuthenticationListener)
		 */
		@Override
		public Builder listener(AuthenticationListener authenticationListener) {
			this.realm.addAuthenticationListener(authenticationListener);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.RealmBuilder#build()
		 */
		@Override
		public Realm build() {
			return realm;
		}

	}

}
