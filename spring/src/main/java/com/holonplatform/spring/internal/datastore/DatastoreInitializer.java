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
package com.holonplatform.spring.internal.datastore;

import java.io.Serializable;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.ListableBeanFactory;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.DatastoreCommodityFactory;
import com.holonplatform.core.datastore.DatastoreCommodityRegistrar;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.AnnotationUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.spring.DatastorePostProcessor;
import com.holonplatform.spring.DatastoreResolver;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * Utility class to initialize a {@link Datastore} in the Spring context.
 *
 * @since 5.0.0
 */
public final class DatastoreInitializer implements Serializable {

	private static final long serialVersionUID = -7904316986702168964L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = SpringLogger.create();

	private DatastoreInitializer() {
	}

	/**
	 * Register {@link DatastoreResolver}s bean into given <code>datastore</code>.
	 * @param datastore Datastore
	 * @param datastoreBeanName Datastore bean name
	 * @param factory BeanFactory
	 */
	public static void configureDatastore(Datastore datastore, String datastoreBeanName, BeanFactory factory) {
		ObjectUtils.argumentNotNull(datastore, "Null Datastore");
		ObjectUtils.argumentNotNull(datastoreBeanName, "Null Datastore bean name");
		ObjectUtils.argumentNotNull(factory, "Null BeanFactory");

		if (factory instanceof ListableBeanFactory) {
			configureDatastoreResolvers(datastore, datastoreBeanName, (ListableBeanFactory) factory);
			configureDatastoreCommodityFactories(datastore, datastoreBeanName, (ListableBeanFactory) factory);
			configureDatastorePostProcessors(datastore, datastoreBeanName, (ListableBeanFactory) factory);
		} else {
			LOGGER.warn(
					"The BeanFactory [" + factory + "] is not a ListableBeanFactory: skipping Datastore configuration");
		}

	}

	/**
	 * Register {@link DatastoreResolver} annotated beans as <code>datastore</code> {@link ExpressionResolver}s.
	 * @param datastore Datastore to configure
	 * @param datastoreBeanName Datastore bean name to configure
	 * @param beanFactory Bean factory
	 */
	private static void configureDatastoreResolvers(Datastore datastore, String datastoreBeanName,
			ListableBeanFactory beanFactory) {
		final String[] beanNames = beanFactory.getBeanNamesForAnnotation(DatastoreResolver.class);
		if (beanNames != null && beanNames.length > 0) {
			for (String beanName : beanNames) {
				if (!ExpressionResolver.class.isAssignableFrom(beanFactory.getType(beanName))) {
					throw new BeanNotOfRequiredTypeException(beanName, ExpressionResolver.class,
							beanFactory.getType(beanName));
				}
				DatastoreResolver dr = beanFactory.findAnnotationOnBean(beanName, DatastoreResolver.class);
				String datastoreRef = AnnotationUtils.getStringValue(dr.datastoreBeanName());
				if (datastoreRef == null || datastoreRef.equals(datastoreBeanName)) {
					// register resolver
					ExpressionResolver<?, ?> resolver = (ExpressionResolver<?, ?>) beanFactory.getBean(beanName);
					datastore.addExpressionResolver(resolver);
					LOGGER.debug(() -> "Registered expression resolver [" + resolver.getClass().getName()
							+ "] into Datastore with bean name [" + datastoreBeanName + "]");
				}
			}
		}
	}

	/**
	 * Register {@link com.holonplatform.spring.DatastoreCommodityFactory} annotated beans as <code>datastore</code>
	 * {@link DatastoreCommodityFactory}s.
	 * @param datastore Datastore to configure
	 * @param datastoreBeanName Datastore bean name to configure
	 * @param beanFactory Bean factory
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void configureDatastoreCommodityFactories(Datastore datastore, String datastoreBeanName,
			ListableBeanFactory beanFactory) {
		if (datastore instanceof DatastoreCommodityRegistrar) {
			final Class<? extends DatastoreCommodityFactory> baseType = ((DatastoreCommodityRegistrar<?>) datastore)
					.getCommodityFactoryType();
			if (baseType != null) {
				final String[] beanNames = beanFactory
						.getBeanNamesForAnnotation(com.holonplatform.spring.DatastoreCommodityFactory.class);
				if (beanNames != null && beanNames.length > 0) {
					for (String beanName : beanNames) {
						com.holonplatform.spring.DatastoreCommodityFactory dr = beanFactory.findAnnotationOnBean(
								beanName, com.holonplatform.spring.DatastoreCommodityFactory.class);
						String datastoreRef = AnnotationUtils.getStringValue(dr.datastoreBeanName());
						if (datastoreRef == null || datastoreRef.equals(datastoreBeanName)) {
							if (!baseType.isAssignableFrom(beanFactory.getType(beanName))) {
								throw new BeanNotOfRequiredTypeException(beanName, baseType,
										beanFactory.getType(beanName));
							}
							// register resolver
							DatastoreCommodityFactory datastoreCommodityFactory = (DatastoreCommodityFactory) beanFactory
									.getBean(beanName);
							((DatastoreCommodityRegistrar) datastore).registerCommodity(datastoreCommodityFactory);
							LOGGER.debug(() -> "Registered factory [" + datastoreCommodityFactory.getClass().getName()
									+ "] into Datastore with bean name [" + datastoreBeanName + "]");
						}
					}
				}
			} else {
				LOGGER.debug(
						() -> "Datastore [" + datastore + "] does not declares a DatastoreCommodityFactory base type: "
								+ "skipping DatastoreCommodityFactory beans configuration");
			}
		} else {
			LOGGER.debug(() -> "Datastore [" + datastore
					+ "] is not a DatastoreCommodityRegistrar: skipping DatastoreCommodityFactory beans configuration");
		}
	}

	/**
	 * Register {@link DatastorePostProcessor} type beans in <code>datastore</code>.
	 * @param datastore Datastore to configure
	 * @param datastoreBeanName Datastore bean name to configure
	 * @param beanFactory Bean factory
	 */
	private static void configureDatastorePostProcessors(Datastore datastore, String datastoreBeanName,
			ListableBeanFactory beanFactory) {
		final String[] beanNames = beanFactory.getBeanNamesForType(DatastorePostProcessor.class, false, true);
		if (beanNames != null && beanNames.length > 0) {
			for (String beanName : beanNames) {
				beanFactory.getBean(beanName, DatastorePostProcessor.class).postProcessDatastore(datastore,
						datastoreBeanName);
			}
		}
	}

}
