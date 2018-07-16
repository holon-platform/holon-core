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

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * A {@link Property} which handles a {@link Collection} of values.
 * <p>
 * The collection elements type is provided by the {@link #getElementType()} method.
 * </p>
 * 
 * @param <E> Collection elements type
 * @param <C> Concrete collection type
 *
 * @since 5.2.0
 */
public interface CollectionProperty<E, C extends Collection<E>> extends Property<C> {

	/**
	 * Get the collection elements type.
	 * @return collection elements type
	 */
	Class<? extends E> getElementType();

	/**
	 * A {@link CollectionProperty} which uses a {@link List} as concrete collection type.
	 *
	 * @param <T> List elements type
	 */
	public interface ListProperty<T> extends CollectionProperty<T, List<T>> {

	}

	/**
	 * A {@link CollectionProperty} which uses a {@link Set} as concrete collection type.
	 *
	 * @param <T> Set elements type
	 */
	public interface SetProperty<T> extends CollectionProperty<T, Set<T>> {

	}

	/**
	 * Base {@link CollectionProperty} builder.
	 *
	 * @param <E> Collection elements type
	 * @param <C> Concrete collection type
	 * @param <P> Concrete property type
	 * @param <B> Concrete builder type
	 */
	public interface Builder<E, C extends Collection<E>, P extends CollectionProperty<E, C>, B extends Builder<E, C, P, B>>
			extends Property.Builder<C, P, B> {

		/**
		 * Configure a {@link PropertyValueConverter} for the collection type property using the provided functions to
		 * perform the conversion operations for each collection element.
		 * @param <MODEL> Model element type
		 * @param modelElementType Model element type
		 * @param fromModel Function to convert a collection element from the model type to the collection property
		 *        element type
		 * @param toModel Function to convert a collection element from the collection property element type to model
		 *        type
		 * @return this
		 */
		<MODEL> B elementConverter(Class<MODEL> modelElementType, Function<MODEL, E> fromModel,
				Function<E, MODEL> toModel);

		/**
		 * Configure a property value converter for the collection type property using the provided
		 * {@link PropertyValueConverter} to perform the conversion operations for each collection element.
		 * @param <MODEL> Model element type
		 * @param elementConverter Collection element converter (not null)
		 * @return this
		 */
		<MODEL> B elementConverter(PropertyValueConverter<E, MODEL> elementConverter);

	}

}
