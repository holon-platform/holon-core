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
package com.holonplatform.core.datastore.transaction;

import com.holonplatform.core.datastore.transaction.TransactionalOperation.TransactionalInvocation;

/**
 * Represents an object which is capable to manage transactions, providing methods to execute an operation
 * transactionally and using {@link Transaction} as transaction handler.
 *
 * @since 5.1.0
 */
public interface Transactional {

	/**
	 * Execute given operation within a transaction and return a result. A {@link Transaction} reference is provided to
	 * perform <code>commit</code> and <code>rollback</code> operations.
	 * @param <R> Operation result type
	 * @param operation Operation to execute (not null)
	 * @param transactionConfiguration Transaction configuration
	 * @return The operation result
	 */
	<R> R withTransaction(TransactionalOperation<R> operation, TransactionConfiguration transactionConfiguration);

	/**
	 * Execute given operation within a transaction and return a result. A {@link Transaction} reference is provided to
	 * perform <code>commit</code> and <code>rollback</code> operations.
	 * @param <R> Operation result type
	 * @param operation Operation to execute (not null)
	 * @return The operation result
	 */
	default <R> R withTransaction(TransactionalOperation<R> operation) {
		return withTransaction(operation, TransactionConfiguration.getDefault());
	}

	/**
	 * Execute given operation within a transaction. A {@link Transaction} reference is provided to perform
	 * <code>commit</code> and <code>rollback</code> operations.
	 * @param operation Operation to execute (not null)
	 * @param transactionConfiguration Transaction configuration
	 */
	default void withTransaction(TransactionalInvocation operation, TransactionConfiguration transactionConfiguration) {
		withTransaction((TransactionalOperation<?>) operation, transactionConfiguration);
	}

	/**
	 * Execute given operation within a transaction. A {@link Transaction} reference is provided to perform
	 * <code>commit</code> and <code>rollback</code> operations.
	 * @param operation Operation to execute (not null)
	 */
	default void withTransaction(TransactionalInvocation operation) {
		withTransaction((TransactionalOperation<?>) operation);
	}

}
