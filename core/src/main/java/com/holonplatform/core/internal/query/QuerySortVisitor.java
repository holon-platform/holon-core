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
package com.holonplatform.core.internal.query;

import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.query.QuerySort.CompositeQuerySort;
import com.holonplatform.core.query.QuerySort.PathQuerySort;

/**
 * Visitor for standard {@link QuerySort}s.
 * 
 * @param <R> Visitor result type
 * @param <C> Visitor context type
 * 
 * @since 5.0.0
 */
public interface QuerySortVisitor<R, C> {

	/**
	 * Visitable {@link QuerySort}.
	 */
	public interface VisitableQuerySort extends QuerySort {

		/**
		 * Accept the visitor with the given context.
		 * @param <R> Visitor result type
		 * @param <C> Visitor context type
		 * @param visitor Visitor
		 * @param context Context of visit
		 * @return Result of visit
		 */
		<R, C> R accept(QuerySortVisitor<R, C> visitor, C context);

	}

	/**
	 * Visit a {@link PathQuerySort}.
	 * @param sort Sort to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	R visit(PathQuerySort<?> sort, C context);

	/**
	 * Visit a {@link CompositeQuerySort}.
	 * @param sort Sort to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	R visit(CompositeQuerySort sort, C context);

}
