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
package com.holonplatform.core.datastore.bulk;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverHandler;
import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.Datastore.OperationType;
import com.holonplatform.core.datastore.Datastore.WriteOption;
import com.holonplatform.core.query.QueryFilter;

/**
 * Represents a {@link BulkOperation} configuration.
 * <p>
 * Extends {@link ExpressionResolverHandler} to support {@link ExpressionResolver} handling.
 * </p>
 *
 * @since 5.1.0
 */
public interface BulkOperationConfiguration extends ExpressionResolverHandler {

	/**
	 * Get the operation type.
	 * @return the operation type.
	 */
	OperationType getOperationType();

	/**
	 * Get the data target.
	 * @return Optional operation {@link DataTarget}
	 */
	Optional<DataTarget<?>> getTarget();

	/**
	 * Get the optional operation restrictions, expressed as a {@link QueryFilter}.
	 * @return Optional operation filter
	 */
	Optional<QueryFilter> getFilter();

	/**
	 * Get the optional operation values, expressed as a List of {@link Path} - {@link TypedExpression} maps.
	 * @return The path-value expression map list, empty if none
	 */
	List<Map<Path<?>, TypedExpression<?>>> getValues();

	/**
	 * Get the {@link WriteOption}s associated to this operation.
	 * @return The {@link WriteOption}s set, empty if none
	 */
	Set<WriteOption> getWriteOptions();

}
