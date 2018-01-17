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
package com.holonplatform.core.internal.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.holonplatform.core.query.CollectionExpression;
import com.holonplatform.core.query.ConstantExpression;
import com.holonplatform.core.query.ConverterExpression;
import com.holonplatform.core.query.ExpressionValueConverter;
import com.holonplatform.core.query.QueryExpression;

/**
 * {@link ConstantExpression} for a list of values implementation using an {@link ArrayList} as concrete collection
 * class
 * 
 * @param <E> List elements type
 * 
 * @since 5.0.0
 */
public class DefaultCollectionExpression<E> extends ArrayList<E> implements CollectionExpression<E> {

	private static final long serialVersionUID = -3974289166776361490L;

	/*
	 * Optional value converter
	 */
	private final ExpressionValueConverter<E, ?> expressionValueConverter;

	/**
	 * Constructor with a collection of values.
	 * @param values Constant expression values
	 */
	public DefaultCollectionExpression(Collection<? extends E> values) {
		this(null, values);
	}

	/**
	 * Constructor with an array of values.
	 * @param values Constant expression values
	 */
	@SafeVarargs
	public DefaultCollectionExpression(E... values) {
		this(null, values);
	}

	/**
	 * Constructor with an array of values.
	 * @param expression Optional expression from which to inherit an {@link ExpressionValueConverter}, if available.
	 * @param values Constant expression values
	 */
	@SafeVarargs
	public DefaultCollectionExpression(QueryExpression<E> expression, E... values) {
		this(expression, Arrays.asList(values));
	}

	/**
	 * Constructor with a collection of values.
	 * @param expression Optional expression from which to inherit an {@link ExpressionValueConverter}, if available.
	 * @param values Constant expression values
	 */
	@SuppressWarnings("unchecked")
	public DefaultCollectionExpression(QueryExpression<E> expression, Collection<? extends E> values) {
		super(values);
		this.expressionValueConverter = (expression instanceof ConverterExpression)
				? ((ConverterExpression<E>) expression).getExpressionValueConverter().orElse(null) : null;
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
	 * @see com.holonplatform.core.query.ConstantExpression#getModelValue()
	 */
	@Override
	public Object getModelValue() {
		if (getValue() == null || !getExpressionValueConverter().isPresent()) {
			return getValue();
		}
		List<Object> modelValues = new ArrayList<>(getValue().size());
		for (E value : getValue()) {
			modelValues.add(getExpressionValueConverter().get().toModel(value));
		}
		return modelValues;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.ConverterExpression#getExpressionValueConverter()
	 */
	@Override
	public Optional<ExpressionValueConverter<E, ?>> getExpressionValueConverter() {
		return Optional.ofNullable(expressionValueConverter);
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
