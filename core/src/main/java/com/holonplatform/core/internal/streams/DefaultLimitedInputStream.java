/*
 * Copyright 2016-2018 Axioma srl.
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
package com.holonplatform.core.internal.streams;

import java.io.IOException;
import java.io.InputStream;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.streams.LimitedInputStream;

/**
 * Default {@link LimitedInputStream} implementation.
 *
 * @since 5.1.0
 */
public class DefaultLimitedInputStream extends LimitedInputStream {

	private final InputStream stream;
	private final long length;

	/**
	 * Constructor.
	 * @param stream The actual stream (not null)
	 * @param length The stream length in bytes
	 */
	public DefaultLimitedInputStream(InputStream stream, long length) {
		super();
		ObjectUtils.argumentNotNull(stream, "Stream must be not null");
		this.stream = stream;
		this.length = length;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.streams.LimitedInputStream#getLength()
	 */
	@Override
	public long getLength() {
		return length;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.streams.LimitedInputStream#getActualStream()
	 */
	@Override
	public InputStream getActualStream() {
		return stream;
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		return stream.read();
	}

}
