package com.holonplatform.spring.internal.tenant;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.ObjectFactory;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.Logger.Level;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * Store for tenant scoped beans
 */
class TenantBeanStore implements Serializable {

	private static final long serialVersionUID = 1802430095780703386L;

	/**
	 * Callback for bean store destruction.
	 */
	public static interface DestructionCallback extends Serializable {

		/**
		 * The bean store has been destroyed
		 * @param beanStore TenantBeanStore
		 */
		void beanStoreDestroyed(TenantBeanStore beanStore);

	}

	/*
	 * Logger
	 */
	private static final Logger LOGGER = SpringLogger.create();

	/**
	 * Bean instances
	 */
	private final Map<String, Object> objectMap = new ConcurrentHashMap<>();
	/**
	 * Destruction callbacks
	 */
	private final Map<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();

	/**
	 * Store name
	 */
	private final String name;

	/**
	 * Store destruction callback
	 */
	private final DestructionCallback destructionCallback;

	private boolean destroyed = false;

	/**
	 * Constructor with store destruction callback
	 * @param name Bean store name
	 * @param destructionCallback Destruction callback
	 */
	public TenantBeanStore(String name, DestructionCallback destructionCallback) {
		this.name = name;
		this.destructionCallback = destructionCallback;
	}

	/**
	 * Constructor
	 * @param name Bean store name
	 */
	public TenantBeanStore(String name) {
		this(name, null);
	}

	/**
	 * Get the bean instance with given <code>beanName</code>.
	 * @param beanName Bean name to obtain
	 * @param objectFactory Spring ObjectFactory
	 * @return The bean instance present in this store. If not yet available, a new instance will be created, stored and
	 *         returned
	 */
	public Object get(String beanName, ObjectFactory<?> objectFactory) {
		LOGGER.debug(() -> "Getting bean with name [" + beanName + "] from: " + this);

		Object bean = objectMap.get(beanName);
		if (bean == null) {
			bean = create(beanName, objectFactory);
			objectMap.put(beanName, bean);

			if (LOGGER.isEnabled(Level.DEBUG)) {
				final Object b = bean;
				LOGGER.debug(() -> "Added bean [" + b + "] with name [" + beanName + "] to: " + this);
			}
		}
		return bean;
	}

	/**
	 * Create a new bean instance for given <code>beanName</code>.
	 * @param beanName Bean name
	 * @param objectFactory Spring ObjectFactory
	 * @return New bean instance
	 */
	protected Object create(String beanName, ObjectFactory<?> objectFactory) {
		final Object bean = objectFactory.getObject();
		if (!(bean instanceof Serializable)) {
			LOGGER.warn("Storing non-serializable bean [" + bean + "] with name [" + beanName + "] in: " + this);
		}
		return bean;
	}

	/**
	 * Remove the bean instance with given <code>beanName</code> from this store.
	 * @param beanName Bean name to remove
	 * @return Removed instance, or <code>null</code> if it was not stored
	 */
	public Object remove(String beanName) {
		destructionCallbacks.remove(beanName);
		return objectMap.remove(beanName);
	}

	/**
	 * Register a destruction callback for given <code>beanName</code>.
	 * @param beanName Bean name
	 * @param runnable Destruction callback to register
	 */
	public void registerDestructionCallback(String beanName, Runnable runnable) {
		LOGGER.debug(() -> "Registering destruction callback for bean with name [" + beanName + "] in: " + this);

		destructionCallbacks.put(beanName, runnable);
	}

	/**
	 * Destroy this bean store
	 */
	public void destroy() {
		if (destroyed) {
			LOGGER.debug(() -> this + " has already been destroyed, ignoring");
			return;
		}
		try {
			LOGGER.debug(() -> "Destroying " + this);

			for (Runnable destructionCallback : destructionCallbacks.values()) {
				try {
					destructionCallback.run();
				} catch (Exception e) {
					LOGGER.error("TenantBeanStore destruction callback failed", e);
				}
			}
			destructionCallbacks.clear();
			objectMap.clear();
			if (destructionCallback != null) {
				try {
					destructionCallback.beanStoreDestroyed(this);
				} catch (Exception e) {
					LOGGER.error("TenantBeanStore final destruction callback failed", e);
				}
			}
		} finally {
			destroyed = true;
		}
	}

	@Override
	public String toString() {
		return String.format("%s[id=%x, name=%s]", getClass().getSimpleName(), System.identityHashCode(this), name);
	}

}
