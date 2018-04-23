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
package com.holonplatform.core.query;

import java.util.Collection;

import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.query.QueryFunction.Count;
import com.holonplatform.core.query.QueryFunction.Max;
import com.holonplatform.core.query.QueryFunction.Min;
import com.holonplatform.core.query.QueryFunction.PropertyQueryFunction;

/**
 * Represent a general typed expression used by {@link Query} clauses.
 * 
 * @param <T> Expression type
 * 
 * @since 5.0.0
 */
public interface QueryExpression<T> extends TypedExpression<T> {

	// ------- Query filter builders

	/**
	 * Build a filter on this expression, which checks if property value is <code>null</code>.
	 * @return The QueryFilter
	 */
	default QueryFilter isNull() {
		return QueryFilter.isNull(this);
	}

	/**
	 * Build a filter on this expression, which checks if property value is not <code>null</code>.
	 * @return The QueryFilter
	 */
	default QueryFilter isNotNull() {
		return QueryFilter.isNotNull(this);
	}

	/**
	 * Build a filter on this expression, which checks if property value is equal to given constant value.
	 * @param value Filter value (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter eq(T value) {
		return QueryFilter.eq(this, value);
	}

	/**
	 * Build a filter on this expression, which checks if property value is equal to given <code>expression</code>
	 * value.
	 * @param expression Right operand expression (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter eq(TypedExpression<? super T> expression) {
		return QueryFilter.eq(this, expression);
	}

	/**
	 * Build a filter on this expression, which checks if property value is not equal to given constant value.
	 * @param value Filter value (not null)
	 * @return This QueryFilter
	 */
	default QueryFilter neq(T value) {
		return QueryFilter.neq(this, value);
	}

	/**
	 * Build a filter on this expression, which checks if property value is not equal to given <code>expression</code>
	 * value.
	 * @param expression Right operand expression (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter neq(TypedExpression<? super T> expression) {
		return QueryFilter.neq(this, expression);
	}

	/**
	 * Build a filter on this expression, which checks if property value is less than given constant value.
	 * @param value Filter value (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter lt(T value) {
		return QueryFilter.lt(this, value);
	}

	/**
	 * Build a filter on this expression, which checks if property value is less than given <code>expression</code>
	 * value.
	 * @param expression Right operand expression (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter lt(TypedExpression<? super T> expression) {
		return QueryFilter.lt(this, expression);
	}

	/**
	 * Build a filter on this expression, which checks if property value is less than or equal to given constant value.
	 * @param value Filter value (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter loe(T value) {
		return QueryFilter.loe(this, value);
	}

	/**
	 * Build a filter on this expression, which checks if property value is less than or equal to given
	 * <code>expression</code> value.
	 * @param expression Right operand expression (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter loe(TypedExpression<? super T> expression) {
		return QueryFilter.loe(this, expression);
	}

	/**
	 * Build a filter on this expression, which checks if property value is greater than given constant value.
	 * @param value Filter value (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter gt(T value) {
		return QueryFilter.gt(this, value);
	}

	/**
	 * Build a filter on this expression, which checks if property value is greater than given <code>expression</code>
	 * value.
	 * @param expression Right operand expression (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter gt(TypedExpression<? super T> expression) {
		return QueryFilter.gt(this, expression);
	}

	/**
	 * Build a filter on this expression, which checks if property value is greater than or equal to given constant
	 * value.
	 * @param value Filter value (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter goe(T value) {
		return QueryFilter.goe(this, value);
	}

	/**
	 * Build a filter on this expression, which checks if property value is greater than or equal to given
	 * <code>expression</code> value.
	 * @param expression Right operand expression (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter goe(TypedExpression<? super T> expression) {
		return QueryFilter.goe(this, expression);
	}

	/**
	 * Build a filter on this expression, which checks if property value is included in given constant values.
	 * @param values Filter values (not null)
	 * @return The QueryFilter
	 */
	@SuppressWarnings("unchecked")
	default QueryFilter in(T... values) {
		return QueryFilter.in(this, values);
	}

	/**
	 * Build a filter on this expression, which checks if property value is included in given constant values.
	 * @param values Filter values (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter in(Collection<T> values) {
		return QueryFilter.in(this, values);
	}

	/**
	 * Build a filter on this expression, which checks if property value is included in given <code>expression</code>
	 * values.
	 * @param expression Right operand expression (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter in(TypedExpression<? super T> expression) {
		return QueryFilter.in(this, expression);
	}

	/**
	 * Build a filter on this expression, which checks if property value is not included in given constant values.
	 * @param values Filter values (not null)
	 * @return The QueryFilter
	 */
	@SuppressWarnings("unchecked")
	default QueryFilter nin(T... values) {
		return QueryFilter.nin(this, values);
	}

	/**
	 * Build a filter on this expression, which checks if property value is not included in given constant values.
	 * @param values Filter values (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter nin(Collection<T> values) {
		return QueryFilter.nin(this, values);
	}

	/**
	 * Build a filter on this expression, which checks if property value is not included in given
	 * <code>expression</code> values.
	 * @param expression Right operand expression (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter nin(TypedExpression<? super T> expression) {
		return QueryFilter.nin(this, expression);
	}

	/**
	 * Build a filter on this expression, which checks if property value is between given <code>from</code> and
	 * <code>to</code> values.
	 * @param from From value (not null)
	 * @param to To value (not null)
	 * @return The QueryFilter
	 */
	default QueryFilter between(T from, T to) {
		return QueryFilter.between(this, from, to);
	}

	// ------- Aggregation function builders

	/**
	 * Create an aggregation function expression using the {@link Count} function.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A {@link Count} aggregation function expression
	 */
	default Count count() {
		return Count.create(this);
	}

	/**
	 * Create an aggregation function expression using the {@link Min} function.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A {@link Min} aggregation function expression
	 */
	default Min<T> min() {
		return Min.create(this);
	}

	/**
	 * Create an aggregation function expression using the {@link Max} function.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A {@link Max} aggregation function expression
	 */
	default Max<T> max() {
		return Max.create(this);
	}

}
