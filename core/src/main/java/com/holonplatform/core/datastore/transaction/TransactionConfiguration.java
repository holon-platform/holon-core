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

import java.util.Optional;

import com.holonplatform.core.internal.datastore.transaction.DefaultTransactionConfiguration;

/**
 * {@link Transaction} configuration.
 * 
 * @since 5.1.0
 */
public interface TransactionConfiguration {

	/**
	 * Gets whether the transaction must be rolled back when an exception is thrown during a transactional operation
	 * execution.
	 * <p>
	 * Default is <code>true</code>.
	 * </p>
	 * @return <code>true</code> if the transaction must be rolled back when an exception is thrown during a
	 *         transactional operation execution, <code>false</code> otherwise
	 */
	boolean isRollbackOnError();

	/**
	 * Gets whether the transaction must be committed when a transactional operation ends and no error occurred.
	 * <p>
	 * Default is <code>false</code>.
	 * </p>
	 * @return <code>true</code> to commit the transaction when a transactional operation ends
	 */
	boolean isAutoCommit();

	/**
	 * Get the transaction isolation level to use.
	 * @return Optional transaction isolation level
	 */
	Optional<TransactionIsolation> getTransactionIsolation();

	/**
	 * Create a default {@link TransactionConfiguration} setting transaction rollback on errors to <code>true</code>.
	 */
	static TransactionConfiguration getDefault() {
		return new DefaultTransactionConfiguration(true, false, null);
	}

	/**
	 * Create a {@link TransactionConfiguration} setting transaction auto-commit to <code>true</code>.
	 * @return A new transaction configuration
	 * @see #isAutoCommit()
	 */
	static TransactionConfiguration withAutoCommit() {
		return new DefaultTransactionConfiguration(true, true, null);
	}

	/**
	 * Create a {@link TransactionConfiguration}.
	 * @param rollbackOnError Whether the transaction must be rolled back when an exception is thrown during a
	 *        transactional operation execution.
	 * @param autoCommit whether the transaction must be committed when a transactional operation ends and no error
	 *        occurred
	 * @return A new transaction configuration
	 */
	static TransactionConfiguration create(boolean rollbackOnError, boolean autoCommit) {
		return new DefaultTransactionConfiguration(rollbackOnError, autoCommit, null);
	}

	/**
	 * Create a {@link TransactionConfiguration}.
	 * @param transactionIsolation The transation isolation level
	 * @return A new transaction configuration
	 */
	static TransactionConfiguration create(TransactionIsolation transactionIsolation) {
		return new DefaultTransactionConfiguration(true, false, transactionIsolation);
	}

	/**
	 * Create a {@link TransactionConfiguration}.
	 * @param rollbackOnError Whether the transaction must be rolled back when an exception is thrown during a
	 *        transactional operation execution.
	 * @param autoCommit whether the transaction must be committed when a transactional operation ends and no error
	 *        occurred
	 * @param transactionIsolation The transation isolation level
	 * @return A new transaction configuration
	 */
	static TransactionConfiguration create(boolean rollbackOnError, boolean autoCommit,
			TransactionIsolation transactionIsolation) {
		return new DefaultTransactionConfiguration(rollbackOnError, autoCommit, transactionIsolation);
	}

}
