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
package com.holonplatform.core.internal.property;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.property.PropertyRenderer;

/**
 * Class to manage default {@link PropertyRenderer}s obtained using standard Java extensions loader from
 * <code>MET-INF/services</code>.
 * 
 * @since 5.0.0
 */
@SuppressWarnings("rawtypes")
public final class DefaultPropertyRenderers implements Serializable {

	private static final long serialVersionUID = -5663675269204770576L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = PropertyLogger.create();

	/**
	 * Default renderers by ClassLoader
	 */
	private static final Map<ClassLoader, List<PropertyRenderer>> RENDERERS = new WeakHashMap<>();

	/**
	 * Return the default {@link PropertyRenderer}s using given <code>classLoader</code>.
	 * <p>
	 * The default {@link PropertyRenderer}s are loaded using fully qualified name of its implementation class name to a
	 * <code>com.holonplatform.core.property.PropertyRenderer</code> file in the <code>META-INF/services</code>
	 * directory.
	 * </p>
	 * @param classLoader ClassLoader to use, or <code>null</code> for the default ClassLoader
	 * @return Default PropertyRenderers, or an empty List if none
	 */
	public static List<PropertyRenderer> getDefaultRenderers(ClassLoader classLoader) {
		return ensureInited((classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Ensure the default PropertyRenderer list is inited loading instances from <code>META-INF/services</code>.
	 * @param classLoader ClassLoader to use
	 * @return Default PropertyRenderers
	 */
	private static synchronized List<PropertyRenderer> ensureInited(final ClassLoader classLoader) {
		if (!RENDERERS.containsKey(classLoader)) {

			LOGGER.debug(() -> "Load PropertyRenderers for classloader [" + classLoader
					+ "] using ServiceLoader with service name: " + PropertyRenderer.class.getName());

			final List<PropertyRenderer> result = new LinkedList<>();
			// load from META-INF/services
			Iterable<PropertyRenderer> renderers = AccessController
					.doPrivileged(new PrivilegedAction<Iterable<PropertyRenderer>>() {
						@Override
						public Iterable<PropertyRenderer> run() {
							return ServiceLoader.load(PropertyRenderer.class, classLoader);
						}
					});
			renderers.forEach(pr -> {
				result.add(pr);

				LOGGER.debug(() -> "Loaded and registered PropertyRenderer [" + pr + "] for classloader [" + classLoader
						+ "]");
			});
			RENDERERS.put(classLoader, result);
		}
		return RENDERERS.get(classLoader);
	}

	private DefaultPropertyRenderers() {
	}

}
