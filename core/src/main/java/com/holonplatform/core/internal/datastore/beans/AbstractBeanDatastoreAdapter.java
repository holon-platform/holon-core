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
package com.holonplatform.core.internal.datastore.beans;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.holonplatform.core.beans.BeanDataTarget;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.datastore.beans.BeanDatastore.BeanOperationResult;
import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyBox;

/**
 * Abstract bean datastore adapter class.
 * 
 * @param <E> Concrete executor type
 *
 * @since 5.1.0
 */
public abstract class AbstractBeanDatastoreAdapter<E> {

	private final E executor;

	/**
	 * Constructor.
	 * @param executor The concrete operation executor (not null)
	 */
	public AbstractBeanDatastoreAdapter(E executor) {
		super();
		ObjectUtils.argumentNotNull(executor, "Operation executor must be not null");
		this.executor = executor;
	}

	/**
	 * Get the operation executor.
	 * @return the operation executor
	 */
	protected E getExecutor() {
		return executor;
	}

	/**
	 * Get the bean class of given bean instance.
	 * @param <T> Bean type
	 * @param bean Bean instance (not null)
	 * @return Bean class
	 */
	@SuppressWarnings("unchecked")
	protected static <T> Class<? extends T> getBeanClass(final T bean) {
		ObjectUtils.argumentNotNull(bean, "Bean instance must be not null");
		return (Class<? extends T>) bean.getClass();
	}

	/**
	 * Get the bean property set for given bean class.
	 * @param <T> Bean type
	 * @param beanClass Bean class (not null)
	 * @return The bean property set
	 * @throws DataAccessException If an error occurred
	 */
	protected static <T> BeanPropertySet<T> getBeanPropertySet(final Class<? extends T> beanClass)
			throws DataAccessException {
		ObjectUtils.argumentNotNull(beanClass, "Bean class must be not null");
		try {
			return BeanPropertySet.create(beanClass);
		} catch (Exception e) {
			throw new DataAccessException("Failed to obtain bean property set from bean class [" + beanClass + "]", e);
		}
	}

	/**
	 * Get the bean property set for given bean instance class.
	 * @param <T> Bean type
	 * @param bean Bean instance (not null)
	 * @return The bean property set
	 * @throws DataAccessException If an error occurred
	 */
	protected static <T> BeanPropertySet<T> getBeanPropertySet(final T bean) throws DataAccessException {
		ObjectUtils.argumentNotNull(bean, "Bean instance must be not null");
		try {
			return BeanPropertySet.create(getBeanClass(bean));
		} catch (Exception e) {
			throw new DataAccessException(
					"Failed to obtain bean property set from bean class [" + bean.getClass() + "]", e);
		}
	}

	/**
	 * Convert given bean instance into a {@link PropertyBox}.
	 * @param <T> Bean type
	 * @param instance Bean instance to convert (not null)
	 * @return Bean instance property values as a {@link PropertyBox}
	 * @throws DataAccessException If an error occurred
	 */
	protected static <T> PropertyBox asPropertyBox(final T instance) throws DataAccessException {
		try {
			return getBeanPropertySet(instance).read(instance);
		} catch (Exception e) {
			throw new DataAccessException("Failed to convert bean instance [" + instance + "] of type ["
					+ instance.getClass() + "] into a PropertyBox", e);
		}
	}

	/**
	 * Convert given {@link PropertyBox} instance into a bean of required type.
	 * @param <T> Bean type
	 * @param beanClass Bean class (not null)
	 * @param propertyBox {@link PropertyBox} instance to convert
	 * @return A new bean instance with property values read from the {@link PropertyBox} instance
	 * @throws DataAccessException If an error occurred
	 */
	protected static <T> T asBean(final Class<T> beanClass, final PropertyBox propertyBox) throws DataAccessException {
		if (propertyBox == null) {
			return null;
		}
		try {
			return getBeanPropertySet(beanClass).write(propertyBox, beanClass.getDeclaredConstructor().newInstance());
		} catch (Exception e) {
			throw new DataAccessException("Failed to convert PropertyBox instance [" + propertyBox
					+ "] into a bean instance of type [" + beanClass + "]", e);
		}
	}

	/**
	 * Get the {@link DataTarget} to use with given bean class.
	 * @param <T> Bean type
	 * @param beanClass Bean class (not null)
	 * @return The bean class data target
	 * @throws DataAccessException If the data target cannot be obtained
	 */
	protected static <T> DataTarget<T> getDataTarget(final Class<? extends T> beanClass) {
		return BeanDataTarget.of(beanClass);
	}

	/**
	 * Get the {@link DataTarget} to use with given bean instance class.
	 * @param <T> Bean type
	 * @param bean Bean instance (not null)
	 * @return The bean class data target
	 * @throws DataAccessException If the data target cannot be obtained
	 */
	protected static <T> DataTarget<T> getDataTarget(final T bean) {
		ObjectUtils.argumentNotNull(bean, "Bean instance must be not null");
		return getDataTarget(getBeanClass(bean));
	}

	/**
	 * Convert an {@link OperationResult} into a {@link BeanOperationResult}.
	 * @param <T> Bean type
	 * @param result Operation result
	 * @param resultBean Optional result bean instance
	 * @return The {@link BeanOperationResult} instance
	 */
	protected static <T> BeanOperationResult<T> convert(OperationResult result, T resultBean) {
		final BeanOperationResult.Builder<T> builder = BeanOperationResult.builder();
		result.getOperationType().ifPresent(ot -> builder.type(ot));
		builder.affectedCount(result.getAffectedCount());
		if (resultBean != null) {
			builder.result(resultBean);
		}
		return builder.build();
	}

	/**
	 * Process given {@link WriteOption}s, esuring the given <code>addWiteOption</code> is present.
	 * @param options Write options to process
	 * @param addWriteOption The {@link WriteOption} to add if not present
	 * @return Processed write options
	 */
	protected static WriteOption[] processWriteOptions(WriteOption[] options, WriteOption addWriteOption) {
		if (addWriteOption == null) {
			return options;
		}
		List<WriteOption> wos = (options != null) ? Arrays.asList(options) : new LinkedList<>();
		if (!wos.contains(addWriteOption)) {
			List<WriteOption> nwos = new LinkedList<>(wos);
			nwos.add(addWriteOption);
			return nwos.toArray(new WriteOption[nwos.size()]);
		}
		return options;
	}

}
