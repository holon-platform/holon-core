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
package com.holonplatform.http.internal;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.http.Cookie;

/**
 * Default {@link Cookie} implementation.
 * 
 * @since 5.0.0
 */
public class HttpCookie implements Cookie {

	private static final long serialVersionUID = -5514460008515151627L;

	private String name;
	private String value;
	private int version;
	private String path;
	private String domain;

	/**
	 * Empty constructor.
	 */
	protected HttpCookie() {
		this(null, null, DEFAULT_VERSION, null, null);
	}

	/**
	 * Constructor with {@link #DEFAULT_VERSION} and no path/domain
	 * @param name Name
	 * @param value Value
	 */
	public HttpCookie(String name, String value) {
		this(name, value, DEFAULT_VERSION, null, null);
	}

	/**
	 * Constructor with {@link #DEFAULT_VERSION}
	 * @param name Name
	 * @param value Value
	 * @param path Path
	 * @param domain Domain
	 */
	public HttpCookie(String name, String value, String path, String domain) {
		this(name, value, DEFAULT_VERSION, path, domain);
	}

	/**
	 * Constructor
	 * @param name Name
	 * @param value Value
	 * @param version Version
	 * @param path Path
	 * @param domain Domain
	 */
	public HttpCookie(String name, String value, int version, String path, String domain) {
		super();
		this.name = name;
		this.value = value;
		this.version = version;
		this.path = path;
		this.domain = domain;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.http.Cookie#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.http.Cookie#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.http.Cookie#getVersion()
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.http.Cookie#getDomain()
	 */
	@Override
	public String getDomain() {
		return domain;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.http.Cookie#getPath()
	 */
	@Override
	public String getPath() {
		return path;
	}

	/**
	 * Set the cookie name
	 * @param name the name to set (not null)
	 */
	public void setName(String name) {
		ObjectUtils.argumentNotNull(name, "Cookie name must be not null");
		this.name = name;
	}

	/**
	 * Set the cookie value
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Set the cookie version
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Set the cookie path
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Set the cookie domain
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + version;
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
		HttpCookie other = (HttpCookie) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HttpCookie [name=" + name + ", value=" + value + ", version=" + version + ", path=" + path + ", domain="
				+ domain + "]";
	}

	/**
	 * Default {@link Cookie} builder.
	 */
	public static class CookieBuilder implements Builder {

		/**
		 * Instance to build
		 */
		private final HttpCookie cookie = new HttpCookie();

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.Cookie.Builder#name(java.lang.String)
		 */
		@Override
		public Builder name(String name) {
			cookie.setName(name);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.Cookie.Builder#value(java.lang.String)
		 */
		@Override
		public Builder value(String value) {
			cookie.setValue(value);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.Cookie.Builder#version(int)
		 */
		@Override
		public Builder version(int version) {
			cookie.setVersion(version);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.Cookie.Builder#domain(java.lang.String)
		 */
		@Override
		public Builder domain(String domain) {
			cookie.setDomain(domain);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.Cookie.Builder#path(java.lang.String)
		 */
		@Override
		public Builder path(String path) {
			cookie.setPath(path);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.Cookie.Builder#build()
		 */
		@Override
		public Cookie build() {
			return cookie;
		}

	}

}
