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
package com.holonplatform.core.property;

import java.util.Collection;

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.property.DefaultPathProperty;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.FunctionExpression;
import com.holonplatform.core.query.FunctionExpression.PathFunctionExpressionProperty;
import com.holonplatform.core.query.PathExpression;
import com.holonplatform.core.query.QueryExpression;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QueryFilter.FilterOperator;
import com.holonplatform.core.query.QueryFunction.Avg;
import com.holonplatform.core.query.QueryFunction.Count;
import com.holonplatform.core.query.QueryFunction.Max;
import com.holonplatform.core.query.QueryFunction.Min;
import com.holonplatform.core.query.QueryFunction.Sum;
import com.holonplatform.core.query.QueryProjection;
import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.query.QuerySort.SortDirection;

/**
 * A {@link Property} bound to a {@link Path}, using {@link Path#getName()} as property name.
 * <p>
 * This property can be used as {@link QueryExpression} and {@link QueryProjection}, and provides useful methods to
 * create query clauses such as filters, sorts and aggregations.
 * </p>
 * 
 * @param <T> Property value type
 * 
 * @since 5.0.0
 */
public interface PathProperty<T> extends Property<T>, PathExpression<T>, QueryProjection<T> {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#isReadOnly()
	 */
	@Override
	default boolean isReadOnly() {
		return false;
	}

	/**
	 * Clone this property.
	 * @return A new {@link PathPropertyBuilder} using property name, type and configuration cloned from this property
	 */
	PathPropertyBuilder<T> clone();

	// Builders

	/**
	 * Create a new {@link PathProperty} with given <code>name</code> and <code>type</code>.
	 * @param <T> Property (path) type
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 * @return {@link PathProperty} builder
	 */
	static <T> PathPropertyBuilder<T> create(String name, Class<? extends T> type) {
		return new DefaultPathProperty<>(name, type);
	}

	/**
	 * Create a new {@link PathProperty} from given <code>path</code>, using given {@link Path} <code>name</code> and
	 * <code>type</code>.
	 * @param <T> Property (path) type
	 * @param path Path from which to obtain the property path name and type (not null)
	 * @return {@link PathProperty} builder
	 */
	static <T> PathPropertyBuilder<T> create(Path<T> path) {
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		PathPropertyBuilder<T> builder = create(path.getName(), path.getType());
		path.getParent().ifPresent(p -> builder.parent(p));
		return builder;
	}

	/**
	 * {@link PathProperty} builder.
	 * @param <T> Property value type
	 */
	public interface PathPropertyBuilder<T> extends Builder<T, PathPropertyBuilder<T>>, PathProperty<T> {

	}

	/**
	 * Base interface for {@link PathProperty} building.
	 * @param <T> Property value type
	 * @param <B> Concrete builder type
	 */
	public interface Builder<T, B extends Builder<T, B>> extends Path.Builder<T, B>, Property.Builder<T, B> {

	}

	// Query clauses and expression builders

	/**
	 * Build a {@link FilterOperator#NULL} filter using this property as expression, which checks if property value is
	 * <code>null</code>.
	 * @return QueryFilter
	 */
	default QueryFilter isNull() {
		return QueryFilter.isNull(this);
	}

	/**
	 * Build a {@link FilterOperator#NOT_NULL} filter using this property as expression, which checks if property value
	 * is not <code>null</code>.
	 * @return QueryFilter
	 */
	default QueryFilter isNotNull() {
		return QueryFilter.isNotNull(this);
	}

	/**
	 * Build a {@link FilterOperator#EQUAL} filter using this property as expression, which checks if property value is
	 * equal to given constant value.
	 * @param value Filter value (not null)
	 * @return QueryFilter
	 */
	default QueryFilter eq(T value) {
		return QueryFilter.eq(this, value);
	}

	/**
	 * Build a {@link FilterOperator#EQUAL} filter using this property as expression, which checks if property value is
	 * equal to given <code>expression</code> value.
	 * @param expression Right operand expression (not null)
	 * @return QueryFilter
	 */
	default QueryFilter eq(QueryExpression<? super T> expression) {
		return QueryFilter.eq(this, expression);
	}

	/**
	 * Build a {@link FilterOperator#NOT_EQUAL} filter using this property as expression, which checks if property value
	 * is not equal to given constant value.
	 * @param value Filter value (not null)
	 * @return QueryFilter
	 */
	default QueryFilter neq(T value) {
		return QueryFilter.neq(this, value);
	}

