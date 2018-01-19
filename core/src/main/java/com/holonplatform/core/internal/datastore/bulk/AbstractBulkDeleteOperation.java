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

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.Datastore.OperationType;
import com.holonplatform.core.datastore.bulk.BulkDeleteOperation;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryFilter;

/**
 * Abstract {@link BulkDeleteOperation} operation.
 * 
 * @param <O> Actual operation type
 *
 * @since 5.1.0
 */
public abstract class AbstractBulkDeleteOperation<O extends BulkDeleteOperation<O>> extends AbstractBulkOperation<O>
		implements BulkDeleteOperation<O> {

	/**
	 * Constructor.
	 * @param target Operation target (not null)
	 */
	public AbstractBulkDeleteOperation(DataTarget<?> target) {
		super(OperationType.DELETE);
		ObjectUtils.argumentNotNull(target, "Operation data target must be not null");
		getDefinition().setTarget(target);
	}

	/**
	 * Get the actual operation object.
	 * @return actual operation object
	 */
	protected abstract O getActualOperation();

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder#withExpressionResolver(com.holonplatform.core
	 * .ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> O withExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		getDefinition().addExpressionResolver(expressionResolver);
		return getActualOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFilter.QueryFilterSupport#filter(com.holonplatform.core.query.QueryFilter)
	 */
	@Override
	public O filter(QueryFilter filter) {
		getDefinition().addFilter(filter);
		return getActualOperation();
	}

}
