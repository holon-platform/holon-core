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
package com.holonplatform.core.datastore;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport;
import com.holonplatform.core.Path;
import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityConfigurationException;
import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityNotAvailableException;
import com.holonplatform.core.datastore.bulk.BulkDelete;
import com.holonplatform.core.datastore.bulk.BulkInsert;
import com.holonplatform.core.datastore.bulk.BulkUpdate;
import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.internal.datastore.DefaultOperationResult;
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
public interface Datastore extends ExpressionResolverSupport, DataContextBound, Serializable {

	/**
	 * Refresh a {@link PropertyBox}, updating all its model properties to current value in datastore and using given
	 * <code>target</code> to denote persistent entity of datastore
	 * @param target {@link DataTarget} to identify data entity to refresh
	 * @param propertyBox PropertyBox to refresh
	 * @return Refreshed PropertyBox
	 * @throws DataAccessException If underlying Datastore implementation throws an Exception executing operation
	 */
	PropertyBox refresh(DataTarget<?> target, PropertyBox propertyBox);

	/**
	 * Insert a {@link PropertyBox} using given <code>target</code> to denote persistent entity of datastore.
	 * @param target {@link DataTarget} to identify data entity to insert
	 * @param propertyBox PropertyBox to insert
	 * @param options Optional write options. The write options are specific for each concrete Datastore implementation.
	 * @return the result of the operation execution
	 * @throws DataAccessException If underlying Datastore implementation throws an Exception executing operation
	 */
	OperationResult insert(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options);

	/**
	 * Update a {@link PropertyBox} using given <code>target</code> to denote persistent entity of datastore.
	 * @param target {@link DataTarget} to identify data entity to update
	 * @param propertyBox PropertyBox to update
	 * @param options Optional write options. The write options are specific for each concrete Datastore implementation.
	 * @return the result of the operation execution
	 * @throws DataAccessException If underlying Datastore implementation throws an Exception executing operation
	 */
	OperationResult update(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options);

	/**
	 * Save a {@link PropertyBox} using given <code>target</code> to denote persistent entity of datastore: insert data
	 * if not exists, update it otherwise.
	 * @param target {@link DataTarget} to identify data entity to save
	 * @param propertyBox PropertyBox to save
	 * @param options Optional write options. The write options are specific for each concrete Datastore implementation.
	 * @return the result of the operation execution
	 * @throws DataAccessException If underlying Datastore implementation throws an Exception executing operation
	 */
	OperationResult save(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options);

	/**
	 * Remove a persistent element from datastore, using given <code>target</code> to denote persistent entity to delete
	 * and given {@link PropertyBox} to provide key property values
	 * @param target {@link DataTarget} to identify data entity to delete
	 * @param propertyBox PropertyBox which contains key property values to identify element to delete
	 * @param options Optional write options. The write options are specific for each concrete Datastore implementation.
	 * @return the result of the operation execution
	 * @throws DataAccessException If underlying Datastore implementation throws an Exception executing operation
	 */
	OperationResult delete(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options);

	/**
	 * Create a {@link BulkInsert} clause for bulk INSERT operations.
	 * @param target {@link DataTarget} to identify data entity to insert
	 * @param propertySet The property set to use to perform insert operations. Only the properties contained in given
	 *        property set will be taken into account to insert the {@link PropertyBox} values
	 * @param options Optional write options. The write options are specific for each concrete Datastore implementation.
	 * @return {@link BulkInsert} clause
	 */
	BulkInsert bulkInsert(DataTarget<?> target, PropertySet<?> propertySet, WriteOption... options);

	/**
	 * Create a {@link BulkUpdate} clause for bulk UPDATE operations.
	 * @param target {@link DataTarget} to identify data entity to update
	 * @param options Optional write options. The write options are specific for each concrete Datastore implementation.
	 * @return {@link BulkUpdate} clause
	 */
	BulkUpdate bulkUpdate(DataTarget<?> target, WriteOption... options);

	/**
	 * Create a {@link BulkDelete} clause for bulk DELETE operations.
	 * @param target {@link DataTarget} to identify data entity to delete
	 * @param options Optional write options. The write options are specific for each concrete Datastore implementation.
	 * @return {@link BulkDelete} clause
	 */
	BulkDelete bulkDelete(DataTarget<?> target, WriteOption... options);

	// Commodities

	/**
	 * Get the available {@link DatastoreCommodity} types for this Datastore.
	 * @return Available {@link DatastoreCommodity} types collection, empty if none
	 */
	Collection<Class<? extends DatastoreCommodity>> getAvailableCommodities();

	/**
	 * Create a new {@link DatastoreCommodity} of given <code>commodityType</code> type.
	 * @param <C> Commodity type
	 * @param commodityType Commodity type to create (not null)
	 * @return The commodity instance
	 * @throws CommodityNotAvailableException If a commodity of the required type is not available for this Datastore
	 * @throws CommodityConfigurationException If a commodity configuration error occurred
	 */
	<C extends DatastoreCommodity> C create(Class<C> commodityType);

	/**
	 * Convenience method to create the default {@link Query} Datastore commodity, to be used to query the data store
	 * using default {@link DataTarget}, {@link QueryFilter}, {@link QuerySort} and {@link QueryAggregation}
	 * expressions.
	 * @return A new {@link Query} representation and builder
	 * @see Query
	 */
	default Query query() {
		return create(Query.class);
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
	 * Represents a <em>write</em> operation option.
	 * <p>
	 * The meaning and the available write options are specific of a concrete {@link Datastore} implementation.
	 * </p>
	 */
	public interface WriteOption {
		// marker interface
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

		// Builder

		/**
		 * Get a builder to create {@link OperationResult} instances.
		 * @return OperationResult builder
		 */
		static Builder builder() {
			return new DefaultOperationResult.DefaultBuilder();
		}

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
