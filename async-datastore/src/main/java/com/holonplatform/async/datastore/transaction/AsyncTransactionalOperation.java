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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Represents an asynchronous transactional operation execution.
 * <p>
 * The operation execution can return a result.
 * <p>
 * 
 * @param <R> Operation result type
 *
 * @since 5.2.0
 */
@FunctionalInterface
public interface AsyncTransactionalOperation<R> {

	/**
	 * Execute a transactional operation using given {@link AsyncTransaction} and return a result.
	 * @param transaction The transaction reference, which can be used to perform {@link AsyncTransaction#commit()} and
	 *        {@link AsyncTransaction#rollback()} operations
	 * @return A {@link CompletionStage} which can be used to handle the asynchronous operation outcome and the
	 *         operation result
	 */
	CompletionStage<R> execute(AsyncTransaction transaction);

	/**
	 * Represents a asynchronous transactional operation execution which do not return a result.
	 */
	@FunctionalInterface
	public interface AsyncTransactionalInvocation extends AsyncTransactionalOperation<Void> {

		/**
		 * Execute a transactional operation using given {@link AsyncTransaction}.
		 * @param transaction The transaction reference, which can be used to perform {@link AsyncTransaction#commit()}
		 *        and {@link AsyncTransaction#rollback()} operations
		 * @return A {@link CompletionStage} which can be used to handle the asynchronous operation outcome
		 */
		void executeInTransaction(AsyncTransaction transaction);

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.async.datastore.transaction.AsyncTransactionalOperation#execute(com.holonplatform.async.
		 * datastore.transaction.AsyncTransaction)
		 */
		@Override
		default CompletionStage<Void> execute(AsyncTransaction transaction) {
			executeInTransaction(transaction);
			return CompletableFuture.completedFuture(null);
		}

	}

}
