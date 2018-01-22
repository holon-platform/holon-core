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

import java.util.Collection;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport;
import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityConfigurationException;
import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityNotAvailableException;
import com.holonplatform.core.datastore.bulk.BulkDeleteOperation;
import com.holonplatform.core.datastore.bulk.BulkInsertOperation;
import com.holonplatform.core.datastore.bulk.BulkUpdateOperation;
import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.QueryBuilder;

/**
 * Base interface wich represents a generic data store abstraction and provides methods to perform data manipulation
 * operations, enabling persistent data management in a platform/vendor independent way.
 * <p>
 * To preserve abstraction and independence from the underlying persistence context, query and persistence methods rely
 * on the {@link Property} abstraction to represent data attributes and on the {@link PropertyBox} data container type
 * to transport data values in both directions.
 * </p>
 * <p>
 * In addition to default data manipulation methods, a set of <em>commodities</em> can be registered and used to perform
 * specific data operations. See specific {@link DatastoreOperations} extensions to learn how to register and handle a
 * commodity.
 * </p>
 * <p>
 * Extends {@link ExpressionResolverSupport} to allow {@link ExpressionResolver}s registration, which can be used to
 * extend and/or customize the datastore operations.
 * </p>
 *
 * @since 5.1.0
 */
public interface DatastoreOperations<R, BI extends BulkInsertOperation<BI>, BU extends BulkUpdateOperation<BU>, BD extends BulkDeleteOperation<BD>, Q extends QueryBuilder<Q>>
		extends ExpressionResolverSupport, DataContextBound {

	/**
	 * Refresh a {@link PropertyBox}, updating all its model properties to current value in data store and using given
	 * <code>target</code> to denote the data store persistent entity to use.
	 * @param target {@link DataTarget} to declare the data store persistent entity from which data has to be refreshed
	 * @param propertyBox The data to refresh, represented using a {@link PropertyBox} (not null)
	 * @return The refreshed PropertyBox (not null)
	 * @throws DataAccessException If an error occurred during operation execution
	 */
	PropertyBox refresh(DataTarget<?> target, PropertyBox propertyBox);

	/**
	 * Insert a {@link PropertyBox} in the data store, using given <code>target</code> to denote the data store
	 * persistent entity into which the data has to be inserted.
	 * @param target {@link DataTarget} to declare the data store persistent entity into which the data has to be
	 *        inserted (not null)
	 * @param propertyBox The data to insert, represented using a {@link PropertyBox} (not null)
	 * @param options Optional write options. The write options are specific for each concrete
	 *        {@link DatastoreOperations} implementation.
	 * @return the operation execution result
	 * @throws DataAccessException If an error occurred during operation execution
	 */
	R insert(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options);

	/**
	 * Update a {@link PropertyBox} in the data store, using given <code>target</code> to denote the data store
	 * persistent entity into which the data has to be updated.
	 * @param target {@link DataTarget} to declare the data store persistent entity into which the data has to be
	 *        updated (not null)
	 * @param propertyBox The data to update, represented using a {@link PropertyBox} (not null)
	 * @param options Optional write options. The write options are specific for each concrete
	 *        {@link DatastoreOperations} implementation.
	 * @return the operation execution result
	 * @throws DataAccessException If an error occurred during operation execution
	 */
	R update(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options);

	/**
	 * Save a {@link PropertyBox} in the data store, using given <code>target</code> to denote the data store persistent
	 * entity into which the data has to be saved. The <em>save</em> operation semantics is: insert data if not exists,
	 * update it otherwise.
	 * @param target {@link DataTarget} to declare the data store persistent entity into which the data has to be saved
	 *        (not null)
	 * @param propertyBox The data to save, represented using a {@link PropertyBox} (not null)
	 * @param options Optional write options. The write options are specific for each concrete
	 *        {@link DatastoreOperations} implementation.
	 * @return the operation execution result
	 * @throws DataAccessException If an error occurred during operation execution
	 */
	R save(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options);

	/**
	 * Remove a {@link PropertyBox} from the data store, using given <code>target</code> to denote the data store
	 * persistent entity from which the data has to be deleted.
	 * @param target {@link DataTarget} to declare the data store persistent entity from which the data has to be
	 *        deleted (not null)
	 * @param propertyBox The data to delete, represented using a {@link PropertyBox} (not null)
	 * @param options Optional write options. The write options are specific for each concrete
	 *        {@link DatastoreOperations} implementation.
	 * @return the operation execution result
	 * @throws DataAccessException If an error occurred during operation execution
	 */
	R delete(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options);

	/**
	 * Create a {@link BulkInsertOperation} actuator, which can be used to configure and execute a bulk
	 * <code>INSERT</code> operation.
	 * @param target {@link DataTarget} to identify data entity to insert (not null)
	 * @param propertySet The property set to use to perform insert operations. Only the properties contained in given
	 *        property set will be taken into account to insert the {@link PropertyBox} values
	 * @param options Optional write options. The write options are specific for each concrete
	 *        {@link DatastoreOperations} implementation.
	 * @return A new bulk <code>INSERT</code> operation
	 */
	BI bulkInsert(DataTarget<?> target, PropertySet<?> propertySet, WriteOption... options);

	/**
	 * Create a {@link BulkUpdateOperation} actuator, which can be used to configure and execute a bulk
	 * <code>UPDATE</code> operation.
	 * @param target {@link DataTarget} to identify data entity to update (not null)
	 * @param options Optional write options. The write options are specific for each concrete
	 *        {@link DatastoreOperations} implementation.
	 * @return A new bulk <code>UPDATE</code> operation
	 */
	BU bulkUpdate(DataTarget<?> target, WriteOption... options);

	/**
	 * Create a {@link BulkDeleteOperation} actuator, which can be used to configure and execute a bulk
	 * <code>DELETE</code> operation.
	 * @param target {@link DataTarget} to identify data entity to delete (not null)
	 * @param options Optional write options. The write options are specific for each concrete
	 *        {@link DatastoreOperations} implementation.
	 * @return A new bulk <code>DELETE</code> operation
	 */
	BD bulkDelete(DataTarget<?> target, WriteOption... options);

	/**
	 * Create a {@link QueryBuilder} actuator, which can be used to configure and execute a query on the data managed by
	 * this {@link Datastore}.
	 * @return A new query builder
	 */
	Q query();

	// Commodities

	/**
	 * Get the available {@link DatastoreCommodity} types. A registered {@link DatastoreCommodity} can be created using
	 * the {@link #create(Class)} method.
	 * @return Available {@link DatastoreCommodity} types collection, empty if none
	 */
	Collection<Class<? extends DatastoreCommodity>> getAvailableCommodities();

	/**
	 * Create a new {@link DatastoreCommodity} of given <code>commodityType</code> type.
	 * <p>
	 * Available commodity types can be obtained using {@link #getAvailableCommodities()}.
	 * </p>
	 * @param <C> Commodity type
	 * @param commodityType The commodity type to create (not null)
	 * @return The commodity instance
	 * @throws CommodityNotAvailableException If a commodity of the required type is not available
	 * @throws CommodityConfigurationException If a commodity configuration error occurred
	 */
	<C extends DatastoreCommodity> C create(Class<C> commodityType);

	// ------- types

	/**
	 * Represents a <em>write</em> operation option.
	 * <p>
	 * The meaning and the available write options are specific of each concrete implementation.
	 * </p>
	 */
	public interface WriteOption {
		// marker interface
	}

}
