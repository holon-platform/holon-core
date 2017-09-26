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

import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.BeanProjection;
import com.holonplatform.core.query.CountAllProjection;
import com.holonplatform.core.query.FunctionExpression;
import com.holonplatform.core.query.PropertySetProjection;
import com.holonplatform.core.query.QueryProjection;

/**
 * Visitor for standard {@link QueryProjection}s.
 * 
 * @param <R> Visitor result type
 * @param <C> Visitor context type
 * 
 * @since 5.0.0
 */
public interface QueryProjectionVisitor<R, C> {

	/**
	 * Visitable {@link QueryProjection}.
	 */
	public interface VisitableQueryProjection<T> extends QueryProjection<T> {

		/**
		 * Accept the visitor with the given context.
		 * @param <R> Visitor result type
		 * @param <C> Visitor context type
		 * @param visitor Visitor
		 * @param context Context of visit
		 * @return Result of visit
		 */
		<R, C> R accept(QueryProjectionVisitor<R, C> visitor, C context);

	}

	/**
	 * Visit a {@link DataTarget}.
	 * @param <T> Target type
	 * @param projection Projection to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(DataTarget<T> projection, C context);

	/**
	 * Visit a {@link PathProperty}.
	 * @param <T> Property type
	 * @param projection Projection to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(PathProperty<T> projection, C context);

	/**
	 * Visit a {@link ConstantExpressionProjection}.
	 * @param <T> Expression type
	 * @param projection Projection to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(ConstantExpressionProjection<T> projection, C context);

	/**
	 * Visit a {@link PropertySet} projection.
	 * @param <T> Function expression type
	 * @param projection Projection to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(FunctionExpression<T> projection, C context);

	/**
	 * Visit a {@link PropertySet} projection.
	 * @param projection Projection to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	R visit(PropertySetProjection projection, C context);

	/**
	 * Visit a {@link BeanProjection} projection.
	 * @param <T> Bean type
	 * @param projection Projection to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	<T> R visit(BeanProjection<T> projection, C context);

	/**
	 * Visit a {@link CountAllProjection} projection.
	 * @param projection Projection to visit
	 * @param context Visiting context
	 * @return Visit result
	 */
	R visit(CountAllProjection projection, C context);

}
