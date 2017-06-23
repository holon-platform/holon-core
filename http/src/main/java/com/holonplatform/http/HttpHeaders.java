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

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.messaging.MessageHeaders;
import com.holonplatform.http.internal.HttpUtils;

/**
 * HTTP headers representation.
 * 
 * @since 5.0.0
 */
public interface HttpHeaders extends MessageHeaders<List<String>> {

	/**
	 * The HTTP <code>Accept</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.3.2">Section 5.3.2 of RFC 7231</a>
	 */
	String ACCEPT = "Accept";
	/**
	 * The HTTP <code>Accept-Charset</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.3.3">Section 5.3.3 of RFC 7231</a>
	 */
	String ACCEPT_CHARSET = "Accept-Charset";
	/**
	 * The HTTP <code>Accept-Encoding</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.3.4">Section 5.3.4 of RFC 7231</a>
	 */
	String ACCEPT_ENCODING = "Accept-Encoding";
	/**
	 * The HTTP <code>Accept-Language</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.3.5">Section 5.3.5 of RFC 7231</a>
	 */
	String ACCEPT_LANGUAGE = "Accept-Language";
	/**
	 * The HTTP <code>Accept-Ranges</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7233#section-2.3">Section 5.3.5 of RFC 7233</a>
	 */
	String ACCEPT_RANGES = "Accept-Ranges";

	/**
	 * The HTTP <code>Age</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7234#section-5.1">Section 5.1 of RFC 7234</a>
	 */
	String AGE = "Age";
	/**
	 * The HTTP <code>Allow</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.4.1">Section 7.4.1 of RFC 7231</a>
	 */
	String ALLOW = "Allow";

	/**
	 * The HTTP <code>Authorization</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7235#section-4.2">Section 4.2 of RFC 7235</a>
	 */
	String AUTHORIZATION = "Authorization";

