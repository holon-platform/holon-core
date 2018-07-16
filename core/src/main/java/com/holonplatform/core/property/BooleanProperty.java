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

import java.util.function.Consumer;

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.property.DefaultBooleanProperty;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * A boolean type {@link PathProperty}.
 *
 * @since 5.1.0
 */
public interface BooleanProperty extends PathProperty<Boolean> {

	/**
	 * Clone this property.
	 * @param builder A property builder which can be used to perform additional property configuration
	 * @return The cloned property
	 */
	@Override
	BooleanProperty clone(Consumer<Builder<Boolean, PathProperty<Boolean>, ?>> builder);

	/**
	 * Create a new {@link BooleanProperty} with given <code>name</code>.
	 * @param name Property name (not null)
	 * @return {@link BooleanProperty} builder
	 */
	static BooleanPropertyBuilder create(String name) {
		return new DefaultBooleanProperty(name);
	}

	/**
	 * Create a new {@link BooleanProperty} with given <code>name</code>, using a default {@link PropertyValueConverter}
	 * to convert a numeric model data type into the boolean type.
	 * @param <N> Numeric model data type
	 * @param name Property name (not null)
	 * @param modelType Numeric model data type (not null)
	 * @return {@link BooleanProperty} builder
	 * @see PropertyValueConverter#numericBoolean(Class)
	 */
	static <N extends Number> BooleanPropertyBuilder create(String name, Class<N> modelType) {
		ObjectUtils.argumentNotNull(modelType, "Model type must be not null");
		return create(name).converter(PropertyValueConverter.numericBoolean(modelType));
	}

	/**
	 * Create a new {@link BooleanProperty} from given <code>path</code>, using given {@link Path} <code>name</code>.
	 * @param path Path from which to obtain the property path name (not null)
	 * @return {@link BooleanProperty} builder
	 */
	static BooleanPropertyBuilder create(Path<Boolean> path) {
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		BooleanPropertyBuilder builder = create(path.getName());
		path.getParent().ifPresent(p -> builder.parent(p));
		return builder;
	}

	/**
	 * {@link BooleanProperty} builder.
	 */
	public interface BooleanPropertyBuilder
			extends Builder<Boolean, BooleanProperty, BooleanPropertyBuilder>, BooleanProperty {

	}

}
