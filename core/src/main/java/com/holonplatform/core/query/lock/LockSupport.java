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
package com.holonplatform.core.query.lock;

import com.holonplatform.core.datastore.Datastore;

/**
 * Provides lock support for suitable {@link Datastore} operations.
 *
 * @since 5.2.0
 * 
 * @see LockQuery
 */
public interface LockSupport<L extends LockSupport<L>> {

	/**
	 * Configure the lock mode and lock timeout, if supported by the concrete implementation.
	 * @param lockMode Lock mode (not null)
	 * @param timeout Lock timeout in milliseconds, <code>-1</code> for default
	 * @return The parent builder
	 */
	L lock(LockMode lockMode, long timeout);

	/**
	 * Configure the lock mode.
	 * @param lockMode Lock mode (not null)
	 * @return The parent builder
	 */
	default L lock(LockMode lockMode) {
		return lock(lockMode, -1);
	}

	/**
	 * Set the lock mode as {@link LockMode#getDefault()} and configure the lock timeout, if supported by the concrete
	 * implementation.
	 * @param timeout Lock timeout in milliseconds, <code>-1</code> for default
	 * @return The parent builder
	 */
	default L lock(long timeout) {
		return lock(LockMode.getDefault(), timeout);
	}

	/**
	 * Set the lock mode as {@link LockMode#getDefault()} and the lock timeout as the default value.
	 * @return The parent builder
	 */
	default L lock() {
		return lock(LockMode.getDefault(), -1);
	}

	/**
	 * Try to perform the operation using the given lock mode and timeout.
	 * @param lockMode Lock mode (not null)
	 * @param timeout Lock timeout in milliseconds, <code>-1</code> for default
	 * @return <code>true</code> if the lock is successfully acquired, <code>false</code> otherwise
	 */
	boolean tryLock(LockMode lockMode, long timeout);

	/**
	 * Try to perform the operation using the given lock mode.
	 * @param lockMode Lock mode (not null)
	 * @return <code>true</code> if the lock is successfully acquired, <code>false</code> otherwise
	 */
	default boolean tryLock(LockMode lockMode) {
		return tryLock(lockMode, -1);
	}

	/**
	 * Try to perform the operation using the default lock mode and given timeout.
	 * @param timeout Lock timeout in milliseconds, <code>-1</code> for default
	 * @return <code>true</code> if the lock is successfully acquired, <code>false</code> otherwise
	 * @see LockMode#getDefault()
	 */
	default boolean tryLock(long timeout) {
		return tryLock(LockMode.getDefault(), timeout);
	}

	/**
	 * Try to perform the operation using the default lock mode.
	 * @return <code>true</code> if the lock is successfully acquired, <code>false</code> otherwise
	 * @see LockMode#getDefault()
	 */
	default boolean tryLock() {
		return tryLock(LockMode.getDefault(), -1);
	}

}
