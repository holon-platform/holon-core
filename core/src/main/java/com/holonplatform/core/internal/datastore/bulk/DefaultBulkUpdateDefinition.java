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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.query.QueryFilter;

/**
 * Default {@link BulkUpdateDefinition}.
 *
 * @since 5.1.0
 */
public class DefaultBulkUpdateDefinition extends AbstractBulkOperationDefinition implements BulkUpdateDefinition {

	/*
	 * Filter
	 */
	private QueryFilter filter;

	/*
	 * Operation values
	 */
	private Map<Path<?>, TypedExpression<?>> values;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkUpdateConfiguration#getFilter()
	 */
	@Override
	public Optional<QueryFilter> getFilter() {
		return Optional.ofNullable(filter);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.bulk.BulkUpdateConfiguration#getValues()
	 */
	@Override
	public Map<Path<?>, TypedExpression<?>> getValues() {
		return (values != null) ? Collections.unmodifiableMap(values) : Collections.emptyMap();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.bulk.BulkUpdateDefinition#addFilter(com.holonplatform.core.query.
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
	 * @see com.holonplatform.core.internal.datastore.bulk.BulkUpdateDefinition#addValue(com.holonplatform.core.Path,
	 * com.holonplatform.core.TypedExpression)
	 */
	@Override
	public void addValue(Path<?> path, TypedExpression<?> value) {
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		ObjectUtils.argumentNotNull(value, "Value expression must be not null");
		if (values == null) {
			values = new HashMap<>();
		}
		values.put(path, value);
	}

	/* (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.bulk.BulkUpdateDefinition#setValue(com.holonplatform.core.property.PropertyBox, boolean)
	 */
	@Override
	public void setValue(PropertyBox propertyBox, boolean includeNullValues) {
		values = asPathValues(propertyBox, includeNullValues);
	}

	/* (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.bulk.AbstractBulkOperationDefinition#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		super.validate();
		if (getValues().isEmpty()) {
			throw new InvalidExpressionException("No values to update");
		}
	}

}
