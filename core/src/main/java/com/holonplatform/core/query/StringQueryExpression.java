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

import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.query.QueryFunction.PropertyQueryFunction;
import com.holonplatform.core.query.StringFunction.Lower;
import com.holonplatform.core.query.StringFunction.Upper;

/**
 * A {@link QueryExpression} of {@link String} type.
 *
 * @since 5.1.0
 */
public interface StringQueryExpression extends QueryExpression<String> {

	// ------- Query filter builders

	/**
	 * Build a <em>contains</em> query filter, checking if the property value contains given value.
	 * @param value Value which must be contained in expression value
	 * @param ignoreCase Whether to ignore case
	 * @return The QueryFilter
	 */
	default QueryFilter contains(String value, boolean ignoreCase) {
		return QueryFilter.contains(this, value, ignoreCase);
	}

	/**
	 * Build a <em>starts with</em> query filter, checking if the property value starts with given value.
	 * @param value Value with which the expression value must start with
	 * @param ignoreCase Whether to ignore case
	 * @return The QueryFilter
	 */
	default QueryFilter startsWith(String value, boolean ignoreCase) {
		return QueryFilter.startsWith(this, value, ignoreCase);
	}

	/**
	 * Build a <em>ends with</em> query filter, checking if the property value ends with given value.
	 * @param value Value with which the expression value must end with
	 * @param ignoreCase Whether to ignore case
	 * @return The QueryFilter
	 */
	default QueryFilter endsWith(String value, boolean ignoreCase) {
		return QueryFilter.endsWith(this, value, ignoreCase);
	}

	/**
	 * Build a <em>contains</em> query filter, checking if the property value contains given value, in a case-sentive
	 * fashion.
	 * @param value Value which must be contained in expression value
	 * @return The QueryFilter
	 */
	default QueryFilter contains(String value) {
		return contains(value, false);
	}

	/**
	 * Build a <em>contains</em> query filter, checking if the property value contains given value, ignoring case.
	 * @param value Value which must be contained in expression value
	 * @return The QueryFilter
	 */
	default QueryFilter containsIgnoreCase(String value) {
		return contains(value, true);
	}

	/**
	 * Build a <em>starts with</em> query filter, checking if the property value starts with given value, in a
	 * case-sentive fashion.
	 * @param value Value with which the expression value must start with
	 * @return The QueryFilter
	 */
	default QueryFilter startsWith(String value) {
		return startsWith(value, false);
	}

	/**
	 * Build a <em>starts with</em> query filter, checking if the property value starts with given value, ignoring case.
	 * @param value Value with which the expression value must start with
	 * @return The QueryFilter
	 */
	default QueryFilter startsWithIgnoreCase(String value) {
		return startsWith(value, true);
	}

	/**
	 * Build a <em>ends with</em> query filter, checking if the property value ends with given value, in a case-sentive
	 * fashion.
	 * @param value Value with which the expression value must end with
	 * @return The QueryFilter
	 */
	default QueryFilter endsWith(String value) {
		return endsWith(value, false);
	}

	/**
	 * Build a <em>ends with</em> query filter, checking if the property value ends with given value, ignoring case.
	 * @param value Value with which the expression value must end with
	 * @return The QueryFilter
	 */
	default QueryFilter endsWithIgnoreCase(String value) {
		return endsWith(value, true);
	}

	// ------- Function builders

	/**
	 * Creates a function to convert the String data into lowercase.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A new {@link Lower} function
	 */
	default Lower lower() {
		return Lower.create(this);
	}

	/**
	 * Creates a function to convert the String data into uppercase.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A new {@link Lower} function
	 */
	default Upper upper() {
		return Upper.create(this);
	}

}
