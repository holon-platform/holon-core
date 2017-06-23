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
package com.holonplatform.core.internal;

import java.util.Optional;

import com.holonplatform.core.Expression;
import com.holonplatform.core.Expression.ExpressionResolverFunction;
import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * A {@link ExpressionResolver} which uses a {@link ExpressionResolverFunction} in conjunction with expression and
 * resolution type to create the resolver implementation.
 *
 * @param <E> Expression type
 * @param <R> Resolved expression type
 * 
 * @since 5.0.0
 */
public class CallbackExpressionResolver<E extends Expression, R extends Expression>
		implements ExpressionResolver<E, R> {

	private static final long serialVersionUID = -2506193547882496213L;

	private final Class<? extends E> expressionType;
	private final Class<? extends R> resolvedType;
	private final ExpressionResolverFunction<E, R> function;

	public CallbackExpressionResolver(Class<? extends E> expressionType, Class<? extends R> resolvedType,
			ExpressionResolverFunction<E, R> function) {
		super();
		ObjectUtils.argumentNotNull(expressionType, "Expression type must be not null");
		ObjectUtils.argumentNotNull(resolvedType, "Resolved type must be not null");
		ObjectUtils.argumentNotNull(function, "Resolver function must be not null");
		this.expressionType = expressionType;
		this.resolvedType = resolvedType;
		this.function = function;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression.ExpressionResolverFunction#resolve(com.holonplatform.core.Expression,
	 * com.holonplatform.core.ExpressionResolver.ResolutionContext)
	 */
	@Override
	public Optional<R> resolve(E expression, com.holonplatform.core.ExpressionResolver.ResolutionContext context)
			throws InvalidExpressionException {
		return function.resolve(expression, context);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ExpressionResolver#getExpressionType()
	 */
	@Override
	public Class<? extends E> getExpressionType() {
		return expressionType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ExpressionResolver#getResolvedType()
	 */
	@Override
	public Class<? extends R> getResolvedType() {
		return resolvedType;
	}

}
