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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.holonplatform.auth.Account;
import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.Permission;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link Account} implementation
 *
 * @since 5.0.0
 */
public class DefaultAccount implements Account {

	private static final long serialVersionUID = 9026409537754354536L;

	/*
	 * Account id
	 */
	private final String id;
	/*
	 * Root
	 */
	private boolean root = false;

	/*
	 * Credentials
	 */
	private Object credentials;

	/*
	 * Details
	 */
	private Map<String, Object> details;

	/*
	 * Permissions
	 */
	private Collection<Permission> permissions;

	/*
	 * Attributes
	 */
	private boolean enabled = true;
	private boolean locked;
	private boolean expired;

	/**
	 * Constructor
	 * @param id Account id (not null)
	 */
	public DefaultAccount(String id) {
		super();
		ObjectUtils.argumentNotNull(id, "Account id must be not null");
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.account.Account#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.account.Account#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return credentials;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.account.Account#isRoot()
	 */
	@Override
	public boolean isRoot() {
		return root;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.account.Account#getDetails()
	 */
	@Override
	public Map<String, Object> getDetails() {
		return (details != null) ? details : Collections.emptyMap();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.account.Account#getPermissions()
	 */
	@Override
	public Collection<Permission> getPermissions() {
		return (permissions != null) ? permissions : Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.account.Account#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.account.Account#isLocked()
	 */
	@Override
	public boolean isLocked() {
		return locked;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.account.Account#isExpired()
	 */
	@Override
	public boolean isExpired() {
		return expired;
	}

	/**
	 * Set whether account is <code>root</code>, i.e. has any permission
	 * @param root If <code>true</code>, {@link #getPermissions()} is ignored.
	 */
	public void setRoot(boolean root) {
		this.root = root;
	}

	/**
	 * Set account credentials
	 * @param credentials the credentials to set
	 */
	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}

	/**
	 * Set account details
	 * @param details the details to set
	 */
	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	/**
	 * Add (or replace if given key already exists) a detail
	 * @param key Key
	 * @param value Value
	 */
	public void setDetail(String key, Object value) {
		if (key != null) {
			if (details == null) {
				details = new HashMap<>();
			}
			details.put(key, value);
		}
	}

	/**
	 * Set account permissions
	 * @param permissions the permissions to set
	 */
	public void setPermissions(Collection<Permission> permissions) {
		this.permissions = new ArrayList<>();
		if (permissions != null) {
			permissions.forEach(p -> this.permissions.add(p));
		}
	}

	/**
	 * Add given {@link Permission} to granted permissions set
	 * @param permission Permission to add
	 */
	protected void addPermission(Permission permission) {
		if (permission != null) {
			if (permissions == null) {
				permissions = new ArrayList<>();
			}
			permissions.add(permission);
		}
	}

	/**
	 * Set whether is enabled
	 * @param enabled <code>true</code> if enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Set whether is locked
	 * @param locked <code>true</code> if locked
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * Set whether has expired
	 * @param expired <code>true</code> if expired
	 */
	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultAccount other = (DefaultAccount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultAccount [id=" + id + "]";
	}

	// Builder

	/**
	 * Default {@link AccountBuilder} implementation
	 */
	public static class AccountBuilder implements Builder {

		private final DefaultAccount account;

		/**
		 * Constructor
		 * @param id Account id
		 */
		public AccountBuilder(String id) {
			super();
			this.account = new DefaultAccount(id);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.account.AccountBuilder#root(boolean)
		 */
		@Override
		public Builder root(boolean root) {
			this.account.setRoot(root);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.account.AccountBuilder#details(java.util.Map)
		 */
		@Override
		public Builder details(Map<String, Object> details) {
			this.account.setDetails(details);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.Account.Builder#withDetail(java.lang.String, java.lang.Object)
		 */
		@Override
		public Builder withDetail(String key, Object value) {
			this.account.setDetail(key, value);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.account.AccountBuilder#permissions(java.util.Collection)
		 */
		@Override
		public Builder permissions(Collection<Permission> permissions) {
			this.account.setPermissions(permissions);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.Account.Builder#withPermission(com.holonplatform.auth.Permission)
		 */
		@Override
		public Builder withPermission(Permission permission) {
			this.account.addPermission(permission);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.account.AccountBuilder#permissionStrings(java.util.Collection)
		 */
		@Override
		public Builder permissionStrings(Collection<String> permissions) {
			if (permissions != null) {
				for (String permission : permissions) {
					if (permission != null) {
						this.account.addPermission(Permission.create(permission));
					}
				}
			}
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.Account.Builder#withPermission(java.lang.String)
		 */
		@Override
		public Builder withPermission(String permission) {
			if (permission != null) {
				this.account.addPermission(Permission.create(permission));
			}
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.account.AccountBuilder#enabled(boolean)
		 */
		@Override
		public Builder enabled(boolean enabled) {
			this.account.setEnabled(enabled);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.account.AccountBuilder#locked(boolean)
		 */
		@Override
		public Builder locked(boolean locked) {
			this.account.setLocked(locked);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.account.AccountBuilder#expired(boolean)
		 */
		@Override
		public Builder expired(boolean expired) {
			this.account.setExpired(expired);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.account.AccountBuilder#credentials(com.holonplatform.auth.Credentials)
		 */
		@Override
		public Builder credentials(Credentials credentials) {
			this.account.setCredentials(credentials);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.account.AccountBuilder#credentials(java.lang.Object)
		 */
		@Override
		public Builder credentials(Object credentials) {
			this.account.setCredentials(credentials);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.auth.internal.account.AccountBuilder#build()
		 */
		@Override
		public Account build() {
			return account;
		}

	}

}
