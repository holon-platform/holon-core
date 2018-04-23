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
package com.holonplatform.core.internal.datastore.relational;

import java.util.Optional;

import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.relational.Join;
import com.holonplatform.core.datastore.relational.RelationalTarget;
import com.holonplatform.core.internal.DefaultPath;
import com.holonplatform.core.query.QueryFilter;

/**
 * Default {@link Join} implementation.
 * 
 * @param <T> Join path type
 * 
 * @since 5.0.0
 */
public class DefaultJoin<T> extends DefaultPath<T> implements Join<T> {

	private static final long serialVersionUID = -6898402724110070633L;

	/**
	 * Join type
	 */
	private final JoinType joinType;

	/**
	 * Optional data path
	 */
	private final String dataPath;

	/**
	 * Optional alias
	 */
	private String alias;

	/**
	 * Optional filter
	 */
	private QueryFilter on;

	/**
	 * Constructor
	 * @param target Target to join
	 * @param joinType Join type
	 */
	public DefaultJoin(DataTarget<T> target, JoinType joinType) {
		super(target.getName(), target.getType());
		this.joinType = joinType;
		this.dataPath = target.getDataPath().orElse(null);

		// check aliasable
		if (AliasablePath.class.isAssignableFrom(target.getClass())) {
			this.alias = ((AliasablePath<?, ?>) target).getAlias().orElse(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path#getDataPath()
	 */
	@Override
	public Optional<String> getDataPath() {
		return Optional.ofNullable(dataPath);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.relational.Aliasable#getAlias()
	 */
	@Override
	public Optional<String> getAlias() {
		return Optional.ofNullable(alias);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.relational.Aliasable#alias(java.lang.String)
	 */
	@Override
	public Join<T> alias(String alias) {
		this.alias = alias;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.relational.Join#getJoinType()
	 */
	@Override
	public JoinType getJoinType() {
		return joinType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.relational.Join#getOn()
	 */
	@Override
	public Optional<QueryFilter> getOn() {
		return Optional.ofNullable(on);
	}

	/**
	 * Set the ON filter clause.
	 * @param on the on filter to set
	 */
	public void setOn(QueryFilter on) {
		this.on = on;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.DefaultPath#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		super.validate();
		if (getJoinType() == null) {
			throw new InvalidExpressionException("Null join type");
		}
	}

	// Builder

	public static class DefaultJoinBuilder<T> implements JoinBuilder<T> {

		private final RelationalTargetEditor<T> parent;
		private final DefaultJoin<?> join;

		public DefaultJoinBuilder(RelationalTargetEditor<T> parent, DataTarget<?> target, JoinType joinType) {
			super();
			this.parent = parent;
			this.join = new DefaultJoin<>(target, joinType);
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.relational.Join.JoinBuilder#alias(java.lang.String)
		 */
		@Override
		public JoinBuilder<T> alias(String alias) {
			this.join.alias(alias);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.datastore.relational.Join.JoinBuilder#on(com.holonplatform.core.query.QueryFilter)
		 */
		@Override
		public JoinBuilder<T> on(QueryFilter filter) {
			this.join.setOn(filter);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.relational.Join.JoinBuilder#add()
		 */
		@Override
		public RelationalTarget<T> add() {
			return parent.withJoin(join);
		}

	}

}
