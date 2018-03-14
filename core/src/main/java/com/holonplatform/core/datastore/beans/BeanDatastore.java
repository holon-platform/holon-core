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
package com.holonplatform.core.datastore.beans;

import java.io.Serializable;
import java.util.Optional;

import com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport;
import com.holonplatform.core.beans.DataPath;
import com.holonplatform.core.datastore.DataContextBound;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.Datastore.OperationType;
import com.holonplatform.core.datastore.DatastoreCommodityHandler;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.datastore.beans.BeanDatastore.BeanOperationResult;
import com.holonplatform.core.datastore.transaction.Transactional;
import com.holonplatform.core.internal.datastore.beans.DefaultBeanDatastore;
import com.holonplatform.core.internal.datastore.beans.DefaultBeanOperationResult;

/**
 * A {@link Datastore} adapter which uses Java Beans as persistent data representation.
 * 
 * <p>
 * The operations data target (i.e. the persistence entity identifier) is obtained by default using the bean class
 * simple name. The {@link DataPath} annotation can be used on bean class to declare a specific data target name.
 * </p>
 * <p>
 * The bean property names are used as data model attribute names. The {@link DataPath} annotation can be used on bean
 * property fields or getter methods to specify a different data model attribute name.
 * </p>
 * <p>
 * The {@link #of(Datastore)} builder method can be used to obtain a {@link BeanDatastore} from a concrete
 * {@link Datastore} instance.
 * </p>
 *
 * @since 5.1.0
 */
public interface BeanDatastore extends
		BeanDatastoreOperations<BeanOperationResult<?>, BeanBulkInsert<?>, BeanBulkUpdate<?>, BeanBulkDelete<?>, BeanQuery<?>>,
		DatastoreCommodityHandler, ExpressionResolverSupport, DataContextBound {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastoreOperations#insert(java.lang.Object,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	<T> BeanOperationResult<T> insert(T bean, WriteOption... options);

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastoreOperations#update(java.lang.Object,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	<T> BeanOperationResult<T> update(T bean, WriteOption... options);

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastoreOperations#save(java.lang.Object,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	<T> BeanOperationResult<T> save(T bean, WriteOption... options);

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastoreOperations#delete(java.lang.Object,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	<T> BeanOperationResult<T> delete(T bean, WriteOption... options);

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastoreOperations#bulkInsert(java.lang.Class,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	<T> BeanBulkInsert<T> bulkInsert(Class<T> beanClass, WriteOption... options);

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastoreOperations#bulkUpdate(java.lang.Class,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	<T> BeanBulkUpdate<T> bulkUpdate(Class<T> beanClass, WriteOption... options);

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastoreOperations#bulkDelete(java.lang.Class,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	<T> BeanBulkDelete<T> bulkDelete(Class<T> beanClass, WriteOption... options);

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.beans.BeanDatastoreOperations#query()
	 */
	@Override
	<T> BeanQuery<T> query(Class<T> beanClass);

	// Transactions

	/**
	 * Check if the concrete {@link Datastore} is {@link Transactional}, i.e. supports execution of transactional
	 * operations.
	 * @return If the Datastore is transactional, return the Datastore as {@link Transactional}, or an empty Optional
	 *         otherwise
	 */
	Optional<Transactional> isTransactional();

	/**
	 * Requires the concrete {@link Datastore} to be {@link Transactional}, i.e. to support execution of transactional
	 * operations, throwing an {@link IllegalStateException} if this Datastore is not transactional.
	 * @return the Datastore as {@link Transactional}
	 * @throws IllegalStateException If this Datastore is not transactional
	 */
	Transactional requireTransactional();

	// ------- Builders

	/**
	 * Get a {@link BeanDatastore} adapter using given <code>datastore</code> as concrete {@link Datastore}.
	 * @param datastore The concrete {@link Datastore} to use for persistence operations (not null)
	 * @return A new {@link BeanDatastore} adapter which uses given {@link Datastore}
	 */
	static BeanDatastore of(Datastore datastore) {
		return new DefaultBeanDatastore(datastore);
	}

	// ------- Bean operation result

	/**
	 * Represents the result of a {@link BeanDatastore} operation, providing informatons about the operation outcome.
	 * 
	 * @param <T> Bean class
	 */
	public interface BeanOperationResult<T> extends Serializable {

		/**
		 * Get the executed operation type, if available.
		 * @return Executed operation type
		 */
		Optional<OperationType> getOperationType();

		/**
		 * Get the number of elements affected by the the operation execution.
		 * @return Affected elements count
		 */
		long getAffectedCount();

		/**
		 * Get the bean instance which represents the result of the operation, if available. For example, the
		 * <code>insert</code>, <code>update</code> and <code>save</code> operations should return the updated bean
		 * instance, including any generated key value if the operation semantic expects it.
		 * @return The operation result bean instance.
		 */
		Optional<T> getResult();

		// Builder

		/**
		 * Get a builder to create {@link BeanOperationResult} instances.
		 * @param <T> Bean class
		 * @return BeanOperationResult builder
		 */
		static <T> Builder<T> builder() {
			return new DefaultBeanOperationResult.DefaultBuilder<>();
		}

		/**
		 * {@link BeanOperationResult} builder.
		 * 
		 * @param <T> Bean class
		 */
		public interface Builder<T> {

			/**
			 * Set the operation type
			 * @param type Operation type
			 * @return this
			 */
			Builder<T> type(OperationType type);

			/**
			 * Set the affected elements count.
			 * @param count Affected elements count
			 * @return this
			 */
			Builder<T> affectedCount(long count);

			/**
			 * Set the result bean instance.
			 * @param bean The bean instance to set
			 * @return this
			 */
			Builder<T> result(T bean);

			/**
			 * Build the {@link BeanOperationResult}.
			 * @return The {@link BeanOperationResult} instance
			 */
			BeanOperationResult<T> build();

		}

	}

}
