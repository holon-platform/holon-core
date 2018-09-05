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

import java.util.Optional;

import com.holonplatform.core.datastore.transaction.TransactionConfiguration;
import com.holonplatform.core.datastore.transaction.TransactionOptions;

/**
 * Default {@link TransactionConfiguration} implementation.
 *
 * @since 5.1.0
 */
public class DefaultTransactionConfiguration implements TransactionConfiguration {

	private final boolean rollbackOnError;

	private final boolean autoCommit;

	private final TransactionOptions transactionOptions;

	public DefaultTransactionConfiguration(boolean rollbackOnError, boolean autoCommit,
			TransactionOptions transactionOptions) {
		super();
		this.rollbackOnError = rollbackOnError;
		this.autoCommit = autoCommit;
		this.transactionOptions = transactionOptions;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.TransactionConfiguration#isRollbackOnError()
	 */
	@Override
	public boolean isRollbackOnError() {
		return rollbackOnError;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.TransactionConfiguration#isAutoCommit()
	 */
	@Override
	public boolean isAutoCommit() {
		return autoCommit;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.transaction.TransactionConfiguration#getTransactionOptions()
	 */
	@Override
	public Optional<TransactionOptions> getTransactionOptions() {
		return Optional.ofNullable(transactionOptions);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TransactionConfiguration [rollbackOnError=" + rollbackOnError + ", autoCommit=" + autoCommit
				+ ", transactionOptions=" + transactionOptions + "]";
	}

}
