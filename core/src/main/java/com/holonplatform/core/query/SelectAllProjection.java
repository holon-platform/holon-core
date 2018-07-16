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
package com.holonplatform.core.query;

import java.util.Map;

import com.holonplatform.core.internal.query.DefaultSelectAllProjection;

/**
 * A {@link QueryProjection} to obtain all the values af a persistent data entity instance.
 * <p>
 * The values are returned as {@link Map}, each element representing the entity instance attribute <em>name</em> (for
 * example a column name in a RDBMS) and the corresponding entity instance value, which can be <code>null</code> if no
 * value is available.
 * </p>
 * 
 * @since 5.2.0
 */
public interface SelectAllProjection extends QueryProjection<Map<String, Object>> {

	/**
	 * Create a new {@link SelectAllProjection}.
	 * @return A new {@link SelectAllProjection}
	 */
	static SelectAllProjection create() {
		return new DefaultSelectAllProjection();
	}

}
