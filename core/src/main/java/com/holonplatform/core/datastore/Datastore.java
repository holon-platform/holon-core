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
package com.holonplatform.core.datastore;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport;
import com.holonplatform.core.Path;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.bulk.BulkDelete;
import com.holonplatform.core.datastore.bulk.BulkInsert;
import com.holonplatform.core.datastore.bulk.BulkUpdate;
import com.holonplatform.core.datastore.operation.Delete;
import com.holonplatform.core.datastore.operation.Insert;
import com.holonplatform.core.datastore.operation.Refresh;
import com.holonplatform.core.datastore.operation.Save;
import com.holonplatform.core.datastore.operation.Update;
import com.holonplatform.core.datastore.transaction.Transactional;
import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.internal.datastore.DefaultOperationResult;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.Query;

/**
 * Datastore interface represents a generic data store abstraction and provides methods to perform data manipulation
 * operations.
 * <p>
 * This is the central interface to be used for persitent data management in a platform/vendor independent way.
 * </p>
 * <p>
 * To preserve abstraction and independence from the underlying persistence context, query and persistence methods rely
 * on {@link Property} to represent data attributes and {@link PropertyBox} to transport data values in both directions.
 * </p>
 * <p>
 * Extends {@link DatastoreCommodityHandler} to support {@link DatastoreCommodity} creation by type.
 * </p>
 * <p>
 * Extends {@link ExpressionResolverSupport} to allow {@link ExpressionResolver}s registration, which can be used to
 * extend and/or customize the datastore operations.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see DatastoreCommodityFactory
 */
