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
package com.holonplatform.auth.internal.keys;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.holonplatform.auth.keys.KeySource;
import com.holonplatform.auth.keys.KeySource.KetSourceWithCharset;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * A String type {@link KeySource}.
 *
 * @since 5.1.0
 */
public class StringKeySource implements KetSourceWithCharset {

	private final String source;
	private final Charset charset;

	/**
	 * Constructor using the UTF-8 charset.
	 * @param source Key source (not null)
	 */
	public StringKeySource(String source) {
		this(source, StandardCharsets.UTF_8);
	}

	/**
	 * Constructor.
	 * @param source Key source (not null)
	 * @param charset Source charset (not null)
	 */
	public StringKeySource(String source, Charset charset) {
		super();
		ObjectUtils.argumentNotNull(source, "Key source must be not null");
		ObjectUtils.argumentNotNull(charset, "Key source charset must be not null");
		this.source = source;
		this.charset = charset;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.keys.KeySource#getBytes()
	 */
	@Override
	public byte[] getBytes() throws IOException {
		return source.getBytes(charset);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.keys.KeySource.KetSourceWithCharset#getCharset()
	 */
	@Override
	public Optional<Charset> getCharset() {
		return Optional.ofNullable(charset);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StringKeySource [source=" + source + ", charset=" + charset + "]";
	}

}
