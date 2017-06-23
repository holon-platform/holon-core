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
package com.holonplatform.core.internal.datastore.relational;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.relational.Join;
import com.holonplatform.core.datastore.relational.Join.JoinBuilder;
import com.holonplatform.core.datastore.relational.Join.JoinType;
import com.holonplatform.core.datastore.relational.RelationalTarget;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Defalt {@link RelationalTarget} implementation.
 * 
 * @param <T> Path type
 * 
 * @since 5.0.0
 */
public class DefaultRelationalTarget<T> implements RelationalTargetEditor<T> {

	private static final long serialVersionUID = 3685018778877463445L;

	private final DataTarget<T> target;

	private String alias;

	private List<Join<?>> joins = new LinkedList<>();

	/**
	 * Constructor
	 * @param target Source target (not null)
	 */
	public DefaultRelationalTarget(DataTarget<T> target) {
		super();
		ObjectUtils.argumentNotNull(target, "DataTarget must be not null");
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path#getName()
	 */
	@Override
	public String getName() {
		return target.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path#getType()
	 */
	@Override
	public Class<? extends T> getType() {
		return target.getType();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		target.validate();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.relational.Aliasable#getAlias()
	 */
	@Override
	public Optional<String> getAlias() {
		return Optional.ofNullable(alias);
	}

	/**
	 * Set the alias name.
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.relational.Aliasable#alias(java.lang.String)
	 */
	@Override
	public RelationalTarget<T> alias(String alias) {
		DefaultRelationalTarget<T> cloned = clone();
		cloned.setAlias(alias);
		return cloned;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.relational.Joinable#getJoins()
	 */
	@Override
	public Collection<Join<?>> getJoins() {
		return joins;
	}

	/**
	 * Set the {@link Join} list.
	 * @param joins the joins to set
	 */
	protected void setJoins(List<Join<?>> joins) {
		this.joins = joins;
	}

	/**
	 * Add a {@link Join}.
	 * @param join The join to add (not null)
	 */
	protected void addJoin(Join<?> join) {
		ObjectUtils.argumentNotNull(join, "Join to add must be not null");
		this.joins.add(join);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.relational.RelationalTargetEditor#addJoin(com.holonplatform.core.
	 * datastore.relational.Join)
	 */
	@Override
	public RelationalTarget<T> withJoin(Join<?> join) {
		DefaultRelationalTarget<T> cloned = clone();
		cloned.addJoin(join);
		return cloned;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.datastore.relational.RelationalTarget#join(com.holonplatform.core.datastore.DataTarget,
	 * com.holonplatform.core.datastore.relational.Join.JoinType)
	 */
	@Override
	public JoinBuilder<T> join(DataTarget<?> target, JoinType type) {
		ObjectUtils.argumentNotNull(target, "DataTarget to join must be not null");
		ObjectUtils.argumentNotNull(type, "Join type must be not null");
		return new DefaultJoin.DefaultJoinBuilder<>(this, target, type);
	}

	@Override
	protected DefaultRelationalTarget<T> clone() {
		DefaultRelationalTarget<T> cloned = new DefaultRelationalTarget<>(target);
		cloned.setAlias(alias);
		cloned.setJoins(joins);
		return cloned;
	}

}
