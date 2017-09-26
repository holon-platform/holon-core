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

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.http.HttpRequest;

/**
 * {@link HttpRequest} base class.
 * 
 * @since 5.0.0
 */
public abstract class AbstractHttpRequest implements HttpRequest {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.messaging.Message#getPayloadType()
	 */
	@Override
	public Class<? extends String> getPayloadType() throws UnsupportedOperationException {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.messaging.Message#getPayload()
	 */
	@SuppressWarnings("resource")
	@Override
	public Optional<String> getPayload() throws UnsupportedOperationException {
		try {
			InputStream is = getBody();
			if (is != null) {
				byte[] bytes = ConversionUtils.convertInputStreamToBytes(is);
				if (bytes != null && bytes.length > 0) {
					String charset = getCharset();
					String cs = (charset != null) ? charset.toUpperCase() : "UTF-8";
					return Optional.of(new String(bytes, cs));
				}
			}
			return Optional.empty();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Try to get request charset using {@link CONTENT_TYPE} header
	 * @return Request charset, or <code>null</code> if unknown
	 */
	protected String getCharset() {
		String ct = getHeaderValue(CONTENT_TYPE).orElse(null);
		if (ct != null && !ct.trim().equals("")) {
			String[] values = null;
			if (ct.contains(";")) {
				values = ct.split(";");
			} else {
				values = new String[] { ct };
			}
			if (values != null) {
				for (String value : values) {
					String v = (value != null) ? value.trim() : null;
					if (v != null && v.startsWith("charset=") && v.length() > 8) {
						return v.substring(8);
					}
				}
			}
		}
		return null;
	}

}
