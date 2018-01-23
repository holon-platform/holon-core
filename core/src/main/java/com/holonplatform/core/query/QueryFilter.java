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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.holonplatform.core.Expression;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.CallbackExpressionResolver;
import com.holonplatform.core.internal.query.filter.AndFilter;
import com.holonplatform.core.internal.query.filter.BetweenFilter;
import com.holonplatform.core.internal.query.filter.EqualFilter;
import com.holonplatform.core.internal.query.filter.GreaterFilter;
import com.holonplatform.core.internal.query.filter.InFilter;
import com.holonplatform.core.internal.query.filter.LessFilter;
import com.holonplatform.core.internal.query.filter.NotEqualFilter;
import com.holonplatform.core.internal.query.filter.NotFilter;
import com.holonplatform.core.internal.query.filter.NotInFilter;
import com.holonplatform.core.internal.query.filter.NotNullFilter;
import com.holonplatform.core.internal.query.filter.NullFilter;
import com.holonplatform.core.internal.query.filter.OrFilter;
import com.holonplatform.core.internal.query.filter.StringMatchFilter;
import com.holonplatform.core.internal.query.filter.StringMatchFilter.MatchMode;
import com.holonplatform.core.internal.utils.ConversionUtils;

/**
 * A {@link Query} expression representing a filter (query restriction) condition.
 * 
 * @since 4.4.0
 */
public interface QueryFilter extends Expression, Serializable {

	// Filter compositions

	/**
	 * Negate this filter.
	 * @return Negated filter
	 */
	default QueryFilter not() {
		return new NotFilter(this);
	}

	/**
	 * Join another <code>filter</code> in <code>AND</code> mode.
	 * @param filter QueryFilter to join (not null)
	 * @return Conjunction of this filter and given <code>filter</code>
	 */
	default QueryFilter and(QueryFilter filter) {
		return new AndFilter(this, filter);
	}

	/**
	 * Join another <code>filter</code> in <code>OR</code> mode.
	 * @param filter QueryFilter to join (not null)
	 * @return Disjunction of this filter and given <code>filter</code>
	 */
	default QueryFilter or(QueryFilter filter) {
		return new OrFilter(this, filter);
	}

	// Builders and helpers

