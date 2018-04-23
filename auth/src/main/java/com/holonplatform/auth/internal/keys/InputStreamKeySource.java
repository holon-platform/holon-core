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
import java.io.InputStream;

import com.holonplatform.auth.keys.KeySource;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * A {@link InputStream} based {@link KeySource}.
 *
 * @since 5.1.0
 */
public class InputStreamKeySource implements KeySource {

	private final InputStream source;

	/**
	 * Constructor.
	 * @param source The source input stream (not null)
	 */
	public InputStreamKeySource(InputStream source) {
		super();
		ObjectUtils.argumentNotNull(source, "Key source must be not null");
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.keys.KeySource#getBytes()
	 */
	@Override
	public byte[] getBytes() throws IOException {
		return ConversionUtils.convertInputStreamToBytes(source);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InputStreamKeySource [source=" + source + "]";
	}

}
