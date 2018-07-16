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
package com.holonplatform.core.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.holonplatform.core.CollectionConstantExpression;
import com.holonplatform.core.ConverterExpression;
import com.holonplatform.core.ExpressionValueConverter;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Default {@link CollectionConstantExpression} implementation.
 * 
 * @param <E> List elements type
 * 
 * @since 5.0.0
 */
public class DefaultCollectionConstantExpression<E> extends ArrayList<E> implements CollectionConstantExpression<E> {

	private static final long serialVersionUID = -3974289166776361490L;

	/*
	 * Value type
	 */
	private final Class<? extends E> type;

	/*
	 * Expression temporal type
	 */
	private TemporalType temporalType;

	/*
	 * Optional value converter
	 */
	private final ExpressionValueConverter<E, ?> expressionValueConverter;

	/**
	 * Constructor with a collection of values.
	 * @param values Constant expression values
	 */
	public DefaultCollectionConstantExpression(Collection<? extends E> values) {
		this(null, values);
	}

	/**
	 * Constructor with an array of values.
	 * @param values Constant expression values
	 */
	@SafeVarargs
	public DefaultCollectionConstantExpression(E... values) {
		this(null, values);
	}

	/**
	 * Constructor with an array of values.
	 * @param expression Optional expression from which to inherit an {@link ExpressionValueConverter}, if available.
	 * @param values Constant expression values
	 */
	@SafeVarargs
	public DefaultCollectionConstantExpression(TypedExpression<E> expression, E... values) {
		this(expression, Arrays.asList(values));
	}

	/**
	 * Constructor with a collection of values.
	 * @param expression Optional expression from which to inherit an {@link ExpressionValueConverter}, if available.
	 * @param values Constant expression values
	 */
	@SuppressWarnings("unchecked")
	public DefaultCollectionConstantExpression(TypedExpression<E> expression, Collection<? extends E> values) {
		super((values != null) ? values : Collections.emptySet());
		this.expressionValueConverter = (expression instanceof ConverterExpression)
				? ((ConverterExpression<E>) expression).getExpressionValueConverter().orElse(null)
				: null;
		this.type = (expression != null) ? expression.getType()
				: ((values != null && !values.isEmpty()) ? (Class<? extends E>) values.iterator().next().getClass()
						: (Class<? extends E>) Void.class);
		if (expression != null)
			expression.getTemporalType().ifPresent(t -> setTemporalType(t));
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
	public Collection<?> getModelValue() {
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
	@Override
	public Class<? extends E> getType() {
		return type;
	}

	/**
	 * Set the expression value temporal type.
	 * @param temporalType the temporal type to set
	 */
	public void setTemporalType(TemporalType temporalType) {
		this.temporalType = temporalType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.TypedExpression#getTemporalType()
	 */
	@Override
	public Optional<TemporalType> getTemporalType() {
		if (temporalType != null) {
			return Optional.of(temporalType);
		}
		return CollectionConstantExpression.super.getTemporalType();
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
