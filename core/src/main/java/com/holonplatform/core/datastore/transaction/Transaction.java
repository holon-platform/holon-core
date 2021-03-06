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

/**
 * Represents a Datastore <em>transaction</em>, providing methods to <em>commit</em> and <em>rollback</em> the
 * transaction.
 *
 * @since 5.1.0
 */
public interface Transaction extends TransactionStatus {

	/**
	 * Commit the transaction.
	 * <p>
	 * If the transaction has been marked as rollback-only, a rollback action is performed.
	 * </p>
	 * @return <code>true</code> if the transaction was actually committed, or <code>false</code> if it was rolled back
	 *         because the the transaction has been marked as rollback-only
	 * @throws IllegalTransactionStatusException If the transaction is already completed (that is, committed or rolled
	 *         back)
	 * @throws TransactionException If an error occurred during transaction commit
	 */
	boolean commit() throws TransactionException;

	/**
	 * Rollback the transaction.
	 * @throws IllegalTransactionStatusException If the transaction is already completed (that is, committed or rolled
	 *         back)
	 * @throws TransactionException If an error occurred during transaction rollback
	 */
	void rollback() throws TransactionException;

}
