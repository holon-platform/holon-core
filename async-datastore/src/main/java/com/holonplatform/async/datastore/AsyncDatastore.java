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
package com.holonplatform.async.datastore;

import java.io.Serializable;
import java.util.concurrent.CompletionStage;

import com.holonplatform.async.datastore.operation.AsyncBulkDelete;
import com.holonplatform.async.datastore.operation.AsyncBulkInsert;
import com.holonplatform.async.datastore.operation.AsyncBulkUpdate;
import com.holonplatform.async.datastore.operation.AsyncDelete;
import com.holonplatform.async.datastore.operation.AsyncInsert;
import com.holonplatform.async.datastore.operation.AsyncQuery;
import com.holonplatform.async.datastore.operation.AsyncRefresh;
import com.holonplatform.async.datastore.operation.AsyncSave;
import com.holonplatform.async.datastore.operation.AsyncUpdate;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport;
import com.holonplatform.core.datastore.DataContextBound;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.DatastoreCommodity;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.DatastoreCommodityHandler;
import com.holonplatform.core.datastore.DatastoreOperations;
import com.holonplatform.core.exceptions.DataAccessException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;

/**
 * Asynchronous {@link DatastoreOperations} API, which can be used to execute the Datastore operations and obtain the
 * the results asynchronously, using {@link CompletionStage} as operation return type.
 * <p>
 * Extends {@link DatastoreCommodityHandler} to support {@link DatastoreCommodity} creation by type.
 * </p>
 * <p>
 * Extends {@link ExpressionResolverSupport} to allow {@link ExpressionResolver}s registration, which can be used to
 * extend and/or customize the datastore operations.
 * </p>
 * 
 * @see Datastore
 *
 * @since 5.2.0
 */
public interface AsyncDatastore extends
		DatastoreOperations<CompletionStage<OperationResult>, CompletionStage<PropertyBox>, AsyncBulkInsert, AsyncBulkUpdate, AsyncBulkDelete, AsyncQuery>,
		DatastoreCommodityHandler, ExpressionResolverSupport, DataContextBound, Serializable {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.DatastoreOperations#refresh(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.property.PropertyBox)
	 */
	@Override
	default CompletionStage<PropertyBox> refresh(DataTarget<?> target, PropertyBox propertyBox) {
		try {
			return create(AsyncRefresh.class).target(target).value(propertyBox).execute();
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
	default CompletionStage<OperationResult> insert(DataTarget<?> target, PropertyBox propertyBox,
			WriteOption... options) {
		try {
			return create(AsyncInsert.class).target(target).value(propertyBox).withWriteOptions(options)
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
	default CompletionStage<OperationResult> update(DataTarget<?> target, PropertyBox propertyBox,
			WriteOption... options) {
		try {
			return create(AsyncUpdate.class).target(target).value(propertyBox).withWriteOptions(options)
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
	default CompletionStage<OperationResult> save(DataTarget<?> target, PropertyBox propertyBox,
			WriteOption... options) {
		try {
			return create(AsyncSave.class).target(target).value(propertyBox).withWriteOptions(options)
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
	default CompletionStage<OperationResult> delete(DataTarget<?> target, PropertyBox propertyBox,
			WriteOption... options) {
		try {
			return create(AsyncDelete.class).target(target).value(propertyBox).withWriteOptions(options)
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
