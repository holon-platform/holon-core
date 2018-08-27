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
package com.holonplatform.spring.internal.datastore;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.ListableBeanFactory;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.ConfigurableDatastore;
import com.holonplatform.core.datastore.DatastoreCommodityFactory;
import com.holonplatform.core.datastore.DatastoreCommodityRegistrar;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.AnnotationUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.spring.DatastorePostProcessor;
import com.holonplatform.spring.DatastoreResolver;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * Utility class to initialize a {@link ConfigurableDatastore} in the Spring context.
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
	 * @return A message for log purposes which resumes the registered resolvers
	 */
	public static String configureDatastore(ConfigurableDatastore datastore, String datastoreBeanName,
			BeanFactory factory) {
		ObjectUtils.argumentNotNull(datastore, "Null Datastore");
		ObjectUtils.argumentNotNull(datastoreBeanName, "Null Datastore bean name");
		ObjectUtils.argumentNotNull(factory, "Null BeanFactory");

		List<String> messages = new LinkedList<>();

		if (factory instanceof ListableBeanFactory) {
			messages.add(configureDatastoreResolvers(datastore, datastoreBeanName, (ListableBeanFactory) factory));
			messages.add(
					configureDatastoreCommodityFactories(datastore, datastoreBeanName, (ListableBeanFactory) factory));
			messages.add(configureDatastorePostProcessors(datastore, datastoreBeanName, (ListableBeanFactory) factory));
		} else {
			messages.add("Skip Datastore configuration: The BeanFactory is not a ListableBeanFactory");
			LOGGER.warn(
					"The BeanFactory [" + factory + "] is not a ListableBeanFactory: skipping Datastore configuration");
		}

		return messages.stream().filter(m -> m != null).collect(Collectors.joining(", "));
	}

	/**
	 * Register {@link DatastoreResolver} annotated beans as <code>datastore</code> {@link ExpressionResolver}s.
	 * @param datastore Datastore to configure
	 * @param datastoreBeanName Datastore bean name to configure
	 * @param beanFactory Bean factory
	 * @return A message for log purposes which resumes the registered resolvers
	 */
	private static String configureDatastoreResolvers(ConfigurableDatastore datastore, String datastoreBeanName,
			ListableBeanFactory beanFactory) {
		int count = 0;
		final String[] beanNames = beanFactory.getBeanNamesForAnnotation(DatastoreResolver.class);
		if (beanNames != null && beanNames.length > 0) {
			for (String beanName : beanNames) {
				final Class<?> beanType = beanFactory.getType(beanName);
				if (beanType != null) {
					if (!ExpressionResolver.class.isAssignableFrom(beanType)) {
						throw new BeanNotOfRequiredTypeException(beanName, ExpressionResolver.class, beanType);
					}
					DatastoreResolver dr = beanFactory.findAnnotationOnBean(beanName, DatastoreResolver.class);
					String datastoreRef = (dr != null) ? AnnotationUtils.getStringValue(dr.datastoreBeanName()) : null;
					if (datastoreRef == null || datastoreRef.equals(datastoreBeanName)) {
						// register resolver
						ExpressionResolver<?, ?> resolver = (ExpressionResolver<?, ?>) beanFactory.getBean(beanName);
						datastore.addExpressionResolver(resolver);
						count++;
						LOGGER.debug(() -> "Registered expression resolver [" + resolver.getClass().getName()
								+ "] into Datastore with bean name [" + datastoreBeanName + "]");
					}
				}
			}
		}
		return "Registered DatastoreResolvers: " + count;
	}

	/**
	 * Register {@link com.holonplatform.spring.DatastoreCommodityFactory} annotated beans as <code>datastore</code>
	 * {@link DatastoreCommodityFactory}s.
	 * @param datastore Datastore to configure
	 * @param datastoreBeanName Datastore bean name to configure
	 * @param beanFactory Bean factory
	 * @return A message for log purposes which resumes the registered resolvers
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static String configureDatastoreCommodityFactories(ConfigurableDatastore datastore,
			String datastoreBeanName, ListableBeanFactory beanFactory) {
		int count = 0;
		if (datastore instanceof DatastoreCommodityRegistrar) {
			final Class<? extends DatastoreCommodityFactory> baseType = ((DatastoreCommodityRegistrar<?>) datastore)
					.getCommodityFactoryType();
			if (baseType != null) {
				final String[] beanNames = beanFactory
						.getBeanNamesForAnnotation(com.holonplatform.spring.DatastoreCommodityFactory.class);
				if (beanNames != null && beanNames.length > 0) {
					for (String beanName : beanNames) {
						final Class<?> beanType = beanFactory.getType(beanName);
						if (beanType != null) {
							com.holonplatform.spring.DatastoreCommodityFactory dr = beanFactory.findAnnotationOnBean(
									beanName, com.holonplatform.spring.DatastoreCommodityFactory.class);
							String datastoreRef = (dr != null) ? AnnotationUtils.getStringValue(dr.datastoreBeanName())
									: null;
							if (datastoreRef == null || datastoreRef.equals(datastoreBeanName)) {
								if (baseType.isAssignableFrom(beanType)) {
									// register resolver
									DatastoreCommodityFactory datastoreCommodityFactory = (DatastoreCommodityFactory) beanFactory
											.getBean(beanName);
									((DatastoreCommodityRegistrar) datastore)
											.registerCommodity(datastoreCommodityFactory);
									count++;
									LOGGER.debug(() -> "Registered factory ["
											+ datastoreCommodityFactory.getClass().getName()
											+ "] into Datastore with bean name [" + datastoreBeanName + "]");
								} else {
									LOGGER.debug(() -> "Skipped factory of type [" + beanType.getName()
											+ "] because does not match the Datastore [" + datastoreBeanName
											+ "] factory type [" + baseType.getName() + "]");
								}
							}
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
		return "Registered DatastoreCommodityFactories: " + count;
	}

	/**
	 * Register {@link DatastorePostProcessor} type beans in <code>datastore</code>.
	 * @param datastore Datastore to configure
	 * @param datastoreBeanName Datastore bean name to configure
	 * @param beanFactory Bean factory
	 * @return A message for log purposes which resumes the registered resolvers
	 */
	private static String configureDatastorePostProcessors(ConfigurableDatastore datastore, String datastoreBeanName,
			ListableBeanFactory beanFactory) {
		int count = 0;
		final String[] beanNames = beanFactory.getBeanNamesForType(DatastorePostProcessor.class, false, true);
		if (beanNames != null && beanNames.length > 0) {
			for (String beanName : beanNames) {
				beanFactory.getBean(beanName, DatastorePostProcessor.class).postProcessDatastore(datastore,
						datastoreBeanName);
				count++;
			}
		}
		return "Applied DatastorePostProcessors: " + count;
	}

}
