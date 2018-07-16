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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.function.Consumer;

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.property.DefaultTemporalProperty;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.TemporalQueryExpression;

/**
 * A temporal type {@link PathProperty}.
 * <p>
 * Extends {@link TemporalQueryExpression} to provide temporal type related query expression builder methods.
 * </p>
 * 
 * @param <T> Property type
 * 
 * @since 5.1.0
 */
public interface TemporalProperty<T> extends PathProperty<T>, TemporalQueryExpression<T> {

	/**
	 * Clone this property.
	 * @param builder A property builder which can be used to perform additional property configuration
	 * @return The cloned property
	 */
	@Override
	TemporalProperty<T> clone(Consumer<Builder<T, PathProperty<T>, ?>> builder);

	/**
	 * Create a new {@link TemporalProperty} with given <code>name</code> and <code>type</code>.
	 * @param <T> Property (path) type
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 * @return {@link TemporalProperty} builder
	 */
	static <T> TemporalPropertyBuilder<T> create(String name, Class<? extends T> type) {
		return new DefaultTemporalProperty<>(name, type);
	}

	/**
	 * Create a new {@link TemporalProperty} from given <code>path</code>, using given {@link Path} <code>name</code>and
	 * <code>type</code>.
	 * @param <T> Path type
	 * @param path Path from which to obtain the property path name and type (not null)
	 * @return {@link TemporalProperty} builder
	 */
	static <T> TemporalPropertyBuilder<T> create(Path<T> path) {
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		TemporalPropertyBuilder<T> builder = create(path.getName(), path.getType());
		path.getParent().ifPresent(p -> builder.parent(p));
		return builder;
	}

	// Type specific builders

	/**
	 * Create a new {@link Date} type {@link TemporalProperty}.
	 * @param name Property name (not null)
	 * @return {@link TemporalProperty} builder
	 */
	static TemporalPropertyBuilder<Date> date(String name) {
		return create(name, Date.class);
	}

	/**
	 * Create a new {@link LocalDate} type {@link TemporalProperty}.
	 * @param name Property name (not null)
	 * @return {@link TemporalProperty} builder
	 */
	static TemporalPropertyBuilder<LocalDate> localDate(String name) {
		return create(name, LocalDate.class);
	}

	/**
	 * Create a new {@link localDateTime} type {@link TemporalProperty}.
	 * @param name Property name (not null)
	 * @return {@link TemporalProperty} builder
	 */
	static TemporalPropertyBuilder<LocalDateTime> localDateTime(String name) {
		return create(name, LocalDateTime.class);
	}

	/**
	 * Create a new {@link LocalTime} type {@link TemporalProperty}.
	 * @param name Property name (not null)
	 * @return {@link TemporalProperty} builder
	 */
	static TemporalPropertyBuilder<LocalTime> localTime(String name) {
		return create(name, LocalTime.class);
	}

	/**
	 * Create a new {@link OffsetDateTime} type {@link TemporalProperty}.
	 * @param name Property name (not null)
	 * @return {@link TemporalProperty} builder
	 */
	static TemporalPropertyBuilder<OffsetDateTime> offsetDateTime(String name) {
		return create(name, OffsetDateTime.class);
	}

	/**
	 * {@link TemporalProperty} builder.
	 * 
	 * @param <T> Property type
	 */
	public interface TemporalPropertyBuilder<T>
			extends Builder<T, TemporalProperty<T>, TemporalPropertyBuilder<T>>, TemporalProperty<T> {

	}

}
