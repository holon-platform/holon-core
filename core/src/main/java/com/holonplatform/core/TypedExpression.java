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

import java.util.Optional;

import com.holonplatform.core.temporal.TemporalType;

/**
 * An {@link Expression} with a declared type.
 * 
 * @param <T> Expression type
 *
 * @since 5.1.0
 */
public interface TypedExpression<T> extends Expression {

	/**
	 * Get the expression type.
	 * @return Expression type
	 */
	Class<? extends T> getType();

	/**
	 * If the expression type represents a temporal type, get the actual {@link TemporalType} of the type.
	 * @return Optional temporal type
	 */
	default Optional<TemporalType> getTemporalType() {
		return TemporalType.getTemporalType(getType());
	}

	/**
	 * Checks if this expression is a {@link ConverterExpression}.
	 * @return If this expression is a {@link ConverterExpression} returns the expression itself as a
	 *         {@link ConverterExpression}, or an empty Optional otherwise
	 */
	default Optional<ConverterExpression<T>> isConverterExpression() {
		return Optional.ofNullable(
				ConverterExpression.class.isAssignableFrom(getClass()) ? (ConverterExpression<T>) this : null);
	}

	/**
	 * Checks if this expression is a {@link CollectionExpression}.
	 * @return If this expression is a {@link CollectionExpression} returns the expression itself as a
	 *         {@link CollectionExpression}, or an empty Optional otherwise
	 */
	default Optional<CollectionExpression<?, ?>> isCollectionExpression() {
		return Optional.ofNullable(
				CollectionExpression.class.isAssignableFrom(getClass()) ? (CollectionExpression<?, ?>) this : null);
	}

}
