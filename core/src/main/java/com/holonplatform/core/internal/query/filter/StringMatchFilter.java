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

import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.query.QueryFilterVisitor;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.ConstantExpression;
import com.holonplatform.core.query.QueryFilter;

/**
 * Filter which represents a match condition on text values.
 * <p>
 * This filter supports the ignore-case condition, if supported by underlyng data store, to match text values ignoring
 * case.
 * </p>
 * 
 * @since 4.4.0
 * 
 * @see QueryFilter
 */
public class StringMatchFilter extends AbstractOperationQueryFilter<String> {

	private static final long serialVersionUID = -1873151412251025897L;

	/**
	 * Matching mode
	 */
	public enum MatchMode {

		/**
		 * Contains the value
		 */
		CONTAINS,

		/**
		 * Starts with the value
		 */
		STARTS_WITH,

		/**
		 * Ends with the value
		 */
		ENDS_WITH

	}

	/*
	 * Value
	 */
	private final String value;

	/*
	 * Match mode
	 */
	private final MatchMode matchMode;

	/*
	 * Ignore case
	 */
	private final boolean ignoreCase;

	/**
	 * Constructor.
	 * @param expression Operand expression
	 * @param value Filter value
	 * @param matchMode Match mode
	 * @param ignoreCase Set if to match like pattern ignoring case
	 */
	public StringMatchFilter(TypedExpression<String> expression, String value, MatchMode matchMode,
			boolean ignoreCase) {
		super(expression, FilterOperator.MATCH, ConstantExpression.create(expression, value));
		ObjectUtils.argumentNotNull(value, "Value must be not null");
		ObjectUtils.argumentNotNull(matchMode, "Match mode must be not null");
		this.value = value;
		this.matchMode = matchMode;
		this.ignoreCase = ignoreCase;
	}

	/**
	 * Get the value to match.
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Get the match mode
	 * @return the match mode
	 */
	public MatchMode getMatchMode() {
		return matchMode;
	}

	/**
	 * Match like pattern ignoring case
	 * @return <code>true</code> to ignore case in pattern match
	 */
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.filter.AbstractOperationQueryFilter#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		super.validate();
		if (getValue() == null) {
			throw new InvalidExpressionException("Null value to match");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.VisitableQueryData#accept(com.holonplatform.core.query.QueryDataVisitor,
	 * java.lang.Object)
	 */
	@Override
	public <R, C> R accept(QueryFilterVisitor<R, C> visitor, C context) {
		return visitor.visit(this, context);
	}

}
