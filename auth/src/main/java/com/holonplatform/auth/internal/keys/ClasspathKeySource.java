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
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Classpath resource {@link KeySource}.
 *
 * @since 5.1.0
 */
public class ClasspathKeySource implements KeySource {

	private final String source;
	private final ClassLoader classLoader;

	/**
	 * Constructor using the default ClassLoader.
	 * @param source The classpath resource name (not null)
	 */
	public ClasspathKeySource(String source) {
		this(source, ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Constructor.
	 * @param source The classpath resource name (not null)
	 * @param classLoader The ClassLoader to use (not null)
	 */
	public ClasspathKeySource(String source, ClassLoader classLoader) {
		super();
		ObjectUtils.argumentNotNull(source, "Key source must be not null");
		ObjectUtils.argumentNotNull(classLoader, "ClassLoader must be not null");
		this.source = source;
		this.classLoader = classLoader;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.keys.KeySource#getBytes()
	 */
	@Override
	public byte[] getBytes() throws IOException {

		@SuppressWarnings("resource")
		final InputStream is = classLoader.getResourceAsStream(source);
		if (is == null) {
			throw new IOException("Classpath resource [" + source + "] not found in ClassLoader [" + classLoader + "]");
		}

		try {
			return ConversionUtils.convertInputStreamToBytes(is);
		} finally {
			is.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClasspathKeySource [source=" + source + ", classLoader=" + classLoader + "]";
	}

}
