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
import com.holonplatform.core.exceptions.DataAccessException;

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
	 * @throws DataAccessException If an error occurred during transaction management or operation execution
	 */
	<R> R withTransaction(TransactionalOperation<R> operation, TransactionConfiguration transactionConfiguration);

	/**
	 * Execute given operation within a transaction and return a result. A {@link Transaction} reference is provided to
	 * perform <code>commit</code> and <code>rollback</code> operations.
	 * @param <R> Operation result type
	 * @param operation Operation to execute (not null)
	 * @return The operation result
	 * @throws DataAccessException If an error occurred during transaction management or operation execution
	 */
	default <R> R withTransaction(TransactionalOperation<R> operation) {
		return withTransaction(operation, TransactionConfiguration.getDefault());
	}

	/**
	 * Execute given operation within a transaction. A {@link Transaction} reference is provided to perform
	 * <code>commit</code> and <code>rollback</code> operations.
	 * @param operation Operation to execute (not null)
	 * @param transactionConfiguration Transaction configuration
	 * @throws DataAccessException If an error occurred during transaction management or operation execution
	 */
	default void withTransaction(TransactionalInvocation operation, TransactionConfiguration transactionConfiguration) {
		withTransaction((TransactionalOperation<?>) operation, transactionConfiguration);
	}

	/**
	 * Execute given operation within a transaction. A {@link Transaction} reference is provided to perform
	 * <code>commit</code> and <code>rollback</code> operations.
	 * @param operation Operation to execute (not null)
	 * @throws DataAccessException If an error occurred during transaction management or operation execution
	 */
	default void withTransaction(TransactionalInvocation operation) {
		withTransaction((TransactionalOperation<?>) operation);
	}

	/**
	 * Obtain a new {@link Transaction}. The transaction lifecycle must be managed by the caller, ensuring transaction
	 * finalization using the {@link Transaction#commit()} or {@link Transaction#rollback()} methods.
	 * @param transactionConfiguration Transaction configuration
	 * @return A new {@link Transaction}
	 * @since 5.2.0
	 */
	Transaction getTransaction(TransactionConfiguration transactionConfiguration);

	/**
	 * Obtain a new {@link Transaction} using the default transaction configuration. The transaction lifecycle must be
	 * managed by the caller, ensuring transaction finalization using the {@link Transaction#commit()} or
	 * {@link Transaction#rollback()} methods.
	 * @return A new {@link Transaction}
	 * @since 5.2.0
	 */
	default Transaction getTransaction() {
		return getTransaction(TransactionConfiguration.getDefault());
	}

	// ------- Exceptions

	/**
	 * Exception throws when transaction are not actually supported by current implementation and/or driver.
	 */
	public class TransactionNotSupportedException extends RuntimeException {

		private static final long serialVersionUID = 2584187367144720449L;

		/**
		 * Constructor with error message.
		 * @param message Error message
		 */
		public TransactionNotSupportedException(String message) {
			super(message);
		}

	}

}
