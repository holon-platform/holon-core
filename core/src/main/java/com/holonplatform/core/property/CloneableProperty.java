/*
 * Copyright 2016-2018 Axioma srl.
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

/**
 * Declares a {@link Property} as cloneable.
 * 
 * @param <T> Property value type
 * @param <P> Actual property type
 *
 * @since 5.2.0
 */
public interface CloneableProperty<T, P extends Property<T>, B extends Property.Builder<T, P, ?>> extends Property<T> {

	/**
	 * Clone this property.
	 * @param builder A reference to the property builder which can be used to perform additional property configuration
	 * @return The cloned property instance
	 */
	P clone(Consumer<B> builder);

	/**
	 * A {@link Path} type {@link CloneableProperty}.
	 * 
	 * @param <T> Property value type
	 * @param <P> Actual property type
	 */
	public interface CloneablePathProperty<T, P extends Property<T> & Path<T>>
			extends CloneableProperty<T, P, PathProperty.Builder<T, P, ?>>, Path<T> {

	}

}
