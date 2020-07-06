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
package com.holonplatform.core.internal.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.holonplatform.core.i18n.Localizable.LocalizationException;
import com.holonplatform.core.i18n.MessageProvider;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * A {@link MessageProvider} which uses properties files to store localized messages for a Locale.
 * 
 * <p>
 * Properties files are resolved using configured basenames as prefix, and {@link Locale} language, country and variant
 * separated by an underscore as file name. Files must have <code>.properties</code> extension.
 * </p>
 * <p>
 * The basenames follow {@link java.util.ResourceBundle} conventions: essentially, a fully-qualified classpath location.
 * If it doesn't contain a package qualifier, it will be resolved from the classpath root. Note that the JDK's standard
 * ResourceBundle treats dots as package separators: This means that "test.messages" is equivalent to "test/messages".
 * </p>
 * 
 * @since 5.0.0
 */
public class PropertiesMessageProvider implements MessageProvider {

	private static final long serialVersionUID = -3481249503207246736L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = I18nLogger.create();

	/**
	 * Default property file extension
	 */
	private static final String PROPERTIES_SUFFIX = ".properties";

	/*
	 * ClassLoader
	 */
	private transient ClassLoader resourceClassLoader;

	/*
	 * Basenames for properties files
	 */
	private final List<String> basenames = new LinkedList<>();
	/*
	 * Properties file encoding charset
	 */
	private String fileEncoding;

	/*
	 * Locale filenames list cache by basename
	 */
	private final ConcurrentMap<String, Map<Locale, List<String>>> filenamesCache = new ConcurrentHashMap<>();

	/*
	 * Loaded Properties cache
	 */
	private final ConcurrentMap<String, Properties> propertiesCache = new ConcurrentHashMap<>();

	/**
	 * Set basenames for resource locations. Any previous basename will be overridden.
	 * <p>
	 * The associated resource files will be checked sequentially when resolving a message code.
	 * </p>
	 * @param basenames The basenames to set
	 */
	public void setBasenames(String... basenames) {
		this.basenames.clear();
		addBasenames(basenames);
	}

	/**
	 * Add basenames for resource files location.
	 * @param basenames The basenames to add
	 */
	public void addBasenames(String... basenames) {
		if (basenames != null) {
			for (String basename : basenames) {
				if (!this.basenames.contains(basename)) {
					this.basenames.add(basename);
				}
			}
		}
	}

	/**
	 * Set the charset to use for parsing properties files.
	 * <p>
	 * Default is none, using the {@code java.util.Properties} default encoding: ISO-8859-1.
	 * </p>
	 * @param fileEncoding Properties file encoding to set
	 */
	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	/**
	 * Get the charset to use for parsing properties files.
	 * @return Properties files encoding, or <code>null</code> for default (ISO-8859-1).
	 */
	protected String getFileEncoding() {
		return fileEncoding;
	}

	/**
	 * Basenames for resource locations
	 * @return Resource basenames
	 */
	protected List<String> getBasenames() {
		return basenames;
	}

	/**
	 * ClassLoader to load properties files
	 * @return ClassLoader
	 */
	protected ClassLoader getResourceClassLoader() {
		return resourceClassLoader;
	}

