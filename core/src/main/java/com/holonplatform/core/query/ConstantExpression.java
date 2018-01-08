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

import java.util.Collection;

import com.holonplatform.core.internal.query.DefaultCollectionExpression;
import com.holonplatform.core.internal.query.DefaultConstantExpression;

/**
 * Constant value {@link QueryExpression}.
 * 
 * @param <T> Expression type
 * @param <E> Concrete value type
 * 
 * @since 5.0.0
 */
public interface ConstantExpression<T, E> extends QueryExpression<E> {

	/**
	 * Get the constant expression value
	 * @return The expression value
	 */
	T getValue();

	/**
	 * Create a {@link ConstantExpression} which represents a constant value.
	 * @param <T> Expression type
	 * @param value Constant value
	 * @return A new constant expression
	 */
	static <T> ConstantExpression<T, T> create(T value) {
		return new DefaultConstantExpression<>(value);
	}

	/**
	 * Create a {@link ConstantExpression} which represents a collection of constant values.
	 * @param <T> Expression type
	 * @param values Expression values (not null)
	 * @return A new collection expression
	 */
	static <T> ConstantExpression<Collection<T>, T> create(Collection<? extends T> values) {
		return new DefaultCollectionExpression<>(values);
	}

	/**
	 * Create a {@link ConstantExpression} which represents a collection of constant values.
	 * @param <T> Expression type
	 * @param values Expression values (not null)
	 * @return A new collection expression
	 */
	@SafeVarargs
	static <T> ConstantExpression<Collection<T>, T> create(T... values) {
		return new DefaultCollectionExpression<>(values);
	}

	/**
	 * Create a {@link ConstantExpression} which represents a <code>null</code> value.
	 * @return A new <code>null</code> value constant expression
	 */
	static ConstantExpression<Void, Void> nullValue() {
		return new DefaultConstantExpression<>(null);
	}

}
