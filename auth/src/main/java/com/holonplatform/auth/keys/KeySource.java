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
package com.holonplatform.auth.keys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import com.holonplatform.auth.internal.keys.ByteKeySource;
import com.holonplatform.auth.internal.keys.ClasspathKeySource;
import com.holonplatform.auth.internal.keys.FileKeySource;
import com.holonplatform.auth.internal.keys.InputStreamKeySource;
import com.holonplatform.auth.internal.keys.StringKeySource;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Security key source.
 * <p>
 * A key source can be created from different source types and formats using the provided static methods, such as
 * {@link #string(String)}, {@link #stream(InputStream)} or {@link #file(File)}.
 * </p>
 *
 * @since 5.1.0
 */
public interface KeySource {

	/**
	 * Get the key as a byte array.
	 * @return Key bytes
	 * @throws IOException If an error occurred
	 */
	byte[] getBytes() throws IOException;

	/**
	 * Create a key source from an array of bytes.
	 * @param source Key source (not null)
	 * @return A new {@link KeySource}
	 */
	static KeySource bytes(byte[] source) {
		return new ByteKeySource(source);
	}

	/**
	 * Create a key source from a {@link String}. The <code>ISO-LATIN-1</code> (<code>ISO-8859-1</code>) charset is used
	 * by default.
	 * @param source Key source (not null)
	 * @return A new {@link KeySource}
	 */
	static KeySource string(String source) {
		return new StringKeySource(source);
	}

	/**
	 * Create a key source from a {@link String} using given charset.
	 * @param source Key source (not null)
	 * @param charset The charset with which the source was encoded (not null)
	 * @return A new {@link KeySource}
	 */
	static KeySource string(String source, Charset charset) {
		return new StringKeySource(source, charset);
	}

	/**
	 * Create a key source from a {@link String} using given charset name.
	 * @param source Key source (not null)
	 * @param charsetName The charset name with which the source was encoded (not null)
	 * @return A new {@link KeySource}
	 * @throws UnsupportedCharsetException If given charset name is not supported by the JVM
	 */
	static KeySource string(String source, String charsetName) {
		ObjectUtils.argumentNotNull(charsetName, "Charset name must be not null");
		return new StringKeySource(source, Charset.forName(charsetName));
	}

	/**
	 * Create a key source from a {@link InputStream}.
	 * @param source Key source (not null)
	 * @return A new {@link KeySource}
	 */
	static KeySource stream(InputStream source) {
		return new InputStreamKeySource(source);
	}

	/**
	 * Create a key source using a classpath resource. The resource lookup strategy is the same as
	 * {@link ClassLoader#getResource(String)}.
	 * @param name Key resource name (not null)
	 * @param classLoader The ClassLoader to use (not null)
	 * @return A new {@link KeySource}
	 */
	static KeySource resource(String name, ClassLoader classLoader) {
		return new ClasspathKeySource(name, classLoader);
	}

	/**
	 * Create a key source using a classpath resource using the default ClassLoader. The resource lookup strategy is the
	 * same as {@link ClassLoader#getResource(String)}.
	 * @param name Key resource name (not null)
	 * @return A new {@link KeySource}
	 */
	static KeySource resource(String name) {
		return new ClasspathKeySource(name);
	}

	/**
	 * Create a key source from a {@link File}.
	 * @param source Key source (not null)
	 * @return A new {@link KeySource}
	 */
	static KeySource file(File source) {
		return new FileKeySource(source);
	}

	/**
	 * Create a key source from a file with given file name.
	 * @param fileName Key source file name (not null)
	 * @return A new {@link KeySource}
	 */
	static KeySource file(String fileName) {
		return new FileKeySource(new File(fileName));
	}

}
