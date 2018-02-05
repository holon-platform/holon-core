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
package com.holonplatform.core.internal.datastore.operation;

import com.holonplatform.core.datastore.operation.DeleteOperationConfiguration;
import com.holonplatform.core.query.QueryFilter;

/**
 * Default {@link DeleteOperationConfiguration}.
 *
 * @since 5.1.0
 */
public class DefaultDeleteOperationConfiguration extends FilterableValueOperationDefinition
		implements DeleteOperationConfiguration {

	public static class DefaultBuilder extends
			DatastoreOperationConfigurationBuilder<DeleteOperationConfiguration.Builder, DefaultDeleteOperationConfiguration>
			implements DeleteOperationConfiguration.Builder {

		public DefaultBuilder() {
			super(new DefaultDeleteOperationConfiguration());
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.query.QueryFilter.QueryFilterSupport#filter(com.holonplatform.core.query.QueryFilter)
		 */
		@Override
		public DeleteOperationConfiguration.Builder filter(QueryFilter filter) {
			getInstance().setFilter(filter);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.operation.DeleteOperationConfiguration.Builder#build()
		 */
		@Override
		public DeleteOperationConfiguration build() {
			return getInstance();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.datastore.operation.AbstractDatastoreOperationDefinition.
		 * DatastoreOperationConfigurationBuilder#getActualBuilder()
		 */
		@Override
		protected DeleteOperationConfiguration.Builder getActualBuilder() {
			return this;
		}

	}

}
