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

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.query.DefaultPathExpression;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QuerySort.SortDirection;

/**
 * A {@link QueryExpression} wich represents a {@link Path}.
 * 
 * @param <T> Path and expression type
 *
 * @since 5.0.0
 */
public interface PathExpression<T> extends Path<T>, QueryExpression<T>, QueryProjection<T> {

	// ------- sorts

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

	// ------- Builders

	/**
	 * Create a {@link PathExpression}.
	 * @param <T> Path type
	 * @param name Path name (not null)
	 * @param type Path type (not null)
	 * @return A new {@link PathExpression}
	 */
	static <T> PathExpression<T> create(String name, Class<? extends T> type) {
		return new DefaultPathExpression<>(name, type);
	}

	/**
	 * Create a {@link PathExpression} from given {@link Path}.
	 * @param <T> Path type
	 * @param path Suorce path
	 * @return A new {@link PathExpression} with the same name, type and parent of the given path
	 */
	static <T> PathExpression<T> from(Path<T> path) {
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		DefaultPathExpression<T> expression = new DefaultPathExpression<>(path.getName(), path.getType());
		path.getParent().ifPresent(p -> expression.parent(p));
		return expression;
	}

}
