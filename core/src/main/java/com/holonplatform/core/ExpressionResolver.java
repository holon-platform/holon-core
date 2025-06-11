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
package com.holonplatform.core;

import java.io.Serializable;
import java.util.Optional;

import jakarta.annotation.Priority;

import com.holonplatform.core.Expression.ExpressionResolverFunction;
import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.internal.CallbackExpressionResolver;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Resolver to translate an {@link Expression} into another {@link Expression} type.
 * <p>
 * The {@link Priority} annotation can be used on a resolver implementation class to control the invokation order within
 * a set of resolvers. The less is the priority value, the more will be the priority level of the resolver.
 * </p>
 *
 * @param <E> Expression type
 * @param <R> Resolved expression type
 * 
 * @since 5.0.0
 */
public interface ExpressionResolver<E extends Expression, R extends Expression>
		extends ExpressionResolverFunction<E, R>, Serializable {

	/**
	 * Default {@link ExpressionResolver} priority if not specified using {@link Priority} annotation.
	 */
	public static final int DEFAULT_PRIORITY = 10000;

	/**
	 * Get the expression type which this resolver can handle.
	 * @return Expression type (not null)
	 */
	Class<? extends E> getExpressionType();

	/**
	 * Get the resolved expression type
	 * @return Resolved expression type (not null)
	 */
	Class<? extends R> getResolvedType();

	/**
	 * Create an {@link ExpressionResolver} providing expression types and resolution function.
	 * @param <E> Expression type
	 * @param <R> Resolved expression type
	 * @param expressionType The expression type which this resolver can handle (not null)
	 * @param resolvedType The resolved expression type (not null)
	 * @param function Actual resolver function (not null)
	 * @return A new {@link ExpressionResolver}
	 */
	static <E extends Expression, R extends Expression> ExpressionResolver<E, R> create(
			Class<? extends E> expressionType, Class<? extends R> resolvedType,
			ExpressionResolverFunction<E, R> function) {
		return new CallbackExpressionResolver<>(expressionType, resolvedType, function);
	}

	/**
	 * {@link Expression} resolution context.
	 */
	public interface ResolutionContext extends ExpressionResolverHandler {

	}

	/**
	 * {@link ExpressionResolver} provider.
	 * 
	 * @since 5.1.0
	 */
	public interface ExpressionResolverProvider {

		/**
		 * Get the available {@link ExpressionResolver}s.
		 * @return the available {@link ExpressionResolver}s iterable (not null)
		 */
		@SuppressWarnings("rawtypes")
		Iterable<ExpressionResolver> getExpressionResolvers();

	}

	/**
	 * Handler to perform {@link Expression} resolution using a set of registered {@link ExpressionResolver}s.
	 */
	public interface ExpressionResolverHandler extends ExpressionResolverProvider {

		/**
		 * Try to resolve given <code>expression</code> to obtain an {@link Expression} of the specified
		 * <code>resolutionType</code>, using the suitable {@link ExpressionResolver}s among all available resolvers for
		 * given expression and resolution type.
		 * <p>
		 * {@link ExpressionResolver}s invokation order is defined relying on {@link Priority} annotation on resolvers
		 * class, if available.
		 * </p>
		 * @param <E> Expression type to resolve
		 * @param <R> Resolved expression type
		 * @param expression Expression to resolve (not null)
		 * @param resolutionType Type of the expression to obtain from resolvers (not null)
		 * @param context Resolution context
		 * @return Optional resolved expression, empty if none of the suitable {@link ExpressionResolver}s, if any,
		 *         resolved the expression
		 * @throws InvalidExpressionException If an error occurred during expression resolution
		 */
		<E extends Expression, R extends Expression> Optional<R> resolve(E expression, Class<R> resolutionType,
				ResolutionContext context) throws InvalidExpressionException;

	}

	/**
	 * Interface to declare the support for {@link ExpressionResolver} by a class.
	 */
	public interface ExpressionResolverSupport {

		/**
		 * Add an {@link ExpressionResolver}.
		 * @param <E> Expression type
		 * @param <R> Resolved expression type
		 * @param expressionResolver Resolver to add (not null)
		 */
		<E extends Expression, R extends Expression> void addExpressionResolver(
				ExpressionResolver<E, R> expressionResolver);

		/**
		 * Remove an {@link ExpressionResolver}.
		 * @param <E> Expression type
		 * @param <R> Resolved expression type
		 * @param expressionResolver Resolver to remove (not null)
		 */
		<E extends Expression, R extends Expression> void removeExpressionResolver(
				ExpressionResolver<E, R> expressionResolver);

		/**
		 * Add all expression resolvers provided by given <code>resolvers</code> Iterable.
		 * @param resolvers Expression resolvers to add (not null)
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		default void addExpressionResolvers(Iterable<? extends ExpressionResolver> resolvers) {
			ObjectUtils.argumentNotNull(resolvers, "ExpressionResolvers to add must be not null");
			resolvers.forEach(r -> addExpressionResolver(r));
		}

	}

	/**
	 * Builder fragment to support {@link ExpressionResolver} registration.
	 * @param <C> Concrete builder type
	 */
	public interface ExpressionResolverBuilder<C extends ExpressionResolverBuilder<C>> {

		/**
		 * Add an {@link ExpressionResolver}.
		 * @param <E> Expression type
		 * @param <R> Resolved expression type
		 * @param expressionResolver The resolver to add (not null)
		 * @return this
		 */
		<E extends Expression, R extends Expression> C withExpressionResolver(
				ExpressionResolver<E, R> expressionResolver);

	}

}
