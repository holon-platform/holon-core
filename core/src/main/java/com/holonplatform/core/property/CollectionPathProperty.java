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

/**
 * A {@link CollectionProperty} type {@link PathProperty}.
 * 
 * @param <E> Collection element type
 * @param <C> Concrete collection type
 *
 * @since 5.2.0
 * 
 * @see ListPathProperty
 * @see SetPathProperty
 */
public interface CollectionPathProperty<E, C extends Collection<E>> extends CollectionProperty<E, C>, PathProperty<C> {

	/**
	 * Base {@link CollectionPathProperty} builder.
	 *
	 * @param <E> Collection elements type
	 * @param <C> Concrete collection type
	 * @param <P> Concrete property type
	 * @param <B> Concrete builder type
	 */
	public interface Builder<E, C extends Collection<E>, P extends CollectionPathProperty<E, C>, B extends Builder<E, C, P, B>>
			extends CollectionProperty.Builder<E, C, P, B>, PathProperty.Builder<C, P, B> {

	}

}
