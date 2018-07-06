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

import com.holonplatform.core.query.QueryAdapter;
import com.holonplatform.core.query.QueryConfiguration;

/**
 * Adapter to perform a {@link LockQuery} <code>tryLock</code> operation using the provided {@link QueryConfiguration}.
 *
 * @since 5.2.0
 */
public interface LockQueryAdapter<C extends QueryConfiguration> extends QueryAdapter<C> {

	/**
	 * Try to perform the operation using the lock mode and timeout provided by given query configuration.
	 * @param queryConfiguration Query configuration
	 * @return <code>true</code> if the lock is successfully acquired, <code>false</code> otherwise
	 */
	boolean tryLock(QueryConfiguration queryConfiguration);

}
