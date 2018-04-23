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
package com.holonplatform.core.datastore.beans;

import java.util.Set;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;

/**
 * {@link BeanDatastoreOperation} builder.
 * 
 * @param <B> Concrete builder type
 *
 * @since 5.1.0
 */
public interface BeanDatastoreOperationBuilder<B extends BeanDatastoreOperationBuilder<B>>
		extends ExpressionResolverBuilder<B> {

	/**
	 * Add a {@link WriteOption} to this operation.
	 * @param writeOption The write option to add (not null)
	 * @return this
	 */
	B withWriteOption(WriteOption writeOption);

	/**
	 * Add a set of {@link WriteOption}s to this operation.
	 * @param writeOptions The write options to add (not null)
	 * @return this
	 */
	B withWriteOptions(WriteOption... writeOptions);

	/**
	 * Add a set of {@link WriteOption}s to this operation.
	 * @param writeOptions The write options to add (not null)
	 * @return this
	 */
	B withWriteOptions(Set<WriteOption> writeOptions);

	/**
	 * Add all the expression resolvers provided by given {@link Iterable}.
	 * @param expressionResolvers Expression resolvers to add (not null)
	 * @return this
	 */
	@SuppressWarnings("rawtypes")
	B withExpressionResolvers(Iterable<? extends ExpressionResolver> expressionResolvers);

}
