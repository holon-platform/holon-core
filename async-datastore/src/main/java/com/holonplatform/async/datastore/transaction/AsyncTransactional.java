/*
 * Copyright 2016-2018 Axioma srl.
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
package com.holonplatform.async.datastore.transaction;

import java.util.concurrent.CompletionStage;

import com.holonplatform.async.datastore.transaction.AsyncTransactionalOperation.AsyncTransactionalInvocation;
import com.holonplatform.core.datastore.transaction.TransactionConfiguration;

/**
 * Represents an object which is capable to manage asynchronous transactions, providing methods to execute an operation
 * transactionally and using {@link AsyncTransaction} as transaction handler.
 *
 * @since 5.2.0
 */
public interface AsyncTransactional {

	/**
	 * Execute given operation within a transaction and return a result. An {@link AsyncTransaction} reference is
	 * provided to perform <code>commit</code> and <code>rollback</code> operations.
	 * @param <R> Operation result type
	 * @param operation Operation to execute (not null)
	 * @param transactionConfiguration Transaction configuration
	 * @return A {@link CompletionStage} which can be used to handle the asynchronous operation outcome and the
	 *         operation result
	 */
	<R> CompletionStage<R> withTransaction(AsyncTransactionalOperation<R> operation,
			TransactionConfiguration transactionConfiguration);

	/**
	 * Execute given operation within a transaction and return a result. An {@link AsyncTransaction} reference is
	 * provided to perform <code>commit</code> and <code>rollback</code> operations.
	 * @param <R> Operation result type
	 * @param operation Operation to execute (not null)
	 * @return A {@link CompletionStage} which can be used to handle the asynchronous operation outcome and the
	 *         operation result
	 */
	default <R> CompletionStage<R> withTransaction(AsyncTransactionalOperation<R> operation) {
		return withTransaction(operation, TransactionConfiguration.getDefault());
	}

	/**
	 * Execute given operation within a transaction. An {@link AsyncTransaction} reference is provided to perform
	 * <code>commit</code> and <code>rollback</code> operations.
	 * @param operation Operation to execute (not null)
	 * @param transactionConfiguration Transaction configuration
	 */
	default void withTransaction(AsyncTransactionalInvocation operation,
			TransactionConfiguration transactionConfiguration) {
		withTransaction((AsyncTransactionalOperation<?>) operation, transactionConfiguration);
	}

	/**
	 * Execute given operation within a transaction. An {@link AsyncTransaction} reference is provided to perform
	 * <code>commit</code> and <code>rollback</code> operations.
	 * @param operation Operation to execute (not null)
	 */
	default void withTransaction(AsyncTransactionalInvocation operation) {
		withTransaction((AsyncTransactionalOperation<?>) operation);
	}

}
