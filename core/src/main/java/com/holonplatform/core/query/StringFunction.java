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

import com.holonplatform.core.Expression;
import com.holonplatform.core.Path;
import com.holonplatform.core.internal.query.function.LowerFunction;
import com.holonplatform.core.internal.query.function.UpperFunction;
import com.holonplatform.core.query.FunctionExpression.PathFunctionExpression;
import com.holonplatform.core.query.FunctionExpression.PathFunctionExpressionProperty;

/**
 * Represents a {@link QueryFunction} on a String data type.
 * 
 * @since 5.1.0
 */
public interface StringFunction extends QueryFunction<String> {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFunction#getResultType()
	 */
	@Override
	default Class<? extends String> getResultType() {
		return String.class;
	}

	/**
	 * Function to convert a string to lowercase.
	 */
	public interface Lower extends StringFunction {

		/**
		 * Create a new {@link Lower} function instance.
		 * @return New {@link Lower} function instance
		 */
		static Lower create() {
			return new LowerFunction();
		}

		/**
		 * Create a {@link Lower} function {@link Expression} using given <code>path</code> as function argument.
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Lower} function expression on given path
		 */
		static PathFunctionExpression<String, String> of(Path<String> path) {
			return PathFunctionExpressionProperty.create(create(), path);
		}

	}

	/**
	 * Function to convert a string to uppercase.
	 */
	public interface Upper extends StringFunction {

		/**
		 * Create a new {@link Lower} function instance.
		 * @return New {@link Lower} function instance
		 */
		static Upper create() {
			return new UpperFunction();
		}

		/**
		 * Create a {@link Upper} function {@link Expression} using given <code>path</code> as function argument.
		 * @param path Path to which to apply the function (not null)
		 * @return A {@link Upper} function expression on given path
		 */
		static PathFunctionExpression<String, String> of(Path<String> path) {
			return PathFunctionExpressionProperty.create(create(), path);
		}

	}

}