	/**
	 * Build a {@link FilterOperator#NOT_EQUAL} filter using this property as expression, which checks if property value
	 * is not equal to given <code>expression</code> value.
	 * @param expression Right operand expression (not null)
	 * @return QueryFilter
	 */
	default QueryFilter neq(QueryExpression<? super T> expression) {
		return QueryFilter.neq(this, expression);
	}

	/**
	 * Build a {@link FilterOperator#LESS_THAN} filter using this property as expression, which checks if property value
	 * is less than given constant value.
	 * @param value Filter value (not null)
	 * @return QueryFilter
	 */
	default QueryFilter lt(T value) {
		return QueryFilter.lt(this, value);
	}

	/**
	 * Build a {@link FilterOperator#LESS_THAN} filter using this property as expression, which checks if property value
	 * is less than given <code>expression</code> value.
	 * @param expression Right operand expression (not null)
	 * @return QueryFilter
	 */
	default QueryFilter lt(QueryExpression<? super T> expression) {
		return QueryFilter.lt(this, expression);
	}

	/**
	 * Build a {@link FilterOperator#LESS_OR_EQUAL} filter using this property as expression, which checks if property
	 * value is less than or equal to given constant value.
	 * @param value Filter value (not null)
	 * @return QueryFilter
	 */
	default QueryFilter loe(T value) {
		return QueryFilter.loe(this, value);
	}

	/**
	 * Build a {@link FilterOperator#LESS_OR_EQUAL} filter using this property as expression, which checks if property
	 * value is less than or equal to given <code>expression</code> value.
	 * @param expression Right operand expression (not null)
	 * @return QueryFilter
	 */
	default QueryFilter loe(QueryExpression<? super T> expression) {
		return QueryFilter.loe(this, expression);
	}

	/**
	 * Build a {@link FilterOperator#GREATER_THAN} filter using this property as expression, which checks if property
	 * value is greater than given constant value.
	 * @param value Filter value (not null)
	 * @return QueryFilter
	 */
	default QueryFilter gt(T value) {
		return QueryFilter.gt(this, value);
	}

	/**
	 * Build a {@link FilterOperator#GREATER_THAN} filter using this property as expression, which checks if property
	 * value is greater than given <code>expression</code> value.
	 * @param expression Right operand expression (not null)
	 * @return QueryFilter
	 */
	default QueryFilter gt(QueryExpression<? super T> expression) {
		return QueryFilter.gt(this, expression);
	}

	/**
	 * Build a {@link FilterOperator#GREATER_OR_EQUAL} filter using this property as expression, which checks if
	 * property value is greater than or equal to given constant value.
	 * @param value Filter value (not null)
	 * @return QueryFilter
	 */
	default QueryFilter goe(T value) {
		return QueryFilter.goe(this, value);
	}

	/**
	 * Build a {@link FilterOperator#GREATER_OR_EQUAL} filter using this property as expression, which checks if
	 * property value is greater than or equal to given <code>expression</code> value.
	 * @param expression Right operand expression (not null)
	 * @return QueryFilter
	 */
	default QueryFilter goe(QueryExpression<? super T> expression) {
		return QueryFilter.goe(this, expression);
	}

	/**
	 * Build a {@link FilterOperator#IN} filter using this property as expression, which checks if property value is
	 * included in given constant values.
	 * @param values Filter values (not null)
	 * @return QueryFilter
	 */
	@SuppressWarnings("unchecked")
	default QueryFilter in(T... values) {
		return QueryFilter.in(this, values);
	}

	/**
	 * Build a {@link FilterOperator#IN} filter using this property as expression, which checks if property value is
	 * included in given constant values.
	 * @param values Filter values (not null)
	 * @return QueryFilter
	 */
	default QueryFilter in(Collection<T> values) {
		return QueryFilter.in(this, values);
	}

	/**
	 * Build a {@link FilterOperator#IN} filter using this property as expression, which checks if property value is
	 * included in given <code>expression</code> values.
	 * @param expression Right operand expression (not null)
	 * @return QueryFilter
	 */
	default QueryFilter in(QueryExpression<? super T> expression) {
		return QueryFilter.in(this, expression);
	}

	/**
	 * Build a {@link FilterOperator#NOT_IN} filter using this property as expression, which checks if property value is
	 * not included in given constant values.
	 * @param values Filter values (not null)
	 * @return QueryFilter
	 */
	@SuppressWarnings("unchecked")
	default QueryFilter nin(T... values) {
		return QueryFilter.nin(this, values);
	}

