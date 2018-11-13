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

import java.util.Arrays;

import com.holonplatform.core.Expression;
import com.holonplatform.core.Path;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.query.DefaultBeanProjection;
import com.holonplatform.core.internal.query.DefaultConstantExpression;
import com.holonplatform.core.internal.query.DefaultCountAllProjection;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;

/**
 * A query projection {@link Expression} to obtain typed query results.
 * 
 * @param <T> Projection result type
 * 
 * @since 5.0.0
 */
public interface QueryProjection<T> extends TypedExpression<T> {

	/**
	 * Create a {@link QueryProjection} for a constant value.
	 * @param <T> Expression type
	 * @param value Constant value
	 * @return A new constant expression projection
	 */
	static <T> QueryProjection<T> create(T value) {
		return new DefaultConstantExpression<>(value);
	}

	/**
	 * Create a {@link QueryProjection} for a {@link Path} expression.
	 * @param <T> Path type
	 * @param path The {@link Path} expression (not null)
	 * @return A new path expression projection
	 */
	static <T> QueryProjection<T> create(Path<T> path) {
		return PathExpression.from(path);
	}

	/**
	 * Create a {@link PropertyBox} type query projection using given <code>properties</code> as property set.
	 * @param properties The projection property set (not null)
	 * @return A new query projection using given <code>properties</code> as property set
	 */
	static QueryProjection<PropertyBox> propertySet(Property<?>... properties) {
		return propertySet(Arrays.asList(properties));
	}

	/**
	 * Create a {@link PropertyBox} type query projection using given <code>properties</code> as property set.
	 * @param <P> Property type
	 * @param properties The projection property set (not null)
	 * @return A new query projection using given <code>properties</code> as property set
	 */
	@SuppressWarnings("rawtypes")
	static <P extends Property> QueryProjection<PropertyBox> propertySet(Iterable<P> properties) {
		return PropertySetProjection.of(properties);
	}

	/**
	 * Create a {@link QueryProjection} using given bean class.
	 * @param <T> Bean type
	 * @param beanClass The bean class (not null)
	 * @param selection Optional selection paths. If not provided, all valid bean property paths will be used.
	 * @return A new bean projection
	 */
	static <T> QueryProjection<T> bean(Class<? extends T> beanClass, Path<?>... selection) {
		return new DefaultBeanProjection<>(beanClass, selection);
	}

	/**
	 * Create a query projection to count all the query results.
	 * @return A new <em>count all</em> query projection of {@link Long} type
	 */
	static QueryProjection<Long> countAll() {
		return new DefaultCountAllProjection();
	}

}
