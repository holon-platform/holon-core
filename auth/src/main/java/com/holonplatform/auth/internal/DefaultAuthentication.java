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
import java.util.LinkedList;
import java.util.Optional;

import com.holonplatform.auth.Authentication;
import com.holonplatform.auth.Permission;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.internal.DefaultParameterSet;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;

/**
 * Default {@link Authentication} implementation.
 * 
 * <p>
 * This implementation relies on Principal name as authentication identity, so {@link #equals(Object)} and
 * {@link #hashCode()} methods are written according to this assumption.
 * </p>
 * 
 * @since 5.0.0
 */
public class DefaultAuthentication extends DefaultParameterSet implements Authentication {

	private static final long serialVersionUID = -7914956150505864840L;

	/**
	 * Principal name
	 */
	private final String name;

	/**
	 * Root (all permissions)
	 */
	private boolean root = false;

	/**
	 * Permissions
	 */
	private final Collection<Permission> permissions = new LinkedList<>();

	/**
	 * Authentication scheme
	 */
	private String scheme;

	/**
	 * Constructor
	 * @param name Principal name (for example, the username), not null
	 */
	public DefaultAuthentication(String name) {
		super();
		ObjectUtils.argumentNotNull(name, "Principal name must be not null");
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authentication#getScheme()
	 */
	@Override
	public Optional<String> getScheme() {
		return Optional.ofNullable(scheme);
	}

	/**
	 * Set the authentication scheme
	 * @param scheme the scheme to set
	 */
	protected void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authentication#isRoot()
	 */
	@Override
	public boolean isRoot() {
		return root;
	}

	/**
	 * Set whether this Authentication is considered a <code>root</code> principal, for which permission checking is
	 * always skipped, assuming that any permission is granted to this Authentication.
	 * @param root <code>true</code> to set this Authentication as <code>root</code> principal
	 */
	public void setRoot(boolean root) {
		this.root = root;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.Authentication#getPermissions()
	 */
	@Override
	public Collection<Permission> getPermissions() {
		return permissions;
	}

	/**
	 * Check value is consistent with given <code>type</code>
	 * @param <T> Detail value type
	 * @param value Value to check
	 * @param type Required type
	 * @return Value
	 * @throws IllegalArgumentException Types mismatch
	 */
	@SuppressWarnings("unchecked")
	protected <T> T checkDetailValue(Object value, Class<T> type) {
		if (value != null) {
			if (!TypeUtils.isAssignable(value.getClass(), type)) {
				throw new IllegalArgumentException("Detail value type " + value.getClass().getName()
						+ " doesn't match required type " + type.getName());
			}
			return (T) value;
		}
		return null;
	}

	/**
	 * Add given {@link Permission} to granted permissions set
	 * @param permission Permission to add
	 */
	protected void addPermission(Permission permission) {
		if (!permissions.contains(permission)) {
			permissions.add(permission);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DefaultAuthentication [");
		sb.append("name=");
		sb.append(name);
		getScheme().ifPresent(s -> {
			sb.append(", scheme=");
			sb.append(s);
		});
		if (isRoot()) {
			sb.append(" (root)");
		}
		sb.append("]");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Authentication)) {
			return false;
		}
		Authentication other = (Authentication) obj;
		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}

	// Builder

	/**
	 * Default {@link Authentication.Builder} implementation.
	 */
	public static class AuthenticationBuilder implements Authentication.Builder {

		private final DefaultAuthentication authentication;

		/**
		 * Constructor
		 * @param principalName Principal name (not null)
		 */
		public AuthenticationBuilder(String principalName) {
			super();
			ObjectUtils.argumentNotNull(principalName, "Principal name must be not null");
			this.authentication = new DefaultAuthentication(principalName);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.Authentication.Builder#scheme(java.lang.String)
		 */
		@Override
		public Authentication.Builder scheme(String scheme) {
			this.authentication.setScheme(scheme);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.AuthenticationBuilder#root(boolean)
		 */
		@Override
		public Authentication.Builder root(boolean root) {
			this.authentication.setRoot(root);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.Authentication.Builder#parameter(java.lang.String, java.lang.Object)
		 */
		@Override
		public Authentication.Builder parameter(String name, Object value) {
			ObjectUtils.argumentNotNull(name, "Parameter name must be not null");
			this.authentication.addParameter(name, value);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.Authentication.Builder#parameter(com.holonplatform.core.config.ConfigProperty,
		 * java.lang.Object)
		 */
		@Override
		public <T> Authentication.Builder parameter(ConfigProperty<T> property, T value) {
			ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
			this.authentication.addParameter(property.getKey(), value);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.AuthenticationBuilder#permission(com.holonplatform.auth.Permission)
		 */
		@Override
		public Authentication.Builder permission(Permission permission) {
			ObjectUtils.argumentNotNull(permission, "Permission must be not null");
			this.authentication.addPermission(permission);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.AuthenticationBuilder#permissionStrings(java.lang.String[])
		 */
		@Override
		public Authentication.Builder permission(String permission) {
			ObjectUtils.argumentNotNull(permission, "Permission must be not null");
			this.authentication.addPermission(Permission.create(permission));
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.AuthenticationBuilder#build()
		 */
		@Override
		public Authentication build() {
			return authentication;
		}

	}

}
