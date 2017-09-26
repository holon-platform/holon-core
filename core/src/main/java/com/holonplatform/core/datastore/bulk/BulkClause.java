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
package com.holonplatform.core.datastore.bulk;

import com.holonplatform.core.Path;
import com.holonplatform.core.datastore.Datastore;

/**
 * Base interface for {@link Datastore} bulk DML clauses configuration.
 * 
 * @param <C> Concrete subtype
 * 
 * @since 5.0.0
 */
public interface BulkClause<C extends BulkClause<C>> extends DMLClause<C> {

	/**
	 * Add a value binding using a {@link Path}.
	 * @param path Path to be updated
	 * @param value value to set
	 * @param <T> Path type
	 * @return the current object
	 */
	<T> C set(Path<T> path, T value);

	/**
	 * Bind the given {@link Path} to <code>null</code>
	 * @param path Path to be updated
	 * @return the current object
	 */
	@SuppressWarnings("rawtypes")
	C setNull(Path path);

}
