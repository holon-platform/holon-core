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
package com.holonplatform.core.internal.datastore.transaction;

import com.holonplatform.core.datastore.transaction.TransactionStatus;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Base class for transactions which delegates its operations to another transaction.
 * 
 * @param <T> Delegate transaction type
 *
 * @since 5.2.0
 */
public class AbstractDelegatedTransaction<T extends TransactionStatus> implements TransactionStatus {

	/**
	 * Delegate transaction
	 */
	private final T delegate;

	/**
	 * Constructor.
	 * @param delegate Delegate transaction (not null)
	 */
	public AbstractDelegatedTransaction(T delegate) {
		super();
		ObjectUtils.argumentNotNull(delegate, "Delegate transaction must be not null");
		this.delegate = delegate;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.TransactionStatus#isNew()
	 */
	@Override
	public boolean isNew() {
		return false;
	}

	/**
	 * Get the delegate transaction.
	 * @return the delegate transaction
	 */
	protected T getDelegate() {
		return delegate;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.TransactionStatus#setRollbackOnly()
	 */
	@Override
	public void setRollbackOnly() {
		getDelegate().setRollbackOnly();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.TransactionStatus#isRollbackOnly()
	 */
	@Override
	public boolean isRollbackOnly() {
		return getDelegate().isRollbackOnly();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.TransactionStatus#isActive()
	 */
	@Override
	public boolean isActive() {
		return getDelegate().isActive();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.TransactionStatus#isCompleted()
	 */
	@Override
	public boolean isCompleted() {
		return getDelegate().isCompleted();
	}

}
