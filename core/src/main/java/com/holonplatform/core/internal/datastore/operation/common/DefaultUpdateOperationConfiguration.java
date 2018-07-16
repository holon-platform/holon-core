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
package com.holonplatform.core.internal.datastore.operation.common;

import java.util.Map;

import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.query.QueryFilter;

/**
 * Default {@link UpdateOperationConfiguration}.
 *
 * @since 5.1.0
 */
public class DefaultUpdateOperationConfiguration extends FilterableValueOperationDefinition
		implements UpdateOperationConfiguration {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.operation.AbstractDatastoreOperationDefinition#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		super.validate();
		if (getValues() == null || getValues().isEmpty()) {
			throw new InvalidExpressionException("No values to update");
		}
	}

	public static class DefaultBuilder extends
			DatastoreOperationConfigurationBuilder<UpdateOperationConfiguration.Builder, DefaultUpdateOperationConfiguration>
			implements UpdateOperationConfiguration.Builder {

		public DefaultBuilder() {
			super(new DefaultUpdateOperationConfiguration());
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.query.QueryFilter.QueryFilterSupport#filter(com.holonplatform.core.query.QueryFilter)
		 */
		@Override
		public UpdateOperationConfiguration.Builder filter(QueryFilter filter) {
			getInstance().setFilter(filter);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.operation.UpdateOperationConfiguration.Builder#values(java.util.Map)
		 */
		@Override
		public UpdateOperationConfiguration.Builder values(Map<Path<?>, TypedExpression<?>> values) {
			getInstance().setValues(values);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.operation.UpdateOperationConfiguration.Builder#build()
		 */
		@Override
		public UpdateOperationConfiguration build() {
			return getInstance();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.datastore.operation.AbstractDatastoreOperationDefinition.
		 * DatastoreOperationConfigurationBuilder#getActualBuilder()
		 */
		@Override
		protected UpdateOperationConfiguration.Builder getActualBuilder() {
			return this;
		}

	}

}