public interface Datastore
		extends DatastoreOperations<OperationResult, PropertyBox, BulkInsert, BulkUpdate, BulkDelete, Query>,
		ConfigurableDatastore, Serializable {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#refresh(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.property.PropertyBox)
	 */
	@Override
	default PropertyBox refresh(DataTarget<?> target, PropertyBox propertyBox) {
		try {
			return create(Refresh.class).target(target).value(propertyBox).execute();
		} catch (Exception e) {
			throw new DataAccessException("REFRESH operation failed", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#insert(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.property.PropertyBox, com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	default OperationResult insert(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options) {
		try {
			return create(Insert.class).target(target).value(propertyBox).withWriteOptions(options).execute();
		} catch (Exception e) {
			throw new DataAccessException("INSERT operation failed", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#update(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.property.PropertyBox, com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	default OperationResult update(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options) {
		try {
			return create(Update.class).target(target).value(propertyBox).withWriteOptions(options).execute();
		} catch (Exception e) {
			throw new DataAccessException("UPDATE operation failed", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#save(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.property.PropertyBox, com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	default OperationResult save(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options) {
		try {
			return create(Save.class).target(target).value(propertyBox).withWriteOptions(options).execute();
		} catch (Exception e) {
			throw new DataAccessException("SAVE operation failed", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#delete(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.property.PropertyBox, com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	default OperationResult delete(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options) {
		try {
			return create(Delete.class).target(target).value(propertyBox).withWriteOptions(options).execute();
		} catch (Exception e) {
			throw new DataAccessException("DELETE operation failed", e);
		}
	}

	/**
	 * Create a {@link BulkInsert} operation, which can be used to configure and execute a bulk <code>INSERT</code>
	 * operation.
	 * @param target {@link DataTarget} to identify the data entity to insert (not null)
	 * @param propertySet The property set to use to perform insert operations. Only the properties contained in given
	 *        property set will be taken into account to insert the {@link PropertyBox} values
	 * @param options Optional write options. The write options are specific for each concrete {@link Datastore}
	 *        implementation.
	 * @return A new {@link BulkInsert} operation
	 */
	@Override
	default BulkInsert bulkInsert(DataTarget<?> target, PropertySet<?> propertySet, WriteOption... options) {
		return create(BulkInsert.class).target(target).propertySet(propertySet).withWriteOptions(options);
	}

	/**
	 * Create a {@link BulkUpdate} operation, which can be used to configure and execute a bulk <code>UPDATE</code>
	 * operation.
	 * @param target {@link DataTarget} to identify the data entity to update (not null)
	 * @param options Optional write options. The write options are specific for each concrete {@link Datastore}
	 *        implementation.
	 * @return A new {@link BulkUpdate} operation
	 */
	@Override
	default BulkUpdate bulkUpdate(DataTarget<?> target, WriteOption... options) {
		return create(BulkUpdate.class).target(target).withWriteOptions(options);
	}

	/**
	 * Create a {@link BulkDelete} operation, which can be used to configure and execute a bulk <code>DELETE</code>
	 * operation.
	 * @param target {@link DataTarget} to identify the data entity to delete (not null)
	 * @param options Optional write options. The write options are specific for each concrete {@link Datastore}
	 *        implementation.
	 * @return A new {@link BulkDelete} operation
	 */
	@Override
	default BulkDelete bulkDelete(DataTarget<?> target, WriteOption... options) {
		return create(BulkDelete.class).target(target).withWriteOptions(options);
	}

	/**
	 * Create a {@link Query} commodity, which can be used to configure and execute a query on the data managed by this
	 * Datastore.
	 * @return A new {@link Query} instance, which can be used to configure and execute a query
	 */
	@Override
	default Query query() {
		return create(Query.class);
	}

	/**
	 * Create a {@link Query} commodity, setting given <code>target</code> as query data target.
	 * @param target Query data target (not null)
	 * @return A new {@link Query} instance, which can be used to configure and execute a query
	 */
	default Query query(DataTarget<?> target) {
		ObjectUtils.argumentNotNull(target, "Query target must be not null");
		return query().target(target);
	}

	// Transactions

	/**
	 * Check if this Datastore is {@link Transactional}, i.e. supports execution of transactional operations.
	 * @return If this Datastore is transactional, return the Datastore as {@link Transactional}, or an empty Optional
	 *         otherwise
	 */
	default Optional<Transactional> isTransactional() {
		return Optional.ofNullable((this instanceof Transactional) ? (Transactional) this : null);
	}

	/**
	 * Requires this Datastore to be {@link Transactional}, i.e. to support execution of transactional operations,
	 * throwing an {@link IllegalStateException} if this Datastore is not transactional.
	 * @return the Datastore as {@link Transactional}
	 * @throws IllegalStateException If this Datastore is not transactional
	 */
	default Transactional requireTransactional() {
		return isTransactional().orElseThrow(() -> new IllegalStateException("The Datastore is not transactional"));
	}

	// Operations

	/**
	 * Enumeration of {@link Datastore} operation types.
	 */
	public enum OperationType {

		/**
		 * Insert operation
		 */
		INSERT,

		/**
		 * Update operation
		 */
		UPDATE,

		/**
		 * Delete operation
		 */
		DELETE

	}

	/**
	 * Represents the result of a {@link Datastore} operation, providing informatons about the operation outcome.
	 */
	public interface OperationResult extends Serializable {

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
		 * For {@link OperationType#INSERT} operations, returns the inserted key values, if the concrete
		 * {@link Datastore} implementation supports this feature. The returned {@link Map} key set is composed by the
		 * key {@link Path}s, associated with their values.
		 * <p>
		 * This method can be used, for example, to obtain the auto-generated key values, if the concrete persistence
		 * store supports this feature.
		 * </p>
		 * <p>
		 * The key value type is a generic {@link Object}, it is up to the caller to convert the value to the expected
		 * actual type.
		 * </p>
		 * @return Map of the insert keys {@link Path}s and their values, if available. An empty map if the operation is
		 *         not a {@link OperationType#INSERT} type operation or if the concrete {@link Datastore} implementation
		 *         does not support this feature
		 */
		Map<Path<?>, Object> getInsertedKeys();

		/**
		 * For {@link OperationType#INSERT} operations, returns the inserted key value which correspond to given
		 * <code>path</code> name, if available.
		 * @param <T> Key type
		 * @param path Path for which to obtain the inserted key value
		 * @return Optional inserted key value
		 * @throws ClassCastException If the key value type is not compatible with the path type
		 */
		<T> Optional<T> getInsertedKey(Path<T> path);

		/**
		 * Get the first inserted key value, if available.
		 * @return Optional first inserted key value
		 * @since 5.1.0
		 */
		default Optional<Object> getFirstInsertedKey() {
			return Optional.ofNullable(getInsertedKeys().keySet().iterator().next()).map(p -> getInsertedKeys().get(p));
		}

		/**
		 * Get the first inserted key value given target type, if available.
		 * @param <T> Expected value type
		 * @param targetType Expected value type (not null)
		 * @return Optional first inserted key value
		 * @since 5.1.0
		 */
		default <T> Optional<T> getFirstInsertedKey(Class<T> targetType) {
			return getFirstInsertedKey().map(k -> ConversionUtils.convert(k, targetType));
		}

		// Builder

		/**
		 * Get a builder to create {@link OperationResult} instances.
		 * @return OperationResult builder
		 */
		static Builder builder() {
			return new DefaultOperationResult.DefaultBuilder();
		}

		/**
		 * {@link OperationResult} builder.
		 */
		public interface Builder {

			/**
			 * Set the operation type
			 * @param type Operation type
			 * @return this
			 */
			Builder type(OperationType type);

			/**
			 * Set the affected elements count.
			 * @param count Affected elements count
			 * @return this
			 */
			Builder affectedCount(long count);

			/**
			 * Add an inserted key
			 * @param key Key path (not null)
			 * @param value Key value
			 * @return this
			 */
			Builder withInsertedKey(Path<?> key, Object value);

			/**
			 * Build the {@link OperationResult}.
			 * @return OperationResult instance
			 */
			OperationResult build();

		}

	}

}
