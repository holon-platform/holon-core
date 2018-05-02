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

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.internal.DefaultParameterSet;
import com.holonplatform.core.internal.MutableParameterSet;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryConfigurationProvider;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QuerySort;

/**
 * Default {@link QueryConfigurationProvider} implementation.
 *
 * @since 5.2.0
 */
public class DefaultQueryConfigurationProvider implements QueryConfigurationProvider {

	/*
	 * Query filter
	 */
	protected QueryFilter filter;

	/*
	 * Query sort
	 */
	protected QuerySort sort;

	/*
	 * Query parameters
	 */
	protected MutableParameterSet parameters;

	/**
	 * Add a {@link QueryFilter}.
	 * @param filter the filter to add
	 */
	protected void addFilter(QueryFilter filter) {
		ObjectUtils.argumentNotNull(filter, "QueryFilter must be not null");
		if (this.filter == null) {
			this.filter = filter;
		} else {
			this.filter = this.filter.and(filter);
		}
	}

	/**
	 * Add a {@link QuerySort}.
	 * @param sort the sort to add
	 */
	protected void addSort(QuerySort sort) {
		ObjectUtils.argumentNotNull(sort, "QuerySort must be not null");
		if (this.sort == null) {
			this.sort = sort;
		} else {
			this.sort = this.sort.and(sort);
		}
	}

	/**
	 * Add a query parameter.
	 * @param name Parameter name (not null)
	 * @param value Parameter value
	 */
	protected void addParameter(String name, Object value) {
		ObjectUtils.argumentNotNull(name, "Parameter name must be not null");
		if (this.parameters == null) {
			this.parameters = new DefaultParameterSet();
		}
		this.parameters.addParameter(name, value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfigurationProvider#getQueryFilter()
	 */
	@Override
	public QueryFilter getQueryFilter() {
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfigurationProvider#getQuerySort()
	 */
	@Override
	public QuerySort getQuerySort() {
		return sort;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryConfigurationProvider#getQueryParameters()
	 */
	@Override
	public ParameterSet getQueryParameters() {
		return parameters;
	}

	/**
	 * Default {@link Builder}.
	 */
	public static class DefaultBuilder implements Builder {

		private final DefaultQueryConfigurationProvider instance = new DefaultQueryConfigurationProvider();

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.query.QueryConfigurationProvider.Builder#filter(com.holonplatform.core.query.
		 * QueryFilter)
		 */
		@Override
		public Builder filter(QueryFilter filter) {
			instance.addFilter(filter);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.query.QueryConfigurationProvider.Builder#sort(com.holonplatform.core.query.QuerySort)
		 */
		@Override
		public Builder sort(QuerySort sort) {
			instance.addSort(sort);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.query.QueryConfigurationProvider.Builder#parameter(java.lang.String,
		 * java.lang.Object)
		 */
		@Override
		public Builder parameter(String name, Object value) {
			instance.addParameter(name, value);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.query.QueryConfigurationProvider.Builder#parameter(com.holonplatform.core.config.
		 * ConfigProperty, java.lang.Object)
		 */
		@Override
		public <T> Builder parameter(ConfigProperty<T> property, T value) {
			ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
			instance.addParameter(property.getKey(), value);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.query.QueryConfigurationProvider.Builder#build()
		 */
		@Override
		public QueryConfigurationProvider build() {
			return instance;
		}

	}

}
