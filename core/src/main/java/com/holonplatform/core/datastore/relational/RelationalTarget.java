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
package com.holonplatform.core.datastore.relational;

import java.util.Collection;

import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.relational.Aliasable.AliasablePath;
import com.holonplatform.core.datastore.relational.Join.JoinBuilder;
import com.holonplatform.core.datastore.relational.Join.JoinType;
import com.holonplatform.core.internal.datastore.relational.DefaultRelationalTarget;

/**
 * A {@link DataTarget} which can be use with <em>relational</em> {@link Datastore}s.
 * <p>
 * Supports <em>alias</em> name definition through the {@link Aliasable} interface.
 * </p>
 * <p>
 * Supports <em>join</em> definition with other {@link DataTarget}s using the
 * {@link #join(DataTarget, com.holonplatform.core.datastore.relational.Join.JoinType)} method.
 * </p>
 * 
 * @param <T> Path type
 * 
 * @since 5.0.0
 */
public interface RelationalTarget<T> extends DataTarget<T>, AliasablePath<T, RelationalTarget<T>> {

	/**
	 * Get the defined joins.
	 * @return the defined joins, an empty set if none
	 */
	Collection<Join<?>> getJoins();

	/**
	 * Add a join clause using given <code>target</code> as join destination.
	 * @param target Target to join (not null)
	 * @param type Join type (not null)
	 * @return The {@link Join} builder
	 */
	JoinBuilder<T> join(DataTarget<?> target, JoinType type);

	/**
	 * Add a {@link JoinType#INNER} join clause using given <code>target</code>.
	 * @param target Target to join (not null)
	 * @return The {@link Join} builder
	 */
	default JoinBuilder<T> innerJoin(DataTarget<?> target) {
		return join(target, JoinType.INNER);
	}

	/**
	 * Add a {@link JoinType#LEFT} join clause using given <code>target</code>.
	 * @param target Target to join (not null)
	 * @return The {@link Join} builder
	 */
	default JoinBuilder<T> leftJoin(DataTarget<?> target) {
		return join(target, JoinType.LEFT);
	}

	/**
	 * Add a {@link JoinType#RIGHT} join clause using given <code>target</code>.
	 * @param target Target to join (not null)
	 * @return The {@link Join} builder
	 */
	default JoinBuilder<T> rightJoin(DataTarget<?> target) {
		return join(target, JoinType.RIGHT);
	}

	/**
	 * Create a new {@link RelationalTarget} using given <code>target</code> as root.
	 * @param <T> Target path type
	 * @param target Root target (not null)
	 * @return A new {@link RelationalTarget} instance
	 */
	static <T> RelationalTarget<T> of(DataTarget<T> target) {
		return new DefaultRelationalTarget<>(target);
	}

}
