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
package com.holonplatform.core.datastore.transaction;

/**
 * Represents the base API for a <em>transaction</em>, providing methods to inspect and manage the transaction status.
 *
 * @since 5.2.0
 */
public interface TransactionStatus {

	/**
	 * Mark this transaction so that the only possible outcome of the transaction is for the transaction to be rolled
	 * back.
	 * @throws IllegalTransactionStatusException If the transaction is already completed (that is, committed or rolled
	 *         back)
	 */
	void setRollbackOnly();

	/**
	 * Return whether the transaction has been marked as rollback-only.
	 * @return <code>true</code> if the transaction has been marked as rollback-only, <code>false</code> otherwise
	 */
	boolean isRollbackOnly();

	/**
	 * Get whether the transaction is active, that is, whether it is started and configured.
	 * @return Whether the transaction is active
	 */
	boolean isActive();

	/**
	 * Get whether this transaction is completed, that is, whether it has already been committed or rolled back.
	 * @return <code>true</code> if the transaction is completed, <code>false</code> otherwise
	 */
	boolean isCompleted();

	/**
	 * Return whether this transaction is new.
	 * @return <code>true</code> if this is a new transaction, <code>false</code> if participating in an existing
	 *         transaction
	 * @since 5.2.0
	 */
	boolean isNew();

	// ------- Exceptions

	/**
	 * Exception related to a transaction operation error.
	 */
	public class TransactionException extends RuntimeException {

		private static final long serialVersionUID = 3726471507961983929L;

		/**
		 * Constructor with error message.
		 * @param message Error message
		 */
		public TransactionException(String message) {
			super(message);
		}

		/**
		 * Constructor with error message and nested exception.
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public TransactionException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	/**
	 * Exception thrown when an operation is performed on a transaction but the transaction is in an illegal status
	 * according to the transaction semantics.
	 */
	public class IllegalTransactionStatusException extends TransactionException {

		private static final long serialVersionUID = 955102297447646409L;

		/**
		 * Constructor with error message.
		 * @param message Error message
		 */
		public IllegalTransactionStatusException(String message) {
			super(message);
		}

		/**
		 * Constructor with error message and nested exception.
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public IllegalTransactionStatusException(String message, Throwable cause) {
			super(message, cause);
		}

	}

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
