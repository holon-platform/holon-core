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
package com.holonplatform.core.datastore.async;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

import com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport;
import com.holonplatform.core.datastore.DataContextBound;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.DatastoreCommodityHandler;
import com.holonplatform.core.datastore.DatastoreOperations;
import com.holonplatform.core.datastore.async.bulk.AsyncBulkDelete;
import com.holonplatform.core.datastore.async.bulk.AsyncBulkInsert;
import com.holonplatform.core.datastore.async.bulk.AsyncBulkUpdate;
import com.holonplatform.core.datastore.async.operation.AsyncDeleteOperation;
import com.holonplatform.core.datastore.async.operation.AsyncInsertOperation;
import com.holonplatform.core.datastore.async.operation.AsyncRefreshOperation;
import com.holonplatform.core.datastore.async.operation.AsyncSaveOperation;
import com.holonplatform.core.datastore.async.operation.AsyncUpdateOperation;
import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.async.AsyncQuery;

/**
 * Asynchronous {@link DatastoreOperations} API, which can be used to execute the Datastore operations and obtain the
 * the operation results asynchronously.
 * <p>
 * The {@link CompletableFuture} API is used to provide the asynchronous operation results.
 * </p>
 *
 * @since 5.2.0
 */
public interface AsyncDatastore extends
		DatastoreOperations<CompletableFuture<OperationResult>, CompletableFuture<PropertyBox>, AsyncBulkInsert, AsyncBulkUpdate, AsyncBulkDelete, AsyncQuery>,
		DatastoreCommodityHandler, ExpressionResolverSupport, DataContextBound, Serializable {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#refresh(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.property.PropertyBox)
	 */
	@Override
	default CompletableFuture<PropertyBox> refresh(DataTarget<?> target, PropertyBox propertyBox) {
		try {
			return create(AsyncRefreshOperation.class).target(target).value(propertyBox).execute();
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
	default CompletableFuture<OperationResult> insert(DataTarget<?> target, PropertyBox propertyBox,
			WriteOption... options) {
		try {
			return create(AsyncInsertOperation.class).target(target).value(propertyBox).withWriteOptions(options)
					.execute();
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
	default CompletableFuture<OperationResult> update(DataTarget<?> target, PropertyBox propertyBox,
			WriteOption... options) {
		try {
			return create(AsyncUpdateOperation.class).target(target).value(propertyBox).withWriteOptions(options)
					.execute();
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
	default CompletableFuture<OperationResult> save(DataTarget<?> target, PropertyBox propertyBox,
			WriteOption... options) {
		try {
			return create(AsyncSaveOperation.class).target(target).value(propertyBox).withWriteOptions(options)
					.execute();
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
	default CompletableFuture<OperationResult> delete(DataTarget<?> target, PropertyBox propertyBox,
			WriteOption... options) {
		try {
			return create(AsyncDeleteOperation.class).target(target).value(propertyBox).withWriteOptions(options)
					.execute();
		} catch (Exception e) {
			throw new DataAccessException("DELETE operation failed", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#bulkInsert(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.property.PropertySet, com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	default AsyncBulkInsert bulkInsert(DataTarget<?> target, PropertySet<?> propertySet, WriteOption... options) {
		return create(AsyncBulkInsert.class).target(target).operationPaths(propertySet).withWriteOptions(options);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#bulkUpdate(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	default AsyncBulkUpdate bulkUpdate(DataTarget<?> target, WriteOption... options) {
		return create(AsyncBulkUpdate.class).target(target).withWriteOptions(options);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#bulkDelete(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.datastore.DatastoreOperations.WriteOption[])
	 */
	@Override
	default AsyncBulkDelete bulkDelete(DataTarget<?> target, WriteOption... options) {
		return create(AsyncBulkDelete.class).target(target).withWriteOptions(options);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#query()
	 */
	@Override
	default AsyncQuery query() {
		return create(AsyncQuery.class);
	}

	/**
	 * Create an {@link AsyncQuery} commodity, setting given <code>target</code> as query data target.
	 * @param target Query data target (not null)
	 * @return A new {@link AsyncQuery} instance, which can be used to configure and execute an asynchronous query
	 */
	default AsyncQuery query(DataTarget<?> target) {
		ObjectUtils.argumentNotNull(target, "Query target must be not null");
		return query().target(target);
	}

}
