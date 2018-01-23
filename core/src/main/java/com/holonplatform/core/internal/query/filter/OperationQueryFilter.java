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
package com.holonplatform.core.internal.query.filter;

import java.util.Optional;

import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.query.QueryFilter;

/**
 * A {@link QueryFilter} which represents an operation with operator and arguments.
 * 
 * @param <T> Operation subject type
 * 
 * @since 5.1.0
 */
public interface OperationQueryFilter<T> extends QueryFilter {

	/**
	 * Get the left hand operand expression.
	 * @return The left hand operand expression
	 */
	TypedExpression<T> getLeftOperand();

	/**
	 * Get the operator.
	 * @return The operator (not null)
	 */
	FilterOperator getOperator();

	/**
	 * Get the right hand operand expression, if supported.
	 * @return Optional right hand operand expression
	 */
	Optional<TypedExpression<? super T>> getRightOperand();

	/**
	 * Filter operators.
	 * 
	 * @since 4.4.0
	 */
	public enum FilterOperator {

		/**
		 * EQUAL
		 */
		EQUAL("=", "$eq"),

		/**
		 * NOT EQUAL
		 */
		NOT_EQUAL("<>", "$ne"),

		/**
		 * GREATER THAN
		 */
		GREATER_THAN(">", "$gt"),

		/**
		 * GREATER OR EQUAL
		 */
		GREATER_OR_EQUAL(">=", "$goe"),

		/**
		 * LESS THAN
		 */
		LESS_THAN("<", "$lt"),

		/**
		 * LESS OR EQUAL
		 */
		LESS_OR_EQUAL("<=", "$loe"),

		/**
		 * BETWEEN
		 */
		BETWEEN("[]", "$btw"),

		/**
		 * IN
		 */
		IN("^", "$in"),

		/**
		 * NOT IN
		 */
		NOT_IN("!^", "$nin"),

		/**
		 * MATCH
		 */
		MATCH("~", "$mtch"),

		/*
		 * IS NULL
		 */
		NULL("-", "$null"),

		/**
		 * IS NOT NULL
		 */
		NOT_NULL("+", "$notNull");

		/*
		 * Operator symbol
		 */
		private final String symbol;

		/*
		 * Serialized form id
		 */
		private final String serializedId;

		/**
		 * Constructor
		 * @param symbol Operator symbol
		 * @param serializedId Serialized form id
		 */
		private FilterOperator(String symbol, String serializedId) {
			this.symbol = symbol;
			this.serializedId = serializedId;
		}

		/**
		 * Get operator symbol
		 * @return Symbol
		 */
		public String getSymbol() {
			return symbol;
		}

		/**
		 * Serialized form id
		 * @return Serialized id
		 */
		public String getSerializedId() {
			return serializedId;
		}

		/**
		 * Parse a {@link FilterOperator} from symbol
		 * @param symbol Symbol
		 * @return Filter operator, or <code>null</code> if symbol is not associated to any operator
		 */
		public static FilterOperator fromSymbol(String symbol) {
			if (symbol != null) {
				for (FilterOperator operator : values()) {
					if (symbol.equals(operator.symbol)) {
						return operator;
					}
				}
			}
			return null;
		}

		/**
		 * Get the {@link FilterOperator} matching the given serialized id
		 * @param serializedId Serialized operator id
		 * @return FilterOperator, or <code>null</code> if none match
		 */
		public static FilterOperator deserialize(String serializedId) {
			if (serializedId != null) {
				for (FilterOperator operator : values()) {
					if (serializedId.equals(operator.serializedId)) {
						return operator;
					}
				}
			}
			return null;
		}

	}

}
