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

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.FunctionExpression.PathFunctionExpression;
import com.holonplatform.core.query.QueryFunction;

/**
 * Default {@link PathFunctionExpression} implementation.
 *
 * @since 5.0.0
 */
public class DefaultPathFunctionExpression<P, T> extends DefaultFunctionExpression<T>
		implements PathFunctionExpression<P, T> {

	private final Path<P> path;

	/**
	 * Constructor
	 * @param function Expression function (not null)
	 * @param path Path to which to apply the function (not null)
	 */
	public DefaultPathFunctionExpression(QueryFunction<T> function, Path<P> path) {
		super(function);
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		this.path = path;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.AggregationExpression#getPath()
	 */
	@Override
	public Path<P> getPath() {
		return path;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultPathFunctionExpression [function=" + getFunction() + ", path=" + path + "]";
	}

}
