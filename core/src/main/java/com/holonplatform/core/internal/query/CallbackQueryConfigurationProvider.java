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
package com.holonplatform.core.internal.query;

import java.util.function.Supplier;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryConfigurationProvider;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QuerySort;

/**
 * A {@link QueryConfigurationProvider} implementation using callback suppliers to provide the query configuration
 * elements.
 *
 * @since 5.2.0
 */
public class CallbackQueryConfigurationProvider implements QueryConfigurationProvider {

	private final Supplier<QueryFilter> filterSupplier;
	private final Supplier<QuerySort> sortSupplier;
	private final Supplier<ParameterSet> parametersSupplier;

	/**
	 * Constructor.
	 * @param filterSupplier {@link QueryFilter} supplier (not null)
	 * @param sortSupplier {@link QuerySort} supplier (not null)
	 * @param parametersSupplier {@link ParameterSet} supplier (not null)
	 */
	public CallbackQueryConfigurationProvider(Supplier<QueryFilter> filterSupplier, Supplier<QuerySort> sortSupplier,
			Supplier<ParameterSet> parametersSupplier) {
		super();
		ObjectUtils.argumentNotNull(filterSupplier, "QueryFilter supplier must be not null");
		ObjectUtils.argumentNotNull(sortSupplier, "QuerySort supplier must be not null");
		ObjectUtils.argumentNotNull(parametersSupplier, "ParameterSet supplier must be not null");
		this.filterSupplier = filterSupplier;
		this.sortSupplier = sortSupplier;
		this.parametersSupplier = parametersSupplier;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfigurationProvider#getQueryFilter()
	 */
	@Override
	public QueryFilter getQueryFilter() {
		return filterSupplier.get();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfigurationProvider#getQuerySort()
	 */
	@Override
	public QuerySort getQuerySort() {
		return sortSupplier.get();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfigurationProvider#getQueryParameters()
	 */
	@Override
	public ParameterSet getQueryParameters() {
		return parametersSupplier.get();
	}

}