	/**
	 * The HTTP <code>Cache-Control</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7234#section-5.2">Section 5.2 of RFC 7234</a>
	 */
	String CACHE_CONTROL = "Cache-Control";
	/**
	 * The HTTP <code>Connection</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-6.1">Section 6.1 of RFC 7230</a>
	 */
	String CONNECTION = "Connection";
	/**
	 * The HTTP <code>Content-Encoding</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-3.1.2.2">Section 3.1.2.2 of RFC 7231</a>
	 */
	String CONTENT_ENCODING = "Content-Encoding";
	/**
	 * The HTTP <code>Content-Disposition</code> header field name
	 * @see <a href="http://tools.ietf.org/html/rfc6266">RFC 6266</a>
	 */
	String CONTENT_DISPOSITION = "Content-Disposition";
	/**
	 * The HTTP <code>Content-Language</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-3.1.3.2">Section 3.1.3.2 of RFC 7231</a>
	 */
	String CONTENT_LANGUAGE = "Content-Language";
	/**
	 * The HTTP <code>Content-Length</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-3.3.2">Section 3.3.2 of RFC 7230</a>
	 */
	String CONTENT_LENGTH = "Content-Length";
	/**
	 * The HTTP <code>Content-Location</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-3.1.4.2">Section 3.1.4.2 of RFC 7231</a>
	 */
	String CONTENT_LOCATION = "Content-Location";
	/**
	 * The HTTP <code>Content-Range</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7233#section-4.2">Section 4.2 of RFC 7233</a>
	 */
	String CONTENT_RANGE = "Content-Range";
	/**
	 * The HTTP <code>Content-Type</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-3.1.1.5">Section 3.1.1.5 of RFC 7231</a>
	 */
	String CONTENT_TYPE = "Content-Type";
	/**
	 * The HTTP <code>Cookie</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc2109#section-4.3.4">Section 4.3.4 of RFC 2109</a>
	 */
	String COOKIE = "Cookie";
	/**
	 * The HTTP <code>Date</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.1.1.2">Section 7.1.1.2 of RFC 7231</a>
	 */
	String DATE = "Date";
	/**
	 * The HTTP <code>ETag</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-2.3">Section 2.3 of RFC 7232</a>
	 */
	String ETAG = "ETag";
	/**
	 * The HTTP <code>Expect</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.1.1">Section 5.1.1 of RFC 7231</a>
	 */
	String EXPECT = "Expect";
	/**
	 * The HTTP <code>Expires</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7234#section-5.3">Section 5.3 of RFC 7234</a>
	 */
	String EXPIRES = "Expires";
	/**
	 * The HTTP <code>From</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.5.1">Section 5.5.1 of RFC 7231</a>
	 */
	String FROM = "From";
	/**
	 * The HTTP <code>Host</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-5.4">Section 5.4 of RFC 7230</a>
	 */
	String HOST = "Host";
	/**
	 * The HTTP <code>If-Match</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-3.1">Section 3.1 of RFC 7232</a>
	 */
	String IF_MATCH = "If-Match";
	/**
	 * The HTTP <code>If-Modified-Since</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-3.3">Section 3.3 of RFC 7232</a>
	 */
	String IF_MODIFIED_SINCE = "If-Modified-Since";
	/**
	 * The HTTP <code>If-None-Match</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-3.2">Section 3.2 of RFC 7232</a>
	 */
	String IF_NONE_MATCH = "If-None-Match";
	/**
	 * The HTTP <code>If-Range</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7233#section-3.2">Section 3.2 of RFC 7233</a>
	 */
	String IF_RANGE = "If-Range";
	/**
	 * The HTTP <code>If-Unmodified-Since</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-3.4">Section 3.4 of RFC 7232</a>
	 */
	String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
	/**
	 * The HTTP <code>Last-Modified</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-2.2">Section 2.2 of RFC 7232</a>
	 */
	String LAST_MODIFIED = "Last-Modified";
	/**
	 * The HTTP <code>Link</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc5988">RFC 5988</a>
	 */
	String LINK = "Link";
	/**
	 * The HTTP <code>Location</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.1.2">Section 7.1.2 of RFC 7231</a>
	 */
	String LOCATION = "Location";
	/**
	 * The HTTP <code>Max-Forwards</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.1.2">Section 5.1.2 of RFC 7231</a>
	 */
	String MAX_FORWARDS = "Max-Forwards";
	/**
	 * The HTTP <code>Origin</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc6454">RFC 6454</a>
	 */
	String ORIGIN = "Origin";
	/**
	 * The HTTP <code>Pragma</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7234#section-5.4">Section 5.4 of RFC 7234</a>
	 */
	String PRAGMA = "Pragma";
	/**
	 * The HTTP <code>Proxy-Authenticate</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7235#section-4.3">Section 4.3 of RFC 7235</a>
	 */
	String PROXY_AUTHENTICATE = "Proxy-Authenticate";
	/**
	 * The HTTP <code>Proxy-Authorization</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7235#section-4.4">Section 4.4 of RFC 7235</a>
	 */
	String PROXY_AUTHORIZATION = "Proxy-Authorization";
	/**
	 * The HTTP <code>Range</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7233#section-3.1">Section 3.1 of RFC 7233</a>
	 */
	String RANGE = "Range";
	/**
	 * The HTTP <code>Referer</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.5.2">Section 5.5.2 of RFC 7231</a>
	 */
	String REFERER = "Referer";
	/**
	 * The HTTP <code>Retry-After</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.1.3">Section 7.1.3 of RFC 7231</a>
	 */
	String RETRY_AFTER = "Retry-After";
	/**
	 * The HTTP <code>Server</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.4.2">Section 7.4.2 of RFC 7231</a>
	 */
	String SERVER = "Server";
	/**
	 * The HTTP <code>Set-Cookie</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc2109#section-4.2.2">Section 4.2.2 of RFC 2109</a>
	 */
	String SET_COOKIE = "Set-Cookie";

