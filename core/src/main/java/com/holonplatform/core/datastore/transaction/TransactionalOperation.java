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
 * Represents a transactional operation execution.
 * <p>
 * The operation execution can return a result.
 * <p>
 * 
 * @param <R> Operation result type
 *
 * @since 5.1.0
 */
@FunctionalInterface
public interface TransactionalOperation<R> {

	/**
	 * Execute a transactional operation using given {@link Transaction} and return a result.
	 * @param transaction The transaction reference, which can be used to perform {@link Transaction#commit()} and
	 *        {@link Transaction#rollback()} operations
	 * @return The result of the operation
	 * @throws Exception If an execution error occurred
	 */
	R execute(Transaction transaction) throws Exception;

	/**
	 * Represents a transactional operation execution which do not return a result.
	 */
	public interface TransactionalInvocation extends TransactionalOperation<Void> {

		/**
		 * Execute a transactional operation using given {@link Transaction}.
		 * @param transaction The transaction reference, which can be used to perform {@link Transaction#commit()} and
		 *        {@link Transaction#rollback()} operations
		 * @throws Exception If an execution error occurred
		 */
		void executeInTransaction(Transaction transaction) throws Exception;

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.datastore.transaction.TransactionalOperation#execute(com.holonplatform.core.datastore.
		 * transaction.Transaction)
		 */
		@Override
		default Void execute(Transaction transaction) throws Exception {
			executeInTransaction(transaction);
			return null;
		}

	}

}
