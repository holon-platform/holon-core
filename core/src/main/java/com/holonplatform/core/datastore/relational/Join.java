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
package com.holonplatform.core.datastore.relational;

import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.Path.FinalPath;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.relational.Aliasable.AliasablePath;
import com.holonplatform.core.query.QueryFilter;

/**
 * Represents a relational join expression between two {@link DataTarget}s.
 * <p>
 * A Join expression is modelled using a {@link Path} and supports <em>alias</em> name definition through the
 * {@link Aliasable} interface.
 * </p>
 * <p>
 * Supports a <code>ON</code> restriction clause, represented by a {@link QueryFilter}.
 * </p>
 * 
 * @param <T> Join path type
 * 
 * @since 5.0.0
 */
public interface Join<T> extends FinalPath<T>, AliasablePath<T, Join<T>> {

	/**
	 * Enumeration of available join types.
	 */
	public enum JoinType {

		/**
		 * Inner join: returns all rows when there is at least one match in BOTH tables
		 */
		INNER,

		/**
		 * Left join: returns all rows from the left table, and the matched rows from the right table
		 */
		LEFT,

		/**
		 * Right join: return all rows from the right table, and the matched rows from the left table
		 */
		RIGHT;

	}

	/**
	 * Get the {@link JoinType}.
	 * @return The join type (not null)
	 */
	JoinType getJoinType();

	/**
	 * Get the {@link QueryFilter} that corresponds to the <code>ON</code> restriction(s) on the join.
	 * @return Optional <code>ON</code> restriction(s) on the join
	 */
	Optional<QueryFilter> getOn();

	/**
	 * {@link Join} builder.
	 * @param <T> Parent data target type
	 */
	public interface JoinBuilder<T> {

		/**
		 * Set the alias name.
		 * @param alias The alias name to set
		 * @return this
		 */
		JoinBuilder<T> alias(String alias);

		/**
		 * Add a join restriction using given <code>filter</code>.
		 * @param filter Join restriction to add (not null)
		 * @return this
		 */
		JoinBuilder<T> on(QueryFilter filter);

		/**
		 * Add the join expression to the parent data target
		 * @return A new {@link RelationalTarget} with the given join. The join is added to the current target joins
		 *         list.
		 */
		RelationalTarget<T> add();

	}

}
