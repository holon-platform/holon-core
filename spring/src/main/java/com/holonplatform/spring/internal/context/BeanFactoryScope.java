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
package com.holonplatform.spring.internal.context;

import java.lang.ref.WeakReference;
import java.util.Optional;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.holonplatform.core.ContextScope;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.ContextManager;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * A {@link ContextScope} bound to a Spring {@link BeanFactory} context.
 * 
 * <p>
 * When a context resource is requested, the strategy to provide the a matching Spring bean is defined as follow:
 * <ul>
 * <li>If a Spring bean with a name equal to the context resource key and with the same required type is found, this is
 * returned;</li>
 * <li>Otherwise, if {@link #lookupByType} is <code>true</code> and a Spring bean of the required type, ignoring the
 * name, is present and only one candidate is available, this instance is returned.</li>
 * </ul>
 * 
 * <p>
 * This scope is not manageable, i.e. put/remove/clear operations are not supported.
 * </p>
 * 
 * @since 5.0.0
 */
public class BeanFactoryScope implements ContextScope {

	/**
	 * Scope name
	 */
	public final static String NAME = "spring-context";

	/**
	 * Scope order
	 */
	public final static int ORDER = Integer.MIN_VALUE + 2000;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = SpringLogger.create();

	/**
	 * Spring BeanFactory
	 */
	private final WeakReference<ConfigurableListableBeanFactory> beanFactory;

	/**
	 * BeanFactory beans ClassLoader
	 */
	private final ClassLoader beanFactoryClassLoader;

	/**
	 * Spring bean lookup by type ignoring name
	 */
	private final boolean lookupByType;

	public BeanFactoryScope(ConfigurableListableBeanFactory beanFactory, boolean lookupByType) {
		super();
		ObjectUtils.argumentNotNull(beanFactory, "BeanFactory must be not null");
		this.beanFactory = new WeakReference<>(beanFactory);
		this.beanFactoryClassLoader = beanFactory.getBeanClassLoader();
		this.lookupByType = lookupByType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#getName()
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#getOrder()
	 */
	@Override
	public int getOrder() {
		return ORDER;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> Optional<T> get(String resourceKey, Class<T> resourceType) throws TypeMismatchException {
		return checkBeanFactory().map((bf) -> getResource(resourceKey, resourceType, bf, lookupByType));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> Optional<T> put(String resourceKey, T value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#putIfAbsent(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> Optional<T> putIfAbsent(String resourceKey, T value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.ContextScope#remove(java.lang.String)
	 */
	@Override
	public boolean remove(String resourceKey) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Try to obtain a context resource from bean factory, using resource key as bean name or trying to retrieve the
	 * resource by type.
	 * @param key Resource key
	 * @param type Resource type
	 * @param beanFactory Bean factory
	 * @param lookupByType Enable lookup by type
	 * @return Resource reference, or <code>null</code> if not available
	 */
	private static <T> T getResource(String key, Class<T> type, ConfigurableListableBeanFactory beanFactory,
			boolean lookupByType) {
		ObjectUtils.argumentNotNull(key, "Resource key must be not null");
		ObjectUtils.argumentNotNull(type, "Resource type must be not null");

		T value = null;

		// try by name and type
		try {
			value = beanFactory.getBean(key, type);
		} catch (@SuppressWarnings("unused") NoSuchBeanDefinitionException e) {
			// ignore
		} catch (BeanNotOfRequiredTypeException e) {
			// log and ignore
			LOGGER.debug(() -> "Discard Spring bean with name [" + key + "] because not of required resource type ["
					+ type.getName() + "]", e);
		}
		// propagate any other exception

		// try by type
		if (lookupByType) {
			try {
				value = beanFactory.getBean(type);
			} catch (@SuppressWarnings("unused") NoSuchBeanDefinitionException e) {
				// ignore
			} catch (Exception e) {
				// log and ignore
				LOGGER.debug(() -> "Cannot retrieve a bean of required resource type [" + type.getName()
						+ "] from BeanFactory", e);
			}
		}

		return value;
	}

	/**
	 * Get the {@link BeanFactory} instance, unregistering the scope if the reference is no longer valid
	 * @return BeanFactory, or <code>null</code> if no more available
	 */
	private Optional<ConfigurableListableBeanFactory> checkBeanFactory() {
		ConfigurableListableBeanFactory bf = beanFactory.get();
		if (bf == null) {
			// unregister scope
			ContextManager.unregisterScope(beanFactoryClassLoader, NAME);
			// log
			LOGGER.info("Spring BeanFactory context scope unregistered [ClassLoader: " + beanFactoryClassLoader + "]");
		}
		return Optional.ofNullable(bf);
	}

}
