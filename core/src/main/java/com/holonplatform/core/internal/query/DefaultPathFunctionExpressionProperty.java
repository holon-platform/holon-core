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
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.query.FunctionExpression.PathFunctionExpressionProperty;
import com.holonplatform.core.query.QueryFunction;

/**
 * A {@link PathFunctionExpression} implemented as a {@link Property} to be transported into a {@link PropertyBox}.
 *
 * @param <P> Path type
 * @param <T> Property type
 *
 * @since 5.0.0
 */
public class DefaultPathFunctionExpressionProperty<P, T> extends DefaultFunctionExpressionProperty<T>
		implements PathFunctionExpressionProperty<P, T> {

	private static final long serialVersionUID = -2404584269572595638L;

	private final Path<P> path;

	public DefaultPathFunctionExpressionProperty(QueryFunction<T> function, Path<P> path) {
		super(function);
		this.path = path;
	}

	@SuppressWarnings("unchecked")
	public DefaultPathFunctionExpressionProperty(QueryFunction<T> function, PathProperty<P> property) {
		super(function);
		this.path = property;
		// clone configuration
		if (function.getResultType() == property.getType()) {
			((PathProperty<T>) property).getConverter().map(c -> converter(c));
		}
		if (property.getConfiguration() != null) {
			property.getConfiguration().getTemporalType().ifPresent(t -> temporalType(t));
			property.getConfiguration().forEachParameter((n, v) -> configuration(n, v));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.PathFunctionExpression#getPath()
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
		return "DefaultPathFunctionExpressionProperty [function=" + getFunction() + ", path=" + path + "]";
	}

}
