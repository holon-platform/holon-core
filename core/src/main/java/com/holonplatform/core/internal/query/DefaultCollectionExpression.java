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
package com.holonplatform.core.internal.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.holonplatform.core.query.ConstantExpression;

/**
 * {@link ConstantExpression} for a list of values implementation using an {@link ArrayList} as concrete collection
 * class
 * 
 * @param <E> List elements type
 * 
 * @since 5.0.0
 */
public class DefaultCollectionExpression<E> extends ArrayList<E> implements ConstantExpression<Collection<E>, E> {

	private static final long serialVersionUID = -3974289166776361490L;

	/**
	 * Constructor with a collection of values.
	 * @param values Constant expression values
	 */
	public DefaultCollectionExpression(Collection<? extends E> values) {
		super(values);
	}

	/**
	 * Constructor with an array of values.
	 * @param values Constant expression values
	 */
	@SafeVarargs
	public DefaultCollectionExpression(E... values) {
		super(values.length);
		for (E value : values) {
			add(value);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.ConstantExpression#getValue()
	 */
	@Override
	public List<E> getValue() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getValue() == null) {
			throw new InvalidExpressionException("Null value");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryDataExpression#getType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends E> getType() {
		return (Class<? extends E>) Collection.class;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DefaultCollectionExpression [");
		Iterator<E> it = iterator();
		while (it.hasNext()) {
			if (sb.length() > 1) {
				sb.append(",");
			}
			sb.append(it.next());
		}
		sb.append(']');
		return sb.toString();
	}

}
