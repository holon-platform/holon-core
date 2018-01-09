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
import com.holonplatform.core.query.QueryFunction.Avg;
import com.holonplatform.core.query.QueryFunction.PropertyQueryFunction;
import com.holonplatform.core.query.QueryFunction.Sum;

/**
 * A {@link QueryExpression} of {@link Number} type.
 *
 * @param <N> Numeric expression type
 * 
 * @since 5.1.0
 */
public interface NumericQueryExpression<N extends Number> extends QueryExpression<N> {

	// ------- Function builders

	/**
	 * Creates an aggregation function which returns the average value of expression.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A new {@link Avg} aggregation function expression
	 */
	default Avg avg() {
		return Avg.create(this);
	}

	/**
	 * Creates an aggregation function which returns the sum of the expression values.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A new {@link Sum} aggregation function expression
	 */
	default Sum<N> sum() {
		return Sum.create(this);
	}

}