	/**
	 * The HTTP <code>Trailer</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-4.4">Section 4.4 of RFC 7230</a>
	 */
	String TRAILER = "Trailer";
	/**
	 * The HTTP <code>Transfer-Encoding</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-3.3.1">Section 3.3.1 of RFC 7230</a>
	 */
	String TRANSFER_ENCODING = "Transfer-Encoding";
	/**
	 * The HTTP <code>Upgrade</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-6.7">Section 6.7 of RFC 7230</a>
	 */
	String UPGRADE = "Upgrade";
	/**
	 * The HTTP <code>User-Agent</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.5.3">Section 5.5.3 of RFC 7231</a>
	 */
	String USER_AGENT = "User-Agent";
	/**
	 * The HTTP <code>Vary</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.1.4">Section 7.1.4 of RFC 7231</a>
	 */
	String VARY = "Vary";
	/**
	 * The HTTP <code>Via</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-5.7.1">Section 5.7.1 of RFC 7230</a>
	 */
	String VIA = "Via";
	/**
	 * The HTTP <code>Warning</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7234#section-5.5">Section 5.5 of RFC 7234</a>
	 */
	String WARNING = "Warning";
	/**
	 * The HTTP <code>WWW-Authenticate</code> header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7235#section-4.1">Section 4.1 of RFC 7235</a>
	 */
	String WWW_AUTHENTICATE = "WWW-Authenticate";

	// Authentication schemes

	/**
	 * Basic authentication scheme
	 */
	String SCHEME_BASIC = "Basic";

	/**
	 * Bearer authentication scheme
	 */
	String SCHEME_BEARER = "Bearer";

	/**
	 * Digest authentication scheme
	 */
	String SCHEME_DIGEST = "Digest";

	/**
	 * Get a HTTP header as a single string value
	 * @param name The header name (not null)
	 * @return The HTTP header value. If the HTTP header is present more than once then the values of joined together
	 *         and separated by a ',' character.
	 */
	default Optional<String> getHeaderValue(String name) {
		ObjectUtils.argumentNotNull(name, "Header name must be not null");
		return getHeader(name).filter(l -> l != null && !l.isEmpty()).map(l -> l.get(0));
	}

	/**
	 * Return the date and time at which the message was created, as specified by the {@link #DATE} header.
	 * @return Message date/time, if available
	 */
	default Optional<Date> getDate() {
		return getHeaderValue(DATE).map(d -> HttpUtils.parseHeaderDate(d));
	}

	/**
	 * Return the length of the request body in bytes, as specified by the {@link #CONTENT_LENGTH} header.
	 * @return Content length in bytes, if available
	 */
	default Optional<Long> getContentLength() {
		return getHeaderValue(CONTENT_LENGTH).map(v -> Long.parseLong(v));
	}

	/**
	 * Return the resource location as specified by the {@link #LOCATION} header.
	 * @return location URI, if available
	 */
	default Optional<URI> getLocation() {
		return getHeaderValue(LOCATION).map(v -> URI.create(v));
	}

	/**
	 * Try to get a list of {@link Locale} using {@link #ACCEPT_LANGUAGE} header, if present. If more than one language
	 * is specified in Accept-Language header, returned Locales will be ordered relying on <i>quality</i> parameter, if
	 * specified.
	 * @return List of Locale for the languages of the Accept-Language header, if any. If header is not present, an
	 *         empty list is returned.
	 */
	default List<Locale> getLocales() {
		return HttpUtils.getAcceptLanguageLocales(getHeaderValue(ACCEPT_LANGUAGE).orElse(null));
	}

	/**
	 * Get the first (most qualified) {@link Locale} using {@link #ACCEPT_LANGUAGE} header, if present.
	 * @return Request locale
	 */
	default Optional<Locale> getLocale() {
		final List<Locale> locales = getLocales();
		return (!locales.isEmpty()) ? Optional.ofNullable(locales.get(0)) : Optional.empty();
	}

	/**
	 * Return the authorization bearer token from {@link #AUTHORIZATION} header, if present and of scheme type
	 * {@link #SCHEME_BEARER}.
	 * @return Authorization bearer token, if available
	 */
	default Optional<String> getAuthorizationBearer() {
		return getHeaderValue(AUTHORIZATION).map(v -> HttpUtils.extractAuthorizationBearer(v));
	}

	/**
	 * Return the authorization bearer token from {@link #AUTHORIZATION} header, if present and of scheme type
	 * {@link #SCHEME_BASIC}.
	 * @return Authorization credentials, if available
	 */
	default Optional<String[]> getAuthorizationBasicCredentials() {
		return getHeaderValue(AUTHORIZATION).map(v -> HttpUtils.extractAuthorizationBasicCredentials(v));
	}

}
