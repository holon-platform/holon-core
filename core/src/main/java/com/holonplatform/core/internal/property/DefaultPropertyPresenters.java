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
import com.holonplatform.core.property.PropertyValuePresenter;

/**
 * Class to manage default {@link PropertyValuePresenter}s obtained using standard Java extensions loader from
 * <code>MET-INF/services</code>.
 * 
 * @since 5.0.0
 */
@SuppressWarnings("rawtypes")
public final class DefaultPropertyPresenters implements Serializable {

	private static final long serialVersionUID = 5665531723656457977L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = PropertyLogger.create();

	/**
	 * Default presenters by ClassLoader
	 */
	private static final Map<ClassLoader, List<PropertyValuePresenter>> PRESENTERS = new WeakHashMap<>();

	/**
	 * Return the default {@link PropertyValuePresenter}s using given <code>classLoader</code>.
	 * <p>
	 * The default {@link PropertyValuePresenter}s are loaded using fully qualified name of its implementation class
	 * name to a <code>com.holonplatform.core.property.PropertyValuePresenter</code> file in the
	 * <code>META-INF/services</code> directory.
	 * </p>
	 * @param classLoader ClassLoader to use, or <code>null</code> for the default ClassLoader
	 * @return Default PropertyValuePresenters, or an empty List if none
	 */
	public static List<PropertyValuePresenter> getDefaultPresenters(ClassLoader classLoader) {
		return ensureInited((classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Ensure the default PropertyValuePresenter list is inited loading instances from <code>META-INF/services</code>.
	 * @param classLoader ClassLoader to use
	 * @return Default PropertyValuePresenters
	 */
	private static synchronized List<PropertyValuePresenter> ensureInited(final ClassLoader classLoader) {
		if (!PRESENTERS.containsKey(classLoader)) {

			LOGGER.debug(() -> "Load PropertyValuePresenters for classloader [" + classLoader
					+ "] using ServiceLoader with service name: " + PropertyValuePresenter.class.getName());

			final List<PropertyValuePresenter> result = new LinkedList<>();
			// load from META-INF/services
			Iterable<PropertyValuePresenter> presenters = AccessController
					.doPrivileged(new PrivilegedAction<Iterable<PropertyValuePresenter>>() {
						@Override
						public Iterable<PropertyValuePresenter> run() {
							return ServiceLoader.load(PropertyValuePresenter.class, classLoader);
						}
					});
			presenters.forEach(pr -> {
				result.add(pr);

				LOGGER.debug(() -> "Loaded and registered PropertyValuePresenter [" + pr + "] for classloader ["
						+ classLoader + "]");
			});
			PRESENTERS.put(classLoader, result);
		}
		return PRESENTERS.get(classLoader);
	}

	private DefaultPropertyPresenters() {
	}

}
