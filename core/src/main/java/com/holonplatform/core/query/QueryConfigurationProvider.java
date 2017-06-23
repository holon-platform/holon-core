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

import com.holonplatform.core.ParameterSet;

/**
 * Interface that can be used to dynamically provide query configuration elements.
 * 
 * @since 4.5.0
 */
public interface QueryConfigurationProvider {

	/**
	 * Get the query filter.
	 * @return Query filter, or <code>null</code> if none
	 */
	QueryFilter getQueryFilter();

	/**
	 * Get the query sort.
	 * <p>
	 * By default this method returns <code>null</code>.
	 * </p>
	 * @return Query sort, or <code>null</code> if none
	 */
	default QuerySort getQuerySort() {
		return null;
	}

	/**
	 * Get the query parameters.
	 * <p>
	 * By default this method returns <code>null</code>.
	 * </p>
	 * @return Query parameters, or <code>null</code> if none
	 */
	default ParameterSet getQueryParameters() {
		return null;
	}

}
