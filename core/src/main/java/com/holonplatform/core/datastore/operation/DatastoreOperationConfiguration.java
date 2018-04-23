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
package com.holonplatform.core.datastore.operation;

import java.util.Set;

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverBuilder;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverHandler;
import com.holonplatform.core.ExpressionResolver.ExpressionResolverProvider;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.DatastoreOperations.WriteOption;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Represents a {@link DatastoreOperation} configuration.
 * <p>
 * Extends {@link ExpressionResolverHandler} to support {@link ExpressionResolver} handling.
 * </p>
 *
 * @since 5.1.0
 */
public interface DatastoreOperationConfiguration extends Expression, ExpressionResolverProvider {

	/**
	 * Get the data target.
	 * @return The operation {@link DataTarget}
	 */
	DataTarget<?> getTarget();

	/**
	 * Get the {@link WriteOption}s associated to this operation.
	 * @return The {@link WriteOption}s set, empty if none
	 */
	Set<WriteOption> getWriteOptions();

	/**
	 * Checks whether given {@link WriteOption} is present in this configuration.
	 * @param writeOption The write option to look for (not null)
	 * @return <code>true</code> if the write option is present in this configuration, <code>false</code> otherwise
	 */
	default boolean hasWriteOption(WriteOption writeOption) {
		ObjectUtils.argumentNotNull(writeOption, "Write option must be not null");
		return getWriteOptions().contains(writeOption);
	}

	/**
	 * Base {@link DatastoreOperationConfiguration} builder.
	 *
	 * @param <B> Concrete builder type
	 */
	public interface Builder<B extends Builder<B>> extends ExpressionResolverBuilder<B> {

		/**
		 * Set the operation {@link DataTarget}.
		 * @param target the operation data target to set
		 * @return this
		 */
		B target(DataTarget<?> target);

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

}