	/**
	 * Build a {@link QueryFilter} on given <code>expression</code>, which checks if given expression value is
	 * <code>null</code>.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter isNull(TypedExpression<T> expression) {
		return new NullFilter(expression);
	}

	/**
	 * Build a {@link QueryFilter} on given <code>expression</code>, which checks if given expression value is not
	 * <code>null</code>.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter isNotNull(TypedExpression<T> expression) {
		return new NotNullFilter(expression);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>left</code> and <code>right</code> operands, which checks if left
	 * expression value is equal to right expression value.
	 * @param <T> Expression type
	 * @param left Left operand expression (not null)
	 * @param right Right operand expression (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter eq(TypedExpression<T> left, TypedExpression<? super T> right) {
		return new EqualFilter<>(left, right);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is equal to
	 * given constant value.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param value Constant value (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter eq(TypedExpression<T> expression, T value) {
		return new EqualFilter<>(expression, ConstantExpression.create(expression, value));
	}

	/**
	 * Build a {@link QueryFilter} using given <code>left</code> and <code>right</code> operands, which checks if left
	 * expression value is not equal to right expression value.
	 * @param <T> Expression type
	 * @param left Left operand expression (not null)
	 * @param right Right operand expression (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter neq(TypedExpression<T> left, TypedExpression<? super T> right) {
		return new NotEqualFilter<>(left, right);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is not equal to
	 * given constant value.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param value Constant value (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter neq(TypedExpression<T> expression, T value) {
		return new NotEqualFilter<>(expression, ConstantExpression.create(expression, value));
	}

	/**
	 * Build a {@link QueryFilter} using given <code>left</code> and <code>right</code> operands, which checks if left
	 * expression value is less than or less than or equal to right expression value.
	 * @param <T> Expression type
	 * @param left Left operand expression (not null)
	 * @param right Right operand expression (not null)
	 * @param includeEquals <code>true</code> to include value equality, <code>false</code> otherwise
	 * @return QueryFilter
	 */
	static <T> QueryFilter lessThan(TypedExpression<T> left, TypedExpression<? super T> right, boolean includeEquals) {
		return new LessFilter<>(left, right, includeEquals);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is less than or
	 * less than or equal to given constant value.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param value Constant value (not null)
	 * @param includeEquals <code>true</code> to include value equality, <code>false</code> otherwise
	 * @return QueryFilter
	 */
	static <T> QueryFilter lessThan(TypedExpression<T> expression, T value, boolean includeEquals) {
		return new LessFilter<>(expression, ConstantExpression.create(expression, value), includeEquals);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>left</code> and <code>right</code> operands, which checks if left
	 * expression value is less than right expression value.
	 * @param <T> Expression type
	 * @param left Left operand expression (not null)
	 * @param right Right operand expression (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter lt(TypedExpression<T> left, TypedExpression<? super T> right) {
		return lessThan(left, right, false);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is less than
	 * given constant value.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param value Constant value (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter lt(TypedExpression<T> expression, T value) {
		return lessThan(expression, ConstantExpression.create(expression, value), false);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>left</code> and <code>right</code> operands, which checks if left
	 * expression value is less than or equal to right expression value.
	 * @param <T> Expression type
	 * @param left Left operand expression (not null)
	 * @param right Right operand expression (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter loe(TypedExpression<T> left, TypedExpression<? super T> right) {
		return lessThan(left, right, true);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is less than or
	 * equal to given constant value.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param value Constant value (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter loe(TypedExpression<T> expression, T value) {
		return lessThan(expression, ConstantExpression.create(expression, value), true);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>left</code> and <code>right</code> operands, which checks if left
	 * expression value is greater than or greater than or equal to right expression value.
	 * @param <T> Expression type
	 * @param left Left operand expression (not null)
	 * @param right Right operand expression (not null)
	 * @param includeEquals <code>true</code> to include value equality, <code>false</code> otherwise
	 * @return QueryFilter
	 */
	static <T> QueryFilter greaterThan(TypedExpression<T> left, TypedExpression<? super T> right,
			boolean includeEquals) {
		return new GreaterFilter<>(left, right, includeEquals);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is greater than
	 * or greater than or equal to given constant value.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param value Constant value (not null)
	 * @param includeEquals <code>true</code> to include value equality, <code>false</code> otherwise
	 * @return QueryFilter
	 */
	static <T> QueryFilter greaterThan(TypedExpression<T> expression, T value, boolean includeEquals) {
		return new GreaterFilter<>(expression, ConstantExpression.create(expression, value), includeEquals);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>left</code> and <code>right</code> operands, which checks if left
	 * expression value is greater than right expression value.
	 * @param <T> Expression type
	 * @param left Left operand expression (not null)
	 * @param right Right operand expression (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter gt(TypedExpression<T> left, TypedExpression<? super T> right) {
		return greaterThan(left, right, false);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is greater than
	 * given constant value.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param value Constant value (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter gt(TypedExpression<T> expression, T value) {
		return greaterThan(expression, ConstantExpression.create(expression, value), false);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>left</code> and <code>right</code> operands, which checks if left
	 * expression value is greater than or equal to right expression value.
	 * @param <T> Expression type
	 * @param left Left operand expression (not null)
	 * @param right Right operand expression (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter goe(TypedExpression<T> left, TypedExpression<? super T> right) {
		return greaterThan(left, right, true);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is greater than
	 * or equal to given constant value.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param value Constant value (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter goe(TypedExpression<T> expression, T value) {
		return greaterThan(expression, ConstantExpression.create(expression, value), true);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is between
	 * given <code>from</code> and <code>to</code> values.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param from From value (not null)
	 * @param to To value (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter between(TypedExpression<T> expression, T from, T to) {
		return new BetweenFilter<>(expression, from, to);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>left</code> and <code>right</code> operands, which checks if left
	 * expression value is equal to any of the right expression values.
	 * @param <T> Expression type
	 * @param left Left operand expression (not null)
	 * @param right Right operand expression (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter in(TypedExpression<T> left, TypedExpression<? super T> right) {
		return new InFilter<>(left, right);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is equal to any
	 * of the given constant values.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param values Filter values (not null)
	 * @return QueryFilter
	 */
	@SuppressWarnings("unchecked")
	static <T> QueryFilter in(TypedExpression<T> expression, T... values) {
		return in(expression, CollectionExpression.create(expression, values));
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is equal to any
	 * of the given constant values.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param values Filter values (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter in(TypedExpression<T> expression, Collection<T> values) {
		return in(expression, CollectionExpression.create(expression, values));
	}

	/**
	 * Build a {@link QueryFilter} using given <code>left</code> and <code>right</code> operands, which checks if left
	 * expression value is not included in the right expression values.
	 * @param <T> Expression type
	 * @param left Left operand expression (not null)
	 * @param right Right operand expression (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter nin(TypedExpression<T> left, TypedExpression<? super T> right) {
		return new NotInFilter<>(left, right);
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is not included
	 * in given constant values.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param values Filter values (not null)
	 * @return QueryFilter
	 */
	@SuppressWarnings("unchecked")
	static <T> QueryFilter nin(TypedExpression<T> expression, T... values) {
		return nin(expression, CollectionExpression.create(expression, values));
	}

	/**
	 * Build a {@link QueryFilter} using given <code>expression</code>, which checks if expression value is not included
	 * in given constant values.
	 * @param <T> Expression type
	 * @param expression Filter expression (not null)
	 * @param values Filter values (not null)
	 * @return QueryFilter
	 */
	static <T> QueryFilter nin(TypedExpression<T> expression, Collection<T> values) {
		return nin(expression, CollectionExpression.create(expression, values));
	}

	/**
	 * Build a <em>contains</em> {@link QueryFilter} on given {@link String} <code>expression</code>, checking if the
	 * <code>expression</code> value contains given value.
	 * @param expression Filter expression (not null)
	 * @param value Value which must be contained in expression value
	 * @param ignoreCase Whether to ignore case
	 * @return QueryFilter
	 */
	static QueryFilter contains(TypedExpression<String> expression, String value, boolean ignoreCase) {
		return new StringMatchFilter(expression, value, MatchMode.CONTAINS, ignoreCase);
	}

	/**
	 * Build a <em>starts with</em> {@link QueryFilter} on given {@link String} <code>expression</code>, checking if the
	 * <code>expression</code> value starts with given value.
	 * @param expression Filter expression (not null)
	 * @param value Value with which the expression value must start with
	 * @param ignoreCase Whether to ignore case
	 * @return QueryFilter
	 */
	static QueryFilter startsWith(TypedExpression<String> expression, String value, boolean ignoreCase) {
		return new StringMatchFilter(expression, value, MatchMode.STARTS_WITH, ignoreCase);
	}

	/**
	 * Build a <em>ends with</em> {@link QueryFilter} on given {@link String} <code>expression</code>, checking if the
	 * <code>expression</code> value ends with given value.
	 * @param expression Filter expression (not null)
	 * @param value Value with which the expression value must end with
	 * @param ignoreCase Whether to ignore case
	 * @return QueryFilter
	 */
	static QueryFilter endsWith(TypedExpression<String> expression, String value, boolean ignoreCase) {
		return new StringMatchFilter(expression, value, MatchMode.ENDS_WITH, ignoreCase);
	}

	/**
	 * Build a {@link QueryFilter} as ne negation of given <code>filter</code>.
	 * @param filter Filter to negate
	 * @return QueryFilter
	 */
	static QueryFilter not(QueryFilter filter) {
		return new NotFilter(filter);
	}

	/**
	 * Build a {@link QueryFilter} with the conjunction (AND) of all given not null <code>filters</code>
	 * @param filters Filters
	 * @return QueryFilter representing the conjunction (AND) of all given QueryFilters, or an empty optional if no
	 *         not-null filter is given
	 */
	static Optional<QueryFilter> allOf(QueryFilter... filters) {
		if (filters != null) {
			List<QueryFilter> fs = Arrays.asList(filters).stream().filter(f -> f != null).collect(Collectors.toList());
			if (!fs.isEmpty()) {
				return Optional.of((fs.size() == 1) ? fs.get(0) : new AndFilter(fs));
			}
		}
		return Optional.empty();
	}

	/**
	 * Build a {@link QueryFilter} with the conjunction (AND) of all given <code>filters</code>
	 * @param <Q> Actual filter type
	 * @param filters Filters
	 * @return QueryFilter representing the conjunction (AND) of all given QueryFilters, or an empty optional if given
	 *         Iterable has no elements
	 */
	static <Q extends QueryFilter> Optional<QueryFilter> allOf(Iterable<Q> filters) {
		final List<Q> fs = ConversionUtils.iterableAsList(filters);
		return fs.isEmpty() ? Optional.empty()
				: (fs.size() == 1) ? Optional.of(fs.get(0))
						: Optional.of(new AndFilter(ConversionUtils.iterableAsList(filters)));
	}

	/**
	 * Build a {@link QueryFilter} with the disjunction (OR) of all given not null <code>filters</code>
	 * @param filters Filters
	 * @return QueryFilter representing the disjunction (OR) of all given filters, or an empty optional if no not-null
	 *         filter is given
	 */
	static Optional<QueryFilter> anyOf(QueryFilter... filters) {
		if (filters != null && filters.length > 0) {
			List<QueryFilter> fs = Arrays.asList(filters).stream().filter(f -> f != null).collect(Collectors.toList());
			if (!fs.isEmpty()) {
				return Optional.of((fs.size() == 1) ? fs.get(0) : new OrFilter(fs));
			}
		}
		return Optional.empty();
	}

	/**
	 * Build a {@link QueryFilter} with the disjunction (OR) of all given <code>filters</code>
	 * @param <Q> Actual filter type
	 * @param filters Filters
	 * @return QueryFilter representing the disjunction (OR) of all given QueryFilters, or an empty optional if given
	 *         Iterable has no elements
	 */
	static <Q extends QueryFilter> Optional<QueryFilter> anyOf(Iterable<Q> filters) {
		final List<Q> fs = ConversionUtils.iterableAsList(filters);
		return fs.isEmpty() ? Optional.empty()
				: (fs.size() == 1) ? Optional.of(fs.get(0))
						: Optional.of(new OrFilter(ConversionUtils.iterableAsList(filters)));
	}

	/**
	 * Interface implemented by classes which support {@link QueryFilter}s addition.
	 * @param <C> Concrete type
	 */
	public interface QueryFilterSupport<C extends QueryFilterSupport<C>> {

		/**
		 * Add a filter clause
		 * @param filter Filter clause to add. If <code>null</code>, the filter clause is ignored.
		 * @return the QueryFilterSupport which contains the added filter clause (usually the same instance)
		 */
		C filter(QueryFilter filter);

	}

	/**
	 * Convenience interface to create an {@link ExpressionResolver} to resolve a custom {@link QueryFilter} class into
	 * a standard {@link QueryFilter}.
	 * @param <T> QueryFilter type to be resolved
	 */
	public interface QueryFilterResolver<T extends QueryFilter> extends ExpressionResolver<T, QueryFilter> {

		@Override
		default Class<? extends QueryFilter> getResolvedType() {
			return QueryFilter.class;
		}

		/**
		 * Create an {@link ExpressionResolver} to resolve a custom {@link QueryFilter} class into a standard
		 * {@link QueryFilter} using the given resolver function.
		 * @param <T> QueryFilter type to be resolved
		 * @param type QueryFilter type to be resolved
		 * @param function Resolver function
		 * @return A new {@link ExpressionResolver}
		 */
		static <T extends QueryFilter> ExpressionResolver<T, QueryFilter> create(Class<? extends T> type,
				ExpressionResolverFunction<T, QueryFilter> function) {
			return new CallbackExpressionResolver<>(type, QueryFilter.class, function);
		}

	}

	/**
	 * A {@link QueryFilter} which represents a query filters composition.
	 */
	public interface CompositeQueryFilter extends QueryFilter {

		/**
		 * Get the {@link QueryFilter}s which compose the filter.
		 * @return The query filters list, empty if none
		 */
		List<QueryFilter> getComposition();

	}

}
