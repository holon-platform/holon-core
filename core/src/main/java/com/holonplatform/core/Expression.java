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
package com.holonplatform.core;

import java.util.Optional;

import com.holonplatform.core.ExpressionResolver.ResolutionContext;

/**
 * Represents a generic expression with validation support.
 *
 * @since 5.0.0
 */
public interface Expression {

	/**
	 * Validate this expressino.
	 * @throws InvalidExpressionException If the expression is not valid
	 */
	void validate() throws InvalidExpressionException;

	/**
	 * A function to translate an {@link Expression} into another {@link Expression} type.
	 * @param <E> Expression type
	 * @param <R> Resolved expression type
	 * @see ExpressionResolver
	 */
	@FunctionalInterface
	public interface ExpressionResolverFunction<E extends Expression, R extends Expression> {

		/**
		 * Try to resolve given <code>expression</code>. If the resolver is not able to resolve given expression type,
		 * an empty Optional must be returned and the expression resolution engine should delegate the resolution to the
		 * next resolver, if available.
		 * <p>
		 * Concrete resolution environments could provide a more specialized {@link ResolutionContext} interface.
		 * </p>
		 * @param expression Expression to resolve
		 * @param context Resolution context
		 * @return Resolved expression, or an empty Optional if this resolver is not able to resolve given expression
		 * @throws InvalidExpressionException An error occurred during expression resolution
		 */
		Optional<R> resolve(E expression, ResolutionContext context) throws InvalidExpressionException;

	}

	/**
	 * Exception related to {Expression} validation or resolution failures.
	 */
	public static class InvalidExpressionException extends RuntimeException {

		private static final long serialVersionUID = 2984685067061661127L;

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public InvalidExpressionException(String message) {
			super(message);
		}

		/**
		 * Constructor with nested exception
		 * @param cause Nested exception
		 */
		public InvalidExpressionException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public InvalidExpressionException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
