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

import com.holonplatform.core.internal.query.filter.AndFilter;
import com.holonplatform.core.internal.query.filter.BetweenFilter;
import com.holonplatform.core.internal.query.filter.EqualFilter;
import com.holonplatform.core.internal.query.filter.GreaterFilter;
import com.holonplatform.core.internal.query.filter.InFilter;
import com.holonplatform.core.internal.query.filter.LessFilter;
import com.holonplatform.core.internal.query.filter.StringMatchFilter;
import com.holonplatform.core.internal.query.filter.NotEqualFilter;
import com.holonplatform.core.internal.query.filter.NotFilter;
import com.holonplatform.core.internal.query.filter.NotInFilter;
import com.holonplatform.core.internal.query.filter.NotNullFilter;
import com.holonplatform.core.internal.query.filter.NullFilter;
import com.holonplatform.core.internal.query.filter.OrFilter;
import com.holonplatform.core.query.QueryFilter;

/**
 * Visitor for standard {@link QueryFilter} instances.
 * 
 * @param <R> Visitor result type
 * @param <C> Visitor context type
 * 
 * @since 5.0.0
 */
public interface QueryFilterVisitor<R, C> {

	/**
	 * Visitable {@link QueryFilter}.
	 */
	public interface VisitableQueryFilter extends QueryFilter {

		/**
		 * Accept the visitor with the given context.
		 * @param <R> Visitor result type
		 * @param <C> Visitor context type
		 * @param visitor Visitor
		 * @param context Context of visit
		 * @return Result of visit
		 */
		<R, C> R accept(QueryFilterVisitor<R, C> visitor, C context);

	}

	/**
	 * Visit {@link NullFilter}
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	R visit(NullFilter filter, C context);

	/**
	 * Visit {@link NotNullFilter}
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	R visit(NotNullFilter filter, C context);

	/**
	 * Visit {@link EqualFilter}.
	 * @param <T> Filter value type
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(EqualFilter<T> filter, C context);

	/**
	 * Visit {@link NotEqualFilter}.
	 * @param <T> Filter value type
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(NotEqualFilter<T> filter, C context);

	/**
	 * Visit {@link GreaterFilter}.
	 * @param <T> Filter value type
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(GreaterFilter<T> filter, C context);

	/**
	 * Visit {@link LessFilter}.
	 * @param <T> Filter value type
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(LessFilter<T> filter, C context);

	/**
	 * Visit {@link InFilter}.
	 * @param <T> Filter value type
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(InFilter<T> filter, C context);

	/**
	 * Visit {@link NotInFilter}.
	 * @param <T> Filter value type
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(NotInFilter<T> filter, C context);

	/**
	 * Visit {@link BetweenFilter}.
	 * @param <T> Filter value type
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(BetweenFilter<T> filter, C context);

	/**
	 * Visit {@link StringMatchFilter}
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	R visit(StringMatchFilter filter, C context);

	/**
	 * Visit {@link AndFilter}
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	R visit(AndFilter filter, C context);

	/**
	 * Visit {@link OrFilter}
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	R visit(OrFilter filter, C context);

	/**
	 * Visit {@link NotFilter}
	 * @param filter Filter to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	R visit(NotFilter filter, C context);

}
