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
package com.holonplatform.core.query;

import java.util.function.Supplier;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.internal.query.CallbackQueryConfigurationProvider;
import com.holonplatform.core.internal.query.DefaultQueryConfigurationProvider;

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

	// ------- Builders

	/**
	 * Create a new {@link QueryConfigurationProvider} using given Suppliers to provide the query configuration
	 * elements.
	 * @param filterSupplier {@link QueryFilter} supplier (not null)
	 * @param sortSupplier {@link QuerySort} supplier (not null)
	 * @param parametersSupplier {@link ParameterSet} supplier (not null)
	 * @return A new {@link QueryConfigurationProvider} instance using given Suppliers to provide the query
	 *         configuration element
	 * @since 5.2.0
	 */
	static QueryConfigurationProvider create(Supplier<QueryFilter> filterSupplier, Supplier<QuerySort> sortSupplier,
			Supplier<ParameterSet> parametersSupplier) {
		return new CallbackQueryConfigurationProvider(filterSupplier, sortSupplier, parametersSupplier);
	}

	/**
	 * Create a new {@link QueryConfigurationProvider} using given Suppliers to provide the query configuration
	 * elements.
	 * @param filterSupplier {@link QueryFilter} supplier (not null)
	 * @param sortSupplier {@link QuerySort} supplier (not null)
	 * @return A new {@link QueryConfigurationProvider} instance using given Suppliers to provide the query
	 *         configuration element
	 * @since 5.2.0
	 */
	static QueryConfigurationProvider create(Supplier<QueryFilter> filterSupplier, Supplier<QuerySort> sortSupplier) {
		return create(filterSupplier, sortSupplier, () -> null);
	}

	/**
	 * Create a new {@link QueryConfigurationProvider} which provides given {@link QueryFilter} and {@link QuerySort}.
	 * @param filter The {@link QueryFilter} to provide (may be null)
	 * @param sort The {@link QuerySort} to provide (may be null)
	 * @return A new {@link QueryConfigurationProvider} instance using given Suppliers to provide the query
	 *         configuration element
	 * @since 5.2.0
	 */
	static QueryConfigurationProvider create(QueryFilter filter, QuerySort sort) {
		return create(() -> filter, () -> sort, () -> null);
	}

	/**
	 * Create a new {@link QueryConfigurationProvider} which provides a {@link QueryFilter} using given Supplier.
	 * @param filterSupplier {@link QueryFilter} supplier (not null)
	 * @return A new {@link QueryConfigurationProvider} instance
	 * @since 5.2.0
	 */
	static QueryConfigurationProvider withFilter(Supplier<QueryFilter> filterSupplier) {
		return create(filterSupplier, () -> null, () -> null);
	}

	/**
	 * Create a new {@link QueryConfigurationProvider} which provides given {@link QueryFilter}.
	 * @param filter The {@link QueryFilter} to provide (may be null)
	 * @return A new {@link QueryConfigurationProvider} instance
	 * @since 5.2.0
	 */
	static QueryConfigurationProvider withFilter(QueryFilter filter) {
		return withFilter(() -> filter);
	}

	/**
	 * Create a new {@link QueryConfigurationProvider} which provides a {@link QuerySort} using given Supplier.
	 * @param sortSupplier {@link QuerySort} supplier (not null)
	 * @return A new {@link QueryConfigurationProvider} instance
	 * @since 5.2.0
	 */
	static QueryConfigurationProvider withSort(Supplier<QuerySort> sortSupplier) {
		return create(() -> null, sortSupplier, () -> null);
	}

	/**
	 * Create a new {@link QueryConfigurationProvider} which provides given {@link QuerySort}.
	 * @param sort The {@link QuerySort} to provide (may be null)
	 * @return A new {@link QueryConfigurationProvider} instance
	 * @since 5.2.0
	 */
	static QueryConfigurationProvider withSort(QuerySort sort) {
		return withSort(() -> sort);
	}

	/**
	 * Obtain a builder to create a {@link QueryConfigurationProvider} instance.
	 * @return QueryConfigurationProvider builder
	 */
	static Builder builder() {
		return new DefaultQueryConfigurationProvider.DefaultBuilder();
	}

	/**
	 * Builder to create {@link QueryConfigurationProvider} instances.
	 *
	 * @since 5.2.0
	 */
	public interface Builder {

		/**
		 * Add a {@link QueryFilter}.
		 * <p>
		 * If a filter was already added, the given filter will be added to the previous ones using the AND logical
		 * operator.
		 * </p>
		 * @param filter The {@link QueryFilter} to add (not null)
		 * @return this
		 */
		Builder filter(QueryFilter filter);

		/**
		 * Add a {@link QuerySort}.
		 * <p>
		 * If one or more sort is already present, the given sort will be appended to the sort list.
		 * </p>
		 * @param sort The {@link QuerySort} to add (not null)
		 * @return this
		 */
		Builder sort(QuerySort sort);

		/**
		 * Add a query parameter.
		 * @param name Parameter name (not null)
		 * @param value Parameter value
		 * @return this
		 */
		Builder parameter(String name, Object value);

		/**
		 * Add a query parameter using a {@link ConfigProperty} and {@link ConfigProperty#getKey()} as parameter name.
		 * @param <T> Config property type
		 * @param property The configuration property (not null)
		 * @param value Configuration property value
		 * @return this
		 */
		<T> Builder parameter(ConfigProperty<T> property, T value);

		/**
		 * Build the {@link QueryConfigurationProvider} instance.
		 * @return The {@link QueryConfigurationProvider} instance
		 */
		QueryConfigurationProvider build();

	}

}
