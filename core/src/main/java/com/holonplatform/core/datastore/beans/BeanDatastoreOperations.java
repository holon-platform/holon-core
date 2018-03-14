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

import com.holonplatform.core.beans.DataPath;
import com.holonplatform.core.datastore.DatastoreOperations;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.datastore.bulk.BulkDeleteOperation;
import com.holonplatform.core.datastore.bulk.BulkInsertOperation;
import com.holonplatform.core.datastore.bulk.BulkUpdateOperation;
import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.query.QueryBuilder;

/**
 * Interface similar to {@link DatastoreOperations}, which provides the same methods using a bean class to represent the
 * peristent entity data and the data model attributes.
 * 
 * @param <R> Operation result type
 * @param <BI> Bulk insert operation type
 * @param <BU> Bulk update operation type
 * @param <BD> Bulk delete operation type
 * @param <Q> Query builder type
 * 
 * @since 5.1.0
 */
@SuppressWarnings("rawtypes")
public interface BeanDatastoreOperations<R, BI extends BeanBulkInsertOperation, BU extends BeanBulkUpdateOperation, BD extends BeanBulkDeleteOperation, Q extends BeanQueryBuilder> {

	/**
	 * Refresh a bean instance, updating all its properties to current value in data store.
	 * <p>
	 * The operation data target (i.e. the persistence entity identifier) is obtained by default using the bean class
	 * simple name. The {@link DataPath} annotation can be used on bean class to declare a specific data target name.
	 * </p>
	 * <p>
	 * The bean property names are used as data model attribute names. The {@link DataPath} annotation can be used on
	 * bean property fields or getter methods to specify a different data model attribute name.
	 * </p>
	 * @param <T> Bean type
	 * @param bean The bean instance to refresh (not null)
	 * @return The refreshed bean instance (not null)
	 * @throws DataAccessException If an error occurred during operation execution
	 */
	<T> T refresh(T bean);

	/**
	 * Insert a bean instance in the data store.
	 * <p>
	 * The operation data target (i.e. the persistence entity identifier) is obtained by default using the bean class
	 * simple name. The {@link DataPath} annotation can be used on bean class to declare a specific data target name.
	 * </p>
	 * <p>
	 * The bean property names are used as data model attribute names. The {@link DataPath} annotation can be used on
	 * bean property fields or getter methods to specify a different data model attribute name.
	 * </p>
	 * @param <T> Bean type
	 * @param bean The bean instance to insert (not null)
	 * @param options Optional write options. The write options are specific for each concrete implementation.
	 * @return the operation execution result
	 * @throws DataAccessException If an error occurred during operation execution
	 */
	<T> R insert(T bean, WriteOption... options);

	/**
	 * Update a bean instance in the data store.
	 * <p>
	 * The operation data target (i.e. the persistence entity identifier) is obtained by default using the bean class
	 * simple name. The {@link DataPath} annotation can be used on bean class to declare a specific data target name.
	 * </p>
	 * <p>
	 * The bean property names are used as data model attribute names. The {@link DataPath} annotation can be used on
	 * bean property fields or getter methods to specify a different data model attribute name.
	 * </p>
	 * @param <T> Bean type
	 * @param bean The bean instance to insert (not null)
	 * @param options Optional write options. The write options are specific for each concrete implementation.
	 * @return the operation execution result
	 * @throws DataAccessException If an error occurred during operation execution
	 */
	<T> R update(T bean, WriteOption... options);

	/**
	 * Save a bean instance in the data store. The <em>save</em> operation semantics is: insert data if not exists,
	 * update it otherwise.
	 * <p>
	 * The operation data target (i.e. the persistence entity identifier) is obtained by default using the bean class
	 * simple name. The {@link DataPath} annotation can be used on bean class to declare a specific data target name.
	 * </p>
	 * <p>
	 * The bean property names are used as data model attribute names. The {@link DataPath} annotation can be used on
	 * bean property fields or getter methods to specify a different data model attribute name.
	 * </p>
	 * @param <T> Bean type
	 * @param bean The bean instance to save (not null)
	 * @param options Optional write options. The write options are specific for each concrete implementation.
	 * @return the operation execution result
	 * @throws DataAccessException If an error occurred during operation execution
	 */
	<T> R save(T bean, WriteOption... options);

	/**
	 * Remove a bean instance from the data store.
	 * <p>
	 * The operation data target (i.e. the persistence entity identifier) is obtained by default using the bean class
	 * simple name. The {@link DataPath} annotation can be used on bean class to declare a specific data target name.
	 * </p>
	 * <p>
	 * The bean property names are used as data model attribute names. The {@link DataPath} annotation can be used on
	 * bean property fields or getter methods to specify a different data model attribute name.
	 * </p>
	 * @param <T> Bean type
	 * @param bean The bean instance to delete (not null)
	 * @param options Optional write options. The write options are specific for each concrete implementation.
	 * @return the operation execution result
	 * @throws DataAccessException If an error occurred during operation execution
	 */
	<T> R delete(T bean, WriteOption... options);

	/**
	 * Create a {@link BulkInsertOperation} actuator, which can be used to configure and execute a bulk
	 * <code>INSERT</code> operation.
	 * @param <T> Bean type
	 * @param beanClass The bean class to insert
	 * @param options Optional write options. The write options are specific for each concrete implementation.
	 * @return A new bulk <code>INSERT</code> operation
	 */
	<T> BI bulkInsert(Class<T> beanClass, WriteOption... options);

	/**
	 * Create a {@link BulkUpdateOperation} actuator, which can be used to configure and execute a bulk
	 * <code>UPDATE</code> operation.
	 * @param <T> Bean type
	 * @param beanClass The bean class to update
	 * @param options Optional write options. The write options are specific for each concrete implementation.
	 * @return A new bulk <code>UPDATE</code> operation
	 */
	<T> BU bulkUpdate(Class<T> beanClass, WriteOption... options);

	/**
	 * Create a {@link BulkDeleteOperation} actuator, which can be used to configure and execute a bulk
	 * <code>DELETE</code> operation.
	 * @param <T> Bean type
	 * @param beanClass The bean class to delete
	 * @param options Optional write options. The write options are specific for each concrete implementation.
	 * @return A new bulk <code>DELETE</code> operation
	 */
	<T> BD bulkDelete(Class<T> beanClass, WriteOption... options);

	/**
	 * Create a {@link QueryBuilder} actuator, which can be used to configure and execute a query on the data managed by
	 * this Datastore.
	 * @param <T> Bean type
	 * @param beanClass The bean class to query (not null)
	 * @return A new query builder
	 */
	<T> Q query(Class<T> beanClass);

}
