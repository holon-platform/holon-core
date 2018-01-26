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

import com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder;
import com.holonplatform.core.Path;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.bulk.BulkDelete;
import com.holonplatform.core.datastore.bulk.BulkInsert;
import com.holonplatform.core.datastore.bulk.BulkUpdate;
import com.holonplatform.core.datastore.transaction.Transactional;
import com.holonplatform.core.internal.datastore.DefaultOperationResult;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.Query;
import com.holonplatform.core.query.QueryAggregation;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QuerySort;

/**
 * Datastore interface represents a generic data store abstraction and provides methods to perform data manipulation
 * operations.
 * <p>
 * This is the central interface to be used for persitent data management in a platform/vendor independent way.
 * </p>
 * <p>
 * To preserve abstraction and independence from the underlying persistence context, query and persistence methods rely
 * on {@link Property} to represent data attributes and {@link PropertyBox} to transport data values int both
 * directions.
 * </p>
 * <p>
 * In addition to default data manipulation methods, a Datastore can provide a set of <em>commodities</em> which can be
 * used to perform specific data operations. The default {@link Query} Datastore commodity is provided by any Datastore
 * implementation and it is the main interface to configure and execute queries on the data model using the default and
 * implementation-independent {@link DataTarget}, {@link QueryFilter}, {@link QuerySort} and {@link QueryAggregation}
 * expressions. To obtain a {@link Query} builder, the {@link #query()} method is made available.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see Query
 * @see DatastoreCommodityFactory
 */
public interface Datastore
		extends DatastoreOperations<OperationResult, BulkInsert, BulkUpdate, BulkDelete, Query>, Serializable {

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
		return create(BulkInsert.class).target(target).operationPaths(propertySet).withWriteOptions(options);
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
		return isTransactional().orElseThrow(() -> new IllegalStateException("The Datastore is not not transactional"));
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

	// Builder

	/**
	 * Base {@link Datastore} builder.
	 * @param <D> Datastore type
	 * @param <B> Concrete builder type
	 */
	public interface Builder<D extends Datastore, B extends Builder<D, B>> extends ExpressionResolverBuilder<B> {

		/**
		 * Set the <code>data context id</code> to which the Datastore is bound.
		 * @param dataContextId The data context id to set
		 * @return this
		 */
		B dataContextId(String dataContextId);

		/**
		 * Set whether to trace Datastore operations in log.
		 * @param trace <code>true</code> to enable tracing
		 * @return this
		 */
		B traceEnabled(boolean trace);

		/**
		 * Set the datastore configuration property source to use.
		 * @param configuration Datastore configuration properties (not null)
		 * @return this
		 */
		B configuration(DatastoreConfigProperties configuration);

		/**
		 * Build the Datastore.
		 * @return Datastore instance
		 */
		D build();

	}

}
