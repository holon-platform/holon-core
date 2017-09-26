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

import java.io.Serializable;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import com.holonplatform.http.HttpHeaders;

/**
 * Utility class for HTTP.
 * 
 * @since 5.0.0
 */
public final class HttpUtils implements Serializable {

	private static final long serialVersionUID = -5876959899993040059L;

	/**
	 * Request header date formats as specified in the HTTP RFC
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.1.1">Section 7.1.1.1 of RFC 7231</a>
	 */
	public static final String[] DATE_FORMATS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz",
			"EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy" };

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private HttpUtils() {
	}

	/**
	 * Check if given URI is "secure", i.e. has HTTPS as scheme
	 * @param uri URI to check
	 * @return <code>true</code> if URI has HTTPS as scheme
	 */
	public static boolean isSecure(URI uri) {
		return uri != null && uri.getScheme().equalsIgnoreCase("https");
	}

	/**
	 * Get a header date value as {@link Date}
	 * @param headerValue Header
	 * @return Date or <code>null</code> if header is null or invalid
	 */
	public static Date parseHeaderDate(String headerValue) {
		if (headerValue != null && headerValue.length() >= 3) {
			for (String dateFormat : DATE_FORMATS) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
				simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
				try {
					return simpleDateFormat.parse(headerValue);
				} catch (@SuppressWarnings("unused") ParseException ex) {
					// ignore
				}
			}
		}
		return null;
	}

	/**
	 * Try to get a list of {@link Locale} from HTTP request from {@link HttpHeaders#ACCEPT_LANGUAGE} header, if not
	 * null. If more than one language is specified in Accept-Language header, returned Locales will be ordered relying
	 * on <i>quality</i> parameter, if specified.
	 * @param header Accept-Language header
	 * @return List of Locale for the languages of the Accept-Language header, if any. If header is not present, an
	 *         empty list is returned.
	 */
	public static List<Locale> getAcceptLanguageLocales(String header) {
		if (header != null && !header.trim().equals("")) {
			List<QualityLocale> qls = new LinkedList<>();
			for (String part : header.split(",")) {
				QualityLocale ql = parseLanguageAndQuality(part);
				if (ql != null) {
					qls.add(ql);
				}
			}
			if (!qls.isEmpty()) {
				List<Locale> locales = new ArrayList<>();
				Collections.sort(qls);
				for (QualityLocale ql : qls) {
					locales.add(ql.locale);
				}
				return locales;
			}
		}
		return Collections.emptyList();
	}

	private static QualityLocale parseLanguageAndQuality(String token) {
		if (token != null && !token.trim().equals("")) {
			String[] splitted = token.trim().replace("-", "_").split(";");
			if (splitted.length > 0) {
				Locale locale = null;
				String[] l = splitted[0].split("_");
				switch (l.length) {
				case 2:
					locale = new Locale(l[0], l[1]);
					break;
				case 3:
					locale = new Locale(l[0], l[1], l[2]);
					break;
				default:
					locale = new Locale(l[0]);
					break;
				}
				double quality = 1.0d;
				for (String s : splitted) {
					s = s.trim();
					if (s.startsWith("q=")) {
						quality = Double.parseDouble(s.substring(2).trim());
						break;
					}
				}
				return new QualityLocale(locale, quality);
			}
		}
		return null;
	}

	private static class QualityLocale implements Comparable<QualityLocale> {

		public final Locale locale;
		public final double quality;

		public QualityLocale(Locale locale, double quality) {
			super();
			this.locale = locale;
			this.quality = quality;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(QualityLocale o) {
			return Double.valueOf(o.quality).compareTo(Double.valueOf(quality));
		}

	}

	/**
	 * Check if given header value is a Bearer authorization header
	 * @param header Header value
	 * @return <code>true</code> if header starts with Bearer prefix
	 */
	public static boolean isBearerAuthorizationHeader(String header) {
		return !HttpUtils.isEmpty(header) && header.toLowerCase().startsWith(HttpHeaders.SCHEME_BEARER.toLowerCase());
	}

	/**
	 * Check if given header value is a Basic authorization header
	 * @param header Header value
	 * @return <code>true</code> if header starts with Basic prefix
	 */
	public static boolean isBasicAuthorizationHeader(String header) {
		return !HttpUtils.isEmpty(header) && header.toLowerCase().startsWith(HttpHeaders.SCHEME_BASIC.toLowerCase());
	}

	/**
	 * Try to extract the value of a Bearer Authorization header
	 * @param header Header value
	 * @return Extracted bearer value, or <code>null</code> if header is null or invalid
	 */
	public static String extractAuthorizationBearer(String header) {
		if (!HttpUtils.isEmpty(header) && header.toLowerCase().startsWith(HttpHeaders.SCHEME_BEARER.toLowerCase())) {
			String value = header.substring(HttpHeaders.SCHEME_BEARER.length()).trim();
			int comma = value.indexOf(',');
			if (comma > 0) {
				value = value.substring(0, comma);
			}
			return value;
		}
		return null;
	}

	/**
	 * Extract credentials from a Basic Authorization header, if valid
	 * @param header Header value
	 * @return A string array with username at index 0 and password at index 1, or null if header is not valid or it is
	 *         not a Basic authorization header
	 */
	public static String[] extractAuthorizationBasicCredentials(String header) {
		if (!HttpUtils.isEmpty(header) && header.toLowerCase().startsWith(HttpHeaders.SCHEME_BASIC.toLowerCase())) {
			String value = header.substring(HttpHeaders.SCHEME_BASIC.length()).trim();
			int comma = value.indexOf(',');
			if (comma > 0) {
				value = value.substring(0, comma);
			}
			if (value != null) {
				String decoded = new String(Base64.getDecoder().decode(value));
				int column = decoded.indexOf(':');
				if (column > 0 && column < (decoded.length() - 1)) {
					return new String[] { decoded.substring(0, column), decoded.substring(column + 1) };
				}
			}
		}
		return null;
	}

	/**
	 * Try to obtain given payload Object as a String multi map
	 * @param payload Payload (may be null)
	 * @return Optional String multi map
	 */
	public static Map<String, List<String>> getAsMultiMap(Object payload) {
		if (payload != null) {
			if (Map.class.isAssignableFrom(payload.getClass())) {
				Map<?, ?> map = (Map<?, ?>) payload;
				Map<String, List<String>> data = new HashMap<>(map.size());
				for (Entry<?, ?> entry : map.entrySet()) {
					if (entry.getValue() != null) {
						List<String> values = new LinkedList<>();
						if (Collection.class.isAssignableFrom(entry.getValue().getClass())) {
							for (Object value : (Collection<?>) entry.getValue()) {
								if (value != null) {
									values.add(value.toString());
								}
							}
						} else {
							values.add(entry.getValue().toString());
						}
						data.put(entry.getKey().toString(), values);
					}
				}
				return data;
			} else {
				throw new IllegalArgumentException("Invalid form data payload: not a Map");
			}
		}
		return null;
	}

	/**
	 * Check if given value is null or empty
	 * @param headerValue Header value
	 * @return <code>true</code> if value is null or empty
	 */
	public static boolean isEmpty(String headerValue) {
		return headerValue == null || "".equals(headerValue);
	}

}
