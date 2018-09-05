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

import com.holonplatform.core.datastore.transaction.TransactionStatus;

/**
 * Represents an asynchronous Datastore <em>transaction</em>, providing methods to <em>commit</em> and <em>rollback</em>
 * the transaction.
 *
 * @since 5.2.0
 */
public interface AsyncTransaction extends TransactionStatus {

	/**
	 * Commit the transaction.
	 * <p>
	 * If the transaction has been marked as rollback-only, a rollback action is performed.
	 * </p>
	 * @return A {@link CompletionStage} which can be used to handle the asynchronous operation outcome. The stage
	 *         result will be <code>true</code> if the transaction was actually committed, or <code>false</code> if it
	 *         was rolled back because the the transaction has been marked as rollback-only
	 * @throws IllegalTransactionStatusException If the transaction is already completed (that is, committed or rolled
	 *         back)
	 * @throws TransactionException If an error occurred during transaction commit
	 */
	CompletionStage<Boolean> commit();

	/**
	 * Rollback the transaction.
	 * @return A {@link CompletionStage} which can be used to handle the asynchronous operation outcome. No stage result
	 *         value is expected.
	 * @throws IllegalTransactionStatusException If the transaction is already completed (that is, committed or rolled
	 *         back)
	 * @throws TransactionException If an error occurred during transaction rollback
	 */
	CompletionStage<Void> rollback();

}
