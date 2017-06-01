/*
 * Copyright 2000-2016 Holon TDCN.
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
package com.holonplatform.http;

import java.io.Serializable;

import com.holonplatform.http.internal.HttpCookie;

/**
 * Represents a HTTP cookie
 * 
 * @since 5.0.0
 * 
 * @see HttpRequest
 */
public interface Cookie extends Serializable {

	/**
	 * Cookie default version according to RFC 2109
	 */
	public static final int DEFAULT_VERSION = 1;

	/**
	 * Get the name of the cookie
	 * @return the cookie name
	 */
	String getName();

	/**
	 * Get the value of the cookie
	 * @return the cookie value
	 */
	String getValue();

	/**
	 * Get the version of the cookie
	 * @return the cookie version
	 */
	int getVersion();

	/**
	 * Get the domain of the cookie
	 * @return the cookie domain
	 */
	String getDomain();

	/**
	 * Get the path of the cookie
	 * @return the cookie path
	 */
	String getPath();

	// Builder

	/**
	 * Get a builder to create a {@link Cookie} instance.
	 * @return Cookie builder
	 */
	public static Builder builder() {
		return new HttpCookie.CookieBuilder();
	}

	/**
	 * Cookie builder
	 */
	public interface Builder {

		/**
		 * Set the cookie name
		 * @param name Cookie name (not null)
		 * @return this
		 */
		Builder name(String name);

		/**
		 * Set the cookie value
		 * @param value Cookie value
		 * @return this
		 */
		Builder value(String value);

		/**
		 * Set the cookie version
		 * @param version Cookie version
		 * @return this
		 */
		Builder version(int version);

		/**
		 * Set the cookie domain
		 * @param domain Cookie domain
		 * @return this
		 */
		Builder domain(String domain);

		/**
		 * Set the cookie path
		 * @param path Cookie path
		 * @return this
		 */
		Builder path(String path);

		/**
		 * Build the cookie instance
		 * @return Cookie instance
		 */
		Cookie build();

	}

}
