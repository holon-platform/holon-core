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
package com.holonplatform.core.property;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.property.DefaultNumericProperty;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.NumericQueryExpression;

/**
 * A numeric type {@link PathProperty}.
 * <p>
 * Extends {@link NumericQueryExpression} to provide {@link Number} type related query expression builder methods.
 * </p>
 * 
 * @param <N> Property type
 *
 * @since 5.1.0
 */
public interface NumericProperty<N extends Number> extends PathProperty<N>, NumericQueryExpression<N> {

	/**
	 * Create a new {@link NumericProperty} with given <code>name</code> and <code>type</code>.
	 * @param <T> Property (path) type
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 * @return {@link NumericProperty} builder
	 */
	static <T extends Number> NumericPropertyBuilder<T> create(String name, Class<? extends T> type) {
		return new DefaultNumericProperty<>(name, type);
	}

	/**
	 * Create a new {@link NumericProperty} from given <code>path</code>, using given {@link Path} <code>name</code>and
	 * <code>type</code>.
	 * @param <T> Path type
	 * @param path Path from which to obtain the property path name and type (not null)
	 * @return {@link NumericProperty} builder
	 */
	static <T extends Number> NumericPropertyBuilder<T> create(Path<T> path) {
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		NumericPropertyBuilder<T> builder = create(path.getName(), path.getType());
		path.getParent().ifPresent(p -> builder.parent(p));
		return builder;
	}

	// Type specific builders

	/**
	 * Create a new {@link Integer} type {@link NumericProperty}.
	 * @param name Property name (not null)
	 * @return {@link NumericProperty} builder
	 */
	static NumericPropertyBuilder<Integer> integerType(String name) {
		return create(name, Integer.class);
	}

	/**
	 * Create a new {@link Long} type {@link NumericProperty}.
	 * @param name Property name (not null)
	 * @return {@link NumericProperty} builder
	 */
	static NumericPropertyBuilder<Long> longType(String name) {
		return create(name, Long.class);
	}

	/**
	 * Create a new {@link Double} type {@link NumericProperty}.
	 * @param name Property name (not null)
	 * @return {@link NumericProperty} builder
	 */
	static NumericPropertyBuilder<Double> doubleType(String name) {
		return create(name, Double.class);
	}

	/**
	 * Create a new {@link Float} type {@link NumericProperty}.
	 * @param name Property name (not null)
	 * @return {@link NumericProperty} builder
	 */
	static NumericPropertyBuilder<Float> floatType(String name) {
		return create(name, Float.class);
	}

	/**
	 * Create a new {@link Short} type {@link NumericProperty}.
	 * @param name Property name (not null)
	 * @return {@link NumericProperty} builder
	 */
	static NumericPropertyBuilder<Short> shortType(String name) {
		return create(name, Short.class);
	}

	/**
	 * Create a new {@link Byte} type {@link NumericProperty}.
	 * @param name Property name (not null)
	 * @return {@link NumericProperty} builder
	 */
	static NumericPropertyBuilder<Byte> byteType(String name) {
		return create(name, Byte.class);
	}

	/**
	 * Create a new {@link BigDecimal} type {@link NumericProperty}.
	 * @param name Property name (not null)
	 * @return {@link NumericProperty} builder
	 */
	static NumericPropertyBuilder<BigDecimal> bigDecimalType(String name) {
		return create(name, BigDecimal.class);
	}

	/**
	 * Create a new {@link BigInteger} type {@link NumericProperty}.
	 * @param name Property name (not null)
	 * @return {@link NumericProperty} builder
	 */
	static NumericPropertyBuilder<BigInteger> bigIntegerType(String name) {
		return create(name, BigInteger.class);
	}

	/**
	 * {@link NumericProperty} builder.
	 * 
	 * @param <N> Property type
	 */
	public interface NumericPropertyBuilder<N extends Number>
			extends Builder<N, NumericProperty<N>, NumericPropertyBuilder<N>>, NumericProperty<N> {

	}

}
