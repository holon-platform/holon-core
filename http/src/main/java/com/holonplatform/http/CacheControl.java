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
import java.util.Optional;

import com.holonplatform.http.internal.CacheControlDirectives;

/**
 * Representation of a HTTP Cache-Control header.
 * 
 * @since 5.0.0
 */
public interface CacheControl extends Serializable {

	/**
	 * Get the must-revalidate cache control directive.
	 * @return true if the must-revalidate cache control directive will be included in header
	 */
	boolean isMustRevalidate();

	/**
	 * Get the proxy-revalidate cache control directive.
	 * @return true if the proxy-revalidate cache control directive will be included in header
	 */
	boolean isProxyRevalidate();

	/**
	 * Get the max-age cache control directive.
	 * @return the value of the max-age cache control directive, -1 if the directive is disabled
	 */
	long getMaxAge();

	/**
	 * Get the s-maxage cache control directive.
	 * @return the value of the s-maxage cache control directive, -1 if the directive is disabled
	 */
	long getSMaxAge();

	/**
	 * Get the no-cache cache control directive.
	 * @return true if the no-cache cache control directive will be included in header
	 */
	boolean isNoCache();

	/**
	 * Get the private cache control directive.
	 * @return true if the private cache control directive will be included in header
	 */
	boolean isPrivate();

	/**
	 * Get the no-transform cache control directive.
	 * @return true if the no-transform cache control directive will be included in header
	 */
	boolean isNoTransform();

	/**
	 * Get the no-store cache control directive.
	 * @return true if the no-store cache control directive will be included in header
	 */
	boolean isNoStore();

	/**
	 * Returns the cache-control directives as HTTP header value
	 * @return Cache-control directives HTTP header value
	 */
	Optional<String> asHeader();

	/**
	 * Builder to create a {@link CacheControl} instance
	 * @return CacheControl builder
	 */
	static Builder builder() {
		return new CacheControlDirectives.CacheControlBuilder();
	}

	/**
	 * {@link CacheControl} builder.
	 */
	public interface Builder {

		/**
		 * Set the must-revalidate cache control directive.
		 * @param mustRevalidate The directive to set
		 * @return this
		 */
		Builder mustRevalidate(boolean mustRevalidate);

		/**
		 * Set the proxy-revalidate cache control directive.
		 * @param proxyRevalidate The directive to set
		 * @return this
		 */
		Builder proxyRevalidate(boolean proxyRevalidate);

		/**
		 * Set the max-age cache control directive.
		 * @param maxAge The directive to set
		 * @return this
		 */
		Builder maxAge(long maxAge);

		/**
		 * Set the s-maxage cache control directive.
		 * @param sMaxAge The directive to set
		 * @return this
		 */
		Builder sMaxAge(long sMaxAge);

		/**
		 * Set the no-cache cache control directive.
		 * @param noCache The directive to set
		 * @return this
		 */
		Builder noCache(boolean noCache);

		/**
		 * Set the private cache control directive.
		 * @param isprivate The directive to set
		 * @return this
		 */
		Builder setPrivate(boolean isprivate);

		/**
		 * Set the no-transform cache control directive.
		 * @param noTransform The directive to set
		 * @return this
		 */
		Builder noTransform(boolean noTransform);

		/**
		 * Set the no-store cache control directive.
		 * @param noStore The directive to set
		 * @return this
		 */
		Builder noStore(boolean noStore);

		/**
		 * Build the {@link CacheControl} instance
		 * @return CacheControl
		 */
		CacheControl build();

	}

}
