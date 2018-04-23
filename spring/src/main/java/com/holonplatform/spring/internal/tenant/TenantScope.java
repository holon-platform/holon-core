package com.holonplatform.spring.internal.tenant;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.Logger.Level;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * Spring scope related to current tenant id using a {@link TenantResolver}.
 */
public class TenantScope implements Scope {

	/**
	 * Scope name
	 */
	public static final String SCOPE_NAME = "tenant";

	/**
	 * Tenant resolver bean name
	 */
	private final String tenantResolverBeanName;

	/**
	 * Bean factory
	 */
	private final WeakReference<BeanFactory> beanFactory;

	/**
	 * {@link TenantBeanStore}s manager
	 */
	private final TenantBeanStoresManager storesManager;

	/**
	 * Construct a new TenantScope
	 * @param tenantResolverBeanName TenantResolver bean name
	 * @param beanFactory BeanFactory
	 */
	public TenantScope(String tenantResolverBeanName, BeanFactory beanFactory) {
		super();
		ObjectUtils.argumentNotNull(tenantResolverBeanName, "Tenant resolver bean name must be not null");
		ObjectUtils.argumentNotNull(beanFactory, "BeanFactory must be not null");
		this.tenantResolverBeanName = tenantResolverBeanName;
		this.beanFactory = new WeakReference<>(beanFactory);
		this.storesManager = new TenantBeanStoresManager();
	}

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		return getBeanStore().get(name, objectFactory);
	}

	@Override
	public Object remove(String name) {
		return getBeanStore().remove(name);
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		getBeanStore().registerDestructionCallback(name, callback);
	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}

	@Override
	public String getConversationId() {
		return getTenantResolver().getTenantId().orElse(null);
	}

	/**
	 * Destroy tenant scope, calling any scoped bean destruction callback
	 */
	public void destroy() {
		this.storesManager.destroy();
	}

	/**
	 * Destroy the bean store bound to given <code>tenantId</code>, i.e. removes all the scoped bean instances which
	 * refer to given tenant id, triggering any associated destruction callback.
	 * @param tenantId The tenant id which identifies the bean store (not null)
	 */
	public void destroy(String tenantId) {
		ObjectUtils.argumentNotNull(tenantId, "Tenant id must be not null");
		this.storesManager.destroy(tenantId);
	}

	/**
	 * Get the {@link TenantBeanStore} for current tenant id
	 * @return TenantBeanStore
	 */
	private synchronized TenantBeanStore getBeanStore() {
		return storesManager.getBeanStore(getTenantResolver().getTenantId()
				.orElseThrow(() -> new IllegalStateException("No tenant id available from the TenantResolver ["
						+ getTenantResolver() + "] - Tenant resolver bean name: [" + tenantResolverBeanName + "]")));
	}

	/**
	 * Get the {@link TenantResolver} to use from the BeanFactory.
	 * @return The TenantResolver bean instance
	 */
	private TenantResolver getTenantResolver() throws IllegalStateException {
		final BeanFactory factory = beanFactory.get();
		if (factory == null) {
			throw new IllegalStateException("A BeanFactory is not available");
		}
		try {
			return factory.getBean(tenantResolverBeanName, TenantResolver.class);
		} catch (Exception e) {
			throw new IllegalStateException("Tenant scope: failed to obtain a valid TenantResolver", e);
		}
	}

	// Stores manager

	/**
	 * Manager to handle {@link TenantBeanStore}s.
	 */
	static class TenantBeanStoresManager implements Serializable {

		private static final long serialVersionUID = 1374634976273935218L;

		/*
		 * Logger
		 */
		private static final Logger LOGGER = SpringLogger.create();

		/**
		 * Thread-safe tenantId - TenantBeanStore map
		 */
		private final Map<String, TenantBeanStore> stores = new ConcurrentHashMap<>();

		/**
		 * Get (and creta if not present) the TenantBeanStore associated with given <code>tenantId</code>.
		 * @param tenantId Tenant id
		 * @return TenantBeanStore
		 */
		@SuppressWarnings("serial")
		public TenantBeanStore getBeanStore(final String tenantId) {
			TenantBeanStore beanStore = stores.get(tenantId);
			if (beanStore == null) {
				beanStore = new TenantBeanStore(tenantId, new TenantBeanStore.DestructionCallback() {

					@Override
					public void beanStoreDestroyed(TenantBeanStore beanStore) {
						removeBeanStore(tenantId);
					}

				});
				stores.put(tenantId, beanStore);

				if (LOGGER.isEnabled(Level.DEBUG)) {
					final TenantBeanStore tbs = beanStore;
					LOGGER.debug(() -> "Added [" + tbs + "] to: " + this);
				}
			}
			return beanStore;
		}

		/**
		 * Removes the TenantBeanStore associated to given <code>tenantId</code>, if any.
		 * @param tenantId Tenant id
		 */
		void removeBeanStore(final String tenantId) {
			final TenantBeanStore removed = stores.remove(tenantId);
			LOGGER.debug(() -> "Removed [" + removed + "] from: " + this);
		}

		void destroy(String tenantId) {

			LOGGER.debug(() -> "Destroying bean store for tenant id [" + tenantId + "]");

			TenantBeanStore beanStore = stores.get(tenantId);
			if (beanStore != null) {
				beanStore.destroy();
			}

		}

		void destroy() {

			LOGGER.debug(() -> "Destroying [" + this + "]");

			for (TenantBeanStore beanStore : new HashSet<>(stores.values())) {
				beanStore.destroy();
			}

			if (!stores.isEmpty()) {
				throw new IllegalStateException(
						"TenantBeanStore should have been emptied by the destruction callbacks");
			}
		}

	}

}
