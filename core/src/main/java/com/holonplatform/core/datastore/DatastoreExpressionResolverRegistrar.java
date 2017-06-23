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
package com.holonplatform.core.datastore;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverSupport;

/**
 * Interface implemented by {@link Datastore}s which provide default {@link ExpressionResolver}s registration.
 *
 * @since 5.0.0
 */
public interface DatastoreExpressionResolverRegistrar extends ExpressionResolverSupport {

	/**
	 * Get the {@link ExpressionResolver} base type to use with this Datastore for automatic resolver registration.
	 * @return the {@link ExpressionResolver} base type
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends ExpressionResolver> getExpressionResolverType();

}
