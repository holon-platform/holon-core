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

import java.util.Optional;

import com.holonplatform.http.CacheControl;

/**
 * Default {@link CacheControl} implementation.
 * 
 * @since 5.0.0
 */
public class CacheControlDirectives implements CacheControl {

	private static final long serialVersionUID = 4275737348247958629L;

	protected long maxAge = -1;

	protected long sMaxAge = -1;

	protected boolean noCache = false;

	protected boolean noStore = false;

	protected boolean noTransform = false;

	protected boolean mustRevalidate = false;

	protected boolean proxyRevalidate = false;

	protected boolean cachePrivate = false;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.CacheControl#isMustRevalidate()
	 */
	@Override
	public boolean isMustRevalidate() {
		return mustRevalidate;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.CacheControl#isProxyRevalidate()
	 */
	@Override
	public boolean isProxyRevalidate() {
		return proxyRevalidate;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.CacheControl#getMaxAge()
	 */
	@Override
	public long getMaxAge() {
		return maxAge;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.CacheControl#getSMaxAge()
	 */
	@Override
	public long getSMaxAge() {
		return sMaxAge;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.CacheControl#isNoCache()
	 */
	@Override
	public boolean isNoCache() {
		return noCache;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.CacheControl#isPrivate()
	 */
	@Override
	public boolean isPrivate() {
		return cachePrivate;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.CacheControl#isNoTransform()
	 */
	@Override
	public boolean isNoTransform() {
		return noTransform;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.CacheControl#isNoStore()
	 */
	@Override
	public boolean isNoStore() {
		return noStore;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.CacheControl#asHeader()
	 */
	@Override
	public Optional<String> asHeader() {
		StringBuilder sb = new StringBuilder();
		if (this.maxAge != -1) {
			appendDirective(sb, "max-age=" + Long.toString(this.maxAge));
		}
		if (this.noCache) {
			appendDirective(sb, "no-cache");
		}
		if (this.noStore) {
			appendDirective(sb, "no-store");
		}
		if (this.mustRevalidate) {
			appendDirective(sb, "must-revalidate");
		}
		if (this.noTransform) {
			appendDirective(sb, "no-transform");
		}
		if (this.cachePrivate) {
			appendDirective(sb, "private");
		}
		if (this.proxyRevalidate) {
			appendDirective(sb, "proxy-revalidate");
		}
		if (this.sMaxAge != -1) {
			appendDirective(sb, "s-maxage=" + Long.toString(this.sMaxAge));
		}

		String value = sb.toString();
		if (value != null && !value.trim().equals("")) {
			return Optional.of(value);
		}
		return Optional.empty();
	}

	private static void appendDirective(StringBuilder builder, String value) {
		if (builder.length() > 0) {
			builder.append(", ");
		}
		builder.append(value);
	}

	public static class CacheControlBuilder implements Builder {

		private final CacheControlDirectives instance = new CacheControlDirectives();

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.CacheControl.Builder#mustRevalidate(boolean)
		 */
		@Override
		public Builder mustRevalidate(boolean mustRevalidate) {
			instance.mustRevalidate = mustRevalidate;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.CacheControl.Builder#proxyRevalidate(boolean)
		 */
		@Override
		public Builder proxyRevalidate(boolean proxyRevalidate) {
			instance.proxyRevalidate = proxyRevalidate;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.CacheControl.Builder#maxAge(long)
		 */
		@Override
		public Builder maxAge(long maxAge) {
			instance.maxAge = maxAge;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.CacheControl.Builder#sMaxAge(long)
		 */
		@Override
		public Builder sMaxAge(long sMaxAge) {
			instance.sMaxAge = sMaxAge;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.CacheControl.Builder#noCache(boolean)
		 */
		@Override
		public Builder noCache(boolean noCache) {
			instance.noCache = noCache;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.CacheControl.Builder#setPrivate(boolean)
		 */
		@Override
		public Builder setPrivate(boolean isprivate) {
			instance.cachePrivate = isprivate;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.CacheControl.Builder#noTransform(boolean)
		 */
		@Override
		public Builder noTransform(boolean noTransform) {
			instance.noTransform = noTransform;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.CacheControl.Builder#noStore(boolean)
		 */
		@Override
		public Builder noStore(boolean noStore) {
			instance.noStore = noStore;
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.CacheControl.Builder#build()
		 */
		@Override
		public CacheControl build() {
			return instance;
		}

	}

}
