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
 * Transaction isolation levels.
 *
 * @since 5.1.0
 */
public enum TransactionIsolation {

	/**
	 * Implements dirty read, or isolation level 0 locking, which means that no shared locks are issued and no exclusive
	 * locks are honored. When this option is set, it is possible to read uncommitted or dirty data; values in the data
	 * can be changed and rows can appear or disappear in the data set before the end of the transaction. This is the
	 * least restrictive of the four isolation levels.
	 */
	READ_UNCOMMITTED(1),

	/**
	 * Specifies that shared locks are held while the data is being read to avoid dirty reads, but the data can be
	 * changed before the end of the transaction, resulting in nonrepeatable reads or phantom data.
	 */
	READ_COMMITTED(2),

	/**
	 * Locks are placed on all data that is used in a query, preventing other users from updating the data, but new
	 * phantom rows can be inserted into the data set by another user and are included in later reads in the current
	 * transaction. Because concurrency is lower than the default isolation level, use this option only when necessary.
	 */
	REPEATABLE_READ(4),

	/**
	 * Places a range lock on the data set, preventing other users from updating or inserting rows into the data set
	 * until the transaction is complete. This is the most restrictive of the four isolation levels. Because concurrency
	 * is lower, use this option only when necessary.
	 */
	SERIALIZABLE(8),

	/**
	 * For engines that support it, none isolation means that each statement would essentially be its own transaction.
	 */
	NONE(0);

	/**
	 * Connection transaction isolation level
	 */
	private final int level;

	private TransactionIsolation(int level) {
		this.level = level;
	}

	/**
	 * Get transaction isolation level.
	 * @return the transaction isolation level
	 */
	public int getLevel() {
		return level;
	}

}