	/**
	 * Build a {@link FilterOperator#NOT_IN} filter using this property as expression, which checks if property value is
	 * not included in given constant values.
	 * @param values Filter values (not null)
	 * @return QueryFilter
	 */
	default QueryFilter nin(Collection<T> values) {
		return QueryFilter.nin(this, values);
	}

	/**
	 * Build a {@link FilterOperator#NOT_IN} filter using this property as expression, which checks if property value is
	 * not included in given <code>expression</code> values.
	 * @param expression Right operand expression (not null)
	 * @return QueryFilter
	 */
	default QueryFilter nin(QueryExpression<? super T> expression) {
		return QueryFilter.nin(this, expression);
	}

	/**
	 * Build a {@link FilterOperator#BETWEEN} filter using this property as expression, which checks if property value
	 * is between given <code>from</code> and <code>to</code> values.
	 * @param from From value (not null)
	 * @param to To value (not null)
	 * @return QueryFilter
	 */
	default QueryFilter between(T from, T to) {
		return QueryFilter.between(this, from, to);
	}

	/**
	 * Build a <em>contains</em> query filter, checking if the property value contains given value.
	 * <p>
	 * Only applicable for {@link String} value type properties.
	 * </p>
	 * @param value Value which must be contained in expression value
	 * @param ignoreCase Whether to ignore case
	 * @return QueryFilter
	 * @throws UnsupportedOperationException If the property is not a {@link String} type
	 */
	@SuppressWarnings("unchecked")
	default QueryFilter contains(String value, boolean ignoreCase) {
		if (!String.class.isAssignableFrom(getType())) {
			throw new UnsupportedOperationException("LIKE filter condition can be applied "
					+ "only to String type properties, property " + this + " is not of String type");
		}
		return QueryFilter.contains((QueryExpression<String>) this, value, ignoreCase);
	}

	/**
	 * Build a <em>starts with</em> query filter, checking if the property value starts with given value.
	 * <p>
	 * Only applicable for {@link String} value type properties.
	 * </p>
	 * @param value Value with which the expression value must start with
	 * @param ignoreCase Whether to ignore case
	 * @return QueryFilter
	 * @throws UnsupportedOperationException If the property is not a {@link String} type
	 */
	@SuppressWarnings("unchecked")
	default QueryFilter startsWith(String value, boolean ignoreCase) {
		if (!String.class.isAssignableFrom(getType())) {
			throw new UnsupportedOperationException("LIKE filter condition can be applied "
					+ "only to String type properties, property " + this + " is not of String type");
		}
		return QueryFilter.startsWith((QueryExpression<String>) this, value, ignoreCase);
	}

	/**
	 * Build a <em>ends with</em> query filter, checking if the property value ends with given value.
	 * <p>
	 * Only applicable for {@link String} value type properties.
	 * </p>
	 * @param value Value with which the expression value must end with
	 * @param ignoreCase Whether to ignore case
	 * @return QueryFilter
	 * @throws UnsupportedOperationException If the property is not a {@link String} type
	 */
	@SuppressWarnings("unchecked")
	default QueryFilter endsWith(String value, boolean ignoreCase) {
		if (!String.class.isAssignableFrom(getType())) {
			throw new UnsupportedOperationException("LIKE filter condition can be applied "
					+ "only to String type properties, property " + this + " is not of String type");
		}
		return QueryFilter.endsWith((QueryExpression<String>) this, value, ignoreCase);
	}

	/**
	 * Build a <em>contains</em> query filter, checking if the property value contains given value, in a case-sentive
	 * fashion.
	 * <p>
	 * Only applicable for {@link String} value type properties.
	 * </p>
	 * @param value Value which must be contained in expression value
	 * @return QueryFilter
	 * @throws UnsupportedOperationException If the property is not a {@link String} type
	 */
	default QueryFilter contains(String value) {
		return contains(value, false);
	}

	/**
	 * Build a <em>contains</em> query filter, checking if the property value contains given value, ignoring case.
	 * <p>
	 * Only applicable for {@link String} value type properties.
	 * </p>
	 * @param value Value which must be contained in expression value
	 * @return QueryFilter
	 * @throws UnsupportedOperationException If the property is not a {@link String} type
	 */
	default QueryFilter containsIgnoreCase(String value) {
		return contains(value, true);
	}