	/**
	 * Set the ClassLoader to use to load properties files
	 * @param resourceClassLoader the ClassLoader to set
	 */
	public void setResourceClassLoader(ClassLoader resourceClassLoader) {
		this.resourceClassLoader = resourceClassLoader;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.MessageProvider#getMessage(java.util.Locale, java.lang.String)
	 */
	@Override
	public Optional<String> getMessage(Locale locale, String code) throws LocalizationException {

		ObjectUtils.argumentNotNull(locale, "Locale must be not null");

		if (code != null) {

			LOGGER.debug(() -> "PropertiesMessageProvider: get message with code [" + code + "] for Locale [" + locale
					+ "]");

			for (String basename : getBasenames()) {
				List<String> filenames = getFilenames(basename, locale);
				for (String filename : filenames) {
					try {
						Properties properties = getProperties(filename);
						if (properties != null) {
							String value = properties.getProperty(code);
							if (value != null) {

								LOGGER.debug(() -> "PropertiesMessageProvider: message with code [" + code
										+ "] for Locale [" + locale + "] found: [" + value + "]");

								return Optional.of(value);
							}
						}
					} catch (IOException e) {
						throw new LocalizationException(e);
					}
				}
			}

			LOGGER.debug(() -> "PropertiesMessageProvider: message with code [" + code + "] for Locale [" + locale
					+ "] was not found. Basenames: [" + basenames + "]");

		}
		return Optional.empty();
	}

	/**
	 * Get filenames for the given basename and Locale.
	 * @param basename the basename for the resource
	 * @param locale the locale
	 * @return the List of filenames to check
	 */
	protected List<String> getFilenames(String basename, Locale locale) {
		// check cache
		Map<Locale, List<String>> lm = this.filenamesCache.get(basename);
		if (lm != null) {
			List<String> filenames = lm.get(locale);
			if (filenames != null) {
				return filenames;
			}
		}

		// get file names
		List<String> filenames = new ArrayList<>(4);
		filenames.addAll(getLocaleFilenames(basename, locale));
		filenames.add(basename);

		// put in cache
		if (lm == null) {
			lm = new ConcurrentHashMap<>();
			Map<Locale, List<String>> existing = this.filenamesCache.putIfAbsent(basename, lm);
			if (existing != null) {
				lm = existing;
			}
		}
		lm.put(locale, filenames);

		// done
		return filenames;
	}

	/**
	 * Get the filenames for the given basename and Locale, appending language code, country code, and variant code.
	 * E.g.: basename "messages", Locale "it_IT_db" -&gt; "messages_it_IT_db", "messages_it_IT", "messages_it".
	 * @param basename the basename
	 * @param locale the locale
	 * @return the List of filenames to check
	 */
	protected List<String> getLocaleFilenames(String basename, Locale locale) {
		List<String> result = new ArrayList<>(3);
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		StringBuilder sb = new StringBuilder(basename);
		sb.append('_');
		if (language.length() > 0) {
			sb.append(language);
			result.add(0, sb.toString());
		}
		sb.append('_');
		if (country.length() > 0) {
			sb.append(country);
			result.add(0, sb.toString());
		}
		if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
			sb.append('_').append(variant);
			result.add(0, sb.toString());
		}

		LOGGER.debug(() -> "PropertiesMessageProvider: file names for basename [" + basename + "] and Locale [" + locale
				+ "]: [" + result + "]");

		return result;
	}

	/**
	 * Get a Properties instance for the given filename, either from the cache or freshly loaded.
	 * @param filename Filename
	 * @return Properties for filename
	 * @throws IOException Error reading file
	 */
	protected Properties getProperties(String filename) throws IOException {
		if (filename != null) {
			// check cache
			Properties properties = this.propertiesCache.get(filename);
			if (properties != null) {
				return properties;
			}
			// load properties
			properties = loadProperties(filename + PROPERTIES_SUFFIX);
			Properties existing = propertiesCache.putIfAbsent(filename, properties);
			if (existing != null) {
				properties = existing;
			}
			return properties;
		}
		return null;
	}

	/**
	 * Load a {@link Properties} instance from given filename
	 * @param filename File name
	 * @return Properties from file, if file was not found Properties instance will be empty
	 * @throws IOException Error loading properties
	 */
	protected Properties loadProperties(String filename) throws IOException {
		if (filename == null) {
			throw new IOException("Null filename");
		}

		ClassLoader cl = (getResourceClassLoader() != null) ? getResourceClassLoader()
				: ClassUtils.getDefaultClassLoader();

		try (InputStream is = cl.getResourceAsStream(filename)) {
			Properties properties = new Properties();
			if (is != null) {
				if (getFileEncoding() != null) {
					properties.load(new InputStreamReader(is, getFileEncoding()));
				} else {
					properties.load(is);
				}
			}
			return properties;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PropertiesMessageProvider [basenames=" + basenames + ", fileEncoding=" + fileEncoding + "]";
	}

	// Builder

	/**
	 * Default {@link PropertiesMessageProviderBuilder} implementation.
	 */
	public static class Builder implements PropertiesMessageProviderBuilder {

		private final PropertiesMessageProvider instance = new PropertiesMessageProvider();

		/**
		 * Constructor
		 * @param basenames Basenames for resource locations
		 */
		public Builder(String... basenames) {
			super();
			instance.setBasenames(basenames);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.i18n.PropertiesMessageProviderBuilder#basename(java.lang.String)
		 */
		@Override
		public PropertiesMessageProviderBuilder basename(String basename) {
			instance.addBasenames(basename);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.i18n.PropertiesMessageProviderBuilder#encoding(java.lang.String)
		 */
		@Override
		public PropertiesMessageProviderBuilder encoding(String encoding) {
			instance.setFileEncoding(encoding);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.i18n.PropertiesMessageProviderBuilder#classLoader(java.lang.ClassLoader)
		 */
		@Override
		public PropertiesMessageProviderBuilder classLoader(ClassLoader classLoader) {
			instance.setResourceClassLoader(classLoader);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.i18n.PropertiesMessageProviderBuilder#build()
		 */
		@Override
		public MessageProvider build() {
			return instance;
		}

	}

}
