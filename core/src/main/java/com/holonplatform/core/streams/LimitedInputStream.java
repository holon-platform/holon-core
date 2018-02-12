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
package com.holonplatform.core.streams;

import java.io.InputStream;

import com.holonplatform.core.internal.streams.DefaultLimitedInputStream;

/**
 * Represents a <em>limited</em> {@link InputStream}, providing the stream length in bytes.
 * 
 * @since 5.1.0
 */
public abstract class LimitedInputStream extends InputStream {

	/**
	 * Get the stream length.
	 * @return the stream length in bytes
	 */
	public abstract long getLength();

	/**
	 * Get the actual {@link InputStream} if acts as a wrapper.
	 * @return the actual {@link InputStream}, maybe the instance itself
	 */
	public abstract InputStream getActualStream();

	/**
	 * Create a new {@link LimitedInputStream}.
	 * @param stream The input stream (not null)
	 * @param length The stream length in bytes
	 * @return A new {@link LimitedInputStream} instance
	 */
	public static LimitedInputStream create(InputStream stream, long length) {
		return new DefaultLimitedInputStream(stream, length);
	}

}
