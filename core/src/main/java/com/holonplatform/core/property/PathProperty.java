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

import com.holonplatform.core.ConverterExpression;
import com.holonplatform.core.Path;
import com.holonplatform.core.internal.property.DefaultPathProperty;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.CloneableProperty.CloneablePathProperty;
import com.holonplatform.core.query.PathExpression;
import com.holonplatform.core.query.QueryExpression;
import com.holonplatform.core.query.QueryProjection;

/**
 * A {@link Property} bound to a {@link Path}, using {@link Path#getName()} as property name.
 * <p>
 * This property can be used as {@link QueryExpression} and {@link QueryProjection} through the {@link PathExpression}
 * super interface.
 * </p>
 * <p>
 * A {@link PathProperty} is a {@link ConverterExpression} too, using the {@link PropertyValueConverter} as expression
 * value converter, if available.
 * </p>
 * <p>
 * Type specific extensions are available to provide query expression convenience builder methods.
 * </p>
 * <p>
 * The {@link #create(String, Class)} and {@link #create(Path)} builder methods can be used to create and configure a
 * new {@link PathProperty} instance.
 * </p>
 * 
 * @param <T> Property value type
 * 
 * @since 5.0.0
 * 
 * @see StringProperty
 * @see NumericProperty
 * @see TemporalProperty
 * @see BooleanProperty
 * @see PropertyBoxProperty
 */
public interface PathProperty<T> extends CloneablePathProperty<T, PathProperty<T>>, PathExpression<T> {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#isReadOnly()
	 */
	@Override
	default boolean isReadOnly() {
		return false;
	}

	// Builders

	/**
	 * Create a new {@link PathProperty} with given <code>name</code> and <code>type</code>.
	 * @param <T> Property (path) type
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 * @return {@link PathProperty} builder
	 */
	static <T> PathPropertyBuilder<T> create(String name, Class<? extends T> type) {
		return new DefaultPathProperty<>(name, type);
	}

	/**
	 * Create a new {@link PathProperty} from given <code>path</code>, using given {@link Path} <code>name</code> and
	 * <code>type</code>.
	 * @param <T> Property (path) type
	 * @param path Path from which to obtain the property path name and type (not null)
	 * @return {@link PathProperty} builder
	 */
	static <T> PathPropertyBuilder<T> create(Path<T> path) {
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		PathPropertyBuilder<T> builder = create(path.getName(), path.getType());
		path.getParent().ifPresent(p -> builder.parent(p));
		return builder;
	}

	/**
	 * Base interface for {@link Path} and {@link Property} builders.
	 * @param <T> Property value type
	 * @param <P> Property type
	 * @param <B> Concrete builder type
	 */
	public interface Builder<T, P extends Path<T> & Property<T>, B extends Builder<T, P, B>>
			extends Path.Builder<T, B>, Property.Builder<T, P, B> {

	}

	/**
	 * {@link PathProperty} builder.
	 * @param <T> Property value type
	 */
	public interface PathPropertyBuilder<T>
			extends Builder<T, PathProperty<T>, PathPropertyBuilder<T>>, PathProperty<T> {

	}

}
