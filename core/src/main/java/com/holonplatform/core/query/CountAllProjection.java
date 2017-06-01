/*
 * Copyright 2000-2016 Holon TDCN.
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

import com.holonplatform.core.internal.query.DefaultCountAllProjection;

/**
 * A {@link QueryProjection} to represent the count of all the results of a query.
 *
 * @since 5.0.0
 */
public interface CountAllProjection extends QueryProjection<Long> {

	/**
	 * Create a {@link CountAllProjection}.
	 * @return A new CountAllProjection
	 */
	static CountAllProjection create() {
		return new DefaultCountAllProjection();
	}

}
