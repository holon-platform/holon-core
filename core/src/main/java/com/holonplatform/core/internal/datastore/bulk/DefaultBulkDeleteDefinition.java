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
package com.holonplatform.core.internal.datastore.bulk;

import java.util.Optional;

import com.holonplatform.core.internal.datastore.operation.AbstractDatastoreOperationDefinition;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryFilter;

/**
 * Default {@link BulkDeleteDefinition}.
 *
 * @since 5.1.0
 */
public class DefaultBulkDeleteDefinition extends AbstractDatastoreOperationDefinition implements BulkDeleteDefinition {

	/*
	 * Filter
	 */
	private QueryFilter filter;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.bulk.BulkDeleteDefinition#addFilter(com.holonplatform.core.query.
	 * QueryFilter)
	 */
	@Override
	public void addFilter(QueryFilter filter) {
		ObjectUtils.argumentNotNull(filter, "QueryFilter must be not null");
		if (this.filter == null) {
			this.filter = filter;
		} else {
			this.filter = this.filter.and(filter);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkDeleteConfiguration#getFilter()
	 */
	@Override
	public Optional<QueryFilter> getFilter() {
		return Optional.ofNullable(filter);
	}

}