	/**
	 * Build a <em>starts with</em> query filter, checking if the property value starts with given value, in a
	 * case-sentive fashion.
	 * <p>
	 * Only applicable for {@link String} value type properties.
	 * </p>
	 * @param value Value with which the expression value must start with
	 * @return QueryFilter
	 * @throws UnsupportedOperationException If the property is not a {@link String} type
	 */
	default QueryFilter startsWith(String value) {
		return startsWith(value, false);
	}

	/**
	 * Build a <em>starts with</em> query filter, checking if the property value starts with given value, ignoring case.
	 * <p>
	 * Only applicable for {@link String} value type properties.
	 * </p>
	 * @param value Value with which the expression value must start with
	 * @return QueryFilter
	 * @throws UnsupportedOperationException If the property is not a {@link String} type
	 */
	default QueryFilter startsWithIgnoreCase(String value) {
		return startsWith(value, true);
	}

	/**
	 * Build a <em>ends with</em> query filter, checking if the property value ends with given value, in a case-sentive
	 * fashion.
	 * <p>
	 * Only applicable for {@link String} value type properties.
	 * </p>
	 * @param value Value with which the expression value must end with
	 * @return QueryFilter
	 * @throws UnsupportedOperationException If the property is not a {@link String} type
	 */
	default QueryFilter endsWith(String value) {
		return endsWith(value, false);
	}

	/**
	 * Build a <em>ends with</em> query filter, checking if the property value ends with given value, ignoring case.
	 * <p>
	 * Only applicable for {@link String} value type properties.
	 * </p>
	 * @param value Value with which the expression value must end with
	 * @return QueryFilter
	 * @throws UnsupportedOperationException If the property is not a {@link String} type
	 */
	default QueryFilter endsWithIgnoreCase(String value) {
		return endsWith(value, true);
	}

	/**
	 * Build a {@link SortDirection#ASCENDING} sort using this property.
	 * @return Ascending {@link QuerySort}
	 */
	default QuerySort asc() {
		return QuerySort.asc(this);
	}

	/**
	 * Build a {@link SortDirection#DESCENDING} sort using this property.
	 * @return Descending {@link QuerySort}
	 */
	default QuerySort desc() {
		return QuerySort.desc(this);
	}

	// Aggregations

	/**
	 * Build an aggregation {@link FunctionExpression} on this property using the {@link Count} function, which returns
	 * the count of the values of the property.
	 * @return A {@link Count} expression on this property, expressed as a {@link Property} to allow inclusion in
	 *         {@link PropertySet} and {@link PropertyBox} structures.
	 */
	default PathFunctionExpressionProperty<T, Long> count() {
		return PathFunctionExpressionProperty.create(Count.create(), this);
	}

	/**
	 * Build an aggregation {@link FunctionExpression} on this property using the {@link Min} function, which returns
	 * the smallest value of the property.
	 * @return A {@link Min} expression on this property, expressed as a {@link Property} to allow inclusion in
	 *         {@link PropertySet} and {@link PropertyBox} structures.
	 */
	default PathFunctionExpressionProperty<T, T> min() {
		return PathFunctionExpressionProperty.create(Min.create(getType()), this);
	}

	/**
	 * Build an aggregation {@link FunctionExpression} on this property using the {@link Max} function, which returns
	 * the largest value of the property.
	 * @return A {@link Max} expression on this property, expressed as a {@link Property} to allow inclusion in
	 *         {@link PropertySet} and {@link PropertyBox} structures.
	 */
	default PathFunctionExpressionProperty<T, T> max() {
		return PathFunctionExpressionProperty.create(Max.create(getType()), this);
	}

	/**
	 * Build an aggregation {@link FunctionExpression} on this property using the {@link Avg} function, which returns
	 * the average value of the property.
	 * @return A {@link Avg} expression on this property, expressed as a {@link Property} to allow inclusion in
	 *         {@link PropertySet} and {@link PropertyBox} structures.
	 */
	default PathFunctionExpressionProperty<T, Double> avg() {
		return PathFunctionExpressionProperty.create(Avg.create(), this);
	}

	/**
	 * Build an aggregation {@link FunctionExpression} on this property using the {@link Sum} function, which returns
	 * the sum of the property values.
	 * @return A {@link Sum} expression on this property, expressed as a {@link Property} to allow inclusion in
	 *         {@link PropertySet} and {@link PropertyBox} structures.
	 */
	default PathFunctionExpressionProperty<T, T> sum() {
		return PathFunctionExpressionProperty.create(Sum.create(getType()), this);
	}

}
