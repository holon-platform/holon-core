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
package com.holonplatform.core.internal.datastore;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import javax.annotation.Priority;

import com.holonplatform.core.Expression;
import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverHandler;
import com.holonplatform.core.ExpressionResolver.ResolutionContext;
import com.holonplatform.core.ExpressionResolverRegistry;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.DatastoreCommodity;
import com.holonplatform.core.datastore.DatastoreCommodityContext;
import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityConfigurationException;
import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityNotAvailableException;
import com.holonplatform.core.datastore.DatastoreCommodityFactory;
import com.holonplatform.core.datastore.DatastoreCommodityRegistrar;
import com.holonplatform.core.datastore.DatastoreExpressionResolverRegistrar;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Abstract {@link Datastore} implementation.
 * 
 * @param <X> Concrete commodity context type
 * 
 * @since 5.0.0
 */
public abstract class AbstractDatastore<X extends DatastoreCommodityContext> implements Datastore,
		ExpressionResolverHandler, DatastoreCommodityRegistrar<X>, DatastoreExpressionResolverRegistrar {

	private static final long serialVersionUID = 8163804295646169319L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = DatastoreLogger.create();

	/*
	 * Optional data context id id
	 */
	private String dataContextId;

	/*
	 * Trace Datastore operations
	 */
	private boolean traceEnabled = false;

	/*
	 * Expression resolvers
	 */
	private final ExpressionResolverRegistry expressionResolverRegistry = ExpressionResolverRegistry.create();

	@SuppressWarnings("rawtypes")
	private final Class<? extends ExpressionResolver> expressionResolverType;

	/*
	 * Commodities
	 */
	private final Map<Class<? extends DatastoreCommodity>, DatastoreCommodityFactory<X, ?>> commodities = new HashMap<>(
			4);

	@SuppressWarnings("rawtypes")
	private final Class<? extends DatastoreCommodityFactory> commodityFactoryType;

	@SuppressWarnings("rawtypes")
	private static final Comparator<DatastoreCommodityFactory> PRIORITY_COMPARATOR = Comparator
			.comparingInt(p -> p.getClass().isAnnotationPresent(Priority.class)
					? p.getClass().getAnnotation(Priority.class).value() : DatastoreCommodityFactory.DEFAULT_PRIORITY);

	/**
	 * Constructor
	 * @param commodityFactoryType Base {@link DatastoreCommodityFactory} type to be loaded using standard Java
	 *        {@link ServiceLoader} extensions, or <code>null</code> to disable automatic factory registration.
	 * @param expressionResolverType Base {@link ExpressionResolver} type to be loaded using standard Java
	 *        {@link ServiceLoader} extensions, or <code>null</code> to disable automatic resolvers registration.
	 */
	@SuppressWarnings("rawtypes")
	public AbstractDatastore(Class<? extends DatastoreCommodityFactory> commodityFactoryType,
			Class<? extends ExpressionResolver> expressionResolverType) {
		super();
		this.commodityFactoryType = commodityFactoryType;
		this.expressionResolverType = expressionResolverType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreCommodityRegistrar#getCommodityFactoryType()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends DatastoreCommodityFactory> getCommodityFactoryType() {
		return commodityFactoryType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreExpressionResolverRegistrar#getExpressionResolverType()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends ExpressionResolver> getExpressionResolverType() {
		return expressionResolverType;
	}

	/**
	 * Load default {@link DatastoreCommodityFactory}s using Java {@link ServiceLoader} extensions only if
	 * {@link #getCommodityFactoryType()} is not null.
	 * @param classLoader The ClassLoader to use. If <code>null</code>, this class ClassLoader or the default
	 *        ClassLoader will be used.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected synchronized void loadCommodityFactories(ClassLoader classLoader) {
		final Class<? extends DatastoreCommodityFactory> commodityFactoryType = getCommodityFactoryType();
		if (commodityFactoryType != null) {
			final ClassLoader cl = (classLoader != null) ? classLoader
					: (this.getClass().getClassLoader() != null) ? this.getClass().getClassLoader()
							: ClassUtils.getDefaultClassLoader();

			LOGGER.debug(() -> "Load DatastoreCommodityFactory for classloader [" + cl
					+ "] using ServiceLoader with service name: " + commodityFactoryType.getName());

			// load from META-INF/services
			final List<DatastoreCommodityFactory> factories = new LinkedList<>();
			Iterable<? extends DatastoreCommodityFactory> loaded = AccessController
					.doPrivileged(new PrivilegedAction<Iterable<? extends DatastoreCommodityFactory>>() {
						@Override
						public Iterable<? extends DatastoreCommodityFactory> run() {
							return ServiceLoader.load(commodityFactoryType, classLoader);
						}
					});
			loaded.forEach(f -> {
				factories.add(f);
			});
			Collections.sort(factories, PRIORITY_COMPARATOR);
			factories.forEach(f -> {
				final Class commodityType = f.getCommodityType();
				if (commodityType != null) {
					if (!commodities.containsKey(commodityType)) {
						commodities.put(commodityType, f);
						LOGGER.debug(() -> "Registered commodity factory [" + f.getClass().getName()
								+ "] bound to commodity type [" + commodityType.getName() + "]");
					}
				} else {
					LOGGER.warn("Invalid commodity factory [" + f.getClass().getName() + "]: the commodity type "
							+ "returned by getCommodityType() is null - skipping factory registration");
				}
			});
		}
	}

	/**
	 * Load default {@link ExpressionResolver}s using Java {@link ServiceLoader} extensions only if
	 * {@link #getExpressionResolverType()} is not null.
	 * @param classLoader The ClassLoader to use. If <code>null</code>, this class ClassLoader or the default
	 *        ClassLoader will be used.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected synchronized void loadExpressionResolvers(ClassLoader classLoader) {
		final Class<? extends ExpressionResolver> expressionResolverType = getExpressionResolverType();
		if (expressionResolverType != null) {
			final ClassLoader cl = (classLoader != null) ? classLoader
					: (this.getClass().getClassLoader() != null) ? this.getClass().getClassLoader()
							: ClassUtils.getDefaultClassLoader();

			LOGGER.debug(() -> "Load ExpressionResolvers for classloader [" + cl
					+ "] using ServiceLoader with service name: " + expressionResolverType.getName());

			// load from META-INF/services
			Iterable<? extends ExpressionResolver> loaded = AccessController
					.doPrivileged(new PrivilegedAction<Iterable<? extends ExpressionResolver>>() {
						@Override
						public Iterable<? extends ExpressionResolver> run() {
							return ServiceLoader.load(expressionResolverType, classLoader);
						}
					});
			loaded.forEach(er -> {
				addExpressionResolver(er);
				LOGGER.debug(() -> "Registered ExpressionResolver [" + er.getClass().getName() + "]");
			});
		}
	}

	/**
	 * Get the Datastore {@link ExpressionResolverRegistry}.
	 * @return the ExpressionResolverRegistry
	 */
	protected ExpressionResolverRegistry getExpressionResolverRegistry() {
		return expressionResolverRegistry;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverHandler#resolve(com.holonplatform.core.Expression,
	 * java.lang.Class, com.holonplatform.core.ExpressionResolver.ResolutionContext)
	 */
	@Override
	public <E extends Expression, R extends Expression> Optional<R> resolve(E expression, Class<R> resolutionType,
			ResolutionContext context) throws InvalidExpressionException {
		return getExpressionResolverRegistry().resolve(expression, resolutionType, context);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DataContextBound#getDataContextId()
	 */
	@Override
	public Optional<String> getDataContextId() {
		return Optional.ofNullable(dataContextId);
	}

	/**
	 * Optional id to distinguish this datastore data context between multiple avaialable contexts
	 * @param dataContextId Data context id
	 */
	public void setDataContextId(String dataContextId) {
		this.dataContextId = dataContextId;
		LOGGER.debug(() -> "Datastore [" + this + "]: setted data context id [" + dataContextId + "]");
	}

	/**
	 * Get whether to trace Datastore operations.
	 * @return the trace <code>true</code> if tracing is enabled, <code>false</code> otherwise
	 */
	public boolean isTraceEnabled() {
		return traceEnabled;
	}

	/**
	 * Set whether to trace Datastore operations.
	 * @param trace Whether to trace Datastore operations
	 */
	public void setTraceEnabled(boolean trace) {
		this.traceEnabled = trace;
		LOGGER.debug(() -> "Datastore [" + this + "]: setted trace enabled [" + trace + "]");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport#addExpressionResolver(com.holonplatform.core.
	 * ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> void addExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		getExpressionResolverRegistry().addExpressionResolver(expressionResolver);
		LOGGER.debug(() -> "Datastore [" + this + "]: added ExpressionResolver [" + expressionResolver + "]");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport#removeExpressionResolver(com.holonplatform.
	 * core.ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> void removeExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		getExpressionResolverRegistry().removeExpressionResolver(expressionResolver);
		LOGGER.debug(() -> "Datastore [" + this + "]: removed ExpressionResolver [" + expressionResolver + "]");
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport#getExpressionResolvers()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Iterable<ExpressionResolver> getExpressionResolvers() {
		return getExpressionResolverRegistry().getExpressionResolvers();
	}

	/**
	 * Get the {@link DatastoreCommodityContext} to use for {@link DatastoreCommodity} setup.
	 * @return The {@link DatastoreCommodityContext}
	 * @throws CommodityConfigurationException Error configuring context
	 */
	protected abstract X getCommodityContext() throws CommodityConfigurationException;

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.datastore.DatastoreCommodityRegistrar#registerCommodity(com.holonplatform.core.datastore.
	 * DatastoreCommodityFactory)
	 */
	@Override
	public synchronized <C extends DatastoreCommodity> void registerCommodity(
			DatastoreCommodityFactory<X, C> commodityFactory) {
		ObjectUtils.argumentNotNull(commodityFactory, "The DatastoreCommodityFactory to register must be not null");
		final Class<? extends C> commodityType = commodityFactory.getCommodityType();
		if (commodityType == null) {
			throw new CommodityConfigurationException(
					"Invalid commodity factory [" + commodityFactory.getClass().getName() + "]: the commodity type "
							+ "returned by getCommodityType() must be not null");
		}
		DatastoreCommodityFactory<X, ?> previous = commodities.put(commodityType, commodityFactory);
		LOGGER.debug(() -> "Registered commodity factory [" + commodityFactory.getClass().getName()
				+ "] bound to commodity type [" + commodityType.getName() + "]");
		if (previous != null) {
			LOGGER.debug(() -> "The commodity factory [" + previous.getClass().getName() + "] bound to commodity type ["
					+ commodityType.getName() + "] was replaced by [" + commodityFactory.getClass().getName() + "]");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.Datastore#getAvailableCommodities()
	 */
	@Override
	public Collection<Class<? extends DatastoreCommodity>> getAvailableCommodities() {
		return Collections.unmodifiableSet(commodities.keySet());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.Datastore#create(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends DatastoreCommodity> C create(Class<C> commodityType) {
		ObjectUtils.argumentNotNull(commodityType, "The commodity type to create must be not null");
		DatastoreCommodityFactory<X, ?> factory = commodities.get(commodityType);
		if (factory == null) {
			throw new CommodityNotAvailableException(
					"No Datastore commodity of type [" + commodityType.getName() + "] is available");
		}
		C commodity = (C) factory.createCommodity(getCommodityContext());
		if (commodity == null) {
			throw new CommodityConfigurationException("The commodity factory [" + factory.getClass().getName()
					+ "] returned null for commodity type [" + commodityType.getName() + "]");
		}
		return commodity;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbstractDatastore [dataContextId=" + dataContextId + "]";
	}

}
