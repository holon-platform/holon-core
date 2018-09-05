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
package com.holonplatform.core.internal.datastore.transaction;

import com.holonplatform.core.datastore.transaction.Transaction;
import com.holonplatform.core.datastore.transaction.TransactionStatus;

/**
 * Abstract {@link Transaction} implementation.
 *
 * @since 5.0.1
 */
public abstract class AbstractTransaction implements TransactionStatus {

	private boolean rollbackOnly = false;

	private boolean completed = false;

	private final boolean newTransaction;

	public AbstractTransaction(boolean newTransaction) {
		super();
		this.newTransaction = newTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.TransactionStatus#isNew()
	 */
	@Override
	public boolean isNew() {
		return newTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.Transaction#setRollbackOnly()
	 */
	@Override
	public void setRollbackOnly() {
		if (isCompleted()) {
			throw new IllegalTransactionStatusException(
					"Cannot mark the transaction as rollback-only: the transaction is already completed");
		}
		this.rollbackOnly = true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.Transaction#isRollbackOnly()
	 */
	@Override
	public boolean isRollbackOnly() {
		return rollbackOnly;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.Transaction#isCompleted()
	 */
	@Override
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * Set the transaction as completed.
	 */
	protected void setCompleted() {
		this.completed = true;
	}

}
